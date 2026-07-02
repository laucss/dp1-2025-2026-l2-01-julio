import { useEffect, useState } from "react"
import '../../../static/css/match/discardModal.css';
import tokenService from "../../services/token.service";
import getIdFromUrl from "../../util/getIdFromUrl";

// imports del dnd-kit (librería para el arrastre de cartas)
import {DndContext, DragOverlay, PointerSensor, useSensor, useSensors } from '@dnd-kit/core';
import Card from "./dnd-kit/Card";
import BagZone from "./dnd-kit/BagZone";
import { arrayMove } from '@dnd-kit/sortable';
import {restrictToWindowEdges} from '@dnd-kit/modifiers';
import { SortableContext } from '@dnd-kit/sortable';
import SortableCard from './dnd-kit/SortableCard';

// para alerta de errores
import { toast } from "react-toastify"

const jwt = tokenService.getLocalAccessToken();

export default function BagModal({isVisible, hand, bag, deck, onClose, player, onSave}){
    const matchId = getIdFromUrl(2);
    const[handCards, setHandCards] = useState([])
    const[bagCards, setBagCards] = useState([])
    const [activeCard, setActiveCard] = useState(null);


    const[deckCards, setDeckCards] = useState([])


    const sensors = useSensors(
        useSensor(PointerSensor, {
            activationConstraint: {
            distance: 3
            },
        })
    );


    useEffect(() => {
        setHandCards(hand)
        setBagCards(bag)
        setDeckCards(deck)
    }, [bag, deck, hand, isVisible])

    if (!isVisible) return null



    const confirm = async () => {
        try {
            
            const bagToCheck = {
                cards: bagCards.map(card => ({
                    id: card.id,
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
                toast.error("Word not valid, try another")
            }}

        } catch (error) {
            console.error("Error during confirm:", error)
            toast.error("An error occurred. Could not confirm discard.")
                
        }}

    const updateCards = async () => {
        try {
            
            const bagToUpdate = {
                cards: bagCards.map(card => ({
                    id: card.id,
                    frontImage: card.frontImage,
                    backImage: card.backImage,
                    letter: card.letter
                }))
                }

        const handToUpdate = {
                cards: handCards.map(card => ({
                    id: card.id,
                    frontImage: card.frontImage,
                    backImage: card.backImage,
                    letter: card.letter
                }))
                }

        const updatedDeckInGame = {
            notDiscardedCards: deckCards.notDiscardedCards.map(card => ({
                id: card.id,
                frontImage: card.frontImage,
                backImage: card.backImage,
                letter: card.letter
            })),
            discardedCards: deckCards.discardedCards.map(card => ({
                id: card.id,
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
            toast.error("An error occurred. Could not confirm discard.", error);
            
        }
        
        

    } 

    const handleDragStart = (event) => {
        const card =
            handCards.find(c => c.id === event.active.id) ||
            bagCards.find(c => c.id === event.active.id);

        setActiveCard(card);
    };


    const handleDragEnd = (event) => {
        const { active, over } = event;

        setActiveCard(null);

        if (!over) return;

        // active.id -> carta arrastrada
        // over.id   -> zona o carta destino

        // caso 1: de la mano a la bolsa 
        if (over.id === 'bag') {
            const card = handCards.find(c => c.id === active.id);

            setHandCards(prev => prev.filter(c => c.id !== active.id));
            setBagCards(prev => [...prev, card]);
        }

        // caso 2: reordenar dentro de la bolsa 
        if (over.id !== 'bag') {
            const oldIndex = bagCards.findIndex(c => c.id === active.id);
            const newIndex = bagCards.findIndex(c => c.id === over.id);

            setBagCards(arrayMove(bagCards, oldIndex, newIndex));
        }

    };

    

    return (
        <div className="modal-overlay">
            <div className="window">
                <div className="modal-content-wrapper">
                    <div className="sections-container">
                        <DndContext 
                            sensors={sensors}
                            onDragStart={handleDragStart} 
                            onDragEnd={handleDragEnd}>

                        <div className="hand-section">
                            <h3 className="section-title">Hand</h3>
                            <div className="cards-grid">
                                {handCards.map((card) => (
                                    <Card key={card.id} card={card} />
                                ))}
                            </div>
                        </div>

                        <div className="bag-section">
                            <h3 className="section-title">Bag</h3>
                            {/*<div className="cards-grid">*/}
                            <BagZone>
                                <SortableContext items={bagCards.map(c => c.id)}>
                                    {bagCards.map(card => (
                                    <SortableCard key={card.id} card={card} />
                                    ))}
                                </SortableContext>
                            </BagZone>
                        </div>

                        <DragOverlay modifiers={[restrictToWindowEdges]}> {/*"Restringe el movimiento a los bordes de la ventana.*/}
                            {activeCard ? (
                                // eslint-disable-next-line jsx-a11y/alt-text
                                <img
                                    src={`/resources${activeCard.frontImage}`}
                                    className="card"
                                    style={{ cursor: 'grabbing', opacity: 1 }}
                                />
                            ) : null}
                        </DragOverlay>

                        </DndContext>
                    </div>
                    

                    <div className="buttons"> 
                        <button onClick={confirm} className="confirm-button">
                            Confirm
                        </button>

                        <button onClick={onClose} className="cancel-button">
                            Cancel
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )

}