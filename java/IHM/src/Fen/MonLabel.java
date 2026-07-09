package Fen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonLabel extends JLabel implements ActionListener {

    public MonLabel(String text){
        super(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setBackground(Color.RED);
        this.setOpaque(true);
}
}
