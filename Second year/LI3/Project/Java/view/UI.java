package src.view;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import src.model.Pair;
import src.model.PairF;
import src.model.Stats;
import src.model.StructuresStats;
import src.model.TupleIIF;

public class UI {
    // Menu inicial do programa.
    public static void mainMenuUI() {
        StringBuilder sb = new StringBuilder("\n*------------------------------------------------*"
                                            +"\n|                Plataforma Yelp!                |"
                                            +"\n*------------------------------------------------*");
        sb.append("\n| 1. Carregar/Gravar ficheiros");
        sb.append("\n| 2. Consultar estatísticas");
        sb.append("\n| 3. Consultar queries");
        sb.append("\n| 4. Sair");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu intermédio de escolha entre carregar ou gravar ficheiros.
    public static void loadSaveMenuUI() {
        StringBuilder sb = new StringBuilder("\n\t   *-- Carregar/Gravar ficheiros --*");
        sb.append("\n| 1. Carregar a partir de ficheiros de texto predefinidos");
        sb.append("\n| 2. Carregar a partir de ficheiros de texto personalizados");
        sb.append("\n| 3. Carregar a partir de ficheiro de objetos predefinido");
        sb.append("\n| 4. Carregar a partir de ficheiro de objetos personalizado");
        sb.append("\n| 5. Gravar em ficheiro de objetos num caminho predefinido");
        sb.append("\n| 6. Gravar em ficheiro de objetos num caminho personalizado");
        sb.append("\n| 7. Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu para carregar a partir de ficheiros de texto.
    public static void loadTextMenuUI() {
        StringBuilder sb = new StringBuilder("\nIntroduza os caminhos para os ficheiros, um por linha, pela seguinte ordem: negócios, avaliações e utilizadores\n");
        System.out.print(sb.toString());
    }

    // Menu para carregar a partir de ficheiro de objetos.
    public static void loadObjectMenuUI() {
        StringBuilder sb = new StringBuilder("\nIntroduza o caminho para o ficheiro\n");
        System.out.print(sb.toString());
    }

    // Menu para gravar ficheiros.
    public static void saveMenuUI() {
        StringBuilder sb = new StringBuilder("\nIntroduza o caminho para onde deseja que o ficheiro seja criado\n");
        System.out.print(sb.toString());
    }

    // Menu intermédio de escolhe entre as possíveis consultas de estatísticas.
    public static void statsMenuUI() {
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Estatísticas --*");
        sb.append("\n| 1. Nome dos ficheiros lidos");
        sb.append("\n| 2. Referentes aos negócios");
        sb.append("\n| 3. Referentes às avaliações");
        sb.append("\n| 4. Referentes aos utilizadores");
        sb.append("\n| 5. Referentes às estruturas");
        sb.append("\n| 6. Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu com os nomes dos ficheiros.
    public static void filesNames(String businessesFileName, String usersFileName, String reviewsFileName) {
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Nome dos ficheiros --*");
        sb.append("\n* Negócios: "+ businessesFileName);
        sb.append("\n* Utilizadores: "+ usersFileName);
        sb.append("\n* Avaliações: "+ reviewsFileName);
        sb.append("\n| 1. Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu com as várias opções de estatística referente aos negócios.
    public static void businessesStatsMenuUI(Stats stats) {
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Estatísticas dos negócios --*");
        sb.append("\n* Número total de negócios: "+ Integer.toString(stats.getValidBusinesses()));
        sb.append("\n* Número total de diferentes negócios avaliados: "+ Integer.toString(stats.getBusinessesReviewed()));
        sb.append("\n* Número total de diferentes negócios não avaliados: "+ Integer.toString(stats.getBusinessesNotReviewed()));
        sb.append("\n| 1. Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu com as várias opções de estatística referente às avaliações.
    public static void reviewsStatsMenuUI(Stats stats) {
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Estatísticas das avaliações --*");
        sb.append("\n* Número total de avaliações inválidas: "+ Integer.toString(stats.getInvalidReviews()));
        sb.append("\n* Número total de avaliações sem impacto: "+ Integer.toString(stats.getNoImpactReviews()));
        sb.append("\n| 1. Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu com as várias opções de estatística referente aos utilizadores.
    public static void usersStatsMenuUI(Stats stats) {
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Estatísticas dos utilizadores --*");
        sb.append("\n* Número total de utilizadores: "+ Integer.toString(stats.getValidUsers()));
        sb.append("\n* Número total de utilizadores que realizaram pelo menos uma avaliação: "+ Integer.toString(stats.getUsersThatReviewed()));
        sb.append("\n* Número total de utilizadores que não realizaram nenhuma avaliação: "+ Integer.toString(stats.getUsersThatDidntReview()));
        sb.append("\n| 1. Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu com as várias opções de estatística referente à estrutura.
    public static void structureStatsMenuUI(StructuresStats structuresStats) {
        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Estatísticas das estruturas --*");
        sb.append("\n* Número total de avaliações por mês:");
        for (int month = 0; month < 12; month++)
            sb.append("\n\tMês "+ (month+1) +": "+ Integer.toString(structuresStats.getRevPerMonth(month)) +" avaliações");
        
        sb.append("\n\n* Média das avaliações por mês:");
        double totalReviews = 0;
        for (int month = 0; month < 12; month++) {
            sb.append("\n\tMês "+ (month+1) +": "+ df.format(structuresStats.getMediaPerMonth(month)));
            totalReviews += structuresStats.getMediaPerMonth(month);
        }

        sb.append("\n\n* Média total de avaliações: "+ df.format(totalReviews/12));

        sb.append("\n\n* Número total de diferentes utilizadores que avaliaram em cada mês: ");
        for (int month = 0; month < 12; month++)
            sb.append("\n\tMês "+ (month+1) +": "+ Integer.toString(structuresStats.getRevUserPerMonth(month)) +" utilizadores");
        sb.append("\n\n| 1. Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu intermédio de escolha entre as possíveis queries.
    public static void querysMenuUI() {
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Queries --*/");
        sb.append("\n| 1. Query 1");
        sb.append("\n| 2. Query 2");
        sb.append("\n| 3. Query 3");
        sb.append("\n| 4. Query 4");
        sb.append("\n| 5. Query 5");
        sb.append("\n| 6. Query 6");
        sb.append("\n| 7. Query 7");
        sb.append("\n| 8. Query 8");
        sb.append("\n| 9. Query 9");
        sb.append("\n| 10. Query 10");
        sb.append("\n| 11. Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu para terminar o programa.
    public static void exitMenuUI() {
        StringBuilder sb = new StringBuilder("\n*------------------------------------------------*"
                                            +"\n|                Até uma próxima!                |"
                                            +"\n|                Plataforma Yelp!                |"
                                            +"\n*------------------------------------------------*\n");
        System.out.print(sb.toString());
    }

    // Menu para a paginação das queries que devolvem o resultado numa lista de Strings.
    public static void indexListS_UI(List<String> list, int queryNumber, int index, int queryLength, int pageNum, String time) {
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Query "+ Integer.toString(queryNumber) +" --*");
        sb.append("\n*------------------------------------------------*");
        for (int i = index; i < (index+25); i++) {
            if (i < queryLength)
                sb.append("\n"+ Integer.toString(i) +"| "+ list.get(i));
            else
                break;
        }
        sb.append("\n*------------------------------------------------*");
        sb.append("\n| Página "+ Integer.toString(pageNum) +"   Total "+ Integer.toString(queryLength) +"   Tempo "+ time +" segundos");
        sb.append("\n| (1)Página anterior   (2)Página seguinte");
        sb.append("\n| (3)Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu para a paginação das queries que devolvem o resultado numa lista de Maps (Map<String, Integer>).
    public static void indexListM_UI(List<Map.Entry<String,Integer>> list, int queryNumber, int index, int queryLength, int pageNum, String time) {
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Query "+ Integer.toString(queryNumber) +" --*");
        sb.append("\n*------------------------------------------------*");
        for (int i = index; i < (index+25); i++)
            if (i < queryLength)
                sb.append("\n"+ Integer.toString(i) +"| "+ list.get(i).getKey() +" - "+ list.get(i).getValue());
            else
                break;
        sb.append("\n*------------------------------------------------*");
        sb.append("\n| Página "+ Integer.toString(pageNum) +"   Total "+ Integer.toString(queryLength) +"   Tempo "+ time +" segundos");
        sb.append("\n| (1)Página anterior   (2)Página seguinte");
        sb.append("\n| (3)Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu para a paginação das queries que devolvem o resultado numa lista de Maps de Maps (Map<Integer, Map<String, Integer>>).
    public static void indexListMM_UI(List<Map.Entry<Integer, Map<String, Integer>>> list, int actualList, int queryNumber, int index, int queryLength, int pageNum, String time, int maxPerYear) {
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Query "+ Integer.toString(queryNumber) +" --*");
        sb.append("\n*------------------------------------------------*");
        sb.append("\n* Ano "+ list.get(actualList).getKey() +" *");
        List<String> keys = list.get(actualList).getValue().keySet().stream().collect(Collectors.toList());
        List<Integer> values = list.get(actualList).getValue().values().stream().collect(Collectors.toList());
        for (int i = 0; i < maxPerYear; i++, index++)
            sb.append("\n"+ Integer.toString(index) +"| "+ Integer.toString(i+1) + "º | "+ keys.get(i) +" - "+ values.get(i));
        sb.append("\n*------------------------------------------------*");
        sb.append("\n| Página "+ Integer.toString(pageNum) +"   Total "+ Integer.toString(queryLength) +"   Tempo "+ time +" segundos");
        sb.append("\n| (1)Página anterior   (2)Página seguinte");
        sb.append("\n| (3)Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu para a paginação das queries que devolvem o resultado numa lista de Maps (<Map<String, PairF>).
    public static void indexListMSP_UI(List<Map.Entry<String, PairF>> list, int queryNumber, int index, int queryLength, int pageNum, String time) {
        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Query "+ queryNumber +" --*");
        sb.append("\n*------------------------------------------------*");
        for (int i = index; i < (index+25); i++)
            if (i < queryLength)
                sb.append("\n"+ i +"| "+ list.get(i).getKey() +" - "+ list.get(i).getValue().getY() +" - "+ df.format(list.get(i).getValue().getX()));
            else
                break;
        sb.append("\n*------------------------------------------------*");
        sb.append("\n| Página "+ Integer.toString(pageNum) +"   Total "+ queryLength +"   Tempo "+ time +" segundos");
        sb.append("\n| (1)Página anterior   (2)Página seguinte");
        sb.append("\n| (3)Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu para a paginação das queries que devolvem o resultado numa lista de Maps de lista de Maps de lista de Maps (Map<String, List<Map<String, List<Map<String, Float>>>>>).
    public static void indexListLMLM_UI(List<Map.Entry<String, List<Map.Entry<String, List<Map.Entry<String, Float>>>>>> list, int actualState, int actualCity, int queryNumber, int queryLength, String time) {
        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Query "+ Integer.toString(queryNumber) +" --*");
        sb.append("\n*------------------------------------------------*");
        sb.append("\n* Estado "+ list.get(actualState).getKey() +": Cidade "+ list.get(actualState).getValue().get(actualCity).getKey() +" *");
        List<Map.Entry<String, Float>> businessesList = list.get(actualState).getValue().get(actualCity).getValue();
        int listLength = businessesList.size();
        for (int i = 0; i < listLength; i++)
            sb.append("\n"+ i +"| "+ businessesList.get(i).getKey() +" - "+ df.format(businessesList.get(i).getValue()));
        sb.append("\n*------------------------------------------------*");
        sb.append("\n| Página "+ actualCity+1 +"   Total "+ Integer.toString(queryLength) +"   Tempo "+ time +" segundos");
        sb.append("\n| (1)Recuar estado   (2)Avançar estado");
        sb.append("\n| (3)Recuar cidade   (4)Avançar cidade");
        sb.append("\n| (5)Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu para a mostragem de um par.
    public static void indexPairUI(Pair pair, int queryNumber, String time) {
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Query "+ Integer.toString(queryNumber) +" --*");
        switch (queryNumber) {
            case 2:
                sb.append("\n* Número total de avaliações realizadas: "+ Integer.toString(pair.getX()));
                sb.append("\n* Número total de utilizadores distintos que as realizaram: "+ Integer.toString(pair.getY()));
                sb.append("\n* Tempo "+ time +" segundos");
                sb.append("\n| 1. Voltar atrás");
                sb.append("\n\nOpção: ");
                break;
            default:
                sb.append("\nOpção inválida...\n\n");
        }
        System.out.print(sb.toString());
    }

    // Menu para a mostragem de um tuplo (Int, Int, Float).
    public static void indexTupleIIF_UI(TupleIIF tuple, int queryNumber, String time) {
        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Query "+ Integer.toString(queryNumber) +" --*");
        sb.append("\n* Número de avaliações, por mês:");
        for (int month = 0; month < 12; month++) {
            int[] tupleX = tuple.getX();
            sb.append("\n\tMês "+ (month+1) +": "+ Integer.toString(tupleX[month]) +" avaliações");
        }

        switch (queryNumber) {
            case 3:
                sb.append("\n\n* Número de negócios distintos que avaliou, por mês:");
                for (int month = 0; month < 12; month++) {
                    int[] tupleY = tuple.getY();
                    sb.append("\n\tMês "+ (month+1) +": "+ Integer.toString(tupleY[month]) +" negócios");
                }
                break;
            case 4:
                sb.append("\n\n* Número de utilizadores distintos que avaliaram o negócio, por mês:");
                for (int month = 0; month < 12; month++) {
                    int[] tupleY = tuple.getY();
                    sb.append("\n\tMês "+ (month+1) +": "+ Integer.toString(tupleY[month]) +" utilizadores");
                }
                break;
        }
        
        sb.append("\n\n* Nota média das avaliações, por mês:");
        for (int month = 0; month < 12; month++) {
            float[] tupleZ = tuple.getZ();
            sb.append("\n\tMês "+ (month+1) +": "+ df.format(tupleZ[month]));
        }

        sb.append("\n\n* Tempo "+ time +" segundos");
        sb.append("\n| 1. Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }

    // Menu para a paginação das queries que devolvem o resultado num Map de lista de Maps (List<Map<String, Integer>>).
    public static void indexMapLM_UI(Map<String, List<Map.Entry<String, Integer>>> map, int queryNumber, int queryLength, int index, int actualCity, String time) {
        StringBuilder sb = new StringBuilder("\n\n\t   *-- Query "+ Integer.toString(queryNumber) +" --*");
        sb.append("\n*------------------------------------------------*");
        List<String> citiesList = map.keySet().stream().collect(Collectors.toList());
        List<List<Map.Entry<String, Integer>>> businessesList = map.values().stream().collect(Collectors.toList());
        sb.append("\n* Cidade "+ citiesList.get(actualCity) +" *");
        for (int i = 0; i < 3; i++, index++)
            sb.append("\n"+ index +"| "+ i+1 +"º | "+ businessesList.get(0).get(i).getKey() +" - "+ businessesList.get(0).get(i).getValue());
        sb.append("\n*------------------------------------------------*");
        sb.append("\n| Página "+ actualCity+1 +"   Total "+ Integer.toString(queryLength) +"   Tempo "+ time +" segundos");
        sb.append("\n| (1)Recuar cidade   (2)Avançar cidade");
        sb.append("\n| (3)Voltar atrás");
        sb.append("\n\nOpção: ");
        System.out.print(sb.toString());
    }
}
