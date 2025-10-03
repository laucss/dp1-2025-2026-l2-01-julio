import React from 'react';
import '../App.css';
import '../static/css/home/home.css'; 
import logo from '../static/images/napoleon.png'

export default function Home(){
    return(
        <div className="home-page-container">
            <div className="hero-div">
                <h1>ESCAPE FROM ELBA</h1>
                <img src={logo} className="napoleon-div" />                       
            </div>
        </div>
    );
}