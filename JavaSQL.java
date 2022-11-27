package bddrelationnel;

import donnees.Fichier;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.reflect.*;

public class JavaSQL {
    List<Database> baseDeDonnees = new ArrayList<Database>();

    public List<Database> getBaseDeDonnees() {
        return baseDeDonnees;
    }

    public void setBaseDeDonnees(List<Database> baseDeDonnees) {
        this.baseDeDonnees = baseDeDonnees;
    }

    public void start() {
        this.getAllDatabases();
        // Fichier file = new Fichier("C:/Users/ONEF/Documents/Java Projects/BDD/division.txt");
        // Database bdd = new Database("MyBDD", file);
        // bdd.dataToTable();
        // this.getBaseDeDonnees().add(bdd);
        Requete r = new Requete(this);
        try {
            while (true) {
                Scanner entree = new Scanner(System.in);
                System.out.print("JavaSQL>");
                String statement = entree.nextLine();
                System.out.println();
                if (statement.equals("exit"))
                    break;
                Table t = r.executeQuery(statement);
            }
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void showDatabases(){
        System.out.println("+--------------------+");
        System.out.println("| Database           |");
        System.out.println("+--------------------+");
        for(int i = 0; i < this.baseDeDonnees.size(); i++){
            System.out.println("| " + this.baseDeDonnees.get(i).getNom() + espaceRestant(this.baseDeDonnees.get(i).getNom()) + "|");
        }
        System.out.println("+--------------------+\n");
    }

    public void getAllDatabases(){
        Fichier repertory = new Fichier("C:/Users/ONEF/Documents/Java Projects/BDD/Databases");
        File[] files = repertory.listFiles();
        for(int i = 0; i < files.length; i++){
            System.out.println(files[i].getPath());
            Database bdd = new Database(files[i].getName().replaceAll(".txt", ""), new Fichier(files[i].getPath()));
            bdd.dataToTable();
            this.getBaseDeDonnees().add(bdd);
        }
    }

    public String espaceRestant(String nom){
        String e = "";
        for(int i = 0; i < (20-(nom.length()+1)); i++){
            e = e.concat(" ");
        }
        return e;
    }
}