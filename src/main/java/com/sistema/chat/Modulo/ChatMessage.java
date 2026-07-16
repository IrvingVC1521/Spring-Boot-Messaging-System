package com.sistema.chat.Modulo;

import com.sistema.chat.Enums.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder //Es para crear los objetos de una manera sencilla y clara
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender; //remitente

}

//Ejemplo para crear una instancia sin meter a @Builder:
//ChatMessage message = new ChatMessage(MessageType.CHAT, "Hola", "Irvin");

//con @Builder:
/*
* ChatMessage message = ChatMessage.builder()
        .type(MessageType.CHAT)
        .content("Hola")
        .sender("Irvin")
        .build(); // <- ¡Cierra y fabrica el objeto!
*/