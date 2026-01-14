import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import tokenService from "../../services/token.service";
import getErrorModal from "../../util/getErrorModal";
import { Button, ButtonGroup } from "reactstrap";

const jwt = tokenService.getLocalAccessToken();

const AQUA = "#22a8b7";
const AQUA_GLOW = "rgba(9, 141, 123, 0.6)";
const COLOR = "#ebfdff";

export default function History({ userId: propUserId, userName: propUserName }) {
  const params = useParams();
  const userId = propUserId ?? params.userId;
  const userName = propUserName ?? params.userName ?? propUserName;
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [matches, setMatches] = useState([]);
  const [filter, setFilter] = useState("played"); // played | created | won
  const [openFilter, setOpenFilter] = useState(false);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const fetchMatches = async (currentPage = 0) => {
    if (!userId) return;
    try {
      let url;
      if (filter === "played") url = `/api/v1/matches/all-Matches/${userId}?page=${currentPage}&size=3`;
      else if (filter === "created") url = `/api/v1/matches/matches-created/${userId}?page=${currentPage}&size=3`;
      else url = `/api/v1/matches/matches-won/${userId}?page=${currentPage}&size=3`;

      const res = await fetch(url, { headers: { Authorization: `Bearer ${jwt}` } });
      if (!res.ok) throw new Error("Error al cargar el historial");
      const data = await res.json();

      // Expecting a Page response with .content and .totalPages
      const content = data.content || data;
      setMatches(content);
      setTotalPages(data.totalPages ?? 0);
    } catch (err) {
      setMessage(err.message);
      setVisible(true);
    }
  };

  useEffect(() => {
    fetchMatches(page);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filter, page, userId]);

  const formatDuration = (duration) => {
    if (!duration) return "En curso";
    const match = typeof duration === "string" && duration.match(/PT(?:(\d+)H)?(?:(\d+)M)?/);
    if (match) return `${match[1] ? match[1] + "h " : ""}${match[2] ? match[2] + "m" : ""}`.trim();
    if (typeof duration === "number") {
      const totalSeconds = duration > 1000 ? Math.floor(duration / 1000) : Math.floor(duration);
      const totalMinutes = Math.floor(totalSeconds / 60);
      const hours = Math.floor(totalMinutes / 60);
      const minutes = totalMinutes % 60;
      return `${hours ? hours + "h " : ""}${minutes ? minutes + "m" : ""}`.trim() || `${totalSeconds}s`;
    }
    return String(duration);
  };

  const avatar = (user, size = 36) =>
    user && (
      <span title={user.username} style={{ display: "inline-block", lineHeight: 0 }}>
        <img
          src={user.avatar || "/Avatar_default.png"}
          alt={user.username}
          style={{
            width: size,
            height: size,
            borderRadius: "50%",
            objectFit: "cover",
            border: `3px solid ${AQUA}`,
            boxShadow: `0 0 8px ${AQUA_GLOW}`,
            cursor: "default",
          }}
        />
      </span>
    );

  const optionStyle = (active) => ({
    padding: "8px 12px",
    borderRadius: "6px",
    cursor: "pointer",
    background: active ? AQUA : "white",
    color: active ? "#003b3a" : "#000",
    marginBottom: "4px",
  });

  const modal = getErrorModal(setVisible, visible, message);

  if (!userId) return (
    <div className="admin-page-container">
      <h2 style={{ textAlign: "center" }}>Historial</h2>
      <p style={{ textAlign: "center" }}>No se ha proporcionado `userId`.</p>
    </div>
  );

  return (
    <div className="admin-page-container">
      <h1 style={{ textAlign: "center", margin: "24px 0 24px", color: COLOR, fontSize: 40 }}>{userName ? `Historial de ${userName}` : "Historial"}</h1>

      {modal}

      {/* filtro izquierdo */}
      <div style={{ position: "absolute", top: 140, left: 24, width: 220, zIndex: 100 }}>
        <Button style={{ width: "100%", backgroundColor: AQUA, border: "none", color: "#fff", fontWeight: "bold" }} onClick={() => setOpenFilter(!openFilter)}>
          Filtrar por ▾
        </Button>

        {openFilter && (
          <div style={{ marginTop: 8 }}>
            <div style={optionStyle(filter === "played")} onClick={() => { setFilter("played"); setPage(0); setOpenFilter(false); }}>Partidas jugadas</div>
            <div style={optionStyle(filter === "created")} onClick={() => { setFilter("created"); setPage(0); setOpenFilter(false); }}>Partidas creadas</div>
            <div style={optionStyle(filter === "won")} onClick={() => { setFilter("won"); setPage(0); setOpenFilter(false); }}>Partidas ganadas</div>
          </div>
        )}
      </div>

      {/* listado similar a prueba/index.js */}
      <div style={{ position: "relative", left: "50%", transform: "translateX(-70%)", maxWidth: "1040px", width: "95%", display: "flex", flexDirection: "column", gap: "28px" }}>
        {matches.map((match) => (
          <div key={match.id} style={{ borderRadius: "22px", padding: "32px", display: "flex", justifyContent: "space-between", background: "#ebfdff", border: `3px solid ${AQUA}`, boxShadow: `0 0 20px ${AQUA_GLOW}` }}>
            <div>
              <strong style={{ fontSize: "1.2rem" }}>Nombre: {match.name}</strong>
              <div style={{ marginTop: "16px" }}>
                Jugadores:
                <div style={{ display: "flex", gap: "10px", marginTop: "8px" }}>
                  {match.players?.map((p) => (
                    <div key={p.id} style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                      {avatar(p.user)}
                    </div>
                  ))}
                </div>
              </div>
            </div>

            <div style={{ textAlign: "right" }}>
              <div>Duración: {formatDuration(match.duration)}</div>
              <div style={{ marginTop: "12px", display: "flex", alignItems: "center", gap: 8 }}>
                <strong style={{ marginRight: 4 }}>Creador:</strong>
                {match.creator?.user ? (
                  <>
                    {avatar(match.creator.user, 34)}
                    <span style={{ marginLeft: 8 }}>{match.creator.user.username}</span>
                  </>
                ) : (
                  "—"
                )}
              </div>
              <div style={{ marginTop: "12px", display: "flex", alignItems: "center", gap: 8 }}>
                <strong style={{ marginRight: 4 }}>Ganador:</strong>
                {match.winner?.user ? (
                  <>
                    {avatar(match.winner.user, 34)}
                    <span style={{ marginLeft: 8 }}>{match.winner.user.username}</span>
                  </>
                ) : (
                  "—"
                )}
              </div>
            </div>
          </div>
        ))}
      </div>

      {totalPages > 1 && (
        <div className="text-center mt-5">
          <ButtonGroup>
            <Button style={{ backgroundColor: AQUA, border: "none", color: "#003b3a" }} disabled={page === 0} onClick={() => setPage(page - 1)}>◀</Button>
            <Button style={{ backgroundColor: AQUA, border: "none", color: "#003b3a" }} disabled>{page + 1} / {totalPages}</Button>
            <Button style={{ backgroundColor: AQUA, border: "none", color: "#003b3a" }} disabled={page >= totalPages - 1} onClick={() => setPage(page + 1)}>▶</Button>
          </ButtonGroup>
        </div>
      )}
    </div>
  );
}
