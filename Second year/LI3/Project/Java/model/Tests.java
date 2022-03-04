package src.model;

import java.util.Scanner;

public class Tests {
    public static void main(String[] args) {
        Crono.start();
        System.out.print("\nPerformance Tests\n");
        GestReviews sgr = null;
        String usersFilePath = "project_java/db/users_full.csv";
        String businessesFilePath = "project_java/db/business_full.csv";
        String reviewsFilePath = "project_java/db/reviews_1M.csv";
        sgr = new GestReviews();
        sgr.loadSGRFromFiles(usersFilePath, businessesFilePath, reviewsFilePath);

        System.out.println("\nFiles read time: " + Crono.getTime() + "\n");

        // Query 1
        Crono.start();
        sgr.query1();
        System.out.println("Query 1: " + Crono.getTime() + "\n");

        // Query 2
        Crono.start();
        sgr.query2(10, 2010);
        System.out.println("Query 2: " + Crono.getTime() + "\n");

        // Query 3
        Crono.start();
        sgr.query3("YoVfDbnISlW0f7abNQACIg");
        System.out.println("Query 3: " + Crono.getTime() + "\n");

        // Query 4
        Crono.start();
        sgr.query4("0AzLzHfOJgL7ROwhdww2ew");
        System.out.println("Query 4: " + Crono.getTime() + "\n");

        // Query 5
        Crono.start();
        sgr.query5("YoVfDbnISlW0f7abNQACIg");
        System.out.println("Query 5: " + Crono.getTime() + "\n");

        // Query 6
        Crono.start();
        sgr.query6(5);
        System.out.println("Query 6: " + Crono.getTime() + "\n");

        // Query 7
        Scanner inputScanner = new Scanner(System.in);
        System.out.print("Deseja correr a query7? (1: Sim , 2: Não)");
        System.out.print("\nOpção: ");
        String option = inputScanner.nextLine();
        int intValue = -1, isNumeric = -1;
        try {
            intValue = Integer.parseInt(option);
            isNumeric = 1;
        } catch (NumberFormatException e) {
            isNumeric = 0;
        }
        if (isNumeric == 1)
            if (intValue == 1) {
            Crono.start();
            sgr.query7();
            System.out.println("Query 7: " + Crono.getTime() + "\n");
            }
        else
            System.out.println("Inseriu uma opção inválida ou escolheu não correr a query7...\n");

        // Query 8
        Crono.start();
        sgr.query8(10);
        System.out.println("Query 8: " + Crono.getTime() + "\n");

        // Query 9
        Crono.start();
        sgr.query9("v1UzkU8lEWdjxq8byWFOKg", 3000);
        System.out.println("Query 9: " + Crono.getTime() + "\n");

        // Query 10
        Crono.start();
        sgr.query10();
        System.out.println("Query 10: " + Crono.getTime() + "\n");

        Crono.stop();
        System.out.print("Performance tests ended");
    }
}