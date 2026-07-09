package packTP3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Boite extends JDialog implements ActionListener {

    public static final int AJOUTER   = 0;
    public static final int SUPPRIMER = 1;
    public static final int MODIFIER  = 2;

    private JButton btValider, btAnnuler, btSuivant, btRetour;
    private JTextField tfNom, tfTarif, tfIngredients;
    private JComboBox<Pizza> cbPizzas;
    private CardLayout cardLayout;

    private Pizza pizza = null;
    private int mode;
    private List<Pizza> listePizzas;
    private MaFenetre owner;

    public Boite(MaFenetre owner, int mode, List<Pizza> listePizzas) {
        super(owner, titreSelon(mode), true);
        this.owner       = owner;
        this.mode        = mode;
        this.listePizzas = listePizzas;

        btValider = new JButton("Valider");
        btAnnuler = new JButton("Annuler");
        btValider.addActionListener(this);
        btAnnuler.addActionListener(this);

        switch (mode) {
            case AJOUTER   -> buildAjout();
            case SUPPRIMER -> buildSupprimer();
            case MODIFIER  -> buildModifier();
        }

        pack();
        setLocationRelativeTo(owner);
    }

    private static String titreSelon(int mode) {
        return switch (mode) {
            case AJOUTER   -> "Ajouter une pizza";
            case SUPPRIMER -> "Supprimer une pizza";
            case MODIFIER  -> "Modifier une pizza";
            default        -> "Pizza";
        };
    }

    // ── Ajout ─────────────────────────────────────────────────────────────
    private void buildAjout() {
        setLayout(new BorderLayout());
        add(panFormulaire(null), BorderLayout.CENTER);
        add(panBoutons(),        BorderLayout.SOUTH);
    }

    // ── Suppression ───────────────────────────────────────────────────────
    private void buildSupprimer() {
        cbPizzas = new JComboBox<>(listePizzas.toArray(new Pizza[0]));
        // Afficher le nom dans la combobox
        cbPizzas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Pizza) setText(((Pizza) value).getNom());
                return this;
            }
        });

        JPanel pan = new JPanel(new FlowLayout());
        pan.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        pan.add(new JLabel("Pizza à supprimer :"));
        pan.add(cbPizzas);

        setLayout(new BorderLayout());
        add(pan,          BorderLayout.CENTER);
        add(panBoutons(), BorderLayout.SOUTH);
    }

    // ── Modification (2 étapes via CardLayout) ────────────────────────────
    private void buildModifier() {
        cbPizzas = new JComboBox<>(listePizzas.toArray(new Pizza[0]));
        cbPizzas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Pizza) setText(((Pizza) value).getNom());
                return this;
            }
        });

        btSuivant = new JButton("Suivant >");
        btRetour  = new JButton("< Retour");
        btSuivant.addActionListener(this);
        btRetour.addActionListener(this);

        // Étape 1 : sélection
        JPanel ligneChoix = new JPanel(new FlowLayout());
        ligneChoix.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        ligneChoix.add(new JLabel("Pizza à modifier :"));
        ligneChoix.add(cbPizzas);

        JPanel nav1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nav1.add(btSuivant);
        nav1.add(btAnnuler);

        JPanel panSelection = new JPanel(new BorderLayout());
        panSelection.add(ligneChoix, BorderLayout.CENTER);
        panSelection.add(nav1,       BorderLayout.SOUTH);

        // Étape 2 : édition (formulaire vide, pré-rempli dans actionPerformed)
        panFormulaire(null); // initialise tfNom, tfTarif, tfIngredients

        JPanel nav2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nav2.add(btRetour);
        nav2.add(btValider);
        nav2.add(btAnnuler);

        JPanel panEdition = new JPanel(new BorderLayout());
        panEdition.add(panFormulaire(null), BorderLayout.CENTER);
        panEdition.add(nav2,               BorderLayout.SOUTH);

        cardLayout = new CardLayout();
        setLayout(cardLayout);
        add(panSelection, "selection");
        add(panEdition,   "edition");
    }

    // ── Panneau formulaire réutilisable ───────────────────────────────────
    private JPanel panFormulaire(Pizza p) {
        tfNom         = new JTextField(p != null ? p.getNom()                    : "", 15);
        tfTarif       = new JTextField(p != null ? String.valueOf(p.getTarif())  : "", 15);
        tfIngredients = new JTextField(p != null ? p.getIngredients()            : "", 15);

        JPanel pan = new JPanel(new GridLayout(3, 2, 5, 5));
        pan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pan.add(new JLabel("Nom :"));          pan.add(tfNom);
        pan.add(new JLabel("Tarif (€) :"));    pan.add(tfTarif);
        pan.add(new JLabel("Ingrédients :"));  pan.add(tfIngredients);
        return pan;
    }

    // ── Panneau Valider / Annuler ─────────────────────────────────────────
    private JPanel panBoutons() {
        JPanel pan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pan.add(btValider);
        pan.add(btAnnuler);
        return pan;
    }

    // ── Événements ────────────────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btAnnuler) {
            pizza = null;
            setVisible(false);

        } else if (src == btSuivant) {
            Pizza sel = (Pizza) cbPizzas.getSelectedItem();
            if (sel != null) {
                // Pré-remplissage des champs avec la pizza sélectionnée
                tfNom.setText(sel.getNom());
                tfTarif.setText(String.valueOf(sel.getTarif()));
                tfIngredients.setText(sel.getIngredients());
                cardLayout.show(getContentPane(), "edition");
                pack();
                setLocationRelativeTo(owner);
            }

        } else if (src == btRetour) {
            cardLayout.show(getContentPane(), "selection");

        } else if (src == btValider) {
            if (mode == SUPPRIMER) {
                pizza = (Pizza) cbPizzas.getSelectedItem();
                setVisible(false);
            } else {
                // AJOUTER ou MODIFIER
                try {
                    String nom         = tfNom.getText().trim();
                    double tarif       = Double.parseDouble(tfTarif.getText().trim());
                    String ingredients = tfIngredients.getText().trim();

                    if (nom.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Le nom ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (mode == AJOUTER) {
                        pizza = new Pizza(nom, ingredients, tarif);
                    } else { // MODIFIER
                        Pizza sel = (Pizza) cbPizzas.getSelectedItem();
                        sel.setNom(nom);
                        sel.setTarif(tarif);
                        sel.setIngredients(ingredients);
                        pizza = sel;
                    }
                    setVisible(false);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Tarif invalide (ex: 9.90)", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public Pizza showDialog() {
        setVisible(true);
        return pizza;
    }
}