import { useState } from "react";
import { Link } from "react-router-dom";
import { Button, ButtonGroup, Card, Table } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";
import { FaArrowLeft, FaArrowRight } from "react-icons/fa";

const jwt = tokenService.getLocalAccessToken();

export default function UserListAdmin() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [users, setUsers] = useFetchState(
    [],
    `/api/v1/users`,
    jwt,
    setMessage,
    setVisible
    
  );
  const [alerts, setAlerts] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const usersPerPage = 5;
  const totalPages = Math.ceil(users.length / usersPerPage);
  const LastUser = currentPage * usersPerPage;
  const FirstUser = LastUser - usersPerPage;
  const currentUsers = users.slice(FirstUser, LastUser);

  const userList = currentUsers.map((user) => {
    return (
      <tr key={user.id}>
        <td>
          <img src={user.avatar ? user.avatar : '/Avatar_default.png'} alt="Avatar" className="user-avatar" />
        </td>
        <td>{user.username}</td>
        <td>{user.authority.authority}</td>
        <td>
          <ButtonGroup>
            <Button
              size="sm"
              color="primary"
              aria-label={"edit-" + user.id}
              tag={Link}
              to={"/users/" + user.id}
            >
              Edit
            </Button>
            <Button
              size="sm"
              color="danger"
              aria-label={"delete-" + user.id}
              onClick={() =>
                deleteFromList(
                  `/api/v1/users/${user.id}`,
                  user.id,
                  [users, setUsers],
                  [alerts, setAlerts],
                  setMessage,
                  setVisible
                )
              }
            >
              Delete
            </Button>
          </ButtonGroup>
        </td>
      </tr>
    );
  });
  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="admin-page-container" style={{borderRadius:'15px'}}>
      <div className="admin-users-list"> 
        <div style={{background:'#f8f9fa', display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 20px', borderRadius: '15px 15px 0 0'}}>
          <h1 >Users</h1>
          <Button color="success" tag={Link} to="/users/new" >
            Add User
          </Button>
        </div> 
      {alerts.map((a) => a.alert)}
      {modal}
        <Table style={{marginBottom: 0, width: '100%', tableLayout: 'fixed', background: '#fff' }}>
          <thead style={{borderBottom: '1px solid #000'}}> 
            <tr style={{background:'#f8f9fa'}}>
              <th>Username</th>
              <th>Authority</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>{userList}</tbody>
        </Table>
      </div>
    <div className="pagination-buttons" style={{marginTop: '25px'}}>
        <Button
          disabled={currentPage === 1}
          onClick={() => setCurrentPage((prev) => prev - 1)}
        >
          <FaArrowLeft />
        </Button>
        <span>
          {currentPage} of {totalPages}
        </span>
        <Button
          disabled={currentPage === totalPages}
          onClick={() => setCurrentPage((prev) => prev + 1)}
        >
          <FaArrowRight />
        </Button>
      </div>
    </div>
    
  );
}