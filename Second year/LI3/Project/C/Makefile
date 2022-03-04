CC = gcc
CFLAGS = -std=c99 -Wextra -g -O2 -D_GNU_SOURCE
LIBS = `pkg-config --cflags --libs glib-2.0`
INCLUDES = includes
SRC=src
OBJ=obj

program: programAUX view paginacao main prog

programAUX: user business review allUsers allBusinesses allReviews sgr

test: programAUX allsgr

user: $(SRC)/model/users.c $(INCLUDES)/model/users.h
	$(CC) $(CFLAGS) -o $(OBJ)/user.o $(LIBS) -c $(SRC)/model/users.c
business: $(SRC)/model/business.c $(INCLUDES)/model/business.h
	$(CC) $(CFLAGS) -o $(OBJ)/business.o $(LIBS) -c $(SRC)/model/business.c
review: $(SRC)/model/review.c $(INCLUDES)/model/review.h
	$(CC) $(CFLAGS) -o $(OBJ)/review.o $(LIBS) -c $(SRC)/model/review.c

allUsers: $(SRC)/model/allUsers.c $(INCLUDES)/model/allUsers.h
	$(CC) $(CFLAGS) -o $(OBJ)/allUsers.o $(LIBS) -c $(SRC)/model/allUsers.c
allBusinesses: $(SRC)/model/allBusinesses.c $(INCLUDES)/model/allBusinesses.h
	$(CC) $(CFLAGS) -o $(OBJ)/allBusinesses.o $(LIBS) -c $(SRC)/model/allBusinesses.c
allReviews: $(SRC)/model/allReviews.c $(INCLUDES)/model/allReviews.h
	$(CC) $(CFLAGS) -o $(OBJ)/allReviews.o $(LIBS) -c $(SRC)/model/allReviews.c

sgr: $(SRC)/model/sgr.c $(OBJ)/user.o $(OBJ)/business.o $(OBJ)/review.o $(OBJ)/allUsers.o $(OBJ)/allBusinesses.o $(OBJ)/allReviews.o
	$(CC) $(CFLAGS) -o $(OBJ)/sgr.o $(LIBS) -c $(SRC)/model/sgr.c
allsgr: $(OBJ)/sgr.o $(OBJ)/user.o $(OBJ)/business.o $(OBJ)/review.o $(OBJ)/allUsers.o $(OBJ)/allBusinesses.o $(OBJ)/allReviews.o
	$(CC) $(CFLAGS) $(OBJ)/user.o $(OBJ)/business.o $(OBJ)/review.o $(OBJ)/allUsers.o $(OBJ)/allBusinesses.o $(OBJ)/allReviews.o $(OBJ)/sgr.o $(LIBS) -o test


view: $(SRC)/view/ui.c $(INCLUDES)/view/ui.h
	$(CC) $(CFLAGS) -o $(OBJ)/ui.o $(LIBS) -c $(SRC)/view/ui.c
paginacao: $(SRC)/model/paginacao.c $(OBJ)/ui.o $(INCLUDES)/model/paginacao.h
	$(CC) $(CFLAGS) -o $(OBJ)/paginacao.o $(LIBS) -c $(SRC)/model/paginacao.c
main: $(SRC)/controller/main.c $(OBJ)/user.o $(OBJ)/business.o $(OBJ)/review.o $(OBJ)/allUsers.o $(OBJ)/allBusinesses.o $(OBJ)/allReviews.o $(OBJ)/sgr.o $(OBJ)/paginacao.o
	$(CC) $(CFLAGS) -o $(OBJ)/main.o $(LIBS) -c $(SRC)/controller/main.c

prog: $(OBJ)/main.o $(OBJ)/sgr.o $(OBJ)/paginacao.o $(OBJ)/ui.o $(OBJ)/user.o $(OBJ)/business.o $(OBJ)/review.o $(OBJ)/allUsers.o $(OBJ)/allBusinesses.o $(OBJ)/allReviews.o
	$(CC) $(CFLAGS) $(OBJ)/user.o $(OBJ)/business.o $(OBJ)/review.o $(OBJ)/allUsers.o $(OBJ)/allBusinesses.o $(OBJ)/allReviews.o $(OBJ)/sgr.o $(OBJ)/ui.o $(OBJ)/paginacao.o $(OBJ)/main.o $(LIBS) -o program

memcheck:
	valgrind --leak-check=full ./program

clean:
		rm -rf $(OBJ)/*.o program test