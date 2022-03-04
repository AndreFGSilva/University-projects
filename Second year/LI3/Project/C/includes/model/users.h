#ifndef USERS_H
#define USERS_H

typedef struct user *USER; //Estrutura do Users

USER initUser(char *u_id, char *n); //Constrói um User a partir dos argumentos

USER initUserWithFriends(char* u_id, char*n, GHashTable* friends); //Constrói um User com os amigos armazenados a partir dos argumentos

USER initUserByLine(char *line); //Recebe uma linha do ficheiro e constrói um User a partir dela

USER initUserByLineWithFriends(char *line); //Recebe uma linha do ficheiro e constrói um User com amigos a partir dela

//Getters
char* u_getUserID(USER u);
char* u_getName(USER u);
GHashTable* u_getHashTableFriends(GHashTable* old);

void u_getUserInfo(USER u); //Info do User

USER cloneUser(USER u); //Clone de User

//Destroyers/Frees
void destroyFriend(char* friend);
void destroyTableFriends(USER u);
void destroyUser(USER u);


#endif