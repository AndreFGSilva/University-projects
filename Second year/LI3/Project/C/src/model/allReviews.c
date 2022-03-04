#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <glib.h>

#include "../../includes/model/allReviews.h"

#define BUFF_SIZE 5500

//Estutura de Catálogo de Reviews
struct reviews {
    GHashTable* revs;
};

//Inicializador de Reviews
REVS initReviews() {
    REVS reviews = malloc(sizeof(struct reviews));
    reviews->revs = g_hash_table_new_full(g_str_hash, g_str_equal, (GDestroyNotify)destroyReviewKey, (GDestroyNotify)destroyReview); 
    
    return reviews;
}

//Carregamento de ficheiro para estrutura de Reviews, para além disso recebe a hashtable
// que contém Negócios e outra com Users para a validaçãoo
REVS loadAllReviewsFromFile(REVS rs,GHashTable* b,GHashTable* u, char* filename) {
    FILE* f = fopen(filename, "r");
    if(f == NULL)
        return NULL;

    char buffer[BUFF_SIZE];
    while(fgets(buffer, BUFF_SIZE, f)) {
        char* reviewLine = (strtok((buffer), "\r\n"));
        REVIEW r = initReviewByLine(reviewLine);
        if (r!=NULL){
            char* b_id = r_getBusinessID(r);
            char* u_id = r_getUserID(r);
            if(g_hash_table_contains(b, b_id) && g_hash_table_contains(u, u_id)){ //Verifica se os Negócios e Users existem
                g_hash_table_insert(rs->revs, r_getRevID(r), r);
            }
            else{
                destroyReview(r);
            }
            free(b_id);
            free(u_id);
        }
        //free(r);
    }
    fclose(f);  
    printf("Reviews - Done! Size: %d\n", g_hash_table_size(rs->revs));
    return rs;  
}

void destroyReviews(REVS rs) {
    g_hash_table_destroy(rs->revs);
    free(rs);
}

//Query 3 AUX
//Armazena apenas a média de stars e o total de reviews do negócio
char* searchReviews(REVS r, char* business_id) {
    GHashTableIter iter;
    gpointer key, value;
    
    char* stars_and_reviews = malloc(sizeof(char)*20); //Array que vai ser retornado
    float stars = 0.0;
    int total_reviews = 0; 
    
    //Iterador da HashTable
    g_hash_table_iter_init(&iter, r->revs);
    while(g_hash_table_iter_next(&iter, &key, &value)){
        char* r_id = r_getBusinessID(value);
        if((strcmp(r_id, business_id)) == 0){ //Comparar se é o neǵocio certo 
            total_reviews++;
            stars += r_getStars(value);
        }
        free(r_id);
    }

    if (total_reviews != 0) //Caso tenha encontrado reviews
        sprintf(stars_and_reviews, "%.1f;%d", (stars/total_reviews), total_reviews);
    else //Caso não tenha encontrado reviews
        sprintf(stars_and_reviews, "%s", "No reviews found!");

    return stars_and_reviews;
}

//Query 4 AUX
//Armazena num Array de Strings as business_id que foram classificadas pelo user
char** searchUserReviews(REVS r, char* user_id) {
    GHashTableIter iter;
    gpointer key, value;

    char** matrix = malloc(sizeof(char *));
    int i = 0; //Iterador da matrix
    int size = 1; //Controlador do tamanho da matrix
    
    //Iterar HashTable
    g_hash_table_iter_init(&iter, r->revs);
    while (g_hash_table_iter_next(&iter, &key, &value)){
        char* u_id = r_getUserID(value);
        if((strcmp(u_id, user_id)) == 0){ 
            matrix[i] = r_getBusinessID(value);
            i++;
            size++;
            matrix = realloc(matrix, (size) * sizeof(char*));
        }
        free(u_id);
    }
    matrix[i] = NULL;
    
    return matrix;
}

//Query 5 AUX
//Dado um business_id e stars, verifica se a média de stars desse negócio é menor ou igual à stars dada como argumento
//retorna 1 caso seja menor ou igual, retorna 0 caso contrário
int searchBusinessStars(REVS r, char* business_id, float stars) {
    GHashTableIter iter;
    gpointer key, value;
    g_hash_table_iter_init(&iter, r->revs);
    while (g_hash_table_iter_next(&iter, &key, &value)){
        char* b_id = r_getBusinessID(value);
        if ((strcmp(business_id, b_id)==0)){
            if (stars <= r_getStars(value)){
                free(b_id);
                return 1;
            }
        }
        free(b_id);
    }
    return 0;
}

//Query 7 AUX
GHashTable *searchReviewByBusiness(REVS r, GHashTable *allBus) {
    GHashTable *allRev = g_hash_table_new_full(g_str_hash, g_str_equal, (GDestroyNotify)destroyReviewKey, NULL); 
    GHashTableIter iter;
    gpointer key, value;
    
    g_hash_table_iter_init(&iter, r->revs);
    while (g_hash_table_iter_next(&iter, &key, &value)) {
        char *b_id = r_getBusinessID(value);
        if (g_hash_table_contains(allBus, b_id))
            g_hash_table_insert(allRev, r_getRevID(value), value);
        free(b_id);
    }

    return allRev;
}

//Query 8 AUX
//Retorna a média de stars
float getAverageStars(GHashTable* r, char* business_id) {
    GHashTableIter iter;
    gpointer key, value;
    
    float stars = 0.0;
    int i = 0;

    g_hash_table_iter_init(&iter, r);
    while(g_hash_table_iter_next(&iter, &key, &value)){
        char* b_id = r_getBusinessID(value);
        if( (strcmp(b_id, business_id)) == 0) {
            i++;
            stars += r_getStars(value);
        }
        free(b_id);
    }

    if (i != 0)
        return (stars/i);
    else
        return 0.0;
}

//Query 9 AUX
//Retorna um Array de Strings com as reviews que têm a word no campo text
char** searchWordInReviews (REVS r, char* word){
    char** review_ids = malloc(sizeof(char*));
    char* text;
    char* t_word;
    int i=0; //Iterador de reviews_ids
    GHashTableIter iter;
    gpointer key, value;
    //Iterar HashTable
    g_hash_table_iter_init(&iter, r->revs);
    while (g_hash_table_iter_next(&iter, &key, &value)) {
        text = r_getText(value); 
        t_word = strtok(text, " ");
        char* rev_id = r_getRevID(value); 
        if(t_word != NULL && strcmp(word, t_word)==0){
            if(!(isInList2(review_ids, rev_id, i))){ //Verifica se a word existe até list[j]
                review_ids[i] = r_getRevID(value);
                i++;
                review_ids = realloc(review_ids,(sizeof(char *)* (i+1)));
            }
        }

        while(t_word != NULL){ //Percorre o campo Text
            t_word = strtok(NULL, " ");
            if(t_word != NULL && strcmp(word, t_word)==0){ 
                if(!(isInList2(review_ids, rev_id, i))){ //Verifica se a word existe até list[j]
                    review_ids[i] = r_getRevID(value);
                    i++;
                    review_ids = realloc(review_ids,(sizeof(char *)* (i+1)));
                }
            }
        }
        free(rev_id);
        free(text);
    }
    review_ids[i]=NULL;

    return review_ids;
}

//searchWordInReviews AUX
//Verifica se a word existe até list[j]
int isInList2 (char** list, char* word, int j){
    for(int i = 0; i<j; i++){
        if ((strcmp(list[i], word)) == 0) return 1;
    }    
    return 0;    
}

//Retorna a HashTable do Catálogo de Reviews
GHashTable* r_getTable(REVS r) {
    GHashTable *rt;
    rt = r->revs;
    return rt;
}