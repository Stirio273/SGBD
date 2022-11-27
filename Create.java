package bddrelationnel;

import donnees.Fichier;

public class Create extends KeyWord {
    String nom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Create() {
        super("create");
    }

    public void execute(String[] req, JavaSQL appli) throws Exception{
        switch(req[1]){
            case "database":
                String nomDatabase = req[2];
                Fichier file = new Fichier("C:/Users/ONEF/Documents/Java Projects/BDD", nomDatabase + ".txt");
                file.createNewFile();
                Database bdd = new Database(nomDatabase, file);
                appli.getBaseDeDonnees().add(bdd);
                break;

            case "table":
                String nomTable = req[2];
                
                break;

            default:
                break;
        }
    }
}
