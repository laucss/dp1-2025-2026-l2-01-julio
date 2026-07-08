import {useNavigate} from "react-router-dom";
import "../../../static/css/match/modals/endMatchModal.css"

export default function EndMatchModal({match}){
    const navigate = useNavigate();
    
    return (
        <div className="match-ended">
            <div className="end-overlay">
                <div className="end-text-box">

                    {match?.winner?.user ? (
                        <>
                            <div className="winner-trophy">🏆</div>

                            <h2>Match Finished</h2>

                            <p className="winner-label">
                                Winner
                            </p>

                            <h1 className="winner-name">
                                {match.winner.user.username}
                            </h1>

                            <p className="end-message">
                                Congratulations on your victory!
                            </p>
                        </>
                    ) : (
                        <>
                            <h2>Match Finished</h2>

                            <p className="end-message">
                                The match ended without a winner.
                            </p>
                        </>
                    )}

                    <button
                        className="end-return-menu-button"
                        onClick={() => navigate(`/`)}
                    >
                        Return to Main Menu
                    </button>

                </div>
            </div>
        </div>
    );
}