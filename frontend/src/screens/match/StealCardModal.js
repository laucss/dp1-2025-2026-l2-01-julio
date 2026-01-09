import React, { useEffect, useState } from 'react';
import tokenService from '../../services/token.service';

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
      const cardIdToSend = sourceToUse === 'hand' ? null : (card?.id || null);
      console.log('📤 Enviando al backend:', { cardId: cardIdToSend, fromWhere: sourceToUse });
      const res = await fetch(`/api/v1/matches/${matchId}/${winnerId}/steal-card-from/${loserId}`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${jwt}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          cardId: cardIdToSend,
          fromWhere: sourceToUse
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
    console.log('🎯 Carta seleccionada:', card);
    console.log('📦 Source:', source);
    setLoading(true);
    await stealCard(card, source);
  };

  if (!isOpen) return null;

  return (
    <div style={{
      position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
      background: 'rgba(0,0,0,0.6)', zIndex: 2000,
      display: 'flex', alignItems: 'center', justifyContent: 'center'
    }}>
      <div style={{
        background: '#ffffff', borderRadius: 8, padding: 20,
        width: 600, maxHeight: '80vh', overflowY: 'auto'
      }}>
        {step === 'choose' && (
          <div>
            <h3 style={{ marginTop: 0 }}>From where would you like to steal?</h3>
            <div style={{ display: 'flex', gap: 12 }}>
              <button 
                onClick={() => handleChoose('hand')} 
                disabled={loading}
                style={{ padding: '10px 16px', cursor: loading ? 'not-allowed' : 'pointer' }}
              >
                Hand
              </button>
              <button 
                onClick={() => handleChoose('bag')} 
                disabled={loading || loserCards.bag.length === 0}
                style={{ padding: '10px 16px', cursor: (loading || loserCards.bag.length === 0) ? 'not-allowed' : 'pointer' }}
              >
                Bag
              </button>
            </div>
          </div>
        )}

        {step === 'select' && (
          <div>
            <h4 style={{ marginTop: 0 }}>Select a card from the {source}</h4>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 10 }}>
              {(source === 'bag' ? loserCards.bag : loserCards.hand).map((card, idx) => (
                <div 
                  key={idx} 
                  style={{ cursor: loading ? 'not-allowed' : 'pointer', opacity: loading ? 0.6 : 1 }} 
                  onClick={() => !loading && handleSelectCard(card.id)}
                >
                  <img
                    src={source === 'bag' ? `/resources${card.frontImage}` : '/backCard.png'}
                    alt={source === 'bag' ? `Carta ${card.letter}` : 'Carta oculta'}
                    style={{ width: 100, height: 'auto', borderRadius: 6, boxShadow: '0 2px 6px rgba(0,0,0,0.2)' }}
                  />
                </div>
              ))}
            </div>
            <div style={{ marginTop: 16 }}>
              <button 
                onClick={() => { setStep('choose'); setSource(null); }} 
                disabled={loading}
                style={{ padding: '8px 12px', cursor: loading ? 'not-allowed' : 'pointer' }}
              >
                Volver
              </button>
            </div>
          </div>
        )}

        {step === 'choose' && (
          <div style={{ marginTop: 20, textAlign: 'right' }}>
            <button 
              onClick={onClose}
              disabled={loading}
              style={{ padding: '8px 12px', cursor: loading ? 'not-allowed' : 'pointer' }}
            >
              Cerrar
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
