package iutbg.lpiem.aideauxdys.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corentin on 07/01/2016.
 */
public class Decoupe {
    private String mot;
    private ArrayList<Character> listCharater=new ArrayList<>();
    private ArrayList<TypeCharacter> listeTypeCaracter=new ArrayList<>();
    private ArrayList<Integer> listCoupure=new ArrayList<>();

    private ArrayList<Character> listVoyelle=new ArrayList<Character>(){{add('A');add('E');add('I');add('O');add('U');add('È');add('Ë');add('Ê');
    add('Ù');add('À'); add('É');add('Ï');add('Y');}};
    private ArrayList<Character> listConsonne=new ArrayList<Character>(){{add('B');add('C');add('D');add('F');add('G');add('H');add('J');
    add('K');add('L');add('M');add('N');add('P');add('Q');add('R');add('S');add('T');add('V');add('W');add('X');add('X');add('Z');}};

    public Decoupe(String mot) {
        this.mot = mot.toUpperCase();

        for(int i=0;i<this.mot.length();i++){
            listCharater.add(this.mot.charAt(i));
        }

        determineListCharacter();

        for(int i=0;i<listeTypeCaracter.size();i++){
            if(listeTypeCaracter.get(i)==TypeCharacter.c){
                i=determineCoupure(i);
            }
        }

    }

    private void determineListCharacter(){
        for (Character aListCharater : listCharater) {
            if (listVoyelle.contains(aListCharater)) {
                listeTypeCaracter.add(TypeCharacter.v);
            } else if(listConsonne.contains(aListCharater)){
                listeTypeCaracter.add(TypeCharacter.c);
            }else{
                listeTypeCaracter.add(TypeCharacter.o);
            }
        }
        int i=0;
        while(listeTypeCaracter.get(i)==TypeCharacter.c && i!=listeTypeCaracter.size()){
            listeTypeCaracter.set(i,TypeCharacter.o);
            i++;
        }
        i=listeTypeCaracter.size()-1;
        while(listeTypeCaracter.get(i)==TypeCharacter.c && i!=-1){
            listeTypeCaracter.set(i,TypeCharacter.o);
            i--;
        }
    }

    private int determineCoupure(int startNumberConsonne){
        int i=startNumberConsonne;
        ArrayList<Character> listGroupeConsonne=new ArrayList<>();
        while(listeTypeCaracter.get(i)==TypeCharacter.c){
            listGroupeConsonne.add(listCharater.get(i));
            i++;
        }
        i--;
        if(i==startNumberConsonne){
            listCoupure.add(i);
            return i;
        }
        i=listGroupeConsonne.size()-1;
        i = coupureRegleSpe(i, listGroupeConsonne);
        listCoupure.add(startNumberConsonne+i);
        return startNumberConsonne+listGroupeConsonne.size()-1;
    }

    private int coupureRegleSpe(int i, ArrayList<Character> listGroupeConsonne) {
        if(i!=0) {
            if (listGroupeConsonne.get(i).equals('R') || listGroupeConsonne.get(i).equals('L')) {
                if (!listGroupeConsonne.get(i - 1).equals('R') || !listGroupeConsonne.get(i - 1).equals('L') || !listGroupeConsonne.get(i - 1).equals('N')) {
                    i--;
                    if (i != 0) {
                        i = coupureRegleSpe(i, listGroupeConsonne);
                    }
                }

            }
        }
        if(i!=0) {
            if (listGroupeConsonne.get(i) ==listGroupeConsonne.get(i - 1) && !listGroupeConsonne.get(i).equals('C')) {
                i--;
                if (i != 0) {
                    i = coupureRegleSpe(i, listGroupeConsonne);
                }
            }
        }

        if(i!=0) {
            if (listGroupeConsonne.get(i).equals('H') && (listGroupeConsonne.get(i - 1).equals('C') || listGroupeConsonne.get(i - 1).equals('P') || listGroupeConsonne.get(i - 1).equals('T') || listGroupeConsonne.get(i - 1).equals('R'))) {
                i--;
                if (i != 0) {
                    i = coupureRegleSpe(i, listGroupeConsonne);
                }

            }
        }
        if(i!=0) {
            if (listGroupeConsonne.get(i).equals('N') && listGroupeConsonne.get(i).equals('G')) {
                i--;
                if (i != 0) {
                    i = coupureRegleSpe(i, listGroupeConsonne);
                }

            }
        }
        return i;
    }

    public String afficheSyllabe(){
        String syllabe="";
        for(int i=0;i<listCharater.size();i++){
            if(listCoupure.contains(i)){
                syllabe=syllabe.concat(""+"-"+listCharater.get(i));
            }else{
                syllabe=syllabe.concat(""+listCharater.get(i));
            }
        }
        return syllabe;
    }

    public List<String> getSyllabe(){
        List<String> listSyllabe = new ArrayList<>();
        String part = "";
        for(int i=0;i<listCharater.size();i++){
            if(listCoupure.contains(i)){
                listSyllabe.add(part.toLowerCase());
                part = ""+listCharater.get(i);
            }else{
                part=part.concat(""+listCharater.get(i));
            }
        }
        listSyllabe.add(part.toLowerCase());
        return listSyllabe;
    }

}
