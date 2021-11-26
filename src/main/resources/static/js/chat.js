/* Variables */
const copyChatURLButton = document.getElementById("copy-chat-url-btn")
const messageTextarea = document.getElementById("message-textarea");

let isCopyChatURLButtonClickable = true;

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

/* Code execution */
copyChatURLButton.addEventListener("click", copyChatURLToClipboard);
messageTextarea.addEventListener('input', resizeTextAreaToFitContent)