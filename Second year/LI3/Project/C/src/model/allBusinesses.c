#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#include "../../includes/model/allBusinesses.h"
#include "../../includes/model/allReviews.h"


#define BUFF_SIZE 5500

//Estrutura do Catálogo de Negócios
struct businesses {
    GHashTable *bizs;
};

//Inicializador de Negócios
BIZS initBusinesses() {
    BIZS businesses = malloc(sizeof(struct businesses));
    businesses->bizs = g_hash_table_new_full(g_str_hash, g_str_equal, (GDestroyNotify)destroyBusinessKey, (GDestroyNotify)destroyBusiness);
    
    return businesses;
}

//Carregamento de ficheiro para estrutura de Negócios
BIZS loadBusinessesFromFile(BIZS b, char* filename) {
    int i = 0;
    FILE* f = fopen(filename, "r");
    if (f == NULL)
        return NULL;

    char buffer[BUFF_SIZE];
    while(fgets(buffer, BUFF_SIZE, f)) {
        char* businessLine = strtok((buffer), "\r\n");
        BUSINESS new_business = initBusinessByLine(businessLine);
        if(new_business != NULL){
            g_hash_table_insert(b->bizs, b_getBusinessId(new_business), new_business);
        }
    }
    printf("Business - Done! Size: %d\n", g_hash_table_size(b->bizs));
    fclose(f);
    return b;
}

//Free da estrutura de Negócios
void destroyBusinesses(BIZS b) {
    g_hash_table_destroy(b->bizs);
    free(b);
}

//Retorna a HashTable da estrutura Negócios
GHashTable* b_getTable(BIZS b){
    GHashTable* bt;
    bt = b->bizs;
    return bt;
}

//Query 2 AUX
//Dada uma letra, itera a hashtable procurando na key se a primeira letra de cada key
//é igual à letra recebida como argumento
char** searchBusinessLetter(BIZS b, char letter) {
    GHashTableIter iter;
    gpointer key, value;

    //Array de Strings dinâmica que armazena todos os businesses que começam pela letra recebida como argumento
    char** matrix = malloc(sizeof(char *) * 2);
    matrix[0] = malloc(sizeof(char *) * 20);
    sprintf(matrix[0], "%s", "business_id;name");

    //Iterar a hashtable
    int i = 1; //Controlador do tamanha do array de strings dinâmico e iterador da variável matrix
    g_hash_table_iter_init(&iter, b->bizs);
    while (g_hash_table_iter_next(&iter, &key, &value)) {
        char* b_name = b_getBusinessName(value);
        char* b_id = b_getBusinessId(value);
        if (b_name[0] == tolower(letter) || b_name[0] == toupper(letter)) {
            matrix[i] = malloc(sizeof(char *) * 150);
            sprintf(matrix[i], "%s;%s", b_id, b_name); //Coloca a informação necessária em cada posição da matrix
            matrix = realloc(matrix, (i+2) * sizeof(char *));
            i++;           
        }
        free(b_name); 
        free(b_id); 
    }
    matrix[i] = NULL;

    return matrix;
}

//Query 3 AUX
//Armazena apenas num Array de String o id, o nome, a cidade e o estado do negócio pedido
char** searchBusinessInfo(BIZS b, char* business_id) {
    char** matrix = malloc(sizeof(char*)*3);
    matrix[0] = malloc(sizeof(char) * 55);
    sprintf(matrix[0], "%s", "business_id;name;city;state;stars;total_reviews"); //Cabeçalho

    gpointer value = g_hash_table_lookup(b->bizs, business_id); //Retorna o value, ie, a estrutura negócio que tem como key o business_id
    if (value != NULL) {
        matrix[1] = malloc(sizeof(char) * 200);
        char* b_id = b_getBusinessId(value);
        char* b_name = b_getBusinessName(value);
        char* b_city = b_getBusinessCity(value);
        char* b_state = b_getBusinessState(value);
        sprintf(matrix[1], "%s;%s;%s;%s;", b_id, b_name, b_city, b_state); //Guarda na matrix a informação necessária
        free(b_id);
        free(b_name);
        free(b_city);
        free(b_state);
        matrix[2]= NULL;
    }
    else{
        matrix[1]=NULL;
    }
    
    return matrix;
}

//Query 4 AUX
//Retorna o nome de um dado negócio
char* g_hash_SearchByBusinessName(BIZS b, char* business_name) {
    char* bus = (b_getBusinessName(g_hash_table_lookup(b->bizs, business_name)));
    if ( bus != NULL){
        return bus;
    }    
    else return "No Business found";    
}

//Query 5 AUX
//Dado um nome, retorna um Array de Strings com todos os businesses id e respestivos nomes, com esse nome 
GHashTable *searchBusinessCity(BIZS b, char* city_name) {
    GHashTableIter iter;
    gpointer key, value;

    GHashTable *allBus = g_hash_table_new_full(g_str_hash, g_str_equal, (GDestroyNotify)destroyBusinessKey, NULL);

    int i = 0;  //Iterador e controlador do tamanho de matrix
    //Iterar HashTable
    g_hash_table_iter_init(&iter, b->bizs);
    while (g_hash_table_iter_next(&iter, &key, &value)) {
        char* b_city = b_getBusinessCity(value);
        if ((strcmp(b_city, city_name)) == 0) {
            g_hash_table_insert(allBus, b_getBusinessId(value), value);
            i++;
        }
        free(b_city);
    }

    return allBus;
}

//Query 6 AUX
//Retorna um Array de Strings com todas as cidades
char** getAllCities(BIZS b){
    GHashTableIter iter;
    gpointer key, value;

    char** cities = malloc(sizeof(char *));
    cities[0]=NULL;
    int i = 0;  //Iterador
    int size = 1; //Controlador do tamanho de Cities
    g_hash_table_iter_init(&iter, b->bizs);
    while (g_hash_table_iter_next(&iter, &key, &value)) {
        char* b_city = b_getBusinessCity(value);
        if(isInList(cities, b_city) == 0){ //Verifica se o Array de Strings já contêm a b_city
            cities[i] = b_getBusinessCity(value);
            i++;
            size++;
            cities = realloc(cities, sizeof(char*) * (size));
            cities[i]=NULL;
        }
        free(b_city);
    }
    if(i==0) //Caso não tenha encontrado nenhuma cidade
        return NULL;
    
    cities[i] = NULL;    
    return cities;
}

//getAllCities AUX
//Verifica se o Array de Strings já contêm a b_city
int isInList (char** cities, char* city){
    for(int i = 0; cities[i]; i++){
        if ((strcmp(cities[i], city)) == 0){
            return 1;
        }
    }
    return 0;    
}

//Query 6 AUX
//Guarda numa GList todas os négocios que têm como cidade o argumento city
GList* findAllBusinessWithCity(BIZS b, GHashTable* r, char* city){
    GList* table = NULL;
    GHashTableIter iter;
    gpointer key, value;
    //Iterar HashTable
    g_hash_table_iter_init(&iter, b->bizs);
    while (g_hash_table_iter_next(&iter, &key, &value)) {  
        char* b_city = b_getBusinessCity(value);
        if( (strcmp(b_city, city) == 0) ){ //Caso o negócio tenha a cidade igual à cidade do argumento
            char* bus = malloc(sizeof(char) * 120);
            char* b_id = b_getBusinessId(value);
            char* b_name = b_getBusinessName(value);
            float star = getAverageStars(r, b_id); //Retorna a média de stars
            if (b_city != NULL && b_id != NULL && b_name != NULL){
                sprintf(bus, "%s;%s;%s;%.1f;", b_city, b_id, b_name ,star); //Armazena a cidade, id, nome e média de stars
                table = g_list_prepend(table, bus);
            }
            free(b_id);
            free(b_name);
        } 
        free(b_city);   
    }

    if(table == NULL){
        return NULL;
    }
    else{
        return table;     
    }
}

//Query 7
//Retorna uma String com o estado de um dado business_id
char* getBusinessState(BIZS b, char* business_reviewed) {
    gpointer value = g_hash_table_lookup(b->bizs, business_reviewed);

    if(value != NULL)
        return b_getBusinessState(value);

    return NULL;
}

//Query 8 AUX
GHashTable *searchBusinessCategory(BIZS b, char *category) {
    GHashTable *allBus = g_hash_table_new_full(g_str_hash, g_str_equal, (GDestroyNotify)destroyBusinessKey, NULL);
    GHashTableIter iter;
    gpointer key, value;

    g_hash_table_iter_init(&iter, b->bizs);
    while (g_hash_table_iter_next(&iter, &key, &value))
        if (busContainsCategory(value, category))      //Verifica se o negócio contém a categoria
            g_hash_table_insert(allBus, b_getBusinessId(value), value);

    return allBus;
}

//Guarda numa GList todas os négocios que têm a category
GList* findAllBusinessWithCategory(BIZS b, GHashTable* r, char* category){
    GList* table = NULL;
    GHashTableIter iter;
    gpointer key, value;
    //Iterar HashTable
    g_hash_table_iter_init(&iter, b->bizs);
    while (g_hash_table_iter_next(&iter, &key, &value)) {       
        if(busContainsCategory(value, category)){ //Verifica se o negócio contém a categoria
            char* bus = malloc(sizeof(char) * 120);
            char* b_id = b_getBusinessId(value);
            char* b_name = b_getBusinessName(value);
            float star = getAverageStars(r, b_id); //Retorna a média de stars
            sprintf(bus, "%s;%s;%.1f;", b_id, b_name, star); //Armazena a cidade, id, nome e média de stars
            table = g_list_prepend(table, bus);
            free(b_id);
            free(b_name);
        }     
    }

    if(table == NULL){
        return NULL;
    }
    else{
        return table;     
    }
}

//Query 8 AUX
//Verifica se o negócio contém a categoria
int busContainsCategory(BUSINESS b, char* category){
    char** categories = b_getBusinessCategories(b);
    if (categories != NULL)
        for(int i = 0; categories[i]; i++)
            if( (strcmp(categories[i],category) == 0) ) return 1;
    return 0;
}