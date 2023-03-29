package com.example.instagram.Model;

public class Chats {
    private String sender;
    private String receiver;
    private String message;

    public Chats() {
    }

    public Chats(String sender, String receiver, String chatmessage) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = chatmessage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
