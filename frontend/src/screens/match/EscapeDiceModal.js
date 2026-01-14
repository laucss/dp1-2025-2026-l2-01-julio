import React, { useState } from 'react';
import '../../static/css/match/startDiceModal.css';
import tokenService from '../../services/token.service';
import getIdFromUrl from '../../util/getIdFromUrl';

export default function EscapeDiceModal({ isOpen, onClose, onResult }) {
  const jwt = tokenService.getLocalAccessToken();
  const currentUser = tokenService.getUser();
  const matchId = getIdFromUrl(2);

  const [whiteDice, setWhiteDice] = useState('1');
  const [blackDice, setBlackDice] = useState('1');
  const [diceRolled, setDiceRolled] = useState(false);
  const [isRolling, setIsRolling] = useState(false);

  const rollDice = () => {
    const rollWhite = Math.floor(Math.random() * 6) + 1;
    const rollBlack = Math.floor(Math.random() * 6) + 1;
    return [rollWhite, rollBlack];
  };

  const submitEscapeAttempt = async (totalRoll) => {
    try {
      const resp = await fetch(`/api/v1/matches/${matchId}/escape-attempt?userId=${currentUser.id}&rollDice=${totalRoll}`, {
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
    if (diceRolled || isRolling) return;
    setIsRolling(true);

    const animationInterval = setInterval(() => {
      const randomWhite = Math.floor(Math.random() * 6) + 1;
      const randomBlack = Math.floor(Math.random() * 6) + 1;
      setWhiteDice(randomWhite.toString());
      setBlackDice(randomBlack.toString());
    }, 100);

    setTimeout(() => {
      clearInterval(animationInterval);
      const [white, black] = rollDice();
      setWhiteDice(white.toString());
      setBlackDice(black.toString());
      setDiceRolled(true);
      setIsRolling(false);
      submitEscapeAttempt(white + black);
    }, 1500);
  };

  if (!isOpen) return null;

  return (
    <div className="start-dice-modal-overlay">
      <div className="start-dice-modal-content">
        <h2 className="start-dice-title">Intentar escapar</h2>

        <div className="start-dice-container">
          <div className="dice-wrapper">
            <img src={`/Dice/B${whiteDice}.png`} alt="White Die" className={`start-dice white-dice ${isRolling ? 'rolling' : ''}`} />
          </div>
          <div className="dice-wrapper">
            <img src={`/Dice/N${blackDice}.png`} alt="Black Die" className={`start-dice black-dice ${isRolling ? 'rolling' : ''}`} />
          </div>
        </div>

        <button onClick={throwDice} className={`throw-dice-button ${diceRolled || isRolling ? 'disabled' : ''}`} disabled={diceRolled || isRolling}>
          {isRolling ? 'Throwing...' : diceRolled ? 'thrown dice' : 'Roll the dice'}
        </button>

        {diceRolled && (
          <p className="dice-result-text">Resultado: {parseInt(whiteDice) + parseInt(blackDice)}</p>
        )}

        <button onClick={() => { onClose(); }} style={{ marginTop: 12 }}>Cancelar</button>
      </div>
    </div>
  );
}
