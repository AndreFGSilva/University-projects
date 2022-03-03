package centro_reparacoes.business.reparacao;

import java.util.ArrayList;
import java.util.List;

/*
 *  PlanoTrabalho:  - Classe que guarda toda a informação de um plano de trabalho;
 *                  - Possui todos os métodos necessários para alterar as variáveis de um plano de trabalho.
 */

public class PlanoTrabalho {
    List<String> passos; // Lista com todos os passos necessários para a reparação
    List<Integer> custos; // Lista com todos os custos necessários para cada passo
    List<Integer> tempo; // Lista com todos os tempos necessários para cada passo

    public PlanoTrabalho() {
        this.passos = new ArrayList<>();
        this.custos = new ArrayList<>();
        this.tempo = new ArrayList<>();
    }

    // Getters
    public List<String> getPassos() {
        return this.passos;
    }

    // Método: Adiciona passo, custo e tempo ao plano de trabalho
    public boolean adicionaPassoeCusto(String passo, int custo, int minutos) {
        boolean boolA, boolB, boolC = false;
        boolA = passos.add(passo);
        boolB = custos.add(custo);
        boolC = tempo.add(minutos);

        return boolA & boolB & boolC;
    }

    // Método: Calcula o tempo total do plano de trabalho
    public int calculaTotalMinutos() {
        int total = 0;
        for (int i = 0; i < this.tempo.size(); i++) {
            total += this.tempo.get(i);
        }
        return total;
    }

    // Método: Calcula o custo total do plano de trabalho
    public double calculaTotalGasto() {
        double total = 0;
        for (int i = 0; i < this.custos.size(); i++) {
            total += this.custos.get(i);
        }
        return total;
    }

    // Método: Transforma as informações do Plano de Trabalho para String
    public String toFile(){
        StringBuilder s = new StringBuilder();
        for(String sp : this.passos) {
            s.append(sp + ",");
        }
        s.deleteCharAt(s.length()-1);
        s.append("-");
        for(int sc : this.custos) {
            s.append(sc + ",");
        }
        s.deleteCharAt(s.length()-1);
        s.append("-");
        for(int st : this.tempo) {
            s.append(st + ",");
        }
        s.deleteCharAt(s.length()-1);
        return s.toString();
    }
}
