package com.sistema.chat.Service;


import com.sistema.chat.Modulo.ChatMessage;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Service
public class ChatMessageService {

    static final List<String> malasPalabras = List.of("PENDEJO", "PENDEJA", "CABRÓN","CABRON", "CABRONA", "CULERO", "CULERA", "CHINGAR", "CHINGADA", "CHINGADO", "CHINGAS",
            "CHINGUEN", "PINCHE", "VERGA", "V3RGA", "VETE A LA VERGA", "A LA VERGA","IMBECIL","MAMÓN", "MAMONA", "PUTO", "PUTA", "HIJO DE PUTA", "HIJA DE PUTA", "PUTA MADRE",
            "PTM", "CHINGA TU MADRE", "CTM", "HIJO DE TU PUTA MADRE", "PUTIZA", "PUTAZO", "CULO", "OJETE", "OJETES", "MIERDA", "MIERDERO", "JOTO", "MARICÓN", "MARICON",
            "SARRA", "NACO", "GATA", "PERRA", "PENDEJADA", "CHINGADERA", "CULIADA", "PUÑETAS", "PUÑETÓN", "MAMADAS", "ME VALE VERGA", "VALES VERGA", "VALE VERGA", "VETE ALV",
            "ALV", "HDP", "HJDTPM","PUTITO","GAY", "VICTOR","PENE","PNDJ","PNDJO");

    ConcurrentHashMap<String, Integer> salas = new ConcurrentHashMap<>();

    public boolean agregarSala(String nombreSala) {
        if (nombreSala != null && !nombreSala.trim().isEmpty()) {
            // Se usa putIfAbsent para verificar y agregar en una sola operación indivisible
            // Retorna true solo si la clave no existía
            return salas.putIfAbsent(nombreSala.trim(), 0) == null;
        }
        return false;
    }

    public void unirseASala(String sala) {
        if (sala != null) {
            //Con computeIfPresent nos aseguramos de que no haya conflictos al unirse a la sala
            //Esta hecho para mantener atomicidad, evita el problema de concurrencia entre hilos
            salas.computeIfPresent(sala, (nombreDeSala, contadorActual) -> contadorActual + 1);
        }
    }

    public Set<String> getSalas(){
        return salas.keySet();
    }



    public void salirDeSala(String sala) {
        if (sala != null) {
            // Disminuye el conteo de forma atómica. Si llega a 0 o menos, elimina la clave retornando null.
            salas.computeIfPresent(sala, (k, conteoActual) -> {
                int nuevoConteo = conteoActual - 1;
                return (nuevoConteo <= 0) ? null : nuevoConteo;
            });
        }
    }



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
