#ifndef ALLBUSINESSES_H
#define ALLBUSINESSES_H

#include "business.h"
#include <glib.h>

typedef struct businesses* BIZS; //Estrutura do Catálogo de Negócios

BIZS initBusinesses(); //Inicializador de Negócios

BIZS loadBusinessesFromFile(BIZS b, char* filename); //Carregamento de ficheiro para estrutura de Negócios

void destroyBusinesses (BIZS b); //Free da estrutura de Negócios

GHashTable* b_getTable(BIZS b); //Retorna a HashTable da estrutura Negócios

char** searchBusinessLetter(BIZS b, char letter); //Quert 2 AUX

char** searchBusinessInfo(BIZS b, char* business_id); //Query 3 AUX

char* g_hash_SearchByBusinessName(BIZS b, char* business_name); //Query 4 AUX

GHashTable *searchBusinessCity(BIZS b, char* city_name); //Query 5 AUX

char** getAllCities(BIZS b); //Query 6 AUX

int isInList (char** cities, char* city); //getAllCities AUX

GList* findAllBusinessWithCity(BIZS b, GHashTable* r, char* city); //Query 6 AUX

char* getBusinessState(BIZS b, char* business_reviewed); //Query 7 AUX

GHashTable *searchBusinessCategory(BIZS b, char *category);

GList* findAllBusinessWithCategory(BIZS b, GHashTable* r, char* category); //Query 8 AUX

int busContainsCategory(BUSINESS b, char* category); //Query 8 AUX


#endif