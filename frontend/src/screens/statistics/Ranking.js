import { useState, useEffect } from "react";
import { Button, Card } from "reactstrap";
import tokenService from "../../services/token.service";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";
// Using simple symbols for pagination arrows (styled in CSS)
import { FaTrophy } from "react-icons/fa";
import "./Ranking.css";

const jwt = tokenService.getLocalAccessToken();

export default function Ranking() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [loading, setLoading] = useState(true);

  const [users, setUsers] = useFetchState(
    [],
    `/api/v1/users`,
    jwt,
    setMessage,
    setVisible
  );


  const [statsMap, setStatsMap] = useState({});
  const [sortedUsers, setSortedUsers] = useState([]);

  const [currentPage, setCurrentPage] = useState(1);
  const usersPerPage = 5;

  /* ===============================
     Fetch stats & sort users
  =============================== */
  useEffect(() => {
    let cancelled = false;

    const fetchStats = async () => {
      if (!users.length) {
        setLoading(false);
        return;
      }

      setLoading(true);

      const map = {};

      await Promise.all(
        users.map(async (user) => {
          try {
            const res = await fetch(`/api/v1/statistics/${user.id}`, {
              headers: { Authorization: `Bearer ${jwt}` },
            });
            map[user.id] = res.ok ? await res.json() : {};
          } catch {
            map[user.id] = {};
          }
        })
      );

      if (!cancelled) {
        setStatsMap(map);
        setSortedUsers(
          [...users].sort(
            (a, b) =>
              (map[b.id]?.totalVictories || 0) -
              (map[a.id]?.totalVictories || 0)
          )
        );
        setLoading(false);
      }
    };

    fetchStats();

    return () => {
      cancelled = true;
    };
  }, [users]);

  /* ===============================
     Podium & list
  =============================== */
  const podium = sortedUsers.slice(0, 3);
  const restUsers = sortedUsers.slice(3);

  const totalPages =
    restUsers.length === 0
      ? 0
      : Math.ceil(restUsers.length / usersPerPage);

  const lastUser = currentPage * usersPerPage;
  const firstUser = lastUser - usersPerPage;
  const currentUsers = restUsers.slice(firstUser, lastUser);

  const modal = getErrorModal(setVisible, visible, message);



  return (
    <div className="ranking-page-container">
      <Card className="ranking-card">
        <h2 className="ranking-title">Ranking</h2>

        {loading ? (
          <div className="ranking-loading">Loading...</div>
        ) : (
          <>

        {/* ===============================
            PODIUM
        =============================== */}
        <div className="podium">
          {podium[1] && (
            <div className="podium-item second">
              <FaTrophy className="trophy-icon" />
              <span className="podium-rank">2</span>
              <img
                src={podium[1].avatar || "/Avatar_default.png"}
                className="podium-avatar"
                alt="avatar"
              />
              <span className="podium-name">{podium[1].username}</span>
              <span className="podium-score">
                {statsMap[podium[1].id]?.totalVictories || 0}
              </span>
            </div>
          )}

          {podium[0] && (
            <div className="podium-item first">
              <FaTrophy className="trophy-icon" />
              <span className="podium-rank">1</span>
              <img
                src={podium[0].avatar || "/Avatar_default.png"}
                className="podium-avatar"
                alt="avatar"
              />
              <span className="podium-name">{podium[0].username}</span>
              <span className="podium-score">
                {statsMap[podium[0].id]?.totalVictories || 0}
              </span>
            </div>
          )}

          {podium[2] && (
            <div className="podium-item third">
              <FaTrophy className="trophy-icon" />
              <span className="podium-rank">3</span>
              <img
                src={podium[2].avatar || "/Avatar_default.png"}
                className="podium-avatar"
                alt="avatar"
              />
              <span className="podium-name">{podium[2].username}</span>
              <span className="podium-score">
                {statsMap[podium[2].id]?.totalVictories || 0}
              </span>
            </div>
          )}
        </div>

        {/* ===============================
            LIST FROM 4th
        =============================== */}
        <ul className="ranking-list">
          {currentUsers.map((user, index) => (
            <li key={user.id} className="ranking-item">
              <span className="ranking-position">
                {firstUser + index + 4}
              </span>

              <img
                src={user.avatar || "/Avatar_default.png"}
                alt="avatar"
                className="user-avatar"
              />

              <div className="user-info">
                <span className="username">{user.username}</span>
              </div>

              <span className="victories-badge">
                {statsMap[user.id]?.totalVictories || 0}
              </span>
            </li>
          ))}
        </ul>

        {/* ===============================
            PAGINATION
        =============================== */}
        <div className="pagination-controls">
          <Button
            className="pagination-arrow"
            aria-label="Previous page"
            disabled={currentPage === 1}
            onClick={() => setCurrentPage(currentPage - 1)}
          >
            ◀
          </Button>

          <span className="pagination-info">
            Page {currentPage} of {totalPages}
          </span>

          <Button
            className="pagination-arrow"
            aria-label="Next page"
            disabled={currentPage === totalPages}
            onClick={() => setCurrentPage(currentPage + 1)}
          >
            ▶
          </Button>
        </div>
          </>
        )}
      </Card>

      {modal}
    </div>
  );
}
