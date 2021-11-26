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
    chatMessagesSubscription: `/topic/chat/${UUID}`,
    chatMessagesSending: `/app/chat/${UUID}`
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

const addMessageToChatBox = (chatMessageObj) => {
    console.log(`chatMessageObj: ${chatMessageObj.message}`);

    const chatMessageTemplate = (chatMessageObj.username === username) ? chatMessageSentTemplate : chatMessageReceivedTemplate;
    const chatMessageClone = chatMessageTemplate.content.cloneNode(true);

    const localDate = new Date(chatMessageObj.sendingDateTime);
    const hours = (localDate.getHours() < 10) ? `0${localDate.getHours()}` : localDate.getHours();
    const minutes = (localDate.getMinutes() < 10) ? `0${localDate.getMinutes()}` : localDate.getMinutes();
    chatMessageClone.querySelector(".chat-message").textContent = chatMessageObj.message;
    chatMessageClone.querySelector(".chat-message-time").textContent = `${hours}:${minutes}`;

    if (chatMessageObj.username !== username) {
        const chatMessageUsernameElement = chatMessageClone.querySelector(".chat-message-username")
        chatMessageUsernameElement.textContent = chatMessageObj.username;
        chatMessageUsernameElement.classList.add((chatMessageObj.username === "OP") ? "text-warning" : "text-info");
    }

    chatBox.appendChild(chatMessageClone);
}

const chatMessagesSubscriptionCallback = (chatMessageAsJSON) => {
    addMessageToChatBox(JSON.parse(chatMessageAsJSON.body));
}

const websocketConnectionCallback = (frame) => {
        if (stompClient != null) {
            stompClient.subscribe(
                websocketEndpoints.chatMessagesSubscription,
                (chatMessageAsJSON) => chatMessagesSubscriptionCallback(chatMessageAsJSON)
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
            websocketEndpoints.chatMessagesSending,
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

/* Code execution */
connectToWebsocket();
copyChatURLButton.addEventListener("click", copyChatURLToClipboard);
messageTextarea.addEventListener("input", resizeTextAreaToFitContent)
messageTextarea.addEventListener("keydown", sendMessageOnEnter);
sendMessageButton.addEventListener("click", sendMessageOnClick);
