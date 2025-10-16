import React from 'react';
import { Nav, NavItem, NavLink } from 'reactstrap';
import { Link } from 'react-router-dom';
import './static/css/appnavbar/sidebar.css'; 
import tokenService from './services/token.service';



function Sidebar({ isOpen, toggle, user}) {
  console.log("Sidebar user prop:", user);
  return (
    
    <div className={`sidebar ${isOpen ? 'is-open' : ''}`}>
      <div className="sidebar-header">
        <span className="sidebar-close" onClick={toggle}>&times;</span>
        <h3>{user.username}</h3>
        <img src={user.avatar ? user.avatar : '/Avatar_default.png'} alt="Avatar" className="sidebar-avatar" />
      </div>
      <Nav vertical>
        <NavItem>
          <NavLink tag={Link} to={`/users/${user.id}`} onClick={toggle}>
            Edit Profile
          </NavLink>
        </NavItem>
        <NavItem>
          <NavLink tag={Link} to="/friends" onClick={toggle}>
            Friends
          </NavLink>
        </NavItem>
        <NavItem>
          <NavLink tag={Link} to="/achievements" onClick={toggle}>
            Achievement
          </NavLink>
        </NavItem>
        <NavItem>
          <NavLink tag={Link} to="/statistics" onClick={toggle}>
            Statistics
          </NavLink>
        </NavItem>
        <NavItem>
          <NavLink tag={Link} to="/rules" onClick={toggle}>
            Rules
          </NavLink>
        </NavItem>
        <NavItem>
          <NavLink tag={Link} to="/logout" onClick={toggle}>
            Logout
          </NavLink>
        </NavItem>
      </Nav>
    </div>
  );
}

export default Sidebar;