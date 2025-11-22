import React, { useContext, useEffect, useRef, useState } from "react";
import ChatMessage from "./chatMessage";
import "./chat.css";
import { HiMiniChatBubbleOvalLeftEllipsis } from "react-icons/hi2";
import { BiSolidSend } from "react-icons/bi";
import ErrorMessageAlert from "../../../../../components/ErrorMessageAlert";
import useGameStates from "../../../../../hooks/useGameStates";
import tokenService from "../../../../../services/token.service";
import useGameService from "../../../../../hooks/useGameService";
import { FaAngleDoubleDown } from "react-icons/fa";
import { ChatContext } from "../../../../../context/chatContext";

export default function Chat() {
  const jwt = tokenService.getLocalAccessToken();
  const { chatIsShown, setChatIsShown } = useContext(ChatContext);
  const [text, setText] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [isAtTheBottom, setIsAtTheBottom] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [chatSize, setChatSize] = useState(0);
  const [showNewMessageIcon, setShowNewMessageIcon] = useState(false);
  const chatBoxRef = useRef(null);
  const intervalRef = useRef(null);

  const { chat, getAndSetChat } = useGameStates(
    jwt,
    errorMessage,
    setErrorMessage
  );

  const { sendChatMessage } = useGameService(
    jwt,
    errorMessage,
    setErrorMessage
  );

  const handleKeyDown = (e) => {
    if (e.keyCode == 13 && e.shiftKey == false) {
      e.preventDefault();
      handleSubmit();
    }
  };

  const handleSubmit = async () => {
    const length = text.trim().length;
    if (length >= 1000) {
      setErrorMessage(new String("Usa menos de 1000 caracteres para el mensaje."));
      return;
    } else if (length > 0) {
      setSubmitting(true);
      await sendChatMessage({ content: text });
      await getAndSetChat();
      setText("");
      setTimeout(goDown, 200);
    }
    setErrorMessage(null);
  };

  const goDown = () => {
    if (chatBoxRef.current) {
      chatBoxRef.current.scrollTop = chatBoxRef.current.scrollHeight;
    }
    setShowNewMessageIcon(false);
  };

  const checkIsAtBottom = () => {
    if (
      chatBoxRef.current &&
      chatBoxRef.current.scrollTop + chatBoxRef.current.clientHeight >=
        chatBoxRef.current.scrollHeight - 60
    ) {
      setIsAtTheBottom(true);
      setShowNewMessageIcon(false);
      return;
    }
    setIsAtTheBottom(false);
  };

  const chatPolling = async () => {
    checkIsAtBottom();
    await getAndSetChat();
  };

  const handleClick = async () => {
    const currentShown = chatIsShown;
    setChatIsShown(!currentShown);
    if (!currentShown) {
      await getAndSetChat();
      intervalRef.current = setInterval(chatPolling, 1000);
      goDown();
    } else {
      clearInterval(intervalRef.current);
      intervalRef.current = null;
    }
  };

  useEffect(() => {
    if (chat.length !== chatSize && !submitting) {
      if (isAtTheBottom) {
        goDown();
      } else {
        setShowNewMessageIcon(true);
      }
      setChatSize(chat.length);
    } else if (chat.length !== chatSize) {
      setShowNewMessageIcon(false);
      setChatSize(chat.length);
      setSubmitting(false);
    }
  }, [chat]);

  useEffect(() => {
    setChatIsShown(false);
  }, []);

  return (
    <>
      <button
        className={`chat-button ${chatIsShown ? " chat-button-pressed" : ""}`}
        onClick={handleClick}
      >
        <HiMiniChatBubbleOvalLeftEllipsis
          className="chat-button-icon"
          size={30}
          color="white"
        />
      </button>
      <div className={`chat-box ${chatIsShown ? "" : "hide"}`}>
        {chatIsShown && !isAtTheBottom && showNewMessageIcon && (
          <FaAngleDoubleDown
            onClick={() => {
              goDown();
              setShowNewMessageIcon(false);
            }}
            className="chat-new-message-icon"
            size={35}
          />
        )}
        <>
          <div className="chat-content" ref={chatBoxRef}>
            {chat.map((c) => (
              <ChatMessage key={c.id} message={c} />
            ))}
          </div>
          <div className="chat-write">
            <textarea
              className="chat-write-input"
              placeholder="Talk with others here..."
              value={text}
              onChange={(e) => setText(e.target.value)}
              onKeyDown={handleKeyDown}
            />
            <button className="chat-submit-button" onClick={handleSubmit}>
              <BiSolidSend className="chat-submit-icon" />
            </button>
          </div>
          <ErrorMessageAlert errorMessage={errorMessage} />
        </>
      </div>
    </>
  );
}
