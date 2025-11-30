import { useEffect, useState } from "react"
import '../../static/css/match/discardModal.css';
import tokenService from "../../services/token.service";
import getIdFromUrl from "../../util/getIdFromUrl";

const jwt = tokenService.getLocalAccessToken();

export default function BagModal({isVisible, hand, bag, deck, onClose, player, onSave}){
    const matchId = getIdFromUrl(2);
    const[handCards, setHandCards] = useState([])
    const[bagCards, setBagCards] = useState([])

    const[deckCards, setDeckCards] = useState([])

    const[currentPlayer, setCurrentPlayer] = useState({})

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        setHandCards(hand)
        setBagCards(bag)
        setDeckCards(deck)
        setCurrentPlayer(player)
    }, [isVisible])

    if (!isVisible) return null

    const confirm = async () => {
        try {
            
            const bagToCheck = {
                cards: bagCards.map(card => ({
                    frontImage: card.frontImage,
                    backImage: card.backImage,
                    letter: card.letter
                }))
                };
           
            const response = await fetch (`/api/v1/bag/validate`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: 'application/json',
                'Content-Type': 'application/json',
            }, body : JSON.stringify(bagToCheck)
           
           })
           
          
          if (response.ok) {
            const isValid = await response.json()
            console.log('Is valid word?:', isValid)
             
            if (isValid) {
                await updateCards()
                onSave()

            } else {
                setMessage("Word not valid, try another")
                setVisible(true);
            }}

        } catch (error) {
            console.error("Error during confirm:", error)
            setMessage("An error occurred. Could not confirm discard.")
            setVisible(true)
                
        }}

    const updateCards = async () => {
        try {
            
            const bagToUpdate = {
                cards: bagCards.map(card => ({
                    frontImage: card.frontImage,
                    backImage: card.backImage,
                    letter: card.letter
                }))
                }

        const handToUpdate = {
                cards: handCards.map(card => ({
                    frontImage: card.frontImage,
                    backImage: card.backImage,
                    letter: card.letter
                }))
                }

        const updatedDeckInGame = {
            notDiscardedCards: deckCards.notDiscardedCards.map(card => ({
                frontImage: card.frontImage,
                backImage: card.backImage,
                letter: card.letter
            })),
            discardedCards: deckCards.discardedCards.map(card => ({
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
        })
        console.log('status update',response.status )
            
        } catch (error) {
            console.error("Error during validation or update:", error);
            setMessage("An error occurred. Could not confirm discard.");
            setVisible(true);
            
        }
        
        

    } 

    

    return (
        <div className="modal-overlay">
            <div className="window">
                <div className="modal-content-wrapper">
                    <div className="sections-container">
                        <div className="hand-section">
                            <h3 className="section-title">Hand</h3>
                            {handCards.map((card, index) => (
                                <div key={index} >
                                    <img 
                                        src={`/resources${card.frontImage}`} 
                                        alt={`Carta ${card.letter}`}  
                                        className="card"
                                        onClick={() => {
                                            setBagCards(prev => [...prev, card]);
                                            setHandCards(prev => prev.filter((_, i) => i !== index));
                                        }}/>
                                </div>
                            ))}
                        </div>

                        <div className="bag-section">
                            <h3 className="section-title">Bag</h3>
                            {bagCards.map((card, index) => (
                                <div key={index} >
                                    <img 
                                        src={`/resources${card.frontImage}`} 
                                        alt={`Carta ${card.letter}`}  
                                        className="card"
                                        onClick={() => {
                                            setHandCards(prev => [...prev, card]);
                                            setBagCards(prev => prev.filter((_, i) => i !== index));
                                        }}
                                    />
                                </div>
                            ))}
                        </div>
                    </div>

                    <div className="buttons"> 
                        <button onClick={confirm} className="confirm-button">
                            Confirm
                        </button>

                        <button onClick={onClose} className="confirm-button">
                            Cancel
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )

}