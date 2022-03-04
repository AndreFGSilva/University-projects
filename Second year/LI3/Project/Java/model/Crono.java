package src.model;

import static java.lang.System.nanoTime;

public class Crono {
    private static long startTime = 0L;
    private static long endTime = 0L;

    // Método que guarda na variável 'startTime' o tempo atual e que reseta a variável 'endTime'.
    public static void start() {
        startTime = nanoTime();
        endTime = 0L;
    }

    // Método para guardar na variável 'endTime' o tempo atual, retornando o tempo que passou desde o 'startTime'.
    public static double stop() {
        endTime = nanoTime();
        return (endTime - startTime)/1.0E09;
    }

    // Getters
    public static String getTime() {
        return Double.toString(stop());
    }

    // Método para retornar uma string com o tempo passado.
    public static String getTimeString() {
        return "\nPassou "+ getTime() +" segundos\n";
    }
}
