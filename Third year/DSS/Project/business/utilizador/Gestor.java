package centro_reparacoes.business.utilizador;

/*
 *  Gestor: - Classe que extende a classe abstrata Trabalhador;
 *          - Classe que guarda toda a informação de um gestor;
 */

public class Gestor extends Trabalhador {
    double avaliacaoDada; // Variável que guarda a avaliação dada pelo gestor

    Gestor(String id, String nome, String email, String password, double avaliacaoDada) {
        super(id, nome, email, password);
        this.avaliacaoDada = avaliacaoDada;
    }

    // Getters
    public double getAvaliacaoDada() {
        return this.avaliacaoDada;
    }

    // Setters
    public void setAvaliacaoDada(double avaliacao) {
        this.avaliacaoDada = avaliacao;
    }

    // Método: Transforma as informações do Gestor para String
    public String toFile(){
        return String.valueOf(avaliacaoDada);
    }
}
