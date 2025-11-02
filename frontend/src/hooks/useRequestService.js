import * as RequestService from "../services/requestService";

const useRequestService = (jwt, message, setMessage) => {
  const getAllFriends = async (userId) => {
    return await RequestService.getAllFriendsByUserId(
      userId,
      jwt,
      message,
      setMessage
    );
  };

  const getAllSent = async (userId) => {
    return await RequestService.getAllPendingRequestsByUserId(
      userId,
      jwt,
      message,
      setMessage
    );
  };

  const getAllReceived = async (userId) => {
    return await RequestService.getAllReceivedRequestsByUserId(
      userId,
      jwt,
      message,
      setMessage
    );
  };

  const createRequest = async (username) => {
    return await RequestService.createNewRequest(
      username,
      jwt,
      message,
      setMessage
    );
  };

  const acceptRequest = async () => {
    return await RequestService.acceptFriendRequest(jwt, message, setMessage);
  };

  const rejectRequest = async () => {
    return await RequestService.rejectFriendRequest(jwt, message, setMessage);
  };

  const deleteFriend = async () => {
    return await RequestService.deleteFriendRequest(jwt, message, setMessage);
  };

  return {
    getAllFriends,
    getAllReceived,
    getAllSent,
    createRequest,
    acceptRequest,
    rejectRequest,
    deleteFriend,
  };
};

export default useRequestService;
