#ifndef MENU_H
#define MENU_H

#include <stdio.h>
#include <string.h>

void showMenu();  // Imprime a mensagem inicial do nosso programa

void show(char** matrix, int inicial, int range, int rangeMAX, int pag, int max); // Imprime uma página conforme os dados que são dados

void showValue(char* str);  // Imprime uma entrada específica dada

void showMessage(char* message);  // Imprime uma mensagem dada

void showExitProgram(); // Imprime a mensagem de encerramento do programa


#endif