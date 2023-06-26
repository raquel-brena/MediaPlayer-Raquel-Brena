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

    public boolean adicionarMusicaObj(Musica musica) {
        bd_musicas.add(musica);
        return true;
    }

    public boolean adicionarMusicaFile(Musica novaMusica) {
        File diretorio = new File(caminho);
        File arquivoDestino = new File(diretorio, novaMusica.getTitulo());
        novaMusica.setCaminho(arquivoDestino.getPath());

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
        getDaoDiretorios().removerMusicadoTXT(musica.getCaminho());
        return true;
    }

    public boolean excluirTodasAsMusicas() {
        bd_musicas.clear();

        File folder = new File(caminho);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        boolean deleted = file.delete();
                        if (!deleted) {
                            System.out.println("Falha ao excluir o arquivo: " + file.getAbsolutePath());
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public static diretoriosDAO getDaoDiretorios() {
        return DAO_DIRETORIOS;
    }

    public List<Musica> getAllSongs() {
        return bd_musicas;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }



    @Override
    public String toString() {
        return "Directory{" +
                "caminho='" + caminho + '}';
    }
}
