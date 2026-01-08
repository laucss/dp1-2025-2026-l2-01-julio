import { useState, useEffect } from "react";
import { Button, Card, Table } from "reactstrap";
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
        `/api/v1/users`,
        jwt,
        setMessage,
        setVisible
    );

    

    const [currentPage, setCurrentPage] = useState(1);
    const usersPerPage = 5;
    const isEmpty = users.length === 0;
    const totalPages = isEmpty ? 0 : Math.ceil(users.length / usersPerPage);
    const LastUser = currentPage * usersPerPage;
    const FirstUser = LastUser - usersPerPage;
    const currentUsers = users.slice(FirstUser, LastUser);

    useEffect(() => {
        if (!isEmpty && currentPage > totalPages) {
            setCurrentPage(totalPages);
        }
        if (isEmpty && currentPage !== 1) {
            setCurrentPage(1);
        }
    }, [users, totalPages, isEmpty, currentPage]);

    const [statsMap, setStatsMap] = useState({});
    const [sortedUsers, setSortedUsers] = useState([]);

    // Fetch statistics for ALL users, then sort by totalVictories
    useEffect(() => {
        let cancelled = false;
        const fetchAllStatsAndSort = async () => {
            if (!users.length) {
                setSortedUsers([]);
                return;
            }
            const map = {};
            await Promise.all(
                users.map(async (user) => {
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
            if (!cancelled) {
                setStatsMap(map);
                const ordered = [...users].sort((a, b) => {
                    const va = map[a.id]?.totalVictories || 0;
                    const vb = map[b.id]?.totalVictories || 0;
                    return vb - va;
                });
                setSortedUsers(ordered);
            }
        };
        fetchAllStatsAndSort();
        return () => { cancelled = true; };
    }, [users, jwt]);

    const listSource = sortedUsers.length ? sortedUsers.slice(FirstUser, LastUser) : currentUsers;
    const userList = listSource.map((user, index) => {
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
                <h2
                    className="ranking-title"
                    style={{
                        writingMode: 'horizontal-tb',
                        transform: 'none',
                        whiteSpace: 'normal',
                        wordBreak: 'normal'
                    }}
                >
                    Ranking de Usuarios
                </h2>
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
                        disabled={isEmpty || currentPage === 1}
                        onClick={() => setCurrentPage(currentPage - 1)}
                    >
                        <FaArrowLeft /> Anterior
                    </Button>
                    <span className="pagination-info">
                        Página {isEmpty ? 0 : currentPage} de {isEmpty ? 0 : totalPages}
                    </span>
                    <Button
                        color="primary"
                        disabled={isEmpty || currentPage === totalPages}
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