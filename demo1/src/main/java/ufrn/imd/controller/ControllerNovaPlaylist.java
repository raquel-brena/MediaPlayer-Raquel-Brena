package ufrn.imd.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ufrn.imd.HelloApplication;
import ufrn.imd.entities.*;

import java.io.File;
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
    private Usuario usuarioVIP;
    private List<Musica> SONGS_ONLINE_DIRETORIO;
    private List<Musica> SONGS_PLAYLIST;


    private Directory playlist;


    ControllerNovaPlaylist() {
        usuarioVIP = new UsuarioVip();
        SONGS_ONLINE_DIRETORIO = new ArrayList<>();
        SONGS_PLAYLIST = new ArrayList<>();
        playlist = new Directory();
        MusicaObservableListPlaylist = FXCollections.observableArrayList();
        MusicaObservableListDiretorio = FXCollections.observableArrayList();
    }
    public void setUsuarioOnline(Usuario usuarioOnline) {
        this.usuarioVIP = usuarioOnline;
        SONGS_ONLINE_DIRETORIO = usuarioOnline.getDirectory().getAllSongs();
        exibirMusicasDiretorio();
    }

    public void addMusicaPlaylist(){
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
        }
    }

    public boolean createPath(){
        String nomePlaylist = nomePlaylistTextField.getText();
        String caminho = usuarioVIP.getDirectory().getCaminho()+"/"+nomePlaylist+"Playlist";

        File diretorioPlaylist = new File(caminho);

        // Verificar se a pasta já existe
        if (diretorioPlaylist.exists()) {
            System.out.println("A Playlist de músicas do usuário já existe.");
            return false;
        }

        // Criar a pasta do usuário
        if (diretorioPlaylist.mkdirs()) {
            playlist.setFile(diretorioPlaylist);
            playlist.setMusicasPlaylist(SONGS_PLAYLIST);
            playlist.setCaminho(caminho);
            //Acessar um DAO de playlist e
            playlist.getDaoDiretorios().salvarMemoria(playlist);
            playlist.getDaoDiretorios().salvarSrcDiretorio(playlist);
            System.out.println("Pasta de músicas do usuário criada com sucesso.");
        } else {
            System.out.println("Erro ao criar a pasta de músicas do usuário.");
            return false;
        }

        return true;
    }
    @FXML
    public void createPlaylist (){
       createPath();
    }

    @FXML
    public void removeMusicaPlaylist() {
        Musica musicaSelecionada = listViewSongsPlaylist.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null) {
            MusicaObservableListPlaylist.remove(musicaSelecionada);
            SONGS_PLAYLIST.remove(musicaSelecionada);
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
