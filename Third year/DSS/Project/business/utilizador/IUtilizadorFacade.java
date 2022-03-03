package centro_reparacoes.business.utilizador;

import java.util.List;
import java.util.Map;

/*
 *  IUtilizador: - Inferface da classe Utilizador;
 */

public interface IUtilizadorFacade {

    Map<String, String> getLogin();

    Map<String, Trabalhador> getTrabalhadores();

    Map<String, Cliente> getClientes();

    String getNome(String id);

    Double getAvaliacao(String id);

    List<Tecnico> getTecnicos();

    List<FuncionarioBalcao> getFBs();

    int autenticarTrabalhador(String idU, String password);

    boolean validaLogin(String id, String password);

    boolean validaNIF(String nif);

    void terminarSessao(String idUtilizador);

    String adicionarTrabalhador(int estatuto, String nome, String email, String password);

    void lerTrabalhador(int estatuto, String nome, String email, String password, double d, int i1, int i2, List<String> l);

    boolean adicionaCliente(String nome, String nif, String email);

    boolean removeTrabalhador(String idTrabalhador);

    boolean haTrabalhadores();
    
    boolean avaliarCentro(String id, Double avaliacao);

    void incrementaRececao (String id_fb);

    void incrementaEntrega (String id_fb);

    void adicionaPedidoAoTecnico (String id_tecnico, String id_pedido);

    List<String> dadosTecnico(Tecnico t, List<Integer> resultados);

    List<String> infFB(FuncionarioBalcao fb);

    List<String> infTecnico(Tecnico t);
}
