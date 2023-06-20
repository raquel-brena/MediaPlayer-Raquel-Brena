package ufrn.imd.DAO;

import ufrn.imd.entities.ArquivoUtil;
import ufrn.imd.entities.Directory;
import ufrn.imd.entities.Musica;
import ufrn.imd.entities.UsuarioVip;

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
    private static final String SRC_DIRETORIOS = "demo1/src/diretorios.txt";
    private static final String SRC_MUSICAS = "demo1/src/musicas.txt";
    private static List<String> bd_dir_caminhos;
    private static List<Directory> bd_diretorios;
    private static List<Musica> bd_allMusica;
    private static List <Directory> bd_playlists;
    private ArquivoUtil arquivo = new ArquivoUtil();
    //private Map<String, Directory> diretoriosUsuario = new HashMap<>();
    /**
     * Construtor padrão.
     */
    public diretoriosDAO() {
        bd_dir_caminhos = new ArrayList<>();
        bd_diretorios = new ArrayList<>();
        bd_allMusica = new ArrayList<>();
    }

    /**
     * Busca um diretório pelo nome do autor.
     *
     * @param autor O nome do autor do diretório.
     * @return O objeto Directory correspondente ao diretório encontrado, ou null se não for encontrado.
     */
    public Directory buscarDiretorio(String autor) {
        for (Directory diretorio : bd_diretorios) {
            String caminho = diretorio.getCaminho();
            if (caminho.contains(autor)) {
                return diretorio;
            }
        }
        return null;
    }

    /**
     * Busca as músicas de um diretório específico.
     *
     * @param caminhoDiretorio O caminho do diretório.
     * @return A lista de músicas do diretório.
     */
    public List<Musica> buscarMusicas(String caminhoDiretorio) {
        List<Musica> musicasDiretorio = new ArrayList<>();
        lerArquivoDiretorio();

        for (Musica musica : bd_allMusica) {
            String caminhoMusica = musica.getCaminho();
            if (caminhoMusica.contains(caminhoDiretorio)) {
                musicasDiretorio.add(musica);
            }
        }
        return musicasDiretorio;
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
                String caminhoPath = linha;
                File diretorioUsuario = new File(caminhoPath);

                diretorio.setCaminho(caminhoPath);
                diretorio.setFile(diretorioUsuario);

                if (!caminhoPath.contains("Playlist")) {
                    // Separando música por diretório
                    for (Musica musica : bd_allMusica) {
                        if (musica.getCaminho().contains(caminhoPath)) {
                            diretorio.adicionarMusicaObj(musica);
                            System.out.println("Musica: " + musica.getTitulo() + " adicionada em: " + musica.getCaminho());
                        }
                    }
                    // Adiciona o objeto Directory na lista bd_diretorios
                    bd_diretorios.add(diretorio);
                } else {
                    bd_playlists.add(diretorio);
                }
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

    public void lerArquivoPlaylist() {
        bd_playlists.clear();
        String arquivoTXT = "src/"; // Substitua pelo caminho do diretório que você deseja pesquisar
        File folder = new File(arquivoTXT);

        File[] txt = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (txt != null) {
            for (File file : txt) {
                Directory playlist = new Directory();
                  try (BufferedReader br = new BufferedReader(new FileReader(file.getPath()))) {
                    String linha;
                    while ((linha = br.readLine()) != null) {
                        String[] dadosPlaylist = linha.split(",,");

                        String caminhoPlaylist = dadosPlaylist[0];
                        File filePlaylist = new File(caminhoPlaylist);

                        playlist.setCaminho(caminhoPlaylist);
                        playlist.setFile(filePlaylist);
                        //playlist.setBd_musicas();

                        if (!caminhoPath.contains("Playlist")) {
                            // Separando música por diretório
                            for (Musica musica : bd_allMusica) {
                                if (musica.getCaminho().contains(caminhoPath)) {
                                    diretorio.adicionarMusicaObj(musica);
                                    System.out.println("Musica: " + musica.getTitulo() + " adicionada em: " + musica.getCaminho());
                                }
                            }
                            // Adiciona o objeto Directory na lista bd_diretorios
                            bd_diretorios.add(diretorio);

                        System.out.println("Música carregada! Título: " + musica.getTitulo());
                        bd_allMusica.add(musica);
                    }
                }
            } catch (IOException e) {
                      throw new RuntimeException(e);
                  }

                System.out.println(file.getAbsolutePath());
            }
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
    public boolean salvarMemoriaPlaylist(Directory playlist) {
        bd_playlists.add(playlist);
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

    /**
     * Obtém uma lista de todos os arquivos de música nos diretórios do DAO.
     *
     * @return A lista de arquivos de música.
     */
    public List<File> getAllFileSong() {
        List<File> allFiles = new ArrayList<>();
        List<String> allPathSong = new ArrayList<>();
        allPathSong = readSongPathsFromTextFile(SRC_DIRETORIOS);

        for (String path : allPathSong) {
            File file = new File(path);
            allFiles.add(file);
        }
        return allFiles;
    }

    /**
     * Lê os caminhos das músicas a partir do arquivo de texto.
     *
     * @param filePath O caminho do arquivo de texto.
     * @return A lista de caminhos das músicas.
     */
    public List<String> readSongPathsFromTextFile(String filePath) {
        List<String> caminhos = new ArrayList<>();
        try {
            Path arquivo = Paths.get(filePath);
            caminhos = Files.readAllLines(arquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return caminhos;
    }

    /**
     * Obtém a lista de todos os caminhos dos diretórios no DAO.
     *
     * @return A lista de caminhos dos diretórios.
     */
    public List<String> listarPaths() {
        return bd_dir_caminhos;
    }

    // Métodos não implementados

    /**
     * Busca um objeto por ID.
     *
     * @param id O ID do objeto a ser buscado.
     * @return O objeto encontrado ou null se não for encontrado.
     */
    public Object buscarPorId(int id) {
        return null;
    }

    /**
     * Atualiza um objeto no DAO.
     *
     * @param objeto O objeto a ser atualizado.
     * @return True se o objeto for atualizado com sucesso, False caso contrário.
     */
    public boolean atualizar(Object objeto) {
        return false;
    }

    /**
     * Exclui um objeto do DAO.
     *
     * @param objeto O objeto a ser excluído.
     * @return True se o objeto for excluído com sucesso, False caso contrário.
     */
    public boolean excluir(Object objeto) {
        return false;
    }

    /**
     * Atualiza o arquivo de texto com os diretórios.
     *
     * @param diretorios A lista de diretórios.
     */
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
    public void salvarMusicasPlaylist(List <Musica> songs){

    }
    public boolean createPlaylist(UsuarioVip usuarioVIP, String nomeArquivo, List <Musica> songs) throws IOException {
        String nomePlaylist = nomeArquivo;
        String caminhoPath = usuarioVIP.getDirectory().getCaminho() + "/" + nomePlaylist + "Playlist";
        String caminhoTXT = "src/playlist_" + nomePlaylist + ".txt";


        Directory playlist = new Directory();

        File diretorioPlaylist = new File(caminhoPath);

        // Verificar se a pasta já existe
        if (diretorioPlaylist.exists()) {
            System.out.println("A playlist de músicas do usuário já existe.");
            return false;
        }

        // Criar a pasta da playlist
        if (diretorioPlaylist.mkdirs()) {
            File txtPlaylist = new File(caminhoTXT);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtPlaylist))) {
                writer.write(usuarioVIP.getEmail() + ",," + usuarioVIP.getSenha()+",,"+nomeArquivo);
                writer.newLine();

                System.out.println("Arquivo de playlist criado com sucesso!");
            }

            playlist.setFile(diretorioPlaylist);
            playlist.setMusicasPlaylist(songs);
            playlist.setCaminho(caminhoPath);

            // Acessar um DAO de playlist e salvar informações
            salvarSrcDiretorio(playlist);
            salvarMusicasPlaylist(txtPlaylist, songs);
            bd_playlists.add(playlist);

            System.out.println("Pasta de músicas da playlist criada com sucesso.");
        } else {
            System.out.println("Erro ao criar a pasta de músicas da playlist.");
            return false;
        }

        return true;
    }

    public void salvarMusicasPlaylist(File txtPlaylist, List <Musica> songs){
        for (Musica musica : songs ){
            arquivo.escreverArquivo(txtPlaylist.getName(), musica.getTitulo()+","+musica.getArtista()+","+musica.getCaminho());
        }
    }
    public static List<String> getBd_dir_caminhos() {
        return bd_dir_caminhos;
    }

    public static List<Directory> getBd_diretorios() {
        return bd_diretorios;
    }

    public static final String getSrcDiretorios() {
        return SRC_DIRETORIOS;
    }

    public static final String getSrcMusicas() {
        return SRC_MUSICAS;
    }
}