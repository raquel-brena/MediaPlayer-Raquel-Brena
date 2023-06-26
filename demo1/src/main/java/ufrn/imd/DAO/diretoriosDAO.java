package ufrn.imd.DAO;

import ufrn.imd.entities.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por acessar e manipular os dados dos diretórios.
 */
public class diretoriosDAO {
    private static final String SRC_DIRETORIOS = "src/diretorios.txt";
    private static final String SRC_MUSICAS = "src/musicas.txt";
    private static List<String> bd_dir_caminhos;
    private static List<Directory> bd_diretorios;
    private static List<Musica> bd_allMusica;
    private ArquivoUtil arquivo = new ArquivoUtil();

    /**
     * Construtor padrão.
     */
    public diretoriosDAO() {
        bd_dir_caminhos = new ArrayList<>();
        bd_diretorios = new ArrayList<>();
        bd_allMusica = new ArrayList<>();

    }

    /**
     * Lê os dados dos diretórios a partir do arquivo de texto.
     */
    public void lerArquivoDiretorio() {
        bd_diretorios.clear();
        lerArquivoMusica();

        try (BufferedReader br = new BufferedReader(new FileReader(SRC_DIRETORIOS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                bd_dir_caminhos.add(linha);

                Directory diretorio = new Directory();
                File diretorioUsuario = new File(linha);

                diretorio.setCaminho(linha);
                diretorio.setFile(diretorioUsuario);

                // Separando músicas por diretório
                for (Musica musica : bd_allMusica) {
                    if (musica.getCaminho().contains(linha)) {
                        diretorio.adicionarMusicaObj(musica);
                        System.out.println("Música: " + musica.getTitulo() + " adicionada em: " + musica.getCaminho());
                    }
                }

                // Adiciona o objeto Directory na lista bd_diretorios
                bd_diretorios.add(diretorio);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Lê os dados das músicas a partir do arquivo de texto.
     */
    public void lerArquivoMusica() {
        bd_allMusica.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(SRC_MUSICAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dadosMusica = linha.split(",,");
                String titulo = dadosMusica[0];
                String artista = dadosMusica[1];
                String caminho = dadosMusica[2];

                Musica musica = new Musica();
                musica.setTitulo(titulo);
                musica.setArtista(artista);
                musica.setCaminho(caminho);
                File musicaMP3 = new File(caminho);
                musica.setFile(musicaMP3);

                System.out.println("Música carregada! Título: " + musica.getTitulo());
                bd_allMusica.add(musica);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Salva um diretório na memória do DAO.
     *
     * @param diretorio O diretório a ser salvo.
     * @return True se o diretório for salvo com sucesso, False caso contrário.
     */
    public boolean salvarMemoria(Directory diretorio) {
        bd_diretorios.add(diretorio);
        return true;
    }

    /**
     * Salva um diretório no arquivo de texto.
     *
     * @param diretorio O diretório a ser salvo.
     * @return True se o diretório for salvo com sucesso, False caso contrário.
     */
    public boolean salvarSrcDiretorio(Directory diretorio) {
        arquivo.escreverArquivo(SRC_DIRETORIOS, diretorio.getFile().getPath());
        return true;
    }

    public void removerMusicadoTXT(String caminho) {
        List<Musica> apagar = new ArrayList<>();
        for (Musica musica : bd_allMusica) {
            if (musica.getCaminho().equals(caminho)) {
                apagar.add(musica);
            }
        }
        for (Musica musica : apagar) {
            if (bd_allMusica.contains(musica)) {
                bd_allMusica.remove(musica);
            }
        }
        atualizarArquivoMusica(bd_allMusica);

            File musica = new File(caminho);
            if (musica != null) {
                musica.delete();
            }
    }


    /**
     * Exclui um diretório do DAO e do arquivo de texto.
     *
     * @param diretorio O diretório a ser excluído.
     * @return True se o diretório for excluído com sucesso, False caso contrário.
     */
    public boolean excluir(File diretorio) {
        bd_dir_caminhos.remove(diretorio.getPath());
        bd_diretorios.remove(diretorio);
        atualizarArquivo(bd_dir_caminhos);
        return true;
    }


    // Métodos não implementados

    public void atualizarArquivo(List<String> diretorios) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SRC_DIRETORIOS, false))) {
            for (String caminho : bd_dir_caminhos) {
                bw.write(caminho);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void atualizarArquivoMusica(List<Musica> bd_allMusica) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SRC_MUSICAS, false))) {
            for (Musica musica : bd_allMusica) {
                arquivo.escreverArquivo(SRC_MUSICAS, musica.getTitulo() + ",," + musica.getArtista() + ",," + musica.getCaminho());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Directory> getBd_diretorios() {
        return bd_diretorios;
    }

    public static final String getSrcMusicas() {
        return SRC_MUSICAS;
    }

    public static List<Musica> getBd_allMusica() {
        return bd_allMusica;
    }


}