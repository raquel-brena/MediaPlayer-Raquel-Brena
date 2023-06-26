package ufrn.imd.DAO;

import ufrn.imd.entities.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por acessar e manipular os dados das playlists.
 */
public class playlistDAO {

    private static Map<Integer, List<Playlist>> playlistsDatabase;
    private static final String PLAYLISTS_FOLDER_PATH = "src/playlists/";

    /**
     * Construtor padrão.
     */
    public playlistDAO() {
        playlistsDatabase = new HashMap<>();
    }

    /**
     * Lê as playlists a partir dos arquivos de texto.
     */
    public void lerArquivoPlaylist() {
        playlistsDatabase.clear();

        File folder = new File(PLAYLISTS_FOLDER_PATH);
        File[] txtFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (folder != null && txtFiles != null) {
            for (File file : txtFiles) {
                Pair<Integer, Playlist> playlistPair = lerPlaylistArquivo(file);
                Integer idPlaylist = playlistPair.getFirst();
                Playlist playlist = playlistPair.getSecond();
                salvarMemoriaPlaylist(idPlaylist, playlist);
            }
        }
    }

    /**
     * Lê os dados de uma playlist a partir de um arquivo de texto.
     *
     * @param arquivo O arquivo de texto da playlist.
     * @return Um par contendo o ID da playlist e a playlist lida do arquivo.
     * @throws RuntimeException Se ocorrer um erro ao ler o arquivo.
     */
    private Pair<Integer, Playlist> lerPlaylistArquivo(File arquivo) {
        Integer idPlaylist = null;
        Playlist playlist = null;
        List<Musica> allMusics = Directory.getDaoDiretorios().getBd_allMusica();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo.getPath()))) {
            String idPlaylistStr = br.readLine();
            if (idPlaylistStr != null && !idPlaylistStr.isEmpty()) {
                playlist = new Playlist();
                idPlaylist = Integer.valueOf(idPlaylistStr);

                String nomePlaylist = br.readLine();
                playlist.setNome(nomePlaylist);

                String linha;
                while ((linha = br.readLine()) != null) {
                    String caminhoMusicaPlaylist = linha;

                    for (Musica musica : allMusics) {
                        if (caminhoMusicaPlaylist.equals(musica.getCaminho())) {
                            playlist.getBd_musicasPlay().add(musica);
                        }
                    }
                }
            }
            return new Pair<>(idPlaylist, playlist);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean salvarMemoriaPlaylistsUsuario(int ID, List<Playlist> playlistsUsuario) {
        if (playlistsUsuario != null) {
            playlistsDatabase.put(ID, playlistsUsuario);
        }
        return true;
    }

    public boolean salvarMemoriaPlaylist(int ID, Playlist playlist) {
        if (playlist != null) {
            List<Playlist> playlists = playlistsDatabase.getOrDefault(ID, new ArrayList<>());
            playlists.add(playlist);
            playlistsDatabase.put(ID, playlists);
        }
        return true;
    }

    public boolean salvarSrcPlaylist(int id, List<Playlist> playlistsUsuario) {
        for (Playlist playlist : playlistsUsuario) {
            String caminhoTXT = PLAYLISTS_FOLDER_PATH + "playlist_" + playlist.getNome() + ".txt";
            File txtPlaylist = new File(caminhoTXT);

            if (txtPlaylist.exists()) {
                System.out.println("Arquivo txt já existe.");
            }
            try {
                if (txtPlaylist.createNewFile()) {
                    writePlaylistToFile(txtPlaylist, id, playlist);
                    System.out.println("Arquivo txt escrito.");
                } else {
                    System.out.println("Erro ao criar o arquivo txt.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    /**
     * Escreve uma playlist em um arquivo de texto.
     *
     * @param txtPlaylist O arquivo de texto da playlist.
     * @param id          O ID da playlist.
     * @param playlist    A playlist a ser escrita no arquivo.
     * @throws IOException Se ocorrer um erro ao escrever no arquivo.
     */
    private void writePlaylistToFile(File txtPlaylist, int id, Playlist playlist) throws IOException {
        FileWriter fileWriter = new FileWriter(txtPlaylist);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write(String.valueOf(id));
        bufferedWriter.newLine();
        bufferedWriter.write(playlist.getNome());
        bufferedWriter.newLine();

        for (Musica musica : playlist.getBd_musicasPlay()) {
            bufferedWriter.write(musica.getCaminho());
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
        fileWriter.close();
    }

    /**
     * Obtém o banco de dados das playlists.
     *
     * @return O banco de dados das playlists.
     */
    public static Map<Integer, List<Playlist>> getPlaylistsDatabase() {
        return playlistsDatabase;
    }


}
