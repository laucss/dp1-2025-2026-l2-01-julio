import React, { useState } from 'react';
import '../../../static/css/match/modals/startDiceModal.css';
import tokenService from '../../../services/token.service';
import getIdFromUrl from '../../../util/getIdFromUrl';
import { toast } from 'react-toastify';

export default function EscapeDiceModal({ isOpen, onClose, onResult, canAttemptEscape = true, escapeAttemptReason = '' }) {
  const jwt = tokenService.getLocalAccessToken();
  const currentUser = tokenService.getUser();
  const matchId = getIdFromUrl(2);

  const [whiteDice, setWhiteDice] = useState('1');
  const [diceRolled, setDiceRolled] = useState(false);
  const [isRolling, setIsRolling] = useState(false);
  const [totalRoll, setTotalRoll] = useState(null);

  const rollDice = () => {
    return Math.floor(Math.random() * 6) + 1;
  };

  const submitEscapeAttempt = async (totalRoll) => {
    try {
      const resp = await fetch(`/api/v1/actions/${matchId}/escape-attempt?userId=${currentUser.id}&rollDice=${totalRoll}`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${jwt}`,
          'Content-Type': 'application/json'
        }
      });

      if (!resp.ok) {
        const body = await resp.text();
        throw new Error(`Escape attempt failed: ${resp.status} ${body}`);
      }

      const result = await resp.json();
      if (onResult) onResult(result);
    } catch (err) {
      console.error('Error attempting escape:', err);
    } finally {
      onClose();
    }
  };

  const throwDice = () => {
    if (!canAttemptEscape) {
      toast.error(escapeAttemptReason || 'Conditions not met to attempt escape.');
      return;
    }

    if (diceRolled || isRolling) return;
    setIsRolling(true);

    const animationInterval = setInterval(() => {
      const randomWhite = Math.floor(Math.random() * 6) + 1;
      setWhiteDice(randomWhite.toString());
    }, 100);

    setTimeout(() => {
      clearInterval(animationInterval);
      const white = rollDice();
      setWhiteDice(white.toString());
      setTotalRoll(white);
      setDiceRolled(true);
      setIsRolling(false);
      // Wait for user confirmation before submitting
    }, 1500);
  };

  if (!isOpen) return null;

  return (
    <div className="start-dice-modal-overlay">
      <div className="start-dice-modal-content">
        <h2 className="start-dice-title">Attempt Escape</h2>

        <div className="start-dice-container">
          <div className="dice-wrapper">
            <img src={`/Dice/B${whiteDice}.png`} alt="Die" className={`start-dice white-dice ${isRolling ? 'rolling' : ''}`} />
          </div>
        </div>

        <button onClick={throwDice} className={`throw-dice-button ${diceRolled || isRolling ? 'disabled' : ''}`} disabled={diceRolled || isRolling}>
          {isRolling ? 'Rolling...' : diceRolled ? 'Dice rolled' : 'Roll Dice'}
        </button>

        {diceRolled && (
          <>
            <p className="dice-result-text">Result: {parseInt(whiteDice)}</p>
            <button
              className="confirm-dice-button"
              onClick={() => {
                if (totalRoll != null && !isRolling) {
                  submitEscapeAttempt(totalRoll);
                }
              }}
            >
              Confirm
            </button>
          </>
        )}

      </div>
    </div>
  );
}
