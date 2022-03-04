#ifndef BUSINESS_H
#define BUSINESS_H

typedef struct business* BUSINESS; //Estrutura dos Negócios

BUSINESS initBusiness (char* biz_id, char* biz_name, char* biz_city, char* biz_state, char** biz_categories); //Constrói um Negócio a partir dos argumentos

BUSINESS initBusinessByLine (char* line); //Recebe uma linha do ficheiro e constrói um Negócio a partir dela

//Getters
char* b_getBusinessId (BUSINESS b);
char* b_getBusinessName (BUSINESS b);
char* b_getBusinessCity (BUSINESS b);
char* b_getBusinessState (BUSINESS b);
char** b_getBusinessCategories (BUSINESS b);

void b_getBusinessInfo(BUSINESS b);

BUSINESS cloneBusiness (BUSINESS b); //Clone do Negócio

//Destroyers/Frees
void destroyBusiness(BUSINESS b);
void freeCategories(char** biz_categories);
void freeCategories2(BUSINESS b);
void destroyBusinessKey(char* b);


#endif