using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HermesTravel.Data
{
    public class User
    {
        public string email { get; set; }

        public string password { get; set; }

        public string nome { get; set; }

        public string imagemURL { get; set; }

        public int nivel { get; set; }

        public Boolean login { get; set; }

        public List<string> friends = new List<string>();

        public List<InterestPoint> suggestionsPoints = new List<InterestPoint>();

        public List<InterestPoint> favouritePoints = new List<InterestPoint>();

        public Dictionary<string, Route> routeHistory = new Dictionary<string, Route>();

        public User(string email, string password, string name, string avatar, int level, bool loggedin)
        {
            this.email = email;
            this.password = password;
            this.nome = name;
            this.imagemURL = avatar;
            this.nivel = level;
            this.login = loggedin;
            this.favouritePoints = new List<InterestPoint>();
            this.suggestionsPoints = new List<InterestPoint>();
            this.routeHistory = new Dictionary<string, Route>();
        }

        public User(string email, string password, string name)
        {
            this.email = email;
            this.password = password;
            this.nome = name;
            this.imagemURL = "./images/InitialAvatar.png";
            this.nivel = 0;
            this.login = false;
        }

        public User()
        {
            this.email = "";
            this.password = "";
            this.nome = "";
            this.imagemURL = "";
            this.nivel = 0;
            this.login = false;
        }

        public void buildUser(List<string> friends, List<InterestPoint> favouritePoints, List<InterestPoint> suggestionPoints, List<Route> routeHistory, User u)
        {
            foreach (var f in friends)
            {
                this.friends.Add(f);
            }

            foreach (var fp in favouritePoints)
            {
                this.favouritePoints.Add(fp);
            }

            foreach (var sp in suggestionPoints)
            {
                this.suggestionsPoints.Add(sp);
            }

            foreach (var rh in routeHistory)
            {
                this.routeHistory.Add(rh.nome, rh);
            }

            this.email = u.email;
            this.password = u.password;
            this.nome = u.nome;
            this.imagemURL = u.imagemURL;
            this.nivel = u.nivel;
            this.login = u.login;
        }

        // Getters
        public List<string> getFriends()
        {
            return friends;
        }

        public List<InterestPoint> getFavouritePoints()
        {
            return favouritePoints;
        }

        public Dictionary<string, Route> getDRouteHistory()
        {
            return routeHistory;
        }

        // Setters
        public void setPassword(string password)
        {
            this.password = password;
        }

        public void setAvatar(string avatar)
        {
            this.imagemURL = avatar;
        }

        // Método: Sobe o nível do utilizador
        public void levelUP()
        {
            this.nivel = this.nivel + 1;
        }

        // Método: Adicionar amigo
        public void addFriend(string friend)
        {
            friends.Add(friend);
        }

        // Método: Remover amigo
        public Boolean deleteFriend(string friend)
        {
            return friends.Remove(friend);
        }

        // Método: Adicionar sugestão de ponto de interesse
        public void addSuggestionPoint(InterestPoint suggestionPoint)
        {
            suggestionsPoints.Add(suggestionPoint);
        }

        // Método: Adicionar ponto de interesse favorito
        public void addFavouritePoint(InterestPoint favouritePoint)
        {
            favouritePoints.Add(favouritePoint);
        }

        // Método: Remover ponto de interesse favorito
        public Boolean removeFavouritePoint(InterestPoint favouritePoint)
        {
            return favouritePoints.Remove(favouritePoint);
        }

        // Método: Remover ponto de interesse favorito
        public Boolean removeFavouritePoint2(String favouritePoint)
        {
            foreach (var p in this.favouritePoints)
            {
                if (p.nome.Equals(favouritePoint))
                {
                    this.favouritePoints.Remove(p);
                    return true;
                }
            }
            return false;
        }

        // Método: Adicionar rota ao histórico do utilizador
        public void addRoute(Route route)
        {
            routeHistory.Add(route.nome, route);
        }

        // Método: Remover rota do histórico de rotas do utilizador
        public Boolean removeRouteHistory(string routeName)
        {
            return routeHistory.Remove(routeName);
        }

        // Método: Obter a lista com todas as rotas do histórico do utilizador
        public List<Route> getRouteHistory()
        {
            List<Route> routes = new List<Route>();
            foreach (var r in routeHistory)
                routes.Add(r.Value);
            return routes;
        }

        // Método: Calcula fase de nível atual, percentagem atual em string e número
        public (int, string, string) calculateLevel()
        {
            int xp = this.nivel;
            int base_xp = 1;
            int mult = 2;
            int currentLevel = 0;

            while (xp > 0)
            {
                xp = xp - (base_xp * mult);
                currentLevel++;
                base_xp = base_xp * mult;
            }
            float percentage = 0;
            int inteiro = 0;
            if (xp != 0)
                percentage = (float)1 - ((-xp) / (float)(base_xp));
            percentage = percentage * 100;
            inteiro = (int)percentage;
            string answer = inteiro.ToString();
            string prc = answer;
            answer = answer + "%";

            return (currentLevel, answer, prc); // CurrentLevel, percentagem da barra
        }

        public void addXP(int xp)
        {
            this.nivel+=xp;
        }

        // Método: Apaga o utilizador (logout).
        public void deleteUser()
        {
            this.favouritePoints.Clear();
            this.friends.Clear();
            this.routeHistory.Clear();
            this.suggestionsPoints.Clear();
        }
    }
}