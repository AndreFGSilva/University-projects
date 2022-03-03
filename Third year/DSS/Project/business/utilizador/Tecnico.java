package centro_reparacoes.business.utilizador;

import java.util.List;

/*
 *  Tecnico:    - Classe que extende a classe abstrata Trabalhador;
 *              - Classe que guarda toda a informação de um técnico;
 */

public class Tecnico extends Trabalhador {
    int reparacoes; // Variável que guarda o número de reparações efetuadas
    List<String> listaPedidos; // Lista que contém os ids dos pedidos reparados

    Tecnico(String id, String nome, String email, String password, int reparacoes, List<String> listaPedidos) {
        super(id, nome, email, password);
        this.reparacoes = reparacoes;
        this.listaPedidos = listaPedidos;
    }

    // Getters
    public int getReparacoes() {
        return this.reparacoes;
    }

    public List<String> getListaPedidos() {
        return this.listaPedidos;
    }

    // Setters
    public void setReparacoes(int reparacoes) {
        this.reparacoes = reparacoes;
    }

    public void setListaPedidos(List<String> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    // Método: Transforma as informações do Técnico para String
    public String toFile(){
        StringBuilder s = new StringBuilder();
        for(String p : this.listaPedidos)
            s.append(p + ",");
        s.deleteCharAt(s.length()-1);
        return this.reparacoes + ";" + s.toString();
    }

}
