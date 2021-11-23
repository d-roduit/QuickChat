let stompClient = null;
let connectButton = null;
let disconnectButton = null;
let sendButton = null;
let conversation = null;
let greetings = null;
let nameInput = null;

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

document.addEventListener('DOMContentLoaded', () => {
    connectButton = document.getElementById("connect");
    disconnectButton = document.getElementById("disconnect");
    sendButton = document.getElementById("send");
    conversation = document.getElementById("conversation");
    greetings = document.getElementById("greetings");
    nameInput = document.getElementById("name");

    for (let form of document.forms) {
        form.addEventListener('submit', (e) => e.preventDefault());
    }
    connectButton.addEventListener('click', connect);
    disconnectButton.addEventListener('click', disconnect);
    sendButton.addEventListener('click', sendName);
}, false);