package gui.pl.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class JDBC {
    public static ArrayList<HelloApplication.Point> bestPath = new ArrayList<HelloApplication.Point>();
    public static Vector<Option> options = new Vector<Option>();
    public static Vector<String> getData() throws SQLException {
        String host = "127.0.0.1";
        int port = 1521;
        String database = "xe";
        String user = "system";
        String password = "1234";
        String url = "jdbc:oracle:thin:" + user + "/" + password + "@" + host + ":" + port + ":" + database;
        try(
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
        ) {
            String query = "select zestaw.id as Zid, nazwa, sciezka.id as Sid, data from zestaw " +
                    "INNER JOIN sciezka on sciezka.IDZestawu=zestaw.id order by zestaw.id,sciezka.id";
            ResultSet resultSet = statement.executeQuery(query);
            Vector<String> zestawy = new Vector<String>();
            while (resultSet.next()) {
                zestawy.add(resultSet.getString("nazwa")
                        +" "+resultSet.getString("Sid")
                        +" "+resultSet.getString("data"));
                options.add(new Option(resultSet.getInt("Zid"),resultSet.getInt("Sid"),zestawy.lastElement()));
            }
            return zestawy;
        }
    }
    public static void save(String query) throws SQLException {
        String host = "127.0.0.1";
        int port = 1521;
        String database = "xe";
        String user = "system";
        String password = "1234";
        String url = "jdbc:oracle:thin:" + user + "/" + password + "@" + host + ":" + port + ":" + database;
        try(
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(query);
        }
    }
    public static void savePath(ArrayList<HelloApplication.Point> bestPath, int sciezka) throws SQLException {
        String host = "127.0.0.1";
        int port = 1521;
        String database = "xe";
        String user = "system";
        String password = "1234";
        String url = "jdbc:oracle:thin:" + user + "/" + password + "@" + host + ":" + port + ":" + database;
        try(
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
        ) {
            for(int i=0;i<bestPath.size();i++) {
                statement.executeUpdate("INSERT INTO kolejnosc VALUES(" + sciezka + "," + bestPath.get(i).id + "," + i + ")");
                System.out.println(i);
            }
        }
    }

    public static ArrayList<Point> load(Vector<Integer> kolejnosc, String zestaw, int AXIS_GAP) throws SQLException {
        String host = "127.0.0.1";
        int port = 1521;
        String database = "xe";
        String user = "system";
        String password = "1234";
        String url = "jdbc:oracle:thin:" + user + "/" + password + "@" + host + ":" + port + ":" + database;

        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
        ) {
            int Zid=-1, Sid=-1;
            for (Option element : options) {
                if(element.nazwa.equals(zestaw))
                {
                    Zid=element.idZestawu;
                    Sid=element.idSciezki;
                    break;
                }
            }
            ResultSet resultSet = statement.executeQuery("select coordX, coordY, czyStartowy from punkt where idzestawu ="+Zid);
            ArrayList<Point> points = new ArrayList<>();
            int i=0;
            while (resultSet.next()) {
                    Point p = new Point(i, resultSet.getDouble("coordX"),
                            resultSet.getDouble("coordY"), AXIS_GAP);
                    if(resultSet.getInt("czyStartowy")==1)
                        p.setFirst(true);
                    points.add(p);
                    i++;
            }
            resultSet = statement.executeQuery("select * from kolejnosc where IDSciezki ="+Sid+" order by nrodwiedzin");
            while (resultSet.next()){
                kolejnosc.add(resultSet.getInt("idpunktu"));
            }
            return points;
        }
    }
    public static int get() throws SQLException {
        String host = "127.0.0.1";
        int port = 1521;
        String database = "xe";
        String user = "system";
        String password = "1234";
        String url = "jdbc:oracle:thin:" + user + "/" + password + "@" + host + ":" + port + ":" + database;

        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
        ) {
            int data = 0;
            ResultSet resultSet = statement.executeQuery("select zestawyID.nextval from dual");
            ArrayList<Point> points = new ArrayList<>();
            while (resultSet.next()) {
                data = resultSet.getInt("nextval");
            }
            return data;
        }
    }
    public static int getLastPathID() throws SQLException {
        String host = "127.0.0.1";
        int port = 1521;
        String database = "xe";
        String user = "system";
        String password = "1234";
        String url = "jdbc:oracle:thin:" + user + "/" + password + "@" + host + ":" + port + ":" + database;

        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
        ) {
            int data = 0;
            ResultSet resultSet = statement.executeQuery("select sciezkaID.nextval from dual");
            ArrayList<Point> points = new ArrayList<>();
            while (resultSet.next()) {
                data = resultSet.getInt("nextval");
            }
            return data;
        }
    }


    public static class Point {
        public int id;
        public double x;
        public double y;
        public boolean isFirst = false;

        public Point(int id,double x, double y, int AXIS_GAP) {
            this.id=id;
            this.x = x*AXIS_GAP;
            this.y = y*AXIS_GAP;
        }

        public void setFirst(boolean first) {
            isFirst = first;
        }
    }
}

