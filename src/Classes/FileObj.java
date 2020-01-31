package Classes;

import java.util.ArrayList;

public class FileObj {
    private int id;
    private String name;
    private ArrayList<UserObj> users;

    public FileObj(int id, String name){
        this.id = id;
        this.name = name;
    }

    public FileObj(String name, ArrayList<UserObj> users){
        this.name = name;
        this.users = users;
    }

    public ArrayList<UserObj> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserObj> users) {
        this.users = users;
    }



    public String getName(){
        return name;
    }

    public int getId() {
        return id;
    }
}
