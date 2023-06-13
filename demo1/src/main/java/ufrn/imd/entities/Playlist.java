package ufrn.imd.entities;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Playlist {

    private String nome;
    private Map<Integer, Musica> musicas;

    public Playlist( String nome) {
        this.nome = nome;
        this.musicas = new HashMap<>();
    }

    public void adicionarMusica(Musica musica) {
        int posicao = musicas.size() + 1;
        musicas.put(posicao, musica);
    }

    public void removerMusica(int posicao) {
        musicas.remove(posicao);
    }

    public void limpar() {
        musicas.clear();
    }

    public int tamanho() {
        return musicas.size();
    }

    public Map<Integer, Musica> getMusicas() {
        return musicas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void alterarOrdem(List<Integer> novasPosicoes) {
        if (novasPosicoes.size() != musicas.size()) {
            throw new IllegalArgumentException("A lista de posições precisa ter o mesmo tamanho da playlist.");
        }

        Map<Integer, Musica> novoMap = new HashMap<>();
        for (int i = 0; i < novasPosicoes.size(); i++) {
            int novaPosicao = novasPosicoes.get(i);
            if (novaPosicao < 1 || novaPosicao > musicas.size()) {
                throw new IllegalArgumentException("Posição inválida na lista.");
            }
            novoMap.put(novaPosicao, musicas.get(i + 1));
        }
        musicas = novoMap;
    }
}
