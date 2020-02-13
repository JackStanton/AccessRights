package GUI;

import Classes.DBWorker;
import Classes.UserObj;
import GUI.Panels.DocumentsPane;
import GUI.Panels.WelcomePan;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 700;
    private static final int WINDOW_WIDTH = 1100;

    private static final int WINDOW_POSITION_X = 220;
    private static final int WINDOW_POSITION_Y = 100;
    static JButton exitButton = new JButton("Выход");
    static JButton resetButton = new JButton("Сброс");
    public static JButton backButton = new JButton();
    static public JPanel btnPan = new JPanel();
    public static WelcomePan welcomePan;
    static public JScrollPane scrollPane;
    public static String title = "Права доступа";

    public MainWindow() throws SQLException, ClassNotFoundException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(WINDOW_POSITION_X,WINDOW_POSITION_Y);
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setTitle(title);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                delButton();
                DocumentsPane.panel.removeAll();
                remove(scrollPane);
                try {
                    paintMain();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                add(scrollPane);
                paintExit();
                add(btnPan,BorderLayout.SOUTH);

            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    DBWorker.resetRights();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
                scrollPane.setEnabled(false);
                    Inform inform = new Inform();
                    inform.setVisible(true);
                delButton();
                DocumentsPane.panel.removeAll();
                remove(scrollPane);
                try {
                    paintMain();
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    DBWorker.resetRights();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
                add(scrollPane);
                paintExit();
                add(btnPan,BorderLayout.SOUTH);
            }
        });

        paintMain();
        add(scrollPane);
        paintExit();
        add(btnPan,BorderLayout.SOUTH);
        setVisible(true);
    }

    private static int search(String[] array, String authUser){
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(authUser)){
                index = i;
            }
        }
        return index;
    }

    private static String getUserRole(ArrayList<UserObj> list, int index){
        return list.get(index).getGrant();
    }

    public static void delButton(){
        btnPan.removeAll();
        btnPan.updateUI();
    }

    public static void paintExit(){
        btnPan.add(exitButton, BorderLayout.SOUTH);
        btnPan.updateUI();
    }

    public static void paintBack(int init){

        String role = "";
        if (init == 1){
            backButton.setText("Назад");
            resetButton.setVisible(false);
        }
        if (init == 0) {
            backButton.setText("Сменить пользователя");
            resetButton.setVisible(true);
            int index = search(WelcomePan.users,WelcomePan.autUser1);
            role = getUserRole(WelcomePan.usersList, index);
        }
        btnPan.add(backButton, BorderLayout.SOUTH);
        if (role.equals("admin")){btnPan.add(resetButton, BorderLayout.SOUTH);}
        btnPan.updateUI();
    }

    public static void paintMain() throws SQLException, ClassNotFoundException {
        welcomePan = new WelcomePan(0);
        scrollPane = new JScrollPane(welcomePan);
        scrollPane.getVerticalScrollBar().setValue(1);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }
}
