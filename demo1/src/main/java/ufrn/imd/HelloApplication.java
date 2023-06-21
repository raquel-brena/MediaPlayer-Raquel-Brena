package ufrn.imd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ufrn.imd.controller.ControllerLogin;
import ufrn.imd.controller.ControllerNovaPlaylist;
import ufrn.imd.controller.ControllerPlayer;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage stage;
    private static Scene loginScene;
    private static Scene playerScene;
    private static Scene playlistScene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        FXMLLoader fxmlLoaderPlaylist = new FXMLLoader(HelloApplication.class.getResource("controller/panel-playlist.fxml"));
        Parent playlistRoot = fxmlLoaderPlaylist.load();
        playlistScene = new Scene(playlistRoot);

        FXMLLoader fxmlLoaderPlayer = new FXMLLoader(HelloApplication.class.getResource("controller/panel-player.fxml"));
        Parent playerRoot = fxmlLoaderPlayer.load();
        playerScene = new Scene(playerRoot);

        FXMLLoader fxmlLoaderLogin = new FXMLLoader(HelloApplication.class.getResource("controller/panel-login.fxml"));
        Parent loginRoot = fxmlLoaderLogin.load();
        loginScene = new Scene(loginRoot);

        ControllerLogin controllerLogin = fxmlLoaderLogin.getController();
        ControllerPlayer controllerPlayer = fxmlLoaderPlayer.getController();
        ControllerNovaPlaylist controllerNovaPlaylist = fxmlLoaderPlaylist.getController();

        controllerLogin.setControllerPlayer(controllerPlayer);
        controllerPlayer.setControllerPlaylist(controllerNovaPlaylist);

        stage.setTitle("Media Player");
        stage.setScene(loginScene);
        stage.show();
    }

    public static void changeScreen(String screen, String nomeUsuario) {
        switch (screen) {
            case "login":
                stage.setTitle("Media Player");
                stage.setScene(loginScene);
                break;
            case "player":
                stage.setScene(playerScene);
                stage.setTitle("Media Player de " + nomeUsuario);
                break;
            case "playlist":
                stage.setScene(playlistScene);
                break;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
