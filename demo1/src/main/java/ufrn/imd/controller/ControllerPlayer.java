package ufrn.imd.controller;

import ufrn.imd.HelloApplication;
import ufrn.imd.entities.Musica;
import ufrn.imd.entities.Usuario;
import ufrn.imd.entities.UsuarioComum;
import ufrn.imd.entities.UsuarioVip;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Controlador para a tela do player.
 */
public class ControllerPlayer {

    @FXML
    private Button playButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button previousButton;

    @FXML
    private Slider volumeSlider;

    @FXML
    private ProgressBar songProgressBar;

    @FXML
    private Label nameMusicLabel;

    @FXML
    private Label nameArtistLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private MenuItem mnAddMusica;
    @FXML
    private MenuItem mnLogout;

    @FXML
    private ListView<Musica> listViewMusicas;

    private Stage dialogStage;

    private File directory;
    private File[] files;
    private List<File> fileSongs;
    private List<Musica> SONGS_ONLINE;

    private int songNumber;
    private Timer time;
    private TimerTask task;
    private boolean running;
    private Media media;
    private MediaPlayer mediaPlayer;
    private static Usuario USUARIO_ONLINE;
    private ObservableList<Musica> MusicaObservableList;

    /**
     * Construtor padrão.
     */
    public ControllerPlayer() {
        USUARIO_ONLINE = new UsuarioComum();
        SONGS_ONLINE = new ArrayList<>();
        MusicaObservableList = FXCollections.observableArrayList();
    }

    /**
     * Define o usuário online.
     *
     * @param usuario O usuário online.
     */
    public void setUsuarioOnline(Usuario usuario) {
        this.USUARIO_ONLINE = usuario;
        this.SONGS_ONLINE = USUARIO_ONLINE.getDirectory().getAllSongs();
        atualizarListaMusica();

    }

    @FXML
    public void logout (){
        HelloApplication.changeScreen("login","");
        this.USUARIO_ONLINE = null;
        //this.dialogStage.close();

    }
    /**
     * Atualiza a lista de música da tela
     *
     */
    public void atualizarListaMusica(){
        MusicaObservableList = FXCollections.observableArrayList();

        for (Musica musica : SONGS_ONLINE) {
            MusicaObservableList.add(musica);
        }

        listViewMusicas.setItems(MusicaObservableList);

        listViewMusicas.setCellFactory(param -> new ListCell<Musica>() {
            @Override
            protected void updateItem(Musica musica, boolean empty) {
                super.updateItem(musica, empty);

                if (empty || musica == null) {
                    setText(null);
                } else {
                    setText(musica.getTitulo() + " - " + musica.getArtista());
                }
            }
        });
    }
    /**
     * Exibe o painel de adição de nova música.
     *
     * @throws IOException Exceção lançada se houver um erro ao carregar o arquivo FXML do painel de adição de nova música.
     */
    @FXML
    public void showFXMLPanelNovaMusica() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controllerCadastros.class.getResource("panel-nova-musica.fxml"));
            AnchorPane cadastroMusica = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cadastrar Nova Música");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(cadastroMusica);
            dialogStage.setScene(scene);

            PanelNovaMusicaController controllerMusica = loader.getController();
            controllerMusica.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controllerMusica.isButtonConfirmarClicked()) {
                Musica novaMusica = controllerMusica.getMusicaNova();
                USUARIO_ONLINE.getDirectory().adicionarMusicaFile(novaMusica);
                System.out.println("Música adicionada: " + novaMusica.getTitulo());
                atualizarListaMusica();
            }
        } catch (IOException e) {
            // Trate a exceção adequadamente, exibindo uma mensagem de erro ou realizando outras ações necessárias
            System.out.println("Ocorreu um erro ao carregar o painel de nova música.");
            e.printStackTrace();
        }
    }

    /**
     * Converte o usuário comum em usuário VIP.
     * Se o usuário online for do tipo {@UsuarioComum},
     * ele será substituído por um objeto do tipo {@UsuarioVip}.
     * O usuário comum será removido do DAO de usuários e substituído pelo usuário VIP.
     */
    public void tornarSeVip() {
        if (USUARIO_ONLINE instanceof UsuarioComum) {
            Usuario usuarioVip = new UsuarioVip();
            usuarioVip.setNome(USUARIO_ONLINE.getNome());
            usuarioVip.setEmail(USUARIO_ONLINE.getEmail());
            usuarioVip.setSenha(USUARIO_ONLINE.getSenha());
            usuarioVip.setAdmin(USUARIO_ONLINE.isAdmin());

            USUARIO_ONLINE = usuarioVip;
        }
    }

    /**
     * Define o estágio de diálogo.
     *
     * @param dialogStage O estágio de diálogo.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
