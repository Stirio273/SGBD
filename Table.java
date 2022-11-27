package bddrelationnel;

import java.util.List;
import java.util.ArrayList;

public class Table {
    String name;
    String[] colonnes;
    Class[] types;
    List<Object[]> elements = new ArrayList<Object[]>();

    public String getName() {
        return this.name;
    }

    public void setName(String s) {
        this.name = s;
    }

    public String[] getColonnes() {
        return this.colonnes;
    }

    public void setColonnes(String[] c) {
        this.colonnes = c;
    }

    public Class[] getTypes() {
        return this.types;
    }

    public void setTypes(Class[] c) {
        this.types = c;
    }

    public List<Object[]> getElements() {
        return this.elements;
    }

    public void setElements(List<Object[]> le) {
        this.elements = le;
    }

    public void setElements(int indice, int indiceColonne, Object value) {
        this.setElement(indiceColonne, this.elements.get(indice), value);
    }

    public void setElement(int indice, Object[] o, Object value) {
        o[indice] = value;
    }

    public Table(String name) {
        this.name = name;
    }

    public Table(String name, String[] colonnes, Class[] types) {
        this.name = name;
        this.colonnes = colonnes;
        this.types = types;
    }

    public static Table turnIntoTable(List<Integer> listeLigne) {
        Table t = new Table("NumeroLigne", new String[] { "ligne" }, new Class[] { Integer.class });
        for (int i = 0; i < listeLigne.size(); i++) {
            Object[] o = new Object[1];
            o[0] = listeLigne.get(i);
            t.getElements().add(o);
        }
        return t;
    }

    public int giveIndiceColonne(String col) {
        for (int i = 0; i < this.colonnes.length; i++) {
            if (col.equalsIgnoreCase(this.colonnes[i]))
                return i;
        }
        return -1;
    }

    public void showTable() {
        if (this == null)
            return;
        for (int i = 0; i < this.colonnes.length; i++) {
            System.out.print(this.colonnes[i] + "\t");
        }
        System.out.println();
        for (int i = 0; i < this.colonnes.length; i++) {
            for (int n = 0; n < this.colonnes[i].length(); n++) {
                System.out.print("-");
            }
            System.out.print("\t");
        }
        System.out.println();
        for (int k = 0; k < this.elements.size(); k++) {
            for (int i = 0; i < this.colonnes.length; i++) {
                System.out.print(this.elements.get(k)[i] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
}