package centro_reparacoes;

import centro_reparacoes.ui.*;
import centro_reparacoes.data.*;
import centro_reparacoes.business.*;

public class Main {

    // Método: Cria a aplicação e invoca o método run()
    public static void main(String[] args) {
        try {
            Parse p = new Parse();
            ICentroReparacoesFacade iCR = p.parsing("src/centro_reparacoes/data/data.txt");
            new TextUI(iCR).run();
            p.writeToFile("src/centro_reparacoes/data/data.txt");
        } catch (Exception e) {
            System.out.println("O programa nao conseguiu correr: " + e.getMessage());
        }
    }
}
