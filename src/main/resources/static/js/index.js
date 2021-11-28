/* Variables */
const createChatForm = document.getElementById("create-chat-form");
const chatNameInput = document.getElementById("chat-name");
const chatCardsContainer = document.getElementById("chat-cards-container");
const chatCardTemplate = document.getElementById("chat-card-template");
const noChatCardText = document.getElementById("no-chat-card-text");

let stompClient = null;

const websocketEndpoints = {
    registration: "/register-websocket",
    perUserChatsSubscription: `/user/topic/chats`,
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

const addChatCardsToChatCardsContainer = (chatDataDtoList) => {
    if (chatDataDtoList.length === 0) {
        return;
    }

    if (chatCardsContainer.childElementCount === 0) {
        noChatCardText.classList.add("d-none");
    }

    let chatCardClone;
    chatDataDtoList.forEach((chatDataDto) => {
        chatCardClone = chatCardTemplate.content.cloneNode(true);
        chatCardClone.querySelector(".chat-card").id = `chat-card-${chatDataDto.chat.id}`;
        chatCardClone.querySelector(".card-title").textContent = chatDataDto.chat.name;
        chatCardClone.querySelector(".nb-chat-users").textContent = chatDataDto.nbChatUsers;
        chatCardClone.querySelector(".card-chat-url").href = `/chat/${chatDataDto.chat.uuid}`;
        chatCardsContainer.appendChild(chatCardClone);
    });
}

const updateNbChatUsersInChatCards = (chatDataDtoList) => {
    if (chatDataDtoList.length === 0) {
        return;
    }

    let chatCardToUpdate;
    chatDataDtoList.forEach((chatDataDto) => {
        chatCardToUpdate = document.getElementById(`chat-card-${chatDataDto.chat.id}`);
        chatCardToUpdate.querySelector(".nb-chat-users").textContent = chatDataDto.nbChatUsers;
    });
}

const removeChatCardsFromChatCardsContainer = (chatDataDtoList) => {
    if (chatDataDtoList.length === 0) {
        return;
    }

    let chatCardToRemove;
    chatDataDtoList.forEach((chatDataDto) => {
        chatCardToRemove = document.getElementById(`chat-card-${chatDataDto.chat.id}`);
        chatCardToRemove.remove();
    });

    if (chatCardsContainer.childElementCount === 0) {
        noChatCardText.classList.remove("d-none");
    }
}

const handleChatCards = (chatsActionObj) => {
    switch (chatsActionObj.action) {
        case "CREATE":
            addChatCardsToChatCardsContainer(chatsActionObj.chatDataDtoList);
            break;
        case "UPDATE":
            updateNbChatUsersInChatCards(chatsActionObj.chatDataDtoList);
            break;
        case "DELETE":
            removeChatCardsFromChatCardsContainer(chatsActionObj.chatDataDtoList);
            break;
        default:
            break;
    }
}

const perUserChatsSubscriptionCallback = (chatsActionAsJSON) => {
    const chatsActionObj = JSON.parse(chatsActionAsJSON.body);

    switch (chatsActionObj.action) {
        case "CREATE":
            if (chatsActionObj.chatDataDtoList.length === 0) {
                noChatCardText.classList.remove("d-none");
                return;
            }
            addChatCardsToChatCardsContainer(chatsActionObj.chatDataDtoList);
            break;
        default:
            break;
    }
}

const chatsSubscriptionCallback = (chatsActionAsJSON) => {
    handleChatCards(JSON.parse(chatsActionAsJSON.body));
}

const websocketConnectionCallback = (frame) => {
    if (stompClient != null) {
        stompClient.subscribe(
            websocketEndpoints.perUserChatsSubscription,
            (chatsActionAsJSON) => perUserChatsSubscriptionCallback(chatsActionAsJSON)
        );

        stompClient.subscribe(
            websocketEndpoints.chatsSubscription,
            (chatsActionAsJSON) => chatsSubscriptionCallback(chatsActionAsJSON)
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