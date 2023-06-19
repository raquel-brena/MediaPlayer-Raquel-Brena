package ufrn.imd.entities;

import ufrn.imd.DAO.diretoriosDAO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um diretório de músicas.
 */
public class Directory {
    private String caminho;
    private File file;
    private static diretoriosDAO DAO_DIRETORIOS = new diretoriosDAO(); // todos os diretorios
    private List<Musica> bd_musicas; // lista de files.mp3
    private ArquivoUtil arquivo;

    /**
     * Construtor da classe Directory.
     * Inicializa as variáveis e configurações necessárias.
     */
    public Directory() {
        bd_musicas = new ArrayList<>();
        arquivo = new ArquivoUtil();
        caminho = "";
        file = new File(caminho);

        //buscarMusicas();
    }

    /**
     * Busca músicas no diretório.
     */
    public void buscarMusicas(){
        //  DAO_DIRETORIOS.
    }

    /**
     * Cria o diretório para o usuário especificado.
     *
     * @param nomeUsuario nome do usuário
     * @return true se o diretório foi criado com sucesso, false caso contrário
     */
    public boolean createPath(String nomeUsuario) {
        this.caminho = "src/main/diretorios/" + nomeUsuario + "Diretorio"; // Defina o caminho desejado para a pasta do usuário
        File diretorioUsuario = new File(caminho);

        // Verificar se a pasta já existe
        if (diretorioUsuario.exists()) {
            System.out.println("A pasta de músicas do usuário já existe.");
            return false;
        }

        // Criar a pasta do usuário
        if (diretorioUsuario.mkdirs()) {
            this.file = diretorioUsuario;

            //arquivo.escreverArquivo(DAO_DIRETORIOS.getSrcDiretorios(), diretorioUsuario.getPath());
            System.out.println("Pasta de músicas do usuário criada com sucesso.");
        } else {
            System.out.println("Erro ao criar a pasta de músicas do usuário.");
            return false;
        }

        return true;
    }

    /**
     * Adiciona uma música ao diretório.
     *
     * @param musica a música a ser adicionada
     * @return true se a música foi adicionada com sucesso, false caso contrário
     */
    public boolean adicionarMusicaObj(Musica musica) {
        bd_musicas.add(musica);
        System.out.println("Musica adicionada no diretório: " + musica.getTitulo() +"  //  " + caminho);
        return true;
    }

    /**
     * Adiciona um arquivo de música ao diretório.
     *
     * @param novaMusica o arquivo de música a ser adicionado
     * @return true se o arquivo de música foi adicionado com sucesso, false caso contrário
     */
    public boolean adicionarMusicaFile(Musica novaMusica) {
        File diretorio = new File(caminho);
        File arquivoDestino = new File(diretorio, novaMusica.getTitulo());
        novaMusica.setDiretorio(arquivoDestino.getPath());

        try {
            Files.copy(novaMusica.getFile().toPath(), arquivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo adicionado com sucesso em: " + arquivoDestino);
            bd_musicas.add(novaMusica);
            arquivo.escreverArquivo(DAO_DIRETORIOS.getSrcMusicas(), novaMusica.getTitulo() + ",," + novaMusica.getArtista() + ",," + novaMusica.getCaminho());
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Falha ao copiar o arquivo para o diretório do usuário
        }
        return true;
    }

    /**
     * Exclui uma música do diretório.
     *
     * @param musica a música a ser excluída
     * @return true se a música foi excluída com sucesso, false caso contrário
     */
    public boolean excluirMusica(Musica musica) {
        bd_musicas.remove(musica);
        atualizarArquivoMusica(bd_musicas);
        return true;
    }

    /**
     * Atualiza o arquivo de músicas com a lista atualizada de músicas.
     *
     * @param musicas a lista atualizada de músicas
     */
    public void atualizarArquivoMusica(List<Musica> musicas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DAO_DIRETORIOS.getSrcMusicas(), false))) {
            for (Musica musica : musicas) {
                String linha = musica.getTitulo() + ",," + musica.getArtista();
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtém a lista de músicas do diretório.
     *
     * @return a lista de músicas do diretório
     */
    public List<Musica> getMusicaList (){
        return bd_musicas;
    }

    /**
     * Obtém o DAO de diretórios.
     *
     * @return o DAO de diretórios
     */
    public static diretoriosDAO getDaoDiretorios() {
        return DAO_DIRETORIOS;
    }

    /**
     * Obtém todos os arquivos de música do diretório.
     *
     * @return a lista de arquivos de música do diretório
     */
    public List<File> getAllFileSong() {
        List<File> allFiles = new ArrayList<>();
        // List<String> allPathSong = new ArrayList<>();

        // allPathSong = readSongPathsFromTextFile(DAO_DIRETORIOS.getSrcDiretorios());

        for (Musica musica : bd_musicas) {
            allFiles.add(musica.getFile());
        }
        return allFiles;
    }

    /**
     * Obtém todas as músicas do diretório.
     *
     * @return a lista de músicas do diretório
     */
    public List<Musica> getAllSongs() {
        return bd_musicas;
    }


    /**
     * Obtém o caminho do diretório.
     *
     * @return o caminho do diretório
     */
    public String getCaminho() {
        return caminho;
    }

    /**
     * Define o DAO de diretórios.
     *
     * @param daoDiretorios o DAO de diretórios
     */
    public static void setDaoDiretorios(diretoriosDAO daoDiretorios) {
        DAO_DIRETORIOS = daoDiretorios;
    }

    /**
     * Define o caminho do diretório.
     *
     * @param caminho o caminho do diretório
     */
    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    /**
     * Obtém a lista de músicas do diretório.
     *
     * @return a lista de músicas do diretório
     */
    public List<Musica> getMusicasDiretorio() {
        return bd_musicas;
    }

    /**
     * Obtém a instância de ArquivoUtil.
     *
     * @return a instância de ArquivoUtil
     */
    public ArquivoUtil getArquivo() {
        return arquivo;
    }

    /**
     * Define a instância de ArquivoUtil.
     *
     * @param arquivo a instância de ArquivoUtil
     */
    public void setArquivo(ArquivoUtil arquivo) {
        this.arquivo = arquivo;
    }

    /**
     * Obtém o arquivo correspondente ao diretório.
     *
     * @return o arquivo correspondente ao diretório
     */
    public File getFile() {
        return file;
    }

    /**
     * Define o arquivo correspondente ao diretório.
     *
     * @param file o arquivo correspondente ao diretório
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Define a lista de músicas do diretório.
     *
     * @param bd_musicas a lista de músicas do diretório
     */
    public void setBd_musicas(List<Musica> bd_musicas) {
        this.bd_musicas = bd_musicas;
    }

    /**
     * Define a lista de músicas.
     *
     * @param musicas a lista de músicas
     */
    public void setMusicasPlaylist(List<Musica> musicas) {
        this.bd_musicas = musicas;

        for (Musica musica : musicas) {
            arquivo.escreverArquivo(DAO_DIRETORIOS.getSrcMusicas(), musica.getTitulo() + ",," + musica.getArtista() + ",," + musica.getCaminho());
        }
    }

    @Override
    public String toString() {
        return "Directory{" +
                "caminho='" + caminho;
    }
}
