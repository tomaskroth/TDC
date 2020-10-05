package com.tk.tdc.client;

import com.tk.tdc.client.domain.Message;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Scanner;

@Component
public class ChatCommandLine implements CommandLineRunner {

    private static final String MESSAGE = "%s (%s): %s";

    private static final String URL = "http://localhost:8080";
    private static final String GET_PATH = "/chat/room/%s/stream";
    private static final String POST_PATH = "/chat";

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Type the room name:");
        String room = scanner.nextLine();
        System.out.println("Type your name:");
        String author = scanner.nextLine();

        sendGreeting(room, author);
        subscribeToChat(room);

        while (scanner.hasNextLine()) {
            String content = scanner.nextLine();

            Message message = new Message();
            message.setAuthor(author);
            message.setRoom(room);
            message.setContent(content);

            sendMessage(message);
        }

    }

    private void sendGreeting(String room, String author) {
        Message message = new Message();
        message.setAuthor(author);
        message.setRoom(room);
        message.setContent("Joined the room");

        sendMessage(message);
    }

    private void subscribeToChat(String room) {
        Flux<Message> chatMessages = getChatMessages(room);
        chatMessages.subscribe(message ->
                System.out.printf((MESSAGE) + "%n", message.getAuthor(), message.getTime(), message.getContent())
        );
    }

    private void sendMessage(Message message) {
        WebClient.create(URL)
                 .post()
                 .uri(POST_PATH)
                 .contentType(MediaType.APPLICATION_JSON)
                 .bodyValue(message)
                 .retrieve()
                 .bodyToMono(Message.class)
                 .doOnError(error ->
                         System.out.println("Failed to send message: " + error)
                 )
                 .subscribe();
    }

    private Flux<Message> getChatMessages(String room) {
        return WebClient.builder()
                        .baseUrl(URL)
                        .build()
                        .get()
                        .uri(String.format(GET_PATH, room))
                        .accept(MediaType.APPLICATION_STREAM_JSON)
                        .retrieve()
                        .bodyToFlux(Message.class);
    }
}
