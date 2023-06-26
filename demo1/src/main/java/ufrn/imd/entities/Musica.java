package ufrn.imd.entities;

import java.io.File;

/**
 * Classe que representa uma música.
 */
public class Musica {
    private String titulo;
    private String artista;
    private String caminho;
    private File file;

    public Musica() {
    }

    public Musica(String titulo, String artista, String caminho, File file) {
        this.titulo = titulo;
        this.artista = artista;
        this.caminho = caminho;
        this.file = file;
    }

    /**
     * Obtém o título da música.
     *
     * @return O título da música.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define o título da música.
     *
     * @param titulo O título da música.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtém o artista da música.
     *
     * @return O artista da música.
     */
    public String getArtista() {
        return artista;
    }

    /**
     * Define o artista da música.
     *
     * @param artista O artista da música.
     */
    public void setArtista(String artista) {
        this.artista = artista;
    }

    /**
     * Obtém o caminho da música.
     *
     * @return O caminho da música.
     */
    public String getCaminho() {
        return caminho;
    }

    /**
     * Define o caminho da música.
     *
     * @param caminho O caminho da música.
     */
    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    /**
     * Obtém o arquivo da música.
     *
     * @return O arquivo da música.
     */
    public File getFile() {
        return file;
    }

    /**
     * Define o arquivo da música.
     *
     * @param file O arquivo da música.
     */
    public void setFile(File file) {
        this.file = file;
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
