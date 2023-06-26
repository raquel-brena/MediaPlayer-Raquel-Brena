package ufrn.imd.controller;

import ufrn.imd.entities.Musica;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PanelNovaMusicaController implements Initializable {
    @FXML
    private TextField textFieldMusicaTitulo;

    @FXML
    private TextField textFieldMusicaArtista;

    @FXML
    private TextField textFieldCaminho;

    @FXML
    private Button CadastroBotaoConfirmar;

    @FXML
    private Button CadastroBotaoCancelar;

    private Musica musicaNova = new Musica();
    private File arquivo;

    private boolean buttonConfirmar = false;
    private Stage dialogStage;

    /**
     * Abre o seletor de arquivos para escolher uma música.
     * A música selecionada será atribuída ao objeto Musica.
     */
    @FXML
    public void showFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar música");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivos MP3 (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);
        this.arquivo = fileChooser.showOpenDialog(dialogStage);

        if (arquivo != null) {
            musicaNova.setFile(arquivo);
            textFieldCaminho.setText(arquivo.getPath());
        }
    }

    /**
     * Executa a ação do botão de cadastrar música.
     * Extrai os valores dos campos de texto e atribui à música.
     * Se os campos não estiverem preenchidos corretamente, exibe uma mensagem de erro.
     * Fecha a janela de diálogo quando o cadastro for confirmado.
     */
    @FXML
    public void ButtonCadastrarMusica() {
        String titulo = textFieldMusicaTitulo.getText();
        String artista = textFieldMusicaArtista.getText();
        musicaNova.setTitulo(titulo);
        musicaNova.setArtista(artista);

        if (musicaNova.getFile() != null) {

            if (musicaNova.getTitulo().isEmpty()) {
                musicaNova.setTitulo(musicaNova.getFile().getName());
            }
            if (musicaNova.getArtista().isEmpty()) {
                musicaNova.setArtista("Artista Desconhecido");
            }
            buttonConfirmar = true;
            dialogStage.close();
        } else {
            buttonConfirmar = false;
        }
    }

    /**
     * Retorna a música cadastrada.
     *
     * @return A música cadastrada.
     */
    public Musica getMusicaNova() {
        return musicaNova;
    }

    /**
     * Verifica se o botão de confirmação foi clicado.
     *
     * @return true se o botão de confirmação foi clicado, false caso contrário.
     */
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmar;
    }

    /**
     * Define o estágio de diálogo.
     *
     * @param dialogStage O estágio de diálogo.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Executa a ação do botão de cancelar.
     * Define que o botão de confirmação não foi clicado.
     * Fecha a janela de diálogo.
     */
    @FXML
    public void handleButtonCancelar() {
        buttonConfirmar = false;
        dialogStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
