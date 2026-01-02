const API_BASE_URL = '/api/v1/users/status';

class UserStatusService {
  setOnline(jwtToken) {
    return fetch(`${API_BASE_URL}/online`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${jwtToken}`,
        'Accept': 'application/json',
      },
    });
  }
  setOffline(jwtToken) {
    return fetch(`${API_BASE_URL}/offline`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${jwtToken}`,
        'Accept': 'application/json',
      },
    });
  }
}

export default new UserStatusService();
