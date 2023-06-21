package ufrn.imd.entities;

import ufrn.imd.DAO.diretoriosDAO;
import ufrn.imd.DAO.playlistDAO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String nome;
    private static playlistDAO DAO_PLAYLIST = new playlistDAO();
    private String arquivoTXT;
    private List<Musica> bd_musicasPlay;
    private ArquivoUtil arquivo;

    public Playlist() {
        bd_musicasPlay = new ArrayList<>();
        arquivoTXT = "";
        arquivo = new ArquivoUtil();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static playlistDAO getDaoPlaylist() {
        return DAO_PLAYLIST;
    }

    public static void setDaoPlaylist(playlistDAO daoPlaylist) {
        DAO_PLAYLIST = daoPlaylist;
    }

    public String getArquivoTXT() {
        return arquivoTXT;
    }

    public void setArquivoTXT(String arquivoTXT) {
        this.arquivoTXT = arquivoTXT;
    }

    public List<Musica> getBd_musicasPlay() {
        return bd_musicasPlay;
    }

    public void setBd_musicasPlay(List<Musica> bd_musicasPlay) {
        this.bd_musicasPlay = bd_musicasPlay;
    }
}
