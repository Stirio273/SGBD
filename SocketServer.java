package socket;

import bddrelationnel.JavaSQL;
import bddrelationnel.Requete;
import bddrelationnel.Table;
import execution.ThreadServer;
import java.net.*;
import java.io.*;

public class SocketServer {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java SocketServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(portNumber);) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ThreadServer(clientSocket).start();
            }
        } catch (Exception e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
