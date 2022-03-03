package centro_reparacoes.business.reparacao;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  Reparacao:  - Classe que guarda todos os pedidos até agora solicitados;
 *              - Gera o id de cada pedido automaticamente;
 *              - Possui todos os métodos necessários para manipular pedidos.
 */

public class ReparacaoFacade implements IReparacaoFacade {
    private Map<String, Pedido> pedidos; // Mapa com, key: ids de pedidos , value: pedido
    private int randomizer; // Variável que guarda o próximo número disponível para um id

    public ReparacaoFacade() {
        this.pedidos = new HashMap<>();
        randomizer = 0;
    }

    // Getters
    public Map<String, Pedido> getPedidos() {
        return this.pedidos;
    }

    // Método: Obtém a urgência de um pedido
    public int getUrgencia(String idP) {
        return this.pedidos.get(idP).getUrgencia();
    }

    // Método: Obtém o tipo do pedido
    public int getTipoPedido(String idP) {
        return this.pedidos.get(idP).getTipoPedido();
    }

    // Método: Obtém o orçamento inicial de um pedido
    public double getOrcamentoInicial(String id_pedido) {
        return this.pedidos.get(id_pedido).getOrcamento();
    }

    // Método: Obtém o tipo de um pedido
    public int verificaTipo(String id_pedido) {
        return this.pedidos.get(id_pedido).getTipoPedido();
    }

    // Método: Insere a hora inicial da reparação
    public void setHoraInicioReparacao(String id_pedido) {
        this.pedidos.get(id_pedido).setDataInicioReparacao(LocalDateTime.now());
    }

    // Método: Insere a hora final de reparação
    public void setHoraFimReparacao(String id_pedido) {
        this.pedidos.get(id_pedido).setDataFimReparacao(LocalDateTime.now());
    }

    // Método: Altera o estado de um pedido
    public void setEstadoPedido(String id_pedido, int estado) {
        this.pedidos.get(id_pedido).setEstado(estado);
    }

    // Método: Insere um certo orçamento num certo pedido
    public void setOrcamento(String id_pedido, double orcamento) {
        this.pedidos.get(id_pedido).setOrcamento(orcamento);
    }

    // Método: Criar um pedido feito por um cliente
    public String criaPedido(int tipo_pedido, String id_cliente, int urgencia) {
        for (Map.Entry<String, Pedido> entry : this.pedidos.entrySet()) {
            if (this.randomizer == Integer.parseInt(entry.getKey()))
                this.randomizer++;
        }
        String id = Integer.toString(this.randomizer++);
        pedidos.put(id, new Pedido(tipo_pedido, id, id_cliente, urgencia));
        return id;
    }

    // Método: Adiciona uma novo pedido à lista de pedidos
    public void adicionarPedido(int tipo_pedido, String id_pedido, String id_cliente, int urgencia) {
        pedidos.put(id_pedido, new Pedido(tipo_pedido, id_pedido, id_cliente, urgencia));
    }

    // Método: Adiciona uma novo pedido à lista de pedidos
    public void adicionarPedidoSemPT(int tipo_pedido, String id_pedido, String id_cliente, int estado, int urgencia, LocalDateTime data){
        pedidos.put(id_pedido, new Pedido(tipo_pedido, id_pedido, id_cliente, estado, urgencia, data));
    }

    // Método: Adiciona uma novo pedido à lista de pedidos
    public int adicionarPedidoData(int tipo_pedido, String id_pedido, String id_cliente, String id_equipamento, int estado, int urgencia, LocalDateTime dataPedido,
    LocalDateTime dataInicioReparacao, LocalDateTime dataFimReparacao, double orcamento, boolean pagamento, boolean levantamento,
    boolean abandono) {

        int flag = 1;
        LocalDateTime data = LocalDateTime.now();
        int minutes = (int) ChronoUnit.MINUTES.between(dataPedido, data);
        int daystominutes = 30*24*60;

        if (minutes > daystominutes && estado == 0)
            estado = 6;
        
        if (dataFimReparacao != null)
            minutes = (int) ChronoUnit.MINUTES.between(dataFimReparacao, data);
        else
            minutes = 0;
        daystominutes = 90*24*60;

        if (minutes > daystominutes && (estado == 4 || estado == 5)) {
            flag = 2;
            estado = 5;
        }

        pedidos.put(id_pedido, new Pedido(tipo_pedido, id_pedido, id_cliente, id_equipamento, estado, urgencia, dataPedido,
         dataInicioReparacao, dataFimReparacao, orcamento, pagamento, levantamento, abandono));

         return flag;
    }

    // Método: Adicionar ao plano de trabalho, o passo, custo e tempo
    public boolean adicionaPassoCusto(String id_pedido, String passo, int custo, int minutos) {
        return this.pedidos.get(id_pedido).adicionaPassoeCusto(passo, custo, minutos);
    }

    // Método: Concluir a reparação de um pedido
    public void concluiReparacao(String id_pedido) {
        LocalDateTime data = LocalDateTime.now();
        this.pedidos.get(id_pedido).setDataFimReparacao(data);
    }

    // Método: Concluir um pedido
    public void concluiPedido(String id_pedido) {
        Pedido p = this.pedidos.get(id_pedido);
        p.setPagamento(true);
        p.setLevantamento(true);
        p.setEstado(6);
    }

    // Método: Coloca um pedido no estado de espera
    public void colocarEmEspera(String id_pedido) {
        this.pedidos.get(id_pedido).setEstado(3);
    }

    // Método: Calcula o total de minutos que um pedido demorou a ser reparado
    public int calculaTotalMinutos(String id_pedido) {
        return this.pedidos.get(id_pedido).getTotalMinutos();
    }

    // Método: Calcula o orçamento final
    public double calculaOrcamento(String id_pedido) {
        return this.pedidos.get(id_pedido).calculaOrcamento();
    }

    // Método: Cria um plano de trabalho dadas as listas de passos, custos e tempo
    public void adicionaPlano(String id_pedido, List<String> passos, List<Integer> custos, List<Integer> tempo){
        for(int i = 0; i < passos.size(); i++){
            this.pedidos.get(id_pedido).adicionaPassoeCusto(passos.get(i), custos.get(i), tempo.get(i));
        }
    }

    // Método: Verifica se existe um plano de trabalho de um pedido
    public boolean haPlanoDeTrabalho(String id_pedido) {
        return this.pedidos.get(id_pedido).haPlanoDeTrabalho();
    }

    // Método: Verifica se existem pedidos
    public boolean haPedidos() {
        for (Map.Entry<String, Pedido> entry : this.pedidos.entrySet()) {
            if (entry.getValue().getEstado() == -1 || entry.getValue().getEstado() == 0 || entry.getValue().getEstado() == 2) {
                return true;
            }
        }
        return false;
    }

    // Método: Obtém o ID do pedido mais antigo
    public String getIDMaisAntigo() {
        LocalDateTime data = null;
        String id = null;
        for (Map.Entry<String, Pedido> entry : this.pedidos.entrySet()) {
            int estado = entry.getValue().getEstado();
            if ((estado >= -1 && estado <= 3)) {
                if (data == null) {
                    data = entry.getValue().getDataPedido();
                    id = entry.getValue().getIDPedido();
                } else {
                    if (entry.getValue().getDataPedido().isBefore(data)) {
                        data = entry.getValue().getDataPedido();
                        id = entry.getValue().getIDPedido();
                    }
                }
            }
        }
        return id;
    }

    // Método: Obtém o ID do pedido mais urgente
    public String getIDMaisUrgente() {
        int urgencia = -1;
        String id = null;
        for (Map.Entry<String, Pedido> entry : this.pedidos.entrySet()) {
            int estado = entry.getValue().getEstado();
            if ((estado >= -1 && estado <= 3)) {
                if (entry.getValue().getUrgencia() > urgencia) {
                    urgencia = entry.getValue().getUrgencia();
                    id = entry.getValue().getIDEquipamento();
                }
            }
        }
        return id;
    }

    // Método: Obtém o ID do pedido através do nif de um cliente
    public String getIDFromNIF(int funcao, String nif) {
        for (Map.Entry<String, Pedido> entry : this.pedidos.entrySet()) {
            if (entry.getValue().getIDCliente().equals(nif)) {
                int estado = entry.getValue().getEstado();
                if (funcao == 0)
                    return entry.getKey();
                else if (funcao == 1)
                    if (estado == 4 || estado == 5 || estado == 6) {
                        concluiPedido(entry.getKey());
                        return entry.getKey();
                    }
                return "false";
            }
        }
        return "false";
    }

    // Método: Verifica se o orçamento atual está 120% acima do orçamento previsto
    public boolean orcamentoExtra(String idP) {
        if (verificaTipo(idP) == 0) {
            double orcamento = getOrcamentoInicial(idP); // Get do orçamento para este pedido
            double novo_orcamento = calculaOrcamento(idP); // Calcula o novo orçamento (depois de ser inserido um
                                                           // novo passo de reparacao)
            if (novo_orcamento > (orcamento * 1.2)) {
                // Envia email ao cliente a dizer que o orçamento vai ser maior pelo menos 120%
                // do que o valor dado inicialmente
                setEstadoPedido(idP, 3);
                return true;
            }
        }
        return false;
    }

    // Método: Altera o estado de um pedido
    public int alteraEstadoPedido(String idP, int estado) {
        int estadoAtual = this.pedidos.get(idP).getEstado();
        if (estadoAtual == -1 && (estado == 0 || estado == 1)) {
            setEstadoPedido(idP, estado);
            return 0;
        } else if (estadoAtual == 0 && (estado == 2 || estado == 3 || estado == 5)) {
            setEstadoPedido(idP, estado);
            return 0;
        } else if (estadoAtual == 1 && (estado == 5 || estado == 6)) {
            setEstadoPedido(idP, estado);
            return 0;
        } else if (estadoAtual == 2 && (estado >= 3 && estado <= 6)) {
            setEstadoPedido(idP, estado);
            setHoraInicioReparacao(idP);
            return 1;
        } else if (estadoAtual == 3 && estado == 2) {
            setEstadoPedido(idP, estado);
            return 0;
        }
        return -1;
    }

    // Método: Calcula o número e o desvio de uma lista de pedidos
    public List<Integer> calculaNDD(List<String> idPs) {
        List<Integer> resultados = new ArrayList<>();
        int total = 0, desvios = 0;
        for (int i = 0; i < idPs.size(); i++) {
            Pedido p = this.pedidos.get(idPs.get(i));
            if (p.getEstado() == 4 || p.getEstado() == 6) {
                total += p.calculaDiferenca();
                desvios += Math.abs(p.getPlanoTrabalho().calculaTotalMinutos() - p.calculaDiferenca());
            }
        }
        resultados.add(total);
        resultados.add(desvios);
        return resultados;
    }

    // Método: Constrói um mapa com todos os passos dos pedidos dados
    public Map<String, List<String>> getTodosPassos(List<String> idPs) {
        Map<String, List<String>> passosM = new HashMap<>();
        for (int i = 0; i < idPs.size(); i++) {
            Pedido p = this.pedidos.get(idPs.get(i));
            List<String> passosPT = p.getPlanoTrabalho().getPassos();

            if (!passosM.containsKey(p.getIDPedido()))
                passosM.put(p.getIDPedido(), passosPT);
        }
        return passosM;
    }

    // Método: Obtém uma lista informações de equipamentos arquivados
    public List<String> getPArquivados() {
        List<String> arquivados = new ArrayList<>();
        for (Map.Entry<String, Pedido> entry : this.pedidos.entrySet()) {
            if (entry.getValue().getEstado() == 6) {
                arquivados.add(entry.getKey());
                int dias = (int) ChronoUnit.DAYS.between(entry.getValue().getDataPedido(), LocalDateTime.now());
                arquivados.add(Integer.toString(dias));
            }
        }
        return arquivados;
    }

    // Método: Obtém uma lista informações de equipamentos abandonados
    public List<String> getPAbandonados() {
        List<String> abandonados = new ArrayList<>();
        for (Map.Entry<String, Pedido> entry : this.pedidos.entrySet()) {
            if (entry.getValue().getEstado() == 5) {
                abandonados.add(entry.getKey());
                int dias = (int) ChronoUnit.DAYS.between(entry.getValue().getDataPedido(), LocalDateTime.now());
                abandonados.add(Integer.toString(dias));
            }
        }
        return abandonados;
    }

    // Método: Obtém uma lista com os pedidos atuais
    public List<String> getPAtuais() {
        List<String> atuais = new ArrayList<>();
        for (Map.Entry<String, Pedido> entry : this.pedidos.entrySet()) {
            int estado = entry.getValue().getEstado();
            if (estado >= -1 && estado <= 3 && estado != 1)
                atuais.add(entry.getKey());
        }
        return atuais;
    }
}
