package travelagency;

public class QueryOutput {
    private int ID;
    private String username;
    private String ranga;

    public QueryOutput(int ID, String username,String ranga) {
        this.ID = ID;
        this.username = username;
        this.ranga = ranga;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRanga() {
        return ranga;
    }

    public void setRanga(String ranga) {
        this.ranga = ranga;
    }
}
