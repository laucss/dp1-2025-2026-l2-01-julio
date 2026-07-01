import React, { useState, useEffect, useRef } from "react"
import {useNavigate} from "react-router-dom";
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import '../../static/css/match/Match.css';
import getIdFromUrl from '../../util/getIdFromUrl'
import useFetchState from "../../util/useFetchState";
import tokenService from "../../services/token.service";
import DiscardPhaseModal from "./DiscardPhaseModal";
import ActionsModal from "./ActionsModal";
import ChatBox from "./chatBox";
import { FaComments } from "react-icons/fa";
import FightModal from "./FightModal";
import StartDiceModal from "./StartDiceModal";
import StealCardModal from "./StealCardModal";
import NpcLossDiscardModal from "./NpcLossDiscardModal";
import EscapeDiceModal from "./EscapeDiceModal";
import VotingModal from "./VotingModal";

import { normalizeRoomId, areRoomsAdjacent } from "./utils/roomUtils";
import { getPlayerColor } from "./utils/playersUtil";
import CurrentPlayerInfo from "./components/CurrentPlayerInfo";
import DeckSection from "./components/DeckSection";
import MatchBoardMap from "./components/MatchBoardMap";

// para alerta de errores
import { toast } from "react-toastify";



const jwt = tokenService.getLocalAccessToken();
const currentUser = tokenService.getUser();


export default function Match(){
    const matchId = getIdFromUrl(2);
    const navigate = useNavigate();
    const [currentPlayer, setCurrentPlayer] = useState({}) // el jugador asociado al usuario que está "viendo" la pantalla
    //console.log('jugador', currentPlayer)
    const [player, setPlayer] = useState([])
    const [playersList, setPlayersList] = useState([])
    const [match, setMatch] = useState(null)
    const [currentTurnUserId, setCurrentTurnUserId] = useState(null) // id del usuario al que le toca jugar
    const [stompClient, setStompClient] = useState(null)

    // CARTAS
    const [deck, setDeck] = useState([])
    const [discardPile, setDiscardPile] = useState([])
    const [handCards, setHandCards] = useState([])
    const [bagCards, setBagCards] = useState([])
    const [otherPlayersBags, setOtherPlayersBags] = useState({}) 
    const [numCardsDrawn, setNumCardsDrawn] = useState(0)
    const [bagOpen, setBagOpen] = useState(false)
    const [discardPhaseOpen, setDiscardPhaseOpen] = useState(false)

    // DADOS 
    const [whiteDice, setWhiteDice] = useState("1")
    const [blackDice, setBlackDice] = useState("1")

    const [chatOpen, setChatOpen] = useState(false)
    const [actionPoints, setActionPoints] = useState(0)
    const [strength, setStrength] = useState(1)
    const [moveToAdyacentRoom, setMoveToAdyacentRoom] = useState(false)
    const [moveToRoomWithWord, setMoveToRoomWithWord] = useState(false)
    const [isEndingTurn, setIsEndingTurn] = useState(false)
    const [moveNpcMode, setMoveNpcMode] = useState(false)
    const [selectedNpcId, setSelectedNpcId] = useState(null)
    const [selectedNpcIndex, setSelectedNpcIndex] = useState(null)

    
    // const [playerTurnId, setPlayerTurnId] = useState(null)

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

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
    
    // Determinar si el usuario actual es un espectador
    const isSpectator = !currentUser || !match?.players?.some(p => p.user.id === currentUser.id);
    
    // CARGAR DATOS PARTIDA 
      const [adjacencies, setAdjacencies] = useFetchState(
        [],
        `/api/v1/matches/adjacencies`,
        jwt,
        setMessage,
        setVisible
      );

    // CARGAR DATOS JUGADORES 
    useEffect(() => {
        fetchMatchAndPlayers()
    }, [matchId])
    //console.log('match', match)

    
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
                // Actualizar el match primero para tener datos frescos
                try {
                    const response = await fetch(`/api/v1/matches/${matchId}`, {
                        headers: {
                            Authorization: `Bearer ${jwt}`,
                        },
                    });
                    
                    if (response.ok) {
                        const freshMatch = await response.json();
                        setMatch(freshMatch);
                        
                        // Identificar al atacante (quien inicia la batalla)
                        const attacker = freshMatch.players?.find(p => p.user.id === fightUpdate.attackerId);
                        
                        // Identificar al defensor (puede ser jugador o NPC)
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
                        } else {
                            console.error('Could not find attacker or defender:', { attacker, defender, fightUpdate });
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
                            return {
                                ...p,
                                currentRoom: locationUpdate.newRoom
                            };
                        }
                        return p;
                    })
                };
            });
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId]);

    // websocket para recibir notificaciones de votación de armas
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

    // websocket para recibir resultado de votación de armas
    useEffect(() => {
        if (!stompClient || !stompClient.active) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.weapon.voting.result`, (msg) => {
            const result = JSON.parse(msg.body);
            if (result.status === 'FINISHED') {
                setVotingResult(result); // Guardar el resultado para que FightModal lo procese
                setIsVotingModalOpen(false);
                
                // Mostrar mensaje del resultado
                if (result.result === 'ACCEPTED') {
                    toast.success(`"${result.proposedWeapon}" was accepted! Bonus: +${result.finalBonus}`);
                } else {
                    toast.error(`"${result.proposedWeapon}" was rejected. No bonus awarded.`);
                }
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId]);

    // Suscripción a actualizaciones de ubicación de NPCs
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
                            return {
                                ...npc,
                                room: locationUpdate.newRoom
                            };
                        }
                        return npc;
                    })
                };
            });
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId]);

    // Suscripción a actualizaciones de cartas (mano/bolsa) de todos los jugadores
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

                // Si es el jugador actual, actualizamos su mano y bolsa
                if (currentPlayer?.id === info.playerId) {
                    setHandCards(hand.map(c => ({...c})));
                    setBagCards(bag.map(c => ({...c})));
                } else {
                    // Si es otro jugador, actualizamos su bolsa en el mapa de bolsas
                    setOtherPlayersBags(prev => ({
                        ...prev,
                        [info.playerId]: bag
                    }));
                }

                // Actualizar el mazo y la pila de descarte globalmente
                if (deckInfo) {
                    setDeck(deckInfo);
                    const discarded = Array.isArray(deckInfo.discardedCards) ? deckInfo.discardedCards : [];
                    setDiscardPile(discarded);
                }
            };

            // Aplicar actualizaciones para winner, loser, o cualquier jugador
            if (winner) applyCardsUpdate(winner);
            if (loser) applyCardsUpdate(loser);
            // Si no hay winner/loser, puede haber una actualización directa
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
            
            // Only update action points if the update is for the current player
            if (actionPointsUpdate.userId === currentPlayer.user.id) {
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
        if (!stompClient || !stompClient.active) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.strength`, (msg) => {
            const strengthUpdate = JSON.parse(msg.body);
            
            // Only update strength if the update is for the current player
            if (strengthUpdate.userId === currentPlayer.user.id) {
                setStrength(Math.min(6, strengthUpdate.strength));
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, currentPlayer]);

    useEffect(() => {
            if (player && Array.isArray(player)){
                setPlayersList(player.filter(p => p.user.id !== currentUser?.id))
                setCurrentPlayer(player.filter(p => p.user.id === currentUser?.id)[0])
                
            }
    }, [match])

    // TODO: revisar si esto se puede sacar a otro lado 

    useEffect(() => {
        if (Array.isArray(currentPlayer) && currentPlayer?.id){
            fetchCards()
        }
        if (currentPlayerTurn) { 
            setStrength(Math.min(6, currentPlayer.strength)) }
        setNumCardsDrawn(0)
        
        if (currentTurnUserId && currentPlayer.user.id === currentTurnUserId){ 
            fetchActionPoints() 
        }
    }, [currentTurnUserId])

    useEffect(() => {
        if (playersList.length > 0) {
            fetchOtherPlayersBags()
        }
    }, [playersList])

    useEffect(() => {
        calculateActionPoints()
    }, [handCards])

    useEffect(() => {
        if (!currentPlayer || !currentPlayer.hand || !currentPlayer.bag) return;

        setHandCards(currentPlayer.hand.cards || []);
        setBagCards(currentPlayer.bag.cards || []);
    }, [currentPlayer])



    // Polling para actualizar el match mientras el modal de dados está abierto
    useEffect(() => {
        if (!isDiceModalOpen || match?.currentTurnPhase !== null) return;

        const fetchMatchUpdate = async () => {
            try {
                const response = await fetch(`/api/v1/matches/${matchId}`, {
                    headers: {
                        'Authorization': `Bearer ${jwt}`,
                    }
                });
                if (response.ok) {
                    const updatedMatch = await response.json();
                    setMatch(updatedMatch);
                }
            } catch (error) {
                console.error("Error al actualizar match:", error);
            }
        };

        // Actualizar cada 2 segundos mientras el modal esté abierto
        const intervalId = setInterval(fetchMatchUpdate, 2000);

        return () => clearInterval(intervalId);
    }, [isDiceModalOpen, match?.currentTurnPhase, matchId]);

    // Función para limpiar los estados de votación
    const cleanVotingStates = async () => {
        setVotingResult(null);
        setIsVotingModalOpen(false);
        setWeaponProposed('');
        setProposingUserId(null);
        setProposingUsername('');
        
        // Eliminar las votaciones de la base de datos
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

        // console.log('currentPlayer', currentPlayer)
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

                // Normalizar los roomIds de todos los jugadores y NPCs
                if (data.players) {
                    data.players = data.players.map(p => ({
                        ...p,
                        currentRoom: p.currentRoom ? {
                            ...p.currentRoom,
                            id: normalizeRoomId(p.currentRoom.id)
                        } : null,
                        room: p.room ? {
                            ...p.room,
                            id: normalizeRoomId(p.room.id)
                        } : null,
                        roomId: p.roomId ? normalizeRoomId(p.roomId) : null
                    }));
                }

                if (data.npcs) {
                    data.npcs = data.npcs.map(npc => ({
                        ...npc,
                        room: npc.room ? {
                            ...npc.room,
                            id: normalizeRoomId(npc.room.id)
                        } : null
                    }));
                }

                setMatch(data)
                setPlayer(data.players)
                setDeck(data.deck)
                setActionPoints(data.players.filter(p=>p.user.id === currentUser.id)[0].actionPoints)
                //console.log('posiciones', player)

            } catch (error) {
                
            }
    }


    //console.log('deck', deck)
    // Mover el ganador a la habitación objetivo
    const movePlayerToRoom = async (userId, roomId) => {
        try {
            console.log('movePlayerToRoom (normal move)', { userId, roomId });
            const response = await fetch(`/api/v1/matches/${matchId}/move`, {
                method: "PUT",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ userId, roomId })
            });

            if (!response.ok) {
                const text = await response.text();
                console.error('movePlayerToRoom response not ok', response.status, response.statusText, text);
                throw new Error(`Move failed: ${response.status} ${response.statusText} - ${text}`);
            }

            const data = await response.json();
            updatePlayerData(data);
                
                // Si el ganador es el jugador actual, actualizar los puntos de acción
                const movedPlayer = data.players.find(p => p.user.id === currentUser?.id);
                if (movedPlayer && userId === currentUser?.id) {
                    setActionPoints(movedPlayer.actionPoints);
                    console.log('Action points updated for winner:', movedPlayer.actionPoints);
                }
            return data;
        } catch (err) {
            console.error('Error moving player:', err);
            return null;
        }
    }

    // iNICIALIZAR BARAJA
    const fetchCards = async () => {
        try {
            //console.log('ENTRA EN EL FETCHCARDS')
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
                //console.log('datos fetch cards' , data)
                setHandCards(Array.isArray(data.hand.cards) ? data.hand.cards : [])
                setBagCards(Array.isArray(data.bag.cards) ? data.bag.cards : [])
                setDeck(data.deck || [])
                setDiscardPile(Array.isArray(data.deck?.discardedCards) ? data.deck.discardedCards : [])
                return data
            } 

        
            
        } catch (error) {
            console.log('error', error)
            setMessage("Could not get the cards.");
            setVisible(true);
            
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


    // FUNCION ROBAR CARTA
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

            if (response.ok){
                const data = await response.json()
                //console.log('carta', data.card)
                
                setDeck(data.deck)
                //setHandCards(prev => [...prev, data.card])
                setNumCardsDrawn(prev => prev + 1)
            }
            
        } catch (error) {
            console.log('error', error)
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

            if (!response.ok) {
                throw new Error(`Error ${response.status}: ${response.statusText}`)
            }

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

        const me = data.players.find(
            p => p.user.id === currentUser.id
        );

        if (me) {
            setCurrentPlayer([me]);
            setPlayersList(
                data.players.filter(
                    p => p.user.id !== currentUser.id
                )
            );

            setActionPoints(me.actionPoints ?? actionPoints);
            setStrength(Math.min(6, me.strength ?? strength));
        }
    };

    const notifyFight = async ({
        attackerId,
        attackerUsername,
        defenderId,
        defenderUsername,
        roomId,
        isBot = false
    }) => {
        await fetch(`/api/v1/matches/${matchId}/notify-fight`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${jwt}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                matchId,
                attackerId,
                attackerUsername,
                defenderId,
                defenderUsername,
                roomId,
                action: "START",
                isBot
            })
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
                body: JSON.stringify({
                    matchId,
                    userId,
                    actionPoints
                })
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
                body: JSON.stringify({
                    userId: currentUser.id,
                    roomId,
                    npcId: npcIdToSend
                })
            });

            if (!response.ok) {
                const text = await response.text();
                console.error('Error moving NPC:', response.status, text);
                return;
            }

            const data = await response.json();

            updatePlayerData(data);

            setSelectedNpcIndex(null);
            setSelectedNpcId(null);
            setMoveNpcMode(false);

            const targetRoomNormalized = normalizeRoomId(roomId);

            const playerInRoom = data.players.find(p => {
                const playerRoomId =
                    p.currentRoom?.id ||
                    p.roomId ||
                    p.room?.id;

                return normalizeRoomId(playerRoomId) === targetRoomNormalized;
            });

            if (playerInRoom && roomId !== 37) {

                const movedNpc =
                    data.npcs.find(n => n.id === npcIdToSend);

                if (movedNpc) {

                    await notifyFight({
                        attackerId: playerInRoom.user.id,
                        attackerUsername: playerInRoom.user.username,
                        defenderId: movedNpc.id,
                        defenderUsername: movedNpc.isNiallCampbell
                            ? "Niall Campbell"
                            : "NPC",
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

                const currentPlayerData = currentPlayer?.[0];

                setPendingTargetRoom(roomId);
                setFightDefender(botInRoom);
                setFightAttacker(currentPlayerData);
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
                    body: JSON.stringify({
                        userId: currentUser.id,
                        roomId
                    })
                }
            );

            if (response.ok) {

                const data = await response.json();

                updatePlayerData(data);

                const movedPlayer = data.players.find(
                    p => p.user.id === currentUser.id
                );

                if (movedPlayer) {
                    await notifyActionPoints(
                        currentUser.id,
                        movedPlayer.actionPoints
                    );
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
        return performPlayerMove(
            roomId,
            "moveByLetters",
            setMoveToRoomWithWord
        );
    };

    const handleAdjacentMove = async (roomId) => {
        return performPlayerMove(
            roomId,
            "move",
            setMoveToAdyacentRoom
        );
    };
     



    const move = async (roomId) => {
        // Si vamos a mover un NPC
        if (moveNpcMode) {
            return handleNpcMove(roomId);
        }
        
        // Move with words flow
        if (moveToRoomWithWord === true){
            return handleWordMove(roomId);
        }
        
        if (moveToAdyacentRoom===false) return ;
        if (moveToAdyacentRoom === true){
            return handleAdjacentMove(roomId);
        }
    }

    

    const moveLoserToRandomRoom = async (userId, roomId) => {
        try {
            console.log('movePlayerToRoom', { userId, roomId });
            const response = await fetch(`/api/v1/matches/${matchId}/moveLoser`, {
                method: "PUT",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ userId, roomId })
            });

            if (!response.ok) {
                const text = await response.text();
                console.error('movePlayerToRoom response not ok', response.status, response.statusText, text);
                throw new Error(`Move failed: ${response.status} ${response.statusText} - ${text}`);
            }

            const data = await response.json();
            updatePlayerData(data);
                
            const movedPlayer = data.players.find(p => p.user.id === userId);
            if (movedPlayer) {
                await notifyActionPoints(userId,movedPlayer.actionPoints);
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
                body: JSON.stringify({
                    cardId,
                    fromWhere
                })
            });

            if (!res.ok) {
                console.error('Error discarding card after NPC loss:', res.status);
                alert('No se pudo descartar la carta tras perder contra el NPC.');
            } else {
                await fetchCards();
                await fetchOtherPlayersBags();
            }
        } catch (error) {
            console.error('Error discarding card after NPC loss:', error);
            alert('Ocurrió un error al descartar la carta.');
        } finally {
            setIsNpcLossModalOpen(false);
        }
    }

    
    const endMatch = () => {
        if (!window.confirm("¿Are you sure you want to end the match?")) return; 
        const body =10;
        console.log('body end match', body)
        fetch(`/api/v1/matches/${matchId}/end`, {
            method: "PUT",
            headers: {
                Authorization: `Bearer ${jwt}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        })
        .then(res => res.json())
        .then(updated => {
            console.log("Match ended:", updated)
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

            // Actualizar el estado del match después de cambiar de turno
            await fetchMatchAndPlayers();
        } catch (error) {
            console.error('Error calling next-turn:', error);
            toast.error(error.message)
        } finally {
            setIsEndingTurn(false);
        }
    };

    const leaveMatch = async () => {
        if (!window.confirm("¿Seguro que quieres abandonar la partida?")) return;

        // En modo espectador no hay backend que eliminar; solo volver a la home
        if (isSpectator) {
            navigate('/');
            return;
        }

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
                const text = await response.text();
                console.error('Error leaving match:', response.status, text);
                alert('No se pudo abandonar la partida');
                return;
            }

            // Volver al inicio
            navigate('/');
        } catch (err) {
            console.error('Error leaving match:', err);
            alert('Error al abandonar la partida');
        }
    };

    const currentPlayerTurn = match?.players?.find(p => p.user.id === match.currentTurnUserId);

    const canDraw = match?.currentTurnUserId === currentUser?.id &&
                match?.currentTurnPhase === "DRAW" &&
                numCardsDrawn < 7;



    const calculateActionPoints = () => {
        if (!match) return;
        if (match.currentTurnPhase !== "DRAW")
            return;
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


    const handleFightResult = async (currentUserWon) => {
        try {
            console.log('Fight resolved. currentUserWon=', currentUserWon, 'fightDefender=', fightDefender, 'fightAttacker=', fightAttacker);
            const isCurrentAttacker = currentUser.id === fightAttacker?.user?.id;
            const isCurrentDefender = currentUser.id === fightDefender?.user?.id;

            // Solo el atacante orquesta los movimientos para evitar duplicados
            if (!isCurrentAttacker) {
                setIsFightModalOpen(false);
                setFightDefender(null);
                setFightAttacker(null);
                setPendingTargetRoom(null);
                cleanVotingStates();
                return;
            }

            const defenderRoomId = fightDefender?.currentRoom?.id || fightDefender?.roomId || fightDefender?.room?.id || pendingTargetRoom;

            // currentUserWon aquí representa si el atacante ganó (porque solo el atacante ejecuta esto)
            const attackerWins = currentUserWon;
            
            // Obtener el perdedor (puede ser jugador o NPC)
            const isDefenderNPC = !fightDefender?.user;
            const loserUser = attackerWins ? fightDefender?.user : fightAttacker?.user;
            const loser = attackerWins ? fightDefender : fightAttacker;
            const winnerUser = attackerWins ? fightAttacker?.user : (fightDefender?.user || fightDefender);

            // Si el defensor es un NPC
            if (isDefenderNPC) {
                try {
                    if (attackerWins) {
                        // El jugador ganó contra el bot
                        console.log('Player won against NPC. Defender is NiallCampbell:', fightDefender?.isNiallCampbell);
                        
                        // Si es NiallCampbell, roba de la pila de descarte
                        if (fightDefender?.isNiallCampbell) {
                            console.log('Drawing from discard pile for beating Niall Campbell...');
                            await fetch(`/api/v1/matches/${matchId}/${fightAttacker?.id}/playerWinsNiallCampbell`, {
                                method: "POST",
                                headers: {
                                    Authorization: `Bearer ${jwt}`,
                                    Accept: 'application/json',
                                    'Content-Type': 'application/json',
                                },
                            }).then(r => r.json()).then(data => {
                                // Actualizamos inmediatamente el mazo; la mano se sincroniza por WebSocket
                                setDeck(data.deck);
                            });
                        } else {
                            // Si es un NPC normal, roba 2 cartas de recompensa
                            console.log('Drawing 2 reward cards for beating regular NPC...');
                            await drawCardForWinner(fightAttacker?.id);
                            await drawCardForWinner(fightAttacker?.id);
                        }
                        
                        // Mover al jugador ganador a la habitación donde estaba el bot
                        if (defenderRoomId) {
                            console.log('Moving winner to defender room:', defenderRoomId);
                            await movePlayerToRoom(fightAttacker?.user?.id, defenderRoomId);
                        }
                        
                        // Enviar al bot perdedor a una habitación aleatoria
                        const allRoomIds = [
                            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                            21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37
                        ];
                        
                        const occupiedRoomIds = new Set();
                        (match?.players || []).forEach(p => {
                            const rid = p.currentRoom?.id || p.roomId || p.room?.id;
                            const normalized = normalizeRoomId(rid);
                            if (normalized) occupiedRoomIds.add(normalized);
                        });
                        (match?.npcs || []).forEach(npc => {
                            const rid = npc.room?.id;
                            const normalized = normalizeRoomId(rid);
                            if (normalized) occupiedRoomIds.add(normalized);
                        });
                        
                        const candidates = allRoomIds.filter(r => !occupiedRoomIds.has(r) && r !== normalizeRoomId(defenderRoomId));
                        let botRandomRoomId = null;
                        if (candidates.length > 0) {
                            botRandomRoomId = candidates[Math.floor(Math.random() * candidates.length)];
                        } else {
                            const fallback = allRoomIds.filter(r => r !== normalizeRoomId(defenderRoomId));
                            botRandomRoomId = fallback[Math.floor(Math.random() * fallback.length)];
                        }
                        
                        // Normalizar habitaciones corridor (10 -> 9, 28 -> 27)
                            botRandomRoomId = normalizeRoomId(botRandomRoomId);
                        
                        console.log('Moving NPC loser to random room:', botRandomRoomId);
                        await fetch(`/api/v1/matches/${matchId}/npc/location`, {
                            method: 'PUT',
                            headers: {
                                'Authorization': `Bearer ${jwt}`,
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify({
                                npcId: fightDefender.id,
                                roomId: botRandomRoomId
                            })
                        });
                        
                        // Actualizar el estado del match después de mover al NPC
                        await fetchMatchAndPlayers();
                        
                        // Incrementar fuerza del bot perdedor
                        await fetch(`/api/v1/matches/${matchId}/npc-strength`, {
                            method: 'PUT',
                            headers: {
                                'Authorization': `Bearer ${jwt}`,
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify({
                                npcId: fightDefender.id,
                                strength: (fightDefender?.strength || 1) + 1
                            })
                        });
                    } else {
                        // El jugador perdió contra el bot: mover solo al jugador a habitación aleatoria e incrementar fuerza del jugador
                        console.log('Player lost against NPC. Moving player to random room and increasing player strength...');
                        
                        const allRoomIds = [
                            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                            21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37
                        ];
                        
                        const playerRoomId = normalizeRoomId(currentPlayer?.[0]?.currentRoom?.id);
                        
                        const occupiedRoomIds = new Set();
                        (match?.players || []).forEach(p => {
                            const rid = p.currentRoom?.id || p.roomId || p.room?.id;
                            const normalized = normalizeRoomId(rid);
                            if (normalized) occupiedRoomIds.add(normalized);
                        });
                        (match?.npcs || []).forEach(npc => {
                            const rid = npc.room?.id;
                            const normalized = normalizeRoomId(rid);
                            if (normalized) occupiedRoomIds.add(normalized);
                        });
                        
                        // Mover al jugador perdedor a una sala aleatoria
                        const candidatesPlayer = allRoomIds.filter(r => !occupiedRoomIds.has(r) && r !== playerRoomId);
                        let randomRoomIdPlayer = null;
                        if (candidatesPlayer.length > 0) {
                            randomRoomIdPlayer = candidatesPlayer[Math.floor(Math.random() * candidatesPlayer.length)];
                        } else {
                            const fallback = allRoomIds.filter(r => r !== playerRoomId);
                            randomRoomIdPlayer = fallback[Math.floor(Math.random() * fallback.length)];
                        }
                        
                        // Normalizar habitaciones corridor (10 -> 9, 28 -> 27)
                        randomRoomIdPlayer = normalizeRoomId(randomRoomIdPlayer);
                        
                        console.log('Moving player loser to random room:', randomRoomIdPlayer);
                        await moveLoserToRandomRoom(currentUser.id, randomRoomIdPlayer);
                        
                        // Incrementar fuerza del jugador perdedor
                        await fetch(`/api/v1/matches/${matchId}/player-strength`, {
                            method: 'PUT',
                            headers: {
                                'Authorization': `Bearer ${jwt}`,
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify({
                                userId: currentUser.id,
                                strength: (currentPlayer?.[0]?.strength || 1) + 1
                            })
                        });

                        if ((handCards?.length || 0) > 0 || (bagCards?.length || 0) > 0) {
                            setIsNpcLossModalOpen(true);
                        }
                    }
                } catch (error) {
                    console.error('Error handling NPC fight result:', error);
                }
                
                setIsFightModalOpen(false);
                setFightDefender(null);
                setFightAttacker(null);
                setPendingTargetRoom(null);
                cleanVotingStates();
                return;
            }

            // Si no hay perdedor identificado, no mover
            if (!loserUser) {
                setIsFightModalOpen(false);
                setFightDefender(null);
                setFightAttacker(null);
                setPendingTargetRoom(null);
                cleanVotingStates();
                return;
            }
            const allRoomIds = [
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37
            ];
            const winnerPlayer = match?.players?.find(p => p.user?.id === (winnerUser?.id || winnerUser?.user?.id));
            const winnerRoomIdRaw = winnerPlayer?.currentRoom?.id || winnerPlayer?.roomId || null;
            const winnerRoomId = normalizeRoomId(winnerRoomIdRaw);

            const occupiedRoomIds = new Set();
            (match?.players || []).forEach(p => {
                const rid = p.currentRoom?.id || p.roomId || p.room?.id;
                const normalized = normalizeRoomId(rid);
                if (normalized) occupiedRoomIds.add(normalized);
            });
            (match?.npcs || []).forEach(npc => {
                const rid = npc.room?.id;
                const normalized = normalizeRoomId(rid);
                if (normalized) occupiedRoomIds.add(normalized);
            });

            const candidates = allRoomIds.filter(r => r !== winnerRoomId && !occupiedRoomIds.has(r));
            if (candidates.length === 0) {
                console.warn('No candidate rooms to move loser');
                setIsFightModalOpen(false);
                setFightDefender(null);
                setFightAttacker(null);
                return;
            }
            let randomRoomId = null;
            if (candidates.length > 0) {
                randomRoomId = candidates[Math.floor(Math.random() * candidates.length)];
            } else {
                const fallback = allRoomIds.filter(r => r !== winnerRoomId);
                randomRoomId = fallback[Math.floor(Math.random() * fallback.length)];
            }
            
            const moveResult = await moveLoserToRandomRoom(loserUser.id, randomRoomId);
            if (!moveResult) {
                console.error('Failed to move loser to', randomRoomId);
                alert('No se pudo mover al perdedor. Revisa la consola para más detalles.');
            } else {
                console.log('Loser moved to', randomRoomId, moveResult);
                
                // Si el perdedor es el jugador del turno actual, quitar todos los puntos de acción
                if (loserUser.id === match.currentTurnUserId) {
                    try {
                        await fetch(`/api/v1/matches/${matchId}/consume-all-action-points/${loserUser.id}`, {
                            method: 'POST',
                            headers: {
                                'Authorization': `Bearer ${jwt}`,
                                'Content-Type': 'application/json',
                            }
                        });
                        console.log('All action points consumed for loser:', loserUser.id);
                    } catch (err) {
                        console.error('Error consuming all action points for loser:', err);
                    }
                }
            }

            // Si ganó el atacante, muévelo a la antigua sala del defensor
            if (attackerWins && defenderRoomId) {
                const moveWinner = await movePlayerToRoom(fightAttacker?.user?.id, defenderRoomId);
                if (!moveWinner) {
                    console.error('Failed to move attacker (winner) to defender room', defenderRoomId);
                } else {
                    console.log('Attacker moved to defender room', defenderRoomId, moveWinner);

                }
            }

            const winnerPlayerId = attackerWins ? fightAttacker?.id : fightDefender?.id;
            const loserPlayerId = attackerWins ? fightDefender?.id : fightAttacker?.id;
            if (winnerPlayerId) {
                const drawSuccess = await drawCardForWinner(winnerPlayerId);
                if (drawSuccess) {
                    console.log('Ganador robó una carta');
                    
                    // Publicar evento de resolución de pelea para que todos lo reciban
                    await fetch(`/api/v1/matches/${matchId}/notify-fight-resolved`, {
                        method: 'POST',
                        headers: {
                            'Authorization': `Bearer ${jwt}`,
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            matchId: matchId,
                            winnerId: winnerUser?.id,
                            winnerPlayerId: winnerPlayerId,
                            loserPlayerId: loserPlayerId
                        })
                    }).catch(e => console.warn('Could not notify fight resolved:', e));

                    // Abrir modal de robo si es el usuario actual el ganador
                    if (currentUser.id === fightAttacker?.user?.id && attackerWins) {
                        setStealLoserPlayerId(loserPlayerId);
                        setIsStealModalOpen(true);
                    } else if (currentUser.id === fightDefender?.user?.id && !attackerWins) {
                        setStealLoserPlayerId(loserPlayerId);
                        setIsStealModalOpen(true);
                    }
                } else {
                    console.log('No se pudo robar una carta para el ganador');
                }
            }

        } finally {
            setIsFightModalOpen(false);
            setFightDefender(null);
            setFightAttacker(null);
            setPendingTargetRoom(null);
            cleanVotingStates();
        }
    }


return (
    <div className={`match-container ${isSpectator ? 'spectator-mode' : ''}`}>
        {/* Modal donde se tiran los dados nada más empezar la partida para elegir el orden de los turnos */}
        <StartDiceModal
            isOpen={!isSpectator && isDiceModalOpen && (match?.turnNumber === 0 || match?.currentTurnPhase === null)}
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
                <div className={`current-player ${isSpectator ? 'spectator-hidden' : ''}`}>
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
                isSpectator={isSpectator}
                playersList={playersList}
                otherPlayersBags={otherPlayersBags}
            />
        </div>

        {moveNpcMode && !isSpectator && match?.currentTurnUserId === currentUser?.id && (
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

        <div className={`player-section ${isSpectator ? 'spectator-hidden' : ''}`}>
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
                    <button className={`leave-match-button${isSpectator ? 'spectator-hidden' : ''}`}
                        onClick={() => setDiscardPhaseOpen(true)}
                        disabled={match.currentTurnUserId !== currentUser.id}
                        title="Discard cards from hand"
                    >
                        Discard and bag
                    </button>
                    <button className={`leave-match-button ${isSpectator ? 'spectator-hidden' : ''}`}
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
                        style={{ marginLeft: '15px', background: '#e74c3c', color: 'white', border: 'none', padding: '8px 12px', borderRadius: '6px', cursor: 'pointer', marginTop: isSpectator ? '20px' : undefined }}
                    >
                        Leave Match
                    </button>
                    <button
                        className="end-match-button"
                        onClick={endMatch}
                        style={{ display: (isSpectator || match?.creatorId !== currentUser?.id) ? 'none' : 'block' }}
                    >
                        End match
                    </button>
                    <div className={`match-chat-icon ${isSpectator ? 'spectator-hidden' : ''}`}>
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
