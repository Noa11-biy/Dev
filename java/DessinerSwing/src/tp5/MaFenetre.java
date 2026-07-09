package tp5;

import javax.swing.*;
import java.awt.*;

public class MaFenetre extends JFrame {
    private MonComposant composant;
    private JButton btRAZ;

    public MaFenetre(){
        composant = new MonComposant();
        btRAZ = new JButton("RAZ");
        setTitle("TP5");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        composant.addMouseListener(composant);
        btRAZ.addActionListener(composant);

        init();

    }

    public void init(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(btRAZ, gbc);
        gbc.gridy = 1;
        panel.add(composant, gbc);
        setContentPane(panel);
        this.pack();
    }
}
