package Classes;

public class UserObj {
    private String name;
    private String pass;
    private String grant;

    private String write = "false";
    private String read = "false";
    private String transfer = "false";
    private String full = "false";

    public String getWrite() {
        return write;
    }

    public void setWrite(String write) {
        this.write = write;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public UserObj(String name, String write, String read, String transfer, String full) {
        this.name = name;
        this.write = write;
        this.read = read;
        this.transfer = transfer;
        this.full = full;
    }

    public UserObj(String name, String pass, String grant) {
        this.name = name;
        this.pass = pass;
        this.grant = grant;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public String getGrant() {
        return grant;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setGrant(String grant) {
        this.grant = grant;
    }
}
