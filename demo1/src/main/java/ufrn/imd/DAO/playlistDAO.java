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
public class playlistDAO {

    private static Map<Integer, List<Playlist>> bd_playlists;
    private ArquivoUtil arquivo = new ArquivoUtil();

    /**
     * Construtor padrão.
     */
    public playlistDAO() {
        bd_playlists = new HashMap<>();
    }


    public void lerArquivoPlaylist() {
        bd_playlists.clear();

        String caminhoPastaPlaylists = "src/playlists/"; // Substitua pelo caminho do diretório que você deseja pesquisar
        File folder = new File(caminhoPastaPlaylists);

        File[] txt = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (folder != null) {
            if (txt != null) {
                for (File file : txt) {
                    Playlist playlist = lerPlaylistArquivo(file).getSecond();
                    Integer idPlaylist = lerPlaylistArquivo(file).getFirst();

                    salvarMemoriaPlaylist(idPlaylist, playlist);
                }
            }
        }
    }

    private Pair<Integer, Playlist> lerPlaylistArquivo(File arquivo) {
        Integer idPlaylist = null;
        Playlist playlist = null;
        List<Musica> all_musics = Directory.getDaoDiretorios().getBd_allMusica();

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

                    for (Musica musica : all_musics) {
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



    public boolean salvarSrcPlaylist(int id, Playlist playlist)  {
        String caminhoTXT = "src/playlists/playlist_" + playlist.getNome() + ".txt";
        File txtPlaylist = new File(caminhoTXT);

        if (txtPlaylist.exists()) {
            System.out.println("arquivo txt ja existe");
        }

        try {
            if (txtPlaylist.createNewFile()) {
                //escreve no arquivo
                writePlaylistToFile(txtPlaylist, id, playlist);
                System.out.println("arquivo txt escrito");
            } else {
                System.out.println("Erro ao criar o arquivo txt escrito");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private void writePlaylistToFile(File txtPlaylist,int id, Playlist playlist) throws IOException {

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

    public boolean salvarMemoriaPlaylist(int ID, Playlist playlist) {

        if (playlist != null) {
            List<Playlist>  playlists = bd_playlists.getOrDefault(ID, new ArrayList<>());
            playlists.add(playlist);
            bd_playlists.put(ID, playlists);
        }
        return true;
    }

    public static Map<Integer,List<Playlist>> getBd_playlists() {
        return bd_playlists;
    }

    public static void setBd_playlists(Map<Integer, List<Playlist>> bd_playlists) {
        playlistDAO.bd_playlists = bd_playlists;
    }
}