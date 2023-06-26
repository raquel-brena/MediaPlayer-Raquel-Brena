package ufrn.imd.entities;

import ufrn.imd.DAO.playlistDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma playlist.
 */
public class Playlist {
    private String nome;
    private static playlistDAO DAO_PLAYLIST = new playlistDAO();
    private List<Musica> bd_musicasPlay;

    public Playlist() {
        bd_musicasPlay = new ArrayList<>();
    }

    /**
     * Obtém o nome da playlist.
     *
     * @return O nome da playlist.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da playlist.
     *
     * @param nome O nome da playlist.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o DAO da playlist.
     *
     * @return O DAO da playlist.
     */
    public static playlistDAO getDaoPlaylist() {
        return DAO_PLAYLIST;
    }

    /**
     * Define o DAO da playlist.
     *
     * @param daoPlaylist O DAO da playlist.
     */
    public static void setDaoPlaylist(playlistDAO daoPlaylist) {
        DAO_PLAYLIST = daoPlaylist;
    }

    /**
     * Obtém a lista de músicas da playlist.
     *
     * @return A lista de músicas.
     */
    public List<Musica> getBd_musicasPlay() {
        return bd_musicasPlay;
    }

    /**
     * Define a lista de músicas da playlist.
     *
     * @param bd_musicasPlay A lista de músicas.
     */
    public void setBd_musicasPlay(List<Musica> bd_musicasPlay) {
        this.bd_musicasPlay = bd_musicasPlay;
    }
}
