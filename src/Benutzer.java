
public class Benutzer {

    private int id;
    private String password;
    private String name;
    private String nachname;
    private String rolle;

    public Benutzer(int id, String password, String name, String nachname, String rolle) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.nachname = nachname;
        this.rolle = rolle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

}
