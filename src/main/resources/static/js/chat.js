/* Variables */
const copyChatURLButton = document.getElementById("copy-chat-url-btn")
const messageTextarea = document.getElementById("message-textarea");
const sendMessageButton = document.getElementById("send-message-button");
const nbActiveUsersElement = document.getElementById("nb-chat-users");
const chatBox = document.getElementById("chat-box");
const chatUsersListColumnElement = document.getElementById("users-list-column");
const chatMessageSentTemplate = document.getElementById("chat-message-sent-template");
const chatMessageReceivedTemplate = document.getElementById("chat-message-received-template");
const chatUserTemplate = document.getElementById("chat-user-template");

let isCopyChatURLButtonClickable = true;
let stompClient = null;

const websocketEndpoints = {
    registration: "/register-websocket",
    redirectSubscription: "/user/topic/redirect",
    chatSubscription: `/topic/chat/${UUID}/messages`,
    chatSending: `/app/chat/${UUID}/messages`,
    usersSubscription: `/topic/chat/${UUID}/users`
};

/* Functions */
const copyChatURLToClipboard = () => {
    navigator.clipboard.writeText(window.location.href)
        .then(() => {
            if (isCopyChatURLButtonClickable) {
                isCopyChatURLButtonClickable = false;
                const defaultContent = copyChatURLButton.innerHTML;
                copyChatURLButton.innerHTML = "Copied <i class=\"fas fa-check ms-1\"></i>";
                setTimeout(() => {
                    copyChatURLButton.innerHTML = defaultContent;
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

/* chat messages */
const addMessageToChatBox = (chatMessageObj) => {
    const chatMessageTemplate = (chatMessageObj.username === username) ? chatMessageSentTemplate : chatMessageReceivedTemplate;
    const chatMessageClone = chatMessageTemplate.content.cloneNode(true);

    chatMessageClone.querySelector(".chat-message").textContent = chatMessageObj.message;
    chatMessageClone.querySelector(".chat-message-time").textContent = convertDateTimeInLocalTime(chatMessageObj.sendingDateTime);

    const chatMessageUsernameElement = chatMessageClone.querySelector(".chat-message-username")
    chatMessageUsernameElement.textContent = chatMessageObj.username;
    chatMessageUsernameElement.classList.add((chatMessageObj.username === "OP") ? "text-warning" : "text-info");

    chatBox.appendChild(chatMessageClone);
}

const chatSubscriptionCallback = (chatMessageAsJSON) => {
    addMessageToChatBox(JSON.parse(chatMessageAsJSON.body));
}

/* redirect */
const handleRedirectAction = (redirectMessageObj) => {
    if (redirectMessageObj.action === "REDIRECT") {
        document.location.href = redirectMessageObj.location;
    }
}

const redirectSubscriptionCallback = (redirectMessageAsJSON) => {
    handleRedirectAction(JSON.parse(redirectMessageAsJSON.body));
}

/* chat users */
const updateChatUsersListColumn = (chatUsersList) => {
    chatUsersListColumnElement.innerHTML = "";

    let chatUserClone;
    let chatUserElement;

    chatUsersList.forEach((chatUserEntry) => {
        chatUserClone = chatUserTemplate.content.cloneNode(true);
        chatUserElement = chatUserClone.querySelector(".chat-user");
        chatUserElement.textContent = chatUserEntry.username;
        chatUserElement.classList.add('text-center');
        chatUserElement.classList.add((chatUserEntry.username === "OP") ? "text-warning" : "text-info");
        chatUsersListColumnElement.appendChild(chatUserClone);
    });

    nbActiveUsersElement.textContent = chatUsersList.length;
}

const updateChatUsersLists = (chatUsersList) => {
    updateChatUsersListColumn(chatUsersList);
}

const chatUsersSubscriptionCallback = (chatUsersListAsJSON) => {
    updateChatUsersLists(JSON.parse(chatUsersListAsJSON.body));
}

/* WebSocket connection */
const websocketConnectionCallback = (frame) => {
        if (stompClient != null) {
            stompClient.subscribe(
                websocketEndpoints.redirectSubscription,
                (redirectMessageAsJSON) => redirectSubscriptionCallback(redirectMessageAsJSON)
            )

            stompClient.subscribe(
                websocketEndpoints.usersSubscription,
                (chatUsersListAsJSON) => chatUsersSubscriptionCallback(chatUsersListAsJSON)
            );

            stompClient.subscribe(
                websocketEndpoints.chatSubscription,
                (chatMessageAsJSON) => chatSubscriptionCallback(chatMessageAsJSON),
                { username: username }
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

/* Code execution */
copyChatURLButton.addEventListener("click", copyChatURLToClipboard);
messageTextarea.addEventListener("input", resizeTextAreaToFitContent)
messageTextarea.addEventListener("keydown", sendMessageOnEnter);
sendMessageButton.addEventListener("click", sendMessageOnClick);
convertMessagesDateTimeInLocalTime();
connectToWebsocket();
