package ufrn.imd.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ufrn.imd.MediaPlayer;
import ufrn.imd.entities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerNovaPlaylist {

    @FXML
    private TextField nomePlaylistTextField;
    @FXML
    private Label warningLabel;

    @FXML
    private ListView<Musica> listViewSongsPlaylist;
    @FXML
    private ListView<Musica> listViewSongsDiretorio;
    private ObservableList<Musica> MusicaObservableListPlaylist;
    private ObservableList<Musica> MusicaObservableListDiretorio;
    private UsuarioVip usuarioVIP =  new UsuarioVip();
    private List<Musica> SONGS_ONLINE_DIRETORIO;
    private List<Musica> SONGS_PLAYLIST;
    private boolean buttonConfirmar;
    private Stage dialogStage;
    private String nomePlaylist;

    public ControllerNovaPlaylist() {
        SONGS_ONLINE_DIRETORIO = new ArrayList<>();
        SONGS_PLAYLIST = new ArrayList<>();
        MusicaObservableListPlaylist = FXCollections.observableArrayList();
        MusicaObservableListDiretorio = FXCollections.observableArrayList();
        buttonConfirmar = false;
    }

    public String getNomePlaylist (){
        this.nomePlaylist = nomePlaylistTextField.getText();
        return this.nomePlaylist;
    }


    public List<Musica> getSongsPlaylist(){
        return SONGS_PLAYLIST;
    }


    @FXML
    public void createPlaylist() throws IOException {
        if (nomePlaylistTextField.getText().isEmpty()) {
            warningLabel.setVisible(true);
            warningLabel.setText("É obrigatório adicionar um nome para a sua playlist");
        } else {
            usuarioVIP.criarPlaylist(nomePlaylistTextField.getText(), SONGS_PLAYLIST);
            this.buttonConfirmar = true;
            voltar();
        }
    }
/*
        if (nomePlaylistTextField.getText().isEmpty()) {
            warningLabel.setVisible(true);
            warningLabel.setText("É obrigatório adicionar um nome para a sua playlist");
        } else {
            this.new_playlist.setNome(nomePlaylistTextField.getText());
            this.new_playlist.setBd_musicasPlay(this.SONGS_PLAYLIST);

        }*/


    public void limparTela(){
        MusicaObservableListPlaylist.clear();
        MusicaObservableListDiretorio.clear();
        nomePlaylistTextField.clear();
        SONGS_PLAYLIST.clear();
        this.buttonConfirmar = true;
    }

    public void setUsuarioOnline(UsuarioVip usuarioOnline) {
        this.usuarioVIP = usuarioOnline;
        SONGS_ONLINE_DIRETORIO = usuarioOnline.getDirectory().getAllSongs();

        exibirMusicasDiretorio();
    }

    @FXML
    public void addMusicaPlaylist() {
        warningLabel.setVisible(false);
        Musica musicaSelecionada = listViewSongsDiretorio.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null && !SONGS_PLAYLIST.contains(musicaSelecionada)) {
            MusicaObservableListPlaylist.add(musicaSelecionada);
            SONGS_PLAYLIST.add(musicaSelecionada);
            listViewSongsPlaylist.setItems(MusicaObservableListPlaylist);
            listViewSongsPlaylist.setCellFactory(param -> new ListCell<Musica>() {
                @Override
                protected void updateItem(Musica musica, boolean empty) {
                    super.updateItem(musica, empty);
                    setText(empty || musica == null ? null : musica.getTitulo() + " - " + musica.getArtista());
                }
            });
        } else {
            warningLabel.setText("A música selecionada já está na playlist!");
            warningLabel.setVisible(true);
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
        MediaPlayer.changeScreen("player",usuarioVIP.getNome());
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

}
