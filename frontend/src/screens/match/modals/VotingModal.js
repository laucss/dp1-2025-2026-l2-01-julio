import { useState, useEffect } from 'react';
import tokenService from '../../../services/token.service';
import getIdFromUrl from '../../../util/getIdFromUrl';
import '../../../static/css/match/modals/votingModal.css';

import { toast } from "react-toastify";

export default function VotingModal({
    isOpen,
    onClose,
    userProposingWeapon,
    weaponProposed,
    matchData,
    onSubmit,
    proposingUserId,
    stompClient
}) {

    const [isSubmitting, setIsSubmitting] = useState(false);
    const jwt = tokenService.getLocalAccessToken();
    const currentUser = tokenService.getUser();
    const currentPlayer = matchData?.players?.find(
        p => p.user?.id === currentUser.id
    );
    const matchId = getIdFromUrl(2);

    const [answer, setAnswer] = useState(false);

    useEffect(() => {
    if (isOpen) {
        setAnswer(false);
        setIsSubmitting(false);
        }
    }, [isOpen, weaponProposed]);

    const submitVote = async (voteValue) => {
        if (isSubmitting || answer !== false) return;

        setIsSubmitting(true);

        try {
            const response = await fetch(`/api/v1/voting/vote/${matchId}`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    playerId: currentPlayer.id,
                    inFavor: voteValue,
                }),
            });

            if (response.ok) {
                setAnswer(voteValue);
            } else {
                const error = await response.json();
                throw error;
            }
        } catch (error) {
            console.error(error);
            toast.error(error.message);
            setIsSubmitting(false); // Solo se vuelve a activar si ha fallado
        }
    };

    useEffect(() => {
        if (!stompClient || !stompClient.active || !isOpen) return;

        const subscription = stompClient.subscribe(
            `/topic/match.${matchId}.weapon.voting.result`,
            (msg) => {
                const votingResult = JSON.parse(msg.body);

                if (votingResult.status === "FINISHED") {
                    onSubmit(votingResult);
                }
            }
        );

        return () => subscription.unsubscribe();
    }, [stompClient, matchId, isOpen, onSubmit]);

    if (!isOpen) return null;

    return (
        <div className="modal-voting-overlay">

            <div className="weapon-voting-modal">

                {currentUser.id !== proposingUserId ? (
                    <>
                        <h2> Weapon Proposed</h2>

                        <div className="weapon-word">
                            {weaponProposed}
                        </div>

                        <p className="weapon-proposer">
                            Proposed by <strong>{userProposingWeapon?.username}</strong>
                        </p>

                        <div className="weapon-buttons">
                            <button
                               disabled={answer !== false || isSubmitting}
                                onClick={() => submitVote("YES")}
                            >
                                ✔ Accept
                            </button>

                            <button
                               disabled={answer !== false || isSubmitting}
                                onClick={() => submitVote("NO")}
                            >
                                ✖ Reject
                            </button>
                        </div>
                    </>
                ) : (
                    <>
                        <h2>⏳ Waiting for votes...</h2>

                        <div className="weapon-word">
                            {weaponProposed}
                        </div>

                        <p className="weapon-proposer">
                            Waiting for the other players to finish voting.
                        </p>
                    </>
                )}

            </div>

        </div>
    );
}