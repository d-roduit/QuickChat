/* Variables */
const createChatForm = document.getElementById("create-chat-form");
const chatNameInput = document.getElementById("chat-name");

/* Functions */
const handleCreateChatFormSubmit = (event) => {
    // Assures that the attribute was not removed / modified
    if (chatNameInput.getAttribute("maxlength") !== "60") {
        chatNameInput.setAttribute("maxlength", "60");
    }

    chatNameInput.value = chatNameInput.value.trim();

    // Assures to always submit 60 chars and to alert user otherwise
    if (chatNameInput.value.length > 60) {
        event.preventDefault();
        createChatForm.reportValidity();
    }
}

/* Code execution */
createChatForm.addEventListener('submit', handleCreateChatFormSubmit)