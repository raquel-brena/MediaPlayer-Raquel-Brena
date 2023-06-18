package ufrn.imd.controller;

import com.sun.glass.ui.ClipboardAssistance;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerVIP {

    @FXML
    private Label valorVIP;

    @FXML
    private Button virarVIPButton;

    @FXML
    private Button cancelarButton;

    private boolean buttonConfirmar = false;

    private Stage dialogStage;
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Label getValorVIP() {
        return valorVIP;
    }

    public void setValorVIP(Label valorVIP) {
        this.valorVIP = valorVIP;
    }

    @FXML
    public void comprarVIP(){
        buttonConfirmar = true;
    }

    @FXML
    public void handleButtonCancelar() {
        buttonConfirmar = false;
        this.dialogStage.close();
    }

    public boolean isButtonConfirmar() {
        return buttonConfirmar;
    }

    public void setButtonConfirmar(boolean buttonConfirmar) {
        this.buttonConfirmar = buttonConfirmar;
    }
}
