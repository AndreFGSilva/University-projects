package centro_reparacoes.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import centro_reparacoes.business.ICentroReparacoesFacade;
import centro_reparacoes.business.armazem.Equipamento;
import centro_reparacoes.business.reparacao.Pedido;
import centro_reparacoes.business.utilizador.Cliente;
import centro_reparacoes.business.utilizador.Trabalhador;
import centro_reparacoes.business.CentroReparacoesFacade;


public class Parse {

    ICentroReparacoesFacade iCR;

    // Método: Lê dados a partir de um ficheiro
    public ICentroReparacoesFacade parsing(String filename) {
        this.iCR = new CentroReparacoesFacade();

        try {
            File f = new File(filename);
            Scanner myReader = new Scanner(f);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splitted = data.split(";", 2);
                if (splitted[0].equals("c")) {
                    parseCliente(splitted[1]);
                }
                if (splitted[0].equals("p")) {
                    parsePedido(splitted[1]);
                }
                if (splitted[0].equals("e")){
                    parseEquipamento(splitted[1]);
                }
                if (splitted[0].equals("t")){
                    parseTrabalhador(splitted[1]);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro não existe!!");
            e.printStackTrace();
        }

        return this.iCR;
    }

    // Método: Parsing de um Cliente
    public void parseCliente(String line) {
        String[] parameters = line.split(";");
        this.iCR.adicionaCliente(parameters[0], parameters[1], parameters[2]);
    }
    
    // Método: Parsing de um Trabalhador
    public void parseTrabalhador(String line){
        String[] parameters = line.split(";");
        if (parameters[0].equals("1")){
            List<String> s = new ArrayList<>();
            this.iCR.lerTrabalhador(Integer.parseInt(parameters[0]), parameters[1], parameters[2], parameters[3], 0.0, Integer.parseInt(parameters[4]), Integer.parseInt(parameters[5]), s); 
        }
        if (parameters[0].equals("2")){
            List<String> s = new ArrayList<>();
            String[] lista = parameters[5].split(",");
            for (String l : lista)
                s.add(l);
                this.iCR.lerTrabalhador(Integer.parseInt(parameters[0]), parameters[1], parameters[2], parameters[3], 0.0, Integer.parseInt(parameters[4]), 0, s);    
        }
        if (parameters[0].equals("3")){
            List<String> s = new ArrayList<>();
            this.iCR.lerTrabalhador(Integer.parseInt(parameters[0]), parameters[1], parameters[2], parameters[3], Double.parseDouble(parameters[4]), 0, 0, s); 
        }    
    }
    
    // Método: Parsing de um Pedido
    public void parsePedido(String line) {
        List<String> passos = new ArrayList<>();
        List<Integer> custos = new ArrayList<>();
        List<Integer> tempo = new ArrayList<>(); 

        String[] parameters = line.split(";");

        if (!parameters[4].equals("-1")){
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime dataPedido = LocalDateTime.parse(parameters[6], formatter);
            LocalDateTime dataInicioReparacao = null;
            if (!parameters[7].equals("null"))
                dataInicioReparacao = LocalDateTime.parse(parameters[7], formatter);
            LocalDateTime dataFimReparacao = null;
            if (!parameters[8].equals("null"))
                dataFimReparacao = LocalDateTime.parse(parameters[8], formatter);

            String[] passocustohora = parameters[9].split("-");
            String[] passos_split, custos_split, tempo_split;
            if (!parameters[9].equals("null")) {
                passos_split = passocustohora[0].split(",");
                custos_split = passocustohora[1].split(",");
                tempo_split = passocustohora[2].split(",");

                for(int i = 0; i < passos_split.length; i++){
                    passos.add(passos_split[i]);
                }
                for(int i = 0; i < custos_split.length; i++){
                    custos.add(Integer.parseInt(custos_split[i]));
                }
                for(int i = 0; i < tempo_split.length; i++){
                    tempo.add(Integer.parseInt(tempo_split[i]));
                }
            }

            this.iCR.adicionaPedido(Integer.parseInt(parameters[0]), parameters[1], parameters[2], parameters[3], Integer.parseInt(parameters[4]), Integer.parseInt(parameters[5]),
            dataPedido, dataInicioReparacao, dataFimReparacao, passos, custos, tempo, Double.parseDouble(parameters[10]), Boolean.parseBoolean(parameters[11]),
            Boolean.parseBoolean(parameters[12]), Boolean.parseBoolean(parameters[13]));
        }
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime dataPedido = LocalDateTime.parse(parameters[6], formatter); 
            this.iCR.adicionaPedidoSemPT(Integer.parseInt(parameters[0]), parameters[1], parameters[2], Integer.parseInt(parameters[4]), Integer.parseInt(parameters[5]),
            dataPedido);
        }    
    }

    // Método: Parsing de um equipamento
    public void parseEquipamento(String line) {
        String[] parameters = line.split(";");
        this.iCR.adicionaEquipamento(parameters[0], parameters[1]);
    }

    // Método: Escrita dos dados para um ficheiro
    public void writeToFile(String filename) {
        try {
            FileWriter myWriter = new FileWriter(filename);
            for(Map.Entry<String, Cliente> entry : this.iCR.getAllClientes().entrySet()) {
                myWriter.write(entry.getValue().toFile());
            }
            for(Map.Entry<String, Equipamento> entry : this.iCR.getAllEquipamentos().entrySet()) {
                myWriter.write(entry.getValue().toFile());
            }
            for(Map.Entry<String, Trabalhador> entry : this.iCR.getAllTrabalhadores().entrySet()) {
                String key = entry.getKey().substring(0, 2); //temos que separar a key de forma a ter por exemplo só o fb e convertemos para o estatuto em String, 1, 2 ou 3
                myWriter.write(entry.getValue().toFile(key));
            }
            for(Map.Entry<String, Pedido> entry : this.iCR.getAllPedidos().entrySet()) {
                myWriter.write(entry.getValue().toFile(entry.getValue().getEstado()));
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Ficheiro não existe!!");
        }
    }
}
