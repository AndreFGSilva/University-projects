package centro_reparacoes.business.armazem;

/*
 *  Equipamento: - Classe que guarda toda a informação de um equipamento.
 */

public class Equipamento {
    private String id; // ID do equipamento
    private String nome; // Nome do equipamento

    public Equipamento(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters
    public String getID() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    // Método: Transforma as informações do Equipamento para String
    public String toFile(){
        return "e;" + this.id + ";" + this.nome + "\n";
    }
}
