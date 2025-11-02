import { generalFetcher } from "./generalFetcher";

const requestBaseURL = "/api/v1/friendRequests";

async function fetchAllFriendsByUserId(userId, jwt) {
    return await generalFetcher(`${requestBaseURL}/${userId}`, jwt);
}

async function fetchAllPendingRequestsByUserId(userId,jwt) {
    return await generalFetcher(`${requestBaseURL}/${userId}/pending`, jwt);
}

async function fetchAllReceivedRequestsByUserId(userId,jwt) {
    return await generalFetcher(`${requestBaseURL}/${userId}/received`, jwt);
}

async function createRequest(username, jwt) {
    return await generalFetcher(`${requestBaseURL}/${username}`, jwt, "POST");
}

async function acceptRequest(jwt) {
    return await generalFetcher(`${requestBaseURL}/accept`, jwt, "PUT");
}

async function rejectRequest(jwt) {
    return await generalFetcher(`${requestBaseURL}/reject`, jwt, "PUT");
}

async function deleteFriend(jwt) {
    return await generalFetcher(`${requestBaseURL}`,jwt,"DELETE");
}

export {
    fetchAllFriendsByUserId,
    fetchAllPendingRequestsByUserId,
    fetchAllReceivedRequestsByUserId,
    createRequest,
    acceptRequest,
    rejectRequest,
    deleteFriend
    };