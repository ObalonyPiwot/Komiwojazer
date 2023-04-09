package gui.pl.demo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        JButton button1 = new JButton("Button 1");
        c.gridx = 0;
        c.gridy = 0;
        panel.add(button1, c);

        JButton button2 = new JButton("Button 2");
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 3;
        panel.add(button2, c);

        JButton button3 = new JButton("Button 3");
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 3;
        panel.add(button3, c);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}