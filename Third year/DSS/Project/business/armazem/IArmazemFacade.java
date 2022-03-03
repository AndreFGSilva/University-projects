package centro_reparacoes.business.armazem;

import java.util.List;
import java.util.Map;

/*
 *  IArmazem: - Inferface da classe Armazem;
 */

public interface IArmazemFacade {

    Map<String, Equipamento> getEquipamentos();

    Equipamento getEquipamento(String id_equipamento);

    boolean haEquipamentos();

    void removeEquipamento(String id_equipamento);

    void adicionaEquipamento(String id, String nomeE);

    boolean adicionaAbandonado(String id_equipamento);

    boolean estaAbandonado(String id_equipamento);

    Map<String, List<String>> getInfArquivados(List<String> ids);

    Map<String, List<String>> getInfAbandonados(List<String> ids);
}
