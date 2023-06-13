package ufrn.imd.entities;

import ufrn.imd.DAO.usuarioDAO;

public abstract class Usuario {
    private static int nextId = 1;
    private int id;
    private String nome;
    private String email;
    private String senha;
    private boolean isAdmin;
    private Directory directory;
    private boolean isOnline;
    private static usuarioDAO DAO_USUARIO = new usuarioDAO();

    public Usuario() {
    }
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

    private static synchronized int getNextId() {
        return nextId++;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Integer getId() {
         return id;
}
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Directory getDirectory() {
        return directory;
    }
    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    public boolean createDirectory() {
        directory.getDaoDiretorios().salvarMemoria(directory);
     if(directory.createPath(nome)) {
         directory.getDaoDiretorios().salvarSrcDiretorio(directory);
     }
       return true;
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public usuarioDAO getDaoUsuario() {
        return DAO_USUARIO;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}

