import { Button } from "reactstrap";
import { Link } from "react-router-dom";
import tokenService from "../services/token.service";
import "../static/css/admin/adminPage.css";
import avatar from "../static/images/Avatares/Avatar1.jpg"
import "../static/css/Profile/PlayerProfile.css"
import React from "react";

const jwt = tokenService.getLocalAccessToken();
const user = tokenService.getUser();

const Profile = ({ }) => {
  return (
    <div className="profile-container">
      <h2>Perfil de Usuario</h2>

      <div className="profile-info">

        <img src={avatar} alt="User Avatar" className="profile-avatar" />
        <p className="profile-username">Username: {user.username}</p>

      </div>
      
      <div className="profile-actions">
       <Button
              size="sm"
              color="primary"
              aria-label={"edit-" + user.id}
              tag={Link}
              to={"users/ " + user.id}
            >
              Edit
            </Button>
        <Button
              size="sm"
              color="secondary"
              aria-label={"achievements-" + user.id}
              tag={Link}
              to={"Achievements"}
            >
              Achievements
        </Button>
      </div>
    </div>
  );
};




export default Profile;

//TODO: Añadir más campos del usuario (Achievements, estadisticas) y estilos