package server;

public class MainForHttpSeverSocket {

    public static void main(String[] args) {
        HttpServerSocket httpServerSocket = new HttpServerSocket();
        httpServerSocket.start(8080);
    }

}
