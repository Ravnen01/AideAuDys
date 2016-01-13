package iutbg.lpiem.aideauxdys.Interface;

/**
 * Created by Corentin on 13/01/2016.
 */
public interface Observable {
    void addObserveur(Observeur observeur);
    void notif();
}
