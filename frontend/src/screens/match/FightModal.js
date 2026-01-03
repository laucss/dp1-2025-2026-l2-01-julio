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

    // fuerza de cada jugador 
    const [attackerStrength, setAttackerStrength] = useState(attacker?.strength || 1);
    const [defenderStrength, setDefenderStrength] = useState(defender?.strength || 1);

    //total de puntos
    const [totalAttacker, setTotalAttacker] = useState(0)
    const [totalDefender, setTotalDefender] = useState(0)

    // puntos de los bonus de armas
    const [weaponsAttacker, setWeaponsAttacker] = useState(0);
    const [weaponsDefender, setWeaponsDefender] = useState(0);

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

            setTotalAttacker(attackerStrength)
            setTotalDefender(defenderStrength)

            setWeaponsAttacker(0);
            setWeaponsDefender(0);

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
            setTotalDefender(totalDefender+roll + weaponsDefender)
            setBlackDice(roll.toString());
            setBlackRolled(true);
        }
        if (diceType === 'Blanco') {
            setTotalAttacker(totalDefender+roll + weaponsAttacker)
            setWhiteDice(roll.toString());
            setWhiteRolled(true);
        }
        return roll;
    };

    if (!isOpen || !attacker || !defender) return null;

    return (
        <div className="modal-fight-overlay" onClick={onClose}>
            <div className="modal-fight-content-wrapper" onClick={(e) => e.stopPropagation()}>
                
                <div className='combat-container'>
                    
                    <div className='combat-top'> 

                        <div className='combat-panel'> {/*zona del atacante */}
                            <div className='combat-header'> 
                                <span>{attacker?.user?.username || 'Attacker'}</span>
                                <img 
                                    src={attacker.user.avatar}
                                    alt={`${attacker.user.username}'s avatar`}
                                    style={{ width: '40px', height: '40px', borderRadius: '50%' }}
                                />
                            </div>

                            <div className="total-box">{totalAttacker}</div>

                            {/*FUERZA */}
                            <div className="calc-row">
                                <div className="calc-box"> Strength: {attackerStrength}</div>

                                <span className="calc-operator">+</span>
                                        
                                <button
                                    onClick={() => rollDice('Blanco')}
                                    className='dice-button'
                                    title="Dado Blanco"
                                    disabled={!isAttacker || whiteRolled}
                                >
                                    <img src={`/Dice/B${whiteDice}.png`} alt="Dado Blanco" className='dice' />
                                </button>

                                <span className="calc-operator">+</span>

                                <div className="calc-box">Weapons: {weaponsAttacker}</div>
                            </div>
        
                        </div>

                        <div className='vs-container'>
                            <img src={Fight} alt="Fight" className="fight-logo-central" />
                        </div>

                        <div className='combat-panel'> {/*zona del oponente */}
                            <div className='combat-header'> 
                                <span>{defender?.user?.username || "Defender"}</span>
                                <img 
                                    src={defender.user.avatar}
                                    alt={`${defender.user.username}'s avatar`}
                                    style={{ width: '40px', height: '40px', borderRadius: '50%' }}
                                />
                            </div>

                            <div className="total-box">{totalDefender}</div>

                            {/*FUERZA */}
                            <div className="calc-row">
                                <div className="calc-box">Strength: {defenderStrength}</div>

                                <span className="calc-operator">+</span>
                                        
                                <button
                                    onClick={() => rollDice("Negro")}
                                    disabled={!isDefender || blackRolled}
                                    className='dice-button'
                                    title="Dado Negro"

                                >
                                    <img src={`/Dice/N${blackDice}.png`} alt="Dado Negro" className='dice' />
                                </button>

                                <span className="calc-operator">+</span>

                                <div className="calc-box">Weapons: {weaponsDefender}</div>
                            </div>
                            
                        </div>


                    </div>


                    {/* ZONA INFERIOR */}
                    <div className="combat-bottom"> 

                        <div className='actions'>
                            <button className="action-button">Formar arma</button>
                        </div>

                        <div className='actions'>
                            <button className="action-button">Formar arma</button>
                        </div>

                    </div>


                </div>
            </div>
        </div>
    );
}