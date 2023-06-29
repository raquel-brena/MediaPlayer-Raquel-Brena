package ufrn.imd.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ufrn.imd.DAO.usuarioDAO;
import ufrn.imd.entities.Musica;
import ufrn.imd.entities.Usuario;
import ufrn.imd.entities.UsuarioComum;
import ufrn.imd.entities.UsuarioVip;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador para o painel de cadastro de usuários.
 */
public class ControllerAdmin implements Initializable {

    @FXML
    private TextField textFieldCadastroNome;
    @FXML
    private TextField textFieldCadastroEmail;
    @FXML
    private TextField textFieldCadastroSenha;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Usuario usuarioSelecionado;


    @FXML
    private Label usernameLabel;
    @FXML
    private Label vipStatusLabel;

    @FXML
    private ListView<Usuario> listViewUsuarios;

    @FXML
    private ListView<Musica> listViewMusicas;
    private ObservableList<Musica> MusicaObservableList;
    private ObservableList<Usuario> usuariosObservableList;
    private List<Usuario> usuariosCadastrados;

    /**
     * Construtor da classe controllerCadastros.
     * Cria uma nova instância de UsuarioComum.
     */
    public ControllerAdmin() {
        usuarioSelecionado = new UsuarioComum();
        usuariosCadastrados = new ArrayList<>();
        listViewUsuarios = new ListView<>();
        listViewMusicas = new ListView<>();
        usuariosObservableList = FXCollections.observableArrayList();
        MusicaObservableList = FXCollections.observableArrayList();
        atualizarListaUsuarios();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void selecionarUsuario() {
        atualizarListaUsuarios();

        usuarioSelecionado = listViewUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSelecionado != null) {
            List<Musica> musicasUsuarioSelecionado = usuarioSelecionado.getDirectory().getAllSongs();

            atualizarListaMusica(musicasUsuarioSelecionado);
            usernameLabel.setText(usuarioSelecionado.getNome().toUpperCase());

            if (usuarioSelecionado instanceof UsuarioVip) {
                vipStatusLabel.setText("VIP ATIVO");
            } else {
                vipStatusLabel.setText("VIP INATIVO");
            }
        }
    }

    public void atualizarListaMusica(List<Musica> SONGS) {

        if (SONGS != null) {
            MusicaObservableList = FXCollections.observableArrayList();
            for (Musica musica : SONGS) {
                MusicaObservableList.add(musica);
            }

            listViewMusicas.setItems(MusicaObservableList);

            listViewMusicas.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Musica musica, boolean empty) {
                    super.updateItem(musica, empty);

                    if (empty || musica == null) {
                        setText(null);
                        setStyle(""); // Limpa o estilo da célula
                    } else {
                        setText(musica.getTitulo() + " - " + musica.getArtista());

                        // Verifica se a música está selecionada
                        if (listViewMusicas.getSelectionModel().getSelectedItem() == musica) {
                            setStyle("-fx-background-color: lightblue;"); // Define o estilo da célula selecionada
                        } else {
                            setStyle(""); // Limpa o estilo da célula não selecionada
                        }
                    }
                }
            });
        }
    }


    public void atualizarListaUsuarios() {
        usuariosCadastrados = usuarioDAO.getBd_usuarios();
        // mediaPlayer.dispose();
        usuariosObservableList = FXCollections.observableArrayList();

        if (usuariosCadastrados != null) {
            for (Usuario usuario : usuariosCadastrados) {
                usuariosObservableList.add(usuario);
            }

            listViewUsuarios.setItems(usuariosObservableList);

            listViewUsuarios.setCellFactory(param -> new ListCell<Usuario>() {
                @Override
                protected void updateItem(Usuario usuario, boolean empty) {
                    super.updateItem(usuario, empty);

                    if (empty || usuario == null) {
                        setText(null);
                        setStyle(""); // Limpa o estilo da célula
                    } else {
                        setText(usuario.getNome() + " - " + usuario.getEmail());

                        // Verifica se o usuário está selecionado
                        if (listViewUsuarios.getSelectionModel().getSelectedItem() == usuario) {
                            setStyle("-fx-background-color: lightblue;"); // Define o estilo da célula selecionada
                        } else {
                            setStyle(""); // Limpa o estilo da célula não selecionada
                        }
                    }
                }
            });
        }
    }


    @FXML
    public void removeMusicButton() {
        usuarioSelecionado = listViewUsuarios.getSelectionModel().getSelectedItem();

        usuarioSelecionado.getDirectory().excluirTodasAsMusicas(); //atualizar para excluir pasta
        if (usuarioSelecionado instanceof UsuarioVip) {
            ((UsuarioVip) usuarioSelecionado).excluirTodasAsPlaylists();
        }
        usuarioDAO.remove(usuarioSelecionado.getId());
        atualizarListaUsuarios();

        listViewUsuarios.getSelectionModel().clearSelection();  // Limpe qualquer seleção
        atualizarListaMusica(null);
    }

    /**
     * Obtém o novo usuário cadastrado.
     *
     * @return O novo usuário cadastrado.
     */
    public Usuario getNovoUsuario() {
        return this.usuarioSelecionado;
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

            this.usuarioSelecionado = novoID;

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
