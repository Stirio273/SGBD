package socket;

import bddrelationnel.JavaSQL;
import bddrelationnel.Requete;
import bddrelationnel.Table;
import java.net.*;
import java.io.*;

public class SocketServer {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream dataForClient = new ObjectOutputStream(clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));) {

            String inputLine, outputLine;

            JavaSQL javaSql = new JavaSQL();
            javaSql.getAllDatabases();
            Requete r = new Requete(javaSql);
            outputLine = "JavaSQL>";
            dataForClient.writeObject(outputLine);
            dataForClient.flush();
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equalsIgnoreCase("exit"))
                    break;
                try {
                    Object resultat = r.executeQuery(inputLine);
                    System.out.println("Envoi");
                    if(resultat instanceof JavaSQL){
                        ((JavaSQL) resultat).showDatabases();
                    }
                    dataForClient.reset();
                    dataForClient.writeObject(resultat);
                    dataForClient.flush();
                    System.out.println("Execution terminee");
                    System.out.println(inputLine);
                } catch (Exception e) {
                    dataForClient.writeObject(e.getMessage());
                    dataForClient.flush();
                }
            }
            dataForClient.close();
        } catch (Exception e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
