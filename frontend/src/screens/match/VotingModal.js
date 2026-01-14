import { useState } from 'react';
import tokenService from '../../services/token.service'
import getIdFromUrl from '../../util/getIdFromUrl'
import '../../static/css/match/votingModal.css'

// para alerta de errores
import { toast } from "react-toastify";

export default function StartDiceModal({ isOpen, onClose, userProposingWeapon, weaponProposed, matchData, onSubmit }) {

    const jwt = tokenService.getLocalAccessToken()
    const currentUser = tokenService.getUser()
    const currentPlayer = matchData?.players?.find(p => p.userId === currentUser.id)
    const matchId = getIdFromUrl(2)

    const [answer, setAnswer] = useState(false)
    const [votes, setVotes] = useState(0)

    // Calcular cuántos jugadores hay en total y cuántos han votado
    const totalPlayers = matchData?.players?.length-1 || 0; //  se resta uno para excluir al jugador que propuso el arma 
    // const votes = 
    // const allPlayersVoted = votes >= totalPlayers && totalPlayers > 0;
    

    if (!isOpen) return null

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
        })
            setAnswer(voteValue) // guardamos que ha votado 
            setVotes(prevVotes => prevVotes + 1)
            
            if (response.ok){
                const data = await response.json()
                if (data.status === 'FINISHED'){
                    onSubmit(data) // mandamos todo el objeto para que se lea el resultado y se use la bonificación
                }
            }

            if (!response.ok){
                const error = await response.json();
                throw error;
            }

            else {
                toast.error("There has been an error while submitting your vote.")
            }

        } catch (error) {
            console.error('Error submitting vote', error);
            toast.error(error.message)
        }
    }



    
    return (
        <div className='modal-voting-overlay '>
            <div className='modal-voting-content-wrapper'>

                {currentUser.id !== userProposingWeapon.id ? (
                    <div>

                        <h2>Weapon proposed</h2>
                        <p className="weapon-name">"{weaponProposed}"</p>
                        <p className="proposed-by">
                            Proposed by {userProposingWeapon.username}
                        </p>

                        <button
                            disabled={answer !== null}
                            onClick={() => {
                                submitVote('YES')
                            }}>
                            YES 
                        </button>

                        <button
                            disabled={answer !== null}
                            onClick={() => {
                                submitVote('NO')
                            }}>
                            NO 
                        </button>
                    
                    </div>
                )
                    : null

                }

            </div>

        </div>
        
    )



}