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
    private String room;

}
