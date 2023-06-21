package ufrn.imd.entities;

import java.util.List;

public class UsuarioComum extends Usuario {
    public UsuarioComum() {
        super();
    }

    public UsuarioComum(String nome, String email, String senha, boolean isAdmin) {
        super(nome, email, senha, isAdmin);
    }



    public void tornarVip() {
        // Implemente o código necessário para tornar o usuário comum em um usuário VIP
        // Por exemplo, você pode criar uma nova instância da classe UsuarioVIP e copiar os dados relevantes do usuário comum para o usuário VIP
    }
}
