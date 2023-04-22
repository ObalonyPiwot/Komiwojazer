package gui.pl.demo;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MenuExample extends JFrame {

    private JPanel mainPanel;

    public MenuExample() {
        super("Przykład Menu");

        // Tworzenie menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Plik");
        JMenuItem newMenuItem = new JMenuItem("Nowy");
        newMenuItem.addActionListener(e -> {
            // Wyświetlenie nowego wyglądu po kliknięciu Nowy
            setContentPane(new JPanel());
            pack();
            setVisible(true);
        });
        fileMenu.add(newMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Ustawienie panelu głównego
        mainPanel = new JPanel();
        setContentPane(mainPanel);

        // Konfiguracja okna
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MenuExample();
    }
}