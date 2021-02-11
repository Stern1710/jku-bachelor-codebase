package server;

import contacts.Constants;

import java.util.Scanner;

public class ServerMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Server server = new Server(Constants.PORT);
        Thread serverThread = new Thread(server);

        System.out.println("Starting server");
        serverThread.start();
        //Wait for the user to enter something on the console that ends with a enter
        scanner.nextLine();

        System.out.println("Stopping server - Open client handlers might be still running");
        serverThread.interrupt();

    }
}
