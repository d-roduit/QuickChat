/* Variables */
const copyChatURLButton = document.getElementById("copy-chat-url-btn")
const messageTextarea = document.getElementById("message-textarea");
const sendMessageButton = document.getElementById("send-message-button");
const chatBox = document.getElementById("chat-box");

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

const addMessageToChatBox = (chatMessage) => {

}

const chatMessagesSubscriptionCallback = (chatMessageAsJSON) => {
    console.log(`chatMessage: ${chatMessageAsJSON}`)
    addMessageToChatBox(JSON.parse(chatMessageAsJSON.body));
}

const websocketConnectionCallback = (frame) => {
        console.log(`Connected: ${frame}`);
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

const sendMessage = (event) => {
    event.preventDefault();

    if (stompClient != null) {
        stompClient.send(
            websocketEndpoints.chatMessagesSending,
            JSON.stringify({
                username: username,
                message: messageTextarea.value
            })
        );
    }
}

/* Code execution */
connectToWebsocket();
copyChatURLButton.addEventListener("click", copyChatURLToClipboard);
messageTextarea.addEventListener("input", resizeTextAreaToFitContent)
sendMessageButton.addEventListener("click", sendMessage);