import React, { useState } from "react"
import {useNavigate} from "react-router-dom";
import '../../static/css/match/Match.css';
import getIdFromUrl from '../../util/getIdFromUrl'
import { useEffect } from "react";
import useFetchState from "../../util/useFetchState";
import tokenService from "../../services/token.service";
import BagModal from "./BagModal";
import DiscardHandModal from "./DiscardHandModal";
import ActionsModal from "./ActionsModal";
import ChatBox from "./chatBox";
import { FaComments } from "react-icons/fa";



const jwt = tokenService.getLocalAccessToken();
const currentUser = tokenService.getUser();


export default function Match(){
    const matchId = getIdFromUrl(2);
    const navigate = useNavigate();
    const [currentPlayer, setCurrentPlayer] = useState({})
    const [player, setPlayer] = useState([])
    const [playersList, setPlayersList] = useState([])
    const [match, setMatch] = useState(null)
    const [currentTurnUserId, setCurrentTurnUserId] = useState(null)

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
    const [diceRolled, setDiceRolled] = useState(false)

    const [chatOpen, setChatOpen] = useState(false)
    const [actionPoints, setActionPoints] = useState(null)

    
    // const [playerTurnId, setPlayerTurnId] = useState(null)

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    const [isActionsModalOpen, setIsActionsModalOpen] = useState(false);


        
    // CARGAR DATOS PARTIDA 

    // CARGAR DATOS JUGADORES 
   
    

    useEffect(() => {
        fetchMatchAndPlayers()
    }, [matchId])

    useEffect(() => {
            if (player && Array.isArray(player)){
                setPlayersList(player.filter(p => p.user.id !== currentUser?.id))
                setCurrentPlayer(player.filter(p => p.user.id === currentUser?.id))
            }
    }, [match])

    useEffect(() => {
        if (Array.isArray(currentPlayer) && currentPlayer[0]?.id){
            fetchCards()
        }     
    }, [currentPlayer])

    useEffect(() => {
        calculateActionPoints()
          
    }, [handCards])


    const fetchMatchAndPlayers = async () => {
            try {
                const response = await fetch(`/api/v1/matches/${matchId}`, {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                        Accept: 'application/json',
                        'Content-Type': 'application/json',
                    },
                    })
                const data = await response.json()

                setMatch(data)
                setPlayer(data.players)
                
                
            } catch (error) {
                
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



    // Función que genera el número del dado y actualiza la UI
    const rollDice = (diceType) => {
        const rollWhite = Math.floor(Math.random() * 6) + 1;
        setWhiteDice(rollWhite.toString());
        const rollBlack = Math.floor(Math.random() * 6) + 1;
        setBlackDice(rollBlack.toString());

        return [rollWhite, rollBlack]; // Devuelve el número generado
    };

    // Función que envía la tirada al backend y actualiza el match
    const submitDiceToBackend = (roll) => {
        fetch(`/api/v1/matches/${matchId}/submit-dice?userId=${currentUser.id}&diceRoll=${roll}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${jwt}`,
                'Content-Type': 'application/json',
            },
        })
        .then(async res => {
            if (!res.ok) {
                let errorBody;
                try {
                    errorBody = await res.json();
                } catch {
                    errorBody = await res.text();
                }
                throw new Error(`Error al enviar tirada de dado: ${res.status} ${res.statusText} - ${JSON.stringify(errorBody)}`);
            }
            return res.json();
        })
        .then(updatedMatch => {
            console.log("Match actualizado tras tirar dado:", updatedMatch);
            setMatch(updatedMatch)
            
            setCurrentTurnUserId(updatedMatch.currentTurnUserId)

            if (updatedMatch.players) {
                setPlayer(updatedMatch.players)
            }

        })
        .catch(err => console.error(err));
    };

    const throwDice = () => {
    if (diceRolled) return; // Evitamos tirar más de una vez

        const [white,black] = rollDice();
        submitDiceToBackend(white+black);
        setDiceRolled(true); // Marcamos que ya tiró
    };




    
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
        if (handCards.length > 7 ){
            setActionPoints(0)
        } else {
            setActionPoints(7-handCards.length)
        }
        }


    


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



return (
        <div className="match-container">
            <div className="players-avatars-section">
                {playersList.map((p) => (
                    <div key={p.user.id} className="player-avatar-card">
                        {p.user.avatar ? (
                            <img src={p.user.avatar} alt={`${p.user.username} avatar`} className="player-avatar-img" />
                        ) : <img src="/Avatar_default.png" alt="Default avatar" className="player-avatar-img" />}
                        <p className="player-username">{p.user.username}</p>
                    </div>
                ))}
            </div>
            
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

                <div className="map-column">
                    <map name="Map">
                <area className="Area" href="" target="" alt="Safe Area" title="Safe Area" coords="321,251,84" shape="circle"/>
                <area className="Area" href="" target="" alt="West Tower" title="West Tower" coords="13,489,98,388" shape="rect"/>
                <area className="Area" href="" target="" alt="South Tower" title="South Tower" coords="541,389,628,488" shape="rect"/>
                <area className="Area" href="" target="" alt="North Tower" title="North Tower" coords="13,12,99,113" shape="rect"/>
                <area className="Area" href="" target="" alt="East Tower" title="East Tower" coords="542,11,626,111" shape="rect"/>
                <area className="Area" href="" target="" alt="Caesar Room" title="Caesar Room" coords="110,40,210,114" shape="rect"/>
                <area className="Area" href="" target="" alt="Opal Room" title="Opal Room" coords="220,10,292,69" shape="rect"/>
                <area className="Area" href="" target="" alt="Coral Room" title="Coral Room" coords="345,11,418,69" shape="rect"/>
                <area className="Area" href="" target="" alt="Roof" title="Roof" coords="429,38,530,112" shape="rect"/>
                <area className="Area" href="" target="" alt="Cafe" title="Cafe" coords="293,154,221,80" shape="rect"/>
                <area className="Area" href="" target="" alt="Parlor" title="Parlor" coords="345,81,417,152" shape="rect"/>
                <area className="Area" href="" target="" alt="Pool" title="Pool" coords="369,165,488,166,488,251,419,251,407,206" shape="poly"/>
                <area className="Area" href="" target="" alt="SPA" title="SPA" coords="271,166,237,197,221,236,153,237,152,165" shape="poly"/>
                <area className="Area" href="" target="" alt="Arbor" title="Arbor" coords="151,248,151,334,266,334,236,299,221,250" shape="poly"/>
                <area className="Area" href="" target="" alt="Farm" title="Farm" coords="488,264,488,334,371,334,403,296,416,265" shape="poly"/>
                <area className="Area" href="" target="" alt="Ball Room" title="Ball Room" coords="25,166,98,251" shape="rect"/>
                <area className="Area" href="" target="" alt="Sleep Room" title="Sleep Room" coords="540,165,614,238" shape="rect"/>
                <area className="Area" href="" target="" alt="Class Room" title="Class Room" coords="25,263,97,334" shape="rect"/>
                <area className="Area" href="" target="" alt="Meal Room" title="Meal Room" coords="541,249,613,335" shape="rect"/>
                <area className="Area" href="" target="" alt="Bar" title="Bar" coords="221,346,292,417" shape="rect"/>
                <area className="Area" href="" target="" alt="Lab" title="Lab" coords="346,346,418,418" shape="rect"/>
                <area className="Area" href="" target="" alt="Cellar" title="Cellar" coords="109,387,209,460" shape="rect"/>
                <area className="Area" href="" target="" alt="Apple Room" title="Apple Room" coords="221,430,293,488" shape="rect"/>
                <area className="Area" href="" target="" alt="Parole Room" title="Parole Room" coords="429,387,529,459" shape="rect"/>
                <area className="Area" href="" target="" alt="Map Room" title="Map Room" coords="345,430,419,490" shape="rect"/>
                <area className="Area" href="" target="" alt="Corridor 1" title="Corridor 1" coords="25,123,209,153" shape="rect"/>
                <area className="Area" href="" target="" alt="Corridor 2" title="Corridor 2" coords="304,57,335,155" shape="rect"/>
                <area className="Area" href="" target="" alt="Corridor 3" title="Corridor 3" coords="430,122,613,154" shape="rect"/>
                <area className="Area" href="" target="" alt="Corridor 4" title="Corridor 4" coords="109,164,141,250" shape="rect"/>
                <area className="Area" href="" target="" alt="Corridor 5" title="Corridor 5" coords="500,164,529,238" shape="rect"/>
                <area className="Area" href="" target="" alt="Corridor 6" title="Corridor 6" coords="109,262,141,334" shape="rect"/>
                <area className="Area" href="" target="" alt="Corridor 7" title="Corridor 7" coords="500,248,529,333" shape="rect"/>
                <area className="Area" href="" target="" alt="Corridor 8" title="Corridor 8" coords="25,345,209,376" shape="rect"/>
                <area className="Area" href="" target="" alt="Corridor 9" title="Corridor 9" coords="304,345,335,441" shape="rect"/>
                <area className="Area" href="" target="" alt="Corridor 10" title="Corridor 10" coords="429,346,613,376" shape="rect"/>
                    </map>
                    <img src="/ElbaBoard.png" useMap="#Map" className="Map"/>
                </div>
                <div className="Dice-pack">
                    <button
                        onClick={() => throwDice('Blanco')}
                        style={{ border: "none", background: "transparent",padding: 0,cursor: diceRolled ? "not-allowed" : "pointer",marginRight: "15px"}}
                        title="Dado Blanco"
                        disabled={diceRolled} // Deshabilitado si ya tiró
                    >
                        <img src={`/Dice/B${whiteDice}.png`} alt="Dado Blanco" style={{ width: "80px", height: "auto" }} />
                    </button>
                    <button
                        onClick={() => throwDice('Negro')}
                        style={{ border: "none", background: "transparent", padding: 0, cursor: diceRolled ? "not-allowed" : "pointer" }}
                        title="Dado Negro"
                        disabled={diceRolled}
                        
                    >
                        <img src={`/Dice/N${blackDice}.png`} alt="Dado Negro" style={{ width: "80px", height: "auto" }} />
                    </button>
                </div>
            </div>
            <div className="action-points-section">
                <h1>{actionPoints}</h1>
                <p>Action points </p>
            </div>

            {/* TABLA DE JUGADORES Y NPCS */}
            <div
            className="entities-panel"
            style={{
                position: 'absolute',
                top: '70%',
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
                    match.currentTurnUserId !== currentUser.id }
                    title="Accede to your bag"
                >
                    Form my bag
                </button>
                <button className="bag-button"
                    onClick={() => setDiscardHandOpen(true)}
                    disabled={
                    match.currentTurnUserId !== currentUser.id }
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