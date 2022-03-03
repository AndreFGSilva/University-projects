package centro_reparacoes.business.armazem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  Armazem:    - Classe que guarda todos os equipamentos presentes no armazém;
 *              - Possui todos os métodos necessários para manipular o armazém.
 */

public class ArmazemFacade implements IArmazemFacade {
    private Map<String, Equipamento> equipamentos; // Mapa com, key: ID do equipamento, value: equipamento
    private List<String> abandonados; // Lista com ids dos equipamentos abandonados

    public ArmazemFacade() {
        this.equipamentos = new HashMap<>();
        this.abandonados = new ArrayList<>();
    }

    // Getters
    public Map<String, Equipamento> getEquipamentos() {
        return this.equipamentos;
    }

    public List<String> getAbandonados() {
        return this.abandonados;
    }

    // Método: Dado um id de equipamento, retorna o equipamento
    public Equipamento getEquipamento(String id_equipamento) {
        return this.equipamentos.get(id_equipamento);
    }

    // Método: Verifica se existem equipamentos no armazém
    public boolean haEquipamentos() {
        if (this.equipamentos.size() == 0)
            return false;
        return true;
    }

    // Método: Remove um id de equipamento do armazém
    public void removeEquipamento(String id_equipamento) {
        this.equipamentos.remove(id_equipamento);
    }

    // Método: Adiciona um equipamento ao armazém
    public void adicionaEquipamento(String id, String nomeE) {
        this.equipamentos.put(id, new Equipamento(id, nomeE));
    }

    // Método: Adiciona um equipamento abandonado
    public boolean adicionaAbandonado(String id_equipamento){
        return this.abandonados.add(id_equipamento);
    }
    
    // Método: Verifica se um certo equipamento está abandonado
    public boolean estaAbandonado(String id_equipamento){
        return this.abandonados.contains(id_equipamento);
    }

    // Método: Obtém um mapa com informações de equipamentos arquivados
    public Map<String, List<String>> getInfArquivados(List<String> ids) {
        Map<String, List<String>> arquivados = new HashMap<>();
        for (int i = 0; i < ids.size(); i+=2) {
            if (!arquivados.containsKey(ids.get(i))) {
                List<String> dados = new ArrayList<>();
                dados.add(this.equipamentos.get(ids.get(i)).getNome());
                dados.add(ids.get(i+1));
                arquivados.put(ids.get(i), dados);
            }
        }
        return arquivados;
    }

    // Método: Obtém um mapa com informações de equipamentos abandonados
    public Map<String, List<String>> getInfAbandonados(List<String> ids) {
        Map<String, List<String>> abandonados = new HashMap<>();
        for (int i = 0; i < this.abandonados.size(); i++) {
            if (!abandonados.containsKey(this.abandonados.get(i)))
                for (int j = 0; j < ids.size(); j+=2) {
                    if (ids.get(j).equals(this.abandonados.get(i))) {
                        List<String> dados = new ArrayList<>();
                        dados.add(this.equipamentos.get(this.abandonados.get(i)).getNome());
                        dados.add(ids.get(j+1));
                        abandonados.put(this.abandonados.get(i), dados);
                    }
                }
        }
        return abandonados;
    }
}
