package execution;

import bddrelationnel.JavaSQL;
import bddrelationnel.Requete;
import bddrelationnel.Table;
import java.net.*;
import java.io.*;

public class ThreadServer extends Thread {
    private Socket clientSocket;

    public ThreadServer() {
        super();
    }

    public ThreadServer(Socket clientSocket) {
        super();
        this.clientSocket = clientSocket;
    }

    public void run() {
        try (
                // PrintStream out = new PrintStream(clientSocket.getOutputStream(), true);
                ObjectOutputStream dataForClient = new ObjectOutputStream(this.clientSocket.getOutputStream());
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(this.clientSocket.getInputStream()));) {

            String inputLine, outputLine;

            // Initiate conversation with client
            // KnockKnockProtocol kkp = new KnockKnockProtocol();
            // outputLine = kkp.processInput(null);
            // out.println(outputLine);

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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
