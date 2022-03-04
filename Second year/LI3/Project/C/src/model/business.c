#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#include "../../includes/model/business.h"

//Estrutura dos Negócios
struct business {
    char* business_id;
    char* name;
    char* city;
    char* state;
    char** categories;
};

//Constrói um Negócio a partir dos argumentos
BUSINESS initBusiness(char* biz_id, char* biz_name, char* biz_city, char* biz_state, char** biz_categories) {
    BUSINESS biz = malloc(sizeof(struct business));

    biz->business_id = strdup(biz_id);
    biz->name = strdup(biz_name);
    biz->city = strdup(biz_city);
    biz->state = strdup(biz_state);
    biz->categories = malloc(sizeof(char*));
    if(biz_categories){
        int size = 2;
        int i;
        for(i = 0; biz_categories[i]; i++, size++){
            biz->categories[i] = strdup(biz_categories[i]);
            biz->categories = realloc(biz->categories, sizeof(char*)*size);
        }
        biz->categories[i] = NULL;
        free(biz_categories);
    }
    else{
        free(biz->categories);
        biz->categories = NULL;
    }

    return biz; 
}

//Recebe uma linha do ficheiro e constrói um Negócio a partir dela
BUSINESS initBusinessByLine(char * line) {
    char* b_id = strtok(line, ";");
    char* b_name = strtok(NULL, ";");
    char verifier = (b_name-1)[0]; //Verifica se existem dois ";;", i.e, business_id;;name
    char* b_city = strtok(NULL, ";");
    char verifier1 = (b_city-1)[0];
    char* b_state = strtok(NULL, ";");
    
    int i = 0, size = 1;
    char** b_categories = malloc(sizeof(char*));
    while((b_categories[i] = strtok(NULL, ",")) != NULL) {
        i++;
        size++;
        b_categories = realloc(b_categories, sizeof(char*) * (size));
    }
    if (i != 0)
        b_categories[i] = NULL;
    else{
        free(b_categories);
        b_categories = NULL;
    }

    //Verificação de dados do ficheiro  
    if(b_id == NULL || b_name == NULL || b_city == NULL || b_state == NULL || verifier == ';' || verifier1 == ';'){
        if(b_categories != NULL)
            free(b_categories);
        return NULL;
    }
    return initBusiness(b_id, b_name, b_city, b_state, b_categories);
}


//Getters
char* b_getBusinessId(BUSINESS b) {
    return strdup(b->business_id);
}

char* b_getBusinessName(BUSINESS b) {
    return strdup(b->name);
}

char* b_getBusinessCity(BUSINESS b) {
    return strdup(b->city);
}

char* b_getBusinessState(BUSINESS b) {
    return strdup(b->state);
}

char** b_getBusinessCategories(BUSINESS b) {
    char** categories = b->categories;
    return categories;
}

void b_getBusinessInfo(BUSINESS b) {
    printf("Business_ID: %s\nName: %s\nCity: %s\nState: %s\n", b->business_id, b->name, b->city, b->state);
    printf("Categories:\n");
    if(b->categories)
        for (int i = 0; b->categories[i]; i++)
            printf("\t%s\n", b->categories[i]);

    else printf("No categories\n");        
}

//Clone do Negócio
BUSINESS cloneBusiness(BUSINESS b) {
    BUSINESS new_b = malloc(sizeof(struct business));
    new_b->business_id = strdup(b->business_id);
    new_b->name = strdup(b->name);
    new_b->city = strdup(b->city);
    new_b->state = strdup(b->state);

    int size = 1;
    int i=0;
    if(b->categories!=NULL){
        new_b->categories = malloc(sizeof(char*));
        for(i; b->categories[i];i++, size++){
            new_b->categories[i] = strdup(b->categories[i]);
            new_b->categories = realloc(new_b->categories, sizeof(char*) * (size+1));
        }
        new_b->categories[i] = NULL;
    }
    else{
        new_b->categories = NULL;
    }

    return new_b;
}

//Destroyers/Free
//para destruir as categorias temos de as percorrer uma a uma, logo temos de ter uma função que retorna o total de categorias existentes
void destroyBusiness(BUSINESS b) {
    free(b->business_id);
    free(b->name);
    free(b->city);
    free(b->state);
    if(b->categories){
        freeCategories(b->categories); 
    }
    free(b);
}

void freeCategories(char** biz_categories){
    for(int i=0; biz_categories[i]!=NULL; i++){
        free(biz_categories[i]);
    }
    free(biz_categories);
}

void freeCategories2(BUSINESS b){
    if(b->categories)
        free(b->categories);
}

void destroyBusinessKey(char* b) {
    free(b);
}