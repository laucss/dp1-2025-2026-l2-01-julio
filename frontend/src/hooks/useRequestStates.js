import { useState } from "react";
import useRequestService from "./useRequestService";

const useRequestStates = (jwt, errorMessage, setErrorMessage) => {
  const [allFriends, setAllFriends] = useState([]);
  const [allReceived, setAllReceived] = useState([]);
  const [allSent, setAllSent] = useState([]);
  const [message, setMessage] = useState(null);

  const {
    getAllFriends,
    getAllReceived,
    getAllSent,
    createRequest: svcCreateRequest,
    acceptRequest: svcAcceptRequest,
    rejectRequest: svcRejectRequest,
    deleteFriend: svcDeleteFriend,
  } = useRequestService(jwt, errorMessage, setErrorMessage);

  const getAndSetAllFriends = async (userId) => {
  const allFriendsResponse = await getAllFriends(userId);
    // Normalizar distintas formas de respuesta del backend
    let friendsArray = [];
    if (Array.isArray(allFriendsResponse)) {
      friendsArray = allFriendsResponse;
    } else if (allFriendsResponse && Array.isArray(allFriendsResponse.content)) {
      friendsArray = allFriendsResponse.content;
    } else if (allFriendsResponse && Array.isArray(allFriendsResponse.data)) {
      friendsArray = allFriendsResponse.data;
    }
    setAllFriends(friendsArray);
    return friendsArray;
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
    const newRequest = await svcCreateRequest(username);
    return newRequest;
  };

  const acceptRequest = async (requestId) => {
    const acceptedRequest = await svcAcceptRequest(requestId);
    return acceptedRequest;
  };

  const rejectRequest = async () => {
    const rejected = await svcRejectRequest();
    return rejected;
  };

  const deleteFriend = async () => {
    const deletedFriend = await svcDeleteFriend();
    return deletedFriend;
  };

  // Añadir un amigo directamente al estado (evita esperar al GET si el backend tarda)
  const addFriendToState = (friend) => {
    if (!friend) return;
    setAllFriends((prev) => {
      try {
        const exists = prev.some((f) => f.id === friend.id);
        if (exists) return prev;
      } catch (e) {
        // en caso de que prev no sea array
      }
      return Array.isArray(prev) ? [...prev, friend] : [friend];
    });
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
    addFriendToState,
  };
};

export default useRequestStates;