import { useEffect, useState } from "react"
import '../../static/css/match/weaponModal.css';
import tokenService from "../../services/token.service";
import getIdFromUrl from "../../util/getIdFromUrl";

// para mostrar las excepciones
import { toast } from "react-toastify";

const jwt = tokenService.getLocalAccessToken();

export default function WeaponModal({ isVisible, bagCards, onClose, player, onWeaponSelected }) {
    const matchId = getIdFromUrl(2);
    const [availableCards, setAvailableCards] = useState([]);
    const [selectedCards, setSelectedCards] = useState([]);
    const [formedWord, setFormedWord] = useState('');
    const [message, setMessage] = useState(null);
    const [messageType, setMessageType] = useState('error'); // 'error' o 'success'
    const [visible, setVisible] = useState(false);
    const [isValidating, setIsValidating] = useState(false);

    useEffect(() => {
        setAvailableCards(bagCards || []);
        setSelectedCards([]);
        setFormedWord('');
        setMessage(null);
    }, [isVisible, bagCards]);

    useEffect(() => {
        const word = selectedCards.map(card => card.letter).join('');
        setFormedWord(word);
    }, [selectedCards]);

    if (!isVisible) return null;

    const handleAddCard = (card, index) => {
        setSelectedCards(prev => [...prev, card]);
        setAvailableCards(prev => prev.filter((_, i) => i !== index));
    };

    const handleRemoveCard = (card, index) => {
        setAvailableCards(prev => [...prev, card]);
        setSelectedCards(prev => prev.filter((_, i) => i !== index));
    };

    const validateAndSelectWeapon = async () => {
        if (selectedCards.length === 0) {
            setMessage('Please select at least one card');
            setMessageType('error');
            setVisible(true);
            return;
        }

        setIsValidating(true);
        try {
            const wordLowerCase = formedWord.toLowerCase();
            const response = await fetch(`/api/v1/bag/validate-weapon`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${jwt}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    cards: selectedCards.map(card => ({
                        id: card.id,
                        frontImage: card.frontImage,
                        backImage: card.backImage,
                        letter: card.letter.toLowerCase()
                    }))
                })
            });

            if (response.ok) {
                const result = await response.json();
                if (result.isValid) {
                    setMessage(`"${formedWord}" is a valid weapon! Bonus: +${result.bonusValue || 0}`);
                    setMessageType('success');
                    setVisible(true);
                    
                    // Callback con la palabra en minúsculas y el bonus
                    setTimeout(() => {
                        onWeaponSelected({
                            word: wordLowerCase,
                            bonus: result.bonusValue || 0,
                            cards: selectedCards
                        });
                    }, 500);
                } else {
                    toast.error(`"${formedWord}" is not a valid weapon. Try another combination.`);
                    //setMessageType('error');
                    //setVisible(true);
                }
            } else {
                const errorData = await response.json();
                setMessage(errorData.message || 'Error validating weapon');
                setMessageType('error');
                setVisible(true);
            }
        } catch (error) {
            console.error('Error validating weapon:', error);
            setMessage('An error occurred while validating the weapon');
            setMessageType('error');
            setVisible(true);
        } finally {
            setIsValidating(false);
        }
    };

    const handleCancel = () => {
        setAvailableCards(bagCards || []);
        setSelectedCards([]);
        setFormedWord('');
        setMessage(null);
        onClose();
    };

    return (
        <div className="modal-overlay">
            <div className="window weapon-modal-window">
                <div className="modal-content-wrapper">
                    {visible && message && (
                        <div className={`message ${messageType}`}>
                            {message}
                        </div>
                    )}

                    <div className="weapon-sections-container">
                        <div className="bag-section">
                            <h3 className="section-title">Bag</h3>
                            <div className="cards-grid">
                                {availableCards.length > 0 ? (
                                    availableCards.map((card, index) => (
                                        <div key={index} className="card-item">
                                            <img
                                                src={`/resources${card.frontImage}`}
                                                alt={`Card ${card.letter}`}
                                                className="card"
                                                onClick={() => handleAddCard(card, index)}
                                                title={`Click to select ${card.letter}`}
                                            />
                                            <span className="card-letter">{card.letter}</span>
                                        </div>
                                    ))
                                ) : (
                                    <p className="empty-message">No cards available in your bag</p>
                                )}
                            </div>
                        </div>

                        <div className="selected-section">
                            <h3 className="section-title">Weapon</h3>
                            <div className="formed-word">
                                <span className="word-display">{formedWord || '---'}</span>
                            </div>
                            <div className="cards-grid">
                                {selectedCards.length > 0 ? (
                                    selectedCards.map((card, index) => (
                                        <div key={index} className="card-item">
                                            <img
                                                src={`/resources${card.frontImage}`}
                                                alt={`Card ${card.letter}`}
                                                className="card"
                                                onClick={() => handleRemoveCard(card, index)}
                                                title={`Click to deselect ${card.letter}`}
                                            />
                                            <span className="card-letter">{card.letter}</span>
                                        </div>
                                    ))
                                ) : (
                                    <p className="empty-message">Select cards</p>
                                )}
                            </div>
                        </div>
                    </div>

                    <div className="buttons">
                        <button
                            onClick={validateAndSelectWeapon}
                            className="confirm-button"
                            disabled={selectedCards.length === 0 || isValidating}
                        >
                            {isValidating ? 'Validating...' : 'Confirm'}
                        </button>
                        <button onClick={handleCancel} className="cancel-button">
                            Cancel
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
