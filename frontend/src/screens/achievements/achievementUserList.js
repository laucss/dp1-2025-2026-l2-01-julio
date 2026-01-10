import React, { useState } from 'react';
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import './AchievementUserList.css';

const jwt = tokenService.getLocalAccessToken();
const currentUser = tokenService.getUser();

export default function AchievementUserList() {
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const userId = currentUser.id;

    const [achievements, setAchievements] = useFetchState(
        [],
        `/api/v1/achievements`,
        jwt,
        setMessage,
        setVisible
    );

    console.log('imagen', achievements)
    const [statisticsUser, setStatistics] = useFetchState(
        {},
        `/api/v1/statistics/${userId}`,
        jwt,
        setMessage,
        setVisible
    );

    const getMetricValue = (metric) => {
        switch (metric) {
            case 'GAMES_PLAYED':
                return statisticsUser.matchesPlayed || 0;
            case 'VICTORIES':
                return statisticsUser.totalVictories || 0;
            case 'TOTAL_PLAY_TIME':
                return statisticsUser.totalTimePlayed || 0;
            case 'ACTION_POINTS_EARNED':
                return statisticsUser.totalActionPoints || 0;
            default:
                return 0;
        }
    };

    const formatValue = (value, metric) => {
        if (metric === 'TOTAL_PLAY_TIME') {
            return `${value} min`;
        }
        return value;
    };

    const sortedAchievements = [...achievements].sort((a, b) =>
        a.metric.localeCompare(b.metric)
    );
    
    const achievementCards = sortedAchievements.map((a) => { 
        const value = getMetricValue(a.metric);
        const completed = value >= a.threshold;
        const cardClass = completed ? 'achievement-card completed' : 'achievement-card pending';
        
        return (
            <div key={a.id} className={cardClass}>
                <div className="achievement-badge">
                    <img src={a.badgeImage} alt={a.name} />
                </div>
                <h3 className="achievement-title">{a.description}</h3>
                <p className="achievement-value">{formatValue(value, a.metric)}</p>
                <p className="achievement-threshold">Meta: {a.threshold}</p>
            </div>
        );
    }); 

    return ( 
        <div className="achievement-user-container"> 
            <div className="achievement-header">
                <h1>Logros</h1>
                <p className="subtitle">Demuestra tu dominio en el juego</p>
            </div>
            <div className="achievement-grid"> 
                {achievementCards}
            </div> 
        </div> 
    ); 
}