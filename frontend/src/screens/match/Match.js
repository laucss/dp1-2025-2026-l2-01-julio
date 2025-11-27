import React, { useState } from "react"
import '../../static/css/match/Match.css';
import getIdFromUrl from '../../util/getIdFromUrl'
import { useEffect } from "react";
import useFetchState from "../../util/useFetchState";
import tokenService from "../../services/token.service";
import DiscardModal from "./DiscardModal";
//import Chat from "./chat";


const jwt = tokenService.getLocalAccessToken();
const currentUser = tokenService.getUser();


export default function Match(){
    const matchId = getIdFromUrl(2);
    const [deck, setDeck] = useState(null)
    const [discarPile, setDiscardPile] = useState([])
    const [handCards, setHandCards] = useState([])
    const [bagCards, setBagCards] = useState([])
    const [whiteDice, setWhiteDice] = useState("1")
    const [blackDice, setBlackDice] = useState("1")
    const[numCardsDrawn, setNumCardsDrawn] = useState(0)

    const [currentPlayer, setCurrentPlayer] = useState({})

    const[discardOpen, setDiscardOpen] = useState(false)
    // const playerId = 
    // const [playerTurnId, setPlayerTurnId] = useState(null)

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
        
    // CARGAR DATOS PARTIDA 
    const [match, setMatch] = useFetchState(
        [],
        `/api/v1/matches/${matchId}`,
        jwt,
        setMessage,
        setVisible
    )

    // CARGAR DATOS JUGADORES 
    const [player, setPlayer] = useFetchState(
        [],
        `/api/v1/matches/${matchId}/players`,
        jwt,
        setMessage,
        setVisible
    );
    

    useEffect(() => {
        initializeDeck(); 
        // TODO: cambiar esta funcion pq realmente hay que repartir primero a todos los jugadores
        setCurrentPlayer(player.filter(p => p.user.id === currentUser?.id))
}, [matchId, player]);



    // iNICIALIZAR BARAJA
    const initializeDeck = () => {
        fetch(`/api/v1/deck/${matchId}`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: 'application/json',
                'Content-Type': 'application/json',
            },
        })
        .then(async res => {
            if (!res.ok) {
                // TODO: CAMBIAR, ESTÁ SACADO DE CHATI A MODO DE PRUEBA DE FUNCIONALIDAD DE BACKEND, hay que ponerlo bien
                let errorBody;
                try {
                    errorBody = await res.json();
                } catch {
                    errorBody = await res.text();
                }
                throw new Error(`Error al obtener el mazo: ${res.status} ${res.statusText} - ${JSON.stringify(errorBody)}`);
            }
            return res.json();
        })
        .then(data => {
            setDeck(data);
            console.log("Deck cargado:", data);
        })
        .catch(err => {
            console.error("Fetch fallido:", err);
        });

    }

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
            console.log('datos devueltos' ,data)
            
            setDeck(data.deck)
            console.log('deck', deck)
            setHandCards(prev => [...prev, data.card])

            
        } catch (error) {
            console.log('error', error)
            
        }    
        
            

    }

    const throwDice = (diceType) => {
        const roll = Math.floor(Math.random() * 6) + 1;
        if (diceType === 'Blanco') {
            setWhiteDice(roll.toString());
        } else {
            setBlackDice(roll.toString());
        }
    }

    
    const endMatch = () => {
        if (!window.confirm("¿Seguro que quieres finalizar la partida?")) return;

        fetch(`/api/v1/matches/${matchId}/end`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${jwt}`,
                'Content-Type': 'application/json'
            }
        })
        .then(res => res.json())
        .then(() => window.location.reload())
        .catch(err => console.error(err));
    };


    if (match.status === "FINISHED") {
        return (
            <div className="match-ended">
                <h2>La partida ha finalizado!!!!!</h2>
                <p>Gracias por jugar.</p>
            </div>
        );
    }


    const playersList = (Array.isArray(player) ? player : (player?.players || [])).filter(p => p.user.id !== currentUser?.id);

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
                            onClick={drawCard}
                            style={{ 
                                border: "none", 
                                background: "transparent", 
                                padding: 0, 
                                cursor: "pointer",
                            }}
                        >
                            <img 
                                src="/backCard.png" 
                                alt="Robar carta"
                                style={{ width: "150px", height: "auto" }}
                            />
                        </button>
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
                        style={{ border: "none", background: "transparent",padding: 0,cursor: "pointer",marginRight: "15px"}}
                        title="Dado Blanco"
                    >
                        <img src={`/Dice/B${whiteDice}.png`} alt="Dado Blanco" style={{ width: "80px", height: "auto" }} />
                    </button>
                    <button
                        onClick={() => throwDice('Negro')}
                        style={{ border: "none", background: "transparent", padding: 0, cursor: "pointer" }}
                        title="Dado Negro"
                    >
                        <img src={`/Dice/N${blackDice}.png`} alt="Dado Negro" style={{ width: "80px", height: "auto" }} />
                    </button>
                </div>
            </div>
            <div className="player-section">
                <div className="player-hand">
                    {handCards.map((carta, index) => (
                                    <div key={index} >
                                        <img src={`/resources${carta.frontImage}`} alt={`Carta ${carta.letter}`} className="card"/>
                                    </div>
                    ))}
                </div>
                <div className="player-bag">

                </div>
                
            </div>
            <div>
                <button className="discard-button"
                    onClick={() => setDiscardOpen(true)}
                    title="Descartar cartas"
                >
                    Discard cards
                </button>
            </div>
            

            <button
                className="end-match-button"
                onClick={endMatch}
                style={{
                    marginLeft: "10px",
                    padding: "10px 15px",
                    background: "#c0392b",
                    color: "white",
                    border: "none",
                    borderRadius: "8px",
                    cursor: "pointer"
                }}
            >
                Finalizar partida
            </button>

        <DiscardModal
            isVisible={discardOpen}
            hand={handCards}
            bag={bagCards}
            deck={deck}
            discardPile={discarPile}
            player={currentPlayer[0]}
            onClose={() => setDiscardOpen(false)}
            onSave={() =>
                setDiscardOpen(false)}
            />
        
        </div>
            )

        
    }