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

    public void execute(String re, Requete query) throws Exception {
        try{
            switch (re.split(" ")[1]) {
                case "database":
                    String[] req = re.split(" ");
                    String nomDatabase = req[2];
                    Fichier file = new Fichier("./Databases", nomDatabase + ".txt");
                    file.createNewFile();
                    Database bdd = new Database(nomDatabase, file);
                    query.getAppli().getBaseDeDonnees().add(bdd);
                    break;

                case "table":
                    req = re.split("\\(");   
                    String nomTable = req[0].split(" ")[2];
                    String[] colAndType = req[1].substring(0,re.split("\\(")[1].length() - 1).split(",");
                    String[] colonnes = new String[colAndType.length];
                    Class[] types = new Class[colAndType.length];
                    for(int i = 0; i < colAndType.length; i++){
                        colonnes[i] = colAndType[i].split(" ")[0];
                        types[i] = Class.forName(colAndType[i].split(" ")[1]);
                    }
                    Table t = new Table(nomTable, colonnes, types);
                    query.getBaseDeDonnees().getTable().add(t);
                    break;

                default:
                    throw new Exception("You have an error in your sql syntax");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
