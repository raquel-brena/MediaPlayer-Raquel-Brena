module ufrn.imd.mediaplayer2 {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
        requires javafx.web;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
            requires net.synedra.validatorfx;
                requires org.kordamp.bootstrapfx.core;
            requires eu.hansolo.tilesfx;
            requires com.almasb.fxgl.all;
    
    opens ufrn.imd.mediaplayer2 to javafx.fxml;
    exports ufrn.imd.mediaplayer2;
}