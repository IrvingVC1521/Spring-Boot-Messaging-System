package com.sistema.chat.Controllers;


import com.sistema.chat.Modulo.ChatMessage;
import com.sistema.chat.Service.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;
@Controller
public class ChatController {

    private final ChatMessageService  chatMessageServicee;

    public ChatController(ChatMessageService chatMessageServicee) {
        this.chatMessageServicee = chatMessageServicee;
    }

    @MessageMapping("/sendMessage")
    //con esto enviamos el mensaje a esa sesión
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        return chatMessageServicee.EliminarMalasPalabras(chatMessage);
    }

    @MessageMapping("/addUser")
    @SendTo("/topic/public")
    //Recordamos que SimpMessage guarda los metadatos del usuario
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){
        //Payload le dice a java que convierta el JSON a la clase ChatMessage
        //Add username in web socket session

        //Con esto guardamos los datos del usuario, el .put agrega el usuario a un map, "username" es la key
        //y  lo otro es el valor
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username",chatMessage.getSender());
        return chatMessage;
    }
}
