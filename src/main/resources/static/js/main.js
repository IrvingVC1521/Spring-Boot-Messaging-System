'use strict';

let stompClient = null;
let username = null;
let currentRoom = null;
let isConnected = false;

// -------------------- Referencias al DOM --------------------
const screenName = document.querySelector('#screen-name');
const screenRooms = document.querySelector('#screen-rooms');
const screenChat = document.querySelector('#screen-chat');

const nameInputEl = document.querySelector('#name');
const roomSelectEl = document.querySelector('#roomSelect');
const newRoomInputEl = document.querySelector('#newRoomInput');
const roomTitleEl = document.querySelector('#roomTitle');
const messageAreaEl = document.querySelector('#messageArea');
const messageInputEl = document.querySelector('#message');

// -------------------- Utilidades de pantalla --------------------
function showScreen(screen) {
    [screenName, screenRooms, screenChat].forEach(s => s.classList.add('hidden'));
    screen.classList.remove('hidden');
}

// -------------------- Paso 1: nombre de usuario --------------------
function guardarNombre() {
    const nombre = nameInputEl.value.trim();
    if (!nombre) {
        alert('Escribe un nombre antes de continuar.');
        return;
    }
    username = nombre;
    showScreen(screenRooms);
    cargarSalas();
}

// -------------------- Cargar / crear salas --------------------
function cargarSalas() {
    fetch('/ObtenerSalas')
        .then(response => {
            if (!response.ok) {
                throw new Error('Respuesta no OK: ' + response.status);
            }
            return response.json();
        })
        .then(salas => {
            roomSelectEl.innerHTML = '<option value="">Selecciona una sala...</option>';
            salas.forEach(sala => {
                const option = document.createElement('option');
                option.value = sala;
                option.textContent = sala;
                roomSelectEl.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al cargar las salas:', error);
            alert('No se pudieron cargar las salas. ¿Está corriendo el backend?');
        });
}

function crearSala() {
    const nombreSala = newRoomInputEl.value.trim();
    if (!nombreSala) {
        alert('Escribe un nombre de sala.');
        return;
    }

    fetch(`/AgregarSala?nombreSala=${encodeURIComponent(nombreSala)}`, {
        method: 'POST'
    })
        .then(response => {
            if (response.ok) {
                newRoomInputEl.value = '';
                cargarSalas();
            } else {
                alert('Hubo un error al crear la sala o la sala ya existe.');
            }
        })
        .catch(error => {
            console.error('Error al crear la sala:', error);
            alert('No se pudo conectar con el servidor para crear la sala.');
        });
}

// -------------------- Paso 2: entrar a una sala --------------------
function entrarASalaSeleccionada() {
    const salaSeleccionada = roomSelectEl.value;
    if (!salaSeleccionada) {
        alert('Selecciona una sala primero.');
        return;
    }

    currentRoom = salaSeleccionada;
    roomTitleEl.textContent = 'Sala: ' + currentRoom;
    messageAreaEl.innerHTML = ''; // limpiar mensajes de una sesión anterior
    showScreen(screenChat);

    if (isConnected) {
        // Ya conectados por un socket previo: solo avisamos que entramos a la nueva sala
        enviarEntradaASala();
    } else {
        conectarWebSocket();
    }
}

function salirDeSala() {
    if (isConnected && stompClient) {
        // Desconectamos por completo: esto dispara SessionDisconnectEvent en el backend,
        // que ya se encarga de hacer salirDeSala() y de avisar el LEAVE a los demás.
        stompClient.disconnect();
    }
    isConnected = false;

    currentRoom = null;
    messageAreaEl.innerHTML = '';
    showScreen(screenRooms);
    cargarSalas();
}

// -------------------- WebSocket / STOMP --------------------
function conectarWebSocket() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onConnectionError);
}

function onConnected() {
    isConnected = true;
    stompClient.subscribe('/topic/public', onMessageReceived);
    enviarEntradaASala();
}

function onConnectionError() {
    alert('No se pudo conectar al servidor WebSocket. Verifica que el backend esté corriendo.');
}

function enviarEntradaASala() {
    const joinMessage = {
        sender: username,
        type: 'JOIN',
        room: currentRoom
    };
    stompClient.send('/app/entrarASala', {}, JSON.stringify(joinMessage));
}

function enviarMensaje() {
    const contenido = messageInputEl.value.trim();
    if (!contenido) {
        return;
    }
    if (!isConnected || !currentRoom) {
        alert('Debes unirte a una sala antes de enviar mensajes.');
        return;
    }

    const chatMessage = {
        sender: username,
        content: contenido,
        type: 'CHAT',
        room: currentRoom
    };

    stompClient.send('/app/sendMessage', {}, JSON.stringify(chatMessage));
    messageInputEl.value = '';
}

function onMessageReceived(payload) {
    const message = JSON.parse(payload.body);

    // Ignoramos mensajes que no pertenezcan a la sala actual
    if (message.room && message.room !== currentRoom) {
        return;
    }

    const messageElement = document.createElement('li');

    if (message.type === 'JOIN') {
        messageElement.classList.add('message', 'join');
        messageElement.textContent = message.sender + ' se ha unido al chat.';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('message', 'leave');
        messageElement.textContent = message.sender + ' ha abandonado el chat.';
    } else {
        messageElement.classList.add('message', 'chat');
        if (message.sender === username) {
            messageElement.classList.add('my-message');
        }

        const senderElement = document.createElement('span');
        senderElement.classList.add('sender');
        senderElement.textContent = message.sender;

        const textElement = document.createElement('span');
        textElement.textContent = message.content;

        messageElement.appendChild(senderElement);
        messageElement.appendChild(textElement);
    }

    messageAreaEl.appendChild(messageElement);
    messageAreaEl.scrollTop = messageAreaEl.scrollHeight;
}

// -------------------- Eventos --------------------
document.querySelector('#btn-guardar-nombre').addEventListener('click', guardarNombre);
nameInputEl.addEventListener('keypress', e => {
    if (e.key === 'Enter') guardarNombre();
});

document.querySelector('#btn-entrar-sala').addEventListener('click', entrarASalaSeleccionada);
document.querySelector('#btn-crear-sala').addEventListener('click', crearSala);
newRoomInputEl.addEventListener('keypress', e => {
    if (e.key === 'Enter') crearSala();
});

document.querySelector('#btn-enviar').addEventListener('click', enviarMensaje);
document.querySelector('#btn-salir').addEventListener('click', salirDeSala);
messageInputEl.addEventListener('keypress', e => {
    if (e.key === 'Enter') enviarMensaje();
});