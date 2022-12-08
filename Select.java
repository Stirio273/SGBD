package bddrelationnel;

public class Select extends KeyWord {
    String[] colonnes;

    public String[] getColonnes() {
        return colonnes;
    }

    public void setColonnes(String[] colonnes) {
        this.colonnes = colonnes;
    }

    public Select() {
        super("select");
    }

    public void fillAttributes(String re, int position, Database bdd) throws Exception {
        String[] req = re.split(" ");
        if (bdd == null)
            throw new Exception("No database selected");
        this.setPosition(position);
        if (req[this.position + 1].equalsIgnoreCase("*")) {
            this.colonnes = null;
        } else
            this.colonnes = req[this.position + 1].split(",");
        if (req[this.position + 2].equalsIgnoreCase("from")) {
            this.next = new From(this, bdd.getTableByName(req[this.position + 3]).clone());
            ((From) this.next).fillAttributes(re, bdd);
        }
    }

    public Table execute() {
        return this.projection(((From) this.getNext()).getRelation(), this.colonnes);
    }

    public Table projection(Table t, String[] colonne) {
        if (colonne == null)
            return t;
        Table projection = new Table("Projection");
        projection.setColonnes(colonne);
        Class[] types = new Class[colonne.length];
        int n = 0;
        for (int i = 0; i < t.getColonnes().length; i++) {
            if (t.getColonnes()[i].equalsIgnoreCase(colonne[n])) {
                types[n] = t.getTypes()[i];
                n++;
            }
            if (n == colonne.length)
                break;
        }
        projection.setTypes(types);
        for (int k = 0; k < t.getElements().size(); k++) {
            n = 0;
            Object[] o = new Object[colonne.length];
            for (int i = 0; i < t.getColonnes().length; i++) {
                if (t.getColonnes()[i].equalsIgnoreCase(colonne[n])) {
                    o[n] = t.getElements().get(k)[i];
                    n++;
                }
                if (n == colonne.length)
                    break;
            }
            if (Requete.isInArray(o, projection.getElements()) == false)
                projection.getElements().add(o);
        }
        return projection;
    }
}
