package bddrelationnel;

public class From extends KeyWord {
    Table relation;

    public Table getRelation() {
        return relation;
    }

    public void setRelation(Table relation) {
        this.relation = relation;
    }

    public From(KeyWord previous, Table t) {
        super("from");
        this.previous = previous;
        this.position = this.previous.getPosition() + 2;
        this.relation = t;
    }

    public void fillAttributes(String re, Database bdd) throws Exception {
        String[] req = re.split(" ");
        if (req.length <= this.position + 2)
            return;
        switch (req[this.position + 2].toLowerCase()) {
            case "where":
                this.next = new Where(this);
                ((Where) this.next).execute(req);
                break;

            case "join":
                this.next = new Join(this);
                ((Join) this.next).fillAttributes(req, bdd);
                this.setRelation(((Join) this.next).execute());
                break;

            case "division":
                this.next = new Division(this);
                ((Division) this.next).fillAttributes(re, bdd);
                this.setRelation(Requete.division(((Division) this.next).getT1(), ((Division) this.next).getT2()));
                break;

            case "order":
                this.next = new Order(this);
                ((Order) this.next).fillAttributes(req, this.position + 2);
                ((Order) this.next).execute();
                break;
            default:
                break;
        }
    }
}
