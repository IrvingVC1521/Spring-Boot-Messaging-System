package com.sistema.chat.Service;


import com.sistema.chat.Modulo.ChatMessage;
import java.util.List;

public class ChatMessageService {
    List<String> malasPalabras = List.of("PENDEJO", "PENDEJA", "CABRÓN", "CABRONA", "CULERO", "CULERA", "CHINGAR", "CHINGADA", "CHINGADO", "CHINGAS",
            "CHINGUEN", "PINCHE", "VERGA", "V3RGA", "VETE A LA VERGA", "A LA VERGA", "MAMÓN", "MAMONA", "PUTO", "PUTA", "HIJO DE PUTA", "HIJA DE PUTA", "PUTA MADRE",
            "PTM", "CHINGA TU MADRE", "CTM", "HIJO DE TU PUTA MADRE", "PUTIZA", "PUTAZO", "CULO", "OJETE", "OJETES", "MIERDA", "MIERDERO", "JOTO", "MARICÓN", "MARICON",
            "SARRA", "NACO", "GATA", "PERRA", "PENDEJADA", "CHINGADERA", "CULIADA", "PUÑETAS", "PUÑETÓN", "MAMADAS", "ME VALE VERGA", "VALES VERGA", "VALE VERGA", "VETE ALV",
            "ALV", "HDP", "HJDTPM");



    public ChatMessage EliminarMalasPalabras(ChatMessage chatMessage){
        String contenido = chatMessage.getContent();
        String[] palabras = contenido.toUpperCase().split(" ");
        for(int i = 0; i < palabras.length; i++){
            if(malasPalabras.contains(palabras[i])){
                int caracteres =  palabras[i].length();
                palabras[i] = "*".repeat(caracteres);
            }
        }
        contenido = String.join(" ",palabras).toLowerCase();
        chatMessage.setContent(contenido);
        return chatMessage;
    }
}
