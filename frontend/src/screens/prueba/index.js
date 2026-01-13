import { useState, useEffect } from "react";
import tokenService from "../../services/token.service";
import getErrorModal from "../../util/getErrorModal";
import { Button, ButtonGroup } from "reactstrap";

const jwt = tokenService.getLocalAccessToken();

export default function MatchList() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [matches, setMatches] = useState([]);
  const [filter, setFilter] = useState("all"); // all | inProgress | finished
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
      <img
        src={user.avatar || "/Avatar_default.png"}
        alt={user.username}
        title={user.username}
        style={{
          width: size,
          height: size,
          borderRadius: "50%",
          objectFit: "cover",
        }}
      />
    );

  const optionStyle = (active) => ({
    padding: "8px 12px",
    borderRadius: "6px",
    cursor: "pointer",
    background: active ? "#3b82f6" : "transparent",
    color: active ? "#fff" : "#000",
    marginBottom: "4px",
  });

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="admin-page-container">
      <h1 className="text-center mb-4">Historial</h1>
      {modal}

      {/* LAYOUT PRINCIPAL */}
      <div
        style={{
          display: "flex",
          alignItems: "flex-start",
          justifyContent: "space-between",
          paddingLeft: "20px",
        }}
      >
        {/* COLUMNA IZQUIERDA – FILTRO */}
        <div style={{ width: "220px", marginRight: "40px" }}>
          <div style={{ position: "relative" }}>
            <Button
              color="secondary"
              style={{ width: "100%" }}
              onClick={() => setOpenFilter(!openFilter)}
            >
              Filtrar por ▾
            </Button>

            {openFilter && (
              <div
                style={{
                  position: "absolute",
                  top: "110%",
                  left: 0,
                  background: "#fff",
                  border: "2px solid #3b82f6",
                  borderRadius: "10px",
                  padding: "8px",
                  width: "100%",
                  zIndex: 10,
                }}
              >
                <div
                  style={optionStyle(filter === "all")}
                  onClick={() => {
                    setFilter("all");
                    setPage(0);
                    setOpenFilter(false);
                  }}
                >
                  Todas
                </div>

                <div
                  style={optionStyle(filter === "inProgress")}
                  onClick={() => {
                    setFilter("inProgress");
                    setPage(0);
                    setOpenFilter(false);
                  }}
                >
                  Partidas en curso
                </div>

                <div
                  style={optionStyle(filter === "finished")}
                  onClick={() => {
                    setFilter("finished");
                    setPage(0);
                    setOpenFilter(false);
                  }}
                >
                  Partidas finalizadas
                </div>
              </div>
            )}
          </div>
        </div>

        {/* COLUMNA CENTRAL – LISTADO */}
        <div style={{ flex: 1, display: "flex", justifyContent: "center" }}>
          <div
            style={{
              maxWidth: "1000px",
              width: "100%",
              display: "flex",
              flexDirection: "column",
              gap: "16px",
            }}
          >
            {matches.map(match => (
              <div
                key={match.id}
                style={{
                  border: "1px solid #ddd",
                  borderRadius: "16px",
                  padding: "16px",
                  display: "flex",
                  justifyContent: "space-between",
                  background: "#fff",
                }}
              >
                <div>
                  <strong>Nombre: {match.name}</strong>
                  <div style={{ marginTop: "8px" }}>
                    Jugadores:
                    <div
                      style={{
                        display: "flex",
                        gap: "6px",
                        marginTop: "4px",
                      }}
                    >
                      {match.players?.map(p => avatar(p.user))}
                    </div>
                  </div>
                </div>

                <div style={{ textAlign: "right" }}>
                  <div>Duración: {formatDuration(match.duration)}</div>
                  <div style={{ marginTop: "6px" }}>
                    Creador: {avatar(match.creator?.user, 28)}
                  </div>
                  <div style={{ marginTop: "6px" }}>
                    Ganador:{" "}
                    {match.winner ? avatar(match.winner.user, 28) : "—"}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* COLUMNA DERECHA FANTASMA (para centrar) */}
        <div style={{ width: "220px" }} />
      </div>

      {/* PAGINACIÓN */}
      {totalPages > 1 && (
        <div className="text-center mt-4">
          <ButtonGroup>
            <Button disabled={page === 0} onClick={() => setPage(page - 1)}>
              ◀
            </Button>
            <Button disabled>
              {page + 1} / {totalPages}
            </Button>
            <Button
              disabled={page >= totalPages - 1}
              onClick={() => setPage(page + 1)}
            >
              ▶
            </Button>
          </ButtonGroup>
        </div>
      )}
    </div>
  );
}
