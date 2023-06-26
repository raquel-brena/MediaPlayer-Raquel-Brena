package ufrn.imd.entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A classe ArquivoUtil fornece métodos para leitura e escrita em arquivos.
 */
public class ArquivoUtil {

    /**
     * Lê o conteúdo de um arquivo e retorna uma lista de linhas.
     *
     * @param nomeArquivo o nome do arquivo a ser lido
     * @return uma lista de strings contendo as linhas do arquivo
     */
    public List<String> lerArquivo(String nomeArquivo) {
        List<String> linhas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return linhas;
    }

    /**
     * Escreve uma linha em um arquivo.
     *
     * @param nomeArquivo o nome do arquivo a ser escrito
     * @param linha       a linha a ser escrita no arquivo
     * @return true se a linha foi escrita com sucesso, false caso contrário
     */
    public boolean escreverArquivo(String nomeArquivo, String linha) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo, true))) {
            bw.write(linha);
            bw.newLine();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
