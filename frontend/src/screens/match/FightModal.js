import React, {useState, useEffect} from 'react';
import '../../static/css/match/fightModal.css';
import Fight from '../../static/images/Fight.png';
import tokenService from '../../services/token.service';

export default function FightModal({ isOpen, onClose, opponent, attacker, onResolve }) {
    const currentUser = tokenService.getUser();
    const isAttacker = currentUser?.id === attacker?.user?.id;
    const isOpponent = currentUser?.id === opponent?.user?.id;

    const [whiteDice, setWhiteDice] = useState('1');
    const [blackDice, setBlackDice] = useState('1');
    const [whiteRolled, setWhiteRolled] = useState(false);
    const [blackRolled, setBlackRolled] = useState(false);

    useEffect(() => {
        if (whiteRolled && blackRolled) {
            const w = parseInt(whiteDice, 10);
            const b = parseInt(blackDice, 10);
            const attackerWins = w >= b;  // En caso de empate "attacker" gana
            const currentUserWon = attackerWins ? isAttacker : isOpponent;
            // pequeña pausa para mostrar el resultado visualmente
            setTimeout(() => {
                onResolve(currentUserWon);
            }, 700);
        }
    }, [whiteRolled, blackRolled]);

    const rollDice = (diceType) => {
        const roll = Math.floor(Math.random() * 6) + 1;
        if (diceType === 'Negro') {
            setBlackDice(roll.toString());
            setBlackRolled(true);
        }
        if (diceType === 'Blanco') {
            setWhiteDice(roll.toString());
            setWhiteRolled(true);
        }
        return roll;
    };

    if (!isOpen) return null;

    return (
        <div className="modal-fight-overlay" onClick={onClose}>
            <div className="modal-fight-content-wrapper" onClick={(e) => e.stopPropagation()}>
                <img src={Fight} alt="Fight" style={{ alignSelf: 'center', width: '75%', borderRadius: '10px' }} />
                <div style={{ display: 'flex', gap: '8px', justifyContent: 'center', alignItems: 'center' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                        <img 
                            src={attacker.user.avatar}
                            alt={`${attacker.user.username}'s avatar`}
                            style={{ width: '40px', height: '40px', borderRadius: '50%' }}
                        />
                        <div style={{ textAlign: 'center' }}>
                            <div style={{ marginBottom: 6 }}>{attacker?.user?.username || 'Agresor'}</div>
                            <button
                                onClick={() => rollDice('Blanco')}
                                style={{ border: "none", background: "transparent", padding: 0, cursor: whiteRolled ? 'not-allowed' : 'pointer' }}
                                title="Dado Blanco"
                                disabled={!isAttacker || whiteRolled}
                            >
                                <img src={`/Dice/B${whiteDice}.png`} alt="Dado Blanco" style={{ width: "80px", height: "auto", borderRadius: '18px' }} />
                            </button>
                        </div>
                    </div>

                    <div style={{ width: 30, textAlign: 'center' }}>VS</div>

                    <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                        <div style={{ textAlign: 'center' }}>
                            <div style={{ marginBottom: 6 }}>{opponent?.user?.username || 'Oponente'}</div>
                            <button
                                onClick={() => rollDice('Negro')}
                                style={{ border: "none", background: "transparent", padding: 0, cursor: blackRolled ? 'not-allowed' : 'pointer' }}
                                title="Dado Negro"
                                disabled={!isOpponent || blackRolled}
                            >
                                <img src={`/Dice/N${blackDice}.png`} alt="Dado Negro" style={{ width: "80px", height: "auto", borderRadius: '18px' }} />
                            </button>
                        </div>
                        <img 
                            src={opponent.user.avatar}
                            alt={`${opponent.user.username}'s avatar`}
                            style={{ width: '40px', height: '40px', borderRadius: '50%' }}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
}
