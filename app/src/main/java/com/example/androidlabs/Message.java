package com.example.androidlabs;


public class Message {

    private String message;
    private boolean isSend;
    private long messageID;

    public Message(String message, boolean isSend) {
        this.message = message;
        this.isSend = isSend;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public long getMessageID(){
        return messageID;
    }

    public void setMessageID(long msgID){
        this.messageID = msgID;
    }
}