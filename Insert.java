package bddrelationnel;

public class Insert extends KeyWord {
    Table table;
    String[] colonnes;
    String[] values;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

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

    public Insert() {
        super("insert");
        this.position = 0;
    }

    public void fillAttributes(String re, Database bdd) throws Exception {
        String[] req = re.split(" ");
        if (bdd == null)
            throw new Exception("No database selected");
        if (!req[1].equalsIgnoreCase("into") || req.length != 4) {
            throw new Exception("You have an error in your sql syntax");
        }
        if (req[2].contains("(")) {
            String[] tableAndCol = req[2].replaceAll("\\)", "").split("\\(");
            this.table = bdd.getTableByName(tableAndCol[0]);
            this.colonnes = tableAndCol[1].split(",");
        } else {
            this.table = bdd.getTableByName(req[2]);
            this.colonnes = null;
        }
        if (req[3].substring(0, 6).equalsIgnoreCase("values")) {
            this.values = req[3].replaceAll("\\)", "").split("\\(")[1].split(",");
        } else {
            throw new Exception("You have an error in your sql syntax");
        }
    }

    public void execute() throws Exception {
        int c = 0;
        if (this.colonnes == null)
            c = this.table.getColonnes().length;
        else
            c = this.colonnes.length;
        if (c != this.values.length)
            throw new Exception("Column count doesn' t match value");
        Object[] o = new Object[this.table.getTypes().length];
        int n = 0;
        for (int i = 0; i < this.table.getColonnes().length; i++) {
            if (this.colonnes == null || this.table.getColonnes()[i].equalsIgnoreCase(this.colonnes[n])) {
                o[i] = this.changeType(this.values[n], this.table.getTypes()[i]);
                n++;
            }
        }
        this.table.getElements().add(o);
        // this.table.showTable();
    }

}
