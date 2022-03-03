package centro_reparacoes.business.utilizador;

/*
 *  Cliente: - Classe que guarda toda a informação de um cliente.
 */

public class Cliente {
    String nome; // Nome do cliente
    String nif; // NIF do cliente
    String email; // Email do cliente

    public Cliente(String nome, String nif, String email) {
        this.nome = nome;
        this.nif = nif;
        this.email = email;
    }

    // Getters
    public String getNome() {
        return this.nome;
    }

    public String getNIF() {
        return this.nif;
    }

    public String getEmail() {
        return this.nome;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNIF(String nif) {
        this.nif = nif;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Método: Transforma as informações do Cliente para String
    public String toFile() {
        return "c;" + this.nome + ";" + this.nif + ";" + this.email + "\n";  
    }
}
