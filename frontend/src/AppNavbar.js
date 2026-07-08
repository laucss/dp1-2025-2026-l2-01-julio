import React, { useState, useEffect } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import { Link } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from "jwt-decode";
import Sidebar from './Sidebar';
import { FaBars } from 'react-icons/fa';
import { IoNotifications } from "react-icons/io5";
import { Client } from '@stomp/stompjs';
import '../src/static/css/appnavbar/navbar.css';
import NotificationsModal from './NotificationsModal';

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function AppNavbar() {
    const [roles, setRoles] = useState([]);
    const [username, setUsername] = useState("");
    const [user, setUser] = useState({});
    const jwt = tokenService.getLocalAccessToken();
    const [collapsed, setCollapsed] = useState(true);
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const [notifications, setNotifications] = useState([]);
    const [showNotifications, setShowNotifications] = useState(false);
      
    const handleNotificationsClick = () => {
        setShowNotifications(true);
    };
    const handleCloseNotifications = () => setShowNotifications(false);
    
    const toggleNavbar = () => setCollapsed(!collapsed);
    const toggleSidebar = () => setSidebarOpen(!sidebarOpen);

    // Función para transformar el NotificationType del backend en un texto amigable
    const getToastMessage = (type) => {
        switch (type) {
            case "FRIEND_REQUEST":
                return "You received a new friend request!";
            case "MATCH_INVITATION_AS_PLAYER":
                return "You have been invited to join a match as a player!";
            case "MATCH_INVITATION_AS_SPECTATOR":
                return "You have been invited to spectate a match!";
            case "ACCEPT_FRIEND_REQUEST":
                return "Your friend request was accepted!";
            case "REJECT_FRIEND_REQUEST":
                return "Your friend request was rejected.";
            case "ACCEPT_INVITATION":
                return "A player accepted your match invitation!";
            case "REJECT_INVITATION":
                return "A player rejected your match invitation.";
            default:
                return "🔔 New notification received!";
        }
    };

    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
            setUsername(jwt_decode(jwt).sub);
            const currentUser = tokenService.getUser();
            setUser(currentUser);
        }
    }, [jwt]);
    
    const fetchNotifications = async () => {
        if (!jwt) return;
        try {
            const res = await fetch("/api/v1/notifications", {
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            });
            if (!res.ok) return;
            const data = await res.json();
            setNotifications(Array.isArray(data) ? data : []);
        } catch (e) {
            console.error(e);
        }
    };

    useEffect(() => {
        if (jwt) {
            fetchNotifications();
        }
    }, [jwt]);

    useEffect(() => {
        if (!jwt || !user?.id) return;

        const client = new Client({
            brokerURL: 'ws://localhost:8080/ws', 
            connectHeaders: { 
                'Authorization': `Bearer ${jwt}` 
            },
            onConnect: () => {
                console.log('Connected to notifications websocket');
                
                client.subscribe(`/topic/user.${user.id}.notifications`, (message) => {
                    if (message.body) {
                        const notificationType = message.body.replace(/(^"|"$)/g, '').trim(); 
                        
                        console.log('notificationType limpia:', notificationType);

                        toast.info(getToastMessage(notificationType), {
                            position: "top-right",
                            autoClose: 5000,
                            hideProgressBar: false,
                            closeOnClick: true,
                            pauseOnHover: true,
                            draggable: true,
                            progress: undefined,
                            theme: "colored",
                        });

                        fetchNotifications();
                    }
                });
            },
            onStompError: (frame) => {
                console.error('Broker error: ' + frame.headers['message']);
            }
        });

        client.activate();

        return () => {
            if (client.active) {
                client.deactivate();
            }
        };
    }, [jwt, user?.id]);

    let adminLinks = <></>;
    let playerLinks = <></>;
    let ownerLinks = <></>;
    let userLinks = <></>;
    let userLogout = <></>;
    let publicLinks = <></>;
    let profileLinks = <></>;

    roles.forEach((role) => {
        if (role === "ADMIN" || role === "PLAYER") {
            profileLinks = (
                <>
                    <NavItem>
                        <NavLink
                            style={{ color: "white", cursor: "pointer", marginRight: "20px" }}
                            onClick={handleNotificationsClick}
                        >
                            <div className="notification-container">
                                <IoNotifications size={24} />
                                {notifications.length > 0 && (
                                    <span className="notification-badge">
                                        {notifications.length}
                                    </span>
                                )}
                            </div>
                            Notifications 
                        </NavLink>
                    </NavItem>
                    <NavbarText style={{ color: "white" }} className="justify-content-end">{username}</NavbarText>
                    <NavItem>
                        <NavLink style={{ color: "white", cursor: "pointer" }} onClick={toggleSidebar}><FaBars/></NavLink>
                    </NavItem> 
                </>
            );
        }   
    });

    if (!jwt) {
        publicLinks = (
            <>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="rules" tag={Link} to="/rules">Rules</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="ranking" tag={Link} to="/ranking">Ranking</NavLink>
                </NavItem>
            </>
        );
    }

    return (
        <div>
            {/* Contenedor global de Toastify indispensable para renderizar las alertas flotantes */}
            <ToastContainer />

            <Navbar expand="md" dark style={{ backgroundColor: '#d58a5b', borderBottom: '4px solid #a7661b' }}>
                <NavbarBrand href="/" style={{ color: '#ffffff', fontWeight: 700 }}>
                    Escape From Elba
                </NavbarBrand>
                <NavbarToggler onClick={toggleNavbar} className="ms-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav className="me-auto mb-2 mb-lg-0" navbar>
                        {userLinks}
                        {adminLinks}
                        {ownerLinks}
                    </Nav>
                    <Nav className="ms-auto mb-2 mb-lg-0" navbar>
                        {publicLinks}
                        {profileLinks}
                        {userLogout}
                    </Nav>
                </Collapse>
            </Navbar>

            <Sidebar isOpen={sidebarOpen} toggle={toggleSidebar} user={user} />
            {sidebarOpen && <div className="sidebar-overlay" onClick={toggleSidebar}></div>}
            
            <NotificationsModal 
                isOpen={showNotifications} 
                onClose={handleCloseNotifications} 
                notifications={notifications}
                refreshNotifications={fetchNotifications}
            />
        </div>
    );
}

export default AppNavbar;