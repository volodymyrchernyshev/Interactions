package com.interactions;

import com.interactions.reader.LogReader;
import com.interactions.writer.OutFileWriter;
import com.interactions.writer.WriterWorker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class WriterMain {


    public static void main(String[] args) throws Exception {


        Properties props = new Properties();

        props.load(WriterMain.class.getClassLoader().getResourceAsStream("application.properties"));

        if ("writer".equals(args[0])) {

            runWriter(props);
        } else if ("reader".equals(args[0])) {
            runReader(props);
        } else {
            System.exit(1);
        }


    }

    private static void runWriter(Properties props) throws IOException {
        OutFileWriter outFileWriter = new OutFileWriter(props.getProperty("name"));


        ThreadGroup writersThreadGroup = new ThreadGroup("writers");
        for (Enumeration<Object> keys = props.keys(); keys.hasMoreElements(); ) {

            String key = String.valueOf(keys.nextElement());
            if (key.startsWith("writers")) {

                String[] writerSplit = key.split("\\.");
                int writersCount = Integer.parseInt(props.getProperty(key));

                AtomicInteger messageId = new AtomicInteger(1000);
                for (int i = 0; i < writersCount; i++) {

                    WriterWorker worker = new WriterWorker(writerSplit[1], messageId, outFileWriter, i);

                    Thread thread = new Thread(writersThreadGroup, worker);
                    thread.start();
                }
            }

        }


    }

    private static void runReader(Properties props) throws Exception {
        BufferedReader logFile = new BufferedReader(new FileReader(props.getProperty("name")));

        Map<String, LogReader> readers = prepareReaders(props);

        processLogFile(logFile, readers);
        return;


    }

    private static void processLogFile(BufferedReader logFile, Map<String, LogReader> readers) throws IOException, InterruptedException {
        while (true) {

            String line = logFile.readLine();
            if (line == null) {
                Thread.sleep(100);
                continue;
            }


            String[] lineSplit = line.split(":\\s", 3);
            String type = lineSplit[0];
            String lineId = lineSplit[1];
            String message = lineSplit[2];
            LogReader logReader = readers.get(type);
            if (logReader != null) {
                logReader.process(type, lineId, message);
            } else {
                System.out.println("Unknown reader type " + type);
            }

        }
    }

    private static Map<String, LogReader> prepareReaders(Properties props) {
        Map<String, LogReader> readers = new HashMap<String, LogReader>();
        for (Enumeration<Object> keys = props.keys(); keys.hasMoreElements(); ) {

            String key = String.valueOf(keys.nextElement());
            if (key.startsWith("writers")) {
                String[] writerSplit = key.split("\\.");
                readers.put(writerSplit[1], new LogReader(writerSplit[1]));
            }

        }
        return readers;
    }

}
