package iutbg.lpiem.aideauxdys.Model;

public class Setting {
    private long id;
    private String schema;
    private boolean gras;
    private boolean italique;
    private boolean souligner;
    private int taille;
    private String police;
    private String couleur;

    public Setting(long id, String schema, boolean gras, boolean italique, boolean souligner, int taille, String police, String couleur) {
        this.id = id;
        this.schema = schema;
        this.gras = gras;
        this.italique = italique;
        this.souligner = souligner;
        this.taille = taille;
        this.police = police;
        this.couleur = couleur;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public boolean isBold() {
        return gras;
    }

    public void setBold(boolean gras) {
        this.gras = gras;
    }

    public boolean isItalic() {
        return italique;
    }

    public void setItalic(boolean italique) {
        this.italique = italique;
    }

    public boolean isUnderline() {
        return souligner;
    }

    public void setUnderline(boolean souligner) {
        this.souligner = souligner;
    }

    public int getTaille() {
        return taille;
    }

    public void setSize(int taille) {
        this.taille = taille;
    }

    public String getPolice() {
        return police;
    }

    public void setPolice(String police) {
        this.police = police;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }
}
