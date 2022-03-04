#ifndef PAGINACAO_H
#define PAGINACAO_H

#include "../view/ui.h"

int isNumberSTR(char* line);  // Verificar se a string que foi dada só é constituída por números

int isNumberC(char c);  // Verificar se o char que foi dado é um número

void paginacao(char** query, int pos);    // Realizar a paginação de uma tabela pedida para ser mostrada


#endif