package ufrn.imd.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ufrn.imd.entities.Usuario;
import ufrn.imd.entities.UsuarioComum;
import ufrn.imd.entities.UsuarioVip;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerVIP implements Initializable {

    @FXML
    private Label valorVIP;

    @FXML
    private Label msgSucessoLabel;


    @FXML
    private Button virarVIPButton;

    @FXML
    private Button cancelarButton;

    private boolean buttonConfirmar;

    Usuario usuarioVIP = new UsuarioVip();
    Usuario usuarioComum = new UsuarioComum();
    private Stage dialogStage;
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        buttonConfirmar = false;
        this.msgSucessoLabel.setVisible(false);
    }

    public Label getValorVIP() {
        return valorVIP;
    }

    public void setValorVIP(Label valorVIP) {
        this.valorVIP = valorVIP;
    }

    @FXML
    public void comprarVIP(){

       this.usuarioVIP = usuarioVIP.getDaoUsuario().updateVIP(usuarioComum);


        this.buttonConfirmar = true;
        msgSucessoLabel.setVisible(true);
    }

    @FXML
    public void handleButtonCancelar() {
        this.dialogStage.close();
    }

    public boolean isButtonConfirmar() {
        return buttonConfirmar;
    }

    public void setButtonConfirmar(boolean buttonConfirmar) {
        this.buttonConfirmar = buttonConfirmar;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setUsuarioComum(Usuario usuarioOnline) {

        this.usuarioComum = usuarioOnline;
    }

    public Usuario getUsuarioVIP() {
        return usuarioVIP;
    }

    public void setUsuarioVIP(Usuario usuarioVIP) {
        this.usuarioVIP = usuarioVIP;
    }

    public Usuario getUsuarioComum() {
        return usuarioComum;
    }
}
