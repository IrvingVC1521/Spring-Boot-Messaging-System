package com.sistema.chat.Components;
import com.sistema.chat.Enums.MessageType;
import com.sistema.chat.Modulo.ChatMessage;
import com.sistema.chat.Service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component//Le avisa a Spring que cree esta clase de forma automática al arrancar
@Slf4j //Crea un log de forma automatica
@RequiredArgsConstructor
public class WebSocketEventListener {
        private final ChatMessageService chatMessageService;
        private final SimpMessageSendingOperations messagingTemplate;//Esto es un semaforo global
        //Manda mensajes de manera global, por ejemplo, cuando un usuario se desconecta  de la sala
        //Se ejecuta cuando alguien se conecta, pero en consola
        @EventListener
        public void handleWebSocketConnectListener(SessionConnectedEvent event){
            log.info("Received a new web socket connection");//cada vez que un usuario abra el chat, se imprimirá en consola
        }

        //Se ejecuta cuando alguien se desconecta de la sala
        @EventListener
        public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
            //Con esto obtenemos los metadatos del usuaruo que se salió, con wrap lo traducimos a un formato legible
            StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
            //SessionAttribute esto registra el nombre del usuario cuando este se conecta
            var attributes = headerAccesor.getSessionAttributes();
            if (attributes != null) {
                String username = (String) attributes.get("username");
                String salaVieja = (String) attributes.get("sala");

                if (salaVieja != null) {
                    chatMessageService.salirDeSala(salaVieja);
                }
                if (username != null) {
                    log.info("user disconnected: {}",  username);
                    //Fabricamos el mensaje de que el usuario se desconectó
                    var chatMessage = ChatMessage.builder()
                            .type(MessageType.LEAVE)
                            .sender(username)
                            .build();
                    //Con esto se avsia a todos en la sesión
                    messagingTemplate.convertAndSend("/topic/public", chatMessage);
                }
            }
        }


}
