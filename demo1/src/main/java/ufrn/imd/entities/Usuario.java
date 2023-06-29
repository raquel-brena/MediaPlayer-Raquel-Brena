package ufrn.imd.entities;

import ufrn.imd.DAO.usuarioDAO;

import java.util.List;

/**
 * Classe abstrata que representa um usuário do sistema.
 */
public abstract class Usuario {
    private static final usuarioDAO DAO_USUARIO = new usuarioDAO();
    private static int nextId = 1;
    private int id;
    private String nome;
    private String email;
    private String senha;
    private boolean isAdmin;
    private Directory directory;
    private boolean isOnline;

    /**
     * Construtor padrão.
     */
    public Usuario() {
    }

    /**
     * Construtor que recebe os dados do usuário.
     *
     * @param nome    O nome do usuário.
     * @param email   O email do usuário.
     * @param senha   A senha do usuário.
     * @param isAdmin Indica se o usuário é um administrador.
     */
    public Usuario(String nome, String email, String senha, boolean isAdmin) {
        this.id = getNextId();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.isAdmin = isAdmin;
        isOnline = false;
        directory = new Directory();
        createDirectory();
    }

    /**
     * Obtém o próximo ID disponível para o usuário.
     *
     * @return O próximo ID.
     */
    private static synchronized int getNextId() {
        return nextId++;
    }

    /**
     * Obtém o DAO do usuário.
     *
     * @return O DAO do usuário.
     */
    public static usuarioDAO getDaoUsuario() {
        return DAO_USUARIO;
    }

    /**
     * Obtém o ID do usuário.
     *
     * @return O ID do usuário.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Define o ID do usuário.
     *
     * @param id O ID do usuário.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtém o nome do usuário.
     *
     * @return O nome do usuário.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do usuário.
     *
     * @param nome O nome do usuário.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o email do usuário.
     *
     * @return O email do usuário.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o email do usuário.
     *
     * @param email O email do usuário.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtém a senha do usuário.
     *
     * @return A senha do usuário.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define a senha do usuário.
     *
     * @param senha A senha do usuário.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Obtém o diretório do usuário.
     *
     * @return O diretório do usuário.
     */
    public Directory getDirectory() {
        return directory;
    }

    /**
     * Define o diretório do usuário.
     *
     * @param directory O diretório do usuário.
     */
    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    /**
     * Cria o diretório do usuário.
     *
     * @return True se o diretório for criado com sucesso, False caso contrário.
     */
    public boolean createDirectory() {
        if (!isAdmin) {
            if (directory.createPath(nome)) {
                Directory.getDaoDiretorios().salvarMemoria(directory);
                Directory.getDaoDiretorios().salvarSrcDiretorio(directory);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica se o usuário é um administrador.
     *
     * @return True se o usuário for um administrador, False caso contrário.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Define se o usuário é um administrador.
     *
     * @param isAdmin Indica se o usuário é um administrador.
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Verifica se o usuário está online.
     *
     * @return True se o usuário estiver online, False caso contrário.
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     * Define se o usuário está online.
     *
     * @param online Indica se o usuário está online.
     */
    public void setOnline(boolean online) {
        isOnline = online;
    }
}
