package ufrn.imd.controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ufrn.imd.HelloApplication;
import ufrn.imd.entities.*;
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
public class ControllerPlayer  {

    @FXML
    private ToggleButton playButton;
    @FXML
    private ToggleButton diretorioButton;

    @FXML
    private Slider volumeSlider;

    @FXML
    private ProgressBar songProgressBar;

    @FXML
    private Label nameMusicLabel;

    @FXML Label durationSong;

    @FXML
    private ImageView userImage;
    @FXML
    private Label nameArtistLabel;

    @FXML
    private Label nameDiretorio;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label vipStatusLabel;

    @FXML
    private MenuItem mnAddMusica;
    @FXML
    private MenuItem mnLogout;
    @FXML
    private MenuItem mnVirarVIP;
    @FXML
    private MenuItem mnNewPlaylist;



    @FXML
    private ListView<Musica> listViewMusicas;
    @FXML
    private ListView<Playlist> listViewPlaylist;
    private Stage dialogStage;
    private List<Musica> SONGS_ONLINE;
    private List<Playlist> PLAYLISTS_ONLINE;
    private Timer timer;
    private TimerTask task;
    private boolean running;
    private Media media;
    private MediaPlayer mediaPlayer;
    private static Usuario USUARIO_ONLINE;
    private ObservableList<Musica> MusicaObservableList;
    private ObservableList<Playlist> PlaylistObservableList;
    private ControllerNovaPlaylist controllerNovaPlaylist;
    private List<Musica> filaReproducao;
    private int songNumber;
    private ControllerNovaPlaylist controllerPlaylist;

    /**
     * Construtor padrão.
     */
    public ControllerPlayer() {


    }
    public void inicializar(){
        USUARIO_ONLINE = new UsuarioComum();
        SONGS_ONLINE = new ArrayList<>();
        filaReproducao = new ArrayList<>();
        PlaylistObservableList = FXCollections.observableArrayList();
        MusicaObservableList = FXCollections.observableArrayList();
    }
    /**
     * Define o usuário online.
     *
     * @param usuario O usuário online.
     */
    public void setUsuarioOnline(Usuario usuario) {
        inicializar();
        nameDiretorio.setText("DIRETÓRIO");


        this.USUARIO_ONLINE = usuario;
        this.SONGS_ONLINE = USUARIO_ONLINE.getDirectory().getAllSongs();
        this.usernameLabel.setText("Usuario: " + USUARIO_ONLINE.getNome());

        for (Musica musica: SONGS_ONLINE){
            System.out.print("musica: " + musica.getTitulo());
        }
        if (USUARIO_ONLINE instanceof UsuarioVip){
            PLAYLISTS_ONLINE = ((UsuarioVip) usuario).getPlaylists();
            vipStatusLabel.setText("VIP ATIVO");
            mnNewPlaylist.setDisable(false);
            atualizarListaPlaylist();
        } else {
            vipStatusLabel.setText("VIP INATIVO");
           mnNewPlaylist.setDisable(true);
        }

        Image image = new Image("C:\\Users\\RB\\Desktop\\java\\mediaplyer\\demo1\\src\\main\\java\\ufrn\\imd\\extra\\png-user.png");
        userImage.setImage(image);

        volumeSlider.setValue(100);
        atualizarListaMusica(SONGS_ONLINE);
        songProgressBar.setStyle("-fx-accent: #00FF00;");
    }


    @FXML
    public void removeButton() {
        //warningLabel.setVisible(false);

        Musica musicaSelecionada = listViewMusicas.getSelectionModel().getSelectedItem();
        Playlist playlistSelecionada = listViewPlaylist.getSelectionModel().getSelectedItem();

        if (musicaSelecionada != null) {
            //if (musicaSelecionada.getFile() == mediaPlayer.getOnPlaying()){
           //     mediaPlayer.stop();
           // }
            USUARIO_ONLINE.getDirectory().excluirMusica(musicaSelecionada);
            SONGS_ONLINE = USUARIO_ONLINE.getDirectory().getAllSongs();
            atualizarListaMusica(SONGS_ONLINE);

        } else if (playlistSelecionada != null){
            //PlaylistObservableList.remove(playlistSelecionada);
            ((UsuarioVip)USUARIO_ONLINE).excluirPlaylist(playlistSelecionada);
            PLAYLISTS_ONLINE.remove(playlistSelecionada);

            exibirPlaylist();
        }

    }


    @FXML
    public void volume() {
        mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
    }

    @FXML
    public void dragProgressBar() {
        songProgressBar.setOnMouseDragged(event -> {
            double progress = event.getX() / songProgressBar.getWidth();
            mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(progress));
            //double tempoAtual = mediaPlayer.getCurrentTime().toMinutes()-mediaPlayer.getTotalDuration().multiply(progress).toMinutes();
            //durationSong.setText(String.valueOf(tempoAtual));
        });
    }

    public void playSong (int number) {
        if (mediaPlayer != null) {mediaPlayer.stop();}
            this.media = new Media(filaReproducao.get(number).getFile().toURI().toString());
            this.mediaPlayer = new MediaPlayer(media);
            this.mediaPlayer.play();
            volume();
            beginTimer();
            nameMusicLabel.setText(filaReproducao.get(number).getTitulo() + " - " + filaReproducao.get(number).getArtista());
            listViewMusicas.getSelectionModel().select(number);

            //double tempoAtual = mediaPlayer.getTotalDuration().toMinutes()-mediaPlayer.getCurrentTime().toMinutes();
            //durationSong.setText(String.valueOf(tempoAtual));

    }

    @FXML
    public void play() {
        Musica musicaSelecionada = listViewMusicas.getSelectionModel().getSelectedItem();
        if (playButton.isSelected()) {
            if (musicaSelecionada != null) {
                int numberMusica = listViewMusicas.getSelectionModel().getSelectedIndex();
                playSong(numberMusica);
            } else {
                playSong(songNumber);
            }
        }
        if(mediaPlayer!=null) {
            if (!playButton.isSelected()) {
                mediaPlayer.pause();
            }
        }
    }

    public void cancelTimer() {
        running = false;
        timer.cancel();
    }

    public void beginTimer() {
        if (mediaPlayer != null) {
            timer = new Timer();
            task = new TimerTask() {

                public void run() {
                    running = true;
                    double current = mediaPlayer.getCurrentTime().toSeconds();
                    double end = media.getDuration().toSeconds();
                    songProgressBar.setProgress(current / end);

                    if (current / end == 1) {
                        cancelTimer();
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 1000, 1000);
        }
    }


    @FXML
    public void nextMedia() {
        if(songNumber < filaReproducao.size() - 1) {
            songNumber++;
            mediaPlayer.stop();
            if(running) {
                cancelTimer();
            }
            playSong(songNumber);

        }
        else {
            songNumber = 0;
            mediaPlayer.stop();
            playSong(songNumber);
        }
    }

    @FXML
    public void previousMedia() {

        if(songNumber>0) {
            songNumber--;
            mediaPlayer.stop();
            if(running) {cancelTimer();}
            mediaPlayer.stop();
            playSong(songNumber);
        }
    }


    /**
     * Atualiza a lista de música da tela
     *
     */
    @FXML
    public void atualizarListaMusica(List <Musica> SONGS) {
        if (SONGS != null) {
            for (Musica musica : SONGS) {
                filaReproducao.add(musica);
            }
            MusicaObservableList = FXCollections.observableArrayList();
            for (Musica musica : SONGS) {
                MusicaObservableList.add(musica);
            }

            listViewMusicas.setItems(MusicaObservableList);

            listViewMusicas.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Musica musica, boolean empty) {
                    super.updateItem(musica, empty);

                    if (empty || musica == null) {
                        setText(null);
                        setStyle(""); // Limpa o estilo da célula
                    } else {
                        setText(musica.getTitulo() + " - " + musica.getArtista());

                        // Verifica se a música está selecionada
                        if (listViewMusicas.getSelectionModel().getSelectedItem() == musica) {
                            setStyle("-fx-background-color: lightblue;"); // Define o estilo da célula selecionada
                        } else {
                            setStyle(""); // Limpa o estilo da célula não selecionada
                        }
                    }
                }
            });
        }
    }

    @FXML
    public void dirToggleButton (){
        if (diretorioButton.isSelected()){
            nameDiretorio.setText("DIRETÓRIO");
            atualizarListaMusica(SONGS_ONLINE);
        } else {
            exibirPlaylist();
        }
    }

    @FXML
    public void exibirPlaylist(){
        Playlist playlistSelecionada = listViewPlaylist.getSelectionModel().getSelectedItem();
        if (playlistSelecionada!=null) {
            atualizarListaMusica(playlistSelecionada.getBd_musicasPlay());
            nameDiretorio.setText(playlistSelecionada.getNome());
            diretorioButton.setSelected(false);
        }
    }

    public void atualizarListaPlaylist() {
        //if ()
        PLAYLISTS_ONLINE = (((UsuarioVip) USUARIO_ONLINE).getPlaylists());
        PlaylistObservableList= FXCollections.observableArrayList();

        if (PLAYLISTS_ONLINE != null) {
            for (Playlist playlist : PLAYLISTS_ONLINE) {
                System.out.println(playlist.getNome());
                PlaylistObservableList.add(playlist);
            }
        }
            listViewPlaylist.setItems(PlaylistObservableList);

            listViewPlaylist.setCellFactory(param -> new ListCell<>() {
                int id = 0;

                @Override
                protected void updateItem(Playlist playlist, boolean empty) {
                    super.updateItem(playlist, empty);
                    if (empty || playlist == null) {
                        setText(null);
                    } else {
                        setText(playlist.getNome());
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
            loader.setLocation(PanelNovaMusicaController.class.getResource("panel-nova-musica.fxml"));
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
                atualizarListaMusica(SONGS_ONLINE);
                dialogStage.close();
            }
        } catch (IOException e) {
            // Trate a exceção adequadamente, exibindo uma mensagem de erro ou realizando outras ações necessárias
            System.out.println("Ocorreu um erro ao carregar o painel de nova música.");
            e.printStackTrace();
        }
    }

    /*@FXML
    public void showFXMLPanelPlaylist(){
        HelloApplication.changeScreen("playlist",USUARIO_ONLINE.getNome());
        controllerNovaPlaylist.setUsuarioOnline(USUARIO_ONLINE);
        atualizarListaPlaylist();
    }
*/
    @FXML
    public void showFXMLPanelPlaylist() {
        HelloApplication.changeScreen("playlist", USUARIO_ONLINE.getNome());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ControllerNovaPlaylist.class.getResource("panel-nova-playlist.fxml"));

        try {
            AnchorPane novaPlaylist = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Criar Nova Playlist");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(novaPlaylist);
            dialogStage.setScene(scene);

            controllerNovaPlaylist = loader.getController();
            controllerNovaPlaylist.setDialogStage(dialogStage);
            controllerNovaPlaylist.setControllerPlayer(this);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        atualizarListaPlaylist();
    }



    /**
     * Converte o usuário comum em usuário VIP.
     * Se o usuário online for do tipo {@UsuarioComum},
     * ele será substituído por um objeto do tipo {@UsuarioVip}.
     * O usuário comum será removido do DAO de usuários e substituído pelo usuário VIP.
     */
    @FXML
    public void setMnVirarVIP() {
        if (USUARIO_ONLINE instanceof UsuarioVip) {
           System.out.println("usuario ja é vip");
            Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Usuario já possui cadastro VIP");
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(ControllerVIP.class.getResource("panel-pagamento.fxml"));
                AnchorPane virarVIP = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Torne-se VIP");
                dialogStage.initModality(Modality.WINDOW_MODAL);

                Scene scene = new Scene(virarVIP);
                dialogStage.setScene(scene);

                ControllerVIP controllerVIP = loader.getController();
                controllerVIP.setDialogStage(dialogStage);

                controllerVIP.setUsuarioComum((UsuarioComum) USUARIO_ONLINE);

                dialogStage.showAndWait();

                if (controllerVIP.isButtonConfirmar()) {
                    USUARIO_ONLINE = controllerVIP.getUsuarioVIP();
                    controllerVIP.setButtonConfirmar(false);
                    vipStatusLabel.setText("ATIVO");
                    mnNewPlaylist.setDisable(false);
                    // tornarSeVip();
                }
            } catch (IOException e) {
                // Trate a exceção adequadamente, exibindo uma mensagem de erro ou realizando outras ações necessárias
                System.out.println("Ocorreu um erro ao carregar o painel de nova música.");
                e.printStackTrace();
            }
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

    @FXML
    public void logout (){
        HelloApplication.changeScreen("login","");
        this.USUARIO_ONLINE = null;
        this.media=null;
        this.filaReproducao =null;
        if(mediaPlayer!=null){
        mediaPlayer.stop();
        mediaPlayer.dispose();}
        mediaPlayer = null;
        if (timer != null) {
            cancelTimer();
            timer = null;
        }

        //this.dialogStage.close();

    }

    public void setControllerPlaylist(ControllerNovaPlaylist controllerNovaPlaylist) {

    this.controllerNovaPlaylist = controllerNovaPlaylist;
    }
}
