import React, { useState, useEffect } from "react"
import {useNavigate} from "react-router-dom";
import { Client } from '@stomp/stompjs';
import '../../static/css/match/Match.css';
import DiscardPhaseModal from "./modals/DiscardPhaseModal";
import ActionsModal from "./modals/ActionsModal";
import ChatBox from "./chatBox";
import { FaComments } from "react-icons/fa";
import FightModal from "./modals/FightModal";
import StartDiceModal from "./modals/StartDiceModal";
import StealCardModal from "./modals/StealCardModal";
import NpcLossDiscardModal from "./modals/NpcLossDiscardModal";
import EscapeDiceModal from "./modals/EscapeDiceModal";
import VotingModal from "./modals/VotingModal";

import { normalizeRoomId } from "./utils/roomUtils";
import { getPlayerColor } from "./utils/playersUtil";
import CurrentPlayerInfo from "./components/CurrentPlayerInfo";
import DeckSection from "./components/DeckSection";
import MatchBoardMap from "./components/MatchBoardMap";
import { getRandomFreeRoom } from "./utils/roomHelpers";
import { handlePlayerFight, handleNpcFight} from "./utils/fightHelpers";

// para alerta de errores
import { toast } from "react-toastify"

export default function PlayerMatch({ initialMatch, matchId, currentUser, jwt }){
    const navigate = useNavigate();
    const [currentPlayer, setCurrentPlayer] = useState({}) // el jugador asociado al usuario que está "viendo" la pantalla
    const [player, setPlayer] = useState([])
    const [playersList, setPlayersList] = useState([])
    const [match, setMatch] = useState(initialMatch) // Inicializamos con la prop recibida
    const [currentTurnUserId, setCurrentTurnUserId] = useState(initialMatch?.currentTurnUserId || null) 
    const [stompClient, setStompClient] = useState(null)

    // CARTAS
    const [deck, setDeck] = useState([])
    const [handCards, setHandCards] = useState([])
    const [bagCards, setBagCards] = useState([])
    const [otherPlayersBags, setOtherPlayersBags] = useState({}) 
    const [numCardsDrawn, setNumCardsDrawn] = useState(0)
    const [discardPhaseOpen, setDiscardPhaseOpen] = useState(false)

    const [chatOpen, setChatOpen] = useState(false)
    const [actionPoints, setActionPoints] = useState(0)
    const [strength, setStrength] = useState(1)
    const [moveToAdyacentRoom, setMoveToAdyacentRoom] = useState(false)
    const [moveToRoomWithWord, setMoveToRoomWithWord] = useState(false)
    const [isEndingTurn, setIsEndingTurn] = useState(false)
    const [moveNpcMode, setMoveNpcMode] = useState(false)
    const [selectedNpcId, setSelectedNpcId] = useState(null)
    const [selectedNpcIndex, setSelectedNpcIndex] = useState(null)

    const [isDiceModalOpen, setIsDiceModalOpen] = useState(true);
    const [isActionsModalOpen, setIsActionsModalOpen] = useState(false);
    const [isEscapeModalOpen, setIsEscapeModalOpen] = useState(false);
    const [isFightModalOpen, setIsFightModalOpen] = useState(false);
    const [fightDefender, setFightDefender] = useState(null);
    const [fightAttacker, setFightAttacker] = useState(null);
    const [pendingTargetRoom, setPendingTargetRoom] = useState(null);
    const [isStealModalOpen, setIsStealModalOpen] = useState(false);
    const [stealLoserPlayerId, setStealLoserPlayerId] = useState(null);
    const [isNpcLossModalOpen, setIsNpcLossModalOpen] = useState(false);
    const [npcLossModalTitle, setNpcLossModalTitle] = useState();
    const [npcLossModalSubtitle, setNpcLossModalSubtitle] = useState();
    const [isVotingModalOpen, setIsVotingModalOpen] = useState(false);
    const [weaponProposed, setWeaponProposed] = useState('');
    const [proposingUserId, setProposingUserId] = useState(null);
    const [proposingUsername, setProposingUsername] = useState('');
    const [votingResult, setVotingResult] = useState(null);
    

    // CARGAR DATOS JUGADORES 
    useEffect(() => {
        fetchMatchAndPlayers()
    }, [matchId])
    
    useEffect(() => {
        const client = new Client({
            brokerURL: 'ws://localhost:8080/ws',
            connectHeaders: { 'Authorization': `Bearer ${jwt}` },
            onConnect: () => setStompClient(client)
        });

        client.activate();
        return () => client.active && client.deactivate();
    }, [jwt]);

    useEffect(() => {
        if (!stompClient || !stompClient.active) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.turn`, (msg) => {
            const turnUpdate = JSON.parse(msg.body);
            setCurrentTurnUserId(turnUpdate.currentTurnUserId);
            fetchMatchAndPlayers();
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId]);

    useEffect(() => {
        if (!stompClient || !stompClient.active) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.fight`, async (msg) => {
            const fightUpdate = JSON.parse(msg.body);
            
            if (fightUpdate.action === 'START') {
                try {
                    const response = await fetch(`/api/v1/matches/${matchId}`, {
                        headers: { Authorization: `Bearer ${jwt}` },
                    });
                    
                    if (response.ok) {
                        const freshMatch = await response.json();
                        setMatch(freshMatch);
                        
                        const attacker = freshMatch.players?.find(p => p.user.id === fightUpdate.attackerId);
                        let defender = null;
                        
                        if (fightUpdate.isBot) {
                            defender = freshMatch.npcs?.find(n => n.id === fightUpdate.defenderId);
                        } else {
                            defender = freshMatch.players?.find(p => p.user.id === fightUpdate.defenderId);
                        }
                        
                        if (attacker && defender) {
                            setFightAttacker(attacker);
                            setFightDefender(defender);
                            setPendingTargetRoom(fightUpdate.roomId || fightUpdate.roomName);
                            setIsFightModalOpen(true);
                        }
                    }
                } catch (error) {
                    console.error('Error fetching fresh match data for fight:', error);
                }
            } else if (fightUpdate.action === 'RESOLVE') {
                const winnerId = fightUpdate.winnerId;
                const winnerPlayerId = fightUpdate.winnerPlayerId;
                const loserPlayerId = fightUpdate.loserPlayerId;

                if (currentUser.id === winnerId && winnerPlayerId && loserPlayerId) {
                    setStealLoserPlayerId(loserPlayerId);
                    setIsStealModalOpen(true);
                }
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, jwt, currentUser]);

    useEffect(() => {
        if (!stompClient || !stompClient.active) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.location`, (msg) => {
            const locationUpdate = JSON.parse(msg.body);
            
            setMatch(prevMatch => {
                if (!prevMatch || !prevMatch.players) return prevMatch;
                return {
                    ...prevMatch,
                    players: prevMatch.players.map(p => {
                        if (p.id === locationUpdate.playerId) {
                            return { ...p, currentRoom: locationUpdate.newRoom };
                        }
                        return p;
                    })
                };
            });
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId]);

    useEffect(() => {
        if (!stompClient || !stompClient.active) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.weapon.voting`, (msg) => {
            const votingData = JSON.parse(msg.body);
            setIsVotingModalOpen(true);
            setWeaponProposed(votingData.weapon);
            setProposingUserId(votingData.proposingUserId);
            setProposingUsername(votingData.proposingUsername);
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId]);

    useEffect(() => {
        if (!stompClient || !stompClient.active) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.weapon.voting.result`, (msg) => {
            const result = JSON.parse(msg.body);
            if (result.status === 'FINISHED') {
                setVotingResult(result);
                setIsVotingModalOpen(false);
                
                if (result.result === 'ACCEPTED') {
                    toast.success(`"${result.proposedWeapon}" was accepted! Bonus: +${result.finalBonus}`);
                } else {
                    toast.error(`"${result.proposedWeapon}" was rejected. No bonus awarded.`);
                }
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId]);

    useEffect(() => {
        if (!stompClient || !stompClient.active) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.npc.location`, (msg) => {
            const locationUpdate = JSON.parse(msg.body);
            
            setMatch(prevMatch => {
                if (!prevMatch || !prevMatch.npcs) return prevMatch;
                return {
                    ...prevMatch,
                    npcs: prevMatch.npcs.map(npc => {
                        if (npc.id === locationUpdate.npcId) {
                            return { ...npc, room: locationUpdate.newRoom };
                        }
                        return npc;
                    })
                };
            });
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId]);

    useEffect(() => {
        if (!stompClient || !stompClient.active) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.cards`, (msg) => {
            const cardsUpdate = JSON.parse(msg.body);
            const { winner, loser } = cardsUpdate || {};

            const applyCardsUpdate = (info) => {
                if (!info || !info.playerId) return;
                const hand = Array.isArray(info.hand?.cards) ? info.hand.cards : [];
                const bag = Array.isArray(info.bag?.cards) ? info.bag.cards : [];
                const deckInfo = info.deck;

                if (currentPlayer?.id === info.playerId) {
                    setHandCards(hand.map(c => ({...c})));
                    setBagCards(bag.map(c => ({...c})));
                } else {
                    setOtherPlayersBags(prev => ({
                        ...prev,
                        [info.playerId]: bag
                    }));
                }

                if (deckInfo) {
                    setDeck(deckInfo);
                }
            };

            if (winner) applyCardsUpdate(winner);
            if (loser) applyCardsUpdate(loser);
            if (!winner && !loser && cardsUpdate.playerId) {
                applyCardsUpdate(cardsUpdate);
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, currentPlayer]);

    useEffect(() => {
        if (!stompClient || !stompClient.active || !currentPlayer) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.actionPoints`, (msg) => {
            const actionPointsUpdate = JSON.parse(msg.body);
            if (actionPointsUpdate.userId === currentPlayer.user?.id) {
                setActionPoints(actionPointsUpdate.actionPoints);
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, currentPlayer]);

    useEffect(() => {
        if (!stompClient || !stompClient.active || !currentPlayer) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.hand.${currentPlayer.id}`, (msg) => {
            const handUpdate = JSON.parse(msg.body);
            if (handUpdate && handUpdate.hand) {
                const updatedHand = Array.isArray(handUpdate.hand.cards) ? handUpdate.hand.cards : [];
                setHandCards(updatedHand);
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, currentPlayer]);

    useEffect(() => {
        if (!stompClient || !stompClient.active || !currentPlayer) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.strength`, (msg) => {
            const strengthUpdate = JSON.parse(msg.body);
            if (strengthUpdate.userId === currentPlayer.user?.id) {
                setStrength(Math.min(6, strengthUpdate.strength));
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, currentPlayer]);

    useEffect(() => {
        if (player && Array.isArray(player)){
            setPlayersList(player.filter(p => p.user.id !== currentUser?.id))
            const me = player.find(p => p.user.id === currentUser?.id);
            setCurrentPlayer(me || {});
        }
    }, [match]);

    useEffect(() => {
        if (currentPlayer && currentPlayer.id){
            fetchCards()
        }
        if (currentPlayerTurn) { 
            setStrength(Math.min(6, currentPlayer.strength)) 
        }
        setNumCardsDrawn(0)
        
        if (currentTurnUserId && currentPlayer.user?.id === currentTurnUserId){ 
            fetchActionPoints() 
        }
    }, [currentTurnUserId]);

    useEffect(() => {
        if (playersList.length > 0) {
            fetchOtherPlayersBags()
        }
    }, [playersList]);

    useEffect(() => {
        calculateActionPoints()
    }, [handCards]);

    useEffect(() => {
        if (!currentPlayer || !currentPlayer.hand || !currentPlayer.bag) return;
        setHandCards(currentPlayer.hand.cards || []);
        setBagCards(currentPlayer.bag.cards || []);
    }, [currentPlayer]);

    useEffect(() => {
        if (!isDiceModalOpen || match?.currentTurnPhase !== null) return;

        const fetchMatchUpdate = async () => {
            try {
                const response = await fetch(`/api/v1/matches/${matchId}`, {
                    headers: { 'Authorization': `Bearer ${jwt}` }
                });
                if (response.ok) {
                    const updatedMatch = await response.json();
                    setMatch(updatedMatch);
                }
            } catch (error) {
                console.error("Error al actualizar match:", error);
            }
        };

        const intervalId = setInterval(fetchMatchUpdate, 2000);
        return () => clearInterval(intervalId);
    }, [isDiceModalOpen, match?.currentTurnPhase, matchId]);

    const cleanVotingStates = async () => {
        setVotingResult(null);
        setIsVotingModalOpen(false);
        setWeaponProposed('');
        setProposingUserId(null);
        setProposingUsername('');
        
        try {
            await fetch(`/api/v1/voting/${matchId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${jwt}`,
                    'Content-Type': 'application/json'
                }
            });
        } catch (error) {
            console.error('Error al eliminar votaciones:', error);
        }
    };

    const fetchActionPoints = async () => {
        try {
            const response = await fetch(`/api/v1/matches/${matchId}/${currentPlayer.id}/actionPoints`, {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: 'application/json',
                    'Content-Type': 'application/json',
                }
            })
            if (response.ok){
                const data = await response.json()
                setActionPoints(data)
            }
        } catch (error) {
            console.error('error trayendo los puntos de acción del jugador', error)
        }
    }

    const fetchMatchAndPlayers = async () => {
        try {
            const response = await fetch(`/api/v1/matches/${matchId}`, {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: 'application/json',
                    'Content-Type': 'application/json',
                }
            })
            const data = await response.json()

            if (data.players) {
                data.players = data.players.map(p => ({
                    ...p,
                    currentRoom: p.currentRoom ? { ...p.currentRoom, id: normalizeRoomId(p.currentRoom.id) } : null,
                    room: p.room ? { ...p.room, id: normalizeRoomId(p.room.id) } : null,
                    roomId: p.roomId ? normalizeRoomId(p.roomId) : null
                }));
            }

            if (data.npcs) {
                data.npcs = data.npcs.map(npc => ({
                    ...npc,
                    room: npc.room ? { ...npc.room, id: normalizeRoomId(npc.room.id) } : null
                }));
            }

            setMatch(data)
            setPlayer(data.players)
            setDeck(data.deck)
            const me = data.players.find(p => p.user.id === currentUser.id);
            if (me) {
                setActionPoints(me.actionPoints)
                setStrength(me.strength)
            }
        } catch (error) {
            console.error(error);
        }
    }

    const movePlayerToRoom = async (userId, roomId) => {
        try {
            const response = await fetch(`/api/v1/matches/${matchId}/move`, {
                method: "PUT",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ userId, roomId })
            });

            if (!response.ok) throw new Error("Move failed");

            const data = await response.json();
            updatePlayerData(data);
                
            const movedPlayer = data.players.find(p => p.user.id === currentUser?.id);
            if (movedPlayer && userId === currentUser?.id) {
                setActionPoints(movedPlayer.actionPoints);
            }
            return data;
        } catch (err) {
            console.error('Error moving player:', err);
            return null;
        }
    }

    const fetchCards = async () => {
        try {
            const response = await fetch(`/api/v1/matches/${matchId}/${currentPlayer.id}/getAllCards`, {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: 'application/json',
                    'Content-Type': 'application/json',
                },
            })

            if (response.ok){
                const data = await response.json()
                setHandCards(Array.isArray(data.hand.cards) ? data.hand.cards : [])
                setBagCards(Array.isArray(data.bag.cards) ? data.bag.cards : [])
                setDeck(data.deck || [])
                return data
            } 
        } catch (error) {
            console.log('error', error)
            toast.error(error);
        }
    }

    const fetchOtherPlayersBags = async () => {
        try {
            const bags = {}
            for (const player of playersList) {
                const response = await fetch(`/api/v1/matches/${matchId}/${player.id}/getAllCards`, {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                        Accept: 'application/json',
                        'Content-Type': 'application/json',
                    },
                })
                if (response.ok) {
                    const data = await response.json()
                    bags[player.id] = Array.isArray(data.bag.cards) ? data.bag.cards : []
                }
            }
            setOtherPlayersBags(bags)
        } catch (error) {
            console.log('Error fetching other players bags:', error)
        }
    }

    const drawCard = async () => { 
        try {
            const response = await fetch(`/api/v1/matches/${matchId}/${currentPlayer.id}/drawCardFromDeck`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: 'application/json',
                    'Content-Type': 'application/json',
                },
            })

            if (!response.ok) {
                const error = await response.json();
                throw error;
            }

            const data = await response.json()
            setDeck(data.deck)
            setNumCardsDrawn(prev => prev + 1)
            
        } catch (error) {
            toast.error(error.message)
        }    
    }

    const drawCardForWinner = async (winnerId) => {
        try {
            const response = await fetch(`/api/v1/matches/${matchId}/${winnerId}/drawRewardCard`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: 'application/json',
                    'Content-Type': 'application/json',
                },
            })

            if (!response.ok) throw new Error("Error rewarding card");

            const data = await response.json()
            if (winnerId === currentPlayer?.id) {
                setDeck(data.deck)
                setHandCards(prev => [...prev, data.card])
            }
            return true
        } catch (error) {
            console.log('Error al robar carta para el ganador:', error)
            return false
        }
    }

    const getPlayerInRoom = (roomId) => {
        const targetRoomNormalized = normalizeRoomId(roomId);
        return match?.players?.find(p => {
            const playerRoomId = p.currentRoom?.id || p.roomId || p.room?.id;
            return (
                p.user?.id !== currentUser?.id &&
                normalizeRoomId(playerRoomId) === targetRoomNormalized
            );
        });
    };

    const getNpcInRoom = (roomId) => {
        const targetRoomNormalized = normalizeRoomId(roomId);
        return match?.npcs?.find(npc => {
            const npcRoomId = npc.room?.id;
            return (
                npcRoomId &&
                normalizeRoomId(npcRoomId) === targetRoomNormalized
            );
        });
    };

    const updatePlayerData = (data) => {
        setMatch(data);
        if (!data.players) return;
        setPlayer(data.players);

        const me = data.players.find(p => p.user.id === currentUser.id);
        if (me) {
            setCurrentPlayer(me);
            setPlayersList(data.players.filter(p => p.user.id !== currentUser.id));
            setActionPoints(me.actionPoints ?? actionPoints);
            setStrength(Math.min(6, me.strength ?? strength));
        }
    };

    const notifyFight = async ({ attackerId, attackerUsername, defenderId, defenderUsername, roomId, isBot = false }) => {
        await fetch(`/api/v1/matches/${matchId}/notify-fight`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${jwt}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ matchId, attackerId, attackerUsername, defenderId, defenderUsername, roomId, action: "START", isBot })
        });
    };

    const notifyActionPoints = async (userId, actionPoints) => {
        try {
            await fetch(`/api/v1/matches/${matchId}/notify-action-points`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ matchId, userId, actionPoints })
            });

            if (userId === currentUser.id) {
                setActionPoints(actionPoints);
            }
        } catch (err) {
            console.error("Error notifying action points:", err);
        }
    };

    const handleNpcMove = async (roomId) => {
        if (selectedNpcIndex === null) {
            alert('Selecciona primero un NPC en el mapa');
            return;
        }

        const npc = match?.npcs?.[selectedNpcIndex];
        const npcIdToSend = npc?.id ?? null;

        if (!npcIdToSend) {
            alert('No se puede mover: el NPC no tiene identificador.');
            setSelectedNpcIndex(null);
            setSelectedNpcId(null);
            setMoveNpcMode(false);
            return;
        }

        try {
            const response = await fetch(`/api/v1/matches/${matchId}/moveNpc`, {
                method: 'PUT',
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ userId: currentUser.id, roomId, npcId: npcIdToSend })
            });

            if (!response.ok) return;

            const data = await response.json();
            updatePlayerData(data);

            setSelectedNpcIndex(null);
            setSelectedNpcId(null);
            setMoveNpcMode(false);

            const targetRoomNormalized = normalizeRoomId(roomId);
            const playerInRoom = data.players.find(p => {
                const playerRoomId = p.currentRoom?.id || p.roomId || p.room?.id;
                return normalizeRoomId(playerRoomId) === targetRoomNormalized;
            });

            if (playerInRoom && roomId !== 37) {
                const movedNpc = data.npcs.find(n => n.id === npcIdToSend);
                if (movedNpc) {
                    await notifyFight({
                        attackerId: playerInRoom.user.id,
                        attackerUsername: playerInRoom.user.username,
                        defenderId: movedNpc.id,
                        defenderUsername: movedNpc.isNiallCampbell ? "Niall Campbell" : "NPC",
                        roomId,
                        isBot: true
                    });
                }
            }
        } catch (err) {
            console.error(err);
        } finally {
            setSelectedNpcIndex(null);
            setSelectedNpcId(null);
            setMoveNpcMode(false);
        }
    };

    const performPlayerMove = async (roomId, endpoint, resetMoveMode) => {
        try {
            const isSafeArea = roomId === 37;
            const otherPlayer = getPlayerInRoom(roomId);

            if (otherPlayer && !isSafeArea) {
                setPendingTargetRoom(roomId);
                setFightDefender(otherPlayer);
                setIsFightModalOpen(true);
                resetMoveMode(false);

                await notifyFight({
                    attackerId: currentUser.id,
                    attackerUsername: currentUser.username,
                    defenderId: otherPlayer.user.id,
                    defenderUsername: otherPlayer.user.username,
                    roomId
                });
                return;
            }

            const botInRoom = getNpcInRoom(roomId);

            if (botInRoom && !isSafeArea) {
                setPendingTargetRoom(roomId);
                setFightDefender(botInRoom);
                setFightAttacker(currentPlayer);
                setIsFightModalOpen(true);
                resetMoveMode(false);

                try {
                    const consumeResponse = await fetch(
                        `/api/v1/matches/${matchId}/consume-action-point/${currentUser.id}`,
                        {
                            method: "POST",
                            headers: {
                                Authorization: `Bearer ${jwt}`,
                                "Content-Type": "application/json",
                            },
                        }
                    );

                    if (consumeResponse.ok) {
                        setActionPoints(prev => Math.max(0, prev - 1));
                    }
                } catch (err) {
                    console.error(err);
                }

                await notifyFight({
                    attackerId: currentUser.id,
                    attackerUsername: currentUser.username,
                    defenderId: botInRoom.id,
                    defenderUsername: `Bot ${botInRoom.id}`,
                    roomId,
                    isBot: true
                });
                return;
            }

            const response = await fetch(
                `/api/v1/matches/${matchId}/${endpoint}`,
                {
                    method: "PUT",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                        Accept: "application/json",
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ userId: currentUser.id, roomId })
                }
            );

            if (response.ok) {
                const data = await response.json();
                updatePlayerData(data);

                const movedPlayer = data.players.find(p => p.user.id === currentUser.id);
                if (movedPlayer) {
                    await notifyActionPoints(currentUser.id, movedPlayer.actionPoints);
                }
                resetMoveMode(false);
            } else {
                resetMoveMode(false);
                toast.error(response.statusText);
            }
        } catch (error) {
            console.error(error);
        }
    };

    const handleWordMove = async (roomId) => {
        return performPlayerMove(roomId, "moveByLetters", setMoveToRoomWithWord);
    };

    const handleAdjacentMove = async (roomId) => {
        return performPlayerMove(roomId, "move", setMoveToAdyacentRoom);
    };
     
    const move = async (roomId) => {
        if (moveNpcMode) return handleNpcMove(roomId);
        if (moveToRoomWithWord === true) return handleWordMove(roomId);
        if (moveToAdyacentRoom === false) return;
        if (moveToAdyacentRoom === true) return handleAdjacentMove(roomId);
    }

    const moveLoserToRandomRoom = async (userId, roomId) => {
        try {
            const response = await fetch(`/api/v1/matches/${matchId}/moveLoser`, {
                method: "PUT",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ userId, roomId })
            });

            if (!response.ok) throw new Error("Move loser failed");

            const data = await response.json();
            updatePlayerData(data);
                
            const movedPlayer = data.players.find(p => p.user.id === userId);
            if (movedPlayer) {
                await notifyActionPoints(userId, movedPlayer.actionPoints);
            }
            return data;
        } catch (err) {
            console.error('Error moving player:', err);
            return null;
        }
    }

    const handleNpcLossDiscard = async ({ cardId, fromWhere }) => {
        if (!cardId || !fromWhere || !currentPlayer?.id) {
            setIsNpcLossModalOpen(false);
            return;
        }

        try {
            const res = await fetch(`/api/v1/matches/${matchId}/${currentPlayer.id}/lose-against-npc`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${jwt}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ cardId, fromWhere })
            });

            if (!res.ok) {
                alert('No se pudo descartar la carta tras perder contra el NPC.');
            } else {
                await fetchCards();
                await fetchOtherPlayersBags();
            }
        } catch (error) {
            console.error('Error discarding card after NPC loss:', error);
        } finally {
            setIsNpcLossModalOpen(false);
        }
    }

    const endMatch = () => {
        if (!window.confirm("¿Are you sure you want to end the match?")) return; 
        fetch(`/api/v1/matches/${matchId}/end`, {
            method: "PUT",
            headers: {
                Authorization: `Bearer ${jwt}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(10)
        })
        .then(res => res.json())
        .then(updated => {
            setMatch(updated)
        })
        .catch(err => console.error(err))
    }

    const handleEndTurn = async () => {
        if (isEndingTurn) return;
        setIsEndingTurn(true);
        setNumCardsDrawn(0);
        try {
            const response = await fetch(`/api/v1/matches/${matchId}/next-turn`, {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message);
            }

            await fetchMatchAndPlayers();
        } catch (error) {
            toast.error(error.message)
        } finally {
            setIsEndingTurn(false);
        }
    };

    const leaveMatch = async () => {
        if (!window.confirm("¿Seguro que quieres abandonar la partida?")) return;

        try {
            const response = await fetch(`/api/v1/matches/${matchId}/leaveMatch`, {
                method: 'PUT',
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(currentUser?.id)
            });

            if (!response.ok) {
                alert('No se pudo abandonar la partida');
                return;
            }

            navigate('/');
        } catch (err) {
            console.error('Error leaving match:', err);
        }
    };

    const currentPlayerTurn = match?.players?.find(p => p.user.id === match.currentTurnUserId);

    const canDraw = match?.currentTurnUserId === currentUser?.id &&
                match?.currentTurnPhase === "DRAW" &&
                numCardsDrawn < 7;

    const calculateActionPoints = () => {
        if (!match) return;
        if (match.currentTurnPhase !== "DRAW") return;
        if (handCards.length > 7 ){
            setActionPoints(0)
        } else {
            setActionPoints(7-handCards.length)
        }
    }

    const handleDiceRolled = (updatedMatch) => {
        setMatch(updatedMatch);
        setCurrentTurnUserId(updatedMatch.currentTurnUserId);
        if (updatedMatch.players) {
            setPlayer(updatedMatch.players);
        }
        fetchCards()
    };

    if (match?.status === "FINISHED") {
        return (
            <div className="match-ended">
                <div className="end-overlay">
                    <div className="end-text-box">
                        <h2>The match has ended</h2>
                        {match?.winner?.user ? (
                            <p style={{ fontWeight: 700, margin: '8px 0' }}>Winner: {match.winner.user.username}</p>
                        ) : null}
                        <p>Thanks for playing.</p>
                        <button className="return-menu-button" onClick={() => navigate(`/`)}>Return to main menu</button>
                    </div>
                </div>
            </div>
        );
    }

    if (!match) {
        return <div>Cargando partida...</div>;
    }

    const clearFightState = async () => {
        setIsFightModalOpen(false);
        setFightDefender(null);
        setFightAttacker(null);
        setPendingTargetRoom(null);
        await cleanVotingStates();
    };

    const handleFightResult = async (currentUserWon) => {
        try {
            const isCurrentAttacker = currentUser.id === fightAttacker?.user?.id;
           
            if (!isCurrentAttacker) {
                await clearFightState();
                return;
            }
            const defenderRoomId = fightDefender?.currentRoom?.id || fightDefender?.roomId || fightDefender?.room?.id || pendingTargetRoom;
            const attackerWins = currentUserWon;
            const isDefenderNPC = !fightDefender?.user;
            const loserUser = attackerWins ? fightDefender?.user : fightAttacker?.user;

            if (isDefenderNPC) {
                await handleNpcFight({
                    attackerWins, fightAttacker, fightDefender, match, defenderRoomId, currentPlayer,
                    handCards, bagCards, drawCardForWinner, movePlayerToRoom, moveLoserToRandomRoom,
                    getRandomFreeRoom, fetchMatchAndPlayers, setDeck, setIsNpcLossModalOpen, matchId, jwt,
                });
                return; 
            }
                
            if (!loserUser) return;

            await handlePlayerFight({
                attackerWins, fightAttacker, fightDefender, match, currentUser, moveLoserToRandomRoom,
                movePlayerToRoom, drawCardForWinner, getRandomFreeRoom, normalizeRoomId, defenderRoomId,
                fetchConsumeAllActionPoints: async (userId) => {
                    await fetch(`/api/v1/matches/${matchId}/consume-all-action-points/${userId}`, {
                        method: "POST", headers: { Authorization: `Bearer ${jwt}`, "Content-Type": "application/json" },
                    });
                },
                notifyFightResolved: async (winnerId, winnerPlayerId, loserPlayerId) => {
                    await fetch(`/api/v1/matches/${matchId}/notify-fight-resolved`, {
                        method: "POST", headers: { Authorization: `Bearer ${jwt}`, "Content-Type": "application/json" },
                        body: JSON.stringify({ matchId, winnerId, winnerPlayerId, loserPlayerId }),
                    });
                },
                setStealLoserPlayerId, setIsStealModalOpen,
            });      
        } finally {
            await clearFightState();    
        }
    }

    return (
        <div className="match-container">
            <StartDiceModal
                isOpen={isDiceModalOpen && (match?.turnNumber === 0 || match?.currentTurnPhase === null)}
                onClose={() => setIsDiceModalOpen(false)}
                onDiceRolled={handleDiceRolled}
                matchData={match}
            />
            <EscapeDiceModal
                isOpen={isEscapeModalOpen}
                onClose={() => setIsEscapeModalOpen(false)}
                onResult={async (result) => {
                    try {
                        await fetchMatchAndPlayers();
                        if (!result.success && result.discardRequired) {
                            setNpcLossModalTitle('Tu intento de escape ha fallado');
                            setNpcLossModalSubtitle('Elige de donde descartar una carta');
                            setIsNpcLossModalOpen(true);
                        }
                    } catch (err) {
                        console.error('Error handling escape result:', err);
                    }
                }}
            />

            <div className="match-board" style={{ position: 'relative' }}>
                <div className="player-and-decks-section">
                    <div className="current-player">
                        <CurrentPlayerInfo
                            currentPlayer={currentPlayer}
                            actionPoints={actionPoints}
                            strength={strength}
                            getPlayerColor={getPlayerColor}
                            players={match?.players}
                        />
                    </div>

                    <DeckSection
                        numCardsDrawn={numCardsDrawn}
                        drawCard={drawCard}
                        canDraw={canDraw}
                        deck={deck}
                        match={match}
                        currentUser={currentUser}
                    />
                </div>

                <MatchBoardMap
                    match={match}
                    currentUser={currentUser}
                    move={move}
                    moveNpcMode={moveNpcMode}
                    selectedNpcIndex={selectedNpcIndex}
                    selectedNpcId={selectedNpcId}
                    setSelectedNpcIndex={setSelectedNpcIndex}
                    setSelectedNpcId={setSelectedNpcId}
                    playersList={playersList}
                    otherPlayersBags={otherPlayersBags}
                />
            </div>

            {moveNpcMode && match?.currentTurnUserId === currentUser?.id && (
                <div style={{ width: '100%', display: 'flex', justifyContent: 'center', marginTop: '40px', marginBottom: '60px', zIndex: 30 }}>
                    <div style={{ padding: '14px 42px', background: '#d200005e', color: '#fff', borderRadius: '10px', fontWeight: '700', fontSize: '20px', minWidth: '280px', textAlign: 'center', transform: 'translateX(20px)' }}>
                        {selectedNpcIndex === null ? 'Select the NPC you want to move' : 'Select the room'}
                    </div>
                </div>
            )}

            {match?.currentTurnUserId === currentUser?.id && (
                <button
                    className="end-turn-button"
                    onClick={handleEndTurn}
                    disabled={isEndingTurn}
                >
                    End your turn
                </button>
            )}

            <div className="player-section">
                <div className="cards-section">
                    <div className="player-hand">
                        <div className="hand-cards">
                            {Array.isArray(handCards) && handCards.map((carta) => (
                                <div key={carta.id}>
                                    <img src={`/resources${carta.frontImage}`} alt={`Carta ${carta.letter}`} className="card" />
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className="player-bag">
                        <div className="bag-cards">
                            {Array.isArray(bagCards) && bagCards.map((carta) => (
                                <div key={carta.id}>
                                    <img src={`/resources${carta.frontImage}`} alt={`Carta ${carta.letter}`} className="card" />
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
                <div className="buttons-section">
                    <button className="leave-match-button"
                        onClick={() => setDiscardPhaseOpen(true)}
                        disabled={match.currentTurnUserId !== currentUser.id}
                        title="Discard cards from hand"
                    >
                        Discard and bag
                    </button>
                    <button className="leave-match-button"
                        title="Discard cards from hand"
                        onClick={() => setIsActionsModalOpen(true)}
                        disabled={match.currentTurnUserId !== currentUser.id || actionPoints === 0}
                    >
                        Actions
                    </button>
                    <ActionsModal
                        isOpen={isActionsModalOpen}
                        onClose={() => setIsActionsModalOpen(false)}
                        moveToAdyacent={() => setMoveToAdyacentRoom(true)}
                        moveToRoomWithWord={() => setMoveToRoomWithWord(true)}
                        onMoveNpcRequested={() => { setMoveNpcMode(true); setSelectedNpcId(null); setSelectedNpcIndex(null); }}
                        onAttemptEscape={() => { setIsEscapeModalOpen(true); }}
                        canAttemptEscape={[1, 6, 31, 36].includes(normalizeRoomId(currentPlayer && (currentPlayer.roomId ?? currentPlayer.room?.id ?? currentPlayer.currentRoom?.id)))}
                    />
                    <button
                        className="leave-match-button"
                        onClick={leaveMatch}
                        style={{ marginLeft: '15px', background: '#e74c3c', color: 'white', border: 'none', padding: '8px 12px', borderRadius: '6px', cursor: 'pointer' }}
                    >
                        Leave Match
                    </button>
                    <button
                        className="end-match-button"
                        onClick={endMatch}
                        style={{ display: ( match?.creatorId !== currentUser?.id) ? 'none' : 'block' }}
                    >
                        End match
                    </button>
                    <div className="match-chat-icon ">
                        <div className="match-chat-icon-button" onClick={() => setChatOpen(!chatOpen)}>
                            <FaComments size={30} color="white" />
                        </div>
                    </div>
                    {chatOpen && <ChatBox matchId={matchId} />}
                </div>
            </div>

            <FightModal
                isOpen={isFightModalOpen}
                onClose={() => {
                    setIsFightModalOpen(false);
                    setFightDefender(null);
                    setFightAttacker(null);
                    cleanVotingStates();
                }}
                defender={fightDefender}
                attacker={fightAttacker}
                stompClient={stompClient}
                bagCards={bagCards}
                matchData={match}
                votingResult={votingResult}
                proposingUserId={proposingUserId}
                onVotingResultProcessed={() => setVotingResult(null)}
                onResolve={async (currentUserWon) => { handleFightResult(currentUserWon); }}
            />

            <DiscardPhaseModal
                isVisible={discardPhaseOpen}
                hand={handCards}
                bag={bagCards}
                deck={deck}
                player={currentPlayer}
                onClose={() => setDiscardPhaseOpen(false)}
                updateCurrentTurnId={(newTurnId) => setCurrentTurnUserId(newTurnId)}
                onSave={async () => {
                    await fetchCards();
                    setDiscardPhaseOpen(false);
                }}
            />

            <StealCardModal
                isOpen={isStealModalOpen}
                loserId={stealLoserPlayerId}
                matchId={matchId}
                winnerId={currentPlayer?.id}
                onClose={() => {
                    setIsStealModalOpen(false);
                    setStealLoserPlayerId(null);
                }}
                onSteal={async () => {
                    await fetchCards();
                    setIsStealModalOpen(false);
                    setStealLoserPlayerId(null);
                }}
            />

            <NpcLossDiscardModal
                isOpen={isNpcLossModalOpen}
                handCards={handCards}
                bagCards={bagCards}
                onClose={() => { setIsNpcLossModalOpen(false); setNpcLossModalTitle(); setNpcLossModalSubtitle(); }}
                title={npcLossModalTitle}
                subtitle={npcLossModalSubtitle}
                onDiscard={handleNpcLossDiscard}
            />

            <VotingModal
                isOpen={isVotingModalOpen}
                onClose={() => setIsVotingModalOpen(false)}
                weaponProposed={weaponProposed}
                userProposingWeapon={match?.players?.find(p => p.user?.id === proposingUserId)?.user}
                proposingUserId={proposingUserId}
                matchData={match}
                stompClient={stompClient}
                onSubmit={(voting) => {
                    setIsVotingModalOpen(false);
                }}
            />
        </div>
    );
}