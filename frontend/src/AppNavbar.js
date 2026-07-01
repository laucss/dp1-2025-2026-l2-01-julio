import React, { useState, useEffect } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse, Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Link } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from "jwt-decode";
import Sidebar from './Sidebar';
import { FaBars } from 'react-icons/fa'

function AppNavbar() {
    const [roles, setRoles] = useState([]);
    const [username, setUsername] = useState("");
    const [user, setUser] = useState({});
    const jwt = tokenService.getLocalAccessToken();
    const [collapsed, setCollapsed] = useState(true);
    const [sidebarOpen, setSidebarOpen] = useState(false);
    
    const toggleNavbar = () => setCollapsed(!collapsed);
    const toggleSidebar = () => setSidebarOpen(!sidebarOpen);


    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
            setUsername(jwt_decode(jwt).sub);
            setUser(tokenService.getUser());
        }
    }, [jwt])

    let adminLinks = <></>;
    let playerLinks = <></>;
    let ownerLinks = <></>;
    let userLinks = <></>;
    let userLogout = <></>;
    let publicLinks = <></>;
    let profileLinks = <></>;

    roles.forEach((role) => {
        if (role === "ADMIN") {
        
            profileLinks = (
                <>
                    <NavbarText style={{ color: "white" }} className="justify-content-end">{username}</NavbarText>
                    {/* TODO: provisional hasta poner logout en AppNavbar cuando  eres admin */}
                    <NavItem>
                        <NavLink style={{ color: "white", cursor: "pointer" }} onClick={toggleSidebar}><FaBars/></NavLink>
                    </NavItem> 
                </>
            )
        }   
    })
        roles.forEach((role) => {
        if (role === "PLAYER") {
            profileLinks = (
                <>
                    <NavItem>
                        <NavLink style={{ color: "white", cursor: "pointer" }} onClick={toggleSidebar}><FaBars/></NavLink>
                    </NavItem> 
                </>
            )
        }        
    })

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
        )
    } else {
        userLinks = (
            <>
            </>
        )

    }

    return (
        <div>
            <Navbar
                expand="md"
                dark
                style={{ backgroundColor: '#d58a5b', borderBottom: '4px solid #a7661b' }}
            >
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

        </div>
    );
}

export default AppNavbar;