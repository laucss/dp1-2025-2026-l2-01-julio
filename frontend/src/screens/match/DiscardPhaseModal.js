import { useEffect, useState } from "react"
import '../../static/css/match/discardModal.css';
import tokenService from "../../services/token.service";
import getIdFromUrl from "../../util/getIdFromUrl";

import { useMemo } from 'react';

import { toast } from "react-toastify";

// imports del dnd-kit (librería para el arrastre de cartas)
import {DndContext, DragOverlay, PointerSensor, useSensor, useSensors } from '@dnd-kit/core';
import Card from "./dnd-kit/Card";
import BagZone from "./dnd-kit/BagZone";
import DiscardZone from "./dnd-kit/DiscardZone";
import { arrayMove } from '@dnd-kit/sortable';
import {restrictToWindowEdges} from '@dnd-kit/modifiers';
import { SortableContext } from '@dnd-kit/sortable';
import SortableCard from './dnd-kit/SortableCard';
import { closestCenter } from '@dnd-kit/core';


const jwt = tokenService.getLocalAccessToken();

export default function DiscardPhaseModal({isVisible, hand, bag, deck, onClose, player, onSave, updateCurrentTurnId}){
    const matchId = getIdFromUrl(2);
    const[handCards, setHandCards] = useState([])
    const[cardsToDiscard, setCardsToDiscard] = useState([])
    const[deckCards, setDeckCards] = useState({})
    const[bagCards, setBagCards] = useState([])
    const[playerTurnId, setPlayerTurnId] = useState(null)

    // qué carta se está arrastrando 
    const [activeCard, setActiveCard] = useState(null);

    const[currentPlayer, setCurrentPlayer] = useState({})

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    const overlayCard = useMemo(() => activeCard, [activeCard]);

    // esto es para intentar hacer más rápido el movimiento de arrastrar
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
                    id: card.id,
                    frontImage: card.frontImage,
                    backImage: card.backImage,
                    letter: card.letter
                }))
            };
            const bagToUpdate = {
                cards: bagCards.map(card => ({
                    id: card.id,
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
                    id: card.id, 
                    frontImage: card.frontImage,
                    backImage: card.backImage,
                    letter: card.letter
                })),
                discardedCards: updatedDiscardedCards.map(card => ({
                    id: card.id,
                    frontImage: card.frontImage,
                    backImage: card.backImage,
                    letter: card.letter
                }))
            };

            const response = await fetch(`/api/v1/matches/${matchId}/confirmDiscardPhase`, {
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
            } if (!response.ok){
                const error = await response.json();
                throw error;

            
            }

        } catch (error) {
            console.error("Error during discard:", error)
            toast.error(error.message);
        }
    }

    const handleCancel = () => {
        // Revertir cambios
        setHandCards(hand)
        setCardsToDiscard([])
        onClose()
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

        const cardFromHand = handCards.find(c => c.id === active.id);
        const cardFromBag = bagCards.find(c => c.id === active.id);
        const card = cardFromHand || cardFromBag;

        // de la mano o bolsa a zona de descarte 
        if (over.id === 'discard') {
            const card = handCards.find(c => c.id === active.id);

            setHandCards(prev => prev.filter(c => c.id !== active.id));
            setBagCards(prev => prev.filter(c => c.id !== active.id));
            setCardsToDiscard(prev => [...prev, card]);
            return
        }

        // caso 1: de la mano a la bolsa 
        if (over.id === 'bag' && cardFromHand) {
            const card = handCards.find(c => c.id === active.id);

            setHandCards(prev => prev.filter(c => c.id !== active.id));
            setBagCards(prev => [...prev, card]);
        }

        // caso 2: reordenar dentro de la bolsa 
        if (cardFromBag && over.id !== 'bag') {
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
                            onDragEnd={handleDragEnd}
                             collisionDetection={closestCenter}>

                            <div className="hand-and-bag-container">
                                
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
                                    <BagZone>
                                        <div className="cards-grid">
                                            <SortableContext items={bagCards.map(c => c.id)}>
                                                {bagCards.map(card => (
                                                <SortableCard key={card.id} card={card} />
                                                ))}
                                            </SortableContext>
                                        </div>
                                    </BagZone>
                                </div>
                            </div>



                            <div className="discard-section">
                                <h3 className="section-title">To Discard</h3>
                                <div className="cards-grid">
                                    <DiscardZone>
                                        {cardsToDiscard.map((card) => (
                                            <img 
                                                key={card.id}
                                                src={`/resources${card.frontImage}`} 
                                                alt={`Carta ${card.letter}`}  
                                                className="card"/>
                                        ))} 
                                    </DiscardZone>
                                </div>
                            </div>

                            <DragOverlay modifiers={[restrictToWindowEdges]}> {/*"restrictToWindowEdges : restringe el movimiento a los bordes de la ventana.*/}
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

                        <button onClick={handleCancel} className="cancel-button">
                            Cancel
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}
