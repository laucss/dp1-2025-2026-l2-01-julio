import { Button } from "reactstrap";
import { Link } from "react-router-dom";
import deleteFromList from "../../util/deleteFromList";
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

    const [alerts, setAlerts] = useState([]); 
    const [achievements, setAchievements] = useFetchState(
        [],
        `/api/v1/achievements`,
        jwt,
        setMessage,
        setVisible
    );

    const [statisticsUser, setStatistics] = useFetchState(
        {},
        `/api/v1/statistics/${userId}`,
        jwt,
        setMessage,
        setVisible
    );


    const sortedAchievements = [...achievements].sort((a, b) =>
        a.metric.localeCompare(b.metric)
    );
    
    const achievementCards = sortedAchievements.map((a) => { 
        
        return (
            <div key={a.id} className='achievement-card completed'>
                <div className="achievement-badge">
                    <img src={a.badgeImage} alt={a.name} />
                </div>
                <h3 className="achievement-title">{a.description}</h3>
                <div className="achievement-actions">
                    <Link to={`/achievements/`+a.id}>
                        <Button color="warning" size="sm" style={{ width: "100%" }}>
                            Edit
                        </Button>
                    </Link>
                    <Button color="danger" size="sm" style={{ width: "100%" }} onClick={() => deleteFromList(
                        `/api/v1/achievements/${a.id}`,
                        a.id,
                        [achievements, setAchievements],
                        [alerts, setAlerts],
                        setMessage,
                        setVisible
                        )}>
                        Delete
                    </Button>
                </div>
            </div>
        );
    }); 

    return ( 
        <div className="achievement-user-container"> 
            <div className="achievement-header">
                <h1>Logros</h1>
            </div>
            <div style={{ textAlign: "center", margin: "10px 0" }}>
                <Button color="success" >
                    <Link to={`/achievements/new`} className="btn sm"
                        style={{ textDecoration: "none", color: "white" }}>Create achievement</Link>
                </Button>
            </div>
            <div className="achievement-grid"> 
                {achievementCards}
            </div> 
        </div> 
    ); 
}