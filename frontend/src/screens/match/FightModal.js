import React, {useState, useEffect} from 'react';
import '../../static/css/match/fightModal.css';
import Fight from '../../static/images/Fight.png';
import tokenService from '../../services/token.service';
import getIdFromUrl from '../../util/getIdFromUrl';

export default function FightModal({ isOpen, onClose, defender, attacker, onResolve, stompClient }) {
    const currentUser = tokenService.getUser();
    const jwt = tokenService.getLocalAccessToken();
    const matchId = getIdFromUrl(2);
    
    const isAttacker = currentUser?.id === attacker?.user?.id;
    const isDefender = currentUser?.id === defender?.user?.id;

    const [whiteDice, setWhiteDice] = useState('1');
    const [blackDice, setBlackDice] = useState('1');
    const [whiteRolled, setWhiteRolled] = useState(false);
    const [blackRolled, setBlackRolled] = useState(false);

    // Suscribirse a las actualizaciones de dados en combate
    useEffect(() => {
        if (!stompClient || !stompClient.active || !isOpen) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.fight.dice`, (msg) => {
            const diceUpdate = JSON.parse(msg.body);
            
            if (diceUpdate.diceType === 'WHITE') {
                setWhiteDice(diceUpdate.diceValue.toString());
                setWhiteRolled(true);
            } else if (diceUpdate.diceType === 'BLACK') {
                setBlackDice(diceUpdate.diceValue.toString());
                setBlackRolled(true);
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, isOpen]);

    // Resetear dados cuando se abre el modal
    useEffect(() => {
        if (isOpen) {
            setWhiteDice('1');
            setBlackDice('1');
            setWhiteRolled(false);
            setBlackRolled(false);
        }
    }, [isOpen]);

    useEffect(() => {
        if (whiteRolled && blackRolled) {
            const w = parseInt(whiteDice, 10);
            const b = parseInt(blackDice, 10);
            const attackerWins = w >= b;  // En caso de empate "attacker" gana
            const currentUserWon = attackerWins ? isAttacker : isDefender;
            // pequeña pausa para mostrar el resultado visualmente
            setTimeout(() => {
                onResolve(currentUserWon);
            }, 700);
        }
    }, [whiteRolled, blackRolled]);

    const rollDice = async (diceType) => {
        const roll = Math.floor(Math.random() * 6) + 1;
        const diceTypeUpper = diceType === 'Negro' ? 'BLACK' : 'WHITE';
        
        // Notificar a todos los jugadores sobre la tirada
        await fetch(`/api/v1/matches/${matchId}/notify-fight-dice`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${jwt}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                matchId: matchId,
                playerId: currentUser.id,
                playerUsername: currentUser.username,
                diceType: diceTypeUpper,
                diceValue: roll
            })
        });

        // Actualizar localmente también
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

    if (!isOpen || !attacker || !defender) return null;

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
                            <div style={{ marginBottom: 6 }}>{attacker?.user?.username || 'Atacante'}</div>
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
                            <div style={{ marginBottom: 6 }}>{defender?.user?.username || 'Defensor'}</div>
                            <button
                                onClick={() => rollDice('Negro')}
                                style={{ border: "none", background: "transparent", padding: 0, cursor: blackRolled ? 'not-allowed' : 'pointer' }}
                                title="Dado Negro"
                                disabled={!isDefender || blackRolled}
                            >
                                <img src={`/Dice/N${blackDice}.png`} alt="Dado Negro" style={{ width: "80px", height: "auto", borderRadius: '18px' }} />
                            </button>
                        </div>
                        <img 
                            src={defender.user.avatar}
                            alt={`${defender.user.username}'s avatar`}
                            style={{ width: '40px', height: '40px', borderRadius: '50%' }}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
}
