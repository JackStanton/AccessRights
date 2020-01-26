package GUI.Panels;

import Classes.FileObj;
import Classes.XMLWorker;
import GUI.MainWindow;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class TransferPan extends JPanel {
    String[] users = WelcomePan.users;
    String[] usersTrim = new String[users.length-1];
    String authUser = "";
    public static ArrayList<FileObj> files = DocumentsPane.files;
    int selectedUser = -1;
    int indexAutUser = -1;
    String read = DocumentsPane.read;
    String write = DocumentsPane.write;
    String transfer = DocumentsPane.transfer;
    String full = DocumentsPane.full;
    String filename = DocumentsPane.filename;
    TransferPan(String authUser){
        this.authUser = authUser;
        removeAll();
        updateUI();
        delAutUser();
        search(users,authUser);
        JLabel titleLabel = new JLabel("Выберите пользователя : ");
        JPanel panel = new JPanel(new GridLayout(5,1));
        JPanel panel1 = new JPanel(new GridLayout(1,2));
        JComboBox<String> comboBox = new JComboBox<>(usersTrim);
        JButton okButton = new JButton("Применить");
        JButton backButton = new JButton("Назад");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                RightsPane rightsPane = new RightsPane(authUser,read,write,transfer,full,filename);
                removeAll();
                updateUI();
                add(rightsPane);
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedUser = comboBox.getSelectedIndex();
                searchSelected(users,selectedUser);
                listWorker();
                try {
                    XMLWorker.applyRights(files);
                } catch (TransformerException | FileNotFoundException | ParserConfigurationException e) {
                    e.printStackTrace();
                }

                files = XMLWorker.parseRights();
                DocumentsPane.panel.removeAll();
                MainWindow.paintBack(0);
                removeAll();
                updateUI();
                DocumentsPane docPan = new DocumentsPane();
                add(docPan);
            }
        });

        JPanel btnPanel = new JPanel(new GridLayout(1,2));
        btnPanel.add(okButton);
        btnPanel.add(backButton);
        panel1.add(titleLabel);
        panel1.add(comboBox);
        panel.add(panel1);
        panel.add(btnPanel);
        add(panel);
        updateUI();
    }

    private void delAutUser(){
        int j = 0;
        for (int i = 0; i < users.length; i++) {
            if (!users[i].equals(authUser)){
                 usersTrim[j] = users[i];
                j++;
            }
        }
    }

    private void listWorker(){
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getName().equals(filename)){
                if (!files.get(i).getUsers().get(selectedUser).getTransfer().equals("true")){files.get(i).getUsers().get(selectedUser).setTransfer(files.get(i).getUsers().get(indexAutUser).getTransfer());}
                if (!files.get(i).getUsers().get(selectedUser).getRead().equals("true")){files.get(i).getUsers().get(selectedUser).setRead(files.get(i).getUsers().get(indexAutUser).getRead());}
                if (!files.get(i).getUsers().get(selectedUser).getWrite().equals("true")){files.get(i).getUsers().get(selectedUser).setWrite(files.get(i).getUsers().get(indexAutUser).getWrite());}
                if (!files.get(i).getUsers().get(selectedUser).getFull().equals("true")){files.get(i).getUsers().get(selectedUser).setFull(files.get(i).getUsers().get(indexAutUser).getFull());}
                files.get(i).getUsers().get(indexAutUser).setFull("false");
                files.get(i).getUsers().get(indexAutUser).setRead("false");
                files.get(i).getUsers().get(indexAutUser).setWrite("false");
                files.get(i).getUsers().get(indexAutUser).setTransfer("false");
            }
        }
    }

    private void search(String[] array, String authUser){
        for (int i = 0; i < users.length; i++) {
            if (users[i].equals(authUser)){
                indexAutUser = i;
            }
        }
    }

    private void searchSelected(String[] array, int select){
        String name = usersTrim[select];
        for (int i = 0; i < users.length; i++) {
            if (users[i].equals(name)){
                selectedUser = i;
            }
        }
    }
}
