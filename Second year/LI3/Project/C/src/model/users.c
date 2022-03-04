#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <glib.h>

#include "../../includes/model/users.h"

//Estrutura do Users
struct user	{
	char* user_id;
	char* name;
	GHashTable* friends;
};

//Constrói um User a partir dos argumentos
USER initUser(char *u_id, char *n) {
	USER u = malloc(sizeof(struct user));

	u->user_id = strdup(u_id);
	u->name = strdup(n);
	u->friends = NULL;

	return u;
}

//Constrói um User com os amigos armazenados a partir dos argumentos
USER initUserWithFriends(char* u_id, char*n, GHashTable* friends){
	USER u = malloc(sizeof(struct user));

	u->user_id = strdup(u_id);
	u->name = strdup(n);
	u->friends = friends;
	
	return u;
}

//Recebe uma linha do ficheiro e constrói um User a partir dela
USER initUserByLine(char *line) {
	char *u_id = strtok(line, ";");
	char *n = strtok(NULL, ";");
	char verifier = (n-1)[0]; //Verifica se existem dois ";;", i.e, user_id;;name
	char* buf1, *buf2;

	//Verificação de dados do ficheiro
	if (u_id == NULL || n == NULL || verifier == ';')  //Verifica se existe algo mais para além dos amigos
		return NULL;

	return initUser(u_id, n);
}

//Recebe uma linha do ficheiro e constrói um User com amigos a partir dela
USER initUserByLineWithFriends(char *line) {
	GHashTable* f = g_hash_table_new_full(g_str_hash, g_str_equal,(GDestroyNotify)destroyFriend, (GDestroyNotify)destroyFriend);
	char *u_id = strtok(line, ";");
	char *n = strtok(NULL, ";");
	char verifier = (n-1)[0]; //Verifica se existem dois ";;", i.e, user_id;;name
	char* buf;
	while((buf = strtok(NULL, ",")) != NULL)
		g_hash_table_insert(f, strdup(buf), strdup(buf));

	if (u_id == NULL || n == NULL || verifier == ';')
		return NULL; 	
	
	return initUserWithFriends(u_id, n, f);
}

// GET functions //
char* u_getUserID(USER u) {
	return strdup(u->user_id);
}

char* u_getName(USER u) {
	return strdup(u->name);
}

GHashTable* u_getHashTableFriends(GHashTable* old){
	GHashTable* f = g_hash_table_new_full(g_str_hash, g_str_equal,(GDestroyNotify)destroyFriend, (GDestroyNotify)destroyFriend);
	GHashTableIter iter;
	gpointer key, value;
	g_hash_table_iter_init(&iter, old);

	while(g_hash_table_iter_next(&iter, &key, &value)){
		g_hash_table_insert(f,strdup((char*)key), strdup((char*)value));
	}

	return f;
}

//Info do User
void u_getUserInfo(USER u) {
	printf("User_ID: %s\nName: %s\nFriends:\n", u->user_id, u->name);
	GHashTableIter iter;
    gpointer key, value;
	g_hash_table_iter_init(&iter, u->friends);
    while (g_hash_table_iter_next(&iter, &key, &value)) {
		printf("%s\n", (char*)value);
	}	
}

//Clone do User
USER cloneUser(USER u) {
	USER newU = malloc(sizeof(struct user));
	newU->user_id = strdup(u->user_id);
	newU->name = strdup(u->name);
	
	if (u->friends != NULL){
		newU->friends = u_getHashTableFriends(u->friends);
	}
	else 
		newU->friends = NULL;

	return newU;
}

//Destroyers/Frees
void destroyUser(USER u) {
	free(u->user_id);
	free(u->name);
	destroyTableFriends(u);
	free(u);
}

void destroyFriend(char* friend){
	free(friend);
}

void destroyTableFriends(USER u){
	if (u->friends)
		g_hash_table_destroy(u->friends);
}