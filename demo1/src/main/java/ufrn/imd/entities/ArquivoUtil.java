package ufrn.imd.entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArquivoUtil {

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
