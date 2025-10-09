import { Button, Form, FormGroup, Label, Input } from "reactstrap";
import { Link, Navigate, useNavigate } from "react-router-dom";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import avatar from "../../static/images/Avatares/Avatar1.jpg"
import "../../static/css/Profile/PlayerProfile.css"
import React, {useState} from "react";
import getIdFromUrl from "../../util/getIdFromUrl";
import useFetchState from "../../util/useFetchState";
import useFetchData from "../../util/useFetchData";

const jwt = tokenService.getLocalAccessToken();


const Profile = ({ }) => {
  const emptyItem = {
      id: null,
      username: "",
      password: "",
      authority: null,
    };
    const id = getIdFromUrl(2);
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
     const navigate = useNavigate();
    const [user, setUser] = useFetchState(
      emptyItem,
      `/api/v1/users/${id}`,
      jwt,
      setMessage,
      setVisible,
      id
    );


  const handleChange = (event) => {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setUser({ ...user, [name]: value })
  };

    const handleSubmit = (e) => {
    e.preventDefault();
    const body = { ...user, authority: { id: 2, authority: "PLAYER" } };
    
      fetch(`/api/v1/users/${user.id}`, {
      method: "PUT",
      headers: {
        "Authorization": `Bearer ${jwt}`,
        "Accept": "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    })
    .then((response) => response.json())
    .then((json) => {
      if (json.message) {
        setMessage(json.message);
        setVisible(true);

      } else {
        console.log(json)
        tokenService.setUser(json);
        tokenService.updateLocalAccessToken(json.token);
        window.location.href = "/";
      }
    })
    .catch((message) => alert(message));
  };

  return (
    <div className="profile-container">
      <h2>Editar Perfil</h2>

      <div className="profile-info">
        <img src={avatar} alt="User Avatar" className="profile-avatar" />
      </div>
      
      <Form onSubmit={handleSubmit}>
          <div className="custom-form-input">
            <Label for="username" className="custom-form-input-label">
              Username
            </Label>
            <Input
              type="text"
              required
              name="username"
              id="username"
              value={user.username || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="lastName" className="custom-form-input-label">
              Password
            </Label>
            <Input
              type="password"
              required
              name="password"
              id="password"
              value={user.password || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
        

        <div className="profile-actions">
          <Button color="primary" type="submit">
            Guardar Cambios
          </Button>
          <Button tag={Link} to="/" color="secondary">
            Cancelar
          </Button>
        </div>
      </Form>
    </div>
  );
};




export default Profile;