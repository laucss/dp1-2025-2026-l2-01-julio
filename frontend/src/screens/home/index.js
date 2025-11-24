import React from 'react';
import { useState } from "react";
import '../../App.css';
import '../../static/css/home/home.css';
import { useNavigate } from 'react-router-dom';
import tokenService from "../../services/token.service";
import useFetchState from '../../util/useFetchState';

const jwt = tokenService.getLocalAccessToken();

export default function Home(){
    const navigate = useNavigate();
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const user = tokenService.getUser();
    const currentMatch = useFetchState(
        [],
        user && user.id ? `/api/v1/matches/user/${user.id}/in` : null,
        jwt,
        setMessage,
        setVisible
    )[0];
    const returnToMatch = async () => {
        if (!currentMatch) return;
        try {
            const res = await fetch(`/api/v1/matches/${currentMatch}`, {
                headers: jwt ? { 'Authorization': `Bearer ${jwt}` } : {}
            });
            if (!res.ok) {
                navigate(`/match/${currentMatch}`);
                return;
            }
            const m = await res.json();
            const started = m && (m.startTime != null || m.start_time != null);
            if (started) navigate(`/match/${currentMatch}`);
            else navigate(`/lobby/${currentMatch}`);
        } catch (e) {
            navigate(`/match/${currentMatch}`);
        }
    };

    return(
        <div className="home-page-container">
            {jwt ? (
                <div className='home-buttons'>
                {((Array.isArray(currentMatch) && currentMatch.length>0) || (typeof currentMatch === 'number' && currentMatch>0) || (typeof currentMatch === 'string' && currentMatch.length>0)) ? (
                <button className='return-button' onClick={returnToMatch}>
                    Return to Match
                </button>
                ) : (
                <>      
                    <button className='create-button' onClick={() => navigate('/matchs/new')}>
                        Create Match  
                    </button> 
                    <button className='join-button'onClick={() => navigate('/lobbies')}>
                        Join Match  
                    </button>
                </>
                )}
            </div>   
            ) : (
            <div className='home-buttons'>
                <button className='create-button' onClick={() => navigate('/login')}>
                    Login  
                </button> 
                <button className='join-button'onClick={() => navigate('/register')}>
                    Register  
                </button>
            </div>            
            )}
        </div>

    );



}


