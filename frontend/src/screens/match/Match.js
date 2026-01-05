import React, { useState, useEffect, useRef } from "react"
import {useNavigate} from "react-router-dom";
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import '../../static/css/match/Match.css';
import getIdFromUrl from '../../util/getIdFromUrl'
import useFetchState from "../../util/useFetchState";
import tokenService from "../../services/token.service";
import BagModal from "./BagModal";
import DiscardHandModal from "./DiscardHandModal";
import ActionsModal from "./ActionsModal";
import ChatBox from "./chatBox";
import { FaComments } from "react-icons/fa";
import FightModal from "./FightModal";
import StartDiceModal from "./StartDiceModal";



const jwt = tokenService.getLocalAccessToken();
const currentUser = tokenService.getUser();


export default function Match(){
    const matchId = getIdFromUrl(2);
    const navigate = useNavigate();
    const [currentPlayer, setCurrentPlayer] = useState({}) // el jugador asociado al usuario que está "viendo" la pantalla
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
    const [numCardsDrawn, setNumCardsDrawn] = useState(0)
    const [bagOpen, setBagOpen] = useState(false)
    const [discardHandOpen, setDiscardHandOpen] = useState(false)

    // DADOS 
    const [whiteDice, setWhiteDice] = useState("1")
    const [blackDice, setBlackDice] = useState("1")

    const [chatOpen, setChatOpen] = useState(false)
    const [actionPoints, setActionPoints] = useState(0)
    const [strength, setStrength] = useState(1)
    const [moveToAdyacentRoom, setMoveToAdyacentRoom] = useState(false)

    
    // const [playerTurnId, setPlayerTurnId] = useState(null)

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    const [isDiceModalOpen, setIsDiceModalOpen] = useState(true);
    const [isActionsModalOpen, setIsActionsModalOpen] = useState(false);
    const [isFightModalOpen, setIsFightModalOpen] = useState(false);
    const [fightDefender, setFightDefender] = useState(null);
    const [fightAttacker, setFightAttacker] = useState(null);
    const [pendingTargetRoom, setPendingTargetRoom] = useState(null);
    
    // Función para obtener un color único para cada jugador
    const getPlayerColor = (playerId) => {
        const colors = ['#FF6B6B', '#4ECDC4', '#f833e4ff', '#e5541aff', '#52a852ff', '#2a15ceff'];
        const allPlayers = match?.players || [];
        const playerIndex = allPlayers.findIndex(p => p.id === playerId);
        return colors[playerIndex % colors.length];
    };
    
    // const posiciones de las habitaciones en el mapa 
    const roomPositions = {
        1: { left: '26%', top: '12%' },   // North Tower v
        2: { left: '35.5%', top: '15%' },   // Caesar Room v
        3: { left: '44.5%', top: '9%' },   // Opal Room v
        4: { left: '56%', top: '8%' },   // Coral Room v 
        5: { left: '64.5%', top: '15%' },   // Roof v
        6: { left: '74%', top: '13%' },   // East Tower v 
        7: { left: '32%', top: '27.5%' },   // Corridor 1 v
        8: { left: '44.5%', top: '23.5%' },   // Cafe v
        9: { left: '50%', top: '21%' },   // Corridor 2 v
        10: { left: '50%', top: '21%' },  // Corridor 2 (dup) v
        11: { left: '55.5%', top: '23.5%' },  // Parlor v
        12: { left: '68%', top: '27.5%' },  // Corridor 3 v 
        13: { left: '26.5%', top: '41%' },  // Ball Room v
        14: { left: '32.5%', top: '40%' },  // Corridor 4 v
        15: { left: '38.5%', top: '41%' },  // SPA v 
        16: { left: '61.5%', top: '41%' },  // Pool v
        17: { left: '67.5%', top: '41%' },  // Corridor 5 v
        18: { left: '73.5%', top: '41%' },  // Sleep Room v
        19: { left: '26.5%', top: '58%' },  // Class Room v
        20: { left: '32.5%', top: '58%' },  // Corridor 6 v
        21: { left: '38.5%', top: '58%' },  // Arbor v
        22: { left: '61.5%', top: '58%' },  // Farm v
        23: { left: '67.5%', top: '58%' },  // Corridor 7 v
        24: { left: '73.5%', top: '58%' },  // Meal Room v
        25: { left: '32%', top: '72%' },  // Corridor 8 v
        26: { left: '44.5%', top: '76%' },  // Bar v
        27: { left: '50%', top: '78%' },  // Corridor 9 v
        28: { left: '50%', top: '78%' },  // Corridor 9 (dup) v
        29: { left: '55.5%', top: '76%' },  // Lab v
        30: { left: '68%', top: '72%' },  // Corridor 10 v
        31: { left: '26%', top: '87%' },  // West Tower v
        32: { left: '35.5%', top: '85%' },  // Cellar v
        33: { left: '44.5%', top: '92%' },  // Apple Room v
        34: { left: '56%', top: '92%' },  // Map Room v 
        35: { left: '64.5%', top: '85%' },  // Parole Room v
        36: { left: '74%', top: '87%' },  // South Tower v
        37: { left: '50%', top: '50%' },  // Safe Area v
    };
    // CARGAR DATOS PARTIDA 

    // CARGAR DATOS JUGADORES 
   
    

    useEffect(() => {
        fetchMatchAndPlayers()
    }, [matchId])
    
    // Inicializar conexión WebSocket para actualizaciones de turno
    useEffect(() => {
        const client = new Client({
            brokerURL: 'ws://localhost:8080/ws',
            connectHeaders: { 'Authorization': `Bearer ${jwt}` },
            onConnect: () => setStompClient(client)
        });

        client.activate();
        return () => client.active && client.deactivate();
    }, [jwt]);

    // Suscribirse a las actualizaciones de turno
    useEffect(() => {
        if (!stompClient || !stompClient.active) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.turn`, (msg) => {
            const turnUpdate = JSON.parse(msg.body);
            setCurrentTurnUserId(turnUpdate.currentTurnUserId);
            fetchMatchAndPlayers();
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId]);

    // Suscribirse a las actualizaciones de combate
    useEffect(() => {
        if (!stompClient || !stompClient.active) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.fight`, (msg) => {
            const fightUpdate = JSON.parse(msg.body);
            
            if (fightUpdate.action === 'START') {
                // Identificar al atacante (quien inicia la batalla)
                const attacker = match?.players?.find(p => p.user.id === fightUpdate.attackerId);
                
                // Identificar al defensor (quien está en la habitación)
                const defender = match?.players?.find(p => p.user.id === fightUpdate.defenderId);
                
                if (attacker && defender) {
                    setFightAttacker(attacker);
                    setFightDefender(defender);
                    setPendingTargetRoom(fightUpdate.roomName);
                    setIsFightModalOpen(true);
                }
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, match, currentUser]);


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

    useEffect(() => {
        if (!stompClient || !stompClient.active || !currentPlayer[0]) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.actionPoints`, (msg) => {
            const actionPointsUpdate = JSON.parse(msg.body);
            
            // Only update action points if the update is for the current player
            if (actionPointsUpdate.userId === currentPlayer[0].user.id) {
                setActionPoints(actionPointsUpdate.actionPoints);
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, currentPlayer]);

    useEffect(() => {
        if (!stompClient || !stompClient.active || !currentPlayer[0]) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.strength`, (msg) => {
            const strengthUpdate = JSON.parse(msg.body);
            
            // Only update strength if the update is for the current player
            if (strengthUpdate.userId === currentPlayer[0].user.id) {
                setStrength(Math.min(6, strengthUpdate.strength));
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, currentPlayer]);

    useEffect(() => {
            if (player && Array.isArray(player)){
                setPlayersList(player.filter(p => p.user.id !== currentUser?.id))
                setCurrentPlayer(player.filter(p => p.user.id === currentUser?.id))
                
            }
    }, [match])

    useEffect(() => {
        if (Array.isArray(currentPlayer) && currentPlayer[0]?.id){
            fetchCards()
            setStrength(Math.min(6, currentPlayer[0].strength))
        }     
    }, [currentPlayer])

    useEffect(() => {
        calculateActionPoints()
    }, [handCards])



    useEffect(() => {
        if (currentTurnUserId && currentPlayer[0].user.id === currentTurnUserId){
            fetchActionPoints()
        }
    }, [currentTurnUserId])

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

    

    // console.log('currentPlayer', currentPlayer)
    const fetchActionPoints = async () => {
        try {
            const response = await fetch(`/api/v1/matches/${matchId}/${currentPlayer[0].id}/actionPoints`, {
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

                setMatch(data)
                setPlayer(data.players)
                

            } catch (error) {
                
            }
    }


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
            console.log('movePlayerToRoom success', data);
            setMatch(data);
            if (data.players) setPlayer(data.players);
            return data;
        } catch (err) {
            console.error('Error moving player:', err);
            return null;
        }
    }

    // iNICIALIZAR BARAJA
    const fetchCards = async () => {
        try {
            console.log('ENTRA EN EL FETCHCARDS')
            const response = await fetch(`/api/v1/matches/${matchId}/${currentPlayer[0].id}/getAllCards`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: 'application/json',
                'Content-Type': 'application/json',
            },
            })

            if (response.ok){
                const data = await response.json()
                console.log('datos fetch cards' , data)
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

    /*
    console.log('hand' , handCards)
    console.log('bag' , bagCards)
    console.log('deck' , deck)
    */

    // FUNCION ROBAR CARTA
    const drawCard = async () => { // TODO: CAMBIAR EL FORMATO Y ESTRUCTURA, ESTA SACADO DE CHATI PQ QUERIA SOLO PROBARLO
        try {
            const response = await fetch(`/api/v1/matches/${matchId}/${currentPlayer[0].id}/drawCardFromDeck`, {
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
            
            setDeck(data.deck)
            setHandCards(prev => [...prev, data.card])
            setNumCardsDrawn(prev => prev + 1)
            
        } catch (error) {
            console.log('error', error)
            
        }    

    }

    
    const drawCardForWinner = async (winnerId) => {
        try {
            const response = await fetch(`/api/v1/matches/${matchId}/${winnerId}/drawCardFromDeck`, {
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
            
            if (winnerId === currentPlayer[0]?.id) {
                setDeck(data.deck)
                setHandCards(prev => [...prev, data.card])
            }
            
            return true
        } catch (error) {
            console.log('Error al robar carta para el ganador:', error)
            return false
        }
    }



    const move = async (roomId) => {
        console.log('roomId', roomId)
        if (moveToAdyacentRoom===false) return ;
        if (moveToAdyacentRoom === true){ //TODO: Comprobar antes de todo si son adyacentes 
            try {
                const isSafeArea = roomId === 37;
                
                const otherPlayer = match?.players?.find(p => p.user?.id !== currentUser?.id && (
                    (p.currentRoom && p.currentRoom.id === roomId) ||
                    (p.roomId && p.roomId === roomId) ||
                    (p.room && p.room.id === roomId)
                ));

                if (otherPlayer && !isSafeArea) {
                    setPendingTargetRoom(roomId);
                    setFightDefender(otherPlayer);
                    setIsFightModalOpen(true);
                    setMoveToAdyacentRoom(false);
                    
                    await fetch(`/api/v1/matches/${matchId}/notify-fight`, {
                        method: 'POST',
                        headers: {
                            'Authorization': `Bearer ${jwt}`,
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            matchId: matchId,
                            attackerId: currentUser.id,
                            attackerUsername: currentUser.username,
                            defenderId: otherPlayer.user.id,
                            defenderUsername: otherPlayer.user.username,
                            roomId: roomId,
                            action: 'START'
                        })
                    });
                    
                    return;
                }

                {console.log(roomId)}
                const response = await fetch (`/api/v1/matches/${matchId}/move`, {
                method: "PUT",
                headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: 'application/json',
                'Content-Type': 'application/json',
                }, body: JSON.stringify({
                    userId: currentUser.id,
                    roomId: roomId
                }) 

                })

                if (response.ok){
                    const data = await response.json()
                    setMatch(data)
                    
                    const movedPlayer = data.players.find(p => p.user.id === currentUser.id);
                    if (movedPlayer) {
                        await fetch(`/api/v1/matches/${matchId}/notify-action-points`, {
                            method: 'POST',
                            headers: {
                                'Authorization': `Bearer ${jwt}`,
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify({
                                matchId: matchId,
                                userId: currentUser.id,
                                actionPoints: movedPlayer.actionPoints
                            })
                        }).catch(err => console.error('Error notifying action points:', err));
                        
                        setActionPoints(movedPlayer.actionPoints);
                    }
                    
                    setMoveToAdyacentRoom(false)
                }

                else if (!response.ok) {
                    setMoveToAdyacentRoom(false)
                    throw new Error(`Error ${response.status}: ${response.statusText}`)
                }
            } catch (error) {
                console.log('error', error)
            }
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
            console.log('movePlayerToRoom success', data);
            setMatch(data);
            if (data.players) {
                setPlayer(data.players);
                
                const movedPlayer = data.players.find(p => p.user.id === userId);
                if (movedPlayer) {
                    await fetch(`/api/v1/matches/${matchId}/notify-action-points`, {
                        method: 'POST',
                        headers: {
                            'Authorization': `Bearer ${jwt}`,
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            matchId: matchId,
                            userId: userId,
                            actionPoints: movedPlayer.actionPoints
                        })
                    }).catch(err => console.error('Error notifying action points:', err));
                }
            }
            return data;
        } catch (err) {
            console.error('Error moving player:', err);
            return null;
        }
    }



    
    const endMatch = () => {
        if (!window.confirm("¿Seguro que quieres finalizar la partida?")) return; 
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
        //.then(() => window.location.reload())
        .then(updated => {
            console.log("Match finalizado:", updated)
            setMatch(updated)
        })
        .catch(err => console.error(err))
    }

    

    const currentPlayerTurn = match?.players.find(p => p.user.id === match.currentTurnUserId);

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
    };

    console.log('match', match)


    if (match?.status === "FINISHED") {
        return (
            <div className="match-ended">
                <div className="end-overlay">
                <div className="end-text-box">
                    <h2>La partida ha finalizado!!!!!</h2>
                    <p>Gracias por jugar.</p>
                    <button className="return-menu-button" onClick={() => navigate(`/`)}>Return to main menu</button>
                </div>
                </div>
            </div>
        );
    }

if (!match) {
    return <div>Cargando partida...</div>;
}

console.log('cards', handCards)

return (
        <div className="match-container">
            <div className="players-avatars-section">
                {playersList.map((p) => (
                    <div key={p.user.id} className="player-avatar-card">
                        <div style={{
                            borderRadius: '50%',
                            border: `4px solid ${getPlayerColor(p.id)}`,
                            display: 'inline-block',
                            padding: '3px'
                        }}>
                            {p.user.avatar ? (
                                <img src={p.user.avatar} alt={`${p.user.username} avatar`} className="player-avatar-img" style={{ borderRadius: '50%' }} />
                            ) : <img src="/Avatar_default.png" alt="Default avatar" className="player-avatar-img" style={{ borderRadius: '50%' }} />}
                        </div>
                        <p className="player-username">{p.user.username}</p>
                    </div>
                ))}
            </div>

            <StartDiceModal 
                isOpen={match?.currentTurnPhase === null && isDiceModalOpen}
                onClose={() => setIsDiceModalOpen(false)}
                onDiceRolled={handleDiceRolled}
                matchData={match}
            />
            
            <div className="match-board">
                <div className="deck-column">
                    <div className="deck-section">
                        <button 
                            onClick={ () => {
                                if (numCardsDrawn < 7) {
                                    drawCard()
                                } else {
                                    alert("No puedes robar más de 7 cartas")
                                } 
                            }}
                            disabled={!canDraw}
                            style={{ 
                                border: "none", 
                                background: "transparent", 
                                padding: 0, 
                                cursor: !canDraw ? "not-allowed" : "pointer", 
                                opacity: !canDraw ? 0.4 : 1,
                                outline: "none",
                            }}
                        >
                            <img 
                                src="/backCard.png" 
                                alt="Robar carta"
                                style={{ width: "150px", height: "auto", outline: "none", }}
                            />
                        </button>
                    </div>
                    <div className="discard-pile-section">
                        {discardPile.length > 0 ? (
                            <img 
                                src={`/resources${discardPile[discardPile.length - 1].frontImage}`} 
                                alt="Última carta descartada"
                                style={{ width: "150px", height: "auto" }}
                            />
                        ) : (
                            <div style={{ 
                                width: "150px", 
                                height: "210px", 
                                border: "2px dashed #ccc", 
                                borderRadius: "8px",
                                display: "flex",
                                alignItems: "center",
                                justifyContent: "center",
                                color: "#999"
                            }}>
                                Empty
                            </div>
                        )}
                    </div>
                </div>

                <div className="map-column" style={{ position: 'relative' }}>
                    <map name="Map">
                <area className="Area" href="#" target="" alt="Safe Area" title="Safe Area" coords="321,251,84" shape="circle" onClick={(e)=>{e.preventDefault(); move(37)}}/>
                <area className="Area" href="#" target="" alt="West Tower" title="West Tower" coords="13,489,98,388" shape="rect" onClick={(e)=>{e.preventDefault(); move(31)}}/>
                <area className="Area" href="#" target="" alt="South Tower" title="South Tower" coords="541,389,628,488" shape="rect" onClick={(e)=>{e.preventDefault(); move(36)}}/>
                <area className="Area" href="#" target="" alt="North Tower" title="North Tower" coords="13,12,99,113" shape="rect" onClick={(e)=>{e.preventDefault(); move(1)}}/>
                <area className="Area" href="#" target="" alt="East Tower" title="East Tower" coords="542,11,626,111" shape="rect" onClick={(e)=>{e.preventDefault(); move(6)}}/>
                <area className="Area" href="#" target="" alt="Caesar Room" title="Caesar Room" coords="110,40,210,114" shape="rect" onClick={(e)=>{e.preventDefault();move(2)}}/>
                <area className="Area" href="#" target="" alt="Opal Room" title="Opal Room" coords="220,10,292,69" shape="rect" onClick={(e)=>{e.preventDefault(); move(3)}}/>
                <area className="Area" href="#" target="" alt="Coral Room" title="Coral Room" coords="345,11,418,69" shape="rect" onClick={(e)=>{e.preventDefault(); move(4)}}/>
                <area className="Area" href="#" target="" alt="Roof" title="Roof" coords="429,38,530,112" shape="rect" onClick={(e)=>{e.preventDefault(); move(5)}}/>
                <area className="Area" href="#" target="" alt="Cafe" title="Cafe" coords="293,154,221,80" shape="rect" onClick={(e)=>{e.preventDefault(); move(8)}}/>
                <area className="Area" href="#" target="" alt="Parlor" title="Parlor" coords="345,81,417,152" shape="rect" onClick={(e)=>{e.preventDefault(); move(11)}}/>
                <area className="Area" href="#" target="" alt="Pool" title="Pool" coords="369,165,488,166,488,251,419,251,407,206" shape="poly" onClick={(e)=>{e.preventDefault(); move(16)}}/>
                <area className="Area" href="#" target="" alt="SPA" title="SPA" coords="271,166,237,197,221,236,153,237,152,165" shape="poly" onClick={(e)=>{e.preventDefault(); move(15)}}/>
                <area className="Area" href="#" target="" alt="Arbor" title="Arbor" coords="151,248,151,334,266,334,236,299,221,250" shape="poly" onClick={(e)=>{e.preventDefault(); move(21)}}/>
                <area className="Area" href="#" target="" alt="Farm" title="Farm" coords="488,264,488,334,371,334,403,296,416,265" shape="poly" onClick={(e)=>{e.preventDefault(); move(22)}}/>
                <area className="Area" href="" target="" alt="Ball Room" title="Ball Room" coords="25,166,98,251" shape="rect" onClick={(e)=>{e.preventDefault(); move(13)}}/>
                <area className="Area" href="" target="" alt="Sleep Room" title="Sleep Room" coords="540,165,614,238" shape="rect" onClick={(e)=>{e.preventDefault(); move(18)}} />
                <area className="Area" href="" target="" alt="Class Room" title="Class Room" coords="25,263,97,334" shape="rect" onClick={(e)=>{e.preventDefault(); move(19)}}/>
                <area className="Area" href="" target="" alt="Meal Room" title="Meal Room" coords="541,249,613,335" shape="rect" onClick={(e)=>{e.preventDefault(); move(24)}}/>
                <area className="Area" href="" target="" alt="Bar" title="Bar" coords="221,346,292,417" shape="rect" onClick={(e)=>{e.preventDefault(); move(26)}}/>
                <area className="Area" href="" target="" alt="Lab" title="Lab" coords="346,346,418,418" shape="rect" onClick={(e)=>{e.preventDefault(); move(29)}}/>
                <area className="Area" href="" target="" alt="Cellar" title="Cellar" coords="109,387,209,460" shape="rect" onClick={(e)=>{e.preventDefault(); move(32)}}/>
                <area className="Area" href="" target="" alt="Apple Room" title="Apple Room" coords="221,430,293,488" shape="rect" onClick={(e)=>{e.preventDefault(); move(33)}}/>
                <area className="Area" href="" target="" alt="Parole Room" title="Parole Room" coords="429,387,529,459" shape="rect" onClick={(e)=>{e.preventDefault(); move(35)}}/>
                <area className="Area" href="" target="" alt="Map Room" title="Map Room" coords="345,430,419,490" shape="rect" onClick={(e)=>{e.preventDefault(); move(34)}}/>
                <area className="Area" href="" target="" alt="Corridor 1" title="Corridor 1" coords="25,123,209,153" shape="rect" onClick={(e)=>{e.preventDefault(); move(7)}}/>
                <area className="Area" href="" target="" alt="Corridor 2" title="Corridor 2" coords="304,57,335,155" shape="rect" onClick={(e)=>{e.preventDefault(); move(9)}}/>
                <area className="Area" href="" target="" alt="Corridor 3" title="Corridor 3" coords="430,122,613,154" shape="rect" onClick={(e)=>{e.preventDefault(); move(12)}}/>
                <area className="Area" href="" target="" alt="Corridor 4" title="Corridor 4" coords="109,164,141,250" shape="rect" onClick={(e)=>{e.preventDefault(); move(14)}}/>
                <area className="Area" href="" target="" alt="Corridor 5" title="Corridor 5" coords="500,164,529,238" shape="rect" onClick={(e)=>{e.preventDefault(); move(17)}}/>
                <area className="Area" href="" target="" alt="Corridor 6" title="Corridor 6" coords="109,262,141,334" shape="rect" onClick={(e)=>{e.preventDefault(); move(20)}}/>
                <area className="Area" href="" target="" alt="Corridor 7" title="Corridor 7" coords="500,248,529,333" shape="rect" onClick={(e)=>{e.preventDefault(); move(23)}}/>
                <area className="Area" href="" target="" alt="Corridor 8" title="Corridor 8" coords="25,345,209,376" shape="rect" onClick={(e)=>{e.preventDefault(); move(25)}}/>
                <area className="Area" href="" target="" alt="Corridor 9" title="Corridor 9" coords="304,345,335,441" shape="rect" onClick={(e)=>{e.preventDefault(); move(27)}}/>
                <area className="Area" href="" target="" alt="Corridor 10" title="Corridor 10" coords="429,346,613,376" shape="rect" onClick={(e)=>{e.preventDefault(); move(30)}}/>
                    </map>
                    <img src="/ElbaBoard.png" useMap="#Map" className="Map"/>
                    
                    {/* Fichas de jugadores sobre el mapa */}
                    {match?.players.map(player => {
                        if (!player.currentRoom) return null;                      
                        const position = roomPositions[player.currentRoom.id];
                        if (!position) return null;
                        return (
                            <img 
                                key={player.id}
                                src={player.user.avatar || "/Avatar_default.png"}
                                alt={player.user.username}
                                title={player.user.username}
                                style={{
                                    position: 'absolute',
                                    left: position.left,
                                    top: position.top,
                                    transform: 'translate(-50%, -50%)',
                                    width: '30px',
                                    height: '30px',
                                    borderRadius: '50%',
                                    border: currentPlayer[0]?.id === player.id ? '3px solid yellow' : `3px solid ${getPlayerColor(player.id)}`,
                                    boxShadow: '0 2px 4px rgba(0,0,0,0.5)',
                                    zIndex: 10,
                                    pointerEvents: 'none'
                                }}
                            />
                        );
                    })}
                    
                    {/* Fichas de NPCs sobre el mapa */}
                    {match?.npcs.map((npc, index) => {
                        if (!npc.room) return null;                        
                        const position = roomPositions[npc.room.id];
                        if (!position) return null;
                        return (
                            <div
                                key={`npc-${index}`}
                                title={npc.name || `NPC ${index+1}`}
                                style={{
                                    position: 'absolute',
                                    left: position.left,
                                    top: position.top,
                                    transform: 'translate(-50%, -50%)',
                                    width: '25px',
                                    height: '25px',
                                    borderRadius: '50%',
                                    backgroundColor: npc.isNiallCampbell ? '#ff0000' : '#666',
                                    border: '2px solid white',
                                    boxShadow: '0 2px 4px rgba(0,0,0,0.5)',
                                    zIndex: 10,
                                    pointerEvents: 'none',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    color: 'white',
                                    fontSize: '10px',
                                    fontWeight: 'bold'
                                }}
                            >
                                {npc.isNiallCampbell ? 'N' : 'X'}
                            </div>
                        );
                    })}
                </div>
            </div>
            <div className="points-section">
                <div className="action-points">
                    <h1>{actionPoints}</h1>
                    <p>Action points </p>
                </div>

                <div className="action-points">
                    <h1>{strength}</h1>
                    <p>strength </p>
                </div>
            </div>
            

            {/* TABLA DE JUGADORES Y NPCS 
            <div
            className="entities-panel"
            style={{
                position: 'absolute',
                top: '80%',
                right: '30px',
                transform: 'translateY(-50%)',
                width: '320px',
                backgroundColor:  '#c0392b',
                color: 'white',
                borderRadius: '8px',
                padding: '10px',
                fontSize: '14px',
                zIndex: 1000,
                boxShadow: '0 0 10px rgba(0,0,0,0.3)'
            }}
            >
            <h4 style={{ textAlign: 'center', margin: '5px 0' }}>Ubicación de Jugadores y NPCs</h4>
                <table style={{ width: '100%', textAlign: 'center', borderCollapse: 'collapse' }}>
                    <thead>
                        <tr>
                            <th style={{ borderBottom: '1px solid #fff', padding: '3px' }}>Nombre</th>
                            <th style={{ borderBottom: '1px solid #fff', padding: '3px' }}>Tipo</th>
                            <th style={{ borderBottom: '1px solid #fff', padding: '3px' }}>Habitación</th>
                        </tr>
                    </thead>
                    <tbody>
                        {match?.players.map(player => (
                            <tr 
                                key={player.id} 
                                style={{
                                    backgroundColor: '#ff4c4cff', // Color de fondo para jugadores
                                    color: 'white',
                                    fontWeight: currentPlayer[0]?.id === player.id ? 'bold' : 'normal' // Resalta tu jugador
                                }}
                            >
                                <td style={{ padding: '3px' }}>{player.user.username}</td>
                                <td style={{ padding: '3px' }}>Jugador</td>
                                <td style={{ padding: '3px' }}>{player.currentRoom?.name}</td>
                            </tr>
                        ))}
                        {match?.npcs.map((npc, index) => (
                            <tr 
                                key={index}
                                style={{
                                    backgroundColor: '#f87575ff',
                                    color: 'white'
                                }}
                            >
                                <td style={{ padding: '3px' }}>{npc.name || `NPC ${index+1}`}{npc.isNiallCampbell ? ' (Niall)' : ''}</td>
                                <td style={{ padding: '3px' }}>NPC</td>
                                <td style={{ padding: '3px' }}>{npc.room?.name}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            */}
            <div className="player-section">
                <div className="player-hand">
                    {Array.isArray(handCards) && handCards.map((carta, index) => (
                                    <div key={index} >
                                        <img src={`/resources${carta.frontImage}`} alt={`Carta ${carta.letter}`} className="card"/>
                                    </div>
                    ))}
                </div>
                <div className="player-bag">
                    {Array.isArray(bagCards) && bagCards.map((carta, index) => (
                                    <div key={index} >
                                        <img src={`/resources${carta.frontImage}`} alt={`Carta ${carta.letter}`} className="card"/>
                                    </div>
                    ))}

                </div>
                
            </div>
            <div>
                <button className="bag-button"
                    onClick={() => setBagOpen(true)}
                    disabled={
                    match.currentTurnUserId !== currentUser.id || actionPoints > 0 }
                    title="Accede to your bag"
                >
                    Form my bag
                </button>
                <button className="bag-button"
                    onClick={() => setDiscardHandOpen(true)}
                    disabled={
                    match.currentTurnUserId !== currentUser.id || actionPoints > 0 }
                    title="Discard cards from hand"
                    style={{ marginLeft: "10px" }}
                >
                    Discard
                </button>
                 <button className="bag-button"
                    title="Discard cards from hand"
                    onClick={() => setIsActionsModalOpen(true) }
                     disabled={
                    match.currentTurnUserId !== currentUser.id || 
                    actionPoints <= 0 }
                    style={{ marginLeft: "15px" }}
                >
                    Actions
                </button>

                <ActionsModal
                isOpen={isActionsModalOpen}
                onClose={() => setIsActionsModalOpen(false)}
                moveToAdyacent={() => setMoveToAdyacentRoom(true) }
            />

    <FightModal
            isOpen={isFightModalOpen}
            onClose={() => { setIsFightModalOpen(false); setFightDefender(null); setFightAttacker(null); }}
            defender={fightDefender}
            attacker={fightAttacker}
            stompClient={stompClient}
            bagCards={bagCards}
            onResolve={async (currentUserWon) => {
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
                        return;
                    }

                    const defenderRoomId = fightDefender?.currentRoom?.id || fightDefender?.roomId || fightDefender?.room?.id || pendingTargetRoom;

                    // currentUserWon aquí representa si el atacante ganó (porque solo el atacante ejecuta esto)
                    const attackerWins = currentUserWon;
                    const loserUser = attackerWins ? fightDefender?.user : fightAttacker?.user;
                    const winnerUser = attackerWins ? fightAttacker?.user : fightDefender?.user;

                    // Si no hay perdedor identificado, no mover
                    if (!loserUser) {
                        setIsFightModalOpen(false);
                        setFightDefender(null);
                        setFightAttacker(null);
                        setPendingTargetRoom(null);
                        return;
                    }
                    const allRoomIds = [
                        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                        21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37
                    ];
                    const winnerPlayer = match?.players?.find(p => p.user?.id === (winnerUser?.id || winnerUser?.user?.id));
                    const winnerRoomId = winnerPlayer?.currentRoom?.id || winnerPlayer?.roomId || null;

                    const occupiedRoomIds = new Set();
                    (match?.players || []).forEach(p => {
                        const rid = p.currentRoom?.id || p.roomId || p.room?.id;
                        if (rid) occupiedRoomIds.add(rid);
                    });
                    (match?.npcs || []).forEach(npc => {
                        const rid = npc.room?.id;
                        if (rid) occupiedRoomIds.add(rid);
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

                    // TODO: Guardar información de la batalla para usar en estadisticas 
                    //try {
                    //    await fetch(`/api/v1/matches/${matchId}/fight`, {
                    //        method: 'PUT',
                    //        headers: {
                    //            Authorization: `Bearer ${jwt}`,
                    //            'Content-Type': 'application/json'
                    //        },
                    //        body: JSON.stringify({
                    //            attackerId: currentUser?.id,
                    //            defenderId: fightOpponent?.user?.id || fightOpponent?.id,
                    //            winnerId: null // TODO: set the winner id here once resolved by server logic
                    //        })
                    //    });
                    //} catch (e) {
                    //    console.debug('Could not record fight event', e);
                    //}
                    const moveResult = await moveLoserToRandomRoom(loserUser.id, randomRoomId);
                    if (!moveResult) {
                        console.error('Failed to move loser to', randomRoomId);
                        alert('No se pudo mover al perdedor. Revisa la consola para más detalles.');
                    } else {
                        console.log('Loser moved to', randomRoomId, moveResult);
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
                    if (winnerPlayerId) {
                        const drawSuccess = await drawCardForWinner(winnerPlayerId);
                        if (drawSuccess) {
                            console.log('Ganador robó una carta');
                        } else {
                            console.log('No se pudo robar una carta para el ganador');
                        }
                    }

                } finally {
                    setIsFightModalOpen(false);
                    setFightDefender(null);
                    setFightAttacker(null);
                    setPendingTargetRoom(null);
                }
            }}
    />

            </div>
            

                <button
                    className="end-match-button"
                    onClick={endMatch}
                    style={{
                        marginLeft: "10px",
                        marginTop: "20px",
                        padding: "10px 25px",
                        background: "#c0392b",
                        color: "white",
                        border: "none",
                        borderRadius: "8px",
                        cursor: "pointer"
                    }}
                >
                    Finalizar partida
                </button>

        <BagModal
            isVisible={bagOpen}
            hand={handCards}
            bag={bagCards}
            deck={deck}
            player={currentPlayer[0]}
            onClose={() => setBagOpen(false)}
            onSave={async () =>{
                await fetchCards()
                setBagOpen(false)

            }
                }
            />

        <DiscardHandModal
            isVisible={discardHandOpen}
            hand={handCards}
            bag={bagCards}
            deck={deck}
            player={currentPlayer[0]}
            onClose={() => setDiscardHandOpen(false)}
            updateCurrentTurnId={(newTurnId) => setCurrentTurnUserId(newTurnId)}
            onSave={async () =>{
                await fetchCards()
                setDiscardHandOpen(false)
            }}
            />

      
            <div className="match-chat-icon">
                <div className="chat-icon-button" onClick={() => setChatOpen(!chatOpen)}>
                    <FaComments size={30} color="white" />
                </div>
            </div>

            {chatOpen && <ChatBox matchId={matchId} />}

            {/* Mensaje de turno */}
            <div
                style={{
                        marginLeft: "050px",
                        position: "absolute",
                        left: "10%",                 
                        transform: "translateX(90%)",
                        marginTop: "-80px",
                        padding: "15px 35px", 
                        fontSize: "22px",
                        background: "#c0392b",
                        color: "white",
                        border: "none",
                        borderRadius: "8px",
                        maxWidth: "500px",
                        minWidth: "230px",
                        cursor: "pointer"
                }}
            >
                {!match?.currentTurnUserId
                ? "Esperando..."
                : match.currentTurnUserId === currentUser?.id
                    ? "Tu turno"
                    : `${match.players.find(p => p.user.id === match.currentTurnUserId)?.user.username} está en su turno`}

            </div>
        </div>
    );
}