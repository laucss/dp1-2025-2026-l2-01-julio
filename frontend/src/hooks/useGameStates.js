import { useState } from "react";
import useGameService from "./useGameService";

const useGameStates = (jwt, errorMessage, setErrorMessage) => {
    const [chat, setChat] = useState([]);

    const {
        getChat,
    } = useGameService(jwt, errorMessage, setErrorMessage);

    const getAndSetChat = async () => {
        const chat = await getChat();
        setChat(chat ? chat : []);
        return chat;
    };

    return {
        chat,
        getAndSetChat,
    };
};

export default useGameStates;