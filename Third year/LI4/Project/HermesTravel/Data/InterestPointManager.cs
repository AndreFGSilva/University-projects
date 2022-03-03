namespace HermesTravel.Data
{
    public class InterestPointManager
    {
        Dictionary<string, InterestPoint> interestPoints = new Dictionary<string, InterestPoint>();

        // Método: Adicionar Ponto de Interesse
        public void addPoint(InterestPoint p)
        {
            interestPoints.Add(p.nome, p);
        }

        // Método: Remover Ponto de Interesse
        public Boolean removePoint(string namePoint)
        {
            return interestPoints.Remove(namePoint);
        }

        // Método: Obter lista com todas as cidades
        public List<string> getCities()
        {
            List<string> cities = new List<string>();
            foreach (var p in interestPoints)
            {
                string city = p.Value.cidade;
                if (!cities.Contains(city))
                    cities.Add(city);
            }
            return cities;
        }

        // Método: Obter lista de todos os Pontos de Interesse
        public List<InterestPoint> consultInterestPoints()
        {
            List<InterestPoint> lp = new List<InterestPoint>();
            foreach(var p in interestPoints)
            {
                lp.Add(p.Value);
            }
            return lp;
        }
        
        /*
        //Método: Obter lista de todos os Pontos de Interesse filtrados
        public List<InterestPoint> consultFilteredInterestPoints(Filter f)
        {

        }
        */

        // Método: Obter Ponto de Interesse pedido
        public InterestPoint consultInterestPoint(string pointName)
        {
            return interestPoints[pointName];
        }

        // Método: Avaliar um Ponto de Interesse
        public void evaluateInterestPoint(string pointName, Evaluation eval)
        {
            interestPoints[pointName].eval.Add(eval);
        }
    }
}