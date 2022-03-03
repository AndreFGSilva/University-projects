package centro_reparacoes.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import centro_reparacoes.business.ICentroReparacoesFacade;

/*
 *  TextUI: - Possui todos os menus necessários para a execução do programa.
 */

public class TextUI {
    private ICentroReparacoesFacade model;
    private static Scanner sc = new Scanner(System.in);
    private static Menu menu = new Menu();

    private String idU;
    private String idP;

    public TextUI(ICentroReparacoesFacade iCR) {
        this.model = iCR;
        this.idU = "";
        this.idP = null;
    }

    public void run() {
        menu.message("\n\nBem vindo ao sistema!!!\n");
        this.menuInicial();
    }

    // Método: Menu inicial
    public void menuInicial() {
        // Criar menu
        List<String> options = Arrays.asList(
                "Autenticar", // 1
                "Sair"); // 2
        menu.setOptions(options);

        // Registar handlers
        menu.setHandlers(1, () -> autenticar());
        menu.setHandlers(2, () -> sair());

        menu.run();
    }

    // Método: Menu autenticar
    public void autenticar() {
        menu.message("\nID: ");
        String id = sc.nextLine();
        menu.message("Password: ");
        String pw = sc.nextLine();

        int estatuto = model.autenticarUtilizador(id, pw);
        if (estatuto == 1) {
            this.idU = id;
            menuPrincipalFB();
        } else if (estatuto == 2) {
            this.idU = id;
            menuPrincipalTN();
        } else if (estatuto == 3) {
            this.idU = id;
            menuPrincipalGT();
        } else
            menuInicial();
    }

    // Método: Menu principal do funcionário de balcão
    public void menuPrincipalFB() {
        menu.message("\n\n***** Funcionário: " + model.getNome(this.idU));

        // Criar menu
        List<String> options = Arrays.asList(
                "Registar pedido", // 1
                "Registar levantamento", // 2
                "Equipamentos", // 3
                "Terminar sessão"); // 4
        menu.setOptions(options);

        // Registar pré-condições
        menu.setPreCondition(3, () -> model.haEquipamentos());

        // Registar handlers
        menu.setHandlers(1, () -> registarPedido());
        menu.setHandlers(2, () -> registarLevantamento());
        menu.setHandlers(3, () -> menuEquipamentos(1));
        menu.setHandlers(4, () -> terminarSessao());

        menu.run();
    }

    // Método: Registar pedido de cliente
    public void registarPedido() {
        menu.message("\nTipo de pedido (0: Normal , 1: Serviço Expresso): ");
        String tipoPedidoST = sc.nextLine();
        menu.message("Nome do cliente: ");
        String nomeC = sc.nextLine();
        menu.message("NIF do cliente: ");
        String nif = sc.nextLine();
        menu.message("Email do cliente: ");
        String email = sc.nextLine();
        menu.message("Equipamento: ");
        String nomeE = sc.nextLine();
        menu.message("Urgência (0 a 10, sendo 10 mais urgente): ");
        String urgenciaST = sc.nextLine();

        int tipoPedido = Integer.parseInt(tipoPedidoST);
        int urgencia = Integer.parseInt(urgenciaST);
        boolean valida = model.registaPedido(this.idU, tipoPedido, nomeC, nif, email, nomeE, urgencia);
        if (valida)
            menu.message("\nPedido inserido com sucesso.");
        else
            menu.message("\nPedido inserido sem sucesso.");

        menuPrincipalFB();
    }

    // Método: Registar levantamento do equipamento pelo cliente
    public void registarLevantamento() {
        menu.message("\nNIF do cliente: ");
        String nif = sc.nextLine();

        int estado = model.getEstado(model.getIDFromNIF(nif));
        boolean valida = model.registarEntrega(this.idU, nif);
        if (valida)
            menu.message("\nEstado final do pedido: " + estado + "\nPedido removido com sucesso.");
        else
            menu.message("\nEstado atual do pedido: " + estado + "\nPedido removido sem sucesso.");

        menuPrincipalFB();
    }

    // Método: Menu das listas de equipamentos
    public void menuEquipamentos(int estatuto) {
        if (estatuto == 1)
            menu.message("\n\n***** Funcionário: " + model.getNome(this.idU));
        else if (estatuto == 3) {
            menu.message("\n\n***** Gestor: " + model.getNome(this.idU));
            Double classification = model.getAvaliacao(this.idU);
            if (classification >= 0)
                menu.message("\n*** Avaliação: " + classification);
        }

        // Criar menu
        List<String> options = Arrays.asList(
                "Arquivados", // 1
                "Abandonados", // 2
                "Voltar atrás"); // 3
        menu.setOptions(options);

        // Registar handlers
        menu.setHandlers(1, () -> equipamentosArquivados(estatuto));
        menu.setHandlers(2, () -> equipamentosAbandonados(estatuto));
        if (estatuto == 1)
            menu.setHandlers(3, () -> menuPrincipalFB());
        else if (estatuto == 3)
            menu.setHandlers(3, () -> menuPrincipalGT());
    }

    // Método: Mostra a lista de equipamentos arquivados
    public void equipamentosArquivados(int estatuto) {
        Map<String, List<String>> lista = model.getArquivados();

        menu.message("\n\n***** Equipamentos arquivados");
        for (Map.Entry<String, List<String>> entry : lista.entrySet()) {
            menu.message("\n** Equipamento: " + entry.getValue().get(0) + " (" + entry.getKey() + ")");
            menu.message("\n* Dias passados: " + entry.getValue().get(1));
            menu.message("\n- - -");
        }
        menu.message("\n");

        // Criar menu
        List<String> options = Arrays.asList("Voltar atrás"); // 1
        menu.setOptions(options);

        // Registar handlers
        menu.setHandlers(1, () -> menuEquipamentos(estatuto));

        menu.run();
    }

    // Método: Mostra a lista de equipamentos abandonados
    public void equipamentosAbandonados(int estatuto) {
        Map<String, List<String>> lista = model.getAbandonados();

        menu.message("\n\n***** Equipamentos abandonados");
        for (Map.Entry<String, List<String>> entry : lista.entrySet()) {
            menu.message("\n** Equipamento: " + entry.getValue().get(0) + " (" + entry.getKey() + ")");
            menu.message("\n* Dias passados: " + entry.getValue().get(1));
            menu.message("\n- - -");
        }
        menu.message("\n");

        // Criar menu
        List<String> options = Arrays.asList("Voltar atrás"); // 1
        menu.setOptions(options);

        // Registar handlers
        menu.setHandlers(1, () -> menuEquipamentos(estatuto));

        menu.run();
    }

    // Método: Menu principal do técnico
    public void menuPrincipalTN() {
        menu.message("\n\n***** Técnico: " + model.getNome(this.idU));
        if (this.idP != null) {
            menu.message("\n*** Pedido: " + this.idP);
            menu.message("\n** Estado: " + model.getEstado(this.idP));
            menu.message("\n** Urgência: " + model.getUrgencia(this.idP));
            menu.message("\n** Tipo: " + model.getTipoPedido(this.idP));
            int time = model.calculaEstimativaTempo(this.idP);
            if (time != 0)
                menu.message("\n** Tempo: " + time + "min");
        }

        // Criar menu
        List<String> options = Arrays.asList(
                "Obter pedido", // 1
                "Aceder pedido", // 2
                "Pedidos atuais", // 3
                "Terminar sessão"); // 4
        menu.setOptions(options);

        // Registar pré-condições
        menu.setPreCondition(2, () -> this.idP != null);

        // Registar handlers
        menu.setHandlers(1, () -> menuObterPedido());
        menu.setHandlers(2, () -> menuPedido());
        menu.setHandlers(3, () -> pedidosAtuais());
        menu.setHandlers(4, () -> terminarSessao());

        menu.run();
    }

    // Método: Menu obter um pedido
    public void menuObterPedido() {
        menu.message("\n\n***** Técnico: " + model.getNome(this.idU));
        if (this.idP != null) {
            menu.message("\n*** Pedido: " + this.idP);
            menu.message("\n** Estado: " + model.getEstado(this.idP));
            menu.message("\n** Urgência: " + model.getUrgencia(this.idP));
            menu.message("\n** Tipo: " + model.getTipoPedido(this.idP));
            int time = model.calculaEstimativaTempo(this.idP);
            if (time != 0)
                menu.message("\n** Tempo: " + time + "min");
        }

        // Criar menu
        List<String> options = Arrays.asList(
                "Inserir um pedido", // 1
                "Pedido mais antigo", // 2
                "Pedido mais urgente", // 3
                "Voltar atrás"); // 4
        menu.setOptions(options);

        // Registar handlers
        menu.setHandlers(1, () -> inserirPedido());
        menu.setHandlers(2, () -> pedidoMaisAntigo());
        menu.setHandlers(3, () -> pedidoMaisUrgente());
        menu.setHandlers(4, () -> menuPrincipalTN());

        menu.run();
    }

    // Método: Inserir o código de um pedido
    public void inserirPedido() {
        menu.message("\nID do pedido: ");
        String idP_AUX = sc.nextLine();

        boolean valida = model.validaIDPedido(idP_AUX);
        if (valida) {
            this.idP = idP_AUX;
            menu.message("\nPedido obtido com sucesso\n");
        } else
            menu.message("\nPedido obtido sem sucesso\n");
    
        menuObterPedido();
    }

    // Método: Pedido mais antigo da lista de pedidos
    public void pedidoMaisAntigo() {
        this.idP = model.getIDMaisAntigo();
        if (this.idP == null)
            menu.message("\nNão existem pedidos\n");
        else
            menu.message("\nID do pedido mais antigo: " + this.idP + "\n");

        menuObterPedido();
    }

    // Método: Pedido mais urgente da lista de pedidos
    public void pedidoMaisUrgente() {
        this.idP = model.getIDMaisUrgente();
        if (this.idP == null)
            menu.message("\nNão existem pedidos\n");
        else
            menu.message("\nID do pedido mais urgente: " + this.idP + "\n");

        menuObterPedido();
    }

    // Método: Menu do pedido
    public void menuPedido() {
        menu.message("\n\n***** Técnico: " + model.getNome(this.idU));
        if (this.idP != null) {
            menu.message("\n*** Pedido: " + this.idP);
            menu.message("\n** Estado: " + model.getEstado(this.idP));
            menu.message("\n** Urgência: " + model.getUrgencia(this.idP));
            menu.message("\n** Tipo: " + model.getTipoPedido(this.idP));
            int time = model.calculaEstimativaTempo(this.idP);
            if (time != 0)
                menu.message("\n** Tempo: " + time + "min");
        }

        // Criar menu
        List<String> options = Arrays.asList(
                "Plano de trabalho", // 1
                "Reparação", // 2
                "Mudar estado do pedido", // 3
                "Voltar atrás"); // 4
        menu.setOptions(options);

        // Registar pré-condições
        menu.setPreCondition(1, () -> model.getTipoPedido(this.idP) == 0);
        menu.setPreCondition(2, () -> model.getOrcamento(this.idP) != -1 || model.getTipoPedido(this.idP) != 1);

        // Registar handlers
        menu.setHandlers(1, () -> menuPT());
        menu.setHandlers(2, () -> menuReparacao());
        menu.setHandlers(3, () -> mudarEstadoPedido());
        menu.setHandlers(4, () -> menuPrincipalTN());

        menu.run();
    }

    // Método: Menu do plano de trabalho do pedido
    public void menuPT() {
        menu.message("\n\n***** Técnico: " + model.getNome(this.idU));
        if (this.idP != null) {
            menu.message("\n*** Pedido: " + this.idP);
            menu.message("\n** Estado: " + model.getEstado(this.idP));
            menu.message("\n** Urgência: " + model.getUrgencia(this.idP));
            menu.message("\n** Tipo: " + model.getTipoPedido(this.idP));
            int time = model.calculaEstimativaTempo(this.idP);
            if (time != 0)
                menu.message("\n** Tempo: " + time + "min");
        }

        // Criar menu
        List<String> options = Arrays.asList(
                "Construir plano de trabalho", // 1
                "Concluir plano de trabalho", // 2
                "Voltar atrás"); // 3
        menu.setOptions(options);

        // Registar pré-condições
        menu.setPreCondition(2, () -> !model.haPlanoDeTrabalho(this.idP));

        // Registar handlers
        menu.setHandlers(1, () -> menuConstruirPT());
        menu.setHandlers(2, () -> concluirPT());
        menu.setHandlers(3, () -> menuPedido());

        menu.run();
    }

    // Método: Meno da construção do plano de trabalho do pedido
    public void menuConstruirPT() {
        menu.message("\n\n***** Técnico: " + model.getNome(this.idU));
        if (this.idP != null) {
            menu.message("\n*** Pedido: " + this.idP);
            menu.message("\n** Estado: " + model.getEstado(this.idP));
            menu.message("\n** Urgência: " + model.getUrgencia(this.idP));
            menu.message("\n** Tipo: " + model.getTipoPedido(this.idP));
            int time = model.calculaEstimativaTempo(this.idP);
            if (time != 0)
                menu.message("\n** Tempo: " + time + "min");
        }

        // Criar menu
        List<String> options = Arrays.asList(
                "Adicionar passo, custo e tempo", // 1
                "Voltar atrás"); // 2
        menu.setOptions(options);

        // Registar pré-condições
        menu.setPreCondition(1, () -> model.getEstado(this.idP) != 3);

        // Registar handlers
        menu.setHandlers(1, () -> construirPT());
        menu.setHandlers(2, () -> menuPT());

        menu.run();
    }

    // Método: Adicionar passo, custo e tempo no plano de trabalho do pedido
    public void construirPT() {
        menu.message("\nPasso: ");
        String step = sc.nextLine();
        menu.message("Custo: ");
        String costST = sc.nextLine();
        menu.message("Tempo, em minutos: ");
        String minST = sc.nextLine();

        int cost = Integer.parseInt(costST);
        int min = Integer.parseInt(minST);
        boolean valida = model.adicionaPassoeCusto(this.idP, step, cost, min);
        if (valida)
            menu.message("\nAdicionado com sucesso\n");
        else
            menu.message("\nAdicionado sem sucesso\n");

        menuConstruirPT();
    }

    // Método: Conclui o plano de trabalho. Imprime o orçamento calculado.
    public void concluirPT() {
        double budget = model.concluirOrcamento(this.idP);
        int time = model.calculaEstimativaTempo(this.idP);
        menu.message("\nOrçamento: " + budget);
        menu.message("\nTempo estimado: " + time + "min");
        menu.message("\nEmail enviado ao cliente com a informação de que o equipamento é reparável, com o orçamento e o tempo estimado\n");

        menuPT();
    }

    // Método: Menu realizar reparação
    public void menuReparacao() {
        menu.message("\n\n***** Técnico: " + model.getNome(this.idU));
        if (this.idP != null) {
            menu.message("\n*** Pedido: " + this.idP);
            menu.message("\n** Estado: " + model.getEstado(this.idP));
            menu.message("\n** Urgência: " + model.getUrgencia(this.idP));
            menu.message("\n** Tipo: " + model.getTipoPedido(this.idP));
            int time = model.calculaEstimativaTempo(this.idP);
            if (time != 0)
                menu.message("\n** Tempo: " + time + "min");
        }

        // Criar menu
        List<String> options = Arrays.asList(
                "Adicionar passo, custo e tempo", // 1
                "Concluir reparação", // 2
                "Voltar atrás"); // 3
        menu.setOptions(options);

        // Registar pré-condições
        menu.setPreCondition(1, () -> model.getEstado(this.idP) != 3);
        menu.setPreCondition(2, () -> model.getEstado(this.idP) != 3);

        // Registar handlers
        menu.setHandlers(1, () -> realizarReparacaoPT());
        menu.setHandlers(2, () -> concluirReparacao());
        menu.setHandlers(3, () -> menuPedido());

        menu.run();
    }

    // Método: Adicionar passo, custo e tempo na realização da reparação
    public void realizarReparacaoPT() {
        menu.message("\nPasso: ");
        String step = sc.nextLine();
        menu.message("Custo: ");
        String costST = sc.nextLine();
        menu.message("Tempo, em minutos: ");
        String minST = sc.nextLine();

        int cost = Integer.parseInt(costST);
        int min = Integer.parseInt(minST);
        boolean validaA = model.adicionaPassoeCusto(this.idP, step, cost, min);
        boolean validaB = model.orcamentoAcima(this.idP);
        if (validaA) {
            menu.message("\nAdicionado com sucesso");
            if (validaB)
                menu.message("\nATENÇÃO!! Orçamento passou dos 120%\nEmail enviado ao cliente com o novo orçamento\n");
        } else
            menu.message("\nAdicionado sem sucesso\n");

        menuReparacao();
    }

    // Método: Concluir a reparação de um pedido
    public void concluirReparacao() {
        int tipo = model.getTipoPedido(this.idP);
        boolean valida = model.registarConclusaoReparacao(this.idU, this.idP);
        if (valida) {
            if (tipo == 0)
                menu.message("\nEmail enviado ao cliente com a informação de que o equipamento está reparado\n");
            else
                menu.message("\nSMS enviada ao cliente com a informação de que o equipamento está reparado\n");
            this.idP = null;
            menuPrincipalTN();
        }
        else {
            menu.message("\nReparação não concluída\n");
            menuReparacao();
        }
    }

    // Método: Mudar o estado atual do pedido
    public void mudarEstadoPedido() {
        menu.message("\nEstados possíveis");
        menu.message("\n\t0: Reparável , 1: Não reparável , 2: Em reparação");
        menu.message("\n\t3: Em espera , 4: Concluído , 5: Cancelado , 6: Arquivado");
        menu.message("\n\nEstado: ");
        String statusST = sc.nextLine();

        int status = Integer.parseInt(statusST);
        if (status >= 0 && status <= 6) {
            boolean valida = model.alteraEstadoPedido(this.idU, this.idP, status);
            if (valida) {
                if (status == 1)
                    menu.message("\nEmail enviado ao cliente com a informação de que o equipamento não é reparável\n");
                else if (status == 4)
                    menu.message("\nEmail enviado ao cliente com a informação de que o equipamento está reparado\n");
                else if (status == 5 || status == 6) {
                    menu.message("\nEmail enviado ao cliente com a informação de que pode recolher o equipamento não reparado\n");
                    this.idP = null;
                    menuPrincipalTN();
                }
            }
            else
                menu.message("\nInseriu um estado inválido\n");
        } else
            menu.message("\nInseriu um estado inválido\n");
        
        menuPedido();
    }

    // Método: Lista dos pedidos atuais no centro de reparações
    public void pedidosAtuais() {
        List<String> pedidos = model.getPedidosAtuais();

        menu.message("\n\n***** Pedidos atuais");
        for (int i = 0; i < pedidos.size(); i++) {
            menu.message("\n** Pedido: " + pedidos.get(i));
            menu.message("\n* Estado: " + model.getEstado(pedidos.get(i)));
            menu.message("\n* Urgência: " + model.getUrgencia(pedidos.get(i)));
            menu.message("\n- - -");
        }

        // Criar menu
        List<String> options = Arrays.asList("Voltar atrás"); // 1
        menu.setOptions(options);

        // Registar handlers
        menu.setHandlers(1, () -> menuPrincipalTN());

        menu.run();
    }

    // Método: Menu principal do gestor
    public void menuPrincipalGT() {
        menu.message("\n\n***** Gestor: " + model.getNome(this.idU));
        Double classification = model.getAvaliacao(this.idU);
        if (classification >= 0)
            menu.message("\n*** Avaliação: " + classification);

        // Criar menu
        List<String> options = Arrays.asList(
                "Listagens", // 1
                "Avaliar centro", // 2
                "Gestão do centro", // 3
                "Terminar sessão"); // 4
        menu.setOptions(options);

        // Registar handlers
        menu.setHandlers(1, () -> menuListagens());
        menu.setHandlers(2, () -> avaliarCentro());
        menu.setHandlers(3, () -> menuGestao());
        menu.setHandlers(4, () -> terminarSessao());

        menu.run();
    }

    // Método: Menu com as listagens
    public void menuListagens() {
        menu.message("\n\n***** Gestor: " + model.getNome(this.idU));
        Double classification = model.getAvaliacao(this.idU);
        if (classification >= 0)
            menu.message("\n*** Avaliação: " + classification);

        // Criar menu
        List<String> options = Arrays.asList(
                "Análise das reparações por técnico", // 1
                "Número de receções e entregas por funcionário", // 2
                "Passos das reparações por técnico", // 3
                "Equipamentos", // 4
                "Voltar atrás"); // 5
        menu.setOptions(options);

        // Registar handlers
        menu.setHandlers(1, () -> listagemA());
        menu.setHandlers(2, () -> listagemB());
        menu.setHandlers(3, () -> listagemC());
        menu.setHandlers(4, () -> menuEquipamentos(3));
        menu.setHandlers(5, () -> menuPrincipalGT());

        menu.run();
    }

    // Método: Listagem com a análise das reparações por técnico
    public void listagemA() {
        Map<String, List<String>> map = model.listagemNDD_TN();

        menu.message("\n\n***** Listagem");
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (map.entrySet().size() != 0) {
                menu.message("\n** Técnico: " + entry.getValue().get(0) + " (" + entry.getKey() + ")");
                menu.message("\n*Número de reparações: " + entry.getValue().get(1));
                menu.message("\n*Duração média das reparações: " + entry.getValue().get(2) + "min");
                menu.message("\n*Desvio médio das durações previstas das reparações: " + entry.getValue().get(3) + "min\n");
            }
        }

        // Criar menu
        List<String> options = Arrays.asList("Voltar atrás"); // 1
        menu.setOptions(options);

        // Registar handlers
        menu.setHandlers(1, () -> menuListagens());

        menu.run();
    }

    // Método: Listagem com o número de receções e entregas por funcionário
    public void listagemB() {
        Map<String, List<String>> map = model.listagemRE_FB();

        menu.message("\n\n***** Listagem");
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            menu.message("\n** Funcionário: " + entry.getValue().get(0) + " (" + entry.getKey() + ")");
            menu.message("\n*Número de receções: " + entry.getValue().get(1));
            menu.message("\n*Número de entregas: " + entry.getValue().get(2) + "\n");
        }

        // Criar menu
        List<String> options = Arrays.asList("Voltar atrás"); // 1
        menu.setOptions(options);

        // Registar handlers
        menu.setHandlers(1, () -> menuListagens());

        menu.run();
    }

    // Método: Listagem com os passos das reparações por técnico
    public void listagemC() {
        Map<List<String>, Map<String, List<String>>> map = model.listagemP_TN();

        menu.message("\n\n***** Listagem");
        for (Map.Entry<List<String>, Map<String, List<String>>> entryA : map.entrySet()) {
            menu.message("\n** Técnico: " + entryA.getKey().get(1) + " (" + entryA.getKey().get(0) + ")");
            for (Map.Entry<String, List<String>> entryB : entryA.getValue().entrySet()) {
                menu.message("\n*Pedido: " + entryB.getKey());
                for (int i = 0; i < entryB.getValue().size(); i++)
                    menu.message("\nPasso " + (i+1) + ": " + entryB.getValue().get(i));
                menu.message("\n--");
            }
            menu.message("\n---\n");
        }

        // Criar menu
        List<String> options = Arrays.asList("Voltar atrás"); // 1
        menu.setOptions(options);

        // Registar handlers
        menu.setHandlers(1, () -> menuListagens());

        menu.run();
    }

    // Método: Avaliar centro
    public void avaliarCentro() {
        menu.message("\nAvaliação (0 a 10): ");
        String classificationST = sc.nextLine();
        Double classification = Double.parseDouble(classificationST);

        boolean valida = model.avaliarCentro(idU, classification);
        if (valida)
            menu.message("\nAvaliação atualizada com sucesso\n");
        else
            menu.message("\nAvaliação atualizada sem sucesso\n");

        menuPrincipalGT();
    }

    // Método: Menu com a gestão do centro
    public void menuGestao() {
        menu.message("\n\n***** Gestor: " + model.getNome(this.idU));
        Double classification = model.getAvaliacao(this.idU);
        if (classification >= 0)
            menu.message("\n*** Avaliação: " + classification);

        // Criar menu
        List<String> options = Arrays.asList(
                "Adicionar trabalhador", // 1
                "Remover trabalhador", // 2
                "Voltar atrás"); // 3
        menu.setOptions(options);

        // Registar pré-condição
        menu.setPreCondition(2, () -> model.haTrabalhadores());

        // Registar handlers
        menu.setHandlers(1, () -> adicionarTrabalhador());
        menu.setHandlers(2, () -> removerTrabalhador());
        menu.setHandlers(3, () -> menuPrincipalGT());

        menu.run();
    }

    // Método: Adicionar um trabalhador ao centro
    public void adicionarTrabalhador() {
        menu.message("\nEstatuto (1: Funcionário Balcão , 2: Técnico , 3: Gestor): ");
        String statuteST = sc.nextLine();
        menu.message("Primeiro e último nome: ");
        String name = sc.nextLine();
        menu.message("Email: ");
        String email = sc.nextLine();
        menu.message("Password: ");
        String password = sc.nextLine();

        int statute = Integer.parseInt(statuteST);
        String idT = model.adicionarTrabalhador(statute, name, email, password);
        if (idT.equals("false"))
            menu.message("\nTrabalhador adicionado sem sucesso\n");
        else
            menu.message("\nTrabalhador adicionado com o id " + idT + "\n");

        menuGestao();
    }

    // Método: Remover um trabalhador do centro
    public void removerTrabalhador() {
        menu.message("\nID trabalhador: ");
        String idT = sc.nextLine();

        boolean valida = model.removeTrabalhador(idT);
        if (valida)
            menu.message("\nTrabalhador removido com o id " + idT + "\n");
        else
            menu.message("\nTrabalhador removido sem sucesso\n");

        menuGestao();
    }

    // Método: Terminar sessão
    public void terminarSessao() {
        model.terminarSessao(this.idU);
        this.idU = "";
        this.idP = null;

        menuInicial();
    }

    // Método: Sair do programa
    public void sair() {
        menu.message("\nAté uma próxima...");
        menu.setExit(true);
    }
}
