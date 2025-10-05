import { Button } from "reactstrap";
import { Link } from "react-router-dom";
import tokenService from "../services/token.service";
import "../static/css/admin/adminPage.css";


import React from "react";

const jwt = tokenService.getLocalAccessToken();
const user = tokenService.getUser();

const Profile = ({ }) => {
  return (
    <div style={{alignContent: "center", textAlign: "center"}}>
      <h2>Perfil de Usuario</h2>
      <p>Username: {user.username}</p>
       <Button
              size="sm"
              color="primary"
              aria-label={"edit-" + user.id}
              tag={Link}
              to={"EditProfile"}
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
  );
};

export default Profile;

//TODO: Añadir más campos del usuario (Achievements, estadisticas) y estilos