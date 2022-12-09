package sub;
import java.net.Socket;
import java.io.*;
import Request.*;

public class Client {

    Socket socket ;
    String nomClient = "Client" ;
    public Client()
    {

    }
    public Client(String nom)
    {
        this.nomClient =  nom;
    }

    public void setUpClient(String server,int port) throws IOException, ClassNotFoundException
    {

        System.out.println("CLIENT --");
        this.socket = new Socket(server,port);
        System.out.println("Connected to the port :"+port);
        ObjectOutputStream dataOut = new ObjectOutputStream(this.getSocket().getOutputStream());
        while(true)
        {
            try {
                String msg = "";
                System.out.print(this.getNomClient()+" :");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                msg = br.readLine();
                System.out.println();
                dataOut.writeObject(msg);
                dataOut.flush();
                ObjectInputStream dataIn = new ObjectInputStream(this.getSocket().getInputStream());
                Object resultRest = dataIn.readObject();
                System.out.println("\nResponse : ");
                if(resultRest instanceof Relation )
                {
                    Relation relation  = (Relation) resultRest;
                    relation.showAllData();
                }
                if(resultRest instanceof String)
                {
                    System.out.println( resultRest);
                }
               
            } catch (Exception e) {

                System.out.println(e);
            }
           
        }
    }

    public Socket getSocket()
    {
        return this.socket;
    }
    public void setNomClient(String nom)
    {
        this.nomClient = nom ;
    }

    public String getNomClient()
    {
        return this.nomClient;
    }

    

}