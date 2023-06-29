package ufrn.imd.entities;


/**
 * Classe que representa um usuário comum.
 */
public class UsuarioComum extends Usuario {
    public UsuarioComum() {
        super();
    }

    /**
     * Construtor que recebe os dados do usuário comum.
     *
     * @param nome    O nome do usuário.
     * @param email   O email do usuário.
     * @param senha   A senha do usuário.
     * @param isAdmin Indica se o usuário é um administrador.
     */
    public UsuarioComum(String nome, String email, String senha, boolean isAdmin) {
        super(nome, email, senha, isAdmin);
    }

    /**
     * Torna o usuário comum em um usuário VIP.
       */
    public void tornarVip() {
    }
}
