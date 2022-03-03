package centro_reparacoes.business;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import centro_reparacoes.business.armazem.*;
import centro_reparacoes.business.reparacao.*;
import centro_reparacoes.business.utilizador.*;

/*
 *  CentroReparacoes:   - Classe que guarda os modelos dos três subsistemas (Armazem, Utilizador, Reparacao);
 *                      - Possui todos os métodos necessários para a resolução dos cenários, junta todos os subsistemas.
 */

public class CentroReparacoesFacade implements ICentroReparacoesFacade {
    private IArmazemFacade modelArmazem; // Model da classe Armazem
    private IUtilizadorFacade modelUtilizador; // Model da classe Utilizador
    private IReparacaoFacade modelReparacao; // Model da classe Reparacao

    public CentroReparacoesFacade() {
        this.modelArmazem = new ArmazemFacade();
        this.modelUtilizador = new UtilizadorFacade();
        this.modelReparacao = new ReparacaoFacade();
    }

    // Método: Regista o pedido de um cliente
    public boolean registaPedido(String id_fb, int tipo_pedido, String nomeC, String nif, String email, String nomeE, int urgencia) {
        if (this.modelUtilizador.validaNIF(nif)) {
            this.modelUtilizador.adicionaCliente(nomeC, nif, email);
            // Criar pedido expresso
            if (tipo_pedido == 1 && !this.modelReparacao.haPedidos()) {
                this.modelReparacao.criaPedido(tipo_pedido, nif, urgencia);
                this.modelUtilizador.incrementaRececao(id_fb);
                return true;
            // Criar pedido normal
            } else if (tipo_pedido == 0) {
                String id = modelReparacao.criaPedido(tipo_pedido, nif, urgencia);
                this.modelArmazem.adicionaEquipamento(id, nomeE);
                this.modelUtilizador.incrementaRececao(id_fb);
                return true;
            }
        }
        return false; // Retorna falso se nao for possivel registar o pedido
    }

    // Método: Altera o estado de um pedido
    public boolean alteraEstadoPedido(String id_tecnico, String id_pedido, int estado) {
        int resultado = this.modelReparacao.alteraEstadoPedido(id_pedido, estado);
        if (resultado == 0) {
            return true;
        } else if (resultado == 1) {
            this.modelUtilizador.adicionaPedidoAoTecnico(id_tecnico, id_pedido);
            return true;
        }
        return false;
    }

    // Método: Regista o pagamento e o levantamento do equipamento. Remove-o do armazém
    public boolean registarEntrega(String id_fb, String nif) {
        String idP = this.modelReparacao.getIDFromNIF(1, nif);
        if (!idP.equals("false")) {
            removeEquipamentoArmazem(idP);
            this.modelUtilizador.incrementaEntrega(id_fb);
            return true;
        }
        return false;
    }

    // Método: Obtém um mapa com informação dos equipamentos arquivados
    public Map<String, List<String>> getArquivados() {
        List<String> idE = this.modelReparacao.getPArquivados();
        return this.modelArmazem.getInfArquivados(idE);
    }

    // Método: Obtém um mapa com informação dos equipamentos abandonados
    public Map<String, List<String>> getAbandonados() {
        List<String> idE = this.modelReparacao.getPAbandonados();
        return this.modelArmazem.getInfAbandonados(idE);
    }

    /**** SUBSISTEMA ARMAZÉM ****/

    // Método: Remove um equipamento do armazém depois do cliente o ter ido buscar
    public void removeEquipamentoArmazem(String id_equipamento) {
        this.modelArmazem.removeEquipamento(id_equipamento);
    }

    // Método: Adiciona um equipamento à lista de equipamentos
    public void adicionaEquipamento(String id, String nomeE) {
        this.modelArmazem.adicionaEquipamento(id, nomeE);
    }

    // Método: Verifica se existem equipamentos no armazém
    public boolean haEquipamentos() {
        return this.modelArmazem.haEquipamentos();
    }

    // Método: Obtém todos os equipamentos
    public Map<String, Equipamento> getAllEquipamentos() {
        return this.modelArmazem.getEquipamentos();
    }

    /**** SUBSISTEMA REPARAÇÃO ****/

    // Método: Adiciona um pedido à lista de pedidos
    public void adicionaPedido(int tipo_pedido, String id_pedido, String id_cliente, String id_equipamento, int estado, int urgencia, LocalDateTime dataPedido,
    LocalDateTime dataInicioReparacao, LocalDateTime dataFimReparacao, List<String> passos, List<Integer> custos, List<Integer> tempo, double orcamento,
    boolean pagamento, boolean levantamento, boolean abandono) {
        int flag = this.modelReparacao.adicionarPedidoData(tipo_pedido, id_pedido, id_cliente, id_equipamento, estado, urgencia, dataPedido,
        dataInicioReparacao, dataFimReparacao, orcamento, pagamento, levantamento, abandono);
        this.modelReparacao.adicionaPlano(id_pedido, passos, custos, tempo);

        if (flag == 2)
            this.modelArmazem.adicionaAbandonado(id_equipamento);
    }

    // Método: Adiciona uma novo pedido à lista de pedidos
    public void adicionaPedidoSemPT(int tipo_pedido, String id_pedido, String id_cliente, int estado, int urgencia, LocalDateTime data){
        this.modelReparacao.adicionarPedidoSemPT(tipo_pedido, id_pedido, id_cliente, estado, urgencia, data);
    }

    // Método: Validar se um código de pedido existe
    public boolean validaIDPedido(String idP) {
        if (this.modelReparacao.getPedidos().containsKey(idP)) {
            int estado = this.modelReparacao.getPedidos().get(idP).getEstado();
            if (estado == -1 || estado == 0 || estado == 3)
                return true;
        }
        return false;
    }

    // Método: Obtém o ID de pedido através de um NIF
    public String getIDFromNIF(String nif) {
        return this.modelReparacao.getIDFromNIF(0, nif);
    }

    // Método: Obtém o ID de pedido mais antigo
    public String getIDMaisAntigo() {
        return this.modelReparacao.getIDMaisAntigo();
    }

    // Método: Obtém o ID de pedido mais urgente
    public String getIDMaisUrgente() {
        return this.modelReparacao.getIDMaisUrgente();
    }

    // Método: Obtém o estado atual de um pedido
    public int getEstado(String idP) {
        return this.modelReparacao.getPedidos().get(idP).getEstado();
    }

    // Método: Obtém a urgência de um pedido
    public int getUrgencia(String idP) {
        return this.modelReparacao.getUrgencia(idP);
    }

    // Método: Obtém o tipo do pedido
    public int getTipoPedido(String idP) {
        return this.modelReparacao.getTipoPedido(idP);
    }

    // Método: Adiciona um passo, custo e tempo a um plano de trabalho de um pedido
    public boolean adicionaPassoeCusto(String id_pedido, String passo, int custo, int minutos) {
        if (!(this.modelReparacao.getPedidos().get(id_pedido).getEstado() == 3))
            return modelReparacao.adicionaPassoCusto(id_pedido, passo, custo, minutos);
        return false;
    }

    // Método: Verifica se existe um plano de trabalho de um pedido
    public boolean haPlanoDeTrabalho(String id_pedido) {
        return this.modelReparacao.haPlanoDeTrabalho(id_pedido);
    }

    // Método: Calcula o total de minutos que um pedido demorou a ser reparado
    public int calculaTotalMinutos(String id_pedido) {
        return this.modelReparacao.calculaTotalMinutos(id_pedido);
    }

    // Método: Calcula o tempo estimado do orçamento
    public int calculaEstimativaTempo(String id_pedido) {
        return this.modelReparacao.calculaTotalMinutos(id_pedido);
    }

    // Método: Calcula o orçamento final de um pedido
    public double calculaOrcamento(String id_pedido) {
        return this.modelReparacao.calculaOrcamento(id_pedido);
    }

    // Método: Calcula o orçamento final
    public double concluirOrcamento(String id_pedido) {
        double orcamento = calculaOrcamento(id_pedido);
        this.modelReparacao.setOrcamento(id_pedido, orcamento);
        return orcamento;
        // Envia email ao cliente atraves de um programa externo
    }

    // Método: Obtém o orçamento de um pedido
    public Double getOrcamento(String idP) {
        return this.modelReparacao.getOrcamentoInicial(idP);
    }

    // Método: Verifica se o orçamento atual está acima 120% do orçamento previsto
    public boolean orcamentoAcima(String id_pedido) {
        return this.modelReparacao.orcamentoExtra(id_pedido);
    }

    // Cliente responde por email a confirmar ou nao o orçamento atraves do programa externo
    public void respostaOrcamento(String id_tecnico, String id_pedido, boolean resposta) {
        if (!resposta) {
            alteraEstadoPedido(id_tecnico, id_pedido, 5);
        }
    }

    // Método: Coloca um pedido no estado de espera
    public void colocarPedidoEmEspera(String id_pedido) {
        this.modelReparacao.colocarEmEspera(id_pedido);
    }

    // Método: Registar a conclusão da reparação de um pedido
    public boolean registarConclusaoReparacao(String id_tecnico, String id_pedido) {
        this.modelReparacao.concluiReparacao(id_pedido);
        return alteraEstadoPedido(id_tecnico, id_pedido, 4);
    }

    // Método: Obtém todos os pedidos atuais
    public List<String> getPedidosAtuais() {
        return this.modelReparacao.getPAtuais();
    }

    // Método: Obtém todos os pedidos
    public Map<String, Pedido> getAllPedidos() {
        return this.modelReparacao.getPedidos();
    }

    /**** SUBSISTEMA UTILIZADOR ****/

    // Método: Autenticar utilizador
    public int autenticarUtilizador(String idUtilizador, String password) {
        return this.modelUtilizador.autenticarTrabalhador(idUtilizador, password);
    }

    // Método: Terminar sessão de utilizador
    public void terminarSessao(String idUtilizador) {
        this.modelUtilizador.terminarSessao(idUtilizador);
    }

    // Método: Obtém o nome de um trabalhador
    public String getNome(String id) {
        return this.modelUtilizador.getNome(id);
    }

    // Método: Verifica se existem trabalhadores no centro
    public boolean haTrabalhadores() {
        return this.modelUtilizador.haTrabalhadores();
    }

    // Método: Gestor adiciona um trabalhador ao centro
    public String adicionarTrabalhador(int estatuto, String nome, String email, String password) {
        return this.modelUtilizador.adicionarTrabalhador(estatuto, nome, email, password);
    }

    // Método: Ler um Trabalhador de um ficheiro
    public void lerTrabalhador(int estatuto, String nome, String email, String password, double d, int i1, int i2, List<String> l){
        this.modelUtilizador.lerTrabalhador(estatuto, nome, email, password, d, i1, i2, l);
    }

    // Método: Adiciona um cliente à lista de clientes
    public void adicionaCliente(String nome, String nif, String email) {
        this.modelUtilizador.adicionaCliente(nome, nif, email);
    }

    // Método: Gestor remove um trabalhador do centro
    public boolean removeTrabalhador(String idTrabalhador) {
        return this.modelUtilizador.removeTrabalhador(idTrabalhador);
    }

    // Método: Gestor avalia o centro
    public boolean avaliarCentro(String id, Double avaliacao) {
        return this.modelUtilizador.avaliarCentro(id, avaliacao);
    }

    // Método: Obtém a avaliação atual de um gestor
    public Double getAvaliacao(String idT) {
        return this.modelUtilizador.getAvaliacao(idT);
    }

    // Método: Obtém todos os clientes
    public Map<String, Cliente> getAllClientes() {
        return this.modelUtilizador.getClientes();
    }

    // Método: Obtém todos os trabalhadores
    public Map<String, Trabalhador> getAllTrabalhadores() {
        return this.modelUtilizador.getTrabalhadores();
    }

    /**** LISTAGENS ****/

    // Método: Listagem que contém o número, a duração média e a média dos desvios
    // das durações previstas das reparações de cada técnico
    public Map<String, List<String>> listagemNDD_TN() {
        Map<String, List<String>> resultado = new HashMap<>();
        List<Tecnico> tecnicos = this.modelUtilizador.getTecnicos();
        for (int i = 0; i < tecnicos.size(); i++) {
            List<Integer> resultados = this.modelReparacao.calculaNDD(tecnicos.get(i).getListaPedidos());
            List<String> dados = this.modelUtilizador.dadosTecnico(tecnicos.get(i), resultados);
            if (!resultado.containsKey(tecnicos.get(i).getID()))
                resultado.put(tecnicos.get(i).getID(), dados);
        }
        return resultado;
    }

    // Método: Listagem que contém o número de receções e entregas de equipamentos
    // de cada funcionário de balcão
    public Map<String, List<String>> listagemRE_FB() {
        Map<String, List<String>> resultado = new HashMap<>();
        List<FuncionarioBalcao> fbs = this.modelUtilizador.getFBs();
        for (int i = 0; i < fbs.size(); i++) {
            List<String> dados = this.modelUtilizador.infFB(fbs.get(i));
            if (!resultado.containsKey(fbs.get(i).getID()))
                resultado.put(fbs.get(i).getID(), dados);
        }
        return resultado;
    }

    // Método: Listagem que contém todos os passos das reparações realizadas por
    // cada técnico
    public Map<List<String>, Map<String, List<String>>> listagemP_TN() {
        Map<List<String>, Map<String, List<String>>> resultado = new HashMap<>();
        List<Tecnico> tecnicos = this.modelUtilizador.getTecnicos();
        for (int i = 0; i < tecnicos.size(); i++) {
            Map<String, List<String>> passosM = this.modelReparacao.getTodosPassos(tecnicos.get(i).getListaPedidos());
            List<String> IDkey = this.modelUtilizador.infTecnico(tecnicos.get(i));
            if (!resultado.containsKey(IDkey))
                resultado.put(IDkey, passosM);
        }
        return resultado;
    }
}
