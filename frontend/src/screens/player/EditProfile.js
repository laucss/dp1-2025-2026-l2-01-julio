import { useState } from "react";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";
import useFetchData from "../../util/useFetchData";
import getIdFromUrl from "../../util/getIdFromUrl";

const jwt = tokenService.getLocalAccessToken();

export default function EditProfile() {
  const emptyItem = {
    id: null,
    username: "",
    password: "",
    authority: null,
  };

  
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [user, setUser] = useFetchState(
    
   );

  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    if (name === "authority") {
      const auth = "PLAYER"
      setUser({ ...user, authority: auth }); 
    } else setUser({ ...user, [name]: value });
  }

  function handleSubmit(event) {
    event.preventDefault();

 console.log("Enviando este objeto al servidor:", user);

    fetch(`/api/v1/users/${user.id}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user), 
    })
      .then((response) => response.json())
      .then((json) => {
        if (json.message) {
          setMessage(json.message);
          setVisible(true);
        } else window.location.href = "/profile";
      })
      .catch((message) => alert(message));
  }

  const modal = getErrorModal(setVisible, visible, message);

  
  return (
    <div className="auth-page-container">
      <h2>Edita tu perfil</h2>
      {modal}
      <div className="auth-form-container">
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
            <Label for="password" className="custom-form-input-label">
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
          <Label className="custom-form-input-label">Authority</Label>
          <div className="custom-button-row">
            <button className="auth-button">Save</button>
            <Link
              to={`/profile`}
              className="auth-button"
              style={{ textDecoration: "none" }}
            >
              Cancel
            </Link>
          </div>
        </Form>
      </div>
    </div>
  );
}
