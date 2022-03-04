#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <glib.h>

#include "../../includes/model/allUsers.h"


#define BUFF_SIZE 350000

//Estrutura do Catálogo de Users
struct users	{
	GHashTable *ht_users;
};

//Inicia a estrutura USERS
USERS initAllUsers() {
	USERS users = malloc(sizeof(struct users));
	users->ht_users = g_hash_table_new_full(g_str_hash, g_str_equal, (GDestroyNotify)destroyKey, (GDestroyNotify)destroyUser);
	return users;
}

void destroyKey(char* key){
	if(key) free(key);
}

//Carregamento de ficheiro para estrutura de Users sem Friends
USERS loadAllUsersFromFile(USERS u, char* filename) {

	FILE* f = fopen(filename, "r");
	if (f == NULL)
		return NULL;

	char buffer[BUFF_SIZE];
	while (fgets(buffer, BUFF_SIZE, f)) {
		char* userLine = (strtok((buffer), "\r\n"));
		USER user = initUserByLine(userLine);
		if (user != NULL)
			g_hash_table_insert(u->ht_users, u_getUserID(user), user);
	}

	fclose(f);
	printf("Users - Done! Size: %d\n", g_hash_table_size(u->ht_users));
	return u;
}

//Carregamento de ficheiro para estrutura de Users com Friends
USERS loadAllUsersFromFileWithFriends(USERS u, char* filename) {

	FILE* f = fopen(filename, "r");
	if (f == NULL)
		return NULL;
	char buffer[BUFF_SIZE];
	while (fgets(buffer, BUFF_SIZE, f)) {
		char* userLine = (strtok((buffer), "\r\n"));
		USER user = initUserByLineWithFriends(userLine);
		g_hash_table_insert(u->ht_users, u_getUserID(user), user);
	}
	
	fclose(f);
	printf("Users - Done! Size: %d\n", g_hash_table_size(u->ht_users));
	return u;
}


//Destrói Users
void destroyUsers(USERS u) {
	g_hash_table_destroy(u->ht_users);
	free(u);
}

//Retorna a HashTable do Catálogo de Users
GHashTable* u_getTable(USERS u){
    GHashTable* bt;
    bt = u->ht_users;
    return bt;
}

//Query 7 AUX
//Retorna um array de String com todos os IDS da Hashtable USERS
char** getAllUserIds(USERS u) {
    int i=0;
    int j=1;
    GHashTableIter iter;
    gpointer key, value;
    char** all_users_id = malloc(sizeof(char*));

    g_hash_table_iter_init(&iter, u->ht_users);
    while (g_hash_table_iter_next(&iter, &key, &value)) {
        all_users_id[i] = u_getUserID(value);
		j++;
        all_users_id = realloc(all_users_id, j * sizeof(char*));
        i++;
    }
    all_users_id[i] = NULL;

    return all_users_id;
}