package ufrn.imd.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private ListView<Musica> listViewSongsPlaylist;
    @FXML
    private ListView<Musica> listViewSongsDiretorio;

    private ObservableList<Musica> MusicaObservableListPlaylist;
    private ObservableList<Musica> MusicaObservableListDiretorio;
    private UsuarioVip usuarioVIP;
    private List<Musica> SONGS_ONLINE_DIRETORIO;
    private List<Musica> SONGS_PLAYLIST;
    private Directory playlist;

    public ControllerNovaPlaylist() {
        usuarioVIP = new UsuarioVip();
        SONGS_ONLINE_DIRETORIO = new ArrayList<>();
        SONGS_PLAYLIST = new ArrayList<>();
        playlist = new Directory();
        MusicaObservableListPlaylist = FXCollections.observableArrayList();
        MusicaObservableListDiretorio = FXCollections.observableArrayList();
    }
    public void setUsuarioOnline(UsuarioVip usuarioOnline) {
        this.usuarioVIP = usuarioOnline;
        SONGS_ONLINE_DIRETORIO = usuarioOnline.getDirectory().getAllSongs();
        exibirMusicasDiretorio();
    }

    public void addMusicaPlaylist() throws IOException {
        Musica musicaSelecionada = listViewSongsDiretorio.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null) {
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

           /* try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtPlaylist))) {
                writer.write(musicaSelecionada.getTitulo()+","+musicaSelecionada.getCaminho()+","+musicaSelecionada.getArtista());
                writer.newLine();
        }*/
        }
    }

    /**
     * Cria uma PASTA e uma arquivo .txt para guardar informações da playlist do usuario.
     * @return
     * @throws IOException
     */



    @FXML
    public void createPlaylist() throws IOException {
        usuarioVIP.criarPlaylist(usuarioVIP,nomePlaylistTextField.getText(),SONGS_PLAYLIST);
       /**

        //ControllerNovaPlaylist >> UsuarioVIP >> Directory >> DAO
        Directory playlist = new Directory();
        playlist.setBd_musicas(SONGS_PLAYLIST);
        playlist.setCaminho(caminhoPath);
        playlist.setFile();


        Directory.getDaoDiretorios().salvarMusicasPlaylist(SONGS_PLAYLIST);
        Directory.getDaoDiretorios().createPlaylist((UsuarioVip) usuarioVIP, nomePlaylist, SONGS_PLAYLIST);
    */}



    @FXML
    public void removeMusicaPlaylist() {
        Musica musicaSelecionada = listViewSongsPlaylist.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null) {
            MusicaObservableListPlaylist.remove(musicaSelecionada);
            SONGS_PLAYLIST.remove(musicaSelecionada);
        }
    }

    @FXML
    public void voltar (){
        Alert alert = new Alert(Alert.AlertType.WARNING, "Dados de playlist perdidos");
        HelloApplication.changeScreen("player",usuarioVIP.getNome());
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


}