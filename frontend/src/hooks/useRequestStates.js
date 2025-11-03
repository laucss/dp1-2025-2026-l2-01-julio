import { useState } from "react";
import useRequestService from "./useRequestService";

const useRequestStates = (jwt, errorMessage, setErrorMessage) => {
  const [allFriends, setAllFriends] = useState([]);
  const [allReceived, setAllReceived] = useState([]);
  const [allSent, setAllSent] = useState([]);

  const {
    getAllFriends,
    getAllReceived,
    getAllSent,
    createNewRequest,
    acceptFriendRequest,
    rejectFriendRequest,
    deleteFriendRequest,
  } = useRequestService(jwt, errorMessage, setErrorMessage);

  const getAndSetAllFriends = async (userId) => {
    const allFriends = await getAllFriends(userId);
    setAllFriends(allFriends);
    return allFriends;
  };

  const getAndSetReceivedRequests = async (userId) => {
    const allReceived = await getAllReceived(userId);
    setAllReceived(allReceived);
    return allReceived;
  };

  const getAndSetSentRequests = async (userId) => {
    const allSent = await getAllSent(userId);
    setAllSent(allSent);
    return allSent;
  };

  const createRequest = async (username) => {
    const newRequest = await createNewRequest(username);
    return newRequest;
  };

  const acceptRequest = async () => {
    const acceptedRequest = await acceptFriendRequest();
    return acceptedRequest;
  };

  const rejectRequest = async () => {
    const acceptedRequest = await rejectFriendRequest();
    return acceptedRequest;
  };

  const deleteFriend = async () => {
    const deletedFriend = await deleteFriendRequest();
    return deletedFriend;
  };

  return {
    allFriends,
    getAndSetAllFriends,
    allReceived,
    getAndSetReceivedRequests,
    allSent,
    getAndSetSentRequests,
    createRequest,
    acceptRequest,
    rejectRequest,
    deleteFriend,
  };
};

export default useRequestStates;