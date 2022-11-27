package bddrelationnel;

import java.util.*;

public class Update extends UpdateKeyWord {
    public Update() {
        super("update");
        this.position = 0;
    }

    public void fillAttributes(String[] req, Database bdd) throws Exception {
        if (req.length < 4 || !req[2].equalsIgnoreCase("set"))
            throw new Exception("You have an error in your sql syntax");
        this.relation = bdd.getTableByName(req[1]);
        this.separateColVal(req[3]);
        if (req[4].equalsIgnoreCase("where")) {
            this.next = new Where(this);
            this.next.setPosition(4);
            ((Where) this.next).executeFilter(req);
        }
    }

    public void execute() {
        int taille = this.relation.getElements().size();
        if (this.tableFiltre.getElements().size() != 0)
            taille = this.tableFiltre.getElements().size();
        for (int i = 0; i < taille; i++) {
            for (int k = 0; k < colonnes.length; k++) {
                for (int n = 0; n < this.relation.getColonnes().length; n++) {
                    if (colonnes[k].equalsIgnoreCase(this.relation.getColonnes()[n])) {
                        if (this.tableFiltre.getElements().size() != 0)
                            this.relation.setElements((int) this.tableFiltre.getElements().get(i)[0], n, values[k]);
                        else
                            this.relation.setElements(i, n, values[k]);
                    }
                }
            }
        }
    }

    public void separateColVal(String set) {
        String[] virgule = set.split(",");
        String[] col = new String[virgule.length];
        Object[] val = new Object[virgule.length];
        for (int i = 0; i < virgule.length; i++) {
            String[] egale = virgule[i].split("=");
            col[i] = egale[0];
            val[i] = this.changeType(egale[1], this.relation.getTypes()[this.relation.giveIndiceColonne(egale[0])]);
        }
        this.colonnes = col;
        this.values = val;
    }
}
