package ufrn.imd.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ufrn.imd.HelloApplication;
import ufrn.imd.entities.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerNovaPlaylist {

    @FXML
    private TextField nomePlaylistTextField;
    @FXML
    private Button addSongButton;
    @FXML
    private Button removeSongButton;
    @FXML
    private Button newPlaylistButton;
    @FXML
    private Button homeButton;

    @FXML
    private Label warningLabel;

    @FXML
    private ListView<Musica> listViewSongsPlaylist;
    @FXML
    private ListView<Musica> listViewSongsDiretorio;

    private ObservableList<Musica> MusicaObservableListPlaylist;
    private ObservableList<Musica> MusicaObservableListDiretorio;
    private Usuario usuarioVIP =  new UsuarioVip();
    private List<Musica> SONGS_ONLINE_DIRETORIO;
    private List<Musica> SONGS_PLAYLIST;

    private boolean buttonConfirmar ;
    private Directory playlist;

    private Stage dialogStage;
    private ControllerPlayer controllerPlayer;

    public ControllerNovaPlaylist() {
        SONGS_ONLINE_DIRETORIO = new ArrayList<>();
        SONGS_PLAYLIST = new ArrayList<>();
        playlist = new Directory();
        MusicaObservableListPlaylist = FXCollections.observableArrayList();
        MusicaObservableListDiretorio = FXCollections.observableArrayList();
        buttonConfirmar = false;
    }

    @FXML
    public void createPlaylist() throws IOException {
        if (nomePlaylistTextField.getText().isEmpty()) {
            warningLabel.setVisible(true);
            warningLabel.setText("É obrigatório adicionar um nome para a sua playlist");
        } else {
            if (usuarioVIP instanceof UsuarioVip) {
                UsuarioVip usuarioVipCasting = (UsuarioVip) usuarioVIP;
                usuarioVipCasting.criarPlaylist(nomePlaylistTextField.getText(), SONGS_PLAYLIST);
            }
            this.buttonConfirmar = true;
        }

        voltar();
    }


    public void limparTela(){
        MusicaObservableListPlaylist.clear();
        MusicaObservableListDiretorio.clear();
        nomePlaylistTextField.clear();
        SONGS_PLAYLIST.clear();
    }

    public void setUsuarioOnline(Usuario usuarioOnline) {
        this.usuarioVIP = usuarioOnline;
        SONGS_ONLINE_DIRETORIO = usuarioOnline.getDirectory().getAllSongs();
        exibirMusicasDiretorio();
    }

    public void addMusicaPlaylist() throws IOException {
        warningLabel.setVisible(false);
        Musica musicaSelecionada = listViewSongsDiretorio.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null ) {
            if (!SONGS_PLAYLIST.contains(musicaSelecionada)) {
                MusicaObservableListPlaylist.add(musicaSelecionada);
                SONGS_PLAYLIST.add(musicaSelecionada);
                listViewSongsPlaylist.setItems(MusicaObservableListPlaylist);
                listViewSongsPlaylist.setCellFactory(param -> new ListCell<Musica>() {
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

            } else {
                warningLabel.setText("A música selecionada já está na playlist!");
            }
        }
    }

    public boolean isButtonConfirmar() {
        return this.buttonConfirmar;
    }

    public void setButtonConfirmar(boolean buttonConfirmar) {
        this.buttonConfirmar = buttonConfirmar;
    }

    @FXML
    public void removeMusicaPlaylist() {
        warningLabel.setVisible(false);
        Musica musicaSelecionada = listViewSongsPlaylist.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null) {
            MusicaObservableListPlaylist.remove(musicaSelecionada);
            SONGS_PLAYLIST.remove(musicaSelecionada);
        }
        listViewSongsPlaylist.setItems(MusicaObservableListPlaylist);
        listViewSongsPlaylist.setCellFactory(param -> new ListCell<Musica>() {
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

    @FXML
    public void voltar (){
        warningLabel.setVisible(false);
        this.buttonConfirmar = true;
        HelloApplication.changeScreen("player",usuarioVIP.getNome());
        limparTela();
    }


    public void exibirMusicasDiretorio() {
        for (Musica musica : SONGS_ONLINE_DIRETORIO) {
            MusicaObservableListDiretorio.add(musica);
        }
        listViewSongsDiretorio.setItems(MusicaObservableListDiretorio);

        listViewSongsDiretorio.setCellFactory(param -> new ListCell<Musica>() {
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

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setControllerPlayer(ControllerPlayer controllerPlayer) {
        this.controllerPlayer = controllerPlayer;
    }
}
