package server;

import common.Constants;

public class MainServer {
    public static void main(String[] args) {
        new StreamingServer(Constants.PORT).startServer();
    }
}
