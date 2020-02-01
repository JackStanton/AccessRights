
import Classes.VBSUtility;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

//    public static void main(String[] args) throws Exception {
//        DBWorker.conn();

//        new MainWindow();
//        EncryptionUntil.generateKey();
//        DBWorker.resetRights();
//        String str = "200";
//        str = Base64.getEncoder().encodeToString(str.getBytes());
//        System.out.println(new String(str));
//    }


    public static void main(String[] args) {
        String[] propNames = new String[] { "Name","MaxClockSpeed", "ProcessorId"};
        Map<String, String> map = VBSUtility.printComputerSystemProductInfo(propNames);
        System.getProperty("user.name");
        for (int i = 0; i < map.size(); i++) {
            System.out.println(propNames[i]+" "+map.get(propNames[i]));
        }
    }

}

