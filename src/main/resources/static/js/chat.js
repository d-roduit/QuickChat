/* Variables */
const copyChatURLButton = document.getElementById("copy-chat-url-btn")
const messageTextarea = document.getElementById("message-textarea");
const sendMessageButton = document.getElementById("send-message-button");
const chatBox = document.getElementById("chat-box");
const chatMessageSentTemplate = document.getElementById("chat-message-sent-template");
const chatMessageReceivedTemplate = document.getElementById("chat-message-received-template");

let isCopyChatURLButtonClickable = true;
let stompClient = null;

const websocketEndpoints = {
    registration: "/register-websocket",
    chatSubscription: `/topic/chat/${UUID}`,
    chatSending: `/app/chat/${UUID}`
};

/* Functions */
const copyChatURLToClipboard = () => {
    navigator.clipboard.writeText(window.location.href)
        .then(() => {
            if (isCopyChatURLButtonClickable) {
                isCopyChatURLButtonClickable = false;
                const defaultText = copyChatURLButton.textContent;
                copyChatURLButton.innerHTML = "Copied <i class=\"fas fa-check\"></i>";
                setTimeout(() => {
                    copyChatURLButton.textContent = defaultText;
                    isCopyChatURLButtonClickable = true;
                }, 1000);
            }
        })
        .catch(err => console.log(err));
}

const resizeTextAreaToFitContent = () => {
    messageTextarea.style.height = '';
    messageTextarea.style.height = messageTextarea.scrollHeight + 'px';
}

const convertDateTimeInLocalTime = (dateTime) => {
    const localDate = new Date(dateTime);
    const hours = (localDate.getHours() < 10) ? `0${localDate.getHours()}` : localDate.getHours();
    const minutes = (localDate.getMinutes() < 10) ? `0${localDate.getMinutes()}` : localDate.getMinutes();
    return `${hours}:${minutes}`;
}

const addMessageToChatBox = (chatMessageObj) => {
    const chatMessageTemplate = (chatMessageObj.username === username) ? chatMessageSentTemplate : chatMessageReceivedTemplate;
    const chatMessageClone = chatMessageTemplate.content.cloneNode(true);

    chatMessageClone.querySelector(".chat-message").textContent = chatMessageObj.message;
    chatMessageClone.querySelector(".chat-message-time").textContent = convertDateTimeInLocalTime(chatMessageObj.sendingDateTime);

    if (chatMessageObj.username !== username) {
        const chatMessageUsernameElement = chatMessageClone.querySelector(".chat-message-username")
        chatMessageUsernameElement.textContent = chatMessageObj.username;
        chatMessageUsernameElement.classList.add((chatMessageObj.username === "OP") ? "text-warning" : "text-info");
    }

    chatBox.appendChild(chatMessageClone);
}

const chatSubscriptionCallback = (chatMessageAsJSON) => {
    addMessageToChatBox(JSON.parse(chatMessageAsJSON.body));
}

const websocketConnectionCallback = (frame) => {
        if (stompClient != null) {
            stompClient.subscribe(
                websocketEndpoints.chatSubscription,
                (chatMessageAsJSON) => chatSubscriptionCallback(chatMessageAsJSON)
            );
        }
}

const connectToWebsocket = () => {
    const socket = new SockJS(websocketEndpoints.registration);
    stompClient = webstomp.over(socket);
    stompClient.connect(
        {},
        (frame) => websocketConnectionCallback(frame)
    );
};

const sendMessage = () => {
    // Handle empty message
    const message = messageTextarea.value.trim();
    if (message === "") return;

    if (stompClient != null) {
        stompClient.send(
            websocketEndpoints.chatSending,
            JSON.stringify({
                username: username,
                message: message
            })
        );
    }
}

const eraseTextArea = () => {
    messageTextarea.value = '';
    resizeTextAreaToFitContent();
}

const sendMessageOnClick = (event) => {
    event.preventDefault();
    sendMessage();
    eraseTextArea();
}

const sendMessageOnEnter = (event) => {
    if (event.key === "Enter" && !event.shiftKey) {
        event.preventDefault();
        sendMessage();
        eraseTextArea();
    }
}

const convertMessagesDateTimeInLocalTime = () => {
    const chatMessageTimeElements = document.getElementsByClassName("chat-message-time");
    for (let chatMessageTimeElement of chatMessageTimeElements) {
        chatMessageTimeElement.textContent = convertDateTimeInLocalTime(chatMessageTimeElement.textContent);
    }
}

/* Code execution */
copyChatURLButton.addEventListener("click", copyChatURLToClipboard);
messageTextarea.addEventListener("input", resizeTextAreaToFitContent)
messageTextarea.addEventListener("keydown", sendMessageOnEnter);
sendMessageButton.addEventListener("click", sendMessageOnClick);
convertMessagesDateTimeInLocalTime();
connectToWebsocket();
