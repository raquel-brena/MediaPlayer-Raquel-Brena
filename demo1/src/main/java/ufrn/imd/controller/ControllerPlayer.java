package ufrn.imd.controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ufrn.imd.MediaPlayer;
import ufrn.imd.entities.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.stage.Modality;
import javafx.stage.Stage;


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


    @FXML
    private ImageView userImage;

    @FXML
    private Label nameDiretorio;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label vipStatusLabel;

    @FXML
    private MenuItem mnNewPlaylist;

    @FXML
    private MenuItem mnItemAbout;

    @FXML
    private MenuItem  mnAdmin;


    @FXML
    private Button deletePlaylistButton;

    @FXML
    private Button refreshButton;

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
    private javafx.scene.media.MediaPlayer mediaPlayer;
    private static Usuario USUARIO_ONLINE;
    private ObservableList<Musica> MusicaObservableList;
    private ObservableList<Playlist> PlaylistObservableList;
    private List<Musica> filaReproducao;
    private int songNumber;

    private ControllerNovaPlaylist controllerPlaylist;
    private ControllerAbout controllerAbout;

    /**
     * Construtor padrão.
     */
    public ControllerPlayer() {
    }

    public void inicializar(){
        USUARIO_ONLINE = new UsuarioComum();
        SONGS_ONLINE = new ArrayList<>();
        PLAYLISTS_ONLINE = new ArrayList<>();
        filaReproducao = new ArrayList<>();
        PlaylistObservableList = FXCollections.observableArrayList();
        //nameMusicLabel.setText("");
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

        atualizarListaMusica(SONGS_ONLINE);

        if (USUARIO_ONLINE instanceof UsuarioVip){
            vipStatusLabel.setText("VIP ATIVO");
            mnNewPlaylist.setDisable(false);
            deletePlaylistButton.setDisable(false);
            refreshButton.setDisable(false);
            listViewPlaylist.setDisable(false);
            PLAYLISTS_ONLINE = ((UsuarioVip) USUARIO_ONLINE).getPlaylists();
            atualizarListaPlaylist();
        } else {
            vipStatusLabel.setText("VIP INATIVO");
            mnNewPlaylist.setDisable(true);
            deletePlaylistButton.setDisable(true);
            refreshButton.setDisable(true);
            listViewPlaylist.setDisable(true);
            PLAYLISTS_ONLINE.clear();
        }
        if (USUARIO_ONLINE.isAdmin()){
            mnAdmin.setDisable(false);
        } else {
            mnAdmin.setDisable(true);
        }

        Image image = new Image("png-user.png");
        userImage.setImage(image);
        volumeSlider.setValue(100);
        songProgressBar.setStyle("-fx-accent: #0c0428;");

    }

    /**
     * Remove a música selecionada. Se uma música estiver selecionada, ela será removida da biblioteca do usuário online.
     * Se uma playlist estiver selecionada, ela será excluída da lista de reprodução do usuário online.
     */
    @FXML
    public void removeMusicButton() {
        Musica musicaSelecionada = listViewMusicas.getSelectionModel().getSelectedItem();
        Playlist playlistSelecionada = listViewPlaylist.getSelectionModel().getSelectedItem();

        if (musicaSelecionada != null) {
            USUARIO_ONLINE.getDirectory().excluirMusica(musicaSelecionada);
            SONGS_ONLINE = USUARIO_ONLINE.getDirectory().getAllSongs();
            atualizarListaMusica(SONGS_ONLINE);

            if (USUARIO_ONLINE instanceof UsuarioVip) {
                ((UsuarioVip) USUARIO_ONLINE).excluirMusicadePlaylists(musicaSelecionada);
                atualizarListaPlaylist();
            }
        } else if (playlistSelecionada != null) {
            PlaylistObservableList.remove(playlistSelecionada);
            ((UsuarioVip) USUARIO_ONLINE).excluirPlaylist(playlistSelecionada);
            PLAYLISTS_ONLINE.remove(playlistSelecionada);
            exibirPlaylist();
        }
    }


    /**
     * Remove a playlist selecionada. Se uma playlist estiver selecionada, ela será excluída da lista de reprodução do usuário online.
     */
    @FXML
    public void removePlaylist() {
        //warningLabel.setVisible(false);
        Playlist playlistSelecionada = listViewPlaylist.getSelectionModel().getSelectedItem();

        if (playlistSelecionada != null) {
            PlaylistObservableList.remove(playlistSelecionada);
            ((UsuarioVip) USUARIO_ONLINE).excluirPlaylist(playlistSelecionada);
            PLAYLISTS_ONLINE.remove(playlistSelecionada);
            exibirPlaylist();
        }
    }


    /**
     * Define o volume do mediaPlayer com base no valor selecionado pelo usuário.
     */
    @FXML
    public void volume() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
        }
    }


    /**
     * Atualiza a posição de reprodução da música com base no arrasto do mouse na barra de progresso.
     */
    @FXML
    public void dragProgressBar() {
        songProgressBar.setOnMouseDragged(event -> {
            double progress = event.getX() / songProgressBar.getWidth();
            mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(progress));
        });
    }

    /**
     * Reproduz a música selecionada na posição especificada da fila de reprodução.
     *
     * @param number A posição da música na fila de reprodução.
     */
    public void playSong(int number) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (number >= 0 && number < filaReproducao.size()) {
            this.media = new Media(filaReproducao.get(number).getFile().toURI().toString());
            this.mediaPlayer = new javafx.scene.media.MediaPlayer(media);
            this.mediaPlayer.play();
            volume();
            beginTimer();
            nameMusicLabel.setText(filaReproducao.get(number).getTitulo() + " - " + filaReproducao.get(number).getArtista());
            listViewMusicas.getSelectionModel().select(number);
        }
    }

    /**
     * Reproduz a música selecionada ou pausa a reprodução, dependendo do estado do botão de reprodução.
     */
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
        if (!playButton.isSelected()) {
            if (mediaPlayer!=null) {
                mediaPlayer.pause();
            }
        }
    }


    /**
     * Atualiza a lista de playlists e executa ações relacionadas.
     */
    @FXML
    public void atualizarButton() {
        atualizarListaPlaylist();
        //atualizarListaMusica(filaReproducao);

        if (this.controllerPlaylist.isButtonConfirmar()) {
            //PLAYLISTS_ONLINE = ((UsuarioVip) USUARIO_ONLINE).getPlaylists();
            this.controllerPlaylist.setButtonConfirmar(false);
        }
    }

    /**
     * Exibe o painel FXML da playlist e configura o controlador com o usuário online atual.
     */
    @FXML
    public void showFXMLPanelPlaylist() {
        MediaPlayer.changeScreen("playlist", USUARIO_ONLINE.getNome());
        this.controllerPlaylist.setUsuarioOnline((UsuarioVip) USUARIO_ONLINE);
    }


    /**
     * Exibe o painel FXML da About e configura o controlador com o usuário online atual.
     */
    @FXML
    public void showFXMLAbout() {
        MediaPlayer.changeScreen("about", USUARIO_ONLINE.getNome());
        this.controllerAbout.setUsuarioOnline(USUARIO_ONLINE);
    }

    /**
     * Cancela o timer em execução.
     */
    public void cancelTimer() {
        running = false;
        timer.cancel();
    }

    /**
     * Inicia o timer para atualizar a barra de progresso da música em reprodução.
     */
    public void beginTimer() {
        if (mediaPlayer != null && media != null) {
            timer = new Timer();
            task = new TimerTask() {
                public void run() {
                    running = true;
                    if (mediaPlayer.getStatus() == javafx.scene.media.MediaPlayer.Status.PLAYING) {
                        double current = mediaPlayer.getCurrentTime().toSeconds();
                        double end = media.getDuration().toSeconds();
                        songProgressBar.setProgress(current / end);

                        if (current / end == 1) {
                            cancelTimer();
                        }
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 1000, 1000);
        }
    }

    /**
     * Avança para a próxima música na fila de reprodução.
     * Se houver mais músicas na fila, reproduz a próxima música.
     * Caso contrário, retorna à primeira música da fila e a reproduz.
     */
    @FXML
    public void nextMedia() {
        if (songNumber < filaReproducao.size() - 1) {
            songNumber++;
            mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }
            playSong(songNumber);
        } else {
            songNumber = 0;
            mediaPlayer.stop();
            playSong(songNumber);
        }
    }

    /**
     * Retrocede para a música anterior na fila de reprodução.
     * Se houver uma música anterior, reproduz a música anterior.
     */
    @FXML
    public void previousMedia() {
        if (songNumber > 0) {
            songNumber--;
            mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }
            mediaPlayer.stop();
            playSong(songNumber);
        }
    }
    /**
     * Atualiza a lista de músicas exibida na tela.
     *
     * @param SONGS A lista de músicas a ser atualizada.
     *             Se for nula, a lista de músicas não será atualizada.
     *             Se não for nula, a lista de músicas será atualizada com as músicas fornecidas.
     *             As músicas também serão adicionadas à fila de reprodução.
     */
    public void atualizarListaMusica(List<Musica> SONGS) {
        filaReproducao.clear();
        MusicaObservableList.clear();

        MusicaObservableList = FXCollections.observableArrayList();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (SONGS != null) {
            for (Musica musica : SONGS) {
                filaReproducao.add(musica);
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
                    }
                }
            });
        }
    }


    /**
     * Alterna entre exibir a lista de músicas do diretório ou a lista de playlists.
     * Se o botão de diretório estiver selecionado, exibe a lista de músicas do diretório.
     * Caso contrário, atualiza a lista de playlists e exibe as playlists.
     */
    @FXML
    public void dirToggleButton() {
        if (diretorioButton.isSelected()) {
            nameDiretorio.setText("DIRETÓRIO");
            atualizarListaMusica(SONGS_ONLINE);
        } else {
            if (USUARIO_ONLINE instanceof UsuarioVip) {
                atualizarListaPlaylist();
                exibirPlaylist();
            }
        }
    }


    /**
     * Exibe a playlist selecionada na tela, atualizando a lista de músicas exibida.
     * Se uma playlist estiver selecionada, atualiza a lista de músicas exibida na tela
     * com as músicas da playlist selecionada. O título da playlist é exibido como o nome
     * do diretório na tela. O botão de diretório é desmarcado.
     */
    @FXML
    public void exibirPlaylist() {
        atualizarListaPlaylist();
        Playlist playlistSelecionada;
        playlistSelecionada = listViewPlaylist.getSelectionModel().getSelectedItem();

        if (playlistSelecionada != null) {
            List<Musica> musicasPlaylistSelecionada = playlistSelecionada.getBd_musicasPlay();
            atualizarListaMusica(musicasPlaylistSelecionada);
            nameDiretorio.setText(playlistSelecionada.getNome());
            diretorioButton.setSelected(false);

        }

    }

    /**
     * Atualiza a lista de playlists exibida na tela.
     * Obtém a lista de playlists do usuário online e atualiza a lista exibida na tela.
     */
    public void atualizarListaPlaylist() {
        this.PLAYLISTS_ONLINE = ((UsuarioVip) USUARIO_ONLINE).getPlaylists();
        PlaylistObservableList = FXCollections.observableArrayList();

        if (PLAYLISTS_ONLINE != null) {
            for (Playlist playlist : PLAYLISTS_ONLINE) {
                PlaylistObservableList.add(playlist);
            }
        }

        listViewPlaylist.setItems(PlaylistObservableList);
        listViewPlaylist.setCellFactory(param -> new ListCell<>() {
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
    @FXML
    public void setMnVirarVIP() {
        if (USUARIO_ONLINE instanceof UsuarioVip || USUARIO_ONLINE.isAdmin()) {
            System.out.println("usuario ja é vip");
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
                    vipStatusLabel.setText("VIP ATIVO");
                    mnNewPlaylist.setDisable(false);
                    deletePlaylistButton.setDisable(false);
                    refreshButton.setDisable(false);
                    listViewPlaylist.setDisable(false);
                    // tornarSeVip();
                }
            } catch (IOException e) {
                System.out.println("Ocorreu um erro ao carregar o painel de nova música.");
                e.printStackTrace();
            }
        }
    }


    /**
     * Abre o painel de administração técnica.
     * Carrega o arquivo FXML do painel de administração técnica e exibe em uma janela separada.
     * O painel permite realizar operações de administração específicas.
     * Antes de exibir o painel, são realizadas algumas configurações iniciais, como definição do título
     * da janela e configuração do controller do painel.
     * Após exibir o painel, aguarda até que a janela seja fechada antes de continuar a execução.
     * Em caso de erro ao carregar o painel, é exibida uma mensagem de erro e a exceção é impressa.
     */
    @FXML
    public void serMnAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControllerVIP.class.getResource("panel-admin.fxml"));
            AnchorPane admin = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Administração Técnica");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(admin);
            dialogStage.setScene(scene);

            ControllerAdmin controllerAdmin = loader.getController();
            controllerAdmin.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao carregar o painel de administração técnica.");
            e.printStackTrace();
        }
    }




    /**
     * Realiza o logout do usuário.
     * Redireciona para a tela de login, encerrando a sessão do usuário atual.
     * Limpa os dados do usuário e realiza outras tarefas de limpeza, como parar a reprodução de mídia,
     * encerrar o timer e liberar recursos do media player.
     *
     */
    @FXML
    public void logout() {
        MediaPlayer.changeScreen("login", "");
        this.USUARIO_ONLINE = null;
        this.PLAYLISTS_ONLINE=null;
        this.SONGS_ONLINE=null;
        this.media = null;
        this.filaReproducao = null;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        if (timer != null) {
            cancelTimer();
            timer = null;
        }

        MusicaObservableList.clear();
        PlaylistObservableList.clear();
    }

    /**
     * Define o controller do painel de nova playlist.
     * Permite configurar o controller do painel de nova playlist para comunicação entre os controllers.
     *
     * @param controllerNovaPlaylist O controller do painel de nova playlist.
     */
    public void setControllerPlaylist(ControllerNovaPlaylist controllerNovaPlaylist) {
        this.controllerPlaylist = controllerNovaPlaylist;
    }


    public void setControllerAbout(ControllerAbout controllerAbout) {
        this.controllerAbout =controllerAbout;
    }
}