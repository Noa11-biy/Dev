package Fen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Fenetre extends JFrame implements ActionListener, FocusListener {

    private JTextField text ;
    private JButton button ;
    private MonLabel labelNB ;
    private MonLabel labelBase ;
    private MonLabel labelResult ;
    private JComboBox<Integer> box;

    public Fenetre(){
        this.setTitle("Tuto");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
        button.addActionListener(this);
        text.addFocusListener(this);
        box.addActionListener(this);
        button.addActionListener(labelResult);
    }

    private void init(){
        text = new JTextField(15);
        button = new JButton("Convertir");
        labelNB = new MonLabel("NB Base 10");
        labelBase = new MonLabel(" -> base");
        labelResult = new MonLabel(" ");
        box = new JComboBox<>();
        box.addItem(2);
        box.addItem(3);
        box.addItem(8);
        box.addItem(16);

        JPanel panneau = new JPanel();
        panneau.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();
        cont.fill = GridBagConstraints.BOTH;
        cont.insets = new Insets(5,6,5,6);

        cont.gridx = 0;
        cont.gridy = 0;
        panneau.add(labelNB, cont);

        cont.gridx = 1;
        panneau.add(labelBase, cont);

        cont.gridx = 0;
        cont.gridy++;
        panneau.add(text, cont);

        cont.gridx = 1;
        panneau.add(box, cont);

        cont.gridy++;
        cont.gridx = 0;
        cont.gridwidth = 2;
        panneau.add(button, cont);
        cont.gridy++;
        panneau.add(labelResult, cont);

        this.setContentPane(panneau);
        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int nombre = Integer.parseInt(text.getText());
        int base = (Integer)box.getSelectedItem();
        String resultat = Integer.toString(nombre,base);
        labelResult.setText(resultat);
    }

    @Override
    public void focusGained(FocusEvent e) {
        text.setText("");
    }

    @Override
    public void focusLost(FocusEvent e) {

    }
}
