package gui.pl.demo;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import static gui.pl.demo.Komiwojazer2.tsp2;

public class HelloApplication extends JPanel implements  ActionListener {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 800;
    private static final int UKLWIDTH = 600;
    private static final int UKLHEIGHT = 600;
    private static final int BORDER_GAP_TOP = 150;
    private static final int BORDER_GAP_LEFT = 10;
    private static final int POINT_RADIUS = 3;
    public int AXIS_GAP;
    public ArrayList<HelloApplication.Point> bestPath;
    public double minDist;
    public boolean drawingPath=false;
    public ArrayList<Point> points = new ArrayList<Point>();
    public JList<Point> list;
    private int ilPunktow = 0;
    public int skalaUkladu = 10;
    public HelloApplication() {

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
        jb.setSize(new Dimension(30, 30));
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randPoint();
            }
        });

        // - komiwojazer
        JButton komiwojazer = new JButton("Rozwiąż problem");
        komiwojazer.setSize(new Dimension(30, 30));
        komiwojazer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tsp2(points,1);
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
                    }
                }
                drawingPath = false;
                repaint();
                list.setListData(points.toArray(new Point[points.size()])); // odświeżenie listy
            }
        });
        GridBagConstraints c = new GridBagConstraints();

        this.add(jb);
        this.add(js);
        this.add(komiwojazer);
        this.add(modifyButton);
        this.add(deleteButton);
        this.add(addButton);
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
        for(int i=0;i<n-1;i++)
        {
            g2d.drawLine((int) bestPath.get(i).getxCoord()*AXIS_GAP + UKLWIDTH / 2 + BORDER_GAP_LEFT,
                    UKLHEIGHT / 2 + BORDER_GAP_TOP - (int) bestPath.get(i).getyCoord()*AXIS_GAP,
                    (int) bestPath.get(i+1).getxCoord()*AXIS_GAP + UKLWIDTH / 2 + BORDER_GAP_LEFT,
                    UKLHEIGHT / 2 + BORDER_GAP_TOP - (int) bestPath.get(i+1).getyCoord()*AXIS_GAP);
        }
        g2d.drawLine((int) bestPath.get(n).getxCoord()*AXIS_GAP + UKLWIDTH / 2 + BORDER_GAP_LEFT,
                UKLHEIGHT / 2 + BORDER_GAP_TOP - (int) bestPath.get(n).getyCoord()*AXIS_GAP,
                (int) bestPath.get(0).getxCoord()*AXIS_GAP + UKLWIDTH / 2 + BORDER_GAP_LEFT,
                UKLHEIGHT / 2 + BORDER_GAP_TOP - (int) bestPath.get(0).getyCoord()*AXIS_GAP);
    }
    public void randPoint()
    {
        Random rand = new Random();
        double x =  (float) (-skalaUkladu + rand.nextInt(skalaUkladu*2));
        double y =  (float) (-skalaUkladu + rand.nextInt(skalaUkladu*2));

        for(Point point: points)
            if(point.xCoord==x && point.yCoord==y){
                System.out.println("Punkt istnieje");
                repaint();
                return;
            }
        points.add(new Point(points.size()+1,x * AXIS_GAP, y * AXIS_GAP));
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

        // Wyświetlenie punktu (0,0)
        g.setColor(Color.RED);
        g.fillOval(UKLWIDTH / 2 + BORDER_GAP_LEFT - POINT_RADIUS, UKLHEIGHT / 2 + BORDER_GAP_TOP - POINT_RADIUS, 2 * POINT_RADIUS, 2 * POINT_RADIUS);

        drawPath(g);
        // Wyświetlenie zapisanych punktów
        g.setColor(Color.BLUE);
        int a;
        for (Point point : points) {
            if (point.isClicked) {
                g.setColor(Color.RED);
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

        // - Panel
        HelloApplication panel = new HelloApplication();

        frame.add(panel);

        frame.setVisible(true);
    }

    public class Point {
        private int id;
        private double x;
        private double y;
        private double xCoord;
        private double yCoord;
        public boolean isFirst = false;
        public boolean isClicked = false;

        public Point(int id,double x, double y) {
            this.id=id;
            this.x = x;
            this.y = y;
            this.xCoord = x/AXIS_GAP;
            this.yCoord = y/AXIS_GAP;
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
            return "Punkt "+id+": "+xCoord+" "+yCoord;
        }
    }
}
