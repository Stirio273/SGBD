package bddrelationnel;

import donnees.Fichier;
import java.util.List;
import java.util.ArrayList;

public class Database {
    String nom = "MyBDD";
    List<Table> table = new ArrayList<Table>();
    Fichier file;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Table> getTable() {
        return table;
    }

    public void setTable(List<Table> table) {
        this.table = table;
    }

    public Fichier getFile() {
        return file;
    }

    public void setFile(Fichier file) {
        this.file = file;
    }

    public Database(String nom, Fichier file) {
        this.nom = nom;
        this.file = file;
    }

    public Database(String nom, List<Table> table) {
        this.nom = nom;
        this.table = table;
    }

    public void createTable(String nom, String[] colonnes, Class[] typeColonnes) {
        Table t = new Table(nom, colonnes, typeColonnes);
        this.getTable().add(t);
    }

    public void dataToTable() {
        try {
            this.file.lecture();
            int line = 0;
            do {
                Table t = new Table(this.file.getData().get(line).replaceAll("---", ""));
                line++;
                String[] colAndClass = this.file.getData().get(line).split("\\|");
                line++;
                String[] colonnes = new String[colAndClass.length];
                Class[] types = new Class[colAndClass.length];
                for (int i = 0; i < colAndClass.length; i++) {
                    String[] coltyp = colAndClass[i].split("\\(");
                    colonnes[i] = coltyp[0];
                    types[i] = Class.forName(coltyp[1].replaceAll("\\)", "").replaceAll("class ", ""));
                }
                t.setTypes(types);
                t.setColonnes(colonnes);
                while (!this.file.getData().get(line).equals("")) {
                    String[] objet = this.file.getData().get(line).split(";");
                    Object[] o = new Object[objet.length];
                    for (int i = 0; i < o.length; i++) {
                        o[i] = objet[i];
                    }
                    t.getElements().add(o);
                    line++;
                }
                line++;
                this.table.add(t);
            } while (line < this.file.getData().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        try {
            this.file.modification(this.table);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Table getTableByName(String s) {
        for (int i = 0; i < this.table.size(); i++) {
            if (this.table.get(i).getName().equalsIgnoreCase(s)) {
                return this.table.get(i);
            }
        }
        return null;
    }
}
