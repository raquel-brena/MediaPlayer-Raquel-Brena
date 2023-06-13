package ufrn.imd.entities;


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

    public void adicionarDirectory(Directory playlist) {
        playlists.add(playlist);
    }

    public void removerDirectory(Directory directory) {
        playlists.remove(directory);
    }

    public List<Directory> getDirectories() {
        return playlists;
    }
}
