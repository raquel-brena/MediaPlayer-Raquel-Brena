package ufrn.imd.controller;

import ufrn.imd.DAO.usuarioDAO;
import ufrn.imd.entities.Usuario;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

import ufrn.imd.entities.UsuarioComum;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para o painel de cadastro de usuários.
 */
public class controllerCadastros implements Initializable {

    @FXML
    private TextField textFieldCadastroNome;

    @FXML
    private TextField textFieldCadastroEmail;

    @FXML
    private TextField textFieldCadastroSenha;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Usuario novoUsuario;

    /**
     * Construtor da classe controllerCadastros.
     * Cria uma nova instância de UsuarioComum.
     */
    public controllerCadastros() {
        novoUsuario = new UsuarioComum();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Obtém o novo usuário cadastrado.
     *
     * @return O novo usuário cadastrado.
     */
    public Usuario getNovoUsuario() {
        return this.novoUsuario;
    }

    /**
     * Verifica se o botão "Confirmar" foi clicado.
     *
     * @return true se o botão "Confirmar" foi clicado, caso contrário, false.
     */
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * Manipula o evento do botão "Confirmar".
     * Realiza a validação dos dados do usuário e cadastra o novo usuário se os dados forem válidos.
     * Exibe uma mensagem de erro se o usuário já estiver cadastrado.
     */
    @FXML
    public void handleButtonConfirmar() {
        String nome = textFieldCadastroNome.getText();
        String email = textFieldCadastroEmail.getText();
        String senha = textFieldCadastroSenha.getText();

        if (usuarioDAO.existeUsuario(nome, email)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Email ou Nickname já cadastrado");
            alert.setContentText("Por favor, tente novamente.");
            alert.showAndWait();
        } else {
            Usuario novoID = new UsuarioComum(nome, email, senha, false);

            this.novoUsuario = novoID;

            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Manipula o evento do botão "Cancelar".
     * Fecha o diálogo de cadastro.
     */
    @FXML
    public void handleButtonCancelar() {
        this.dialogStage.close();
        getDialogStage().close();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
