import { useEffect, useState } from "react"
import '../../static/css/match/discardModal.css';
import tokenService from "../../services/token.service";
import getIdFromUrl from "../../util/getIdFromUrl";

const jwt = tokenService.getLocalAccessToken();

export default function DiscardHandModal({isVisible, hand, deck, onClose, player, onSave, updateCurrentTurnId}){
    const matchId = getIdFromUrl(2);
    const[handCards, setHandCards] = useState([])
    const[cardsToDiscard, setCardsToDiscard] = useState([])
    const[deckCards, setDeckCards] = useState({})
    const[bagCards, setBagCards] = useState([])
    const[playerTurnId, setPlayerTurnId] = useState(null)

    const[currentPlayer, setCurrentPlayer] = useState({})

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        setHandCards(hand)
        setCardsToDiscard([])
        setDeckCards(deck)
        setCurrentPlayer(player)
        setPlayerTurnId()
    }, [isVisible])

    if (!isVisible) return null

    const confirm = async () => {
        try {
            const handToUpdate = {
                cards: handCards.map(card => ({
                    frontImage: card.frontImage,
                    backImage: card.backImage,
                    letter: card.letter
                }))
            };
            const bagToUpdate = {
                cards: bagCards.map(card => ({
                    frontImage: card.frontImage,
                    backImage: card.backImage,
                    letter: card.letter
                }))
            }

            // Añadir las cartas descartadas al deck.discardedCards
            const updatedDiscardedCards = [
                ...(deckCards.discardedCards || []),
                ...cardsToDiscard
            ];

            const updatedDeckInGame = {
                notDiscardedCards: (deckCards.notDiscardedCards || []).map(card => ({
                    frontImage: card.frontImage,
                    backImage: card.backImage,
                    letter: card.letter
                })),
                discardedCards: updatedDiscardedCards.map(card => ({
                    frontImage: card.frontImage,
                    backImage: card.backImage,
                    letter: card.letter
                }))
            };

            const response = await fetch(`/api/v1/matches/${matchId}/discardConfirmed`, {
                method: "PUT", 
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    hand: handToUpdate,
                    bag: bagToUpdate,
                    deck: updatedDeckInGame,
                    playerId: player.id,
                })
            });
            
            if (response.ok) {
                const nextTurnId = await response.json()
                console.log('Cards discarded successfully')
                updateCurrentTurnId(nextTurnId)
                onSave()
            } else {
                setMessage("Could not discard cards.")
                setVisible(true);
            }

        } catch (error) {
            console.error("Error during discard:", error)
            setMessage("An error occurred. Could not discard cards.")
            setVisible(true)
        }
    }

    const handleCancel = () => {
        // Revertir cambios
        setHandCards(hand)
        setCardsToDiscard([])
        onClose()
    }

    return (
        <div className="modal-overlay">
            <div className="window">
                <div className="modal-content-wrapper">
                    <div className="sections-container">
                        <div className="hand-section">
                            <h3 className="section-title">Hand</h3>
                            {handCards.map((card, index) => (
                                <div key={index}>
                                    <img 
                                        src={`/resources${card.frontImage}`} 
                                        alt={`Carta ${card.letter}`}  
                                        className="card"
                                        onClick={() => {
                                            setCardsToDiscard(prev => [...prev, card]);
                                            setHandCards(prev => prev.filter((_, i) => i !== index));
                                        }}
                                        style={{ cursor: 'pointer' }}
                                    />
                                </div>
                            ))}
                        </div>

                        <div className="bag-section">
                            <h3 className="section-title">To Discard</h3>
                            {cardsToDiscard.map((card, index) => (
                                <div key={index}>
                                    <img 
                                        src={`/resources${card.frontImage}`} 
                                        alt={`Carta ${card.letter}`}  
                                        className="card"
                                        onClick={() => {
                                            setHandCards(prev => [...prev, card]);
                                            setCardsToDiscard(prev => prev.filter((_, i) => i !== index));
                                        }}
                                        style={{ cursor: 'pointer' }}
                                    />
                                </div>
                            ))}
                        </div>
                    </div>

                    <div className="buttons"> 
                        <button onClick={confirm} className="confirm-button">
                            Confirm
                        </button>

                        <button onClick={handleCancel} className="confirm-button">
                            Cancel
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}
