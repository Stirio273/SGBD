package bddrelationnel;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Order extends KeyWord {
    String[] colonnes;
    int ordre;

    public String[] getColonnes() {
        return colonnes;
    }

    public void setColonnes(String[] colonnes) {
        this.colonnes = colonnes;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public Order(From from) {
        super("order");
        this.previous = from;
    }

    public void fillAttributes(String[] req, int indice) {
        this.position = indice;
        this.colonnes = req[indice + 2].split(",");
        if(req.length > indice + 3){
            if(req[indice + 3].equalsIgnoreCase("desc")){
                this.ordre = 1;
            }
        }
    }

    public void execute() {
        List liste = this.turnIntoList(this.colonnes[0]);
        int indiceCol = findIndiceCol(this.colonnes[0]);
        Collections.sort(liste);
        for (int i = 0; i < liste.size(); i++) {
            System.out.println(liste.get(i));
        }
        List<Object[]> lignes = this.triLigne(liste, ((From) this.previous).getRelation().getElements(), indiceCol);
        for(int i = 0; i < lignes.size(); i++){
            System.out.println(lignes.get(i)[0]);
        }
        ((From) this.previous).getRelation().setElements(lignes);
    }

    public int getIndiceMin(int debut) {
        int indiceMin = debut;
        return indiceMin;
    }

    public List<Object[]> triLigne(List listeTrier, List<Object[]> listeComplet, int same) {
        //List<Integer> listeLigne = new ArrayList<Integer>();
        List<Object[]> listeCompletTrier = new ArrayList<Object[]>();
        int k = 0;
        for(int i = 0; i < listeTrier.size(); i++){
            int n = k;
            while(n < listeComplet.size()){
                if(listeTrier.get(i).toString().equalsIgnoreCase(listeComplet.get(n)[same].toString())){
                    listeCompletTrier.add(listeComplet.get(n));
                    if(i < listeTrier.size() - 2){
                        if(listeTrier.get(i).toString().equalsIgnoreCase(listeTrier.get(i+1).toString())){
                            k = n + 1;
                        } else {
                            k = 0; 
                        }
                    }
                    break;
                }
                n++;
            }
        }
        return listeCompletTrier;
    }

    public List<?> turnIntoList(String colonne) {
        Table t = ((From) this.previous).getRelation();
        int indiceCol = findIndiceCol(colonne);
        List<String> listeS = new ArrayList<String>();
        List<Integer> listeI = new ArrayList<Integer>();
        for (int i = 0; i < t.getElements().size(); i++) {
            if(t.getTypes()[indiceCol].getSimpleName().equalsIgnoreCase("Integer"))
                listeI.add(Integer.valueOf(t.getElements().get(i)[indiceCol].toString()));
            else 
                listeS.add(t.getElements().get(i)[indiceCol].toString());
        }
        if(t.getTypes()[indiceCol].getSimpleName().equalsIgnoreCase("Integer"))
            return listeI;
        else 
            return listeS;
    }

    public int findIndiceCol(String colonne) {
        Table t = ((From) this.previous).getRelation();
        int same = -1;
        for (int n = 0; n < t.getColonnes().length; n++) {
            if (t.getColonnes()[n].equalsIgnoreCase(colonne)) {
                same = n;
            }
        }
        return same;
    }
}
