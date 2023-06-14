module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
    requires java.desktop;
    requires javafx.media;

    opens ufrn.imd.controller to javafx.fxml;
    exports ufrn.imd.controller;
    exports ufrn.imd;
    opens ufrn.imd to javafx.fxml;
}