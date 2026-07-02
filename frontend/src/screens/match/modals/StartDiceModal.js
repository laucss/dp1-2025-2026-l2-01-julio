import React, { useState } from "react";
import '../../../static/css/match/startDiceModal.css';
import tokenService from '../../../services/token.service';
import getIdFromUrl from '../../../util/getIdFromUrl';

export default function StartDiceModal({ isOpen, onClose, onDiceRolled, matchData }) {
    const jwt = tokenService.getLocalAccessToken();
    const currentUser = tokenService.getUser();
    const matchId = getIdFromUrl(2);

    const [whiteDice, setWhiteDice] = useState("1");
    const [blackDice, setBlackDice] = useState("1");
    const [diceRolled, setDiceRolled] = useState(false);
    const [isRolling, setIsRolling] = useState(false);

    const rollDice = () => {
        const rollWhite = Math.floor(Math.random() * 6) + 1;
        const rollBlack = Math.floor(Math.random() * 6) + 1;
        return [rollWhite, rollBlack];
    };

    const submitDiceToBackend = async (totalRoll) => {
        try {
            const response = await fetch(`/api/v1/matches/${matchId}/submit-dice?userId=${currentUser.id}&diceRoll=${totalRoll}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${jwt}`,
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
                let errorBody;
                try {
                    errorBody = await response.json();
                } catch {
                    errorBody = await response.text();
                }
                throw new Error(`Error al enviar tirada de dado: ${response.status} ${response.statusText}`);
            }

            const updatedMatch = await response.json();
            console.log("Match actualizado tras tirar dado:", updatedMatch);
            
            if (onDiceRolled) {
                onDiceRolled(updatedMatch);
            }

            // Verificar si todos los jugadores han lanzado los dados
            // const totalPlayers = updatedMatch.players?.length || 0;
            // const playersWithDiceRolls = updatedMatch.diceRolls ? Object.keys(updatedMatch.diceRolls).length : 0;
            
            // Solo cerrar el modal si todos los jugadores han lanzado
            if (allPlayersRolled) { // CAMBIÉ ESTO -----------------------------------------------------------------------------------------------------
                setTimeout(() => {
                    onClose();
                }, 10000); 
            }

        } catch (err) {
            console.error(err);
        }
    };

    const throwDice = () => {
        if (diceRolled || isRolling) return;

        setIsRolling(true);

        // Animación de dados girando
        const animationInterval = setInterval(() => {
            const randomWhite = Math.floor(Math.random() * 6) + 1;
            const randomBlack = Math.floor(Math.random() * 6) + 1;
            setWhiteDice(randomWhite.toString());
            setBlackDice(randomBlack.toString());
        }, 100);

        // Detener animación y mostrar resultado final
        setTimeout(() => {
            clearInterval(animationInterval);
            const [white, black] = rollDice();
            setWhiteDice(white.toString());
            setBlackDice(black.toString());
            setDiceRolled(true);
            setIsRolling(false);
            submitDiceToBackend(white + black);
        }, 1500);
    };

    if (!isOpen) return null;

    // Calcular cuántos jugadores han lanzado los dados
    const totalPlayers = matchData?.players?.length || 0;
    const playersWithDiceRolls = matchData?.diceRolls ? Object.keys(matchData.diceRolls).length : 0;
    const allPlayersRolled = playersWithDiceRolls >= totalPlayers && totalPlayers > 0;

    return (
        <div className="start-dice-modal-overlay">
            <div className="start-dice-modal-content">
                <h2 className="start-dice-title">Determine Turn Order</h2>

                <div className="start-dice-container">
                    <div className="dice-wrapper">
                        <img 
                            src={`/Dice/B${whiteDice}.png`} 
                            alt="White Die" 
                            className={`start-dice white-dice ${isRolling ? 'rolling' : ''}`}
                        />
                    </div>
                    
                    <div className="dice-wrapper">
                        <img 
                            src={`/Dice/N${blackDice}.png`} 
                            alt="Black Die" 
                            className={`start-dice black-dice ${isRolling ? 'rolling' : ''}`}
                        />
                    </div>
                </div>

                <button 
                    onClick={throwDice} 
                    className={`throw-dice-button ${diceRolled || isRolling ? 'disabled' : ''}`}
                    disabled={diceRolled || isRolling}
                >
                    {isRolling ? 'Throwing...' : diceRolled ? 'thrown dice' : 'Roll the dice'}
                </button>

                {diceRolled && (
                    <p className="dice-result-text">
                        Resultado: {parseInt(whiteDice) + parseInt(blackDice)}
                    </p>
                )}
                
                {allPlayersRolled && (
                    <p className="all-rolled-text">
                        ¡Todos los jugadores han lanzado! Cerrando modal...
                    </p>
                )}
            </div>
        </div>
    );
}

