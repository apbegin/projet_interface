package com.ift2905.chat;

/**
 * @author greg
 * @since 6/21/13
 */
public class Chat {

    private String message;
    public String author;
    public String destinataire;
    public boolean isPrivate;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Chat() {
    }

    Chat(String message, String author, String destinataire) {
        this.message = message;
        this.author = author;
        this.destinataire=destinataire;
        isPrivate=false;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
