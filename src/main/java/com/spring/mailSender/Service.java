package com.spring.mailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private JavaMailSender mailSender;
    @Resource(name = "messageTemplate")
    private SimpleMailMessage message;

    public boolean validate(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        reader.close();
        boolean valid = (stringBuilder.toString().contains("Subject:")) && (stringBuilder.toString().contains(""));
        return valid;
    }

    public static String subject(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        String line;
        while (!((line = reader.readLine()).contains(""))) {
            stringBuilder.append(line.replace("Subject:", ""));
            stringBuilder.append(ls);
        }
        reader.close();
        return stringBuilder.toString();
    }


    public static String text(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        String line;
        boolean isText = false;
        while (((line = reader.readLine()) != null)) {
            if ((line.contains("")) || isText) {
                stringBuilder.append(line.replace("", ""));
                stringBuilder.append(ls);
                isText = true;
            }
        }
        reader.close();
        return stringBuilder.toString();
    }

    public void sendFile() throws IOException {
        String path = "C:/fnp/letters/in";
        File folder = new File(path);
        File[] listOfFilePaths = folder.listFiles();
        for (int i = 0; i < listOfFilePaths.length; i++) {
            if ((listOfFilePaths[i].isFile()) & (validate(listOfFilePaths[i]))) {

                message.setSubject(subject(listOfFilePaths[i].getPath()));
                message.setText(text(listOfFilePaths[i].getPath()));
                mailSender.send(message);
                listOfFilePaths[i].renameTo(new File("C:fnp/letters/archive", listOfFilePaths[i].getName()));

            } else {
                listOfFilePaths[i].renameTo(new File("C:fnp/letters/error", listOfFilePaths[i].getName()));
            }
        }
    }
}