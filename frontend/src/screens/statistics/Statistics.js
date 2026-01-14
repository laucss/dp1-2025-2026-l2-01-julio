import React, { useState, useEffect } from 'react';
import './Statistics.css';
import tokenService from "../../services/token.service";
import useFetchState from '../../util/useFetchState';

const jwt = tokenService.getLocalAccessToken();
const currentUser = tokenService.getUser();

export default function Statistics(){
    const userId = currentUser.id;

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    const [statisticsUser, setStatistics] = useFetchState(
        {},
        `/api/v1/statistics/${userId}`,
        jwt,
        setMessage,
        setVisible
    );

    const [generalStatistics, setGeneralStatistics] = useFetchState(
        {},
        `/api/v1/statistics/general`,
        jwt,
        setMessage,
        setVisible
    );

    const [activeTab, setActiveTab] = useState('personal');
    
    const data = activeTab === 'personal' ? statisticsUser : generalStatistics;

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
                        <div className="stat-icon">🏆</div>
                        <h2>Victories</h2>
                        <p className="stat-value">{data.totalVictories}</p>
                    </div>

                    <div className="stat-card">
                        <div className="stat-icon">🎮</div>
                        <h2>Matches Played</h2>
                        <p className="stat-value">{data.matchesPlayed}</p>
                    </div>

                    <div className="stat-card">
                        <div className="stat-icon">⏱️</div>
                        <h2>Total Time Played</h2>
                        <p className="stat-value">{data.totalTimePlayed} min</p>
                    </div>

                    <div className="stat-card">
                        <div className="stat-icon">⚡</div>
                        <h2>Action Points</h2>
                        <p className="stat-value">{data.totalActionPoints}</p>
                    </div>
                </div>
                :
                <div className="statistics-container">
                    <div className="stat-card">
                        <div className="stat-icon">👥</div>
                        <h2>Average Players/Match</h2>
                        <p className="stat-value">{parseFloat(data.averagePlayersPerMatch).toFixed(2)}</p>
                    </div>
                    <div className="stat-card">
                        <div className="stat-icon">🎮</div>
                        <h2>Total Matches Played</h2>
                        <p className="stat-value">{data.totalMatchesPlayed}</p>
                    </div>
                </div>
                }
            </div>
        </div>
    );
}