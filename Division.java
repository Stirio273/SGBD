package bddrelationnel;

public class Division extends KeyWord {
    Table t1;
    Table t2;

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

    public Division(From from) {
        super("division");
        this.previous = from;
        this.position = this.previous.getPosition() + 2;
    }

    public void fillAttributes(String[] req, Database bdd) {
        this.t1 = ((Select) this.getPrevious().getPrevious()).execute();
        this.next = new Select();
        ((Select) this.next).fillAttributes(req, this.position + 1, bdd);
        this.t2 = ((Select) this.next).execute();
    }

    public Table execute() {
        return Requete.division(t1, t2);
    }
}
