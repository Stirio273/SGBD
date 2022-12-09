package socket;

import bddrelationnel.JavaSQL;
import bddrelationnel.Database;
import bddrelationnel.Table;
import java.io.*;
import java.net.*;

public class SocketClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println(
                    "Usage: java SocketClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket kkSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                ObjectInputStream dataForMe = new ObjectInputStream(kkSocket.getInputStream());) {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            Object fromServer;
            String fromUser;

            while ((fromServer = dataForMe.readObject()) != null) {

                if (fromServer instanceof Table) {
                    ((Table) fromServer).showTable();
                } else if (fromServer instanceof JavaSQL) {
                    ((JavaSQL) fromServer).showDatabases();
                } else if (fromServer instanceof Database) {
                    ((Database) fromServer).showListTable();
                } else if (!fromServer.toString().equalsIgnoreCase("JavaSQL>")) {
                    System.out.println(fromServer.toString() + "\n");
                }
                System.out.print("JavaSQL>");
                fromUser = stdIn.readLine();
                if (fromUser.equalsIgnoreCase("exit")) {
                    break;
                }
                out.println(fromUser);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
