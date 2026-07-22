package com.sistema.chat.Controllers;


import com.sistema.chat.Modulo.ChatMessage;
import com.sistema.chat.Service.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
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

    @PostMapping("/AgregarSala")
    public ResponseEntity<?> agregarSala(@RequestParam String nombreSala){
        if(chatMessageServicee.agregarSala(nombreSala)) {
            log.info("Sala agregada exitosamente");
            return new ResponseEntity<>("Se ha creado la sala",HttpStatus.CREATED);
        }
        else{
            log.warn("La sala ya existe");
            return new ResponseEntity<>("La sala ya existe",HttpStatus.CONFLICT);
        }

    }

    @GetMapping("/ObtenerSalas")
    public ResponseEntity<?> obtenerSalas(){
        return ResponseEntity.ok().body(chatMessageServicee.getSalas());
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
        log.info("Usuario agregado a la sesión: {}", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/entrarASala")
    @SendTo("/topic/public")
    public ChatMessage ObtenerSalaUsuario(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        //Obtenemos los atributos de la sesion
        var atributos = headerAccessor.getSessionAttributes();
        if (atributos != null) {
            String salaVieja = (String) atributos.get("sala");
            if (salaVieja != null) {
                chatMessageServicee.salirDeSala(salaVieja);
                log.info("Se salió de la sala antigua");

            }
            atributos.put("sala", chatMessage.getRoom());
            chatMessageServicee.unirseASala(chatMessage.getRoom());
        }

        return chatMessage;
    }

}
