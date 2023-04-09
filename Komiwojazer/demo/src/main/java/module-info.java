module gui.pl.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens gui.pl.demo to javafx.fxml;
    exports gui.pl.demo;
}