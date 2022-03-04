#ifndef REVIEWS_H
#define REVIEWS_H


typedef struct review *REVIEW; //Estrutura de uma review

REVIEW initReview (char* r_id, char* u_id, char* b_id, float s, int uf, int fun, int cool, char* date, char* text); //Cria uma review a partir do que recebe como argumento

REVIEW initReviewByLine(char* line); //Recebendo uma linha, cria uma review

//Getters
char* r_getRevID(REVIEW r);
char* r_getUserID(REVIEW r);
char* r_getBusinessID(REVIEW r);
float r_getStars(REVIEW r);
int r_getUseful(REVIEW r);
int r_getFunny(REVIEW r);
int r_getCool(REVIEW r);
char* r_getDate(REVIEW r);
char* r_getText(REVIEW r);

void r_getReviewInfo(REVIEW r); //Info da Review

REVIEW cloneReview (REVIEW r); //Clone da Review

//Destroyers/Free
void destroyReview(REVIEW r);
void destroyReviewKey(char* key);


#endif