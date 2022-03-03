package centro_reparacoes.business.utilizador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  Utilizador: - Classe que guarda todos os logins, trabalhadores e clientes do centro;
 *              - Possui todos os métodos necessários para manipular todas as variáveis do utilizador.
 */

public class UtilizadorFacade implements IUtilizadorFacade {
    private Map<String, String> login; // Mapa com, key: ids de trabalhadores , value: password
    private Map<String, Trabalhador> trabalhadores; // Mapa com, key: id trabalhador , value: trabalhador
    private Map<String, Cliente> clientes; // Mapa com, key: nif , value: cliente

    public UtilizadorFacade() {
        this.trabalhadores = new HashMap<>();
        this.login = new HashMap<>();
        this.clientes = new HashMap<>();
    }

    // Getters
    public Map<String, String> getLogin() {
        return this.login;
    }

    public Map<String, Trabalhador> getTrabalhadores() {
        return this.trabalhadores;
    }

    public Map<String, Cliente> getClientes() {
        return this.clientes;
    }

    // Método: Obtém o nome de um trabalhador
    public String getNome(String id) {
        return this.trabalhadores.get(id).getNome();
    }

    // Método: Obtém a avaliação atual de um gestor
    public Double getAvaliacao(String id) {
        Gestor g = (Gestor) this.getTrabalhadores().get(id);
        return g.getAvaliacaoDada();
    }

    // Método: Obtém a lista com todos os técnicos
    public List<Tecnico> getTecnicos() {
        List<Tecnico> tecnicos = new ArrayList<>();
        for (Map.Entry<String, Trabalhador> entry : this.trabalhadores.entrySet()) {
            if (entry.getValue() instanceof Tecnico) {
                Tecnico t = (Tecnico) entry.getValue();
                tecnicos.add(t);
            }
        }
        return tecnicos;
    }

    // Método: Obtém a lista com todos os funcionários de balcão
    public List<FuncionarioBalcao> getFBs() {
        List<FuncionarioBalcao> fbs = new ArrayList<>();
        for (Map.Entry<String, Trabalhador> entry : this.trabalhadores.entrySet()) {
            if (entry.getValue() instanceof FuncionarioBalcao) {
                FuncionarioBalcao fb = (FuncionarioBalcao) entry.getValue();
                fbs.add(fb);
            }
        }
        return fbs;
    }

    // Método: Autenticar um trabalhador
    public int autenticarTrabalhador(String idU, String password) {
        if (validaLogin(idU, password)) {
            if (this.trabalhadores.get(idU) instanceof FuncionarioBalcao)
                return 1;
            else if (this.trabalhadores.get(idU) instanceof Tecnico)
                return 2;
            else if (this.trabalhadores.get(idU) instanceof Gestor)
                return 3;
        }
        return -1;
    }

    // Método: Validar login de trabalhador
    public boolean validaLogin(String id, String password) {
        if (this.login.get(id).equals(password))
            return true;
        return false;
    }

    // Método: Validar NIF de cliente
    public boolean validaNIF(String nif) {
        if (nif.matches("[0-9]+") && nif.length() == 9)
            return true;
        return false;
    }

    // Método: Terminar a sessão de um utilizador
    public void terminarSessao(String idUtilizador) {
        this.trabalhadores.get(idUtilizador).setAutenticado(false);
    }

    // Método: Adicionar trabalhador
    public String adicionarTrabalhador(int estatuto, String nome, String email, String password) {
        String[] nomeDividido = nome.split(" ");
        String idNome = nomeDividido[0] + nomeDividido[1];
        String idTrabalhador = "";

        Trabalhador trabalhador = null;
        if (estatuto == 1) { // Funcionário Balcão
            idTrabalhador = "fb" + idNome;
            trabalhador = new FuncionarioBalcao(idTrabalhador, nome, email, password, 0, 0);
        } else if (estatuto == 2) { // Técnico
            idTrabalhador = "tn" + idNome;
            trabalhador = new Tecnico(idTrabalhador, nome, email, password, 0, new ArrayList<>());
        } else if (estatuto == 3) { // Gestor
            idTrabalhador = "gt" + idNome;
            trabalhador = new Gestor(idTrabalhador, nome, email, password, -1);
        }

        if (trabalhador != null) {
            trabalhadores.put(idTrabalhador, trabalhador);
            login.put(idTrabalhador, password);
            return idTrabalhador;
        }

        return "false";
    }

    // Método: ler um trabalhador do ficheiro
    public void lerTrabalhador(int estatuto, String nome, String email, String password, double d, int i1, int i2, List<String> l) {
        String[] nomeDividido = nome.split(" ");
        String idNome = nomeDividido[0] + nomeDividido[1];
        String idTrabalhador = "";

        Trabalhador trabalhador = null;
        if (estatuto == 1) { // Funcionário Balcão
            idTrabalhador = "fb" + idNome;
            trabalhador = new FuncionarioBalcao(idTrabalhador, nome, email, password, i1, i2);
        } else if (estatuto == 2) { // Técnico
            idTrabalhador = "tn" + idNome;
            trabalhador = new Tecnico(idTrabalhador, nome, email, password, i1, l);
        } else if (estatuto == 3) { // Gestor
            idTrabalhador = "gt" + idNome;
            trabalhador = new Gestor(idTrabalhador, nome, email, password, d);
        }

        if (trabalhador != null) {
            trabalhadores.put(idTrabalhador, trabalhador);
            login.put(idTrabalhador, password);
        }
    }

    // Método: Adicionar cliente
    public boolean adicionaCliente(String nome, String nif, String email) {
        String cnif = "c" + nif;
        if (validaNIF(nif)) {
            Cliente cliente = new Cliente(nome, nif, email);
            if (!clientes.containsKey(cnif))
                clientes.put(cnif, cliente);
            return true;
        } else
            return false;
    }

    // Método: Remover trabalhador
    public boolean removeTrabalhador(String idTrabalhador) {
        if (trabalhadores.remove(idTrabalhador) == null) {
            return false;
        }
        return true;
    }

    // Método: Verifica se existem trabalhadores no centro (sem contar com o gestor)
    public boolean haTrabalhadores() {
        return !(this.trabalhadores.size() == 1);
    }

    // Método: Avaliar centro
    public boolean avaliarCentro(String id, Double avaliacao) {
        if (avaliacao >= 0 && avaliacao <= 10) {
            Gestor gestor = (Gestor) trabalhadores.get(id);
            gestor.setAvaliacaoDada(avaliacao);

            return true;
        } else
            return false;
    }

    // Método: Incrementa o número de receções feitas por um funcionário
    public void incrementaRececao(String id_fb) {
        FuncionarioBalcao fb = (FuncionarioBalcao) this.trabalhadores.get(id_fb);
        fb.setRececao(fb.getRececao() + 1);
    }

    // Método: Incrementa o número de entregas feitas por um funcionáro
    public void incrementaEntrega(String id_fb) {
        FuncionarioBalcao fb = (FuncionarioBalcao) this.trabalhadores.get(id_fb);
        fb.setEntrega(fb.getEntrega() + 1);
    }

    // Método: Adiciona um pedido a um técnico
    public void adicionaPedidoAoTecnico(String id_tecnico, String id_pedido) {
        Tecnico t = (Tecnico) this.trabalhadores.get(id_tecnico);
        List<String> l = t.getListaPedidos();
        l.add(id_pedido);
        t.setListaPedidos(l);
        t.setReparacoes(t.getReparacoes() + 1);
    }

    // Método: Constrói uma lista de análise do técnico dado
    public List<String> dadosTecnico(Tecnico t, List<Integer> resultados) {
        List<String> dados = new ArrayList<>();
        int reparacoes = t.getReparacoes();
        int media = 0;
        int mediaDesvios = 0;

        if (reparacoes != 0) {
            media = resultados.get(0) / reparacoes;
            mediaDesvios = resultados.get(1) / reparacoes;
        }

        dados.add(t.getNome());
        dados.add(Integer.toString(reparacoes));
        dados.add(Integer.toString(media));
        dados.add(Integer.toString(mediaDesvios));
        return dados;
    }

    // Método: Constrói uma lista com as informações de um funcionário de balcão
    public List<String> infFB(FuncionarioBalcao fb) {
        List<String> informacoes = new ArrayList<>();
        informacoes.add(fb.getNome());
        informacoes.add(Integer.toString(fb.getRececao()));
        informacoes.add(Integer.toString(fb.getEntrega()));
        return informacoes;
    }

    // Método: Constrói uma lista com o id e o nome do técnico dado
    public List<String> infTecnico(Tecnico t) {
        List<String> informacoes = new ArrayList<>();
        informacoes.add(t.getID());
        informacoes.add(t.getNome());
        return informacoes;
    }
}
