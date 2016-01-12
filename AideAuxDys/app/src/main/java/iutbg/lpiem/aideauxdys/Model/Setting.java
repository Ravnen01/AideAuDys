package iutbg.lpiem.aideauxdys.Model;

public class Setting {
    private long id;
    private String schema;
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private int size;
    private int color;
    private boolean isEnabled;

    public Setting(long id, String schema, boolean bold, boolean italic, boolean underline, int size, int color, boolean isEnabled) {
        this.id = id;
        this.schema = schema;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.size = size;
        this.color = color;
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
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
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public Setting setColor(int color) {
        this.color = color;
        return null;
    }
}
