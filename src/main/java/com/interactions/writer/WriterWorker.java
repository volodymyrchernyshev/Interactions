package com.interactions.writer;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class WriterWorker implements Runnable{

    private final String workerType;
    private final AtomicInteger messageId;
    private final OutFileWriter writerOut;
    private final int index;
    private final Random random;


    public WriterWorker(String workerType, AtomicInteger messageId, OutFileWriter writerOut, int index) {
        this.workerType = workerType;
        this.messageId = messageId;
        this.writerOut = writerOut;
        this.random = new Random();
        this.index = index;
    }

    public void run() {

        while(true){


            try {
                Thread.sleep(random.nextInt(2000));

                writerOut.write(workerType +": " + messageId.incrementAndGet() +": " + "I was written by writer " + workerType + "" + index);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }
}
