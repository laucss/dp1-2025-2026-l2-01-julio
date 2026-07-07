import {useNavigate} from "react-router-dom";

export default function EndMatchModal({match}){
    const navigate = useNavigate();
    
    return (
            <div className="match-ended">
                <div className="end-overlay">
                    <div className="end-text-box">
                        <h2>The match has ended</h2>
                        {match?.winner?.user ? (
                            <p style={{ fontWeight: 700, margin: '8px 0' }}>Winner: {match.winner.user.username}</p>
                        ) : null}
                        <p>Thanks for playing.</p>
                        <button className="return-menu-button" onClick={() => navigate(`/`)}>Return to main menu</button>
                    </div>
                </div>
            </div>
        );
}