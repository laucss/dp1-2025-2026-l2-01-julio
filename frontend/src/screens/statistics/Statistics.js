import React, { useState } from 'react';
import './Statistics.css';
import tokenService from "../../services/token.service";
import useFetchState from '../../util/useFetchState';
import {
    FaTrophy,
    FaGamepad,
    FaClock,
    FaBolt,
    FaHome,
    FaCrosshairs,
    FaUser,
    FaDoorOpen,
    FaCrown,
    FaUsers,
    FaHourglassHalf,
    FaMapMarkedAlt,
    FaMedal,
    FaChartLine
} from "react-icons/fa";

const jwt = tokenService.getLocalAccessToken();
const currentUser = tokenService.getUser();

export default function Statistics() {

    const userId = currentUser.id;

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    const [statisticsUser] = useFetchState(
        {},
        `/api/v1/statistics/${userId}`,
        jwt,
        setMessage,
        setVisible
    );

    const [generalStatistics] = useFetchState(
        {},
        `/api/v1/statistics/general`,
        jwt,
        setMessage,
        setVisible
    );

    const [activeTab, setActiveTab] = useState('personal');

    const data = activeTab === 'personal'
        ? statisticsUser
        : generalStatistics;

    
    console.log("GENERAL:", generalStatistics);


    return (
        <div>
            <div className="admin-page-container">

                <div className="statistics-header">
                    <button
                        className={`stats-button ${activeTab === 'personal' ? 'active' : ''}`}
                        onClick={() => setActiveTab('personal')}
                    >
                        My statistics
                    </button>

                    <button
                        className={`stats-button ${activeTab === 'general' ? 'active' : ''}`}
                        onClick={() => setActiveTab('general')}
                    >
                        General statistics
                    </button>
                </div>

                {activeTab === 'personal' ?

                    <div className="statistics-container">

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaCrown />
                            </div>
                            <h2>Victories</h2>
                            <p className="stat-value">{data.totalVictories}</p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaChartLine />
                            </div>
                            <h2>Win Rate</h2>
                            <p className="stat-value">
                                {Number(data.winRate || 0).toFixed(2)}%
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaUser />
                            </div>
                            <h2>Player Type</h2>
                            <p className="stat-value">
                                {data.playerType}
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaMedal />
                            </div>
                            <h2>Total Battles</h2>
                            <p className="stat-value">
                                {data.totalBattlesPlayed}
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaGamepad />
                            </div>
                            <h2>Matches Played</h2>
                            <p className="stat-value">{data.matchesPlayed}</p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaClock />
                            </div>
                            <h2>Total Time Played</h2>
                            <p className="stat-value">
                                {data.totalTimePlayed} min
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaHourglassHalf />
                            </div>
                            <h2>Avg Time/Match</h2>
                            <p className="stat-value">
                                {Number(data.averageTimePerMatch || 0).toFixed(2)} min
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaCrosshairs />
                            </div>
                            <h2>Battles Won/Match</h2>
                            <p className="stat-value">
                                {Number(data.battlesWonPerMatch || 0).toFixed(2)}
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaHome />
                            </div>
                            <h2>Max Rooms In Match</h2>
                            <p className="stat-value">
                                {data.maxRoomsVisitedInMatch}
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaBolt />
                            </div>
                            <h2>Action Points</h2>
                            <p className="stat-value">
                                {data.totalActionPoints}
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaBolt />
                            </div>
                            <h2>Avg Action Points/Match</h2>
                            <p className="stat-value">
                                {Number(data.averageActionPointsPerMatch || 0).toFixed(2)}
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaTrophy />
                            </div>
                            <h2>Battles Won</h2>
                            <p className="stat-value">
                                {data.battlesWon}
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaMapMarkedAlt />
                            </div>
                            <h2>Rooms Visited</h2>
                            <p className="stat-value">
                                {data.roomsVisited}
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaDoorOpen />
                            </div>
                            <h2>Avg Rooms/Match</h2>
                            <p className="stat-value">
                                {Number(data.averageRoomsVisitedPerMatch || 0).toFixed(2)}
                            </p>
                        </div>

                    </div>

                    :

                    <div className="statistics-container-avg">

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaUsers />
                            </div>
                            <h2>Average Players/Match</h2>
                            <p className="stat-value">
                                {Number(data.averagePlayersPerMatch || 0).toFixed(2)}
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaClock />
                            </div>
                            <h2>Longest Match</h2>
                            <p className="stat-value">
                                {data.longestMatchDuration} min
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaHourglassHalf />
                            </div>
                            <h2>Shortest Match</h2>
                            <p className="stat-value">
                                {data.shortestMatchDuration} min
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaGamepad />
                            </div>
                            <h2>Total Matches Played</h2>
                            <p className="stat-value">
                                {data.totalMatchesPlayed}
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaTrophy />
                            </div>
                            <h2>Total Battles Disputed</h2>
                            <p className="stat-value">
                                {data.totalBattlesDisputed}
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaDoorOpen />
                            </div>
                            <h2>Average Rooms Visited/Match</h2>
                            <p className="stat-value">
                                {Number(data.averageRoomsVisitedPerMatch || 0).toFixed(2)}
                            </p>
                        </div>

                        <div className="stat-card">
                            <div className="stat-icon">
                                <FaClock />
                            </div>
                            <h2>Average Match Duration</h2>
                            <p className="stat-value">
                                {Number(data.averageMatchDuration || 0).toFixed(2)} min
                            </p>
                        </div>

                    </div>
                }

            </div>
        </div>
    );
}