package ufrn.imd.controller;

import ufrn.imd.MediaPlayer;
import ufrn.imd.entities.Usuario;
import ufrn.imd.entities.UsuarioComum;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador para o painel de login.
 */
public class ControllerLogin {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField textFieldLoginEmail;
    @FXML
    private TextField textFieldLoginSenha;
    @FXML
    private Button buttonLoginEntrar;
    @FXML
    private Button buttonLoginCadastrar;
    @FXML
    private MenuItem mnIAdicionarMusica;
    private boolean usuarioLogado = false;

    private static Usuario USUARIO;
    private static Usuario novoUsuario;
    private ControllerPlayer controllerPlayer;
    private controllerCadastros cadastros = new controllerCadastros();



    /**
     * Construtor da classe ControllerLogin.
     * Cria uma nova instância de UsuarioComum.
     */
    public ControllerLogin() {
        this.novoUsuario = new UsuarioComum();
        this.USUARIO = new UsuarioComum();
       // UsuarioVip masterAdmin = new UsuarioVip("Master Admin", "admin@email.com","admin123",true);
       // masterAdmin.getDaoUsuario().getBd_usuarios().add(masterAdmin);
    }
    /**
     * Define o controlador do player.
     *
     * @param controllerPlayer O controlador do player.
     */
    public void setControllerPlayer(ControllerPlayer controllerPlayer) {
        this.controllerPlayer = controllerPlayer;
    }
    /**
     * Manipula o evento do botão "Cadastrar".
     * Abre o painel de cadastro de usuários e realiza o cadastro se confirmado pelo usuário.
     *
     * @throws IOException Exceção lançada se houver um erro ao carregar o arquivo FXML do painel de cadastro.
     */
    @FXML
    public void handleButtonCadastrar() throws IOException {
        boolean buttonConfirmarCadastro = showFXMLPanelCadastro();

        if (buttonConfirmarCadastro) {
            if (novoUsuario.getDaoUsuario().salvar(novoUsuario)) {
                String mensagem = "Cadastro realizado! Seja bem-vindo(a) " + novoUsuario.getNome() + "\n Lembre de seus dados:\n" + "Login: " + novoUsuario.getEmail();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensagem);
                alert.showAndWait();
            }
        } else {
            String mensagem = "Cadastro cancelado.";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, mensagem);
            alert.showAndWait();
        }
    }
    /**
     * Manipula o evento do botão "Entrar".
     * Verifica as credenciais do usuário e realiza o login se válido.
     *
     * @throws IOException Exceção lançada se houver um erro ao carregar o arquivo FXML do painel do player.
     */
    @FXML
    public void handleButtonEntrar() throws IOException {
        String email = textFieldLoginEmail.getText();
        String senha = textFieldLoginSenha.getText();

        for (Usuario usuario : novoUsuario.getDaoUsuario().listar()) {
            if (usuario.getEmail().equals(email) && usuario.getSenha().equals(senha)) {
                USUARIO = usuario;
                USUARIO.setOnline(true);
                textFieldLoginEmail.clear();
                textFieldLoginSenha.clear();
                usuarioLogado = true;

                MediaPlayer.changeScreen("player", usuario.getNome());
                System.out.print("class login entrar: diretorio: " + USUARIO.getDirectory().toString());
                controllerPlayer.setUsuarioOnline(USUARIO);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Seja bem-vindo(a)!");
                alert.showAndWait();
                return;
            }
        }
        for (Usuario usuario : novoUsuario.getDaoUsuario().getBd_admins()) {
            if (usuario.getEmail().equals(email) && usuario.getSenha().equals(senha)) {
                USUARIO = usuario;
                USUARIO.setOnline(true);
                textFieldLoginEmail.clear();
                textFieldLoginSenha.clear();
                usuarioLogado = true;

                MediaPlayer.changeScreen("player", usuario.getNome());
                System.out.print("class login entrar: diretorio: " + USUARIO.getDirectory().toString());
                controllerPlayer.setUsuarioOnline(USUARIO);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Seja bem-vindo(a)!");
                alert.showAndWait();
                return;
            }
            }
        System.out.println("Usuário não encontrado.");
    }

    /**
     * Exibe o painel de cadastro de usuários.
     *
     * @return Retorna verdadeiro se o botão de confirmação do cadastro foi clicado, falso caso contrário.
     * @throws IOException Exceção lançada se houver um erro ao carregar o arquivo FXML do painel de cadastro.
     */
    public boolean showFXMLPanelCadastro() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(controllerCadastros.class.getResource("panel-cadastros.fxml"));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Usuario");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(null);

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no Controller de Cadastros
        controllerCadastros controller = loader.getController();
        controller.setDialogStage(dialogStage);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();
        this.novoUsuario = controller.getNovoUsuario();

        return controller.isButtonConfirmarClicked();
    }
}
