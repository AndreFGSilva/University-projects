import java.util.Map;
import java.util.stream.Collectors;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Utilizadores implements Serializable {

  /* STARTING functions */

  private String codigo;
  private String nome;
  private String email;
  private String pw;
  private Ponto GPS;
  private Map<String, Encomenda> encomendasGuardadas;

  public Utilizadores() {
    this.codigo = new String();
    this.nome = new String();
    this.email = new String();
    this.pw = new String();
    this.GPS = new Ponto();
    this.encomendasGuardadas = new HashMap<>();
  }

  public Utilizadores(String codigo, String nome, String email, String pw, Ponto GPS, Map<String, Encomenda> encomendasGuardadas) {
    this.codigo = codigo;
    this.nome = nome;
    this.email = email;
    this.pw = pw;
    this.GPS = GPS.clone();
    setEncomendasGuardadas(encomendasGuardadas);
  }

  public Utilizadores(Utilizadores u) {
    this.codigo = u.getCodigo();
    this.nome = u.getNome();
    this.email = u.getEmail();
    this.pw = u.getPW();
    this.GPS = u.getGPS();
    setEncomendasGuardadas(u.getEncomendasGuardadas());
  }


  /* GET functions */

  public String getCodigo() {
    return this.codigo;
  }

  public String getNome() {
    return this.nome;
  }

  public String getEmail() {
    return this.email;
  }

  public String getPW() {
    return this.pw;
  }

  public Ponto getGPS() {
    return this.GPS.clone();
  }

  public Map<String, Encomenda> getEncomendasGuardadas() {
    Map<String, Encomenda> newEncomendasGuardadas = new HashMap<>();

    for (Map.Entry<String, Encomenda> e: this.encomendasGuardadas.entrySet())
      newEncomendasGuardadas.put(e.getKey(), e.getValue().clone());
    
    return newEncomendasGuardadas;
  }


  /* SET functions */

  public void setCodigo(String newCodigo) {
    this.codigo = newCodigo;
  }

  public void setNome(String newNome) {
    this.nome = newNome;
  }

  public void setEmail(String newEmail) {
    this.email = newEmail;
  }

  public void setPW(String newPW) {
    this.pw = newPW;
  }

  public void setGPS(Ponto newGPS) {
    this.GPS = newGPS.clone();
  }

  public void setEncomendasGuardadas(Map<String, Encomenda> newEncomendasGuardadas) {
    this.encomendasGuardadas = new HashMap<>();

    newEncomendasGuardadas.entrySet().forEach(e -> this.encomendasGuardadas.put(e.getKey(), e.getValue().clone()));
  }


  /* OTHER functions */

  public Utilizadores clone() {
    return new Utilizadores(this);
  }

  public boolean equals(Object obj) {
    if (obj == this)
      return true;
    if (obj == null || obj.getClass() != this.getClass())
      return false;
    
    Utilizadores u = (Utilizadores) obj;
    
    return super.equals(u);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("C??digo: ").append(this.codigo).append("\n");
    sb.append("Nome: ").append(this.nome).append("\n");
    sb.append("Email: ").append(this.email).append("\n");
    sb.append("GPS: ").append(this.GPS).append("\n");
    
    return sb.toString();
  }

  public String toStringCSV() {
    StringBuilder sb = new StringBuilder();

    sb.append("Utilizador:");
    sb.append(this.codigo).append(",");
    sb.append(this.nome).append(",");
    sb.append(this.GPS.getX()).append(",");
    sb.append(this.GPS.getY());

    return sb.toString();
  }
  

  /* REQUIRED functions */

  // M??todo que adiciona uma encomenda ao registo de encomendas do cliente
  public void adicionarEncomenda(Encomenda e) {
    this.encomendasGuardadas.put(e.getCodEncomenda(), e.clone());
  }

  // M??todo que devolve o hist??rico de encomendas at?? ?? data
  public List<Encomenda> historicoEncomenda() {
    return this.encomendasGuardadas.values().stream().map(Encomenda::clone).collect(Collectors.toList());
  }
}