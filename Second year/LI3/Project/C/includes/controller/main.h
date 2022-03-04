#ifndef MAIN_H
#define MAIN_H

#include "../model/sgr.h"
#include "../view/ui.h"
#include "../model/paginacao.h"

void freeMatrix(char** query, int max); // Libertar a memória alocada a uma tabela

char** separateString(char* string, char delim);  // Separa uma string pelo seu separador

int validColumn(char* column_name, char* columns);  // Verificar se o nome da coluna existe em todas as colunas

int validOP(char* operator);  // Verificar se o operador dado é válido

int filterAUX(char* entry, char* value, int operator);  // Verificar se a entrada dada é válida para o operador e o valor dado

char** filter(char** query, int column, char* value, int operator); // Construir uma tabela nova com todas as linhas que são válidas dependendo do operador e do valor a comparar

char** proj(char** query, int* colsNumber, int N);  // Construir uma tabela com os dados das tabelas pretendidas

int toCSV(char** table, char delim, char* filepath);  // Imprimir num ficheiro .csv o conteúdo de uma matriz, em que cada coluna da matriz é separada pelo separador dado

char** fromCSV(FILE* fileCSV, char delim);  // Atribuir a uma matriz o conteúdo de um ficheiro .csv

int transformFunction(char* function);  // Atribuir um inteiro dependendo da string recebida

char** getArgs_from_input(char* input, int argc); // Criar uma matriz com os inputs recebidos

char* getIndexValue(char* str, int y);  // Devolver a String da coluna pedida


#endif