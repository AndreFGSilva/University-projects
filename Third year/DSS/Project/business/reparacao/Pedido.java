package centro_reparacoes.business.reparacao;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/*
 *  Pedido: - Classe que guarda toda a informação de um pedido;
 *          - Possui todos os métodos necessários para alterar as variáveis de um pedido.
 */

public class Pedido {
    int tipoPedido; // Variável para controlar o tipo do pedido. 0: Normal , 1: Serviço Expresso
    String IDPedido; // ID que é gerado automaticamente depois de ser registado um pedido
    String IDCliente; // ID do cliente
    String IDEquipamento; // ID do equipamento
    int estado; // Representa os vários estados do pedido. 0: Reparável , 1: Não reparável ,
                // 2: Em reparação , 3: Em espera , 4: Concluído , 5: Cancelado , 6: Arquivado
    int urgencia; // Variável de 0 a 10 que guarda a urgência de um pedido, sendo que 10 é o mais urgente
    LocalDateTime dataPedido; // Data inicial de um pedido
    LocalDateTime dataInicioReparacao; // Data inicial de uma reparação do pedido
    LocalDateTime dataFimReparacao; // Data final de uma reparação do pedido
    PlanoTrabalho plano; // Variável que contém os passos, os custos e os tempos do plano de trabalho
    double orcamento; // Orçamento do pedido.
    boolean pagamento; // Valida se o pagamento já foi ou não efetuado
    boolean levantamento; // Valida se o levantamento já foi ou não efetuado
    boolean abandono; // Valida se o equipamento foi ou não abandonado

    public Pedido(int tipo_pedido, String id_pedido, String id_cliente, int urgencia) {
        this.tipoPedido = tipo_pedido;
        this.IDPedido = id_pedido;
        this.IDCliente = id_cliente;
        this.IDEquipamento = id_pedido;
        this.estado = -1;
        this.urgencia = urgencia;
        this.dataPedido = LocalDateTime.now();
        this.dataInicioReparacao = null;
        this.dataFimReparacao = null;
        this.plano = new PlanoTrabalho();
        this.orcamento = -1;
        this.pagamento = false;
        this.levantamento = false;
        this.abandono = false;
    }

    public Pedido(int tipo_pedido, String id_pedido, String id_cliente, int estado, int urgencia, LocalDateTime data) {
        this.tipoPedido = tipo_pedido;
        this.IDPedido = id_pedido;
        this.IDCliente = id_cliente;
        this.IDEquipamento = id_pedido;
        this.estado = -1;
        this.urgencia = urgencia;
        this.dataPedido = data;
        this.dataInicioReparacao = null;
        this.dataFimReparacao = null;
        this.plano = new PlanoTrabalho();
        this.orcamento = -1;
        this.pagamento = false;
        this.levantamento = false;
        this.abandono = false;
    }

    public Pedido(int tipo_pedido, String id_pedido, String id_cliente, String id_equipamento, int estado, int urgencia, LocalDateTime dataPedido,
    LocalDateTime dataInicioReparacao, LocalDateTime dataFimReparacao, double orcamento, boolean pagamento, boolean levantamento,
    boolean abandono) {
        this.tipoPedido = tipo_pedido;
        this.IDPedido = id_pedido;
        this.IDCliente = id_cliente;
        this.IDEquipamento = id_equipamento;
        this.estado = estado;
        this.urgencia = urgencia;
        this.dataPedido = dataPedido;
        this.dataInicioReparacao = dataInicioReparacao;
        this.dataFimReparacao = dataFimReparacao;
        this.plano = new PlanoTrabalho();
        this.orcamento = orcamento;
        this.pagamento = pagamento;
        this.levantamento = levantamento;
        this.abandono = abandono;
    }

    // Getters
    public int getTipoPedido() {
        return this.tipoPedido;
    }

    public String getIDPedido() {
        return this.IDPedido;
    }

    public String getIDCliente(){
        return this.IDCliente;
    }

    public String getIDEquipamento() {
        return this.IDEquipamento;
    }

    public int getEstado() {
        return this.estado;
    }
    
    public int getUrgencia() {
        return this.urgencia;
    }

    public LocalDateTime getDataPedido() {
        return this.dataPedido;
    }

    public LocalDateTime getDataInicioReparacao() {
        return this.dataInicioReparacao;
    }

    public LocalDateTime getDataFimReparacao() {
        return this.dataFimReparacao;
    }

    public PlanoTrabalho getPlanoTrabalho() {
        return this.plano;
    }

    public double getOrcamento() {
        return this.orcamento;
    }

    // Setters
    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setDataInicioReparacao(LocalDateTime data) {
        this.dataInicioReparacao = data;
    }

    public void setDataFimReparacao(LocalDateTime data) {
        this.dataFimReparacao = data;
    }

    public void setOrcamento(double orcamento) {
        this.orcamento = orcamento;
    }

    public void setPagamento(boolean bool) {
        this.pagamento = bool;
    }

    public void setLevantamento(boolean bool) {
        this.levantamento = bool;
    }

    // Método: Obtém o tempo total, em minutos, de um plano
    public int getTotalMinutos() {
        return this.plano.calculaTotalMinutos();
    }

    // Método: Calcula o orçamento final do pedido
    public double calculaOrcamento() {
        double total = 0;
        total = this.plano.calculaTotalGasto();
        return total;
    }

    // Método: Calcula o tempo que uma reparação demorou a ser realizada
    public int calculaDiferenca() {
        int minutes = (int) ChronoUnit.MINUTES.between(this.dataFimReparacao, this.dataInicioReparacao);
        return minutes;
    }

    // Método: Verifica se existe um plano de trabalho
    public boolean haPlanoDeTrabalho() {
        return this.plano.getPassos().size() == 0;
    }

    // Método: Adiciona um passo, custo e tempo ao plano de trabalho do pedido
    public boolean adicionaPassoeCusto(String passo, int custo, int minutos) {
        return this.getPlanoTrabalho().adicionaPassoeCusto(passo, custo, minutos);
    }

    // Método: Transforma as informações do Pedido para String
    public String toFile(int estado){
        if (estado == -1 || estado == 6)
            return "p;" + this.tipoPedido + ";" + this.IDPedido + ";" + this.IDCliente + ";" + this.IDEquipamento +
                    ";" + this.estado + ";" + this.urgencia + ";" + this.dataPedido + ";" + "null" + ";" + "null" + ";" + "null" +
                    ";" + this.orcamento + ";" + Boolean.toString(this.pagamento) + ";" + Boolean.toString(this.levantamento) +
                    ";" + Boolean.toString(this.abandono) + "\n";
        else {
            return "p;" + this.tipoPedido + ";" + this.IDPedido + ";" + this.IDCliente + ";" + this.IDEquipamento +
                    ";" + this.estado + ";" + this.urgencia + ";" + this.dataPedido + ";" + this.dataInicioReparacao + 
                    ";" + this.dataFimReparacao + ";" + this.plano.toFile() +
                    ";" + this.orcamento + ";" + Boolean.toString(this.pagamento) + ";" + Boolean.toString(this.levantamento) +
                    ";" + Boolean.toString(this.abandono) + "\n";
        }            
    }
}
