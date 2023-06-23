package ufrn.imd.entities;


import ufrn.imd.DAO.diretoriosDAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UsuarioVip extends Usuario {
    private List<Playlist> playlists;

    public UsuarioVip() {
        playlists = new ArrayList<>();
    }

    public UsuarioVip(String nome, String email, String senha, boolean isAdmin) {
        super(nome, email, senha, isAdmin);
        playlists = new ArrayList<>();
    }

    public void criarPlaylist (String playlistName, List <Musica> musicasPlaylist) throws IOException {
        Playlist playlist = new Playlist();

        playlist.setBd_musicasPlay(musicasPlaylist);
        playlist.setNome(playlistName);

        Playlist.getDaoPlaylist().salvarMemoriaPlaylist(getId(), playlist);
        Playlist.getDaoPlaylist().salvarSrcPlaylist (getId(), playlist);
    }

    public void excluirPlaylist(Playlist playlistSelecionada){
        File folder = new File("src/playlists/playlist_" + playlistSelecionada.getNome()+".txt");
        folder.delete();
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
}



