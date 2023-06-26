package ufrn.imd.controller;

import javafx.scene.control.Button;
import ufrn.imd.MediaPlayer;
import ufrn.imd.entities.Usuario;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * Controlador para o painel de cadastro de usuários.
 */
public class ControllerAbout implements Initializable {

   @FXML
   private Button back;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Usuario novoUsuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setUsuarioOnline (Usuario usuario){
        this.novoUsuario = usuario;
    }

    /**
     * Construtor da classe controllerCadastros.
     * Cria uma nova instância de UsuarioComum.
     */
    public ControllerAbout() {
        ;
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
     * Manipula o evento do botão "Cancelar".
     * Fecha o diálogo de cadastro.
     */
    @FXML
    public void voltar (){
        MediaPlayer.changeScreen("player",novoUsuario.getNome());
    }



    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
