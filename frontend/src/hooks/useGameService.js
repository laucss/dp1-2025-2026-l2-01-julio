//aqui van imports

const useGameService = (jwt, message, setMessage) => {


    //hay que hacer gameService
const getChat = async () => {
    return await GameService.getChat(jwt, message, setMessage);
  };

  const sendChatMessage = async (chatMessage) => {
    return await GameService.sendChatMessage(
      chatMessage,
      jwt,
      message,
      setMessage
    );
  };

  return { getChat, sendChatMessage };

};

export default useGameService;