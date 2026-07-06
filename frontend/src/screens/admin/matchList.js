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
              `/api/v1/matches/all-Matches?filter=${filter}&page=${currentPage}&size=3`,
              {
                  headers: {
                      Authorization: `Bearer ${jwt}`,
                  },
              }
          );

          if (!response.ok)
              throw new Error("Error al cargar las partidas");

          const data = await response.json();

          setMatches(data.content);
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

      // Duración en formato ISO-8601 (PT1H2M3.456S)
      if (typeof duration === "string") {
        const match = duration.match(
          /^PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+(?:\.\d+)?)S)?$/
        );

        if (!match) return duration;

        const hours = Number(match[1] || 0);
        const minutes = Number(match[2] || 0);
        const seconds = Math.floor(Number(match[3] || 0));

        const parts = [];

        if (hours > 0) parts.push(`${hours}h`);
        if (minutes > 0) parts.push(`${minutes}m`);

        // Mostrar segundos si existen o si la duración es menor de un minuto
        if (seconds > 0 || (hours === 0 && minutes === 0)) {
          parts.push(`${seconds}s`);
        }

        return parts.join(" ");
      }

      // Si algún día llega como número
      if (typeof duration === "number") {
        const totalSeconds =
          duration > 1000 ? Math.floor(duration / 1000) : Math.floor(duration);

        const hours = Math.floor(totalSeconds / 3600);
        const minutes = Math.floor((totalSeconds % 3600) / 60);
        const seconds = totalSeconds % 60;

        const parts = [];

        if (hours > 0) parts.push(`${hours}h`);
        if (minutes > 0) parts.push(`${minutes}m`);
        if (seconds > 0 || (hours === 0 && minutes === 0)) {
          parts.push(`${seconds}s`);
        }

        return parts.join(" ");
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
    cursor: "pointer",
    background: active ? AQUA : "white",
    color: active ? "#003b3a" : "#000",
    borderBottom: "1px solid #ddd",
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
        Matches history
      </h1>

      {modal}

      {/* FILTRO A LA IZQUIERDA */}
      <div
        style={{
          position: "absolute",
          top: 60,
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
          Filter by ▾
        </Button>

        {openFilter && (
          <div style={{ marginTop: "8px" }}>
            <div style={optionStyle(filter === "all")} onClick={() => { setFilter("all"); setPage(0); setOpenFilter(false); }}>
              All matches
            </div>
            <div style={optionStyle(filter === "inProgress")} onClick={() => { setFilter("inProgress"); setPage(0); setOpenFilter(false); }}>
              In progress matches
            </div>
            <div style={optionStyle(filter === "finished")} onClick={() => { setFilter("finished"); setPage(0); setOpenFilter(false); }}>
              Finished matches
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
       {matches.length === 0 ? (
          <div
            style={{
              borderRadius: "22px",
              padding: "48px 32px",
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              background: "#ebfdff",
              border: `3px solid ${AQUA}`,
              boxShadow: `0 0 20px ${AQUA_GLOW}`,
              minHeight: "180px",
            }}
          >
            <span
              style={{
                fontSize: "1.5rem",
                fontWeight: "bold",
                color: "#555",
              }}
            >
              No matches to display
            </span>
          </div>
        ) : (
          matches.map(match => (
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
                  Name: {match.name}
                </strong>

                <div style={{ marginTop: "16px" }}>
                  Players:
                  <div style={{ display: "flex", gap: "10px", marginTop: "8px" }}>
                    {match.players?.map(p => avatar(p.user))}
                  </div>
                </div>
              </div>

              <div style={{ textAlign: "right" }}>
                <div>Duration: {formatDuration(match.duration)}</div>

                <div
                  style={{
                    marginTop: "12px",
                    display: "flex",
                    alignItems: "center",
                    gap: 8,
                  }}
                >
                  <strong style={{ marginRight: 4 }}>Creator:</strong>
                  {match.creator?.user ? (
                    <>
                      {avatar(match.creator.user, 34)}
                      <span style={{ marginLeft: 8 }}>
                        {match.creator.user.username}
                      </span>
                    </>
                  ) : (
                    "—"
                  )}
                </div>

                <div
                  style={{
                    marginTop: "12px",
                    display: "flex",
                    alignItems: "center",
                    gap: 8,
                  }}
                >
                  <strong style={{ marginRight: 4 }}>Winner:</strong>
                  {match.winner?.user ? (
                    <>
                      {avatar(match.winner.user, 34)}
                      <span style={{ marginLeft: 8 }}>
                        {match.winner.user.username}
                      </span>
                    </>
                  ) : (
                    "—"
                  )}
                </div>
              </div>
            </div>
          ))
        )}
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
