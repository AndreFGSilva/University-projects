#ifndef ALLUSERS_H
#define ALLUSERS_H

#include "users.h"

typedef struct users *USERS; //Estrutura do Catálogo de Users

USERS initAllUsers(); //Inicia a estrutura USERS

USERS loadAllUsersFromFile(USERS allu, char* filename); //Carregamento de ficheiro para estrutura de Users sem Friends

USERS loadAllUsersFromFileWithFriends(USERS u, char* filename); //Carregamento de ficheiro para estrutura de Users com Friends

void destroyUsers(USERS u);

void destroyKey(char* key);

void destroyAllUsers(USERS u);

GHashTable* u_getTable(USERS u); //Retorna a HashTable do Catálogo de Users

char** getAllUserIds(USERS u); //Query 7 AUX

#endif

