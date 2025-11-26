import { useEffect, useState } from "react"
import '../../static/css/match/discardModal.css';
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function DiscardModal({isVisible, hand, bag, discardPile, onClose}){
    const[handCards, setHandCards] = useState([])
    const[bagCards, setBagCards] = useState([])
    
    const[discardedCards, setDiscardedCards] = useState(discardPile)

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        setHandCards(hand)
        setBagCards(bag)
    
    }, [hand, bag])

    if (!isVisible) return null
    
    const confirm = async () => {
        try {
           const response = await fetch (`api/v1/bag/validate/${bagCards}`, {
            method: "GET",
            headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: 'application/json',
                'Content-Type': 'application/json',
            }
           })
           if (response) {
                updateCards()
           }
           else {
                message("Word not valid, try another")
           }

        } catch (error) {
            
        }

    const updateCards = async () => {


    } 

    }

    return (
        <div className="modal-overlay">
            <div className="window">
                <div className="hand-section">
                {handCards.map((card, index) => (
                    <div key={index} >
                        <img 
                            src={`/resources${card.frontImage}`} 
                            alt={`Carta ${card.letter}`}  
                            className="card"
                            onClick={()=> setBagCards(prev => [...prev, card])}/>
                    </div>
                    ))}
                </div>

                <div className="bag-section">
                    {bagCards.map((card, index) => (
                    <div key={index} >
                        <img 
                            src={`/resources${card.frontImage}`} 
                            alt={`Carta ${card.letter}`}  
                            className="card"/>
                    </div>
                    ))}
                    
                </div>

                <div className="buttons"> 
                    <button onClick={confirm()} className = "confirm-button">
                        Confirm
                    </button>

                    <button onClick={onClose} className = "confirm-button">
                        Cancel
                    </button>

                </div>

            </div>
            
            

        </div>


    )

}