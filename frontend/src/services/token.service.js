import jwt_decode from "jwt-decode";

class TokenService {
    getLocalRefreshToken() {
        const user = JSON.parse(localStorage.getItem("user"));
        return user?.refreshToken;
    }

    // getLocalAccessToken() {
    //     const user = JSON.parse(localStorage.getItem("user"));
    //     return user?.token;
    // }

    getLocalAccessToken() {
        const jwt = JSON.parse(localStorage.getItem("jwt"));
        if (!this.isTokenValid(jwt)) {
            this.removeUser(); 
            return null;
        }

        return jwt;
    }

    updateLocalAccessToken(token) {
        window.localStorage.setItem("jwt", JSON.stringify(token));
    }

    // updateLocalAccessToken(token) {
    //     let user = JSON.parse(localStorage.getItem("user"));
    //     user.token = token;
    //     window.localStorage.setItem("user", JSON.stringify(user));
    // }

    getUser() {
        return JSON.parse(localStorage.getItem("user"));
    }

    setUser(user) {
        window.localStorage.setItem("user", JSON.stringify(user));
    }

    removeUser() {
        window.localStorage.removeItem("user");
        window.localStorage.removeItem("jwt");
    }

    isTokenValid(token) {
        if (!token) return false;

        try {
            const decoded = jwt_decode(token);
            const now = Date.now() / 1000;

            return decoded.exp > now;
        } catch (e) {
            return false;
        }
    }

}
const tokenService = new TokenService();

export default tokenService;