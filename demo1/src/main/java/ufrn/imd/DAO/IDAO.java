package ufrn.imd.DAO;

import java.util.List;

public interface IDAO<T> {

    List<T> listar();

    T buscarPorId(int id);

    boolean salvar(T objeto);

    boolean atualizar(T objeto);

    boolean excluir(T objeto);
    void lerArquivo();

    void atualizarArquivo(List<T> lista);
}
