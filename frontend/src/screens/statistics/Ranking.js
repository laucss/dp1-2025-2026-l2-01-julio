import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { Button, ButtonGroup, Card, Table } from "reactstrap";
import tokenService from "../../services/token.service";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";
import { FaArrowLeft, FaArrowRight } from "react-icons/fa";
import './Ranking.css';

const jwt = tokenService.getLocalAccessToken();

export default function Ranking() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [users, setUsers] = useFetchState(
        [],
        `/api/v1/users/ranking`,
        jwt,
        setMessage,
        setVisible
    );

    

    const [currentPage, setCurrentPage] = useState(1);
    const usersPerPage = 5;
    const totalPages = Math.ceil(users.length / usersPerPage);
    const LastUser = currentPage * usersPerPage;
    const FirstUser = LastUser - usersPerPage;
    const currentUsers = users.slice(FirstUser, LastUser);

    const [statsMap, setStatsMap] = useState({});

    useEffect(() => {
        let cancelled = false;
        const fetchStats = async () => {
            const map = {};
            await Promise.all(
                currentUsers.map(async (user) => {
                    try {
                        const res = await fetch(`/api/v1/statistics/${user.id}`, {
                            headers: { Authorization: `Bearer ${jwt}` },
                        });
                        if (res.ok) {
                            const data = await res.json();
                            map[user.id] = data || {};
                        } else {
                            map[user.id] = {};
                        }
                    } catch (e) {
                        map[user.id] = {};
                    }
                })
            );
            if (!cancelled) setStatsMap((prev) => ({ ...prev, ...map }));
        };

        if (currentUsers.length > 0) fetchStats();
        return () => {
            cancelled = true;
        };
    }, [currentUsers, jwt]);

    const userList = currentUsers.map((user, index) => {
        const stats = statsMap[user.id] || {};
        return (
            <tr key={user.id}>
                <td>{FirstUser + index + 1}</td>
                <td>
                    <img src={user.avatar ? user.avatar : '/Avatar_default.png'} alt="Avatar" className="user-avatar" />
                </td>
                <td>{user.username}</td>
                <td>{stats.totalVictories || 0}</td>
            </tr>
        );
    });

    const modal = getErrorModal(setVisible, visible, message);

    return (
        <div className="ranking-page-container">
            <Card className="ranking-card">
                <h2 className="ranking-title">Ranking de Usuarios</h2>
                <Table striped className="ranking-table">
                    <thead>
                        <tr>
                            <th>Posición</th>
                            <th>Avatar</th>
                            <th>Usuario</th>
                            <th>Victorias</th>
                        </tr>
                    </thead>
                    <tbody>{userList}</tbody>
                </Table>
                <div className="pagination-controls">
                    <Button
                        color="primary"
                        disabled={currentPage === 1}
                        onClick={() => setCurrentPage(currentPage - 1)}
                    >
                        <FaArrowLeft /> Anterior
                    </Button>
                    <span className="pagination-info">
                        Página {currentPage} de {totalPages}
                    </span>
                    <Button
                        color="primary"
                        disabled={currentPage === totalPages}
                        onClick={() => setCurrentPage(currentPage + 1)}
                    >
                        Siguiente <FaArrowRight />
                    </Button>
                </div>
            </Card>
            {modal}
        </div>
    ); 



}