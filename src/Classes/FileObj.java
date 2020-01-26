package Classes;

import java.util.ArrayList;

public class FileObj {
    private String name;
    private ArrayList<UserObj> users = new ArrayList<UserObj>();

    public FileObj(String name){
    this.name = name;
    }

    public ArrayList<UserObj> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserObj> users) {
        this.users = users;
    }

    public FileObj(String name, ArrayList<UserObj> users){
        this.name = name;
        this.users = users;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}
