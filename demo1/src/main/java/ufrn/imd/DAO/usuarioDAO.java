package ufrn.imd.DAO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import ufrn.imd.entities.*;

/**
 * Classe responsável pela persistência dos dados dos usuários em um arquivo de texto.
 */
public class usuarioDAO  {


    private static final String SRC_USUARIOS = "C:\\Users\\RB\\Desktop\\java\\mediaplyer\\demo1\\src\\usuarios.txt";

    private static List<Usuario> bd_usuarios;
    private static int id = 0;
    private ArquivoUtil arquivo = new ArquivoUtil();

    /**
     * Construtor da classe usuarioDAO.
     */
    public usuarioDAO() {
        bd_usuarios = new ArrayList<>();
        lerArquivoUsuario();
    }

    /**
     * Lê os usuários do arquivo de texto e adiciona-os ao usuariosDAO.
     */
    public void lerArquivoUsuario() {
        bd_usuarios.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(SRC_USUARIOS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dadosUsuario = linha.split(",");
                Usuario usuario;
                if (dadosUsuario[5].equals("Usuario Comum")){
                    usuario = new UsuarioComum(dadosUsuario[1],dadosUsuario[2],dadosUsuario[3],Boolean.parseBoolean(dadosUsuario[4]));
                usuario.setId(Integer.parseInt(dadosUsuario[0]));
                } else {
                    usuario = new UsuarioVip(dadosUsuario[1],dadosUsuario[2],dadosUsuario[3],Boolean.parseBoolean(dadosUsuario[4]));
                    usuario.setId(Integer.parseInt(dadosUsuario[0]));
                }
                //adiciondo diretorio ao usuário.
                Directory directory = new Directory();
                directory.getDaoDiretorios().lerArquivoDiretorio();
                for (Directory direcAux: directory.getDaoDiretorios().getBd_diretorios()){
                    if (direcAux.getCaminho().contains(usuario.getNome())){
                        usuario.setDirectory(direcAux);
                    }
                }

                bd_usuarios.add(usuario);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("tentou ler");
        }
    }

    /**
     * Salva a lista de usuários no arquivo de texto.
     *
     * @param usuarios Lista de usuários a serem salvos
     */

    public void atualizarArquivo(List<Usuario> usuarios) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SRC_USUARIOS, false))) {
            for (Usuario usuario : usuarios) {
                String linhaEscrever = usuario.getId() + "," + usuario.getNome() + "," + usuario.getEmail() + "," + usuario.getSenha() + "," + usuario.isAdmin()+ "," +  usuario.getClass().getSimpleName();
                bw.write(linhaEscrever);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifica se um email de usuário já existe no arquivo de texto.
     *
     * @param email Email a ser verificado
     * @return true se o email já existe, false caso contrário
     */
    public static boolean existeUsuario(String nome, String email) {
        for (Usuario usuario : bd_usuarios) {
            if (usuario.getEmail().equals(email) || usuario.getNome().equals(nome)) {
                return true; // Usuário encontrado
            }
        }
        return false; // Usuário não encontrado
    }

    /**
     * Adiciona um novo usuário ao arquivo de texto.
     *
     * @param usuario Usuário a ser adicionado
     * @return true se o usuário foi adicionado com sucesso, false caso contrário
     */

    public boolean salvar(Usuario usuario) {
        bd_usuarios.add(usuario);
        for (Usuario usuarios : bd_usuarios) {
            System.out.println(usuarios.getEmail());
        }
        String linhaEscrever = usuario.getId() + "," + usuario.getNome() + "," + usuario.getEmail() + "," + usuario.getSenha() + "," + usuario.isAdmin()+ "," +  usuario.getClass().getSimpleName();

        arquivo.escreverArquivo(SRC_USUARIOS, linhaEscrever);
        return true; // Retorna verdadeiro para indicar que o usuário foi adicionado com sucesso
    }

    /**
     * Exclui um usuário do arquivo de texto.
     *
     * @param id ID do usuário a ser excluído
     */
    public void excluirUsuario(int id) {
        boolean encontrado = false;

        for (Usuario usuario : bd_usuarios) {
            if (usuario.getId() == id) {
                bd_usuarios.remove(usuario);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            atualizarArquivo(bd_usuarios);
            System.out.println("Usuário excluído com sucesso.");
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    /**
     * Visualiza os dados de um usuário no arquivo de texto.
     *
     * @param id ID do usuário a ser visualizado
     */
    public void visualizarUsuario(int id) {
        List<Usuario> usuarios = listar();

        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                System.out.println(usuario);
                return;
            }
        }

        System.out.println("Usuário não encontrado.");
    }

    /**
     * Limpa a lista de usuários.
     */
    public void clear() {
        bd_usuarios.clear();
    }

    /**
     * Lista todos os usuários armazenados no arquivo de texto.
     *
     * @return Lista de usuários
     */
    public List<Usuario> listar() {
        for (Usuario usuarioss : bd_usuarios) {
            System.out.println(usuarioss.getEmail());
        }

        return bd_usuarios;
    }


    public Usuario updateVIP(Usuario usuarioOnline) {

            System.out.println("instancia ok ");
            Usuario usuarioVIP = new UsuarioVip();
            for (int i = 0; i < bd_usuarios.size(); i++) {
                Usuario usuario = bd_usuarios.get(i);
                if (usuarioOnline.getId() == usuario.getId()) {
                    System.out.println("encontrado ok ");
                    // Atualizar informações do usuário para VIP
                    usuarioVIP.setId(usuario.getId());
                    usuarioVIP.setNome(usuario.getNome());
                    usuarioVIP.setEmail(usuario.getEmail());
                    usuarioVIP.setSenha(usuario.getSenha());
                    usuarioVIP.setAdmin(usuario.isAdmin());
                    usuarioVIP.setDirectory(usuario.getDirectory());


                    // Substituir usuário comum pelo usuário VIP na lista
                    bd_usuarios.set(i, usuarioVIP);
                    break;
                }



            // Atualizar arquivo de texto
            atualizarArquivo(bd_usuarios);
            System.out.println("Usuário atualizado para VIP com sucesso.");
            return usuarioVIP;
        }
        return null;
    }




}
