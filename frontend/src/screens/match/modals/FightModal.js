import React, {useState, useEffect} from 'react';
import '../../../static/css/match/fightModal.css';
import Fight from '../../../static/images/Fight.png';
import tokenService from '../../../services/token.service';
import getIdFromUrl from '../../../util/getIdFromUrl';
import WeaponModal from './WeaponModal';

export default function FightModal({ isOpen, onClose, defender, attacker, onResolve, stompClient, bagCards = [], matchData, votingResult, proposingUserId, onVotingResultProcessed }) {
    const currentUser = tokenService.getUser();
    const jwt = tokenService.getLocalAccessToken();
    const matchId = getIdFromUrl(2);
    const isAttacker = currentUser?.id === attacker?.user?.id;
    const isDefender = currentUser?.id === defender?.user?.id;
    const isDefenderBot = !defender?.user; // El defensor es un bot si no tiene .user

    const [buttonStateAttacker, setButtonStateAttacker] = useState(false);
    const [buttonStateDefender, setButtonStateDefender] = useState(false);
    // fuerza de cada jugador 
    const [attackerStrength, setAttackerStrength] = useState(attacker?.strength || 1);
    const [defenderStrength, setDefenderStrength] = useState(defender?.strength || 1);

    //total de puntos
    const [totalAttacker, setTotalAttacker] = useState(0)
    const [totalDefender, setTotalDefender] = useState(0)

    // armas seleccionadas 
    const [weaponsAttacker, setWeaponsAttacker] = useState([]);
    const [weaponsDefender, setWeaponsDefender] = useState([]);

    const [whiteDice, setWhiteDice] = useState('1');
    const [blackDice, setBlackDice] = useState('1');
    const [whiteRolled, setWhiteRolled] = useState(false);
    const [blackRolled, setBlackRolled] = useState(false);
    const [whiteRolling, setWhiteRolling] = useState(false);
    const [blackRolling, setBlackRolling] = useState(false);

    // Estado del modal de armas
    const [isWeaponModalOpen, setIsWeaponModalOpen] = useState(false);
    const [currentWeaponUser, setCurrentWeaponUser] = useState(null);

    useEffect(() => {
        setAttackerStrength(Math.min(6, attacker?.strength || 1));
        setDefenderStrength(Math.min(6, defender?.strength || 1));
    }, [attacker?.strength, defender?.strength]);

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

    // Suscribirse a las actualizaciones de totales en combate
    useEffect(() => {
        if (!stompClient || !stompClient.active || !isOpen) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.fight.totals`, (msg) => {
            const totalsUpdate = JSON.parse(msg.body);
            
            setTotalAttacker(totalsUpdate.attackerTotal);
            setTotalDefender(totalsUpdate.defenderTotal);
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, isOpen]);

    useEffect(() => {
        if (!stompClient || !stompClient.active || !isOpen) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.fight.weapons`, (msg) => {
            const weaponsUpdate = JSON.parse(msg.body);
            
            if (weaponsUpdate.playerRole === 'ATTACKER') {
                setWeaponsAttacker(weaponsUpdate.weapons || []);
                setTotalAttacker(weaponsUpdate.totalAttacker);
            } else if (weaponsUpdate.playerRole === 'DEFENDER') {
                setWeaponsDefender(weaponsUpdate.weapons || []);
                setTotalDefender(weaponsUpdate.totalDefender);
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, isOpen]);

    // Suscribirse a las actualizaciones del estado Ready
    useEffect(() => {
        if (!stompClient || !stompClient.active || !isOpen) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.fight.ready`, (msg) => {
            const readyUpdate = JSON.parse(msg.body);
            
            if (readyUpdate.playerRole === 'ATTACKER') {
                setButtonStateAttacker(readyUpdate.isReady);
            } else if (readyUpdate.playerRole === 'DEFENDER') {
                setButtonStateDefender(readyUpdate.isReady);
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, isOpen]);

    // Manejar resultado de votación de arma
    useEffect(() => {
        if (!votingResult || votingResult.status !== 'FINISHED') return;

        const handleVotingResult = async () => {
            if (votingResult.result === 'ACCEPTED') {
                // Determinar quién propuso el arma comparando proposingUserId con attacker y defender
                const proposingPlayerIsAttacker = proposingUserId === attacker?.user?.id;
                const playerRole = proposingPlayerIsAttacker ? 'ATTACKER' : 'DEFENDER';

                // Crear el objeto weaponData con la palabra propuesta y el bonus
                const weaponData = {
                    word: votingResult.proposedWeapon,
                    bonus: votingResult.finalBonus,
                    cards: [] // Las cartas ya se quitaron de la bolsa, solo guardamos la palabra
                };

                // Agregar el arma a los totales del combate
                await addWeaponToFight(weaponData, playerRole);
            }

            // Cerrar el modal de arma
            setIsWeaponModalOpen(false);
            setCurrentWeaponUser(null);

            // Notificar que ya procesamos el resultado
            if (onVotingResultProcessed) {
                onVotingResultProcessed();
            }
        };

        handleVotingResult();
    }, [votingResult, attacker?.user?.id, currentUser?.id, onVotingResultProcessed]);

    // Resetear dados cuando se abre el modal
    useEffect(() => {
        if (isOpen) {
            setWhiteDice('1');
            setBlackDice('1');
            setWhiteRolled(false);
            setBlackRolled(false);

            setTotalAttacker(attackerStrength)
            setTotalDefender(defenderStrength)

            setWeaponsAttacker([]);
            setWeaponsDefender([]);

            setButtonStateAttacker(false);
            setButtonStateDefender(false);
        }
    }, [isOpen]);

    // Lanzar dado automáticamente si el defensor es un bot
    useEffect(() => {
        if (isOpen && isDefenderBot && !blackRolled) {
            const timer = setTimeout(() => {
                rollDice('Negro');
            }, 1000); // Esperar 1 segundo después de abrir el modal
            return () => clearTimeout(timer);
        }
    }, [isOpen, isDefenderBot, blackRolled]);

    
    useEffect(() => {
        if (whiteRolled) {
            setTotalAttacker(attackerStrength + parseInt(whiteDice, 10) + getTotalWeaponsBonus(weaponsAttacker));
        }
    }, [weaponsAttacker, attackerStrength, whiteDice, whiteRolled]);

    
    useEffect(() => {
        if (blackRolled) {
            setTotalDefender(defenderStrength + parseInt(blackDice, 10) + getTotalWeaponsBonus(weaponsDefender));
        }
    }, [weaponsDefender, defenderStrength, blackDice, blackRolled]);

    
    useEffect(() => {
        if (buttonStateAttacker && buttonStateDefender && whiteRolled && blackRolled) {
            
            const attackerWins = totalAttacker >= totalDefender;  
            const currentUserWon = attackerWins ? isAttacker : isDefender;
            setTimeout(() => {
                onResolve(currentUserWon);
            }, 700);
        }
    }, [buttonStateAttacker, buttonStateDefender, whiteRolled, blackRolled, totalAttacker, totalDefender]);

    // Hacer que el bot haga ready automáticamente después de lanzar el dado
    useEffect(() => {
        if (isDefenderBot && blackRolled && !buttonStateDefender) {
            const timer = setTimeout(() => {
                toggleReadyState('DEFENDER', false);
            }, 1500); // Esperar un poco después de lanzar el dado
            return () => clearTimeout(timer);
        }
    }, [isDefenderBot, blackRolled, buttonStateDefender]);

    const rollDice = async (diceType) => {
        const roll = Math.floor(Math.random() * 6) + 1;
        const diceTypeUpper = diceType === 'Negro' ? 'BLACK' : 'WHITE';
        const setRolling = diceType === 'Negro' ? setBlackRolling : setWhiteRolling;
        setRolling(true);
        try {
            let newTotalAttacker = totalAttacker;
            let newTotalDefender = totalDefender;
            
            if (diceType === 'Negro') {
                newTotalDefender = defenderStrength + roll + weaponsDefender;
                setTotalDefender(newTotalDefender);
                setBlackDice(roll.toString());
                setBlackRolled(true);
            }
            if (diceType === 'Blanco') {
                newTotalAttacker = attackerStrength + roll + weaponsAttacker;
                setTotalAttacker(newTotalAttacker);
                setWhiteDice(roll.toString());
                setWhiteRolled(true);
            }
            
            await fetch(`/api/v1/fights/${matchId}/notify-fight-dice`, {
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

            await fetch(`/api/v1/fights/${matchId}/notify-dice-totals`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${jwt}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    matchId: matchId,
                    attackerId: attacker?.id,
                    attackerTotal: diceType === 'Blanco' ? newTotalAttacker : totalAttacker,
                    defenderId: defender.id,
                    defenderTotal: diceType === 'Negro' ? newTotalDefender : totalDefender
                })
            });

            return roll;
        } finally {
            setTimeout(() => setRolling(false), 200);
        }
    };

    const toggleReadyState = async (playerRole, currentState) => {
        const newState = !currentState;
        
        
        if (playerRole === 'ATTACKER') {
            setButtonStateAttacker(newState);
        } else {
            setButtonStateDefender(newState);
        }
        
        await fetch(`/api/v1/fights/${matchId}/notify-ready-state`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${jwt}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                matchId: matchId,
                playerId: currentUser.id,
                playerRole: playerRole,
                isReady: newState
            })
        });
    };

    const openWeaponModal = (role) => {
        setCurrentWeaponUser(role);
        setIsWeaponModalOpen(true);
    };

    const getTotalWeaponsBonus = (weapons) => {
        return weapons.reduce((sum, w) => sum + (w.bonus || 0), 0);
    };

    const addWeaponToFight = async (weaponData, playerRole) => {
        let newWeapons, newTotal;
        
        if (playerRole === 'ATTACKER') {
            newWeapons = [...weaponsAttacker, weaponData];
            newTotal = attackerStrength + parseInt(whiteDice, 10) + getTotalWeaponsBonus(newWeapons);
            setWeaponsAttacker(newWeapons);
            setTotalAttacker(newTotal);
        } else {
            newWeapons = [...weaponsDefender, weaponData];
            newTotal = defenderStrength + parseInt(blackDice, 10) + getTotalWeaponsBonus(newWeapons);
            setWeaponsDefender(newWeapons);
            setTotalDefender(newTotal);
        }
        
        await fetch(`/api/v1/fights/${matchId}/notify-fight-weapons`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${jwt}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                matchId: matchId,
                playerId: currentUser.id,
                playerRole: playerRole,
                weapons: newWeapons,
                totalAttacker: playerRole === 'ATTACKER' ? newTotal : totalAttacker,
                totalDefender: playerRole === 'DEFENDER' ? newTotal : totalDefender
            })
        });
    };

    const handleWeaponSelected = async (weaponData) => {
        await addWeaponToFight(weaponData, currentWeaponUser);
        
        setIsWeaponModalOpen(false);
        setCurrentWeaponUser(null);
    };

    if (!isOpen || !attacker || !defender) return null;

    return (
        <div className="modal-fight-overlay">
            <div className="modal-fight-content-wrapper">
                
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

                            <div className="total-box">{whiteRolled ? totalAttacker : '?'}</div>

                            {/*FUERZA */}
                            <div className="calc-row">
                                <div className="calc-box"> Strength: {attackerStrength}</div>

                                <span className="calc-operator">+</span>
                                        
                                <button
                                    onClick={() => rollDice('Blanco')}
                                    className='dice-button'
                                    title="Dado Blanco"
                                    disabled={!isAttacker || whiteRolled || (isDefenderBot && !blackRolled)}
                                >
                                    <img
                                        src={`/Dice/B${whiteDice}.png`}
                                        alt="Dado Blanco"
                                        className={`dice ${whiteRolling ? 'rolling' : ''}`}
                                    />
                                </button>

                                <span className="calc-operator">+</span>

                                <div className="calc-box">Weapons: {getTotalWeaponsBonus(weaponsAttacker)}</div>
                            </div>
                            
                        </div>

                        <div className='vs-container'>
                            <img src={Fight} alt="Fight" className="fight-logo-central" />
                        </div>

                        <div className='combat-panel'> {/*zona del oponente */}
                            <div className='combat-header'> 
                                <span>{defender?.user?.username || (defender?.isNiallCampbell ? 'NiallCampbell' : 'NPC')}</span>
                                {defender?.user?.avatar ? (
                                    <img 
                                        src={defender.user.avatar}
                                        alt={`${defender.user.username}'s avatar`}
                                        style={{ width: '40px', height: '40px', borderRadius: '50%' }}
                                    />
                                ) : (
                                    <div 
                                        style={{ 
                                            width: '40px', 
                                            height: '40px', 
                                            borderRadius: '50%',
                                            backgroundColor: defender?.isNiallCampbell ? '#ff0000' : '#666',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                            color: 'white',
                                            fontWeight: 'bold'
                                        }}
                                    >
                                        {defender?.isNiallCampbell ? 'N' : 'X'}
                                    </div>
                                )}
                            </div>

                            <div className="total-box">{blackRolled ? totalDefender : '?'}</div>

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
                                    <img
                                        src={`/Dice/N${blackDice}.png`}
                                        alt="Dado Negro"
                                        className={`dice ${blackRolling ? 'rolling' : ''}`}
                                    />
                                </button>

                                <span className="calc-operator">+</span>

                                <div className="calc-box">Weapons: {getTotalWeaponsBonus(weaponsDefender)}</div>
                            </div>
                        </div>


                    </div>


                    {/* ZONA INFERIOR */}
                    <div className="combat-bottom"> 

                        <div className='action-column'>
                            <div className='actions'>
                                <button 
                                    className="action-button"
                                    onClick={() => openWeaponModal('ATTACKER')}
                                    disabled={!isAttacker}
                                    title={isAttacker ? "Weapon" : "Solo el atacante puede formar arma"}
                                >
                                    Weapon
                                </button>
                            </div>
                            <button 
                                className={`ready-button ${buttonStateAttacker ? 'green' : ''}`}
                                onClick={() => toggleReadyState('ATTACKER', buttonStateAttacker)}
                                disabled={!isAttacker}
                                title={isAttacker ? "Ready" : "Solo el atacante puede pulsar listo"}
                            >
                                Ready
                            </button>
                        </div>

                        <div className='action-column'>
                            <div className='actions'>
                                <button 
                                    className="action-button"
                                    onClick={() => openWeaponModal('DEFENDER')}
                                    disabled={!isDefender}
                                    title={isDefender ? "Weapon" : "Solo el defensor puede formar arma"}
                                >
                                    Weapon
                                </button>
                            </div>
                            <button 
                                className={`ready-button ${buttonStateDefender ? 'green' : 'red'}`}
                                onClick={() => toggleReadyState('DEFENDER', buttonStateDefender)}
                                disabled={!isDefender}
                                title={isDefender ? "Ready" : "Solo el defensor puede pulsar listo"}
                            > 
                                Ready
                            </button>
                        </div>

                    </div>


                </div>
            </div>

            <WeaponModal
                isVisible={isWeaponModalOpen}
                bagCards={bagCards}
                onClose={() => {
                    setIsWeaponModalOpen(false);
                    setCurrentWeaponUser(null);
                }}
                player={currentWeaponUser === 'ATTACKER' ? attacker : defender}
                onWeaponSelected={handleWeaponSelected}
                matchData={matchData}
                stompClient={stompClient}
            />
        </div>
    );
}
