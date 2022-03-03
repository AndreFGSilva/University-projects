namespace HermesTravel.Data
{
    public class UserManager 
    {
        Dictionary<string, User> users = new Dictionary<string, User>();

        // Getters
        public Dictionary<string, User> getUsers()
        {
            return users;
        }

        // Método: Obter lista de amigos
        public List<User> consultFriends(string username) 
        {   
            List<string> friendsName = new List<string>();
            friendsName = users[username].getFriends();
            List<User> friends = new List<User>();
            foreach (var s in friendsName) 
            {
                friends.Add(users[s]);
            }
            return friends;
        }

        // Método: Obter lista de pontos favoritos
        public List<InterestPoint> consultFavouritePoints(string username) 
        {   
            return users[username].getFavouritePoints();
        }

        // Método: Obter um dicionário com as rotas dos amigos
        public Dictionary<User, List<Route>> consultFriendRouteHistory(string username)
        {
            List<User> myFriends = consultFriends(username);
            Dictionary<User, List<Route>> friendsRoutes = new Dictionary<User, List<Route>>();
            foreach (var f in myFriends)
            {
                friendsRoutes.Add(f, f.getRouteHistory());
            }
            return friendsRoutes;
        }  
    }
}