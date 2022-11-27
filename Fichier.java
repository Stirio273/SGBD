package donnees;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import bddrelationnel.Table;

public class Fichier extends File {
    String path;
    List<String> data = new ArrayList<String>();

    public List<String> getData() {
        return data;
    }

    public Fichier(String pathname) {
        super(pathname);
        this.path = pathname;
    }

    public Fichier(String parent, String child){
        super(parent, child);
    }

    public void ecriture(Table t) throws IOException {
        FileWriter fileWriter = new FileWriter(this.path, true);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        try {
            writer.write("---" + t.getName() + "---");
            writer.newLine();
            for (int i = 0; i < t.getColonnes().length; i++) {
                writer.write(t.getColonnes()[i] + "(" + t.getTypes()[i] + ")|");
            }
            writer.newLine();
            for (int i = 0; i < t.getElements().size(); i++) {
                for (int n = 0; n < t.getElements().get(0).length; n++) {
                    writer.write(t.getElements().get(i)[n] + ";");
                }
                writer.newLine();
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.close();
    }

    public void lecture() throws IOException {
        this.data.clear();
        FileReader fileReader = new FileReader(this.path);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        do {
            line = reader.readLine();
            if (line != null)
                this.data.add(line);
        } while (line != null);
        reader.close();
    }

    public void suppression(Object[] o) throws IOException {
        this.lecture();
        FileWriter fileWriter = new FileWriter(this.path, false);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        for (int i = 0; i < this.data.size(); i++) {
            // if(compareLine(i, o))System.out.println("Trouver");
            // else {
            writer.write(this.data.get(i));
            writer.newLine();
            // }
        }
        writer.close();
    }

    public void modification(List<Table> listTable) throws IOException {
        FileWriter fileWriter = new FileWriter(this.path, false);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write("");
        for (int i = 0; i < listTable.size(); i++) {
            this.ecriture(listTable.get(i));
        }
    }
}
