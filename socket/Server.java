package sub;
import java.net.ServerSocket;
import java.net.Socket;

import Exception.BdbException;

import java.io.*;
import Request.*;
public class Server {

    ServerSocket serverSocket  ;
    Socket socket ;
    public Server()
    {

    }
    
    public void setUpServer(int port ) throws IOException, BdbException, ClassNotFoundException
    {
        System.out.println("SERVER --");
        System.out.println("waiting for a client to connect...");
        this.serverSocket = new ServerSocket(port);
        this.socket = this.serverSocket.accept();
        System.out.println("Client connected ");
        String msg = "";
        Relation relation = new Relation(); 
        ObjectInputStream dataIn = new ObjectInputStream(this.getSocket().getInputStream()); 
        while(true)
        {       
            ObjectOutputStream dataOut = new ObjectOutputStream(this.getSocket().getOutputStream());
            try 
            {  
                msg  = (String) dataIn.readObject();
                Requete req = new Requete(msg);
                relation = req.Traitement();
                if(msg.equals("stop")){ break; }
                System.out.println("\nClient : "+msg);
                System.out.print("Server sending");
                System.out.println();
                dataOut.writeObject(relation);
                dataOut.flush();
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Erreur Server -------------------");
                dataOut.writeObject(e.getMessage());
                dataOut.flush();
                System.out.println("---------------------------------------- ");
            }
           
        }
        socket.close();
    }

    public Socket getSocket()
    {
        return this.socket;
    }

    public ServerSocket getServerSocket()
    {
        return this.serverSocket;
    }
}