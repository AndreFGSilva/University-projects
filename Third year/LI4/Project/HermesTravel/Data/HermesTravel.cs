using System;

namespace HermesTravel.Data
{
    public class HermesTravel
    {
        public UserManager modelUsers { get; }
        public InterestPointManager modelInterestPoints { get; }

        public HermesTravel()
        {

        }

        public void configurarConta(string username, string newPW, string newImg)
        {
            User u = modelUsers.getUsers()[username];
            u.setAvatar(newImg);
            u.setPassword(newPW);
        }

        public Route consultRoute(string routeName)
        {
            Dictionary<string, User> users = modelUsers.getUsers();
            foreach (var v in users)
            {
                Dictionary<string, Route> routes = v.Value.getDRouteHistory();
                if (routes.ContainsKey(routeName))
                    return routes[routeName];
            }
            return null;
        }

        public List<string> getCities()
        {
            return modelInterestPoints.getCities();
        }
    }
}