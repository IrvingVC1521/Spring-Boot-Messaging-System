package com.sistema.chat.Service;

import com.sistema.chat.Enums.MessageType;
import com.sistema.chat.Modulo.ChatMessage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ChatMessageServiceTest {
    ChatMessage message = ChatMessage.builder()
            .type(MessageType.CHAT)
            .content("hola puto")
            .sender("Irvin")
            .build();

    ChatMessageService chatMessageService = new ChatMessageService();
    @Test
    void testParaeliminarMalasPalabras() {
        message = chatMessageService.EliminarMalasPalabras(message);
        String contenido = message.getContent();
        assertEquals("hola ****", contenido);

    }
}