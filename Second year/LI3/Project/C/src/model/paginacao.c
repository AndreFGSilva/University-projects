#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "../../includes/model/paginacao.h"


/* Função Auxiliar
Input: (char*)Linha
Objetivo: Verificar se a string que foi dada só é constituída por números
Retorna: (int)Resultado */
int isNumberSTR(char* line) {
  int result = 0;

  if (line == NULL)     // Verificamos se a string recebida é nula. * Se sim, retornamos -1. * Se não, prosseguimos.
    return -1;

  int i;
  for (i = 0; line[i]; i++)
    if (isNumberC(line[i]) == 1) {    // Percorremos a string char a char.
      result = 1;                     // Invocamos a função auxiliar 'isNumberC' que verifica se o char é número
      break;                          // ou não. Se não for número, terminamos o ciclo.
    }

  if (i == 4 && (line[0] != '1' || line[0] != '0'))     // Condições para não permitir números acima de 1000.
    if (line[1] != '0' || line[2] != '0' || line[3] != '0')   // Máximo definido por nós. Avisamos também o
      result = 1;                                             // utilizador do mesmo.

  return result;
}

/* Função Auxiliar
Input: (char)Char
Objetivo: Verificar se o char que foi dado é um número
Retorna: (int)Resultado */
int isNumberC(char c) {
  int result = 0;

  if (c != '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8' && c != '9')
    result = 1;

  return result;
}

/* Função
Input: (char**)Tabela , (int)Posição
Objetivo: Realizar a paginação de uma tabela pedida para ser mostrada */
void paginacao(char **query, int pos) {
  int max = 0, option = 0;
  if (query != NULL)
    for (max; query[max]; max++);   // Guardamos numa variável o máximo de entradas que essa tabela tem.

  int range = 20;   // Por default a range de entradas por página é de 20.
  showMessage("Indique quantas linhas deseja ver por página (máximo de 1000)");
  char *line1 = NULL;
  size_t len1 = 0;
  if (getline(&line1, &len1, stdin) != -1) {    // Pedimos ao utilizador para nos informar de quantas entradas
    char *line1_AUX = strtok(line1, "\r\n");    // quer ver por página.
    int cN = isNumberSTR(line1_AUX);    // Verificamos se é um número válido e se está dentro do nosso limite
    if (cN == 0)                        // de entradas por páginas (1000).
      range = atoi(line1_AUX);
    else {
      showMessage("Inseriu um número inválido. A mostrar 20 linhas por página.");
      range = 20;
    }
  }
  else
    option = 0;

  free(line1);
  int exit = 0, pag = 1, rangeMAX = 0;
  show(query, pos, range, rangeMAX, pag, max);
  while (!exit) {
    char *line = NULL;
    size_t len = 0;
    if (getline(&line, &len, stdin) != -1) { 
      char *optionSTR = strtok(line, "\r\n");
      if (optionSTR != NULL)
        if (strcmp(optionSTR, "1") == 0 || strcmp(optionSTR, "2") == 0 || strcmp(optionSTR, "3") == 0 || strcmp(optionSTR, "4") == 0)
          option = atoi(optionSTR);   // Verificamos se nos é dado alguma opção válida.
        else
          option = 0;
      else
        option = 0;
    }
    else
      option = 0;

    switch (option) {
      case 1:   // Página anterior
        if (pos-range < 0 || pag == 1) {    // Verificamos se não é possível recuar uma página.
          pos = 1;                    // * Se não for possível, mostramos a página inicial.
          show(query, pos, range, rangeMAX, pag, max);
        }
        else {    // * Se for possível, então recuamos uma página.
          pos -= range;
          pag--;
          rangeMAX = 0;
          show(query, pos, range, rangeMAX, pag, max);
        }

        break;
      case 2:   // Página seguinte
        if (pos+(2*range)-max > 0 && rangeMAX == 0) {   // Verificamos se é possível avançar uma página.
          pag++;                          // Tendo em conta o número de entradas que esta tabela tem.
          pos += range;                   // * Se for possível, então avançamos uma página.
          rangeMAX = max - pos;           // E mostramos a mesma com o número de entradas pedido.
        }
        else
          if (pos+(2*range)-max > 0 && rangeMAX != 0)
            rangeMAX = max - pos;         // * Se não for possível, verificamos se o número de entradas antes dado
          else {                          // por página, é menor que o número máximo de entradas. * Se não for,
          pag++;                          // avançamos apenas até ao número máximo de entradas. * Se for,
          pos += range;                   // avançamos com a página normal.
          rangeMAX = 0;
          }

        if (query[pos])
          show(query, pos, range, rangeMAX, pag, max);

        break;
      case 3: ;   // Modificar range
        showMessage("Indique quantas linhas deseja ver por página (máximo de 1000)");
        char *lineR = NULL;
        size_t lenR = 0;
        if (getline(&lineR, &lenR, stdin) != -1) {
          char *lineR_AUX = strtok(lineR, "\r\n");
          int checkNumber = isNumberSTR(lineR_AUX);
          if (checkNumber == 0)
            range = atoi(lineR_AUX);
          else {
            showMessage("Inseriu um número inválido. A mostrar 20 linhas por página.");
            range = 20;
          }
        }
        else
          range = 20;

        pos = 1;    // Sempre que é pedido para modificar a range, voltamos para a primeira página.
        pag = 1;
        show(query, pos, range, rangeMAX, pag, max);

        break;
      case 4:   // Sair
        exit = 1;

        break;
      default:
        showMessage("Inseriu uma opção inválida.");

        break;
    }
    free(line);
  }
}