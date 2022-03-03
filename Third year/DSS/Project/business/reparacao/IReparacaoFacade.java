package centro_reparacoes.business.reparacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/*
 *  IReparacao: - Inferface da classe Reparacao;
 */

public interface IReparacaoFacade {

    Map<String, Pedido> getPedidos();

    int getUrgencia(String idP);

    int getTipoPedido(String idP);

    double getOrcamentoInicial(String id_pedido);

    int verificaTipo(String id_pedido);

    void setHoraInicioReparacao(String id_pedido);

    void setHoraFimReparacao(String id_pedido);

    void setEstadoPedido(String id_pedido, int estado);

    void setOrcamento(String id_pedido, double orcamento);

    String criaPedido(int tipo_pedido, String id_cliente, int urgencia);

    void adicionarPedido(int tipo_pedido, String id_pedido, String id_cliente, int urgencia);

    void adicionarPedidoSemPT(int tipo_pedido, String id_pedido, String id_cliente, int estado, int urgencia, LocalDateTime data);

    public int adicionarPedidoData(int tipo_pedido, String id_pedido, String id_cliente, String id_equipamento, int estado, int urgencia, LocalDateTime dataPedido,
    LocalDateTime dataInicioReparacao, LocalDateTime dataFimReparacao, double orcamento, boolean pagamento, boolean levantamento,
    boolean abandono);

    boolean adicionaPassoCusto(String id_pedido, String passo, int custo, int minutos);

    void concluiReparacao(String id_pedido);

    void concluiPedido(String id_pedido);

    void colocarEmEspera(String id_pedido);

    int calculaTotalMinutos(String id_pedido);

    double calculaOrcamento(String id_pedido);

    void adicionaPlano(String id_pedido, List<String> passos, List<Integer> custos, List<Integer> tempo);
    
    boolean haPlanoDeTrabalho(String id_pedido);

    boolean haPedidos();

    String getIDMaisAntigo();

    String getIDMaisUrgente();

    String getIDFromNIF(int funcao, String nif);

    boolean orcamentoExtra(String idP);

    int alteraEstadoPedido(String idP, int estado);

    List<Integer> calculaNDD(List<String> idPs);

    Map<String, List<String>> getTodosPassos(List<String> idPs);

    List<String> getPArquivados();

    List<String> getPAbandonados();

    List<String> getPAtuais();
}
