#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "../../includes/model/sgr.h"

//Estrutura SGR
typedef struct sgr  {
	BIZS b;
	REVS r;
	USERS u;
} *SGR;

//Inicializador de SGR
SGR init_sgr() {
	SGR sgr = malloc(sizeof(struct sgr));
	sgr->b = initBusinesses();
	sgr->r = initReviews();
	sgr->u = initAllUsers();

	return sgr;
}

void free_sgr(SGR sgr) {
	destroyUsers(sgr->u);
	destroyBusinesses(sgr->b);
	destroyReviews(sgr->r);
	free(sgr);
}

/* query 1 */
//Dado o caminho para os 3 ficheiros, ler o seu conteúdo e carregar as estruturas de dados correspondentes
int load_sgr(SGR sgr, char* users, char* businesses, char* reviews, int userOption) {
	if ((sgr->b = loadBusinessesFromFile(sgr->b, businesses)) == NULL)
		return 1;
	
	if (userOption == 0) {
		if ((sgr->u = loadAllUsersFromFileWithFriends(sgr->u, users)) == NULL)
			return 1;
	}
	else {
		if ((sgr->u = loadAllUsersFromFile(sgr->u, users)) == NULL)
			return 1;
	}
	
	GHashTable* b = b_getTable(sgr->b);
	GHashTable* u = u_getTable(sgr->u);
	if ((sgr->r = loadAllReviewsFromFile(sgr->r, b, u, reviews)) == NULL)
		return 1;

	return 0;
}


/* query 2 */
//Determinar a lista de nomes de negócios e o número total de negócios cujo nome
//inicia por uma dada letra
char** business_started_by_letter(SGR sgr, char letter) {
	char** table = searchBusinessLetter(sgr->b, letter);

	return table;
}


/* query 3 */
//Dado um id de neǵocio, retornar a sua informação
char** business_info(SGR sgr, char* business_id) {
	char** table = searchBusinessInfo(sgr->b, business_id); //Armazena apenas num Array de String o id, o nome, a cidade e o estado do negócio pedido
	if (table[1]){
		char* stars_review = searchReviews(sgr->r, business_id); //Armazena apenas a média de stars e o total de reviews do negócio
		strcat(table[1], stars_review); //concatena o array que continha o id, o nome, a cidade e estado com o número médio de stars e total de reviews de negócio
		                                //de forma, a obter o resultado pretendido
		if (stars_review != NULL)
			free(stars_review);
	}
	else
		table[1] = "No business found";

	return table;
}

/* query 4 */
//Dado um id de utilizador, determinar a lista de negócios(Review_id)aos quais fez review
char** businesses_reviewed(SGR sgr, char* user_id) {
	char** table = malloc(sizeof(char *) * 2); //Array de Strings de retorno
	table[0] = malloc(sizeof(char) * 20);
	sprintf(table[0], "business_id;name"); //Cabeçalho

	int i, j, size; //i - Iterador da table, j - Iterador de matrix, size - controlador do tamanho da table
	char** matrix = searchUserReviews(sgr->r, user_id); //Armazena num Array de Strings as business_id que foram classificadas pelo user
	for(i = 1, size = 3,j = 0; matrix[j]; i++, j++, size++) {
		table[i] = malloc(sizeof(char) * 100);
		char* b_name = g_hash_SearchByBusinessName(sgr->b, matrix[j]); //Retorna o nome de um dado negócio
		sprintf(table[i], "%s;%s", matrix[j], b_name);
		table = realloc(table, sizeof(char *) * (size));
		free(b_name);
	}
	table[i] = NULL;
	
	for(int k=0; matrix[k]; k++){
		free(matrix[k]);
	}
	free(matrix);

	return table;
}


/* query 5 */
char **businessStarsCity(GHashTable *allBus, GHashTable *allRev, float stars) {
	GHashTableIter iterB, iterR;
	gpointer keyB, keyR, valueB, valueR;

	int i = 1;
	char **matrix = malloc(sizeof(char *) * 2);
	matrix[0] = malloc(sizeof(char) * 20);
	sprintf(matrix[0], "business_id;name");

	g_hash_table_iter_init(&iterB, allBus);
	while (g_hash_table_iter_next(&iterB, &keyB, &valueB)) {
		char *b_idB = (char*)keyB;
		int sumS = 0, counter = 0;

		g_hash_table_iter_init(&iterR, allRev);
		while (g_hash_table_iter_next(&iterR, &keyR, &valueR)) {
			char *b_idR = r_getBusinessID(valueR);
			if ((strcmp(b_idB, b_idR)) == 0) {
				sumS += r_getStars(valueR);
				counter++;
			}
			free(b_idR);
		}

		float averageS = 0;
		if (counter != 0)
			averageS = sumS/(float)counter;

		if (averageS >= stars) {
			matrix[i] = malloc(sizeof(char) * 150);
			char *b_nameB = b_getBusinessName(valueB);
			sprintf(matrix[i++], "%s;%s", b_idB, b_nameB);
			matrix = realloc(matrix, (i+1) * sizeof(char*));
			free(b_nameB);
		}
	}
	matrix[i] = NULL;

	return matrix;
}

//Dado um número n de stars e uma cidade, determina a lista de négocios com n ou mais stars da dada cidade
char** businesses_with_stars_and_city(SGR sgr, float stars, char* city) {
		GHashTable *allBus = searchBusinessCity(sgr->b, city); //Dado um nome, retorna um Array de Strings com todos os businesses com esse nome 
		GHashTable *allRev = searchReviewByBusiness(sgr->r, allBus);
		char **matrix = businessStarsCity(allBus, allRev, stars);

		g_hash_table_destroy(allBus);
		g_hash_table_destroy(allRev);

		return matrix;
}


/* query 6 */
//Dado um número inteiro n, determina a lista dos top n negócios em cada cidade
char** top_businesses_by_city(SGR sgr, int top){
	GHashTable* rt = r_getTable(sgr->r); //HashTable do Catálogo das Reviews
	char** cities = getAllCities(sgr->b); //Retorna um Array de Strings com todas as cidades
	char** table = NULL; //Array de Strings que armazena os top de cada cidade
	char** finalTable = malloc(sizeof(char*)); //Array de Strings de retorno
	int i = 0;
	int pos = 0;
	int size = 1;
	while(cities[i]){
		GList* allBusWithCity = findAllBusinessWithCity(sgr->b, rt, cities[i]); //Guarda numa GList todas os négocios que têm como cidade o argumento city
		allBusWithCity = g_list_sort(allBusWithCity, sortByStarsCity ); //Função auxiliar de sort que comparar a média de stars usada em g_list_sort()
		GList* topN = cutList(allBusWithCity, top); //Corta a GList para conter apenas os top primeiros elementos
		table = convertToChar(topN); //Converte a GList para char**
		for(int k = 0; table[k]; k++, pos++){ //Armazena na finalTable a informação de cada Table
			char* t_k = strdup(table[k]);
			finalTable[pos] = t_k;
			size++;
			finalTable = realloc(finalTable, sizeof(char*)*(size));
		}
		for(int j = 0; table[j]; j++) free(table[j]);
		free(table);
		g_list_free_full(allBusWithCity, (GDestroyNotify)freeList);
		g_list_free_full(topN, (GDestroyNotify)freeList);
		i++;
	}
	if(pos==0) return NULL;
	finalTable[pos] = NULL;
	for(int k=0; cities[k]; k++){
		free(cities[k]);
	}
	free(cities);
	return finalTable;
}

//Query 6 AUX
//Função auxiliar de sort que comparar a média de stars usada em g_list_sort()
gint sortByStarsCity(gconstpointer a, gconstpointer b){
	//Cada posiçaõ da Array de Strings contém: City;business_id;name;stars
	char* a1 = strdup((char*) a);
	char* buf_a;
	int i = 0;
	float star_a = 0.0;
	buf_a = strtok(a1, ";");
	i++;
	while( (buf_a = strtok(NULL, ";")) != NULL){
		if(i == 3) {
			star_a = atof(buf_a);
		}	
		i++; 
	}

	char* b1 = strdup((char*) b);
	char* buf_b;
	int j = 0;
	float star_b = 0.0;
	buf_b = strtok(b1, ";");
	j++;
	while( (buf_b = strtok(NULL, ";")) != NULL){
		if(j == 3){ 
			star_b = atof(buf_b);
		}	
		j++; 
	}
	free(b1);
	free(a1);

	if(star_a < star_b) {
		return 1;
	}	
	else if(star_a > star_b){
		return -1;
	} 
	else return 0;
}

/* query 7 */
//Determina a lista de ids de utilizadores e o número total de utilizadores que tenham
//visitado mais de um estado
char** international_users(SGR sgr) {
		GHashTable *allUs = g_hash_table_new_full(g_str_hash, g_str_equal,(GDestroyNotify)destroyKey, NULL);
		GHashTable *r = r_getTable(sgr->r);
		GHashTable *b = b_getTable(sgr->b);
		GHashTableIter iterA, iterB;
		gpointer keyA, keyB, valueA, valueB;

		char **matrix = malloc(sizeof(char*) * 2);
		matrix[0] = malloc(sizeof(char) * 10);
		sprintf(matrix[0], "user_id");
		int i = 1, j = 0;

		g_hash_table_iter_init(&iterA, r);
		while (g_hash_table_iter_next(&iterA, &keyA, &valueA)) {		// Iteramos duas vezes a HashTable das Reviews
			char *user_idA = r_getUserID(valueA);											// Procuramos por um utilizador que tem 2 estados diferentes nas suas reviews
			char *user_businessidA = r_getBusinessID(valueA);
			char *user_stateA = b_getBusinessState(g_hash_table_lookup(b, user_businessidA));

			if (!g_hash_table_contains(allUs, user_idA)) {
				g_hash_table_insert(allUs, user_idA, NULL);
			
				g_hash_table_iter_init(&iterB, r);
				while (g_hash_table_iter_next(&iterB, &keyB, &valueB)) {
					char *user_idB = r_getUserID(valueB);
					char *user_businessidB = r_getBusinessID(valueB);
					char *user_stateB = b_getBusinessState(g_hash_table_lookup(b, user_businessidB));
					int flagStates = 0;

					if (strcmp(user_idA, user_idB) == 0)
						if (strcmp(user_stateA, user_stateB) != 0)
							flagStates++;

					free(user_idB);
					free(user_businessidB);
					free(user_stateB);
					if (flagStates == 1) {
						matrix[i] = malloc(sizeof(char) * 25);
						sprintf(matrix[i++], "%s", user_idA);
						matrix = realloc(matrix, (i+1) * sizeof(char*));
						break;
					}
				}
			}
			j++;

			free(user_businessidA);
			free(user_stateA);
		}
		matrix[i] = NULL;
		
		g_hash_table_destroy(allUs);

    return matrix;
}


/* query 8 */
//Constrói uma lista com todos os businesses, com a média de estrelas já calculada.
GList* allBusinessesWithCategory(GHashTable *allBus, GHashTable *allRev) {
	GHashTableIter iterB, iterR;
	gpointer keyB, keyR, valueB, valueR;

	GList *list = NULL;

	g_hash_table_iter_init(&iterB, allBus);
	while (g_hash_table_iter_next(&iterB, &keyB, &valueB)) {		// Iterar a HashTable Business
		char *b_idB = (char*)keyB;
		int sumS = 0, counter = 0;

		g_hash_table_iter_init(&iterR, allRev);
		while (g_hash_table_iter_next(&iterR, &keyR, &valueR)) {	// Iterar a HashTable Review
			char *b_idR = r_getBusinessID(valueR);
			if ((strcmp(b_idB, b_idR)) == 0) {		// Se os ids foram igual então somamos e aumentamos a 'counter'
				sumS += r_getStars(valueR);
				counter++;
			}
			free(b_idR);
		}

		float averageS = 0;
		if (counter != 0)
			averageS = sumS/(float)counter;		// Se no final a 'counter' for diferente de 0, calculamos a media

		char *buffer = malloc(sizeof(char) * 150);
		char* b_id = b_getBusinessId(valueB);
		char* b_name = b_getBusinessName(valueB);
		sprintf(buffer, "%s;%s;%.1f", b_id, b_name, averageS);
		list = g_list_prepend(list, buffer);
		free(b_id);
		free(b_name);
	}

	if (list)
		return list;
	else
		return NULL;
}

//Dado um número inteiro n e uma categoria, determina a lista dos top n negócios
//que pertencem a uma determinada categoria
char** top_businesses_with_category(SGR sgr, int top, char* category){
	GHashTable *allBus = searchBusinessCategory(sgr->b, category);	// Todas as business que possuiem a categoria dada
	GHashTable *allRev = searchReviewByBusiness(sgr->r, allBus);		// Todas as reviews que possuem todas as business

	GList* allBusWithCat = allBusinessesWithCategory(allBus, allRev); //Guarda numa GList todas os négocios que têm a category

	g_hash_table_destroy(allBus);
	g_hash_table_destroy(allRev);
	
	if(allBusWithCat == NULL)
		return NULL;

	//Ordena a glist por media de stars
	allBusWithCat = g_list_sort(allBusWithCat, (GCompareFunc)sortByStarsCategories); //Função auxiliar de sort que comparar a média de stars usada em g_list_sort()
	//Guarda apenas os N primeiros elementos
	GList* topN = cutList(allBusWithCat, top);
	g_list_free_full(allBusWithCat, (GDestroyNotify)freeList);

	//Transforma num char**
	char** table = convertToChar(topN);

	g_list_free_full(topN, (GDestroyNotify)freeList);

	return table;
}

void freeList (char* bus){
	free(bus);
}

//Query 6 e 8 AUX
//Corta a GList para conter apenas os top primeiros elementos
GList* cutList(GList* b, int top){
	GList* l = NULL;
	for(int i=0 ; i < top && b->data != NULL && b->next != NULL; b=b->next, i++) {
		l = g_list_append(l, strdup(b->data));
	}
	return l;
}

//Função auxiliar de sort que comparar a média de stars usada em g_list_sort()
gint sortByStarsCategories(gconstpointer a, gconstpointer b){
	//Cada posição da Array de Strings contém: business_id;name;stars
	char* a1 = strdup((char*) a);
	char* buf_a;
	int i = 0;
	float star_a = 0.0;
	buf_a = strtok(a1, ";");
	i++;
	while ((buf_a = strtok(NULL, ";")) != NULL) {
		if (i == 2) {
			star_a = atof(buf_a);
		}	
		i++; 
	}

	char* b1 = strdup((char*) b);
	char* buf_b;
	int j = 0;
	float star_b = 0.0;
	buf_b = strtok(b1, ";");
	j++;
	while ((buf_b = strtok(NULL, ";")) != NULL) {
		if (j == 2){ 
			star_b = atof(buf_b);
		}	
		j++;
	}
	free(b1);
	free(a1);

	if(star_a < star_b) {
		return 1;
	}	
	else 
		if(star_a > star_b) {
			return -1;
		} 
	else
		return 0;
}

//Converte a GList para char**
char** convertToChar(GList* st){
	char** table = malloc(sizeof(char*) * 2);
	table[0] = malloc(sizeof(char) * 25);
	sprintf(table[0], "business_id;name;stars");
	
	int i, size = 1;
	GList* l;
	for(i = 0, l=st; l != NULL ; l=l->next, i++){
		table[size++] = strdup((char*)(l->data));
		table = realloc(table, sizeof(char*) * (size+1));
	}
	table[size] = NULL;

	return table;	
}


/* query 9 */
//Dada uma palavra, determina a lista de ids de reviews que a referem no campo text
char** reviews_with_word(SGR sgr, char* word){
	char** table = malloc(sizeof(char*));
	table = searchWordInReviews (sgr->r, word); //Retorna um Array de Strings com as reviews que têm a word no campo text
	if(table[0]==NULL){
		table = realloc(table, sizeof(char*) * 2);
		table[0] = malloc(sizeof(char) * 17);
		sprintf(table[0], "No reviews found");
		table[1] = NULL;
	}
	return table;
}