package centro_reparacoes.business.utilizador;

/*
 *  FuncionarioBalcao:  - Classe que extende a classe abstrata Trabalhador;
 *                      - Classe que guarda toda a informação de um funcionário de balcão;
 */

public class FuncionarioBalcao extends Trabalhador {
    int rececao; // Variável que guarda o número de receções de equipamento efetuadas
    int entrega; // Variável que guarda o número de entregas de equipamento efetuadas

    FuncionarioBalcao(String id, String nome, String email, String password, int rececao, int entrega) {
        super(id, nome, email, password);
        this.rececao = rececao;
        this.entrega = entrega;
    }

    // Getters
    public int getRececao() {
        return this.rececao;
    }

    public int getEntrega() {
        return this.entrega;
    }

    // Setters
    public void setRececao(int rececao) {
        this.rececao = rececao;
    }

    public void setEntrega(int entrega) {
        this.entrega = entrega;
    }

    // Método: Transforma as informações do Funcionário Balcão para String
    public String toFile(){
        return this.rececao + ";" + this.entrega;
    }
}
