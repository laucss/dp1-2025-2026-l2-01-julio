import React, { useState, useEffect } from "react"
import {useNavigate} from "react-router-dom";
import { Client } from '@stomp/stompjs';
import '../../static/css/match/Match.css';
import DiscardPhaseModal from "./modals/DiscardPhaseModal";
import ActionsModal from "./modals/ActionsModal";
import ChatBox from "./chatBox";
import FightModal from "./modals/FightModal";
import StartDiceModal from "./modals/StartDiceModal";
import StealCardModal from "./modals/StealCardModal";
import NpcLossDiscardModal from "./modals/NpcLossDiscardModal";
import EscapeDiceModal from "./modals/EscapeDiceModal";
import VotingModal from "./modals/VotingModal";
import ReturnedCardModal from "./modals/ReturnedCardModal";

import { normalizeRoomId } from "./utils/roomUtils";
import { getPlayerColor } from "./utils/playersUtil";
import CurrentPlayerInfo from "./components/CurrentPlayerInfo";
import DeckSection from "./components/DeckSection";
import MatchBoardMap from "./components/MatchBoardMap";
import OtherPlayersPanel from './components/OthersPlayersSection';
import MatchButtons from './components/MatchButtons'
import PlayerCardsSection from "./components/PlayerCardsSection";
import { handlePlayerFight, handleNpcFight} from "./utils/fightHelpers";

// para alerta de errores
import { toast } from "react-toastify"
import EndMatchModal from "./modals/EndMatchModal";

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
    const [otherPlayersHands, setOtherPlayersHands] = useState({}) 
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

    const [isReturnedCardModalOpen, setIsReturnedCardModalOpen] = useState(false); 
    const [isDiceModalOpen, setIsDiceModalOpen] = useState(true);
    const [isActionsModalOpen, setIsActionsModalOpen] = useState(false);
    const [isEscapeModalOpen, setIsEscapeModalOpen] = useState(false);
    const [isFightModalOpen, setIsFightModalOpen] = useState(false);
    const [fightDefender, setFightDefender] = useState(null);
    const [fightAttacker, setFightAttacker] = useState(null);
    const [pendingTargetRoom, setPendingTargetRoom] = useState(null);
    const [fightQueue, setFightQueue] = useState([]);
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
    const [returnedFightCard, setReturnedFightCard] = useState(null);

    const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms)); // para que haya un sec entre peleas, que se siente atropellado
    const [confirmModal, setConfirmModal] = useState({
        open: false,
        title: "",
        message: "",
        warning: "",
        confirmText: "",
        onConfirm: null
    });
 

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

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.end`, (msg) => {
            const matchDto = JSON.parse(msg.body);
            setMatch(matchDto)

        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId]);


        useEffect(() => {
            console.log(stompClient);
            console.log(stompClient?.active);
            if (!stompClient || !stompClient.active) return;

            const subscription = stompClient.subscribe(
                `/topic/match.${matchId}.playerLeft`,
                (msg) => {
                    console.log("PLAYER LEFT", msg.body);
                    fetchMatchAndPlayers();
                }
            );

            return () => subscription.unsubscribe();
        }, [stompClient, matchId]);

    useEffect(() => {
        if (!stompClient || !stompClient.active) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.fight`, async (msg) => {
            const fightUpdate = JSON.parse(msg.body);
            
            console.log('fightUpdate', fightUpdate)
            if (fightUpdate.action === 'START') {
                try {
                    const response = await fetch(`/api/v1/matches/${matchId}`, {
                        headers: { Authorization: `Bearer ${jwt}` },
                    });
                    
                    if (response.ok) {
                        const freshMatch = await response.json();
                        setMatch(freshMatch);
                        let attacker =null
                        let defender = null;

                        if (fightUpdate.isBot && (fightUpdate.attackerUsername === 'NPC' || fightUpdate.attackerUsername === 'Niall Campbell' )) {
                            attacker = freshMatch.npcs?.find(n => n.id === fightUpdate.attackerId);
                            defender = freshMatch.players?.find(p => p.user.id === fightUpdate.defenderId);
                        } else if (fightUpdate.isBot && !(fightUpdate.attackerUsername === 'NPC' || fightUpdate.attackerUsername === 'Niall Campbell' )) {
                            attacker =  freshMatch.players?.find(p => p.user.id === fightUpdate.attackerId);
                            defender = freshMatch.npcs?.find(n => n.id === fightUpdate.defenderId);
                        } else {
                            attacker = freshMatch.players?.find(p => p.user.id === fightUpdate.attackerId);
                            defender = freshMatch.players?.find(p => p.user.id === fightUpdate.defenderId);
                        }
                                                
                        if (attacker && defender) {
                            setFightAttacker(attacker);
                            setFightDefender(defender);
                            setPendingTargetRoom(fightUpdate.roomId || fightUpdate.roomName);
                            setIsFightModalOpen(true);
                        }
                        console.log("Abriendo modal", attacker, defender);
                    }
                } catch (error) {
                    console.error('Error fetching fresh match data for fight:', error);
                }
            } else if (fightUpdate.action === 'RESOLVE') {
                const winnerUserId = fightUpdate.winnerUserId
                const loserId = fightUpdate.loserId
                const loserUserId = fightUpdate.loserUserId
                const fightType = fightUpdate.fightResultType

                const currentUserId = currentUser?.id;

                if (currentUserId && currentUserId === winnerUserId) {
                    if (fightType === 'PLAYER_BEATS_PLAYER') {
                        setStealLoserPlayerId(loserId);
                        setIsStealModalOpen(true);
                    }

                    if (fightType === 'PLAYER_BEATS_NPC') {
                        setReturnedFightCard(fightUpdate.card)
                        setIsReturnedCardModalOpen(true);
                    }
                } else if (currentUserId && currentUserId === loserUserId && fightType === 'NPC_BEATS_PLAYER' ){
                    setIsNpcLossModalOpen(true);
                }

                setIsFightModalOpen(false)
                fetchMatchAndPlayers() // REVISAR
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
                    setOtherPlayersHands(prev => ({
                        ...prev,
                        [info.playerId]: hand
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
            console.log('actionPointsUpdate', actionPointsUpdate)
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
            
            console.log('strengthUpdate', strengthUpdate)
            if (strengthUpdate.userId === currentPlayer.user?.id) {
                setStrength(Math.min(6, strengthUpdate.strength));
            } else if (strengthUpdate.npcId) {
                setMatch((prevMatch) => {
                    if (!prevMatch || !prevMatch.npcs) return prevMatch;
                    return {
                        ...prevMatch,
                        npcs: prevMatch.npcs.map((npc) => {
                            if (npc.id === strengthUpdate.npcId) {
                                return {
                                    ...npc,
                                    strength: strengthUpdate.strength 
                                };
                            }
                            return npc;
                        })
                    };
                });
            }
        });

        return () => {
            if (subscription) subscription.unsubscribe();
        };
    }, [stompClient, currentPlayer, matchId]);

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

    }, [currentTurnUserId, currentPlayer]);

    useEffect(() => {
        if (playersList.length > 0) {
            fetchOtherPlayersCards()
        }
    }, [playersList]);

    useEffect(() => {
        calculateActionPoints()
    }, [handCards]);


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

    useEffect(() => {
        // Si el modal de pelea ya está abierto, o si hay fases de resolución activas (robo, descarte), esperamos.
        if (isFightModalOpen || isStealModalOpen || isNpcLossModalOpen || discardPhaseOpen) return;

        if (fightQueue.length > 0) {
            // Obtenemos la siguiente pelea de la cola
            const nextFight = fightQueue[0];
            
            setFightAttacker(nextFight.attacker);
            setFightDefender(nextFight.defender);
            setPendingTargetRoom(nextFight.roomId);
            setIsFightModalOpen(true);

            // Sacamos la pelea procesada de la cola
            setFightQueue(prevQueue => prevQueue.slice(1));
        }
    }, [fightQueue, isFightModalOpen, isStealModalOpen, isNpcLossModalOpen, discardPhaseOpen]);

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

    const fetchOtherPlayersCards = async () => {
        try {
            const bags = {}
            const hands ={}
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
                    hands[player.id] = Array.isArray(data.hand.cards) ? data.hand.cards : []
                }
            }
            setOtherPlayersBags(bags)
            setOtherPlayersHands(hands)
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


   const handleNpcMove = async (roomId) => {
        if (selectedNpcIndex === null) {
            toast.error("Select an NPC first.");
            return;
        }

        const npc = match?.npcs?.[selectedNpcIndex];
        const npcIdToSend = npc?.id ?? null;

        if (!npcIdToSend) {
            toast.error("The selected NPC is not valid.");
            setSelectedNpcIndex(null);
            setSelectedNpcId(null);
            setMoveNpcMode(false);
            return;
        }

        try {
            const response = await fetch(`/api/v1/actions/${matchId}/moveNpc`, {
                method: 'PUT',
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: currentUser.id,
                    roomId,
                    npcId: npcIdToSend
                })
            });

            if (!response.ok) {
                const error = await response.json();
                toast.error(error.message);
                return;
            }

            const data = await response.json();
            updatePlayerData(data);

            setSelectedNpcIndex(null);
            setSelectedNpcId(null);
            setMoveNpcMode(false);

        } catch (err) {
            console.error(err);
            toast.error("Couldn't move the NPC.");
        } finally {
            setSelectedNpcIndex(null);
            setSelectedNpcId(null);
            setMoveNpcMode(false);
        }
    };

    const performPlayerMove = async (roomId, endpoint, resetMoveMode) => {
        try {
            const response = await fetch(
                `/api/v1/actions/${matchId}/${endpoint}`,
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
                updatePlayerData(data); // Actualizamos el mapa y los jugadores con el nuevo estado

                resetMoveMode(false);
            } else {
                resetMoveMode(false);
                  const error = await response.json();
                  toast.error(error.message);
            }
        } catch (error) {
            console.error("Error al realizar el movimiento:", error);
            resetMoveMode(false);
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

    const handleNpcLossDiscard = async ({ cardId, fromWhere }) => {
        if (!cardId || !fromWhere || !currentPlayer?.id) {
            setIsNpcLossModalOpen(false);
            return;
        }

        try {
            const res = await fetch(`/api/v1/fights/${matchId}/${currentPlayer.id}/lose-against-npc`, {
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
                await fetchCards()
                await fetchOtherPlayersCards()
                await handleCheckChainFights()
            }
        } catch (error) {
            console.error('Error discarding card after NPC loss:', error);
        } finally {
            setIsNpcLossModalOpen(false);
        }
    }

    const endMatch = () => {
        setConfirmModal({
            open: true,
            title: "End Match",
            message: "Are you sure you want to end this match?",
            warning: "The match will finish immediately for every player.",
            confirmText: "End Match",
            onConfirm: async () => {
                try {
                    const res = await fetch(`/api/v1/matches/${matchId}/end`, {
                        method: "PUT",
                        headers: {
                            Authorization: `Bearer ${jwt}`,
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify(10)
                    });

                    const updated = await res.json();
                    setMatch(updated);
                } catch (err) {
                    console.error(err);
                }
            }
        });
    };

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
                    toast.error("Couldn't leave the match.");
                    return;
                }

                navigate('/');
            } catch (err) {
                console.error(err);
                toast.error("Error leaving the match.");
            }
        };

    const currentPlayerTurn = match?.players?.find(p => p.user.id === match.currentTurnUserId);
    const isRoomSelectionActive = moveNpcMode || moveToAdyacentRoom || moveToRoomWithWord;
    const roomSelectionMessage = moveNpcMode
        ? (selectedNpcIndex === null ? 'Select the NPC you want to move' : 'Select the room')
        : 'Select the room';

    const canDraw = match?.currentTurnUserId === currentUser?.id &&
                match?.currentTurnPhase === "DRAW" &&
                numCardsDrawn < 7;

    const escapeTowerWords = {
        1: "escape",
        6: "from",
        31: "elba",
        36: "peace",
    };

    const currentRoomId = normalizeRoomId(
        currentPlayer && (currentPlayer.roomId ?? currentPlayer.room?.id ?? currentPlayer.currentRoom?.id)
    );
    const escapeWord = escapeTowerWords[currentRoomId] || null;
    const bagWord = Array.isArray(bagCards)
        ? bagCards
            .map(card => card?.letter || "")
            .join("")
            .toLowerCase()
        : "";

    const hasEscapeActionPoints = actionPoints > 0;
    const isEscapeTower = [1, 6, 31, 36].includes(currentRoomId);
    const hasRequiredEscapeWord = Boolean(escapeWord) && bagWord === escapeWord;
    const canAttemptEscape = hasEscapeActionPoints && isEscapeTower && hasRequiredEscapeWord;

    const escapeAttemptReason = !hasEscapeActionPoints
        ? "No tienes puntos de acción para intentar escapar."
        : !isEscapeTower
            ? "Solo puedes intentar escapar desde una torre de escape."
            : !hasRequiredEscapeWord
                ? "No tienes en la bolsa la palabra correcta para esa torre."
                : "";

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
            <EndMatchModal
                match={match}
            />
        );
    }

    if (!match) {
        return <div>Cargando partida...</div>;
    }


    const handleFightResult = async (attackerWins) => {
        
        try {
            const npcIsAttacker = !fightAttacker?.user;

            if (npcIsAttacker) {
                const isCurrentDefender = currentUser.id === fightDefender?.user?.id;
                if (!isCurrentDefender) {
                    await clearFightState();
                    return;
                }
            } else {
                const isCurrentAttacker = currentUser.id === fightAttacker?.user?.id;
                if (!isCurrentAttacker) {
                    await clearFightState();
                    return;
                }
            }

            const defenderRoomId = fightDefender?.currentRoom?.id || fightDefender?.roomId || fightDefender?.room?.id || pendingTargetRoom;
            
            const isNpcFight = !fightAttacker?.user || !fightDefender?.user;

            if (isNpcFight) {
                await handleNpcFight({
                    matchId,
                    jwt,
                    attackerWins,
                    fightAttacker,
                    fightDefender,
                    npcIsAttacker, 
                    defenderRoomId,
                    handCards,
                    bagCards,
                    setIsNpcLossModalOpen,
                    setReturnedFightCard,
                    setIsReturnedCardModalOpen
                });
                return;
            }
            
            await handlePlayerFight({
                matchId,
                jwt,
                attackerWins,
                fightAttacker,
                fightDefender,
                defenderRoomId,
                currentUser,
                setStealLoserPlayerId,
                setIsStealModalOpen
            });

        } catch (err) {
            console.error("Error al procesar el resultado de la pelea:", err);
        } finally {
            //await clearFightState();
            
        }
    }

    const handleCheckChainFights = async () => {
        try {
            await delay(1000);
            // Avisamos al backend de que la interfaz está despejada 
            // y puede comprobar si hay que encadenar combates
            await fetch(`/api/v1/fights/${matchId}/check-pending-fights`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    "Content-Type": "application/json"
                }
            });

        } catch (error) {
            console.error("Error al comprobar peleas en cadena:", error);
        }
    }

    const clearFightState = async () => {
        setIsFightModalOpen(false);
        setFightDefender(null);
        setFightAttacker(null);
        setPendingTargetRoom(null);
        await cleanVotingStates();
    };

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
                canAttemptEscape={canAttemptEscape}
                escapeAttemptReason={escapeAttemptReason}
                onResult={async (result) => {
                    try {
                        await fetchMatchAndPlayers();
                        if (!result.success && result.discardRequired) {
                            setNpcLossModalTitle('Your escape attempt failed');
                            setNpcLossModalSubtitle('Choose a card to discard');
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
                            actionPoints={actionPoints ? actionPoints : 0}
                            strength={strength ? strength : 1 }
                            getPlayerColor={getPlayerColor}
                            players={match?.players}
                            match={match}
                            currentUser={currentUser}
                        />
                    </div>

     

                <DeckSection
                    numCardsDrawn={numCardsDrawn}
                    drawCard={drawCard}
                    canDraw={canDraw}
                    deck={deck}
                />

                

                {match?.creatorId === currentUser?.id && (
                    <button
                        className="end-match-button deck-end-match-button"
                        onClick={endMatch}
                    >
                        End Match
                    </button>
                )}

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
                />

                <div className="panel-players-and-buttons"> 
                    <OtherPlayersPanel
                        playersList={playersList}
                        otherPlayersHands={otherPlayersHands}
                        otherPlayersBags={otherPlayersBags}
                        getPlayerColor={getPlayerColor}
                        players={match?.players}
                        npcs={match.npcs}
                    />

                    <MatchButtons
                        match={match}
                        matchId={matchId}
                        currentUser={currentUser}
                        actionPoints={actionPoints}
                        setDiscardPhaseOpen={setDiscardPhaseOpen}
                        setIsActionsModalOpen={setIsActionsModalOpen}
                        leaveMatch={() =>
                            setConfirmModal({
                                open: true,
                                title: "Leave Match",
                                message: "Are you sure you want to leave this match?",
                                warning: "Your progress in this match will be lost.",
                                confirmText: "Leave Match",
                                onConfirm: leaveMatch
                            })
                        }                   
                        handleEndTurn={handleEndTurn}
                        isEndingTurn={isEndingTurn}
                        chatOpen={chatOpen}
                        setChatOpen={setChatOpen}
                        ChatBox={ChatBox}
                    />
                </div>
                <ActionsModal
                        isOpen={isActionsModalOpen}
                        onClose={() => setIsActionsModalOpen(false)}
                        moveToAdyacent={() => setMoveToAdyacentRoom(true)}
                        moveToRoomWithWord={() => setMoveToRoomWithWord(true)}
                        onMoveNpcRequested={() => { setMoveNpcMode(true); setSelectedNpcId(null); setSelectedNpcIndex(null); }}
                        onAttemptEscape={() => {
                            if (!canAttemptEscape) {
                                toast.error(escapeAttemptReason);
                                return;
                            }

                            setIsEscapeModalOpen(true);
                        }}
                        canAttemptEscape={canAttemptEscape}
                        escapeAttemptReason={escapeAttemptReason}
                    />


            </div>

            {isRoomSelectionActive && match?.currentTurnUserId === currentUser?.id && (
                <div style={{ width: '100%', display: 'flex', justifyContent: 'center', marginTop: '40px', marginBottom: '60px', zIndex: 30 }}>
                    <div style={{ padding: '14px 42px', background: '#d200005e', color: '#fff', borderRadius: '10px', fontWeight: '700', fontSize: '20px', minWidth: '280px', textAlign: 'center', transform: 'translateX(20px)' }}>
                        {roomSelectionMessage}
                    </div>
                </div>
            )}

 
            <PlayerCardsSection
                handCards={handCards}
                bagCards={bagCards}
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
                onResolve={async (attackerWins) => { handleFightResult(attackerWins); }}
            />

            <ReturnedCardModal
                isOpen={isReturnedCardModalOpen}
                onClose={async () => {
                    setIsReturnedCardModalOpen(false);
                    setIsFightModalOpen(false);
                    await handleCheckChainFights()
                }}
                card={returnedFightCard}
            />

            <StealCardModal
                isOpen={isStealModalOpen}
                loserId={stealLoserPlayerId}
                matchId={matchId}
                winnerId={currentPlayer?.id}
                onClose={async () => {
                    setIsStealModalOpen(false);
                    setStealLoserPlayerId(null);
                    setIsFightModalOpen(false);
                    await handleCheckChainFights()
                }}
                onSteal={async () => {
                    await fetchCards();
                    setIsStealModalOpen(false);
                    setStealLoserPlayerId(null);
                    setIsFightModalOpen(false);
                    await handleCheckChainFights()
                }}
            />

            <NpcLossDiscardModal
                isOpen={isNpcLossModalOpen}
                handCards={handCards}
                bagCards={bagCards}
                onClose={async () => { 
                    setIsNpcLossModalOpen(false);
                    setNpcLossModalTitle(); 
                    setNpcLossModalSubtitle(); 
                    setIsFightModalOpen(false);
                    await handleCheckChainFights()
                 }}
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
            {confirmModal.open && (
                <div className="leave-match-overlay">
                    <div className="leave-match-modal">
                        <h2>{confirmModal.title}</h2>

                        <p>{confirmModal.message}</p>

                        <p className="leave-warning">
                            {confirmModal.warning}
                        </p>

                        <div className="leave-buttons">
                            <button
                                className="leave-cancel"
                                onClick={() =>
                                    setConfirmModal(prev => ({ ...prev, open: false }))
                                }
                            >
                                Cancel
                            </button>

                            <button
                                className="leave-confirm"
                                onClick={async () => {
                                    setConfirmModal(prev => ({ ...prev, open: false }));
                                    await confirmModal.onConfirm();
                                }}
                            >
                                {confirmModal.confirmText}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}