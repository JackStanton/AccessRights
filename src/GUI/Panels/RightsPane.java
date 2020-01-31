package GUI.Panels;

import GUI.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RightsPane extends JPanel {
    RightsPane(String username, String read, String write, String transfer, String full, String file){
        removeAll();
        updateUI();
        JPanel panel = new JPanel(new GridLayout(7,1));
        JPanel usernameF = new JPanel(new GridLayout(1,2));
        JPanel nameF = new JPanel(new GridLayout(1,2));
        JPanel readP = new JPanel(new GridLayout(1,2));
        JPanel writeP = new JPanel(new GridLayout(1,2));
        JPanel transP = new JPanel(new GridLayout(1,2));
        JPanel fullP = new JPanel(new GridLayout(1,2));
        JCheckBox readBox = new JCheckBox();
        readBox.setEnabled(false);
        if (read.equals("true")){readBox.setSelected(true);}else{readBox.setSelected(false);}
        JCheckBox writeBox = new JCheckBox();
        writeBox.setEnabled(false);
        if (write.equals("true")){writeBox.setSelected(true);}else{writeBox.setSelected(false);}
        JCheckBox transBox = new JCheckBox();
        transBox.setEnabled(false);
        if (transfer.equals("true")){transBox.setSelected(true);}else{transBox.setSelected(false);}
        JCheckBox fullBox = new JCheckBox();
        fullBox.setEnabled(false);
        if (full.equals("true")){fullBox.setSelected(true);}else{fullBox.setSelected(false);}
        usernameF.add(new JLabel("Имя пользователя :"));
        usernameF.add(new JLabel(username));
        nameF.add(new JLabel("Название файла :"));
        nameF.add(new JLabel(file));
        readP.add(new JLabel("Чтение : "));
        readP.add(readBox);
        writeP.add(new JLabel("Запись : "));
        writeP.add(writeBox);
        transP.add(new JLabel("Передача прав : "));
        transP.add(transBox);
        fullP.add(new JLabel("Полный доступ : "));
        fullP.add(fullBox);
        JButton button = new JButton("Назад");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DocumentsPane.panel.removeAll();
                MainWindow.paintBack(0);
                removeAll();
                updateUI();
                DocumentsPane docPan = null;
                try {
                    docPan = new DocumentsPane(username);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                add(docPan);
            }
        });

        JButton transButton = new JButton("Передать права");
        if (transfer.equals("true")){
            transButton.setEnabled(true);
        } else {
            transButton.setEnabled(false);
        }

        transButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.removeAll();
                TransferPan transferPan = new TransferPan(username);
                add(transferPan);
                updateUI();
            }
        });

        JPanel buttonPan = new JPanel(new GridLayout(1,2));
        panel.add(usernameF);
        panel.add(nameF);
        panel.add(readP);
        panel.add(writeP);
        panel.add(transP);
        panel.add(fullP);
        buttonPan.add(button);
        buttonPan.add(transButton);
        panel.add(buttonPan);
        add(panel);
        updateUI();
    }
}
