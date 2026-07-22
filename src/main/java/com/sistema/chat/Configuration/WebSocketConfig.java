package com.sistema.chat.Configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration//Contenedor de Spring sobre como preparar los componentes  del sistema
@EnableWebSocketMessageBroker//Esto sirve para gestionar salas virtuales, el broker se encarga de
//que si una persona manda un mensaje, este mensaje llegará  a las demás personas de la sala

public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

   @Override
   public void registerStompEndpoints(StompEndpointRegistry registry) {//STOMP es el protocolo de mensajeria
        registry.addEndpoint("/ws").setAllowedOrigins(
                "http://localhost:3000","http://localhost:8080"
        ).withSockJS();//ws define la URL, por ejemplo: ws://localhost:8080/ws
        //withSockJS permite que si se usa un navegador viejo para usar el chat, si este no soporta
        //webSocket, se implemente mediante peticiones HTTP para mantener el chat
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
       registry.setApplicationDestinationPrefixes("/app");//con /app lo que hacemos es mandar el  mensaje al programa
        //para que lo pueda procesar, por ejemplo, hacia un metodo que detecta groserias
       registry.enableSimpleBroker("/topic" );//Con esto hacemos que todos los usuaris que
        //esten en la misma sala sean capaces de recibir un mismo mensaje de alguno de ellos
    }





}
