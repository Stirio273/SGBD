package bddrelationnel;

public class Delete extends UpdateKeyWord {
    public Delete() {
        super("delete");
        this.position = 0;
    }

    public void fillAttributes(String[] req, Database bdd) {
        if (!req[1].equalsIgnoreCase("from")) {

        }
        this.relation = bdd.getTableByName(req[2]);
        if (req[3].equalsIgnoreCase("where")) {
            this.next = new Where(this);
            this.next.setPosition(3);
            ((Where) this.next).executeFilter(req);
            this.getTableFiltre().showTable();
        }
    }

    public void execute() {
        int taille = this.relation.getElements().size();
        if (this.tableFiltre.getElements().size() != 0)
            taille = this.tableFiltre.getElements().size();
        for (int i = 0; i < taille; i++) {
            if (this.tableFiltre.getElements().size() != 0)
                this.relation.getElements().remove((int) this.tableFiltre.getElements().get(i)[0]);
            else
                this.relation.getElements().remove(i);
        }
    }
}
