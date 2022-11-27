package bddrelationnel;

import java.util.ArrayList;
import java.util.List;

import bddrelationnel.Update;
import bddrelationnel.UpdateKeyWord;

public class Where extends KeyWord {
    String[] colonnes;
    String[] values;

    public String[] getColonnes() {
        return colonnes;
    }

    public void setColonnes(String[] colonnes) {
        this.colonnes = colonnes;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public Where() {
    }

    public Where(KeyWord avant) {
        super("where");
        this.previous = avant;
        this.position = this.previous.getPosition() + 2;
        this.next = null;
    }

    public void execute(String[] req) {
        int i = this.position + 3;
        Table t = new Table("filtre");
        t = this.selection(((From) this.previous).getRelation(), req[i - 2].split("=")[0], req[i - 2].split("=")[1]);
        while (i < req.length) {
            String[] colAndVal = req[i].split("=");
            if (req[i - 1].equalsIgnoreCase("and")) {
                t = Requete.intersection(((From) this.previous).getRelation(), this
                        .selection(((From) this.previous).getRelation(), req[i].split("=")[0], req[i].split("=")[1]));
            } else {
                t = Requete.union(((From) this.previous).getRelation(), this
                        .selection(((From) this.previous).getRelation(), req[i].split("=")[0], req[i].split("=")[1]));
            }
            i += 2;
        }
        ((From) this.previous).setRelation(t);
    }

    public void executeFilter(String[] req) {
        int i = this.position + 3;
        Table t = null;
        t = Table.turnIntoTable(this.filter(((UpdateKeyWord) this.previous).getRelation(), req[i - 2].split("=")[0],
                req[i - 2].split("=")[1]));
        ((UpdateKeyWord) this.previous).setTableFiltre(t);
        while (i < req.length) {
            String[] colAndVal = req[i].split("=");
            if (req[i - 1].equalsIgnoreCase("and")) {
                t = Requete.intersection(((UpdateKeyWord) this.previous).getTableFiltre(), Table
                        .turnIntoTable(this.filter(((UpdateKeyWord) this.previous).getRelation(), req[i].split("=")[0],
                                req[i].split("=")[1])));
                ((UpdateKeyWord) this.previous).setTableFiltre(t);
            } else {
                t = Requete.union(((UpdateKeyWord) this.previous).getTableFiltre(), Table.turnIntoTable(this
                        .filter(((UpdateKeyWord) this.previous).getRelation(), req[i].split("=")[0],
                                req[i].split("=")[1])));
                ((UpdateKeyWord) this.previous).setTableFiltre(t);
            }
            i += 2;
        }
        ((UpdateKeyWord) this.previous).setTableFiltre(t);
    }

    public List<Integer> filter(Table t, String colonne, String value) {
        try {
            if (colonne == null && value == null)
                return null;
            List<Integer> listeIndice = new ArrayList<Integer>();
            for (int i = 0; i < t.getElements().size(); i++) {
                for (int n = 0; n < t.getTypes().length; n++) {
                    if (t.getColonnes()[n].equalsIgnoreCase(colonne)
                            && t.getElements().get(i)[n].toString().equalsIgnoreCase(value)) {
                        listeIndice.add(i);
                    }
                }
            }
            return listeIndice;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Table selection(Table t, String colonne, String valeur) {
        try {
            List<Integer> listeIndice = this.filter(t, colonne, valeur);
            if (listeIndice == null) {
                return t;
            }
            Table selection = new Table("Selection", t.getColonnes(), t.getTypes());
            for (int i = 0; i < t.getElements().size(); i++) {
                if (listeIndice.indexOf(i) != -1) {
                    selection.getElements().add(t.getElements().get(i));
                }
            }
            return selection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
