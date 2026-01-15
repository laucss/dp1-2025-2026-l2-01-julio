import React, { useEffect, useState } from 'react';
import '../../static/css/match/NpcLossDiscardModal.css';

export default function NpcLossDiscardModal({
  isOpen,
  handCards = [],
  bagCards = [],
  onDiscard,
  onClose
  , title = 'You have lost against a NPC', subtitle = 'Choose from where to discard a card:'
}) {
  const [step, setStep] = useState('choose'); // 'choose' | 'select'
  const [source, setSource] = useState(null); // 'hand' | 'bag'

  useEffect(() => {
    if (isOpen) {
      setStep('choose');
      setSource(null);
    }
  }, [isOpen]);

  if (!isOpen) return null;

  const safeHand = Array.isArray(handCards) ? handCards : [];
  const safeBag = Array.isArray(bagCards) ? bagCards : [];
  const hasHand = safeHand.length > 0;
  const hasBag = safeBag.length > 0;

  const handleChoose = (src) => {
    setSource(src);
    setStep('select');
  };

  const handleSelectCard = (card) => {
    if (!card || !source) return;
    onDiscard({ cardId: card.id, fromWhere: source });
  };

  return (
    <div className="npc-loss-discard-modal-overlay">
      <div className="npc-loss-discard-modal-content">
        {step === 'choose' && (
          <div>
            <h3 style={{ margin: 0, marginBottom: 12,color: '#000000'}}>{title}</h3>
            <p style={{ marginTop: 0, color: '#000000' }}>{subtitle}</p>
            <div className="npc-loss-discard-modal-button-group">
              <button
                onClick={() => handleChoose('hand')}
                disabled={!hasHand}
                className="npc-loss-discard-button"
              >
                Hand
              </button>
              <button
                onClick={() => handleChoose('bag')}
                disabled={!hasBag}
                style={{ padding: '10px 16px', cursor: hasBag ? 'pointer' : 'not-allowed' }}
                className="npc-loss-discard-button"
              >
                Bag
              </button>
            </div>
            {!hasHand && !hasBag && (
              <p style={{ color: '#c0392b', marginTop: 12 }}>
                You have no cards to discard.
              </p>
            )}
          </div>
        )}

        {step === 'select' && (
          <div>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 10 }}>
              {(source === 'hand' ? safeHand : safeBag).map((card, idx) => (
                <div
                  key={card.id || idx}
                  style={{ cursor: 'pointer', transition: 'transform 0.2s ease', borderRadius: 6 }}
                  onClick={() => handleSelectCard(card)}
                  onMouseEnter={(e) => { e.currentTarget.style.transform = 'translateY(-4px)'; }}
                  onMouseLeave={(e) => { e.currentTarget.style.transform = 'translateY(0)'; }}
                >
                  <img
                    src={`/resources${card.frontImage}`}
                    alt={`Carta ${card.letter}`}
                    style={{ width: 110, height: 'auto', borderRadius: 6, boxShadow: '0 4px 10px rgba(0,0,0,0.25)' }}
                  />
                </div>
              ))}
            </div>
          </div>
        )}

        <div style={{ marginTop: 18, textAlign: 'right' }}>
          {(!hasHand && !hasBag) ? (
            <button onClick={onClose} style={{ padding: '8px 12px' }}>
              Cerrar
            </button>
          ) : (
            null
          )}
        </div>
      </div>
    </div>
  );
}