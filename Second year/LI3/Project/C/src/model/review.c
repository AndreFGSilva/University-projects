#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "../../includes/model/review.h"

//Estrutura de uma review
typedef struct review {
	char* review_id;
	char* user_id;
	char* business_id;
	float stars;
	int useful;
	int funny;
	int cool;
	char* date;
	char* text;	
} *REVIEW;

//Cria uma review a partir do que recebe como argumento
REVIEW initReview(char* r_id, char* u_id, char* b_id, float s, int uf, int fun, int cool, char* date, char* text) {
	REVIEW rev = malloc(sizeof(struct review));
	
	rev->review_id = strdup(r_id);
	rev->user_id = strdup(u_id);
	rev->business_id = strdup(b_id);
	rev->stars = s;
	rev->useful = uf;
	rev->funny = fun;
	rev->cool = cool;
	rev->date = strdup(date);
	if(text){
		rev->text = strdup(text);
	}
	else
		rev->text=NULL;
	
	
	return rev;
}

//Recebendo uma linha, cria uma review
REVIEW initReviewByLine(char* line) {
	char* r_id = strtok(line, ";");
	char* u_id = strtok(NULL, ";");
	char verifier = (u_id-1)[0]; //Verifica se existe 2 ";" seguidos, i.e, r_id;;u_id
	char* b_id = strtok(NULL, ";");
	char verifier1 = (b_id-1)[0]; 
	float star = atof(strtok(NULL, ";"));
	int useful = atoi(strtok(NULL, ";"));
	int funny = atoi(strtok(NULL, ";"));
	int cool = atoi(strtok(NULL, ";"));
	char* date = strtok(NULL, ";");
	char verifier2 = (date-1)[0];
	char* text = strtok(NULL, ";\r\n");
	char verifier3 = (text-1)[0];
	
	//Método de verificação de dados do ficheiro que contém reviews
	if(r_id == NULL || u_id == NULL || b_id == NULL || date == NULL || verifier == ';' || verifier1 == ';' || verifier2 == ';' || verifier3 == ';'){
        return NULL;
    }

	return initReview(r_id, u_id, b_id, star, useful, funny, cool, date, text); 
}

//Getters
char* r_getRevID(REVIEW r) {
	return strdup(r->review_id);
}

char* r_getUserID(REVIEW r) {
	return strdup(r->user_id);
}

char* r_getBusinessID(REVIEW r) {
	return strdup(r->business_id);
}

float r_getStars(REVIEW r) {
	return r->stars;
}

int r_getUseful(REVIEW r) {
	return r->useful;
}

int r_getFunny(REVIEW r) {
	return r->funny;
}

int r_getCool(REVIEW r){
	return r->cool;
}

char* r_getDate(REVIEW r) {
	return strdup(r->date);
}

char* r_getText(REVIEW r) {
	return strdup(r->text);
}

//Info da Review
void r_getReviewInfo(REVIEW r) {
	printf("Review_ID: %s\nUser_ID: %s\nBusiness_ID: %s\nStars: %f\nUseful: %d\nFunny: %d\nCool: %d\nDate: %s\nText: %s\n", r->review_id, r->user_id, r->business_id, r->stars, r->useful, r->funny, r->cool, r->date, r->text);
}

//Clone da Review
REVIEW cloneReview (REVIEW r) {
	REVIEW new_r = malloc(sizeof(struct review));
	
	new_r->review_id = strdup(r->review_id);
	new_r->user_id = strdup(r->user_id);
	new_r->business_id = strdup(r->business_id);
	new_r->stars = r->stars;
	new_r->useful = r->useful;
	new_r->funny = r->funny;
	new_r->cool = r->cool;
	new_r->date = strdup(r->date);
	new_r->text = strdup(r->text);

	return new_r;
}

//Destroyers/Frees
void destroyReview(REVIEW r) {
	free(r->review_id);
	free(r->user_id);
	free(r->business_id);
	free(r->date);
	if(r->text)
		free(r->text);
	free(r);
}

void destroyReviewKey(char* key){
	free(key);
}