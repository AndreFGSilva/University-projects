package src.model;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import src.view.UI;

public class Index {
    // Método que cria a paginação, com uma indexação de 25 entradas por página. Recebe a lista que a query 1 devolve.
    public static void indexQuery1(List<String> list, int queryNumber, int queryLength, String time) {
        Scanner inputScanner = new Scanner(System.in);
        int exit = 0, pageNum = 1, index = 0;
        while (exit == 0) {
            UI.indexListS_UI(list, queryNumber, index, queryLength, pageNum, time);
            String option = inputScanner.nextLine();
            int intValue = -1, isNumeric = -1;
            try {
                intValue = Integer.parseInt(option);
                isNumeric = 1;
            } catch (NumberFormatException e) {
                isNumeric = 0;
            }
            if (isNumeric == 1)
                switch (intValue) {
                    case 1: // Página anterior
                        if (pageNum > 1) {
                            index -= 25;
                            pageNum--;
                        }
                        break;
                    case 2: // Página seguinte
                        if (pageNum * 25 < queryLength) {
                            index += 25;
                            pageNum++;
                        }
                        break;
                    case 3: // Voltar atrás
                        exit = 1;
                        break;
                    default:    // Opção inválida
                        System.out.print("\nInseriu uma opção inválida...\n");
                        break;
                }
        }
    }

    // Método que auxilia a UI para a query 2.
    public static void indexQuery2(Pair pair, int queryNumber, String time) {
        Scanner inputScanner = new Scanner(System.in);
        int exit = 0;
        while (exit == 0) {
            UI.indexPairUI(pair, queryNumber, time);
            String option = inputScanner.nextLine();
            int intValue = -1, isNumeric = -1;
            try {
                intValue = Integer.parseInt(option);
                isNumeric = 1;
            } catch (NumberFormatException e) {
                isNumeric = 0;
            }
            if (isNumeric == 1)
                switch (intValue) {
                    case 1: // Voltar atrás
                        exit = 1;
                        break;
                    default:    // Opção inválida
                        System.out.print("\nInseriu uma opção inválida...\n");
                        break;
                }
        }
    }

    // Método que auxilia a UI para a query 3 e 4.
    public static void indexQuery3_4(TupleIIF tuple, int queryNumber, String time) {
        Scanner inputScanner = new Scanner(System.in);
        int exit = 0;
        while (exit == 0) {
            UI.indexTupleIIF_UI(tuple, queryNumber, time);
            String option = inputScanner.nextLine();
            int intValue = -1, isNumeric = -1;
            try {
                intValue = Integer.parseInt(option);
                isNumeric = 1;
            } catch (NumberFormatException e) {
                isNumeric = 0;
            }
            if (isNumeric == 1)
                switch (intValue) {
                    case 1: // Voltar atrás
                        exit = 1;
                        break;
                    default:    // Opção inválida
                        System.out.print("\nInseriu uma opção inválida...\n");
                        break;
                }
        }
    }

    // Método que cria a paginação, com uma indexação de 25 entradas por página. Recebe a lista que a query 5 devolve.
    public static void indexQuery5_8(List<Map.Entry<String, Integer>> list, int queryNumber, int queryLength, String time) {
        Scanner inputScanner = new Scanner(System.in);
        int exit = 0, pageNum = 1, index = 0;
        while (exit == 0) {
            UI.indexListM_UI(list, queryNumber, index, queryLength, pageNum, time);
            String option = inputScanner.nextLine();
            int intValue = -1, isNumeric = -1;
            try {
                intValue = Integer.parseInt(option);
                isNumeric = 1;
            } catch (NumberFormatException e) {
                isNumeric = 0;
            }
            if (isNumeric == 1)
                switch (intValue) {
                    case 1: // Página anterior
                        if (pageNum > 1) {
                            index -= 25;
                            pageNum--;
                        }
                        break;
                    case 2: // Página seguinte
                        if (pageNum * 25 < queryLength) {
                            index += 25;
                            pageNum++;
                        }
                        break;
                    case 3: // Voltar atrás
                        exit = 1;
                        break;
                    default:    // Opção inválida
                        System.out.print("\nInseriu uma opção inválida...\n");
                        break;
                }
        }
    }

    // Método que cria a paginação, com uma indexação feita por ano. Recebe a lista que a query 6 devolve.
    public static void indexQuery6(List<Map.Entry<Integer, Map<String, Integer>>> list, int queryNumber, int queryLength, String time, int maxPerYear) {
        Scanner inputScanner = new Scanner(System.in);
        int exit = 0, pageNum = 1, index = 0, actualList = 0;
        while (exit == 0) {
            UI.indexListMM_UI(list, actualList, queryNumber, index, queryLength, pageNum, time, maxPerYear);
            String option = inputScanner.nextLine();
            int intValue = -1, isNumeric = -1;
            try {
                intValue = Integer.parseInt(option);
                isNumeric = 1;
            } catch (NumberFormatException e) {
                isNumeric = 0;
            }
            if (isNumeric == 1)
                switch (intValue) {
                    case 1: // Página anterior
                        if (pageNum > 1) {
                            index -= maxPerYear;
                            actualList--;
                            pageNum--;
                        }
                        break;
                    case 2: // Página seguinte
                        if (pageNum * maxPerYear < queryLength) {
                            index += maxPerYear;
                            pageNum++;
                            actualList++;
                        }
                        break;
                    case 3: // Voltar atrás
                        exit = 1;
                        break;
                    default:    // Opção inválida
                        System.out.print("\nInseriu uma opção inválida...\n");
                        break;
                }
        }
    }

    // Método que cria a paginação, com uma indexação por cidade. Recebe o map que a query 7 devolve.
    public static void indexQuery7(Map<String, List<Map.Entry<String, Integer>>> map, int queryNumber, int queryLength, String time) {
        Scanner inputScanner = new Scanner(System.in);
        int numberCities = map.size();
        int exit = 0, actualCity = 0, index = 0;
        while (exit == 0) {
            UI.indexMapLM_UI(map, queryNumber, queryLength, index, actualCity, time);
            String option = inputScanner.nextLine();
            int intValue = -1, isNumeric = -1;
            try {
                intValue = Integer.parseInt(option);
                isNumeric = 1;
            } catch (NumberFormatException e) {
                isNumeric = 0;
            }
            if (isNumeric == 1)
                switch (intValue) {
                    case 1: // Recuar de cidade
                        if (actualCity > 0) {
                            index -= 3;
                            actualCity--;
                        }
                        break;
                    case 2: // Avançar de cidade
                        if (actualCity < numberCities-1) {
                            index += 3;
                            actualCity++;
                        }
                        break;
                    case 3: // Voltar atrás
                        exit = 1;
                        break;
                    default:    // Opção inválida
                        System.out.print("\nInseriu uma opção inválida...\n");
                        break;
                }
        }
    }

    // Método que cria a paginação, com uma indexação de 25 entradas por página. Recebe a lista que a query 9 devolve.
    public static void indexQuery9(List<Map.Entry<String, PairF>> list, int queryNumber, int queryLength, String time) {
        Scanner inputScanner = new Scanner(System.in);
        int exit = 0, pageNum = 1, index = 0;
        while (exit == 0) {
            UI.indexListMSP_UI(list, queryNumber, index, queryLength, pageNum, time);
            String option = inputScanner.nextLine();
            int intValue = -1, isNumeric = -1;
            try {
                intValue = Integer.parseInt(option);
                isNumeric = 1;
            } catch (NumberFormatException e) {
                isNumeric = 0;
            }
            if (isNumeric == 1)
                switch (intValue) {
                    case 1: // Página anterior
                        if (pageNum > 1) {
                            index -= 25;
                            pageNum--;
                        }
                        break;
                    case 2: // Página seguinte
                        if (pageNum * 25 < queryLength) {
                            index += 25;
                            pageNum++;
                        }
                        break;
                    case 3: // Voltar atrás
                        exit = 1;
                        break;
                    default:    // Opção inválida
                        System.out.print("\nInseriu uma opção inválida...\n");
                        break;
                }
        }
    }

    // Método que cria a paginação, com uma indexação feita por cidades. Recebe a lista que a query 8 devolve.
    public static void indexQuery10(List<Map.Entry<String, List<Map.Entry<String, List<Map.Entry<String, Float>>>>>> list, int queryNumber, int queryLength, String time) {
        Scanner inputScanner = new Scanner(System.in);
        int numberStates = list.size();
        int exit = 0, actualState = 0, actualCity = 0;
        while (exit == 0) {
            UI.indexListLMLM_UI(list, actualState, actualCity, queryNumber, queryLength, time);
            String option = inputScanner.nextLine();
            int intValue = -1, isNumeric = -1;
            try {
                intValue = Integer.parseInt(option);
                isNumeric = 1;
            } catch (NumberFormatException e) {
                isNumeric = 0;
            }
            if (isNumeric == 1)
                switch (intValue) {
                    case 1: // Recuar de estado
                        if (actualState > 0) {
                            actualCity = 0;
                            actualState--;
                        }
                        break;
                    case 2: // Avançar de estado
                        if (actualState < numberStates-1) {
                            actualState++;
                            actualCity = 0;
                        }
                        break;
                    case 3: // Recuar de cidade
                        if (actualCity > 0)
                            actualCity--;
                        break;
                    case 4: // Avançar de cidade
                        int numberCities = list.get(actualState).getValue().size();
                        if (actualCity < numberCities-1)
                            actualCity++;
                        break;
                    case 5: // Voltar atrás
                        exit = 1;
                        break;
                    default:    // Opção inválida
                        System.out.print("\nInseriu uma opção inválida...\n");
                        break;
                }
        }
    }
}
