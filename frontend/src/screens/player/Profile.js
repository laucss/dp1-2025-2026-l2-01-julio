import { Button, Form, FormGroup, Label, Input, Col } from "reactstrap";
import { Link, Navigate, useNavigate } from "react-router-dom";
import tokenService from "../../services/token.service";
import Avatar_default from "../../static/images/Avatares/Avatar_default.png"
import "../../static/css/Profile/PlayerProfile.css"
import React, {useState, useEffect} from "react";
import getIdFromUrl from "../../util/getIdFromUrl";
import useFetchState from "../../util/useFetchState";
import useFetchData from "../../util/useFetchData";
import StandardImageList from "./ImageList";

const jwt = tokenService.getLocalAccessToken();


const Profile = ({ }) => {
  const emptyItem = {
      id: null,
      username: "",
      password: "",
      authority: null,
      avatar: Avatar_default,
    };
    const id = getIdFromUrl(2);
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const navigate = useNavigate();
    const [modalOpen, setModalOpen] = useState(false);
    const [password, setPassword] = useState("");

    const toggleModal = () => {
    setModalOpen(!modalOpen);
    };

    const [user, setUser] = useFetchState(
      emptyItem,
      `/api/v1/users/${id}`,
      jwt,
      setMessage,
      setVisible,
      id
    );

    

    const [selectedAvatarUrl, setSelectedAvatarUrl] = useState(Avatar_default);

        useEffect(() => {
        if (user && user.avatar) {
            setSelectedAvatarUrl(user.avatar);
        }
    }, [user]);

   

  const handleChange = (event) => {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setUser({ ...user, [name]: value })
  };

   const handleImageSelect = (imageUrl) => {
        setSelectedAvatarUrl(imageUrl);
        setUser({ ...user, avatar: imageUrl });
        toggleModal(); 
    };

    const handleSubmit = (e) => {
    e.preventDefault();
    const body = { ...user, password: password, authority: { id: 2, authority: "PLAYER" } };
    
    console.log(body);
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
      <h2>Edit Profile</h2>
    <div className="profile-card">
      <div className="profile-info">
        <img src={selectedAvatarUrl} alt="User Avatar" className="user-avatar" onClick={toggleModal} />
      </div>
      {modalOpen && (
          <div className="modal-backdrop" onClick={toggleModal}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
              <h2 className="modal-text">Edit avatar</h2>
              <StandardImageList onImageSelect={handleImageSelect} />
            </div>
          </div>
        )}
      <Form onSubmit={handleSubmit}>
          <div className="custom-form-input">
            <FormGroup row className="mb-3">
          <Label for="username" sm={3} className="custom-form-label">
            Username:
          </Label>
            <Input
              type="text"
              required
              name="username"
              id="username"
              value={user.username || ""}
              onChange={handleChange}
              className="custom-input-text"
            />
        </FormGroup>
          </div>
          <div className="custom-form-input">
            <FormGroup row className="mb-3">
            <Label for="password" sm={3} className="custom-form-label">
              Password:
            </Label>
            <Input
              type="password"
              name="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="custom-input-text"
            />
            </FormGroup>
          </div>
        

        <div className="profile-actions">
          <Button className="play-btn">
            Save Changes
          </Button>
          <Button className="remove-btn" tag={Link} to="/">
            Cancel
          </Button>
        </div>
      </Form>
    </div>
    </div>
  );
};

export default Profile;