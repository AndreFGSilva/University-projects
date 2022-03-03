#include <stdio.h>
#include "estado.h"

int menuprincipal () {
    int i;
    printf("\n\tBEM VINDO AO REVERSI\t\n\n");
    printf("1)\t1 JOGADOR\n");
    printf("2)\t2 JOGADORES\n");
    printf("3)\tSAIR\n"
           "3\n");
    scanf("%d", &i);
    switch (i) {
        case 1:
            printf("\nInício de jogo\n");
//            jogo1();
            break;
        case 2:
            printf("\nInício de jogo\n");
//            jogo2();
            break;
        case 3:
            printf("\nO jogo vai terminar.\n");
            return 0;
        default:
            printf("\nInseriu uma opção inválida.\n");
            menuprincipal();
            break;
    }
    return 0;
}

// exemplo de uma função para imprimir o estado (Tabuleiro)
void printa(ESTADO e)
{
    char c = ' ';


    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            switch (e.grelha[i][j]) {
                case VALOR_O: {
                    c = 'O';
                    break;
                }
                case VALOR_X: {
                    c = 'X';
                    break;
                }
                case VAZIA: {
                    c = '-';
                    break;
                }
            }
            printf("%c ", c);

        }
        printf("\n");
    }

}

