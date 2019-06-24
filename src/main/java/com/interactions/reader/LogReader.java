package com.interactions.reader;

public class LogReader {

    private final String readerType;

    public LogReader(String readerType) {
        this.readerType = readerType;
    }


    public void process(String type, String lineId, String message) {
        System.out.println("TeaderType " + readerType +" is processing message for type " + type
                + ", messageId is " + lineId + ", and message is " + message);
    }
}
