import {
  serviceResponseHandler,
  serviceErrorHandler,
} from "../apiServices/errorHandlingService";
import {
  fetchAllFriendsByUserId,
  fetchAllPendingRequestsByUserId,
  fetchAllReceivedRequestsByUserId,
  createRequest,
  acceptRequest,
  rejectRequest,
  deleteFriend,
} from "../apiServices/requestApiService";

const getAllFriendsByUserId = async (id, jwt, message, setMessage) => {
  return serviceResponseHandler(
    await serviceErrorHandler(
      async () => await fetchAllFriendsByUserId(id, jwt)
    ),
    "friendRequest",
    message,
    setMessage
  );
};

const getAllPendingRequestsByUserId = async (id, jwt, message, setMessage) => {
  return serviceResponseHandler(
    await serviceErrorHandler(
      async () => await fetchAllPendingRequestsByUserId(id, jwt)
    ),
    "friendRequest",
    message,
    setMessage
  );
};

const getAllReceivedRequestsByUserId = async (id, jwt, message, setMessage) => {
  return serviceResponseHandler(
    await serviceErrorHandler(
      async () => await fetchAllReceivedRequestsByUserId(id, jwt)
    ),
    "friendRequest",
    message,
    setMessage
  );
};

const createNewRequest = async (username, jwt, message, setMessage) => {
  return serviceResponseHandler(
    await serviceErrorHandler(async () => await createRequest(username, jwt)),
    "friendRequest",
    message,
    setMessage
  );
};

const acceptFriendRequest = async (requestId, jwt, message, setMessage) => {
  return serviceResponseHandler(
    await serviceErrorHandler(async () => await acceptRequest(requestId, jwt)),
    "friendRequest",
    message,
    setMessage
  );
};

const rejectFriendRequest = async (jwt, message, setMessage) => {
  return serviceResponseHandler(
    await serviceErrorHandler(async () => await rejectRequest(jwt)),
    "friendRequest",
    message,
    setMessage
  );
};

const deleteFriendRequest = async (jwt, message, setMessage) => {
  return serviceResponseHandler(
    await serviceErrorHandler(async () => await deleteFriend(jwt)),
    "friendRequest",
    message,
    setMessage
  );
};

export {
  getAllFriendsByUserId,
  getAllPendingRequestsByUserId,
  getAllReceivedRequestsByUserId,
  createNewRequest,
  acceptFriendRequest,
  rejectFriendRequest,
  deleteFriendRequest,
};
