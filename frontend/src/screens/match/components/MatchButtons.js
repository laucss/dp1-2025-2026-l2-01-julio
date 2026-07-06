// MatchSidebar.js

import { FaComments } from "react-icons/fa";
import ChatBox from "../chatBox";
import "../../../static/css/match/matchButtons.css";

export default function MatchButtons({
    match,
    currentUser,
    actionPoints,
    setDiscardPhaseOpen,
    setIsActionsModalOpen,
    leaveMatch,
    endMatch,
    chatOpen,
    setChatOpen
}) {
    return (
        <div className="match-sidebar">

            <div className="buttons-section">

                <button
                    className="leave-match-button"
                    onClick={() => setDiscardPhaseOpen(true)}
                    disabled={match.currentTurnUserId !== currentUser.id}
                >
                    Discard and bag
                </button>

                <button
                    className="leave-match-button"
                    onClick={() => setIsActionsModalOpen(true)}
                    disabled={
                        match.currentTurnUserId !== currentUser.id ||
                        actionPoints === 0
                    }
                >
                    Actions
                </button>

                <button
                    className="leave-match-button"
                    onClick={leaveMatch}
                    style={{
                        background: "#e74c3c",
                        color: "white"
                    }}
                >
                    Leave Match
                </button>

                {match?.creatorId === currentUser?.id && (
                    <button
                        className="end-match-button"
                        onClick={endMatch}
                    >
                        End match
                    </button>
                )}

                <div className="match-chat-icon">
                    <div
                        className="match-chat-icon-button"
                        onClick={() => setChatOpen(!chatOpen)}
                    >
                        <FaComments size={30} color="white" />
                    </div>
                </div>

                {chatOpen && <ChatBox matchId={match.id} />}

            </div>

        </div>
    );
}