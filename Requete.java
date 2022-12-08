package bddrelationnel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.*;

public class Requete {
    JavaSQL appli;
    Database baseDeDonnees;

    public JavaSQL getAppli(){
        return this.appli;
    }

    public Database getBaseDeDonnees(){
        return this.baseDeDonnees;
    }

    public Requete(JavaSQL appli) {
        this.appli = appli;
    }

    public void useDatabase(String nom) throws Exception {
        for (int i = 0; i < this.appli.getBaseDeDonnees().size(); i++) {
            if (this.appli.getBaseDeDonnees().get(i).getNom().equalsIgnoreCase(nom))
                this.baseDeDonnees = appli.getBaseDeDonnees().get(i);
        }
        if (this.baseDeDonnees == null)
            throw new Exception("la base de donnees ' " + nom + " ' n'existe pas");
    }

    public Object executeQuery(String sql) throws Exception {
        Object result = null;
        try {
            String req = sql.split(";")[0];
            switch (req.split(" ")[0].toLowerCase()) {
                case "select":
                    Select select = new Select();
                    select.fillAttributes(req, 0, this.baseDeDonnees);
                    result = select.execute();
                    System.out.println("Resultat final");
                    ((Table) result).showTable();
                    break;

                case "insert":
                    Insert insert = new Insert();
                    insert.fillAttributes(req, this.baseDeDonnees);
                    insert.execute();
                    this.baseDeDonnees.reset();
                    result = "1 row affected";
                    break;

                case "update":
                    Update update = new Update();
                    update.fillAttributes(req, this.baseDeDonnees);
                    update.execute();
                    this.baseDeDonnees.reset();
                    result = "Update successful"; 
                    break;

                case "delete":
                    Delete delete = new Delete();
                    delete.fillAttributes(req, this.baseDeDonnees);
                    delete.execute();
                    this.baseDeDonnees.reset();
                    result = "Delete successful"; 
                    break;

                case "create":
                    Create create = new Create();
                    create.execute(req, this);
                    result = this.appli;
                    if(req.split(" ")[1].equalsIgnoreCase("table"))this.baseDeDonnees.reset();
                    break;

                case "show":
                    if (req.split(" ")[1].equalsIgnoreCase("databases")) {
                        this.appli.showDatabases();
                        result = this.appli;
                    } else if (req.split(" ")[1].equalsIgnoreCase("tables")) {
                        if(this.baseDeDonnees == null)
                            throw new Exception("No database selected");
                        this.baseDeDonnees.showListTable();
                        result = this.baseDeDonnees;
                    } else {
                        result = "";
                    }
                    break;

                case "use":
                    this.useDatabase(req.split(" ")[1]);
                    result = "Database changed";
                    break;

                default:
                    break;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean isMethod(String syntax) {
        String[] f = syntax.split("\\(");
        if (f.length <= 1)
            return false;
        if (f[1].indexOf(",") != -1) {
            return true;
        }
        return false;
    }

    public Method findMethod(String name) {
        Method[] fonctions = this.getClass().getDeclaredMethods();
        for (int i = 0; i < fonctions.length; i++) {
            if (fonctions[i].getName().equalsIgnoreCase(name))
                return fonctions[i];
        }
        return null;
    }

    public String concatenate(List<String> liste) {
        String c = "";
        for (int i = 0; i < liste.size(); i++) {
            c = c.concat(liste.get(i));
        }
        return c;
    }

    public static Table union(Table t1, Table t2) {
        try {
            isFollowCondition(t1, t2, "Union");
            Table unis = new Table("Union");
            for (int i = 0; i < t1.getElements().size(); i++) {
                unis.getElements().add(t1.getElements().get(i));
            }
            for (int i = 0; i < t2.getElements().size(); i++) {
                Object[] o = new Object[t1.getColonnes().length];
                for (int n = 0; n < t1.getTypes().length; n++) {
                    o[n] = t2.getElements().get(i)[n];
                }
                if (!isInArray(o, unis.getElements()))
                    unis.getElements().add(o);
            }
            unis.setColonnes(t1.getColonnes());
            unis.setTypes(t1.getTypes());
            return unis;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static Table intersection(Table t1, Table t2) {
        try {
            isFollowCondition(t1, t2, "Intersection");
            Table intersect = new Table("Intersection", t1.getColonnes(), t1.getTypes());
            for (int i = 0; i < t1.getElements().size(); i++) {
                Object[] o = t1.getElements().get(i);
                for (int n = 0; n < t2.getElements().size(); n++) {
                    if (isSame(o, t2.getElements().get(n))) {
                        intersect.getElements().add(o);
                    }
                }
            }
            intersect.setColonnes(t1.getColonnes());
            intersect.setTypes(t1.getTypes());
            return intersect;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Table projection(Table t, String[] colonne) {
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
            if (isInArray(o, projection.getElements()) == false)
                projection.getElements().add(o);
        }
        return projection;
    }

    public static Table division(Table fusion, Table t) {
        try {
            Table div = new Table("Division");
            int same = findSameColumn(fusion, t);
            Table R4 = projection(t, new String[] { t.getColonnes()[findSameColumn(t, fusion)] });
            String[] diffColonne = new String[fusion.getColonnes().length - 1];
            Class[] diffTypes = new Class[fusion.getTypes().length - 1];
            int k = 0;
            for (int i = 0; i < fusion.getColonnes().length; i++) {
                if (i != same) {
                    diffColonne[k] = fusion.getColonnes()[i];
                    diffTypes[k] = fusion.getTypes()[i];
                    k++;
                }
            }
            div.setColonnes(diffColonne);
            div.setTypes(diffTypes);
            Table R5 = projection(fusion, diffColonne);
            Table R6 = produitCartesien(R4, R5);
            Table R7 = difference(R6, fusion);
            Table R8 = projection(R7, diffColonne);
            div = difference(R5, R8);
            return div;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int findSameColumn(Table fusion, Table t) {
        int same = 0;
        for (int i = 0; i < fusion.getColonnes().length; i++) {
            for (int n = 0; n < t.getColonnes().length; n++) {
                if (!fusion.getColonnes()[i].equalsIgnoreCase(t.getColonnes()[n])) {

                } else {
                    same = i;
                }
            }
        }
        return same;
    }

    public static Table difference(Table t1, Table t2) {
        try {
            isFollowCondition(t1, t2, "Difference");
            Table difference = new Table("Difference", t1.getColonnes(), t1.getTypes());
            for (int i = 0; i < t1.getElements().size(); i++) {
                if (isInArray(t1.getElements().get(i), t2.getElements()) == false) {
                    difference.getElements().add(t1.getElements().get(i));
                }
            }
            return difference;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Table produitCartesien(Table t1, Table t2) {
        try {
            Table produit = new Table("Produit");
            int taille = t1.getTypes().length + t2.getTypes().length;
            String[] colonnes = new String[t1.getColonnes().length + t2.getColonnes().length];
            Class[] types = new Class[t1.getTypes().length + t2.getTypes().length];
            System.arraycopy(t1.getColonnes(), 0, colonnes, 0, t1.getColonnes().length);
            System.arraycopy(t2.getColonnes(), 0, colonnes, t1.getColonnes().length, t2.getColonnes().length);
            System.arraycopy(t1.getTypes(), 0, types, 0, t1.getTypes().length);
            System.arraycopy(t2.getTypes(), 0, types, t1.getTypes().length, t2.getTypes().length);
            produit.setColonnes(colonnes);
            produit.setTypes(types);
            List<Object[]> pc = new ArrayList<Object[]>();
            int l = 0;
            for (int k = 0; k < t1.getElements().size(); k++) {
                int j = 0;
                for (int i = 0; i < t2.getElements().size(); i++) {
                    Object[] o = new Object[taille];
                    for (int n = 0; n < t1.getTypes().length; n++) {
                        o[j] = t1.getElements().get(k)[n];
                        j++;
                    }
                    for (int n = 0; n < t2.getTypes().length; n++) {
                        o[j] = t2.getElements().get(i)[n];
                        j++;
                    }
                    pc.add(o);
                    l++;
                    if (pc.size() == (t1.getElements().size() * t2.getElements().size()))
                        break;
                    j = 0;
                }
            }
            produit.setElements(pc);
            return produit;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSame(Object[] o1, Object[] o2) {
        for (int i = 0; i < o1.length; i++) {
            try {
                if (!o1[i].equals(o2[i]))
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean isInArray(Object[] o, List<Object[]> liste) {
        for (int i = 0; i < liste.size(); i++) {
            if (isSame(o, liste.get(i)))
                return true;
        }
        return false;
    }

    public static boolean isFollowCondition(Table t1, Table t2, String fonction) throws Exception {
        if (t1.getTypes().length == t2.getTypes().length) {
            for (int i = 0; i < t1.getTypes().length; i++) {
                Class type1 = t1.getTypes()[i];
                Class type2 = t2.getTypes()[i];
                if (type1 != type2)
                    throw new Exception(fonction + " impossible");
            }
            return true;
        }
        throw new Exception(fonction + " impossible");
    }

    public static boolean controlValue(Class c, Object o) {
        if (o.getClass() == c)
            return true;
        return false;
    }
}
