package GUI.Panels;

import Classes.DBWorker;
import Classes.FileObj;
import GUI.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class DocumentsPane extends JPanel {
    public static ArrayList<FileObj> files = new ArrayList<>();
    static int fileCount = -1;
    static int cols = 5;
    static int rows = fileCount / cols;
    public static JPanel panel = new JPanel(new GridLayout(rows,cols));
    public static String read = "";
    public static String write = "";
    public static String transfer = "";
    public static String full = "";
    public static String filename = "";

    DocumentsPane(String autUser) throws SQLException, ClassNotFoundException {

        removeAll();
        updateUI();
        files.clear();
        files = DBWorker.readRights();
        fileCount = files.size();
        for (int i = 0; i < fileCount; i++) {
            JButton button = new JButton();
            button.setText(files.get(i).getName());
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    MainWindow.delButton();
                    for (int j = 0; j < files.size(); j++) {
                        if (files.get(j).getName().equals(button.getText())){
                            filename = files.get(j).getName();
                            System.out.println(files.get(j).getId());
                            System.out.println(files.get(j).getName());
                            for (int k = 0; k < files.get(j).getUsers().size(); k++) {
                                if (files.get(j).getUsers().get(k).getName().equals(autUser)){
                                    read = files.get(j).getUsers().get(k).getRead();
                                    write = files.get(j).getUsers().get(k).getWrite();
                                    transfer = files.get(j).getUsers().get(k).getTransfer();
                                    full = files.get(j).getUsers().get(k).getFull();
                                }
                            }
                        }
                    }
                    RightsPane rightsPane = new RightsPane(autUser, read,write,transfer,full,filename);
                    removeAll();
                    updateUI();
                    add(rightsPane);
                }
            });
            panel.add(button);
        }
        add(panel);
        updateUI();
    }
}
