#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>

#include "../../includes/controller/main.h"

enum OPERATOR{LT, EQ, GT};


/* Função Auxiliar
-- Input: (char**)Tabela
-- Objetivo: Libertar a memória alocada a uma tabela */
void freeMatrix(char** query, int max) {
  if (query != NULL) {
    for (int i = 0; i < max && query[i]; i++) {
      free(query[i]);
    }
    free(query);
  }
}

/* Função Auxiliar
-- Input: (char*)String , (char)Separador
-- Objetivo: Separa uma string pelo seu separador
-- Retorna: (char**)Matriz */
char** separateString(char* string, char delim) {
  char** separated = (char**) malloc(sizeof(char *) * 9);
  char str[250];

  int j = 0, k = 0;
  for (int i = 0; string[i]; i++)
    if (string[i] == delim) {               // Percorremos a string char a char à procura de encontrarmos um char igual ao separador dado.
      str[k] = '\0';                        // Quando encontramos essa posição, 'encerramos' a string e verificamos se existe mais chars
      separated[j++] = strdup(str);         // a seguir. * Se sim, expandimos a matriz.
      k = 0;
    }
    else
      str[k++] = string[i];
  str[k] = '\0';
  separated[j++] = strdup(str);

  while (j < 9)
    separated[j++] = NULL;

  return separated;
}

/* Função Auxiliar
-- Input: (char*)Nome da coluna , (char*)Todas as colunas
-- Objetivo: Verificar se o nome da coluna existe em todas as colunas
-- Retorna: (int)Resultado */
int validColumn(char* column_name, char* columns) {
  char** separatedColumns = separateString(columns, ';');   // Separamos as colunas numa matriz com todas os nomes das colunas.
  int result = -1, j = 0;

  while (separatedColumns[j])
    if (strcmp(separatedColumns[j++], column_name) == 0) {  // Percorremos todas os nomes das colunas e comparamos a ver se encontramos
      result++;                                             // alguma que seja igual à que recebemos. Iteramos o resultado até encontrarmos
      break;                                                // a posição certa. Se encontramos acabamos logo o ciclo.
    }
    else
      result++;
      
  freeMatrix(separatedColumns, j);
  return result;
}

/* Função Auxiliar
-- Input: (char*)Operador
-- Objetivo: Verificar se o operador dado é válido
-- Retorna: (int)Resultado */
int validOP(char* operator) {
  enum OPERATOR result;
  
  if (strcmp(operator, "LT") == 0)
    result = LT;
  else
    if (strcmp(operator, "EQ") == 0)
      result = EQ;
    else
      if (strcmp(operator, "GT") == 0)
        result = GT;
      else
        result = -1;
  
  return result;
}

/* Função Auxiliar
-- Input: (char*)Entrada , (char*)Valor a comparar , (int)Maneira a comparar
-- Objetivo: Verificar se a entrada dada é válida para o operador e o valor dado
-- Retorna: (int)Resultado */
int filterAUX(char* entry, char* value, int operator) {
  int result = 1;

  if (operator == 0 && strcmp(entry, value) < 0)  // Operador = 0 representa LT(Lesser Than).
    result = 0;
  else
    if (operator == 1 && strcmp(entry, value) == 0) // Operador = 1 representa EQ(Equal).
      result = 0;
    else
      if (operator == 2 && strcmp(entry, value) > 0)  // Operador = 2 representa GT(Greater than).
        result = 0;
  
  return result;
}

/* Função
-- Input: (char**)Tabela , (int)Posição da coluna , (char*)Valor a comparar , (int)Operador
-- Objetivo: Construir uma tabela nova com todas as linhas que são válidas dependendo do operador e do valor a comparar
-- Retorna: (char**)Tabela */
char** filter(char** query, int column, char* value, int operator) {
  char** matrix = malloc(sizeof(char *) * 2);
  matrix[0] = strdup(query[0]);

  int i = 1, j = 1;
  while (query[i]) {
    char** separated = separateString(query[i], ';');           // Percorremos todas as linhas da tabela, separamos por ';' numa matriz
    int valid = filterAUX(separated[column], value, operator);  // Auxiliar e verificamos se a coluna é válida.
    
    if (valid == 0) {                                       // * Se sim, então adicionamos à matriz final e alocamos mais memória.
      matrix[j++] = strdup(query[i++]);
      matrix = realloc(matrix, (j+1) * sizeof(char *));
      matrix[j] = NULL;
    }
    else          // * Se não, apenas passamos para a próxima linha da tabela.
      i++;

    int f = 0;
    for (f; separated[f]; f++);
    freeMatrix(separated, f);
  }

  return matrix;
}

/* Função
-- Input: (char**)Tabela , (int*)Array com número das colunas pretendidas , (int)Número de elementos no array
-- Objetivo: Construir uma tabela com os dados das tabelas pretendidas
-- Retorna: (char**)Tabela */
char** proj(char** query, int* colsNumbers, int N) {
  char** matrix = malloc(sizeof(char *) * 1);

  int j = 0;
  for (int i = 0; query[i]; i++) {
    char** separated = separateString(query[i], ';');   // Percorremos a query linha a linha, separando o seu conteúdo pelo delimitador ';'.

    int k = 0;
    char* str = separated[colsNumbers[k++]];            // Criamos uma string que vai guardando as colunas pedidas
    while (k < N) {
      str = strcat(str, ";");
      str = strcat(str, separated[colsNumbers[k++]]);
    }
    str = strcat(str, "\0");

    if (str != NULL) {
      matrix[j++] = strdup(str);                        // Colocamos na matriz, essa mesma string.
      matrix = realloc(matrix, (j+1) * sizeof(char *));
    }
    matrix[j] = NULL;

    int f = 0;
    for (f; separated[f]; f++);
    freeMatrix(separated, f);
  }

  return matrix;
}

/* Função
-- Input: (char**)Tabela , (char)Separador , (char*)Caminho para o ficheiro
-- Objetivo: Imprimir num ficheiro .csv o conteúdo de uma matriz, em que cada coluna da matriz é separada pelo separador dado */
int toCSV(char** query, char delim, char* filepath) {
  int fileCSV = open(filepath, O_WRONLY | O_CREAT | O_TRUNC, 0755);   // Criação do ficheiro, para o caminho dado.
  if (fileCSV == -1)
    return -1;


  int bytes = 0;
  for (int i = 0; query[i]; i++) {
    char str[250];

    char** separated = separateString(query[i], ';');   // Separamos a string de uma linha da matriz dada pelo separador dado.
    int j = 0, k = 0, w = 0;
    while (separated[j]) {              // Percorremos a matriz com a linha separada. Juntamos char a char numa string.
      if (separated[j][k])              // Quando uma linha da matriz acaba, adicionamos o separador dado.
        str[w++] = separated[j][k++];
      else {
        str[w++] = delim;
        j++;
        k = 0;
      }
    }
    str[--w] = '\n';
    str[++w] = '\0';
    
    if ((bytes = write(fileCSV, str, strlen(str))) != (int)strlen(str)) {
      showMessage("Erro na escrita para o ficheiro");    // Escrevemos no ficheiro a string resultante, verificando sempre se escreveu toda
      int f = 0;
      for (f; separated[f]; f++);
      freeMatrix(separated, f);                          // a sua length. * Se não escrever, mostramos uma imagem que ajuda o utilizador a
      break;                                             // perceber o erro que aconteceu.
    }
    int f = 0;
    for (f; separated[f]; f++);
    freeMatrix(separated, f);
  }

  close(fileCSV);
  return 0;
}

/* Função
-- Input: (FILE*)Ficheiro , (char)Separador
-- Objetivo: Atribuir a uma matriz o conteúdo de um ficheiro .csv
-- Retorna: (char**)Matriz */
char** fromCSV(FILE* fileCSV, char delim) {
  char** matrix = malloc(sizeof(char *) * 1);

  char line[300];
  int i = 0, j = 0;
  while (fgets(line, 300, fileCSV) != NULL) {               // Lemos linha a linha de um ficheiro até ao fim.
    j = 0;
    char** separated = separateString(line, delim);         // Separamos essa linha pelo seu separador.
    char* str = malloc(sizeof(char) * 300);
    str = strcpy(str, separated[j++]);
    while (separated[j] && (strlen(str) != strlen(line))) { // Percorremos a matriz com a linha já separada em strings.
      strcat(str, ";");                                     // Juntamos numa string a matriz linha a linha com o separador default ';'.
      strcat(str, separated[j++]);
    }
    str[strlen(str)-1] = '\0';    // No fim adicionamos o '\0' numa posição onde está um separador a mais.
    
    matrix[i++] = strdup(str);
    matrix = realloc(matrix, (i+1) * sizeof(char *));

    int f = 0;
    for (f; separated[f]; f++);
    freeMatrix(separated, f);
    free(str);
  }
  matrix[i] = NULL;

  return matrix;
}

/* Função Auxiliar
-- Input: (char*)Função
-- Objetivo: Atribuir um inteiro dependendo da string recebida
-- Retorna: (int)Query */
int transformFunction(char* function) {
  if (strcmp(function, "show") == 0)
    return 1;
  if (strcmp(function, "businesses_started_by_letter") == 0)
    return 2;
  if (strcmp(function, "business_info") == 0)
    return 3;
  if (strcmp(function, "businesses_reviewed") == 0)
    return 4;
  if (strcmp(function, "businesses_with_stars_and_city") == 0)
    return 5;
  if (strcmp(function, "top_businesses_by_city") == 0)
    return 6;
  if (strcmp(function, "international_users") == 0)
    return 7;
  if (strcmp(function, "top_businesses_with_category") == 0)
    return 8;
  if (strcmp(function, "reviews_with_word") == 0)
    return 9;
  if (strcmp(function, "toCSV") == 0)
    return 10;
  if (strcmp(function, "fromCSV") == 0)
    return 11;
  if (strcmp(function, "filter") == 0)
    return 12;
  if (strcmp(function, "proj") == 0)
    return 13;
  if (strcmp(function, "x") == 0 || strcmp(function, "y") == 0)
    return 14;
  if (strcmp(function, "quit") == 0)
    return 15;
  
  return 0;
}

/* Função Auxiliar
-- Input: (char*)Input , (int)Número de inputs
-- Objetivo: Criar uma matriz com os inputs recebidos
-- Retorna: (char**)Matriz */
char** getArgs_from_input(char* input, int argc) {
  char** args = malloc(sizeof(char *) * argc);
  char* arg;
  int w, j, k;

  for (int i = 0; i < argc; i++) {
    args[i] = malloc(sizeof(char) * 50);

    if (i == 0)                       // Guardamos numa string Auxiliar o conteúdo até à ',' ou até ao ')'.
      arg = strtok(input, ",");
    else
      if (i+1 != argc)
        arg = strtok(NULL, ",");
      else
        arg = strtok(NULL, ")");

    j = 0;
    k = 0;
    while (arg[k] == '(' || arg[k] == ' ' || arg[k] == '\"') {
      j++;                      // Incrementamos as variáveis 'j' e 'k' para saber até que posição podemos avançar até encontrarmos um char
      k++;                      // que nos interesse.
    }

    size_t argLEN = strlen(arg);
    arg[argLEN] = '\0';

    w = 0;
    while (arg[j] != ')' && arg[j] != '\0' && arg[j] != '\"')
      args[i][w++] = arg[j++];          // Adicionamos a string os chars que são diferentes dos dados e no fim terminamos a string com '\0'.
    args[i][w] = '\0';
  }

  return args;
}

/* Função Auxiliar
Input: (char*)String , (int)Coluna
Objetivo: Devolver a String da coluna pedida
Retorna: (char*)String*/
char* getIndexValue(char* str, int y) {
  char* strDUP = strdup(str);
  char* strF = strtok(strDUP, ";");   // Guardamos numa variável a string até ao primeiro ';'.
  int j = 0;
  while (j < y) {                     // Guardamos sempre na mesma variável a string até ao próximo ';', até a variável 'j' igualar
    strF = strtok(NULL, ";");         // a variável dada.
    if (strDUP == NULL)
      strF = strtok(NULL, "\r\n");
    j++;
  }

  free(strDUP);
  if (strF != NULL)     // Se no fim a variável não for nula, retornamos a mesma.
    return strF;        // Já se for nula, mostramos uma mensagem de erro ao utilizador e retornamos o '\0'.
  else {
    showMessage("Indexação inválida\n");
    return '\0';
  }
}


/********** MAIN **********/
int main(int argc, char* argv[]) {
  showMessage("Deseja ler os utilizadores com ou sem amigos? (0 - COM , 1 - SEM)");
  int userOption, flagFirstTime = 0;
  if (scanf("%d", &userOption) == EOF) {
    showMessage("Introduziu um número inválido");
    return 1;
  }
  if (userOption != 0 && userOption != 1) {
    showMessage("Introduziu um número inválido");
    return 1;
  }

	SGR sgr = init_sgr();   // Inicialização da estrutura 'sgr'.
  showMessage("A ler base de dados...");
  int loadCode = load_sgr(sgr, "input_files/users_full.csv", "input_files/business_full.csv", "input_files/reviews_1M.csv", userOption);
  
  if (loadCode != 0) {    // Verificação da leitura correta de todos os ficheiros.
    showMessage("Erro ao carregar a base de dados");
    return 1;
  }
                              // Iniciação das duas tabelas para a realização das querys. Quando id == 'x' é utilizada a queryX e quando
  showMenu();                 // id == 'y' é utilizada a queryY. Iniciamos também a queryF para a filtragem e a queryT para a projeção
  char **queryX = NULL, **queryY = NULL, **queryF = NULL, **queryT = NULL, id = '\0', *z = NULL, *line = NULL;      // de colunas.
  int exit = 0, query = 0, xValue = 0, yValue = 0, fValue = 0, tValue = 0;  // As variáveis xValue, yValue e fValue controlam se as
  while (!exit) {                                                           // tabelas foram utilizadas uu não, para um possível free
    query = 0;                                                              // no final do programa.

    char *function = NULL;
    FILE* fileCSV = NULL;
    char input[100];
    size_t len = 0;
    if (getline(&line, &len, stdin) != -1) {
      if (line[0] == 'x')   // Verificação da variável que nos é dada. Apenas aceitamos 'x' e 'y' para guardar as querys.
        id = 'x';           // Aceitamos 'z' para guardar a indexação, 't' para a projeção de colunas e 'f' para a filtragem.
      else
        if (line[0] == 'y')
          id = 'y';
        else
          if (line[0] == 'z')
            id = 'z';
          else
            if (line[0] == 't')
              id = 't';
            else
              if (line[0] == 'f')
                id = 'f';
      char* freeLine = line;
      if (line[1] == ' ')
        line += 4;

      char* lineCPY = strdup(line);                 // Criamos uma cópia da string, para podermos utilizar a função 'strchr' sem alterar
      lineCPY[strlen(lineCPY)-1] = '\0';            // a string original.

      char* inputCheck = strchr(lineCPY, (const char) '(');  // Pois essa função altera o apontador do input que recebe. Verificamos assim
      if (inputCheck != NULL) {                              // se existe ou não algum char '('. * Se sim, guardamos na string function a
        function = strdup(strtok(line, "("));                // função que queremos utilizar e no input tudo depois da função até ao ';'.
        strcpy(input, inputCheck);
        input[strlen(inputCheck)-1] = '\0';

        query = transformFunction(function);  // Finalmente, obtemos o número da respetiva função, para entrar no case da switch certo.
      }
      else {
        inputCheck = strchr(lineCPY, (const char) '[');      // * Se não, verificamos se existe ou não algum char '[' (caso da indexação).
        if (inputCheck != NULL) {                            // Se existir, a string function guarda a função e o input guarda tudo depois
          function = strdup(strtok(line, "["));              // da função até ao ';'.
          strcpy(input, inputCheck);
          input[strlen(inputCheck)-1] = '\0';

          query = transformFunction(function);  // Obtemos aqui o número da respetiva função, para entrar no case da switch certo.
        }
        else                                    // Caso não haja nem '(' nem '[' na string, procuramos o número que represente a função que
          query = transformFunction(lineCPY);   // nos é chamada. Se não existir, entra no default na switch.
      }
      free(lineCPY);
      free(freeLine);
    }
    else
      query = 0;

    switch (query) {
      case 1: // Show
        if (input[1] == 'x' && xValue != 0 && queryX) {  // Identificação do input para saber qual das tabelas é para dar show.
          if (queryX[2] != NULL) {
            paginacao(queryX, 1);
            printf("Show finished!\n\n");
            showMenu();
          }
          else
            if (queryX[1] != NULL) {
            showValue(queryX[1]);
            printf("Show finished!\n\n");
            }
            else
              showMessage("Nothing to show!\n");
        }
        else
          if (input[1] == 'y' && yValue != 0 && queryY) {
            if (queryY[2] != NULL) {
              paginacao(queryY, 1);
              printf("Show finished!\n\n");
              showMenu();
            }
            else
              if (queryY[1] != NULL) {
              showValue(queryY[1]);
              printf("Show finished!\n\n");
              }
              else
                showMessage("Nothing to show!\n");
          }
          else
            if (input[1] == 'f' && fValue != 0 && queryF) {
              if (queryF[2] != NULL) {
                paginacao(queryF, 1);
                printf("Show finished!\n\n");
                showMenu();
              }
              else
                if (queryF[1] != NULL) {
                  showValue(queryF[1]);
                  printf("Show finished!\n\n");
                }
                else
                  showMessage("Nothing to show!\n");
            }
            else
              if (input[1] == 't' && tValue != 0 && queryT) {
                if (queryT[2] != NULL) {
                  paginacao(queryT, 1);
                  printf("Show finished!\n\n");
                  showMenu();
                }
                else
                  if (queryT[1] != NULL) {
                  showValue(queryT[1]);
                  printf("Show finished!\n\n");
                  }
                  else
                    showMessage("Nothing to show!\n");
              }
              else
                if (input[1] == 'z') {
                  showValue(z);
                  printf("Show finished!\n\n");
                }
                else
                  showMessage("Inseriu um argumento inválido ou a tabela que escolheu não foi iniciada");

        break;
      case 2: ; // Query 2                          // Explicação da execução de uma função. Todas as querys estão escritas da mesma forma,
        char** argsQ2 = NULL;                       // por isso esta explicação serve até ao case 9. Inicialização da matriz para facilitar
        argsQ2 = getArgs_from_input(input, 2);      // o input na função que queremos. Ficamos então com, por exemplo, argsQ2[0] = sgr
                                                    // e argsQ2[1] = 'T'.
        if (strcmp(argsQ2[0], "sgr") == 0)                            // Verificamos se o 1º input é sgr, se sim prosseguimos, se não
          if (id == 'x') {                                            // apresentamos uma mensagem que ajuda o utilizador a perceber o seu
            xValue++;                                                 // erro. Identificamos qual das tabelas é para utilizar (x ou y),
            if (xValue > 1) {                                         // aumentamos o seu respetivo value e executamos a função. Se for dada
              int f = 0;
              for (f; queryX[f]; f++);
              freeMatrix(queryX, f-1);                                // uma variável que o não aceitamos, apresentamos uma mensagem que
            }
            queryX = business_started_by_letter(sgr, argsQ2[1][1]);   // ajuda o utilizador a perceber o que o nosso programa aceita.
          }
          else
            if (id == 'y') {
              yValue++;
              if (yValue > 1) {
                int f = 0;
                for (f; queryY[f]; f++);
                freeMatrix(queryY, f-1); 
              }
              queryY = business_started_by_letter(sgr, argsQ2[1][1]);
            }
            else
              showMessage("Inseriu uma variável inválida");
        else
          showMessage("Inseriu um argumento inválido");

        freeMatrix(argsQ2, 2); // Libertação da memória alocada para a matriz dos argumentos.
        printf("Query finished!\n\n");
        break;
      case 3: ; // Query 3
        char** argsQ3 = NULL;
        argsQ3 = getArgs_from_input(input, 2);

        if (strcmp(argsQ3[0], "sgr") == 0)
          if (id == 'x') {
            xValue++;
            if (xValue > 1) {
              int f = 0;
              for (f; queryX[f]; f++);
              freeMatrix(queryX, f-1); 
            }
            queryX = business_info(sgr, argsQ3[1]);
          }
          else
            if (id == 'y') {
              yValue++;
              if (yValue > 1) {
                int f = 0;
                for (f; queryY[f]; f++);
                freeMatrix(queryY, f-1); 
              }
              queryY = business_info(sgr, argsQ3[1]);
            }
            else
              showMessage("Inseriu uma variável inválida");
        else
          showMessage("Inseriu um argumento inválido");
        
        freeMatrix(argsQ3, 2);
        printf("Query finished!\n\n");
        break;
      case 4: ; // Query 4
        char** argsQ4 = NULL;
        argsQ4 = getArgs_from_input(input, 2);

        if (strcmp(argsQ4[0], "sgr") == 0)
          if (id == 'x') {
            xValue++;
            if (xValue > 1) {
              int f = 0;
              for (f; queryX[f]; f++);
              freeMatrix(queryX, f-1); 
            }
            queryX = businesses_reviewed(sgr, argsQ4[1]);
          }
          else
            if (id == 'y') {
              yValue++;
              if (yValue > 1) {
                int f = 0;
                for (f; queryY[f]; f++);
                freeMatrix(queryY, f-1); 
              }
              queryY = businesses_reviewed(sgr, argsQ4[1]);
            }
            else
              showMessage("Inseriu uma variável inválida");
        else
          showMessage("Inseriu um argumento inválido");
        
        freeMatrix(argsQ4, 2);
        printf("Query finished!\n\n");
        break;
      case 5: ; // Query 5
        char** argsQ5 = NULL;
        argsQ5 = getArgs_from_input(input, 3);

        if (strcmp(argsQ5[0], "sgr") == 0)
          if (id == 'x') {
            xValue++;
            if (xValue > 1) {
              int f = 0;
              for (f; queryX[f]; f++);
              freeMatrix(queryX, f-1); 
            }
            queryX = businesses_with_stars_and_city(sgr, atof(argsQ5[1]), argsQ5[2]);
          }
          else
            if (id == 'y') {
              yValue++;
              if (yValue > 1) {
                int f = 0;
                for (f; queryY[f]; f++);
                freeMatrix(queryY, f-1); 
              }
              queryY = businesses_with_stars_and_city(sgr, atof(argsQ5[1]), argsQ5[2]);
            }
            else
              showMessage("Inseriu uma variável inválida");
        else
          showMessage("Inseriu um argumento inválido");
          
        freeMatrix(argsQ5, 3);
        printf("Query finished!\n\n");
        break;
      case 6: ; // Query 6
        char** argsQ6 = NULL;
        argsQ6 = getArgs_from_input(input, 2);

        if (strcmp(argsQ6[0], "sgr") == 0)
          if (id == 'x') {
            xValue++;
            if (xValue > 1) {
              int f = 0;
              for (f; queryX[f]; f++);
              freeMatrix(queryX, f-1); 
            }
            queryX = top_businesses_by_city(sgr, atoi(argsQ6[1]));
          }
          else
            if (id == 'y') {
              yValue++;
              if (yValue > 1) {
                int f = 0;
                for (f; queryY[f]; f++);
                freeMatrix(queryY, f-1); 
              }
              queryY = top_businesses_by_city(sgr, atoi(argsQ6[1]));
            }
            else
              showMessage("Inseriu uma variável inválida");
        else
          showMessage("Inseriu um argumento inválido");
          
        freeMatrix(argsQ6, 2);
        printf("Query finished!\n\n");
        break;
      case 7: ; // Query 7
        char** argsQ7 = NULL;
        argsQ7 = getArgs_from_input(input, 1);

        if (strcmp(argsQ7[0], "sgr") == 0)
          if (id == 'x') {
            xValue++;
            if (xValue > 1) {
              int f = 0;
              for (f; queryX[f]; f++);
              freeMatrix(queryX, f-1); 
            }
            queryX = international_users(sgr);
          }
          else
            if (id == 'y') {
              yValue++;
              if (yValue > 1) {
                int f = 0;
                for (f; queryY[f]; f++);
                freeMatrix(queryY, f-1); 
              }
              queryY = international_users(sgr);
            }
            else
              showMessage("Inseriu uma variável inválida");
        else
          showMessage("Inseriu um argumento inválido");
          
        freeMatrix(argsQ7, 1);
        printf("Query finished!\n\n");
        break;
      case 8: ; // Query 8
        char** argsQ8 = NULL;
        argsQ8 = getArgs_from_input(input, 3);

        if (strcmp(argsQ8[0], "sgr") == 0)
          if (id == 'x') {
            xValue++;
            if (xValue > 1) {
              int f = 0;
              for (f; queryX[f]; f++);
              freeMatrix(queryX, f-1); 
            }
            queryX = top_businesses_with_category(sgr, atoi(argsQ8[1]), argsQ8[2]);
          }
          else
            if (id == 'y') {
              yValue++;
              if (yValue > 1) {
                int f = 0;
                for (f; queryY[f]; f++);
                freeMatrix(queryY, f-1); 
              }
              queryY = top_businesses_with_category(sgr, atoi(argsQ8[1]), argsQ8[2]);
            }
            else
              showMessage("Inseriu uma variável inválida");
        else
          showMessage("Inseriu um argumento inválido");
          
        freeMatrix(argsQ8, 3);
        printf("Query finished!\n\n");
        break;
      case 9: ; // Query 9
        char** argsQ9 = NULL;
        argsQ9 = getArgs_from_input(input, 2);

        if (strcmp(argsQ9[0], "sgr") == 0)
          if (id == 'x') {
            xValue++;
            if (xValue > 1) {
              int f = 0;
              for (f; queryX[f]; f++);
              freeMatrix(queryX, f-1); 
            }
            queryX = reviews_with_word(sgr, argsQ9[1]);
          }
          else
            if (id == 'y') {
              yValue++;
              if (yValue > 1) {
                int f = 0;
                for (f; queryY[f]; f++);
                freeMatrix(queryY, f-1); 
              }
              queryY = reviews_with_word(sgr, argsQ9[1]);
            }
            else
              showMessage("Inseriu uma variável inválida");
        else
          showMessage("Inseriu um argumento inválido");
          
        freeMatrix(argsQ9, 2);
        printf("Query finished!\n\n");
        break;
      case 10:  ; // Escrita para csv
        char** argsQ10 = NULL;
        argsQ10 = getArgs_from_input(input, 3);

        int result = 0;
        if (strcmp(argsQ10[0], "x") == 0 && xValue != 0)   // Verificamos se é para efetuar a escrita da tabela 'x' ou 'y'.
          result = toCSV(queryX, argsQ10[1][1], argsQ10[2]); 
        else
          if (strcmp(argsQ10[0], "y") == 0 && yValue != 0)
            result = toCSV(queryY, argsQ10[1][1], argsQ10[2]);
          else                              // Mensagem de erro que aparece para avisar o utilizador que introduziu um argumento inválido.
            showMessage("Inseriu um argumento inválido ou a tabela que escolheu não foi iniciada");

        if (result == -1)
          showMessage("Abertura de ficheiro inválida");
          
        freeMatrix(argsQ10, 3);
        printf("ToCSV finished!\n\n");
        break;
      case 11:  ; // Leitura de csv
        char** argsQ11 = NULL;
        argsQ11 = getArgs_from_input(input, 2);

        fileCSV = fopen(argsQ11[0], "r");   // Abrimos aqui o ficheiro. Verificamos se foi corretamente aberto.
        if (fileCSV == NULL)                             // * Se não, apresentamos uma mensagem de erro para ajudar o utilizador.
          showMessage("Erro ao abrir o ficheiro pretendido");
        else {                                           // * Se sim, executamos a função pedida após a verificação em qual das tabelas é
          if (id == 'x') {                               // para guardar a tabela pedida.
            xValue++;
            if (xValue > 1) {
              int f = 0;
              for (f; queryX[f]; f++);
              freeMatrix(queryX, f-1); 
            }
            queryX = fromCSV(fileCSV, argsQ11[1][1]);
          }
          else
            if (id == 'y') {
              yValue++;
              if (yValue > 1) {
                int f = 0;
                for (f; queryY[f]; f++);
                freeMatrix(queryY, f-1); 
              }
              queryY = fromCSV(fileCSV, argsQ11[1][1]);
            }
            else
              showMessage("Inseriu uma variável inválida");
          fclose(fileCSV);    // Fecho do ficheiro.
          fileCSV = NULL;
        }

        freeMatrix(argsQ11, 2);
        printf("FromCSV finished!\n\n");
        break;
      case 12:  // Filtragem de resultados
        if (id != 'f') {
          showMessage("Inseriu uma variável inválida");
          break;
        }

        char** argsQ12 = NULL;
        argsQ12 = getArgs_from_input(input, 4);
        
        int column, operator;
        if (argsQ12[0][0] == 'x' && xValue != 0)    // Verificamos se a tabela pedida é válida ou não.
          if ((column = validColumn(argsQ12[1], queryX[0])) != -1 && (operator = validOP(argsQ12[3])) != -1) {
            fValue++;
            if (fValue > 1) {
              int f = 0;
              for (f; queryF[f]; f++);
              freeMatrix(queryF, f-1); 
            }
            queryF = filter(queryX, column, argsQ12[2], operator);    // Verificamos também se a coluna dada e o operador são válidos.
          }                                                           // Depois apenas executamos a função 'filter'.
          else
            showMessage("Inseriu um argumento inválido ou a tabela que escolheu não foi iniciada");
        else
          if (argsQ12[0][0] == 'y' && yValue != 0)
            if ((column = validColumn(argsQ12[1], queryY[0])) != -1 && (operator = validOP(argsQ12[3])) != -1) {
              fValue++;
              if (fValue > 1) {
                int f = 0;
                for (f; queryF[f]; f++);
                freeMatrix(queryF, f-1); 
              }
              queryF = filter(queryY, column, argsQ12[2], operator);
            }
            else
              showMessage("Inseriu um argumento inválido ou a tabela que escolheu não foi iniciada");
          else
            showMessage("Inseriu um argumento inválido");

        freeMatrix(argsQ12, 4);
        printf("Filter finished!\n\n");
        break;
      case 13:  // Projeção de colunas
        if (id != 't') {
          showMessage("Inseriu uma variável inválida");
          break;
        }

        char** argsQ13 = NULL;
        argsQ13 = getArgs_from_input(input, 2);
        char** cols = NULL;                           // Separamos as colunas dadas numa matriz
        cols = separateString(argsQ13[1], ',');

        int columnP, colsNumbers[9], i = 0;
        if (argsQ13[0][0] == 'x' && xValue != 0) {
          tValue++;
          for (i = 0; cols[i]; i++)
            if ((columnP = validColumn(cols[i], queryX[0])) != -1)    // Se as colunas forem válidas prosseguimos
              colsNumbers[i] = columnP;
          queryT = proj(queryX, colsNumbers, i);
        }
        else
          if (argsQ13[0][0] == 'y' && yValue != 0) {
            tValue++;
            for (i = 0; cols[i]; i++)
              if ((columnP = validColumn(cols[i], queryX[0])) != -1)
                colsNumbers[i] = columnP;
            queryT = proj(queryY, colsNumbers, i);
          }
          else
            showMessage("Inseriu um argumento inválido");

        freeMatrix(cols, i);
        freeMatrix(argsQ13, 2);
        printf("Columns projection finished!\n\n");
        break;
      case 14:  ; // Indexação
        if (id != 'z') {
          showMessage("Inseriu uma variável inválida");
          break;
        }

        if (function[0] == 'x' && xValue == 0) {      // Verificamos aqui se a indexação da tabela pedida é valida. Ou seja, se a tabela já
          showMessage("Tabela 'x' não iniciada\n");   // foi utilizada para alguma função ou não. * Se sim, prosseguimos com a indexação.
          break;                                      // * Se não, apresentamos uma mensagem de erro e voltamos para o menu.
        }
        if (function[0] == 'y' && yValue == 0) {
          showMessage("Tabela 'y' não iniciada\n");
          break;
        }

        char strX[6], strY[2];  // Inicialização das variáveis que vão guardar os números do input para a indexação.
        int flag = 0, j = 0, k = 0;     // A variável Flag é incrementada quando os números entre os primeiros [] acabaram.
        for (int i = 1; input[i]; i++)  // Quando incrementada, os números vão para a strY em vez de strX.
          if (isNumberC(input[i]) == 0)   // Verificamos se o char no input é número ou não.
            if (flag == 0)                // * Se sim e se a Flag for igual a 0, então significa que estamos nos primeiros [] e queremos
              strX[j++] = input[i];       // pôr o número na strX.
            else                          // * Se sim e se a Flag for igual a 1, então significa que estamos nos segundos [] e queremos
              strY[k++] = input[i];       // pôr o número na strY.
          else
            if (input[i] == ']')          // Incrementação da variável Flag, quando detetamos que acabaram os primeiros [].
              flag++;
        strX[j] = '\0';   // Como as strings têm um tamanho fixo, no final acrescentamos um '\0' para darmos as strings como finalizadas.
        strY[k] = '\0';

        if (function[0] == 'x') {   // Verificação de qual das tabelas é que é pedida a indexação (queryX ou queryY):
          int max;
          for (max = 0; queryX[max]; max++);  // Obtemos aqui o número de entradas nas tabelas, de forma a que é possível testarmos se os
          if (atoi(strX) >= max)              // números que nós são dados são possíveis ou não.
            showMessage("Indexação inválida\n");  // * Se não forem possíveis, apresentamos uma mensagem de erro.
          else
            z = getIndexValue(queryX[atoi(strX)], atoi(strY));   // * Se forem possíveis, executamos a função que vai guardar na variável
        }                                                         // 'z' a string resultante da indexação pretendida.
        else
          if (function[0] == 'y') {
            int max;
            for (max = 0; queryY[max]; max++);
            if (atoi(strX) >= max)
              showMessage("Indexação inválida\n");
            else
              z = getIndexValue(queryY[atoi(strX)], atoi(strY));
          }

        printf("Indexation finished!\n\n");
        break;
      case 15:  // Quit
        exit = 1;
        
        if (xValue != 0 && queryX) {
          int f = 0;
          for (f; queryX[f]; f++);
          freeMatrix(queryX, f); 
        }       // Verificamos se a tabela queryX foi utilizada em alguma função, se sim libertamos a sua memória alocada   // se não prosseguimos.
        if (yValue != 0 && queryY) {       // Verificamos se a tabela queryY foi utilizada em alguma função, se sim libertamos a sua memória alocada
          int f = 0;
          for (f; queryY[f]; f++);
          freeMatrix(queryY, f-1);    // se não prosseguimos.
        }
        if (fValue != 0 && queryF) {            // Verificamos se a tabela queryF foi utilizada em alguma função, se sim libertamos a sua memória alocada
          int f = 0;
          for (f; queryF[f]; f++);
          freeMatrix(queryF, f-1);    // se não prosseguimos.
        }
        if (tValue != 0 && queryT) {        // Verificamos se a tabela queryT foi utilizada em alguma função, se sim libertamos a sua memória alocada
          int f = 0;
          for (f; queryT[f]; f++);
          freeMatrix(queryT, f-1);    // se não prosseguimos.
        }

        showExitProgram();  // Apresentação do menu de encerramento do programa.
        break;
      default:  // Se não for introduzido nenhuma função existente no nosso programa, apresentamos uma mensagem de erro.
        if (flagFirstTime == 0)
          flagFirstTime++;
        else
          showMessage("Inseriu uma função inválida.");

        break;
    }
    free(function);
  }

  free_sgr(sgr);  // Libertação da memória alocada para a estrutura iniciada no início do programa 'sgr'.
  return 0;
}

// TODO: Suponho que grande parte dos frees estejam corretos ; Arranjar show de querys que só devolvem 1 elemento