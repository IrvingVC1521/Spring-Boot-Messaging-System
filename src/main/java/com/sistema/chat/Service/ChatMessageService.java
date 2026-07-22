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
            "ALV", "HDP", "HJDTPM","PUTITO","GAY", "VICTOR");

    ConcurrentHashMap<String, Integer> salas = new ConcurrentHashMap<>();
    public void agregarSala(String nombreSala) {
        if(!nombreSala.isEmpty() && !salas.containsKey(nombreSala)){
            salas.put(nombreSala,0);
        }
        else throw new IllegalArgumentException("El nombre de la sala no puede estar vacio");
    }

    public void unirseASala(String sala){
        if(salas.containsKey(sala)){
            salas.put(sala, salas.getOrDefault(sala, 0) + 1);
        }
        else throw new IllegalArgumentException("Sala no encontrada");
    }

    public Set<String> getSalas(){
        return salas.keySet();
    }



    public void salirDeSala(String sala){
        if (salas.containsKey(sala)) {
            int conteo = salas.get(sala) - 1;
            if (conteo <= 0) {
                salas.remove(sala);
            } else {
                salas.put(sala, conteo);
            }
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
