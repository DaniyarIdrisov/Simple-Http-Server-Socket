package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class HttpServerSocket {

    private static final String DIR = "src/main/resources/";

    public void start(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server starting...");
            while (true) {
                Socket client = server.accept();
                BufferedReader fromWebsite = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String response = fromWebsite.readLine();

                StringTokenizer tokenizer = new StringTokenizer(response);
                String httpMethod = tokenizer.nextToken();
                String httpQuery = tokenizer.nextToken();
                StringBuilder stringBuilder = new StringBuilder(httpQuery);
                stringBuilder.deleteCharAt(0);
                String fileName = stringBuilder.toString();

                if (httpQuery.equals("/favicon.ico")) {
                    continue;
                }

                System.out.println("---------------");
                System.out.println(httpMethod);
                System.out.println(httpQuery);
                System.out.println(fileName);
                System.out.println("---------------");

                while (true) {
                    System.out.println(response);
                    response = fromWebsite.readLine();
                    if (response == null || response.trim().length() == 0) {
                        break;
                    }
                }
                System.out.println("---------------");

                PrintWriter toWebsite = new PrintWriter(client.getOutputStream());
                toWebsite.println();
                if (httpMethod.equals("GET")) {
                    fileName = findFile(fileName);
                    System.out.println(fileName);
                    if (fileName != null) {
                        System.out.println("---------------");
                        System.out.println("HTTP/1.1 200 OK");
                        toWebsite.println("HTTP/1.1 200 OK");
                        toWebsite.println("Content-Type: text/html");
                        toWebsite.println();
                        File file = new File(DIR + fileName);
                        sendFile(file, toWebsite);
                        toWebsite.flush();
                        client.close();
                    } else {
                        System.out.println("---------------");
                        System.out.println("HTTP/1.1 404 Not Found");
                        toWebsite.println("HTTP/1.1 404 Not Found");
                        toWebsite.println();
                        toWebsite.println("404 Not Found");
                        toWebsite.flush();
                        client.close();
                    }
                } else {
                    System.out.println("---------------");
                    System.out.println("HTTP/1.1 500 Internal Server Error");
                    toWebsite.println("HTTP/1.1 500 Internal Server Error");
                    toWebsite.println();
                    toWebsite.println("500 Internal Server Error");
                    toWebsite.flush();
                    client.close();
                }
            }
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    private String findFile(String fileName) {
        System.out.println("Directory search...");
        File file = new File(DIR);
        boolean flag = false;
        File[] list = file.listFiles();
        String s = null;
        if (list != null) {
            for (File f : list) {
                String[] name = f.getName().split("\\.");
                if (fileName.equalsIgnoreCase(name[0])) {
                    System.out.println("---------------");
                    System.out.println("File exists!");
                    s = name[1];
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            System.out.println("---------------");
            System.out.println();
            System.out.println("File not exists!");
            return null;
        }
        fileName = fileName + "." + s;
        return fileName;
    }

    private void sendFile(File file, PrintWriter printWriter) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String string = stringBuilder.toString();
        System.out.println(string);
        printWriter.println(string);
        bufferedReader.close();
    }

}
