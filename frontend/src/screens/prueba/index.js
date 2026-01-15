import { useState, useEffect } from "react";
import tokenService from "../../services/token.service";
import getErrorModal from "../../util/getErrorModal";
import { Button, ButtonGroup } from "reactstrap";

const jwt = tokenService.getLocalAccessToken();

const AQUA = "#22a8b7";
const AQUA_GLOW = "rgba(9, 141, 123, 0.6)";
const COLOR ="#ebfdff";

export default function MatchList() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [matches, setMatches] = useState([]);
  const [filter, setFilter] = useState("all");
  const [openFilter, setOpenFilter] = useState(false);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const fetchMatches = async (currentPage = 0) => {
    try {
      const response = await fetch(
        `/api/v1/matches/all-Matches?page=${currentPage}&size=3`,
        { headers: { Authorization: `Bearer ${jwt}` } }
      );

      if (!response.ok) throw new Error("Error al cargar las partidas");

      const data = await response.json();

      const filtered =
        filter === "all"
          ? data.content
          : filter === "inProgress"
          ? data.content.filter(m => !m.duration)
          : data.content.filter(m => m.duration);

      setMatches(filtered);
      setTotalPages(data.totalPages);
    } catch (error) {
      setMessage(error.message);
      setVisible(true);
    }
  };

  useEffect(() => {
    fetchMatches(page);
  }, [filter, page]);

  const formatDuration = (duration) => {
    if (!duration) return "En curso";
    const match = duration.match(/PT(?:(\d+)H)?(?:(\d+)M)?/);
    if (!match) return duration;
    return `${match[1] ? match[1] + "h " : ""}${match[2] ? match[2] + "m" : ""}`;
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

  return (
    <div className="admin-page-container">
      {/* TÍTULO */}
      <h1
        style={{
          textAlign: "center",
          margin: "24px 0 40px",
          color: COLOR,
          fontSize: "55px",
          fontWeight: 700,
          textShadow: ` 0 0 18px ${AQUA_GLOW}`,
        }}
      >
        Historial
      </h1>

      {modal}

      {/* FILTRO A LA IZQUIERDA */}
      <div
        style={{
          position: "absolute",
          top: "160px",
          left: "24px",
          width: "220px",
          zIndex: 100,
        }}
      >
        <Button
          style={{
            width: "100%",
            backgroundColor: AQUA,
            border: "none",
            color: "#ffffff",
            fontWeight: "bold",
          }}
          onClick={() => setOpenFilter(!openFilter)}
        >
          Filtrar por ▾
        </Button>

        {openFilter && (
          <div style={{ marginTop: "8px" }}>
            <div style={optionStyle(filter === "all")} onClick={() => { setFilter("all"); setPage(0); setOpenFilter(false); }}>
              Todas
            </div>
            <div style={optionStyle(filter === "inProgress")} onClick={() => { setFilter("inProgress"); setPage(0); setOpenFilter(false); }}>
              Partidas en curso
            </div>
            <div style={optionStyle(filter === "finished")} onClick={() => { setFilter("finished"); setPage(0); setOpenFilter(false); }}>
              Partidas finalizadas
            </div>
          </div>
        )}
      </div>

      {/* LISTADO */}
      <div
        style={{
          position: "relative",
          left: "50%",
          transform: "translateX(-70%)",
          maxWidth: "1040px",
          width: "95%",
          display: "flex",
          flexDirection: "column",
          gap: "28px",
        }}
      >
        {matches.map(match => (
          <div
            key={match.id}
            style={{
              borderRadius: "22px",
              padding: "32px",
              display: "flex",
              justifyContent: "space-between",
              background: "#ebfdff",
              border: `3px solid ${AQUA}`,
              boxShadow: `0 0 20px ${AQUA_GLOW}`,
            }}
          >
            <div>
              <strong style={{ fontSize: "1.2rem" }}>
                Nombre: {match.name}
              </strong>

              <div style={{ marginTop: "16px" }}>
                Jugadores:
                <div style={{ display: "flex", gap: "10px", marginTop: "8px" }}>
                  {match.players?.map(p => avatar(p.user))}
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

      {/* PAGINACIÓN */}
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
