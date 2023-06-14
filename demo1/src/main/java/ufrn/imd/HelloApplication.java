package ufrn.imd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ufrn.imd.controller.ControllerLogin;
import ufrn.imd.controller.ControllerPlayer;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage stage;
    private static Scene loginScene;
    private static Scene playerScene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        FXMLLoader fxmlLoaderPlayer = new FXMLLoader(HelloApplication.class.getResource("controller/panel-player.fxml"));
        playerScene = new Scene(fxmlLoaderPlayer.load());

        FXMLLoader fxmlLoaderLogin = new FXMLLoader(HelloApplication.class.getResource("controller/panel-login.fxml"));
        loginScene = new Scene(fxmlLoaderLogin.load());

        ControllerLogin controllerLogin = fxmlLoaderLogin.getController();
        ControllerPlayer controllerPlayer = fxmlLoaderPlayer.getController();
        controllerLogin.setControllerPlayer(controllerPlayer);

        stage.setTitle("Media Player");
        stage.setScene(loginScene);

        stage.show();
    }

    public static void changeScreen (String screen, String nomeUsuario)  {
        switch (screen) {
            case "login" -> {
                stage.setTitle("Media Player");
                stage.setScene(loginScene);
            }
            case "player" -> {
                stage.setScene(playerScene);
                stage.setTitle("Media Player de " + nomeUsuario);
            }
        }
    }
    public static void main(String[] args) {
        launch();
    }
}