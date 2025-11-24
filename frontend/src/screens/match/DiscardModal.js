import { useEffect, useState } from "react"
import '../../static/css/match/discardModal.css';

export default function DiscardModal({isVisible, hand, bag, discardPile, onClose}){
    const[handCards, setHandCards] = useState([])
    const[bagCards, setBagCards] = useState([])
    const[discardedCards, setDiscardedCards] = useState(discardPile)

    useEffect(() => {
        setHandCards(hand)
        setBagCards(bag)
    
    }, [hand], [bag])

    if (!isVisible) return null
    

    return (
        <div className="modal-overlay">
            <div className="window">
                <div className="hand-section">
                {handCards.map((carta, index) => (
                    <div key={index} >
                        <img src={`/resources${carta.frontImage}`} alt={`Carta ${carta.letter}`} className="card"/>
                    </div>
                    ))}
                </div>

                <div className="bag-section">

                </div>

                <div className="buttons"> 
                    <button className = "confirm-button">
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