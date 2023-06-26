package ufrn.imd.DAO;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ufrn.imd.entities.*;

/**
 * Classe responsável pela persistência dos dados dos usuários em um arquivo de texto.
 */
public class usuarioDAO  {
    private static final String SRC_USUARIOS = "src/usuarios.txt";
    private static List<Usuario> bd_usuarios;
    private static List<Usuario> bd_admins;
    private static int id = 0;
    private ArquivoUtil arquivo = new ArquivoUtil();
    UsuarioVip masterAdmin;

    /**
     * Construtor da classe usuarioDAO.
     */
    public usuarioDAO() {
        bd_usuarios = new ArrayList<>();
        bd_admins = new ArrayList<>();
        lerArquivoUsuario();
        UsuarioVip admin = new UsuarioVip("admin","admin@email.com","senha", true);
        bd_admins.add(admin);
    }

    /**
     * Lê os usuários do arquivo de texto e adiciona-os ao usuariosDAO.
     */
    public void lerArquivoUsuario() {
        bd_usuarios.clear();

        // Adicionando diretório ao usuário
        Directory directory = new Directory();
        directory.getDaoDiretorios().lerArquivoDiretorio();
        Playlist.getDaoPlaylist().lerArquivoPlaylist();

        try (BufferedReader br = new BufferedReader(new FileReader(SRC_USUARIOS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dadosUsuario = linha.split(",");
                Usuario usuario = criarUsuario(dadosUsuario);

                for (Directory direcAux : directory.getDaoDiretorios().getBd_diretorios()) {
                    System.out.println(direcAux.getCaminho() + " - " + usuario.getNome());
                    String caminho = "src\\main\\diretorios\\" + usuario.getNome() + "Diretorio";
                    if (direcAux.getCaminho().equals(caminho)) {
                        usuario.setDirectory(direcAux);
                        System.out.println("Diretório setado no usuário: " + direcAux.getCaminho());

                        for (Musica musc: directory.getAllSongs()){
                            System.out.println(musc.getTitulo());
                        }
                    }
                }

                System.out.println(usuario.getId());
                bd_usuarios.add(usuario);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao tentar ler o arquivo.");
        }
    }



    private Usuario criarUsuario(String[] dadosUsuario) {
        Usuario usuario;
        boolean isVip;
        if (dadosUsuario[5].equals("UsuarioVip")){
            isVip = true;
        } else {
            isVip = false;
        }

        if (isVip) {
            usuario = new UsuarioVip(dadosUsuario[1],dadosUsuario[2],dadosUsuario[3],Boolean.parseBoolean(dadosUsuario[4]));
        } else {
            usuario = new UsuarioComum(dadosUsuario[1],dadosUsuario[2],dadosUsuario[3],Boolean.parseBoolean(dadosUsuario[4]));
        }

        usuario.setId(Integer.parseInt(dadosUsuario[0]));

        if (isVip) {
           //Playlist playlistsUsuario = diretoriosDAO.getBd_playlists().get(usuario.getId());

            List <Playlist> playlistsUsuario = playlistDAO.getPlaylistsDatabase().get(usuario.getId());
            if (playlistsUsuario!= null){
                ((UsuarioVip) usuario).setPlaylists(playlistsUsuario);
            }
        }
        return usuario;
    }
    /**
     * Salva a lista de usuários no arquivo de texto.
     *
     * @param usuarios Lista de usuários a serem salvos
     */

    public void atualizarArquivo(List<Usuario> usuarios) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SRC_USUARIOS, false))) {
            for (Usuario usuario : usuarios) {
                if (!usuario.isAdmin()){
                String linhaEscrever = usuario.getId() + "," + usuario.getNome() + "," + usuario.getEmail() + "," + usuario.getSenha() + "," + usuario.isAdmin()+ "," +  usuario.getClass().getSimpleName();
                bw.write(linhaEscrever);
                bw.newLine();
            }}
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

    /*
     * Exclui um usuário do arquivo de texto.
     *
     * @param id ID do usuário a ser excluído
     */

    /*
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
    }*/

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


    public UsuarioVip updateVIP(UsuarioComum usuarioOnline) {
            System.out.println("instancia ok ");
            UsuarioVip usuarioVIP = new UsuarioVip();

            for (int i = 0; i < bd_usuarios.size(); i++) {
                Usuario usuario = bd_usuarios.get(i);
                System.out.println("id buscador" + usuario.getId());
                System.out.println("id online" + usuarioOnline.getId());

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
                    atualizarArquivo(bd_usuarios);
                    System.out.println("Usuário atualizado para VIP com sucesso.");
                }

        }
        // Atualizar arquivo de texto
        if (usuarioVIP instanceof UsuarioVip){
            System.out.println("updateVIP: " + usuarioVIP.getNome());
        }
        return usuarioVIP;
    }


    public static List<Usuario> getBd_usuarios() {
        return bd_usuarios;
    }

    public static List<Usuario> getBd_admins() {
        return bd_admins;
    }

    public static void setBd_usuarios(List<Usuario> bd_usuarios) {
        usuarioDAO.bd_usuarios = bd_usuarios;
    }
}
