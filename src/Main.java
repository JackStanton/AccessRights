
import Classes.DBWorker;
import GUI.MainWindow;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        DBWorker.conn();

        new MainWindow();
//        EncryptionUntil.generateKey();
//        DBWorker.resetRights();
//        String str = "200";
//        str = Base64.getEncoder().encodeToString(str.getBytes());
//        System.out.println(new String(str));
    }
}

