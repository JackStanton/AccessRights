package Classes;

import java.sql.*;
import java.util.ArrayList;

public class DBWorker {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void conn() throws ClassNotFoundException, SQLException
    {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        statmt = conn.createStatement();
    }


    public static ArrayList<UserObj> readUsers() throws ClassNotFoundException, SQLException
    {
        ArrayList<UserObj> list = new ArrayList<UserObj>();
        resSet = statmt.executeQuery("SELECT * FROM users");
        while(resSet.next()){
            int id = resSet.getInt("id");
            String username = resSet.getString("username");
            String password = resSet.getString("password");
            String role = resSet.getString("role");
            UserObj user = new UserObj(id, username,password,role);
            list.add(user);
        }
        return list;
    }

    public static ArrayList<FileObj> readFiles() throws ClassNotFoundException, SQLException
    {
        ArrayList<FileObj> list = new ArrayList<FileObj>();
        resSet = statmt.executeQuery("SELECT * FROM files");
        while(resSet.next()){
            int id = resSet.getInt("id");
            String filename = resSet.getString("filename");
            FileObj file = new FileObj(id,filename);
            list.add(file);
        }
        return list;
    }

    public static void resetRights() throws ClassNotFoundException, SQLException
    {
        statmt.execute("DELETE FROM rights WHERE id");
        ArrayList<UserObj> users = readUsers();
        ArrayList<FileObj> files = readFiles();
        for (int i = 0; i < files.size(); i++) {
            for (int j = 0; j < users.size(); j++) {
                int userID = users.get(j).getId();
                int fileID = files.get(i).getId();
                boolean write = false;
                boolean read = false;
                boolean transfer = false;
                boolean full = false;
                if (users.get(j).getGrant().equals("admin")){
                    write = true;
                    read = true;
                    transfer = true;
                    full = true;
                } else {
                    write = rand();
                    read = rand();
                    transfer = rand();
                    full = false;
                }
                statmt.execute("INSERT INTO rights ('userID','fileID','write','read','transfer','full') VALUES ("+userID+","+fileID+","+write+","+read+","+transfer+","+full+")");
            }
        }
    }

    public static ArrayList<FileObj> readRights() throws ClassNotFoundException, SQLException
    {
        ArrayList<FileObj> list = new ArrayList<FileObj>();
        ArrayList<FileObj> files = readFiles();
        ArrayList<UserObj> users = readUsers();
        int usrID = -1;
        String username = "";
        String write = "";
        String read = "";
        String transfer = "";
        String full = "";
        for (int i = 0; i < files.size(); i++) {
            int flID = files.get(i).getId();
            resSet = statmt.executeQuery("SELECT * FROM rights WHERE fileID = "+flID+" ");
            String filename = files.get(i).getName();
            ArrayList<UserObj> readUsers = new ArrayList<>();
            while(resSet.next()){
                usrID = resSet.getInt("userID");
                username = findUser(usrID,users);
                write = String.valueOf(resSet.getBoolean("write"));
                read = String.valueOf(resSet.getBoolean("read"));
                transfer = String.valueOf(resSet.getBoolean("transfer"));
                full = String.valueOf(resSet.getBoolean("full"));
                readUsers.add(new UserObj(username,write,read,transfer,full));
            }
            list.add(new FileObj(filename,readUsers));
        }
        return list;
    }

    public static void addUser(String user, String pas1, String gr) throws SQLException, ClassNotFoundException {
        statmt.execute("INSERT INTO users ('id','username','password','role') VALUES (?,\'"+user+"\',\'"+pas1+"\',\'"+gr+"\')");
        updateRights(user);
    }

    public static void applyRights(String oldUser, int newUser, String file) throws SQLException, ClassNotFoundException {
        ArrayList<UserObj> users = readUsers();
        ArrayList<FileObj> files = readFiles();
        int oldId = findUserId(oldUser, users);
        int fileId = findFileId(file, files);
        String write = "";
        String read = "";
        String transfer = "";
        String full = "";

        resSet = statmt.executeQuery("SELECT * FROM rights WHERE fileID = "+fileId+" AND userID = "+oldId+" ");
        while(resSet.next()){
            write = (resSet.getBoolean("write"))?"1":"0";
            read = (resSet.getBoolean("read"))?"1":"0";
            transfer = (resSet.getBoolean("transfer"))?"1":"0";
            full = (resSet.getBoolean("full"))?"1":"0";
        }
        System.out.println(newUser);
        System.out.println(oldId);
        System.out.println(fileId);
        statmt.executeUpdate("UPDATE  rights SET 'userID' = \'"+newUser+"\','fileID' = \'"+fileId+"\','write' = \'"+write+"\','read' = \'"+read+"\','transfer' = \'"+transfer+"\','full'= \'"+full+"\' WHERE fileID = \'"+fileId+"\' AND userID = \'"+newUser+"\' ");
        statmt.executeUpdate("UPDATE  rights SET 'userID' = "+oldId+",'fileID' = "+fileId+",'write' = false,'read' = false,'transfer' = false,'full'= false WHERE fileID = "+fileId+" AND userID = "+oldId+" ");
    }

    private static void updateRights(String username) throws SQLException, ClassNotFoundException {
        ArrayList<UserObj> users = readUsers();
        int userId = findUserId(username,users);
        ArrayList<FileObj> files = readFiles();
        for (int i = 0; i < files.size(); i++) {
            int fileID = files.get(i).getId();
            boolean write = false;
            boolean read = false;
            boolean transfer = false;
            boolean full = false;
            if (userId != -1){
                statmt.execute("INSERT INTO rights ('userID','fileID','write','read','transfer','full') VALUES ("+userId+","+fileID+","+write+","+read+","+transfer+","+full+")");
            }

        }
    }

    // --------Закрытие--------
    public static void closeDB() throws ClassNotFoundException, SQLException
    {
        conn.close();
        statmt.close();
        resSet.close();
    }

    private static boolean rand(){
        double a = 0;
        boolean newValue = false;
        a = Math.random()*100;
        newValue = a < 37;
        return newValue;
    };

    private static String findUser(int id, ArrayList<UserObj> array){
        String username = "";
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getId() == id) {
                username = array.get(i).getName();
            }
        }
        return username;
    }

    private static int findUserId(String username, ArrayList<UserObj> array){
        int id = -1;
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getName().equals(username)) {
                id = array.get(i).getId();
            }
        }
        return id;
    }

    private static int findFileId(String filename, ArrayList<FileObj> array){
        int id = -1;
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getName().equals(filename)) {
                id = array.get(i).getId();
            }
        }
        return id;
    }

}
