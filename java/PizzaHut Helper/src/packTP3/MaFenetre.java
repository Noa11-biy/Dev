package packTP3;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MaFenetre extends JFrame implements ActionListener{

    private ArrayList<Pizza> listePizzas = new ArrayList<>();
    private ArrayList<JLabel> infoPizzas = new ArrayList<>();
    private ArrayList<JComboBox<Integer>> valPizzas = new ArrayList<>();
    private JMenu menu;
    private JMenuItem ajouter,supprimer,modifier;
    private JButton raz = new JButton("RAZ");
    private JButton calculer = new JButton("Calculer");
    private JLabel lbTotal = new JLabel("0 €");

    public MaFenetre() {
        this.setTitle("Pizza Hut");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ajoutPizza("Quatre Fromages", "Tomate, Parmesan, Mozzarella, Cheddar, Gorgonzola", 8.0);
        ajoutPizza("Diabolico", "Mozarella, Tomate, Merguez, Poivrons", 7.5);
        ajoutPizza("Hawaienne", "Mozarella, Tomate, Jambon, Ananas", 9.0);
        ajoutPizza("Reine", "Mozarella, Tomate, Jambon, Champignons", 7.0);
        ajoutPizza("Marguerite", "Tomate, Mozarella", 6.0);
        ajoutPizza("Calzone", "Mozarella, Tomate, Jambon, Oeuf", 11.0);

        JMenuBar menuBar = new JMenuBar();
        menu = new JMenu("Pizza");
        ajouter = new JMenuItem("Ajouter");
        supprimer = new JMenuItem("Supprimer");
        modifier = new JMenuItem("Modifier");
        menu.add(ajouter);
        menu.add(supprimer);
        menu.add(modifier);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        initFen();

        this.pack();
        this.setLocationRelativeTo(null);


    }


    public void ajoutPizza(String nomPizza, String ingredients, double tarif) {
        listePizzas.add(new Pizza(nomPizza, ingredients, tarif));
    }

    public void initFen() {

        // Réinitialisation du total
        lbTotal.setText("0 €");

        // Couleurs des boutons
        raz.setBackground(Color.RED);
        calculer.setBackground(Color.GREEN);

        // Enregistrement des listeners boutons (on évite les doublons)
        raz.removeActionListener(this);
        calculer.removeActionListener(this);
        ajouter.removeActionListener(this);
        supprimer.removeActionListener(this);
        modifier.removeActionListener(this);

        raz.addActionListener(this);
        calculer.addActionListener(this);
        ajouter.addActionListener(this);
        supprimer.addActionListener(this);
        modifier.addActionListener(this);

        // Reconstruction des listes graphiques
        infoPizzas.clear();
        valPizzas.clear();
        Integer[] qte = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (Pizza p : listePizzas) {
            infoPizzas.add(new JLabel(
                    p.getNom().toUpperCase() + "  " + p.getIngredients() + "  " + p.getTarif() + " €"
            ));
            valPizzas.add(new JComboBox<>(qte));
        }

        // Reconstruction du panel principal
        Container c = this.getContentPane();
        c.removeAll();
        c.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 6, 2, 6);

        // Lignes pizzas : colonne 0 = label, colonne 1 = combobox
        for (int i = 0; i < listePizzas.size(); i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.weightx = 1.0;
            gbc.weighty = 0;
            c.add(infoPizzas.get(i), gbc);

            gbc.gridx = 1;
            gbc.weightx = 0;
            c.add(valPizzas.get(i), gbc);
        }

        // Bouton RAZ — colonne 2, lignes 0 à 1
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        c.add(raz, gbc);

        // Bouton Calculer — colonne 2, lignes 2 à 4
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 3;
        c.add(calculer, gbc);

        // Label Total — colonne 2, dernière ligne
        gbc.gridx = 2;
        gbc.gridy = listePizzas.size() - 1;
        gbc.gridheight = 1;
        lbTotal.setHorizontalAlignment(SwingConstants.CENTER);
        c.add(lbTotal, gbc);

        // Redimensionnement automatique selon le nombre de pizzas
        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == raz) {
            for (JComboBox c : valPizzas)
                c.setSelectedIndex(0);
            lbTotal.setText("0 €");
        }

        if (e.getSource() == calculer) {
            double total = 0.0;
            for (int i = 0; i < valPizzas.size(); ++i)
                total += ((Integer) valPizzas.get(i).getSelectedItem()) * listePizzas.get(i).getTarif();
            lbTotal.setText(total + " €");
        }

        if (e.getSource() == ajouter) {
            Pizza p = new Boite(this, Boite.AJOUTER, listePizzas).showDialog();
            if (p != null) {
                listePizzas.add(p);
                initFen();
            }
        }

        if (e.getSource() == supprimer) {
            if (listePizzas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucune pizza à supprimer.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Pizza p = new Boite(this, Boite.SUPPRIMER, listePizzas).showDialog();
            if (p != null) {
                listePizzas.remove(p);
                initFen();
            }
        }

        if (e.getSource() == modifier) {
            if (listePizzas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucune pizza à modifier.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Pizza p = new Boite(this, Boite.MODIFIER, listePizzas).showDialog();
            if (p != null) initFen();
        }
    }
}