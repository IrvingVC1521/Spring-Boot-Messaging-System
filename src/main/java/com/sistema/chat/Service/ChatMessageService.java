package com.sistema.chat.Service;


import com.sistema.chat.Modulo.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {
    List<String> malasPalabras = List.of("PENDEJO", "PENDEJA", "CABRÓN","CABRON", "CABRONA", "CULERO", "CULERA", "CHINGAR", "CHINGADA", "CHINGADO", "CHINGAS",
            "CHINGUEN", "PINCHE", "VERGA", "V3RGA", "VETE A LA VERGA", "A LA VERGA","IMBECIL","MAMÓN", "MAMONA", "PUTO", "PUTA", "HIJO DE PUTA", "HIJA DE PUTA", "PUTA MADRE",
            "PTM", "CHINGA TU MADRE", "CTM", "HIJO DE TU PUTA MADRE", "PUTIZA", "PUTAZO", "CULO", "OJETE", "OJETES", "MIERDA", "MIERDERO", "JOTO", "MARICÓN", "MARICON",
            "SARRA", "NACO", "GATA", "PERRA", "PENDEJADA", "CHINGADERA", "CULIADA", "PUÑETAS", "PUÑETÓN", "MAMADAS", "ME VALE VERGA", "VALES VERGA", "VALE VERGA", "VETE ALV",
            "ALV", "HDP", "HJDTPM","PUTITO","GAY", "VICTOR");



    public ChatMessage EliminarMalasPalabras(ChatMessage chatMessage){
        String contenido = chatMessage.getContent();
        String[] palabras = contenido.toUpperCase().split(" ");
        int caracteres;
        for(int i = 0; i < palabras.length; i++){
            String palabraLmpia = palabras[i].replaceAll("[^a-zA-Z0-9]", "");
            if(malasPalabras.contains(palabraLmpia)) {
                 caracteres = palabraLmpia.length();
                 String censura = "*".repeat(caracteres);
                 palabras[i] = palabras[i].replace(palabraLmpia, censura);
            }
        }
        contenido = String.join(" ",palabras).toLowerCase();
        chatMessage.setContent(contenido);
        return chatMessage;
    }
}
