#ifndef ALLREVIEWS_H
#define ALLREVIEWS_H

#include "review.h"
#include <glib.h>

typedef struct reviews *REVS; //Estutura de Catálogo de Reviews

REVS initReviews(); //Inicializador de Reviews

REVS loadAllReviewsFromFile(REVS rs,GHashTable* b,GHashTable* u, char* filename); //Carregamento de ficheiro para estrutura de Reviews, para além disso recebe a hashtable
                                                                                  // que contém Negócios e outra com Users para a validaçãoo

void destroyReviews(REVS rs);

char* searchReviews(REVS r, char* business_id); //Query 3 AUX

char** searchUserReviews(REVS r, char* user_id); //Query 4 AUX

int searchBusinessStars(REVS r, char* business_id, float stars); //Query 5 AUX

GHashTable *searchReviewByBusiness(REVS r, GHashTable *allBus);

float getAverageStars(GHashTable* r, char* business_id); //Query 8 AUX

char** searchWordInReviews (REVS r, char* word); //Query 9 AUX

int isInList2 (char** list, char* word, int j); //searchWordInReviews AUX

GHashTable* r_getTable(REVS r); //Retorna a HashTable do Catálogo de Reviews


#endif