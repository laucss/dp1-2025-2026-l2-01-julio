import React, { useEffect, useState } from 'react';
import tokenService from '../../../services/token.service';
import '../../../static/css/match/StealCardModal.css';

export default function StealCardModal({
  isOpen,
  onClose,
  loserId,
  matchId,
  winnerId,
  onSteal
}) {
  const [step, setStep] = useState('choose'); // 'choose' | 'select'
  const [source, setSource] = useState(null); // 'hand' | 'bag'
  const [loserCards, setLoserCards] = useState({ hand: [], bag: [], deck: null });
  const [loading, setLoading] = useState(false);
  const jwt = tokenService.getLocalAccessToken();

  useEffect(() => {
    if (!isOpen || !loserId) return;
    const fetchLoserCards = async () => {
      try {
        const res = await fetch(`/api/v1/matches/${matchId}/${loserId}/getAllCards`, {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${jwt}`,
            Accept: 'application/json',
            'Content-Type': 'application/json'
          }
        });
        if (res.ok) {
          const data = await res.json();
          console.log('📥 Cartas recibidas del servidor:', data);
          setLoserCards({
            hand: Array.isArray(data.hand?.cards) ? data.hand.cards : [],
            bag: Array.isArray(data.bag?.cards) ? data.bag.cards : [],
            deck: data.deck || null
          });
        }
      } catch (e) {
        console.error('Error fetching loser cards', e);
      }
    };
    fetchLoserCards();
  }, [isOpen, loserId, matchId, jwt]);

  const handleChoose = (src) => {
    if (src === 'hand') {
      // Para 'hand', roba automáticamente sin mostrar el modal de selección
      setSource(src);
      setLoading(true);
      stealCard(null, src);
    } else {
      // Para 'bag', muestra el modal de selección
      setSource(src);
      setStep('select');
    }
  };

  const stealCard = async (card, sourceToUse) => {
    try {
      const cardToSend = sourceToUse === 'hand'
        ? null
        : card;
      console.log('📤 Enviando al backend:', { card: cardToSend, fromWhere: sourceToUse });
      const res = await fetch(`/api/v1/fights/${matchId}/steal-card-from-player`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${jwt}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          card: cardToSend,
          fromWhere: sourceToUse,
          winnerId: winnerId,
          loserId: loserId
        })
      });

      if (res.ok) {
        console.log('Carta robada exitosamente');
        await onSteal();
      } else {
        console.error('Error al robar carta:', res.status);
        alert('Error al robar la carta');
      }
    } catch (e) {
      console.error('Error stealing card', e);
      alert('Error al robar la carta');
    } finally {
      setLoading(false);
      setStep('choose');
      setSource(null);
      onClose();
    }
  };

  const handleSelectCard = async (card) => {
    if (!source || !card) return;
    console.log(' Carta seleccionada:', card);
    console.log(' Source:', source);
    setLoading(true);
    await stealCard(card, source);
  };

  if (!isOpen) return null;

  return (
    <div className="steal-card-modal-overlay">
      <div className="steal-card-modal-content">
        {step === 'choose' && (
          <div>
            <h3 style={{ marginTop: 0, color: '#000' }}>From where would you like to steal?</h3>
            <div className="steal-card-modal-button-group">
              <button 
                onClick={() => handleChoose('hand')} 
                disabled={loading}
                className='steal-button'
              >
                Hand
              </button>
              <button 
                onClick={() => handleChoose('bag')} 
                disabled={loading || loserCards.bag.length === 0}
                className='steal-button'
                style={{cursor: (loading || loserCards.bag.length === 0) ? 'not-allowed' : 'pointer' }}
              >
                Bag
              </button>
            </div>
          </div>
        )}

        {step === 'select' && (
          <div>
            <h4 style={{ marginTop: 0, color: '#000' }}>Select a card from the {source}</h4>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 10 }}>
              {(source === 'bag' ? loserCards.bag : loserCards.hand).map((card, idx) => (
                <div 
                  key={idx} 
                  style={{ cursor: loading ? 'not-allowed' : 'pointer', opacity: loading ? 0.6 : 1 }} 
                  onClick={() => !loading && handleSelectCard(card)}
                >
                  <img
                    src={source === 'bag' ? `/resources${card.frontImage}` : '/backCard.png'}
                    alt={source === 'bag' ? `Carta ${card.letter}` : 'Carta oculta'}
                    style={{ width: 100, height: 'auto', borderRadius: 6, boxShadow: '0 2px 6px rgba(0,0,0,0.2)' }}
                    className='steal-card-img'
                  />
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
