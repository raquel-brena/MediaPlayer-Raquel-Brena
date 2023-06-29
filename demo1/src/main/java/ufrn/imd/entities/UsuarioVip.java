package ufrn.imd.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um usuário VIP.
 */
public class UsuarioVip extends Usuario {
    private List<Playlist> playlists;

    public UsuarioVip() {
        playlists = new ArrayList<>();
    }

    /**
     * Construtor que recebe os dados do usuário VIP.
     *
     * @param nome    O nome do usuário.
     * @param email   O email do usuário.
     * @param senha   A senha do usuário.
     * @param isAdmin Indica se o usuário é um administrador.
     */
    public UsuarioVip(String nome, String email, String senha, boolean isAdmin) {
        super(nome, email, senha, isAdmin);
        playlists = new ArrayList<>();
    }

    public void criarPlaylist(String nomePlaylist, List<Musica> songsPlaylist) {
        Playlist playlist = new Playlist();

        playlist.setNome(nomePlaylist);
        playlist.setBd_musicasPlay(songsPlaylist);

        playlists.add(playlist);
        Playlist.getDaoPlaylist().salvarMemoriaPlaylistsUsuario(getId(), playlists);
        Playlist.getDaoPlaylist().salvarSrcPlaylist(getId(), playlists);
    }

    /**
     * Exclui uma playlist do usuário VIP.
     *
     * @param playlistSelecionada A playlist a ser excluída.
     */
    public void excluirPlaylist(Playlist playlistSelecionada) {
        File folder = new File("src/playlists/playlist_" + playlistSelecionada.getNome() + ".txt");
        folder.delete();
    }

    public void excluirMusicadePlaylists(Musica musicaSelecionada) {
        for (Playlist playlist : playlists) {
            playlist.getBd_musicasPlay().remove(musicaSelecionada);
        }
        atualizarPlaylists();
    }

    public void adicionarMusicaPlaylist(String nomePlaylist, Musica musicaSelecionada) {
        for (Playlist playlist : playlists) {
            if (playlist.getNome().equals(nomePlaylist)) {
                playlist.getBd_musicasPlay().add(musicaSelecionada);
            }
        }
        atualizarPlaylists();
    }

    public void excluirTodasAsPlaylists() {
        playlists.clear();
        Playlist.getDaoPlaylist().salvarMemoriaPlaylistsUsuario(getId(), playlists);
        Playlist.getDaoPlaylist().salvarSrcPlaylist(getId(), playlists);
    }

    public void atualizarPlaylists() {
        Playlist.getDaoPlaylist().salvarMemoriaPlaylistsUsuario(getId(), playlists);
        Playlist.getDaoPlaylist().salvarSrcPlaylist(getId(), playlists);
    }

    /**
     * Obtém a lista de playlists do usuário VIP.
     *
     * @return A lista de playlists.
     */
    public List<Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * Define a lista de playlists do usuário VIP.
     *
     * @param playlists A lista de playlists.
     */
    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
}



