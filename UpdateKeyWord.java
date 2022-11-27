package bddrelationnel;

import bddrelationnel.KeyWord;

public class UpdateKeyWord extends KeyWord {
    Table relation;
    Table tableFiltre;
    String[] colonnes;
    Object[] values;

    public Table getRelation() {
        return relation;
    }

    public void setRelation(Table relation) {
        this.relation = relation;
    }

    public Table getTableFiltre() {
        return tableFiltre;
    }

    public void setTableFiltre(Table tableFiltre) {
        this.tableFiltre = tableFiltre;
    }

    public String[] getColonnes() {
        return colonnes;
    }

    public void setColonnes(String[] colonnes) {
        this.colonnes = colonnes;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public UpdateKeyWord(String syntax) {
        super(syntax);
    }
}
