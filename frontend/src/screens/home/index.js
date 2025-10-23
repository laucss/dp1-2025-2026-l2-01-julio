import React from 'react';
import '../../App.css';
import '../../static/css/home/home.css';
import { useNavigate } from 'react-router-dom'; 

export default function Home(){
    const navigate = useNavigate();
    return(
        <div className="home-page-container">
            <div className='home-buttons'>
                <button className='create-button' onClick={() => navigate('/matchs/new')}>
                        Crear partida  
                </button>  
                <button className='join-button'onClick={() => navigate('/lobbies')}>
                        Unirse a partida  
                </button>
            </div>
        </div>

    );



}


