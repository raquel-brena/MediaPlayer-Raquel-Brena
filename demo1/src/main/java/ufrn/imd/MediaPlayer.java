package ufrn.imd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ufrn.imd.controller.ControllerAbout;
import ufrn.imd.controller.ControllerLogin;
import ufrn.imd.controller.ControllerNovaPlaylist;
import ufrn.imd.controller.ControllerPlayer;

import java.io.IOException;

public class MediaPlayer extends Application {
    private static Stage stage;
    private static Scene loginScene;
    private static Scene playerScene;
    private static Scene playerAdminScene;
    private static Scene playlistScene;

    private static Scene aboutScene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        FXMLLoader fxmlLoaderPlaylist = new FXMLLoader(MediaPlayer.class.getResource("controller/panel-playlist.fxml"));
        Parent playlistRoot = fxmlLoaderPlaylist.load();
        playlistScene = new Scene(playlistRoot);

        FXMLLoader fxmlLoaderPlayer = new FXMLLoader(MediaPlayer.class.getResource("controller/panel-player.fxml"));
        Parent playerRoot = fxmlLoaderPlayer.load();
        playerScene = new Scene(playerRoot);

        FXMLLoader fxmlLoaderLogin = new FXMLLoader(MediaPlayer.class.getResource("controller/panel-login.fxml"));
        Parent loginRoot = fxmlLoaderLogin.load();
        loginScene = new Scene(loginRoot);

        FXMLLoader fxmlLoaderAbout= new FXMLLoader(MediaPlayer.class.getResource("controller/panel-about.fxml"));
        Parent aboutRoot = fxmlLoaderAbout.load();
        aboutScene = new Scene(aboutRoot);

        ControllerLogin controllerLogin = fxmlLoaderLogin.getController();
        ControllerPlayer controllerPlayer = fxmlLoaderPlayer.getController();
        ControllerNovaPlaylist controllerNovaPlaylist = fxmlLoaderPlaylist.getController();
        ControllerAbout controllerAbout = fxmlLoaderAbout.getController();


        controllerLogin.setControllerPlayer(controllerPlayer);
        controllerPlayer.setControllerPlaylist(controllerNovaPlaylist);
        controllerPlayer.setControllerAbout(controllerAbout);

        stage.setTitle("Media Player");
        stage.setScene(loginScene);
        stage.setResizable(false);
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
            case "about":
                stage.setScene(aboutScene);
                break;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
