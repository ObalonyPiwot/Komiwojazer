package gui.pl.demo;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;

import static gui.pl.demo.JDBC.options;
import static gui.pl.demo.Komiwojazer2.tsp2;

public class HelloApplication extends JPanel implements  ActionListener {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 870;
    private static final int HEIGHT = 800;
    private static final int UKLWIDTH = 600;
    private static final int UKLHEIGHT = 600;
    private static final int BORDER_GAP_TOP = 80;
    private static final int BORDER_GAP_LEFT = 10;
    private static final int POINT_RADIUS = 3;
    public int AXIS_GAP;
    public String zestaw;
    public Vector<String> zestawy;
    public JComboBox<String> comboBox;
    public ArrayList<HelloApplication.Point> bestPath = new ArrayList<HelloApplication.Point>();
    public double minDist;
    public boolean drawingPath=false;
    public ArrayList<Point> points = new ArrayList<Point>();
    public JList<Point> list;
    private int ilPunktow = 0;
    public int skalaUkladu = 10;
    public HelloApplication() throws SQLException {

        setBackground(Color.WHITE);
        // - lista

        list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(200, 200));

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // sprawdzenie, czy wybór jest ustalony
                    Point p = list.getSelectedValue();
                    for(Point point: points)
                        if(point==p){
                            point.isClicked=true;
                            repaint();
                            return;
                        }
                }
            }
        });

        // inicjalizacja przycisku modyfikacji punktów
        JButton modifyButton; // przycisk do modyfikacji punktów
        modifyButton = new JButton("Modyfikuj");
        modifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point selectedPoint = list.getSelectedValue(); // pobranie zaznaczonego punktu z listy
                if (selectedPoint != null) {
                    int x = Integer.parseInt(JOptionPane.showInputDialog("Podaj nową wartość x:"));
                    int y = Integer.parseInt(JOptionPane.showInputDialog("Podaj nową wartość y:"));
                    selectedPoint.setxCoord(x); // zmiana współrzędnych wybranego punktu
                    selectedPoint.setyCoord(y); // zmiana współrzędnych wybranego punktu
                    list.setListData(points.toArray(new Point[points.size()])); // odświeżenie listy
                    drawingPath = false;
                    repaint();
                }
            }
        });

        // inicjalizacja przycisku usuwania punktów
        JButton deleteButton; // przycisk do usuwania punktów
        deleteButton = new JButton("Usuń");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point selectedPoint = list.getSelectedValue(); // pobranie zaznaczonego punktu z listy
                if (selectedPoint != null) {
                    points.remove(selectedPoint); // usunięcie wybranego punktu z listy
                    for(int i=0; i<points.size();i++)
                        points.get(i).id = i;
                    list.setListData(points.toArray(new Point[points.size()])); // odświeżenie listy
                    drawingPath = false;
                    repaint();
                }
            }
        });

        // inicjalizacja przycisku dodawania punktów
        JButton addButton; // przycisk do dodawania punktów
        addButton = new JButton("Dodaj");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double x = Double.parseDouble(JOptionPane.showInputDialog("Podaj wartość x:"));
                double y = Double.parseDouble(JOptionPane.showInputDialog("Podaj wartość y:"));
                Point p = new Point(points.size()+1,x*AXIS_GAP,y*AXIS_GAP);
                points.add(p); // dodanie punktu do listy
                list.setListData(points.toArray(new Point[points.size()])); // odświeżenie listy
                drawingPath = false;
                repaint();
            }
        });
        // - przycisk
        JButton jb = new JButton("Dodaj losowy punkt");
        jb.setPreferredSize(new Dimension(30, 30));
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randPoint();
            }
        });

        // - ustaw startowy
        JButton jStart = new JButton("Oznacz jako startowy");
        jStart.setSize(new Dimension(30, 30));
        jStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (list.getSelectedValue() != null) {
                    Point selectedPoint = list.getSelectedValue();
                    for (Point point : points)
                        if (point.isFirst) {
                            point.isFirst = false;
                            break;
                        }
                    selectedPoint.isFirst = true;
                    System.out.println("asdsad");
                    drawingPath = false;
                    list.setListData(points.toArray(new Point[points.size()])); // odświeżenie listy
                    repaint();
                }
            }
        });


        // - komiwojazer
        JButton komiwojazer = new JButton("Rozwiąż problem");
        komiwojazer.setSize(new Dimension(30, 30));
        komiwojazer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i;
                for(i=0;i<points.size();i++)
                    if(points.get(i).isFirst) {
                        System.out.println(i+" "+points.get(i).id);
                        break;
                    }
                tsp2(points,i);
                minDist = Komiwojazer2.minDistance;
                bestPath = Komiwojazer2.bestPath;
                System.out.println(minDist);
                System.out.println(bestPath);
                drawingPath = true;
                repaint();
            }
        });



        // - suwak
        JSlider js = new JSlider(JSlider.HORIZONTAL, 5, 15, 10);
        js.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = js.getValue();
                skalaUkladu = value;
                System.out.println("Slider value changed to " + value);
                for (int i = 0; i < points.size(); i++) {
                    if(Math.abs(points.get(i).getxCoord())>value || Math.abs(points.get(i).getyCoord())>value)
                    {
                        points.remove(i);
                        for(int j=0; j<points.size();j++)
                            points.get(j).id = j;
                    }
                }
                drawingPath = false;
                repaint();
                list.setListData(points.toArray(new Point[points.size()])); // odświeżenie listy
            }
        });

        // - baza save
        JButton save = new JButton("Zapisz rozwiązanie");
        save.setSize(new Dimension(30, 30));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(drawingPath) {
                    String tekst = JOptionPane.showInputDialog(null, "Wpisz tekst:");
                    if (tekst != null) {
                        System.out.println("Wpisany tekst: " + tekst);
                        try {
                            int last = JDBC.get();
                            JDBC.save("INSERT INTO zestaw VALUES("+last+", '" + tekst + "',SYSDATE)");
                            for (int i = 0; i < points.size(); i++) {
                                if (points.get(i).isFirst)
                                    JDBC.save("INSERT INTO punkt VALUES(punktyID.nextval,"+last+", " +
                                            points.get(i).getxCoord() + "," + points.get(i).getyCoord() + ", 1)");
                                else
                                    JDBC.save("INSERT INTO punkt VALUES(punktyID.nextval,"+last+", " +
                                            points.get(i).getxCoord() + "," + points.get(i).getyCoord() + ", 0)");
                            }
                            int sciezka = JDBC.getLastPathID();
                            JDBC.save("INSERT INTO sciezka VALUES("+sciezka+","+last+")");
                            JDBC.savePath(bestPath,sciezka);
                            zestawy = JDBC.getData();
                            comboBox.setModel(new DefaultComboBoxModel<>(zestawy));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "Najpierw należy wyznaczyć ścieżkę!");
            }
        });

        // - baza load
        JButton load = new JButton("Wczytaj rozwiązanie");
        load.setSize(new Dimension(30, 30));
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> elements = new ArrayList<>();


                try {
                    points.clear();
                    Vector<Integer> kolejnosc = new Vector<Integer>();
                    if(zestaw==null)
                        zestaw=comboBox.getItemAt(0);
                    ArrayList<JDBC.Point> loaded = JDBC.load(kolejnosc,zestaw, AXIS_GAP);
                    for(int i =0; i<loaded.size();i++ ) {
                        Point p = new Point(i, loaded.get(i).x, loaded.get(i).y);
                        if (loaded.get(i).isFirst)
                            p.isFirst = true;
                        points.add(p);
                    }
                    bestPath.clear();
                    for(Integer el:kolejnosc){
                        bestPath.add(points.get(el));
                    }
                    minDist=0;
                    for(int i=0;i<bestPath.size()-1;i++)
                        minDist+=bestPath.get(i).dist(bestPath.get(i+1));
                    minDist+=bestPath.get(bestPath.size()-1).dist(bestPath.get(0));
                    drawingPath=true;
                    repaint();
                    list.setListData(points.toArray(new Point[points.size()])); // odświeżenie listy
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        //lista rozwijana
        zestawy = JDBC.getData();
        comboBox = new JComboBox<>(zestawy);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Wybrano opcję: " + comboBox.getSelectedItem());
                zestaw = (String) comboBox.getSelectedItem();
            }
        });

        JButton info = new JButton("O programie");
        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Najpierw należy wyznaczyć ścieżkę!");
            }
        });

        this.setLayout(null);
        jb.setBounds(20, BORDER_GAP_TOP/2-15, 150, 30);
        addButton.setBounds(180, BORDER_GAP_TOP/2-15, 100, 30);
        modifyButton.setBounds(290, BORDER_GAP_TOP/2-15, 100, 30);
        deleteButton.setBounds(400, BORDER_GAP_TOP/2-15, 100, 30);
        jStart.setBounds(510, BORDER_GAP_TOP/2-15, 170, 30);
        komiwojazer.setBounds(690, BORDER_GAP_TOP/2-15, 150, 30);
        scrollPane.setBounds(UKLWIDTH + BORDER_GAP_LEFT + 30, BORDER_GAP_TOP, 200, 364);
        js.setBounds(BORDER_GAP_LEFT + UKLWIDTH/2-200,  BORDER_GAP_TOP+UKLHEIGHT+10,400, 30);
        save.setBounds(UKLWIDTH + BORDER_GAP_LEFT + 30, BORDER_GAP_TOP + 364 + 10, 200, 30);
        load.setBounds(UKLWIDTH + BORDER_GAP_LEFT + 30, BORDER_GAP_TOP + 364 + 50, 200, 30);
        comboBox.setBounds(UKLWIDTH + BORDER_GAP_LEFT + 30, BORDER_GAP_TOP + 364 + 90, 200, 30);
        this.add(jb);
        this.add(js);
        this.add(komiwojazer);
        this.add(modifyButton);
        this.add(deleteButton);
        this.add(addButton);
        this.add(jStart);
        this.add(save);
        this.add(load);
        this.add(comboBox);
        this.add(scrollPane);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() - UKLWIDTH / 2 - BORDER_GAP_LEFT;
                int y = UKLHEIGHT / 2 - e.getY() + BORDER_GAP_TOP;

                double xCoord = (double)x/AXIS_GAP;
                xCoord = Math.round(xCoord*2);
                xCoord=xCoord/2;
                double yCoord = (double)y/AXIS_GAP;
                yCoord = Math.round(yCoord*2);
                yCoord=yCoord/2;
                if (Math.abs(xCoord) > skalaUkladu || Math.abs(yCoord) > skalaUkladu) {
                    return;
                }
                for(Point point: points) {
                    System.out.println("X " + point.xCoord + "  Y: " + point.yCoord);
                    if (point.xCoord == xCoord && point.yCoord == yCoord) {
                        System.out.println("Punkt istnieje");
                        list.setListData(points.toArray(new Point[points.size()])); // odświeżenie listy
                        repaint();
                        return;
                    }
                }
                points.add(new Point(points.size()+1,xCoord * AXIS_GAP, yCoord * AXIS_GAP));
                ilPunktow++;
                    System.out.println("Punkt "+ilPunktow+" [X " + xCoord + " Y "+ yCoord+"]");
                list.setListData(points.toArray(new Point[points.size()])); // odświeżenie listy
                drawingPath = false;
                repaint();

            }
        });
    }
    public void drawPath(Graphics g)
    {
        if(!drawingPath)
            return;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(3));
        int n = bestPath.size()-1;
        for(int i=0;i<n;i++)
        {
            g2d.drawLine((int) (bestPath.get(i).getxCoord()*AXIS_GAP) + UKLWIDTH / 2 + BORDER_GAP_LEFT,
                    UKLHEIGHT / 2 + BORDER_GAP_TOP - (int) (bestPath.get(i).getyCoord()*AXIS_GAP),
                    (int) (bestPath.get(i+1).getxCoord()*AXIS_GAP) + UKLWIDTH / 2 + BORDER_GAP_LEFT,
                    UKLHEIGHT / 2 + BORDER_GAP_TOP - (int) (bestPath.get(i+1).getyCoord()*AXIS_GAP));
        }
        g2d.drawLine((int) (bestPath.get(n).getxCoord()*AXIS_GAP) + UKLWIDTH / 2 + BORDER_GAP_LEFT,
                UKLHEIGHT / 2 + BORDER_GAP_TOP - (int) (bestPath.get(n).getyCoord()*AXIS_GAP),
                (int) (bestPath.get(0).getxCoord()*AXIS_GAP) + UKLWIDTH / 2 + BORDER_GAP_LEFT,
                UKLHEIGHT / 2 + BORDER_GAP_TOP - (int) (bestPath.get(0).getyCoord()*AXIS_GAP));
        g.setColor(Color.BLUE);

        for (int index = 0; index < bestPath.size(); index++) {
            Point point = bestPath.get(index);
            double x = point.getxCoord()*AXIS_GAP + UKLWIDTH / 2 + BORDER_GAP_LEFT;
            double y = UKLHEIGHT / 2 + BORDER_GAP_TOP - point.getyCoord()*AXIS_GAP;
            g.drawString(Integer.toString(index), (int) (x + POINT_RADIUS + 5), (int) (y + POINT_RADIUS / 2));
        }
        g.setColor(Color.BLACK);
        g.drawString("Dł. trasy: "+Math.round(minDist*100)/100.0,UKLWIDTH + BORDER_GAP_LEFT + 30, BORDER_GAP_TOP + 364 + 140);

    }
    public void randPoint()
    {
        Random rand = new Random();
        double x =  (double) (-skalaUkladu + Math.round(rand.nextDouble(skalaUkladu*4))/2.0);
        double y =  (double) (-skalaUkladu + Math.round(rand.nextDouble(skalaUkladu*4))/2.0);

        for(Point point: points)
            if(point.xCoord==x && point.yCoord==y){
                System.out.println("Punkt istnieje");
                repaint();
                return;
            }
        points.add(new Point(points.size(),x * AXIS_GAP, y * AXIS_GAP));
        ilPunktow++;
        System.out.println("Punkt "+ilPunktow+" [X " + x + " Y "+ y+"]");
        drawingPath = false;
        repaint();
        list.setListData(points.toArray(new Point[points.size()])); // odświeżenie listy
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Wyświetlenie informacji o punktach
        System.out.println("Lista punktów:");
        for (Point point : points) {
            System.out.println(point);
        }
        System.out.println();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        AXIS_GAP = UKLHEIGHT/skalaUkladu/2;
        // Wyświetlenie osi X i Y
        g.setColor(Color.BLACK);
        g.drawLine(BORDER_GAP_LEFT, UKLHEIGHT / 2 + BORDER_GAP_TOP, UKLWIDTH+BORDER_GAP_LEFT, UKLHEIGHT / 2 + BORDER_GAP_TOP); // Oś X
        g.drawLine(UKLWIDTH / 2 + BORDER_GAP_LEFT, BORDER_GAP_TOP, UKLWIDTH / 2 + BORDER_GAP_LEFT, UKLHEIGHT + BORDER_GAP_TOP); // Oś Y

        // Wyświetlenie podziałek osi X
        for (int i = -skalaUkladu; i <= skalaUkladu; i++) {
            if (i != 0) {
                int x = i * AXIS_GAP + UKLWIDTH / 2 + BORDER_GAP_LEFT;
                g.drawLine(x, UKLHEIGHT / 2 - 5 + BORDER_GAP_TOP, x, UKLHEIGHT / 2 + 5 + BORDER_GAP_TOP);
                g.drawString(Integer.toString(i), x - 3, UKLHEIGHT / 2 + 20 + BORDER_GAP_TOP);
            }
        }

        // Wyświetlenie podziałek osi Y
        for (int i = -skalaUkladu; i <= skalaUkladu; i++) {
            if (i != 0) {
                int y = -i * AXIS_GAP + UKLHEIGHT / 2 + BORDER_GAP_TOP;
                g.drawLine(UKLWIDTH / 2 - 5 + BORDER_GAP_LEFT, y, UKLWIDTH / 2 + 5 + BORDER_GAP_LEFT, y);
                g.drawString(Integer.toString(i), UKLWIDTH / 2 + 20 + BORDER_GAP_LEFT, y + 3);
            }
        }

        drawPath(g);
        // Wyświetlenie zapisanych punktów
        g.setColor(Color.BLUE);
        int a;
        for (int index = 0; index < points.size(); index++) {
            Point point = points.get(index);
            if (point.isClicked) {
                g.setColor(Color.RED);
                point.isClicked=false;
            }
            else if (point.isFirst) {
                g.setColor(Color.magenta);
                point.isClicked=false;
            }
            else
                g.setColor(Color.BLUE);

            double x = point.getxCoord()*AXIS_GAP + UKLWIDTH / 2 + BORDER_GAP_LEFT;
            double y = UKLHEIGHT / 2 + BORDER_GAP_TOP - point.getyCoord()*AXIS_GAP;
            g.fillOval((int) (x - POINT_RADIUS), (int) (y - POINT_RADIUS), 2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Problem Komiwojażera");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Info");
        JMenuItem oProgramie = new JMenuItem("O Programie");
        oProgramie.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Projekt zespołowy Grupa 3ID12B\n" +
                    "Temat: Rozwiązywanie Problemu Komiwojażera\n"+
                    "Autorzy: Filip Tosnowiec, Szymon Śmiglarski, Mariusz Śmistek");
        });
        JMenuItem instrukcja = new JMenuItem("Instrukcja");
        instrukcja.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Dodaj losowy punkt - dodaje losowy punkt w aktualnie ustwionym przedziale układu\n" +
                    "Dodaj - po kliknięciu wyskakują okna dialogowe, w którym wprowadzamy koordynaty x i y\n" +
                    "Modyfikuj - po zaznaczeniu punktu na liście można modyfikować jego koordynaty x i y\n" +
                    "Usuń - po zaznaczeniu punktu na liście można go usunąc\n" +
                    "Oznacz jako startowy - po zaznaczeniu punktu na liście można zaznaczyć go jako startowy, od niego zaczęte bedzie liczone rozwiązanie\n" +
                    "Rozwiąż problem - po oznaczeniu punktu jako startowy można rozwiązać problem Komiwojażera. Po wyliczeniu otrzymujemy ścieżkę i wyliczoną długość trasy\n" +
                    "Lista - zawiera informacje o wszystkich punktach\n" +
                    "Zapisz rozwiązanie - pobiera dane o punktach i ścieżce, zapisuje je do bazy danych pod podaną nazwą\n" +
                    "Wczytaj rozwiązanie - pobiera dane o punktach i ścieżce z bazy danych, wprowadza je do programu i wyświetla\n" +
                    "Suwak - pozwala na zmianę przedziału układu, punkty poza przedziałem są usuwane");
        });
        fileMenu.add(oProgramie);
        fileMenu.add(instrukcja);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        // - Panel
        HelloApplication panel = null;
        try {
            panel = new HelloApplication();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        frame.add(panel);

        frame.setVisible(true);
    }

    public class Point {
        public int id;
        private double x;
        private double y;
        private double xCoord;
        private double yCoord;
        private int nr = 0;
        public boolean isFirst = false;
        public boolean isClicked = false;

        public Point(int id,double x, double y) {
            this.id=id;
            this.x = x;
            this.y = y;
            this.xCoord = Math.round(x/AXIS_GAP*2)/2.0;
            this.yCoord = Math.round(y/AXIS_GAP*2)/2.0;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
        public double getxCoord() {
            return xCoord;
        }

        public double getyCoord() {
            return yCoord;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }

        public void setxCoord(double xCoord) {
            this.xCoord = xCoord;
            this.x = xCoord*AXIS_GAP;
        }

        public void setyCoord(double yCoord) {
            this.yCoord = yCoord;
            this.y = yCoord*AXIS_GAP;
        }

        public double dist(Point p) {
            double dx = this.getxCoord() - p.getxCoord();
            double dy = this.getyCoord() - p.getyCoord();
            return Math.sqrt(dx * dx + dy * dy);
        }

        public String toString() {
            if(nr == 0) {
                if (isFirst) return "✪ Punkt " + id + ": " + xCoord + " " + yCoord;
                else return "Punkt " + id + ": " + xCoord + " " + yCoord;
            }
            else {
                if (isFirst) return "Punkt Startowy " + id + ": " + xCoord + " " + yCoord+"["+nr+"]";
                else return "Punkt " + id + ": " + xCoord + " " + yCoord+"["+nr+"]";
            }
        }
    }
}
