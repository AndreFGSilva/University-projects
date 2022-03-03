package centro_reparacoes.business.utilizador;

/*
 *  Trabalhador: - Classe abstrata que guarda informação de um trabalhador.
 */

public abstract class Trabalhador {
    String id; // ID que é gerado automaticamente depois de ser registado um trabalhador
    String nome; // Primeiro e último nome
    String email; // Email
    String password; // Password para a autenticação do membro
    boolean autenticado; // Variável para controlar se está ou não autenticado

    Trabalhador(String id, String nome, String email, String password) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.autenticado = false;
    }

    // Getters
    public String getID() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean getAutenticado() {
        return this.autenticado;
    }

    // Setters
    public void setID(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAutenticado(boolean autenticado) {
        this.autenticado = autenticado;
    }

    // Método: Transforma as informações do Trabalhador para String
    public String toFile(String estatuto){
        if (estatuto.equals("fb")){
            FuncionarioBalcao fb = (FuncionarioBalcao) this;
            return "t;" + estatuto + ";" + this.nome + ";" + this.email + ";" + this.password + ";" + fb.toFile() + "\n";
        }    
        if (estatuto.equals("tn")){
            Tecnico t = (Tecnico) this;
            return "t;" + "2" + ";" + this.nome + ";" + this.email + ";" + this.password + ";" + t.toFile() + "\n";
        }    
        if (estatuto.equals("gt")){
            Gestor g = (Gestor) this;
            return "t;" + estatuto + ";" + this.nome + ";" + this.email + ";" + this.password + ";" + g.toFile() + "\n";
        }    
        return "";            
    }
}
