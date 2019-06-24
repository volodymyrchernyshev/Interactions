package com.interactions.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.locks.ReentrantLock;

public class OutFileWriter {

    private final ReentrantLock lock = new ReentrantLock();
    private final PrintWriter writer;

    public OutFileWriter(String file) throws IOException {
        this.writer = new PrintWriter(new FileWriter(file));
    }

    public void write(String s) {

        lock.lock();
        try{
            writer.println(s);
            writer.flush();
        } finally {
            lock.unlock();
        }

    }


}
