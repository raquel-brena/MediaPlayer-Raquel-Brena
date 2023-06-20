package ufrn.imd.entities;


import ufrn.imd.DAO.diretoriosDAO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class UsuarioVip extends Usuario {
    private List<Directory> playlists;


    public UsuarioVip() {
        this.playlists = playlists;
    }

    public UsuarioVip(String nome, String email, String senha, boolean isAdmin) {
        super(nome, email, senha, isAdmin);
        playlists = new ArrayList<>();
    }

    public void criarPlaylist (UsuarioVip usuarioVip,String playlistName, List <Musica> musicasPlaylist){
        //CRIAR DIRETORIO E .TXT

        Directory directory = new Directory();
        directory.setBd_musicas(musicasPlaylist);

        if (directory.createPathPlaylist(usuarioVip,playlistName,directory)){
            Directory.getDaoDiretorios().salvarMemoriaPlaylist(directory);
        }

    }

    public List<Directory> getDirectories() {
        return playlists;
    }
}
