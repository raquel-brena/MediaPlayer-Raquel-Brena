package ufrn.imd.entities;

import ufrn.imd.DAO.diretoriosDAO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Directory {
    private String caminho;
    private File file;
    private static diretoriosDAO DAO_DIRETORIOS = new diretoriosDAO();
    private List<Musica> bd_musicas;
    private ArquivoUtil arquivo;

    public Directory() {
        bd_musicas = new ArrayList<>();
        arquivo = new ArquivoUtil();
        caminho = "";
        file = new File(caminho);
    }

    public boolean createPath(String nomeUsuario) {
        this.caminho = "src/main/diretorios/" + nomeUsuario + "Diretorio";
        File diretorioUsuario = new File(caminho);

        if (diretorioUsuario.exists()) {
            System.out.println("A pasta de músicas do usuário já existe.");
            return false;
        }

        if (diretorioUsuario.mkdirs()) {
            this.file = diretorioUsuario;
            System.out.println("Pasta de músicas do usuário criada com sucesso.");
        } else {
            System.out.println("Erro ao criar a pasta de músicas do usuário.");
            return false;
        }

        return true;
    }

    public void buscarMusicas() {
        // Implementação da busca de músicas no diretório
    }

    public boolean adicionarMusicaObj(Musica musica) {
        bd_musicas.add(musica);
        System.out.println("Musica adicionada no diretório: " + musica.getTitulo() + "  //  " + caminho);
        return true;
    }

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
            return false;
        }
        return true;
    }

    public boolean excluirMusica(Musica musica) {
        bd_musicas.remove(musica);
        atualizarArquivoMusica(bd_musicas);
        return true;
    }

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

    public List<Musica> getMusicaList() {
        return bd_musicas;
    }

    public static diretoriosDAO getDaoDiretorios() {
        return DAO_DIRETORIOS;
    }

    public List<File> getAllFileSong() {
        List<File> allFiles = new ArrayList<>();

        for (Musica musica : bd_musicas) {
            allFiles.add(musica.getFile());
        }
        return allFiles;
    }

    public List<Musica> getAllSongs() {
        return bd_musicas;
    }

    public String getCaminho() {
        return caminho;
    }

    public static void setDaoDiretorios(diretoriosDAO daoDiretorios) {
        DAO_DIRETORIOS = daoDiretorios;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public List<Musica> getMusicasDiretorio() {
        return bd_musicas;
    }

    public ArquivoUtil getArquivo() {
        return arquivo;
    }

    public void setArquivo(ArquivoUtil arquivo) {
        this.arquivo = arquivo;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setBd_musicas(List<Musica> bd_musicas) {
        this.bd_musicas = bd_musicas;
    }

    public void setMusicasPlaylist(List<Musica> musicas) {
        this.bd_musicas = musicas;

        for (Musica musica : musicas) {
            arquivo.escreverArquivo(DAO_DIRETORIOS.getSrcMusicas(), musica.getTitulo() + ",," + musica.getArtista() + ",," + musica.getCaminho());
        }
    }

    public boolean createPathPlaylist(UsuarioVip usuarioVip, String playlistName, Directory playlist) {
        String caminhoDiretorioPai = usuarioVip.getDirectory().getCaminho();
        String caminhoPath = caminhoDiretorioPai + "/" + playlistName + "Playlist";
        String caminhoTXT = "src/playlist_" + playlistName + ".txt";
        File txtPlaylist = new File(caminhoTXT);
        File filePlaylist = new File(caminhoPath);
        playlist.setCaminho(caminhoPath);
        playlist.setFile(filePlaylist);

        if (filePlaylist.exists()) {
            System.out.println("A pasta de músicas do usuário já existe.");
            return false;
        }

        if (filePlaylist.mkdirs()) {
            try {
                if (txtPlaylist.createNewFile()) {
                    writePlaylistToFile(txtPlaylist, caminhoPath, usuarioVip, playlistName, playlist.getAllSongs());
                    System.out.println("Pasta de músicas do usuário criada com sucesso.");
                } else {
                    System.out.println("Erro ao criar o arquivo de playlist.");
                    return false;
                }
            } catch (IOException e) {
                System.out.println("Erro ao criar o arquivo de playlist: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Erro ao criar a pasta de músicas do usuário.");
            return false;
        }

        return true;
    }

    private void writePlaylistToFile(File txtPlaylist, String caminhoPlaylist, UsuarioVip usuarioVip, String playlistName, List<Musica> musicas) throws IOException {

        FileWriter fileWriter = new FileWriter(txtPlaylist);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write(caminhoPlaylist +",,"+usuarioVip.getNome() + ",," + playlistName);
        bufferedWriter.newLine();

        for (Musica musica : musicas) {
            bufferedWriter.write(musica.getTitulo() + ",," + musica.getArtista() + ",," + musica.getCaminho());
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
        fileWriter.close();
    }

    @Override
    public String toString() {
        return "Directory{" +
                "caminho='" + caminho + '}';
    }
}
