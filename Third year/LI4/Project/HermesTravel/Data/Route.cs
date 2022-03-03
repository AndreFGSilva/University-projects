namespace HermesTravel.Data
{
    public class Route
    {
        public string nome { get; set; }

        public List<InterestPoint> points = new List<InterestPoint>();

        // Constructor
        public Route (string routeName, List<InterestPoint> p)
        {
            this.nome = routeName;
            this.points = p;
        }

        // Método: Adiciona Ponto de Interesse
        public void addPoint(InterestPoint p) 
        {
            points.Add(p);
        }

        // Método: Remove Ponto de Interesse
        public Boolean removePoint(InterestPoint point)
        {
            return this.points.Remove(point);
        }
    }
}