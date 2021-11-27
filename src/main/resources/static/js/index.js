/* Variables */
const createChatForm = document.getElementById("create-chat-form");
const chatNameInput = document.getElementById("chat-name");
const chatCardsContainer = document.getElementById("chat-cards-container");
const chatCardTemplate = document.getElementById("chat-card-template");
const noChatCardText = document.getElementById("no-chat-card-text");

let stompClient = null;

const websocketEndpoints = {
    registration: "/register-websocket",
    chatsSubscription: `/topic/chats`
};

/* Functions */
const handleCreateChatFormSubmit = (event) => {
    // Assure that the attribute was not removed / modified
    if (chatNameInput.getAttribute("maxlength") !== "60") {
        chatNameInput.setAttribute("maxlength", "60");
    }

    chatNameInput.value = chatNameInput.value.trim();

    // Assure to always submit 60 chars and to alert user otherwise
    if (chatNameInput.value.length > 60 || chatNameInput.value === "") {
        event.preventDefault();
        createChatForm.reportValidity();
    }
}

const addChatCardToChatCardsContainer = (chatObj) => {
    if (chatCardsContainer.childElementCount === 0) {
        noChatCardText.remove();
    }

    const chatCardClone = chatCardTemplate.content.cloneNode(true);

    chatCardClone.querySelector(".card-title").textContent = chatObj.name;
    chatCardClone.querySelector(".card-chat-url").href = `/chat/${chatObj.uuid}`;

    chatCardsContainer.appendChild(chatCardClone);
}

const removeChatCardFromChatCardsContainer = (chatObj) => {

}

const handleChatCardToChatCardsContainer = (chatOperationObj) => {
    switch (chatOperationObj.action) {
        case "CREATE":
            addChatCardToChatCardsContainer(chatOperationObj.chat);
            break;
        case "DELETE":
            removeChatCardFromChatCardsContainer(chatOperationObj.chat);
            break;
        default:
            break;
    }
}

const chatsSubscriptionCallback = (chatOperationAsJSON) => {
    handleChatCardToChatCardsContainer(JSON.parse(chatOperationAsJSON.body));
}

const websocketConnectionCallback = (frame) => {
    if (stompClient != null) {
        stompClient.subscribe(
            websocketEndpoints.chatsSubscription,
            (chatOperationAsJSON) => chatsSubscriptionCallback(chatOperationAsJSON)
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
createChatForm.addEventListener('submit', handleCreateChatFormSubmit)
connectToWebsocket();