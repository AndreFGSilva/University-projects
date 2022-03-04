#include "../../includes/view/ui.h"


/* Função
-- Objetivo: Imprime a mensagem inicial do nosso programa */
void showMenu() {
  printf("\n--------------------------------------------------\n");
  printf("|                Plataforma Yelp!                |");
  printf("\n--------------------------------------------------\n\n");
}

/* Função
-- Input: (char**)Tabela , (int)Posição inicial , (int)Número de entradas por página , (int)Número de entradas por página quando chegar ao
seu máximo , (int)Número da página , (int)Número total de entradas
-- Objetivo: Imprime uma página conforme os dados que são dados */
void show(char** matrix, int inicial, int range, int rangeMAX, int pag, int max) {
  printf("\n--------------------------------------------------\n");
  printf("| %s\n--\n", matrix[0]);

  if (rangeMAX == 0)    // Verificamos se estamos na última página, pois a variável 'rangeMAX' só é != de 0 quando estamos na última página.
    for (int i = inicial; i < (inicial+range) && matrix[i] && i < max; i++) // Percorremos a tabela linha a linha até à range dada
      printf("| %s\n", matrix[i]);
  else
    for (int i = inicial+1; i < (inicial+rangeMAX) && matrix[i] && i < max; i++)
      printf("| %s\n", matrix[i]);
  printf("--------------------------------------------------\n");

  if (rangeMAX == 0)
    printf("-- Página %d -- %d até %d -- Entradas %d\n", pag, inicial, inicial+range, max);
  else
    printf("-- Página %d -- %d até %d -- Entradas %d\n", pag, inicial, inicial+rangeMAX, max);

  printf("-- (1)Página anterior     (2)Página seguinte\n");
  printf("-- (3)Modificar a range   (4)Sair");
  printf("\n--------------------------------------------------\n");
}

/* Função
-- Input: (char*)Entrada específica de uma tabela
-- Objetivo: Imprime uma entrada específica dada */
void showValue(char* str) {
  if (str != NULL) {
    printf("\n--------------------------------------------------\n");
    printf("| %s", str);
    printf("\n--------------------------------------------------\n\n");
  }
}

/* Função
-- Input: (char*)Mensagem
-- Objetivo: Imprime uma mensagem dada */
void showMessage(char* message) {
  printf("\n%s\n", message);
}

/* Função
-- Objetivo: Imprime a mensagem de encerramento do programa */
void showExitProgram() {
  printf("\n--------------------------------------------------\n");
  printf("|                Até uma próxima!                |\n");
  printf("|                Plataforma Yelp!                |");
  printf("\n--------------------------------------------------\n");
}