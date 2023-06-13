package ufrn.imd.entities;

import java.io.File;
import java.util.List;

public interface IDirectory {

    boolean createPath(String nomeUsuario);
    boolean addSong(Musica music);
    boolean removeSong(Musica music);
    List<File> getAllFileSong();
   // boolean writeSongPathsToFile(String filePath); // STRING CAMINHO -> diretorios.txt
    List<String> readSongPathsFromTextFile(String filePath); // diretorios.txt -> List<String>

    String getPath();

    List <Musica> getMusicaList(); //retorna todas as musicas do diretorio;
    // Musica createSong (File file); // recebe um file e retorna uma Musica
}
