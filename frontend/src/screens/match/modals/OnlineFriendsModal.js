import React, { useState, useEffect } from "react";
import tokenService from "../../../services/token.service";
import "../../../static/css/home/waitingRoom.css";

export default function OnlineFriendsModal({ onClose, lobby }) {
  // el lobby es el match vaya 
  const jwt = tokenService.getLocalAccessToken();
  const currentUser = tokenService.getUser();
  const matchId = window.location.pathname.split("/").pop();

  const players = lobby?.players ?? [];

  const [friends, setFriends] = useState([]);
  const [inviteStatus, setInviteStatus] = useState({});

  const fetchFriends = async () => {
    try {
      const userId = currentUser?.id;

      const res = await fetch(`/api/v1/friendRequests/${userId}/${lobby.id}`, {
        headers: { Authorization: `Bearer ${jwt}` },
      });

      const result = await res.json();

      let friendsArray = [];

      if (Array.isArray(result)) friendsArray = result;
      else if (Array.isArray(result.data)) friendsArray = result.data;
      else if (Array.isArray(result.data?.content)) friendsArray = result.data.content;
      else if (Array.isArray(result.data?.data)) friendsArray = result.data.data;

      const mapped = friendsArray.map(f => ({
        id: f.friend.id,
        displayName: f.friend.username,
        avatar: f.friend.avatar,
        isFriendOfAllPlayers: f.friendOfAllPlayers,
        isInLobby: f.inLobby
      }));

      setFriends(mapped);
    } catch (err) {
      console.error("Error fetching friends:", err);
      setFriends([]);
    }
  };

  useEffect(() => {
    fetchFriends();
  }, []);


  const isFriendInLobby = (friend) => {
    return players.some(p => p.user?.id === friend.id);
  };

  const handleInvite = async (friend, spectator) => {
    setInviteStatus(s => ({ ...s, [friend.id]: "loading" }));
    console.log('friendid',friend.id,)
    try {
      const res = await fetch("/api/v1/invitations/invite", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        },
        
        body: JSON.stringify({
          senderId: currentUser.id,
          receiverId: friend.id,
          matchId,
          spectator
        }),
      });
      

      if (res.ok) {
        setInviteStatus(s => ({ ...s, [friend.id]: "success" }));
      } else {
        setInviteStatus(s => ({ ...s, [friend.id]: "error" }));
      }
    } catch {
      setInviteStatus(s => ({ ...s, [friend.id]: "error" }));
    }
  }

  return (
    <div className="modal-overlay">
      <div className="modal-card enlarged-modal">
        <h2>Online Friends</h2>

        <div className="friends-list-scroll">
          {friends?.length === 0 ? (
            <p className="no-friends">No online friends</p>
          ) : (
            friends.map(f => (
              <div key={f.id} className="friend-mini-container">
                <div className="friend-avatar-name">
                  {f.avatar ? (
                    <img src={f.avatar} alt="avatar" className="friend-avatar" />
                  ) : (
                    <div className="friend-avatar-placeholder" />
                  )}
                  <span className="friend-name">{f.displayName}</span>
                </div>

                <div className="friend-actions">
                  {f.isInLobby ? (
                    <span className="waiting-invite-text-small">
                      In a lobby
                    </span>
                  ) : !lobby.isPrivate ? (
                    <>
                      <button
                        className="invite-btn"
                        onClick={() => handleInvite(f, false)}
                        disabled={inviteStatus[f.id] === "loading"}
                      >
                        {inviteStatus[f.id] === "loading"
                          ? "Sending..."
                          : "Player"}
                      </button>

                      <button
                        className="invite-btn"
                        onClick={() => handleInvite(f, true)}
                        disabled={inviteStatus[f.id] === "loading"}
                      >
                        {inviteStatus[f.id] === "loading"
                          ? "Sending..."
                          : "Spectator"}
                      </button>
                    </>
                  ) : (
                    <>
                      {isFriendInLobby(f) ? (
                        <span className="waiting-invite-text-small">
                          Joined
                        </span>
                      ) : inviteStatus[f.id] === "success" ? (
                        <span className="waiting-invite-text-small">
                          Waiting...
                        </span>
                      ) : (
                        <>
                          <button
                            className="invite-btn"
                            onClick={() => handleInvite(f, false)}
                            disabled={inviteStatus[f.id] === "loading"}
                          >
                            {inviteStatus[f.id] === "loading"
                              ? "Sending..."
                              : "Invite"}
                          </button>

                          {f.isFriendOfAllPlayers && (
                            <button
                              className="invite-btn"
                              onClick={() => handleInvite(f, true)}
                              disabled={inviteStatus[f.id] === "loading"}
                            >
                              {inviteStatus[f.id] === "loading"
                                ? "Sending..."
                                : "Spectator"}
                            </button>
                          )}
                        </>
                      )}
                    </>
                  )}
                </div>

                {inviteStatus[f.id] === "error" && (
                  <span className="invite-error">Error inviting</span>
                )}
              </div>
            ))
          )}
        </div>

        <button className="friends-close-btn" onClick={onClose}>
          Close
        </button>
      </div>
    </div>
  );
}