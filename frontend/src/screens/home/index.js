import React from 'react';
import './home.css';
import '../../App.css';
import '../../static/css/home/home.css'; 
import logo from '../../static/images/napoleon.png'

export default function Home(){
    return(
        <div className="home-page-container">
            <div className='home-container'>
                <h1 className='home-title'>
                    ESCAPE FROM ELBA
                </h1>            
                <div className='home-buttons'>
                    <button className='create-button'>
                        Crear partida  
                    </button>  
                    <button className='join-button'>
                        Unirse a partida  
                    </button> 

                </div>
                
            </div>
         <img src={logo} className="napoleon-div" /> 
        </div>
    );



}


