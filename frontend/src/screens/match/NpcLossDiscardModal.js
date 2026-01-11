import React, { useEffect, useState } from 'react';

export default function NpcLossDiscardModal({
  isOpen,
  handCards = [],
  bagCards = [],
  onDiscard,
  onClose
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
    <div style={{
      position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
      background: 'rgba(0,0,0,0.6)', zIndex: 2100,
      display: 'flex', alignItems: 'center', justifyContent: 'center'
    }}>
      <div style={{
        background: '#ffffff', borderRadius: 10, padding: 20,
        width: 640, maxHeight: '80vh', overflowY: 'auto', boxShadow: '0 10px 30px rgba(0,0,0,0.25)'
      }}>
        {step === 'choose' && (
          <div>
            <h3 style={{ margin: 0, marginBottom: 12 }}>Has perdido contra un NPC</h3>
            <p style={{ marginTop: 0 }}>Elige desde dónde descartar una carta:</p>
            <div style={{ display: 'flex', gap: 12 }}>
              <button
                onClick={() => handleChoose('hand')}
                disabled={!hasHand}
                style={{ padding: '10px 16px', cursor: hasHand ? 'pointer' : 'not-allowed' }}
              >
                Hand
              </button>
              <button
                onClick={() => handleChoose('bag')}
                disabled={!hasBag}
                style={{ padding: '10px 16px', cursor: hasBag ? 'pointer' : 'not-allowed' }}
              >
                Bag
              </button>
            </div>
            {!hasHand && !hasBag && (
              <p style={{ color: '#c0392b', marginTop: 12 }}>
                No tienes cartas para descartar.
              </p>
            )}
          </div>
        )}

        {step === 'select' && (
          <div>
            <h4 style={{ margin: 0, marginBottom: 12 }}>Selecciona una carta de tu {source}</h4>
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
            <div style={{ marginTop: 16 }}>
              <button onClick={() => { setStep('choose'); setSource(null); }} style={{ padding: '8px 12px' }}>
                Volver
              </button>
            </div>
          </div>
        )}

        <div style={{ marginTop: 18, textAlign: 'right' }}>
          {(!hasHand && !hasBag) ? (
            <button onClick={onClose} style={{ padding: '8px 12px' }}>
              Cerrar
            </button>
          ) : (
            <span style={{ color: '#555', fontSize: 14 }}>Selecciona una carta para continuar.</span>
          )}
        </div>
      </div>
    </div>
  );
}