package bddrelationnel;

public class Join extends KeyWord {
    Table t1;
    Table t2;
    String colonneT1;
    String colonneT2;

    public Table getT1() {
        return t1;
    }

    public void setT1(Table t1) {
        this.t1 = t1;
    }

    public Table getT2() {
        return t2;
    }

    public void setT2(Table t2) {
        this.t2 = t2;
    }

    public String getColonneT1() {
        return colonneT1;
    }

    public void setColonneT1(String colonneT1) {
        this.colonneT1 = colonneT1;
    }

    public String getColonneT2() {
        return colonneT2;
    }

    public void setColonneT2(String colonneT2) {
        this.colonneT2 = colonneT2;
    }

    public Join(From from) {
        super("join");
        this.previous = from;
        this.position = this.previous.getPosition() + 2;
        this.t1 = ((From) this.previous).getRelation();
        this.next = null;
    }

    public void fillAttributes(String[] req, Database bdd) {
        this.t2 = bdd.getTableByName(req[this.position + 1]);
        // if (!req[this.position + 2].equalsIgnoreCase("on"))
        // throw new Exception("You have an error in your sql syntax");
        String[] lien = req[this.position + 3].split("=");
        this.colonneT1 = lien[0].split("\\.")[1];
        this.colonneT2 = lien[1].split("\\.")[1];
    }

    public Table execute() {
        return this.jointure();
    }

    public Table jointure() {
        Table produit = new Requete(null).produitCartesien(t1, t2);
        int[] indiceCol = new int[2];
        for (int i = 0; i < produit.getColonnes().length; i++) {
            if (produit.getColonnes()[i].equalsIgnoreCase(colonneT1)) {
                indiceCol[0] = i;
                i++;
                if (i == produit.getColonnes().length)
                    break;
            }
            if (produit.getColonnes()[i].equalsIgnoreCase(colonneT2))
                indiceCol[1] = i;
        }
        produit.showTable();
        for (int i = 0; i < produit.getElements().size(); i++) {
            if (!produit.getElements().get(i)[indiceCol[0]].toString()
                    .equalsIgnoreCase(produit.getElements().get(i)[indiceCol[1]].toString())) {
                produit.getElements().remove(produit.getElements().get(i));
                i--;
            }
        }
        return produit;
    }
}
