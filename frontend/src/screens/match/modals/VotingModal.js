import { useState, useEffect } from 'react';
import tokenService from '../../../services/token.service'
import getIdFromUrl from '../../../util/getIdFromUrl'
import '../../../static/css/match/votingModal.css'

// para alerta de errores
import { toast } from "react-toastify";

export default function VotingModal({ isOpen, onClose, userProposingWeapon, weaponProposed, matchData, onSubmit, proposingUserId, stompClient }) {

    const jwt = tokenService.getLocalAccessToken()
    const currentUser = tokenService.getUser()
    const currentPlayer = matchData?.players?.find(p => p.user?.id === currentUser.id)
    const matchId = getIdFromUrl(2)
    
    const [answer, setAnswer] = useState(false)
    const [votes, setVotes] = useState(0)

    // Calcular cuántos jugadores hay en total y cuántos han votado
    // const totalPlayers = matchData?.players?.length-1 || 0; //  se resta uno para excluir al jugador que propuso el arma

    // Reiniciar estados cuando se abre una nueva votación
    useEffect(() => {
        if (isOpen) {
            setAnswer(false);
            setVotes(0);
        }
    }, [isOpen, weaponProposed]); 

    const submitVote = async (voteValue) => {
        try {
            const response = await fetch(`/api/v1/voting/vote/${matchId}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${jwt}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    playerId: currentPlayer.id,
                    inFavor: voteValue, // 'YES' | 'NO'
                })
            });
            
            if (response.ok){
                const data = await response.json();
                setAnswer(voteValue); // guardamos que ha votado
                setVotes(prevVotes => prevVotes + 1);
                
                // Si la votación ha terminado, el backend lo notificará por WebSocket
                // No necesitamos hacer nada aquí
            } else {
                const error = await response.json();
                throw error;
            }

        } catch (error) {
            console.error('Error submitting vote', error);
            toast.error(error.message)
        }
    }

    // Suscribirse a actualizaciones de votación desde el backend
    useEffect(() => {
        if (!stompClient || !stompClient.active || !isOpen) return;

        const subscription = stompClient.subscribe(`/topic/match.${matchId}.weapon.voting.result`, (msg) => {
            const votingResult = JSON.parse(msg.body);
            if (votingResult.status === 'FINISHED') {
                onSubmit(votingResult);
            }
        });

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, isOpen, onSubmit]);

    if (!isOpen) return null

    return (
        <div className='modal-voting-overlay'>
            <div className='modal-voting-content-wrapper'>

                {currentUser.id !== proposingUserId ? (
                    <div>

                        <h2>Weapon proposed</h2>
                        <p className="weapon-name">"{weaponProposed}"</p>
                        <p className="proposed-by">
                            Proposed by {userProposingWeapon?.username || 'Unknown'}
                        </p>

                        <button
                            disabled={answer !== false}
                            onClick={() => {
                                submitVote('YES')
                            }}>
                            YES 
                        </button>

                        <button
                            disabled={answer !== false}
                            onClick={() => {
                                submitVote('NO')
                            }}>
                            NO 
                        </button>
                    
                    </div>
                )
                    : null

                }

                {currentUser.id === proposingUserId ? (
                    <h1> Waiting for the voting to finished</h1>
                ) : null }

            </div>

        </div>
        
    )
}
