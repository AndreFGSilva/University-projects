#ifndef SGR_H
#define SGR_H

#include "allBusinesses.h"
#include "allReviews.h"
#include "allUsers.h"

typedef struct sgr *SGR; //Estrutura SGR

SGR init_sgr(); //Inicializador de SGR

void free_sgr(SGR sgr);

int load_sgr(SGR sgr, char* users, char* businesses, char* reviews, int userOption); /* query 1 */

char** business_started_by_letter(SGR sgr, char letter);

char** business_info(SGR sgr, char* business_id);

char** business_started_by_letter(SGR sgr, char letter); /* query 2 */

char** business_info(SGR sgr, char* business_id); /* query 3 */

char** businesses_reviewed(SGR sgr, char* user_id); /* query 4 */

char **businessStarsCity(GHashTable *allBus, GHashTable *allRev, float stars);

char** businesses_with_stars_and_city(SGR sgr, float stars, char* city); /* query 5 */

char** top_businesses_by_city(SGR sgr, int top); /* query 6 */

gint sortByStarsCity(gconstpointer a, gconstpointer b); //Query 6 AUX

char** international_users (SGR sgr); /* query 7 */

GList* allBusinessesWithCategory(GHashTable *allBus, GHashTable *allRev);

char** top_businesses_with_category(SGR sgr, int top, char* category); /* query 8 */

void freeList (char* bus);

GList* cutList(GList* b, int top); //Query 6 e 8 AUX

gint sortByStarsCategories(gconstpointer a, gconstpointer b); //Query 8 AUX

char** convertToChar(GList* st); //Query 6 e 8 AUX

char** reviews_with_word(SGR sgr, char* word); /* query 9 */


#endif