package ufrn.imd.entities;


import java.io.File;

public class Musica {
    private  String titulo;
    private  String artista;
    private String caminho;
    private File file;

    public Musica(){}
    public Musica(String titulo, String artista, String caminho, File file) {
        this.titulo = titulo;
        this.artista = artista;
        this.caminho = caminho;
        this.file = file;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getDiretorio() {
        return caminho;
    }

    public void setDiretorio(String diretorio) {
        this.caminho = diretorio;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    @Override
    public String toString() {
        return "Musica{" +
                "titulo='" + titulo + '\'' +
                ", artista='" + artista + '\'' +
                ", caminho='" + caminho + '\'' +
                '}';
    }
}
