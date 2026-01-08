import React, { useState } from 'react';
import { Nav, NavItem, NavLink } from 'reactstrap';
import { Link } from 'react-router-dom';
import './static/css/appnavbar/sidebar.css'; 
import tokenService from './services/token.service';
import NotificationsModal from './NotificationsModal';



function Sidebar({ isOpen, toggle, user}) {
  const [showNotifications, setShowNotifications] = useState(false);
  const handleNotificationsClick = () => {
    setShowNotifications(true);
    toggle();
  };
  const handleCloseNotifications = () => setShowNotifications(false);
  return (
    <>
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
            <NavLink tag={Link} to={`/users/${user.id}/achievements`} onClick={toggle}>
              Achievement
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink tag={Link} to={`/users/${user.id}/statistics`} onClick={toggle}>
              Statistics
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink tag={Link} to="/rules" onClick={toggle}>
              Rules
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink href="#" onClick={handleNotificationsClick}>
              Notifications
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink tag={Link} to="/logout" onClick={toggle}>
              Logout
            </NavLink>
          </NavItem>
        </Nav>
      </div>
      <NotificationsModal isOpen={showNotifications} onClose={handleCloseNotifications} />
    </>
  );
}

export default Sidebar;