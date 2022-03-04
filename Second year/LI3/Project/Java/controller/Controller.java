package src.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import src.model.Business;
import src.model.Crono;
import src.model.GestReviews;
import src.model.Index;
import src.model.Pair;
import src.model.PairF;
import src.model.TupleIIF;
import src.model.User;
import src.view.UI;

public class Controller {
    public static void run() {
        GestReviews sgr = null;
        Scanner inputScanner = new Scanner(System.in);
        int exit = 0;
        while (exit == 0) {
            UI.mainMenuUI();
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
                    case 1: // Carregar/Gravar ficheiros
                        int exitC1 = 0;
                        while (exitC1 == 0) {
                            UI.loadSaveMenuUI();
                            String option_C1 = inputScanner.nextLine();
                            int intValue_C1 = -1, isNumeric_C1 = -1;
                            try {
                                intValue_C1 = Integer.parseInt(option_C1);
                                isNumeric_C1 = 1;
                            } catch (NumberFormatException e) {
                                isNumeric_C1 = 0;
                            }
                            if (isNumeric_C1 == 1)
                                switch (intValue_C1) {
                                    case 1: // Carregar a partir de ficheiros de texto predefinidos
                                        sgr = new GestReviews();
                                        String businessesFilePathC1 = "project_java/db/business_full.csv";
                                        String reviewsFilePathC1 = "project_java/db/reviews_1M.csv";
                                        String usersFilePathC1 = "project_java/db/users_full.csv";
                                        
                                        Crono.start();
                                        sgr.loadSGRFromFiles(usersFilePathC1, businessesFilePathC1, reviewsFilePathC1);
                                        System.out.print(Crono.getTimeString());
                                        break;
                                    case 2: // Carregar a partir de ficheiros de texto personalizados
                                        UI.loadTextMenuUI();
                                        sgr = new GestReviews();
                                        String businessesFilePathC2 = inputScanner.nextLine();
                                        String reviewsFilePathC2 = inputScanner.nextLine();
                                        String usersFilePathC2 = inputScanner.nextLine();

                                        Crono.start();
                                        sgr.loadSGRFromFiles(usersFilePathC2, businessesFilePathC2, reviewsFilePathC2);
                                        System.out.print(Crono.getTimeString());
                                        break;
                                    case 3: // Carregar a partir de ficheiro de objetos predefinidos
                                        sgr = new GestReviews();
                                        String objectFilePathC3 = "project_java/obj/gestReviews.dat";
                                        try {
                                            Crono.start();
                                            sgr = GestReviews.loadObject(objectFilePathC3);
                                            System.out.print(Crono.getTimeString());
                                        } catch (IOException | ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 4: // Carregar a partir de ficheiro de objetos personalizados
                                        UI.loadObjectMenuUI();
                                        sgr = new GestReviews();
                                        String objectFilePathC4 = inputScanner.nextLine();
                                        try {
                                            Crono.start();
                                            sgr = GestReviews.loadObject(objectFilePathC4);
                                            System.out.print(Crono.getTimeString());
                                        } catch (IOException | ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 5: // Gravar em ficheiro de objetos num caminho predefinido
                                        String objectFilePathC5 = "project_java/obj/gestReviews.dat";
                                        try {
                                            Crono.start();
                                            GestReviews.saveObject(sgr, objectFilePathC5);
                                            System.out.print(Crono.getTimeString());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 6: // Gravar em ficheiro de objetos num caminho personalizado
                                        UI.saveMenuUI();
                                        String objectFilePathC6 = inputScanner.nextLine();
                                        try {
                                            Crono.start();
                                            GestReviews.saveObject(sgr, objectFilePathC6);
                                            System.out.print(Crono.getTimeString());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case 7: // Voltar atrás
                                        exitC1 = 1;
                                        break;
                                    default:    // Opção inválida
                                        System.out.print("\nInseriu uma opção inválida...\n");
                                        break;
                                }
                            else
                                System.out.print("\nInseriu uma opção inválida...\n");
                        }
                        break;
                    case 2: // Consultar estatísticas
                        if (sgr != null) {
                            int exitC2 = 0;
                            while (exitC2 == 0) {
                                UI.statsMenuUI();
                                String option_C2 = inputScanner.nextLine();
                                int intValue_C2 = -1, isNumeric_C2 = -1;
                                try {
                                    intValue_C2 = Integer.parseInt(option_C2);
                                    isNumeric_C2 = 1;
                                } catch (NumberFormatException e) {
                                    isNumeric_C2 = 0;
                                }
                                if (isNumeric_C2 == 1)
                                    switch (intValue_C2) {
                                        case 1: // Nome dos ficheiros lidos
                                            String businessesFileName = sgr.getStats().getBusinessPath().substring(sgr.getStats().getBusinessPath().lastIndexOf("/")+1);
                                            String usersFileName = sgr.getStats().getUsersPath().substring(sgr.getStats().getUsersPath().lastIndexOf("/")+1);
                                            String reviewsFileName = sgr.getStats().getReviewsPath().substring(sgr.getStats().getReviewsPath().lastIndexOf("/")+1);
                                            int exitCC1 = 0;
                                            while (exitCC1 == 0) {
                                                UI.filesNames(businessesFileName, usersFileName, reviewsFileName);
                                                String option_CC1 = inputScanner.nextLine();
                                                int intValue_CC1 = -1, isNumeric_CC1 = -1;
                                                try {
                                                    intValue_CC1 = Integer.parseInt(option_CC1);
                                                    isNumeric_CC1 = 1;
                                                } catch (NumberFormatException e) {
                                                    isNumeric_CC1 = 0;
                                                }
                                                if (isNumeric_CC1 == 1 && intValue_CC1 == 1)
                                                    exitCC1 = 1;
                                                else
                                                    System.out.print("\nInseriu uma opção inválida...\n");
                                            }
                                            break;
                                        case 2: // Estatísticas dos negócios
                                            int exitCC2 = 0;
                                            while (exitCC2 == 0) {
                                                UI.businessesStatsMenuUI(sgr.getStats());
                                                String option_CC2 = inputScanner.nextLine();
                                                int intValue_CC2 = -1, isNumeric_CC2 = -1;
                                                try {
                                                    intValue_CC2 = Integer.parseInt(option_CC2);
                                                    isNumeric_CC2 = 1;
                                                } catch (NumberFormatException e) {
                                                    isNumeric_CC2 = 0;
                                                }
                                                if (isNumeric_CC2 == 1 && intValue_CC2 == 1)
                                                    exitCC2 = 1;
                                                else
                                                    System.out.print("\nInseriu uma opção inválida...\n");
                                            }
                                            break;
                                        case 3: // Estatísticas das avaliações
                                            int exitCC3 = 0;
                                            while (exitCC3 == 0) {
                                                UI.reviewsStatsMenuUI(sgr.getStats());
                                                String option_CC3 = inputScanner.nextLine();
                                                int intValue_CC3 = -1, isNumeric_CC3 = -1;
                                                try {
                                                    intValue_CC3 = Integer.parseInt(option_CC3);
                                                    isNumeric_CC3 = 1;
                                                } catch (NumberFormatException e) {
                                                    isNumeric_CC3 = 0;
                                                }
                                                if (isNumeric_CC3 == 1 && intValue_CC3 == 1)
                                                    exitCC3 = 1;
                                                else
                                                    System.out.print("\nInseriu uma opção inválida...\n");
                                            }
                                            break;
                                        case 4: // Estatísticas dos utilizadores
                                            int exitCC4 = 0;
                                            while (exitCC4 == 0) {
                                                UI.usersStatsMenuUI(sgr.getStats());
                                                String option_CC4 = inputScanner.nextLine();
                                                int intValue_CC4 = -1, isNumeric_CC4 = -1;
                                                try {
                                                    intValue_CC4 = Integer.parseInt(option_CC4);
                                                    isNumeric_CC4 = 1;
                                                } catch (NumberFormatException e) {
                                                    isNumeric_CC4 = 0;
                                                }
                                                if (isNumeric_CC4 == 1 && intValue_CC4 == 1)
                                                    exitCC4 = 1;
                                                else
                                                    System.out.print("\nInseriu uma opção inválida...\n");
                                            }
                                            break;
                                        case 5: // Estatísticas das estruturas
                                            int exitCC5 = 0;
                                            while (exitCC5 == 0) {
                                                UI.structureStatsMenuUI(sgr.getStructuresStats());
                                                String option_CC5 = inputScanner.nextLine();
                                                int intValue_CC5 = -1, isNumeric_CC5 = -1;
                                                try {
                                                    intValue_CC5 = Integer.parseInt(option_CC5);
                                                    isNumeric_CC5 = 1;
                                                } catch (NumberFormatException e) {
                                                    isNumeric_CC5 = 0;
                                                }
                                                if (isNumeric_CC5 == 1 && intValue_CC5 == 1)
                                                    exitCC5 = 1;
                                                else
                                                    System.out.print("\nInseriu uma opção inválida...\n");
                                            }
                                            break;
                                        case 6: // Voltar atrás
                                            exitC2 = 1;
                                            break;
                                        default:    // Opção inválida
                                            System.out.print("\nInseriu uma opção inválida...\n");
                                            break;
                                    }
                                else
                                    System.out.print("\nInseriu uma opção inválida...\n");
                            }
                        }
                        else                  
                            System.out.print("\nAinda não carregou nenhum ficheiro...\n");
                        break;
                    case 3: // Consultar queries
                        if (sgr != null) {
                            int exitC3 = 0;
                            while (exitC3 == 0) {
                                UI.querysMenuUI();
                                String option_C3 = inputScanner.nextLine();
                                int intValue_C3 = -1, isNumeric_C3 = -1;
                                try {
                                    intValue_C3 = Integer.parseInt(option_C3);
                                    isNumeric_C3 = 1;
                                } catch (NumberFormatException e) {
                                    isNumeric_C3 = 0;
                                }
                                if (isNumeric_C3 == 1)
                                    switch (intValue_C3) {
                                        case 1: // Query 1
                                            Crono.start();
                                            List<String> result_Q1 = sgr.query1();
                                            Index.indexQuery1(result_Q1, 1, result_Q1.size(), Crono.getTime());
                                            break;
                                        case 2: // Query 2
                                            System.out.print("\nIntroduza um mês\n");
                                            String month_Q2 = inputScanner.nextLine();
                                            System.out.print("Introduza um ano\n");
                                            String year_Q2 = inputScanner.nextLine();
                                            int isNumeric_Q2 = -1, intValueM_Q2 = -1, intValueY_Q2 = -1;
                                            try {
                                                intValueM_Q2 = Integer.parseInt(month_Q2);
                                                intValueY_Q2 = Integer.parseInt(year_Q2);
                                                isNumeric_Q2 = 1;
                                            } catch (NumberFormatException e) {
                                                isNumeric_Q2 = 0;
                                            }
                                            if (isNumeric_Q2 == 1 && intValueM_Q2 >= 1 && intValueM_Q2 <= 12 && year_Q2.length() == 4) {
                                                Crono.start();
                                                Pair result_Q2 = sgr.query2(intValueM_Q2-1, intValueY_Q2);
                                                Index.indexQuery2(result_Q2, 2, Crono.getTime());
                                            }
                                            else
                                                System.out.print("\nIntroduziu um número inválido...\n");
                                            break;
                                        case 3: // Query 3
                                            System.out.print("\nIntroduza o código de um utilizador (\"V\" para voltar atrás)\n");
                                            int lenUserID_Q3 = sgr.getUsers().getTreeUsers().first().getID().length();
                                            String userID_Q3 = "0";
                                            while (!userID_Q3.equals("V") && !sgr.getUsers().getTreeUsers().contains(new User(userID_Q3))) {
                                                userID_Q3 = inputScanner.nextLine();
                                                if (userID_Q3.length() != lenUserID_Q3)
                                                    break;
                                            }
                                            if (!userID_Q3.equals("V") && userID_Q3.length() == lenUserID_Q3) {
                                                Crono.start();
                                                TupleIIF result_Q3 = sgr.query3(userID_Q3);
                                                Index.indexQuery3_4(result_Q3, 3, Crono.getTime());
                                            }
                                            else
                                                System.out.print("\nIntroduziu um código de utilizador inválido ou pediu para voltar atrás...\n");
                                            break;
                                        case 4: // Query 4  
                                            System.out.print("\nIntroduza o código de um negócio (\"V\" para voltar atrás)\n");
                                            int lenBusinessID_Q4 = sgr.getBusinesses().getTreeBusinesses().first().getBusinessID().length();
                                            String businessID_Q4 = "0";
                                            while (!businessID_Q4.equals("V") && !sgr.getBusinesses().getTreeBusinesses().contains(new Business(businessID_Q4))) {
                                                businessID_Q4 = inputScanner.nextLine();
                                                if (businessID_Q4.length() != lenBusinessID_Q4)
                                                    break;
                                            }
                                            if (!businessID_Q4.equals("V") && businessID_Q4.length() == lenBusinessID_Q4) {
                                                Crono.start();
                                                TupleIIF result_Q4 = sgr.query4(businessID_Q4);
                                                Index.indexQuery3_4(result_Q4, 4, Crono.getTime());
                                            }
                                            else
                                                System.out.print("\nIntroduziu um código de negócio inválido ou pediu para voltar atrás...\n");
                                            break;
                                        case 5: // Query 5
                                            System.out.print("\nIntroduza o código de um utilizador (\"V\" para voltar atrás)\n");
                                            int lenUserID_Q5 = sgr.getUsers().getTreeUsers().first().getID().length();
                                            String userID_Q5 = "0";
                                            while (!userID_Q5.equals("V") && !sgr.getUsers().getTreeUsers().contains(new User(userID_Q5))) {
                                                userID_Q5 = inputScanner.nextLine();
                                                if (userID_Q5.length() != lenUserID_Q5)
                                                    break;
                                            }
                                            if (!userID_Q5.equals("V") && userID_Q5.length() == lenUserID_Q5) {
                                                Crono.start();
                                                List<Map.Entry<String, Integer>> result_Q5 = sgr.query5(userID_Q5);
                                                Index.indexQuery5_8(result_Q5, 5, result_Q5.size(), Crono.getTime());
                                            }
                                            else
                                                System.out.print("\nIntroduziu um código de utilizador inválido ou pediu para voltar atrás...\n");
                                            break;
                                        case 6: // Query 6
                                            System.out.print("\nIntroduza o número máximo de negócios que quer ver (\"V\" para voltar atrás)\n");
                                            int firstTime_Q6 = 0;
                                            String max_Q6 = "0";
                                            while (max_Q6.equals("0") && firstTime_Q6 <= 1) {
                                                max_Q6 = inputScanner.nextLine();
                                                firstTime_Q6++;
                                            }
                                            int isNumeric_Q6 = -1, intValue_Q6 = -1;
                                            try {
                                                intValue_Q6 = Integer.parseInt(max_Q6);
                                                isNumeric_Q6 = 1;
                                            } catch (NumberFormatException e) {
                                                isNumeric_Q6 = 0;
                                            }
                                            if (isNumeric_Q6 == 1 && intValue_Q6 > 0) {
                                                Crono.start();
                                                List<Map.Entry<Integer, Map<String, Integer>>> result_Q6 = sgr.query6(intValue_Q6);
                                                int queryLength_Q6 = result_Q6.size() * intValue_Q6;
                                                Index.indexQuery6(result_Q6, 6, queryLength_Q6, Crono.getTime(), intValue_Q6);
                                            }
                                            else
                                                System.out.print("\nIntroduziu um número inválido ou pediu para voltar atrás...\n");
                                            break;
                                        case 7: // Query 7
                                            Crono.start();
                                            Map<String, List<Map.Entry<String, Integer>>> result_Q7 = sgr.query7();
                                            int queryLength_Q7 = result_Q7.size() * 3;
                                            Index.indexQuery7(result_Q7, 7, queryLength_Q7, Crono.getTime());
                                            break;
                                        case 8: // Query 8
                                            System.out.print("\nIntroduza o número máximo de utilizadores que quer ver (\"V\" para voltar atrás)\n");
                                            int firstTime_Q8 = 0;
                                            String max_Q8 = "0";
                                            while (max_Q8.equals("0") && firstTime_Q8 <= 1) {
                                                max_Q8 = inputScanner.nextLine();
                                                firstTime_Q8++;
                                            }
                                            int isNumeric_Q8 = -1, intValue_Q8 = -1;
                                            try {
                                                intValue_Q8 = Integer.parseInt(max_Q8);
                                                isNumeric_Q8 = 1;
                                            } catch (NumberFormatException e) {
                                                isNumeric_Q8 = 0;
                                            }
                                            if (isNumeric_Q8 == 1 && intValue_Q8 > 0) {
                                                Crono.start();
                                                List<Entry<String, Integer>> result_Q8 = sgr.query8(intValue_Q8);
                                                Index.indexQuery5_8(result_Q8, 8, result_Q8.size(), Crono.getTime());
                                            }
                                            else
                                                System.out.print("\nIntroduziu um número inválido ou pediu para voltar atrás...\n");
                                            break;
                                        case 9: // Query 9
                                            System.out.print("\nIntroduza o código de um negócio\n");
                                            String businessID_Q9 = inputScanner.nextLine();
                                            System.out.print("Introduza o máximo de utilizadores que quer ver\n");
                                            String max_Q9 = inputScanner.nextLine();
                                            int isNumeric_Q9 = -1, intValue_Q9 = -1;
                                            try {
                                                intValue_Q9 = Integer.parseInt(max_Q9);
                                                isNumeric_Q9 = 1;
                                            } catch (NumberFormatException e) {
                                                isNumeric_Q9 = 0;
                                            }
                                            if (isNumeric_Q9 == 1 && sgr.getBusinesses().getTreeBusinesses().contains(new Business(businessID_Q9)) && intValue_Q9 > 0) {
                                                Crono.start();
                                                List<Map.Entry<String, PairF>> result_Q9 = sgr.query9(businessID_Q9, intValue_Q9);
                                                Index.indexQuery9(result_Q9, 9, result_Q9.size(), Crono.getTime());
                                            }
                                            else
                                                System.out.print("\nIntroduziu um código inválido ou um número inválido...\n");
                                            break;
                                        case 10:    // Query 10
                                            Crono.start();
                                            List<Map.Entry<String, List<Map.Entry<String, List<Map.Entry<String, Float>>>>>> result_Q10 = sgr.query10();
                                            Index.indexQuery10(result_Q10, 10, sgr.getStats().getValidBusinesses(), Crono.getTime());
                                            break;
                                        case 11:    // Voltar atrás
                                            exitC3 = 1;
                                            break;
                                        default:    // Opção inválida
                                            System.out.print("\nInseriu uma opção inválida...\n");
                                            break;
                                    }
                                else
                                    System.out.print("\nInseriu uma opção inválida...\n");
                            }
                        }
                        else
                            System.out.print("\nAinda não carregou nenhum ficheiro...\n");
                        break;
                    case 4: // Sair
                        UI.exitMenuUI();
                        inputScanner.close();
                        exit = 1;
                        break;
                    default:    // Opção inválida
                        System.out.print("\nInseriu uma opção inválida...\n");
                        break;
                }
            else
                System.out.print("\nInseriu uma opção inválida...\n");
        }
    }
}