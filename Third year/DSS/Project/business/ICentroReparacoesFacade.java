package centro_reparacoes.business;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import centro_reparacoes.business.armazem.*;
import centro_reparacoes.business.reparacao.*;
import centro_reparacoes.business.utilizador.*;

/*
 *  ICentroReparacoes: - Inferface da classe CentroReparacoes;
 */

public interface ICentroReparacoesFacade {

    boolean registaPedido(String id_fb, int tipo_pedido, String nome, String nif, String email, String id_equipamento,int urgencia);

    boolean alteraEstadoPedido(String id_tecnico, String id_pedido, int estado);

    boolean registarEntrega(String id_fb, String nif);

    Map<String, List<String>> getArquivados();

    Map<String, List<String>> getAbandonados();

    void removeEquipamentoArmazem(String id_equipamento);

    void adicionaEquipamento(String id, String nomeE);

    boolean haEquipamentos();

    Map<String, Equipamento> getAllEquipamentos();

    void adicionaPedido(int tipo_pedido, String id_pedido, String id_cliente, String id_equipamento, int estado, int urgencia, LocalDateTime dataPedido,
    LocalDateTime dataInicioReparacao, LocalDateTime dataFimReparacao, List<String> passos, List<Integer> custos, List<Integer> tempo, double orcamento,
    boolean pagamento, boolean levantamento, boolean abandono);

    void adicionaPedidoSemPT(int tipo_pedido, String id_pedido, String id_cliente, int estado, int urgencia, LocalDateTime data);
    
    boolean validaIDPedido(String idP);

    String getIDFromNIF(String nif);

    String getIDMaisAntigo();

    String getIDMaisUrgente();

    int getEstado(String idP);

    int getUrgencia(String idP);

    int getTipoPedido(String idP);

    boolean adicionaPassoeCusto(String id_pedido, String passo, int custo, int horas);

    boolean haPlanoDeTrabalho(String id_pedido);

    int calculaTotalMinutos(String id_pedido);

    int calculaEstimativaTempo(String id_pedido);

    double calculaOrcamento(String id_pedido);

    double concluirOrcamento(String id_pedido);

    Double getOrcamento(String idP);

    boolean orcamentoAcima(String id_pedido);

    void respostaOrcamento(String id_tecnico, String id_pedido, boolean resposta);

    void colocarPedidoEmEspera(String id_pedido);

    boolean registarConclusaoReparacao(String id_tecnico, String id_pedido);

    List<String> getPedidosAtuais();
    
    Map<String, Pedido> getAllPedidos();

    int autenticarUtilizador(String idMembro, String password);

    void terminarSessao(String idU);

    String getNome(String id);

    boolean haTrabalhadores();

    String adicionarTrabalhador(int estatuto, String nome, String email, String password);

    void lerTrabalhador(int estatuto, String nome, String email, String password, double d, int i1, int i2, List<String> l);

    void adicionaCliente(String nome, String nif, String email);

    boolean removeTrabalhador(String idTrabalhador);

    boolean avaliarCentro(String id, Double avaliacao);

    Double getAvaliacao(String idT);

    Map<String, Cliente> getAllClientes();

    Map<String, Trabalhador> getAllTrabalhadores();

    Map<String, List<String>> listagemNDD_TN();

    Map<String, List<String>> listagemRE_FB();

    Map<List<String>, Map<String, List<String>>> listagemP_TN();
}
