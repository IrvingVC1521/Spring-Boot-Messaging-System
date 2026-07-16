
    let stompClient;
    let username;

    // 1. Conectar al servidor WebSocket
    function connect() {
        console.log("Se entró al metodo: connect()")
    username = document.querySelector('#name').value.trim();
    if (username) {
    document.querySelector('#login-page').style.display = 'none';
    document.querySelector('#chat-page').style.display = 'flex';

    // Aquí tocamos la "puerta de entrada" que configuraste en tu backend
    let socket = new SockJS('http://localhost:8080/ws');
    stompClient = Stomp.over(socket);

    // Ocultar los logs de depuración del cliente STOMP en la consola web
    stompClient.debug = null;

    stompClient.connect({}, onConnected, onError);
}
}

    // 2. Qué hacer cuando la conexión es exitosa
    function onConnected() {
    // Nos suscribimos al pasillo público
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Avisamos al backend que nos acabamos de unir (enviamos el nombre para la nota adhesiva)
    stompClient.send("/app/addUser",
{},
    JSON.stringify({sender: username, type: 'JOIN'})
    );
}

    function onError(error) {
    alert('No se pudo conectar al servidor WebSocket. ¡Asegúrate de que tu backend esté corriendo!');
}

    // 3. Enviar un mensaje de texto normal
    function sendMessage() {
    let messageInput = document.querySelector('#message');
    let messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
    let chatMessage = {
    sender: username,
    content: messageContent,
    type: 'CHAT'
};

    // Enviamos el mensaje al pasillo de revisión de Java
    stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage));
    messageInput.value = '';
}
}

    // Permite enviar mensaje presionando la tecla "Enter"
    function handleEnter(event) {
    if (event.key === "Enter") {
    sendMessage();
}
}

    // 4. Qué hacer cuando el Bróker nos lanza un mensaje
    function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    let messageContainer = document.querySelector('#message-container');
    let messageElement = document.createElement('div');

    if (message.type === 'JOIN') {
    messageElement.classList.add('message', 'join');
    messageElement.innerText = message.sender + ' se ha unido al chat.';
} else if (message.type === 'LEAVE') {
    messageElement.classList.add('message', 'leave');
    messageElement.innerText = message.sender + ' ha abandonado el chat.';
} else {
    messageElement.classList.add('message', 'chat');
    // Diferenciar mis mensajes de los de otros
    if (message.sender === username) {
    messageElement.classList.add('my-message');
}

    let senderElement = document.createElement('span');
    senderElement.classList.add('sender');
    senderElement.innerText = message.sender;

    let textElement = document.createElement('span');
    textElement.innerText = message.content;

    messageElement.appendChild(senderElement);
    messageElement.appendChild(textElement);
}

    messageContainer.appendChild(messageElement);
    // Hacer scroll automático hacia abajo
    messageContainer.scrollTop = messageContainer.scrollHeight;
}
