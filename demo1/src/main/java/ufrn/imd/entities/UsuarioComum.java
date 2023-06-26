package ufrn.imd.entities;

import java.util.List;

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
     * Implemente o código necessário para tornar o usuário comum em um usuário VIP.
     * Por exemplo, você pode criar uma nova instância da classe UsuarioVIP e copiar os dados relevantes do usuário comum para o usuário VIP.
     */
    public void tornarVip() {
        // Implemente o código necessário para tornar o usuário comum em um usuário VIP
        // Por exemplo, você pode criar uma nova instância da classe UsuarioVIP e copiar os dados relevantes do usuário comum para o usuário VIP
    }
}
