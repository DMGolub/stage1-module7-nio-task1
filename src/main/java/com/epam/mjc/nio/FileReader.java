package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FileReader {

    public Profile getDataFromFile(File file) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        String name = null;
        int age = 0;
        String email = null;
        long phone = 0L;
        try (
                RandomAccessFile reader = new RandomAccessFile(file, "r");
                FileChannel channel = reader.getChannel()
        ) {
            StringBuilder content = new StringBuilder();
            int bytesRead = channel.read(buffer);
            while (bytesRead != -1) {
                buffer.flip();      // switch from write to read mode
                while (buffer.hasRemaining()) {
                    content.append((char) buffer.get());
                }
                buffer.clear();
                bytesRead = channel.read(buffer);
            }
            String[] lines = content.toString().split("\n");
            for (String line : lines) {
                String[] keyValue = line.split(": ");
                switch (keyValue[0]) {
                    case "Name":
                        name = keyValue[1];
                        break;
                    case "Age":
                        age = Integer.parseInt(keyValue[1]);
                        break;
                    case "Email":
                        email = keyValue[1];
                        break;
                    case "Phone":
                        phone = Long.parseLong(keyValue[1]);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            // log error (temp)
        }
        return new Profile(name, age, email, phone);
    }
}