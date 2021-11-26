/* Variables */
let stompClient = null;
let connectButton = connectButton = document.getElementById("connect");
let disconnectButton = disconnectButton = document.getElementById("disconnect");
let sendButton = sendButton = document.getElementById("send");
let conversation = conversation = document.getElementById("conversation");
let greetings = greetings = document.getElementById("greetings");
let nameInput = nameInput = document.getElementById("name");

/* Functions */
const setConnected = (connected) => {
    connectButton.disabled = connected;
    disconnectButton.disabled = !connected;
    if (connected) {
        conversation.removeAttribute("style");
    }
    else {
        conversation.style.display = 'none';
    }
    greetings.innerHTML = "";
};

const connect = () => {
    const socket = new SockJS('/register-websocket');
    stompClient = webstomp.over(socket);
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', (greeting) => {
            console.log(`greeting: ${greeting}`)
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
};

const disconnect = () => {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
};

const sendName = () => {
    if (stompClient != null) {
        stompClient.send("/app/chat", JSON.stringify({'name': nameInput.value}), {});
    }
};

const showGreeting = (message) => {
    const td = document.createElement("td");
    td.textContent = message;

    const tr = document.createElement("tr");
    tr.appendChild(td);

    greetings.appendChild(tr);
};

/* Code Execution */
for (let form of document.forms) {
    form.addEventListener('submit', (e) => e.preventDefault());
}
connectButton.addEventListener('click', connect);
disconnectButton.addEventListener('click', disconnect);
sendButton.addEventListener('click', sendName);
