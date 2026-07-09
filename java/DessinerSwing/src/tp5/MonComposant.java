package tp5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class MonComposant extends JComponent implements MouseListener, ActionListener {

    private ArrayList<Point> liste = new ArrayList<>();


    @Override
    public Dimension getPreferredSize(){
        return new Dimension(600, 400);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        if (!liste.isEmpty()) {
            for (Point p : liste){
                g.drawLine(p.x-5, p.y+5, p.x+5, p.y-5);
                g.drawLine(p.x+5, p.y-5, p.x-5, p.y+5);
            }

            g.setColor(Color.BLUE);
            Point origine, destination;
            for (int i = 0; i < liste.size()-1; i++) {
                origine = liste.get(i);
                destination = liste.get(i+1);
                g.drawLine(origine.x, origine.y, destination.x, destination.y);
            }
        }

    }


    @Override
    public void mouseClicked(MouseEvent e) {
        // RAS
    }

    @Override
    public void mousePressed(MouseEvent e) {
        liste.add(e.getPoint());
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // RAS
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // RAS
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // RAS
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        liste.clear();
        repaint();
    }
}
