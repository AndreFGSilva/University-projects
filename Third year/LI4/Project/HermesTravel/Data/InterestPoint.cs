namespace HermesTravel.Data
{
    public class InterestPoint
    {
        public string nome { get; set; }

        public string cidade { get; set; }

        public string descricao { get; set; }

        public string imagemURL { get; set; }

        public string coordenadas { get; set; }

        public int valido { get; set; }
        public List<Evaluation> eval = new List<Evaluation>();

        //public Coordinates coordinates;

        public List<Category> categories = new List<Category>();

        // Constructor
        public InterestPoint(string name, string city, string description, string img, int valido, List<Evaluation> eval, string coord, List<Category> categories)
        {
            this.nome = name;
            this.cidade = city;
            this.descricao = description;
            this.imagemURL = img;
            this.valido = valido;
            this.eval = eval;
            this.coordenadas = coord;
            this.categories = categories;
        }

        public InterestPoint()
        {
        }

        public InterestPoint buildInterestPoint (List<Evaluation> eval, List<Category> cat, InterestPoint p){
            this.nome = p.nome;
            this.cidade = p.cidade;
            this.descricao = p.descricao;
            this.imagemURL = p.imagemURL;
            this.valido = p.valido;
            this.eval = eval;
            this.coordenadas = p.coordenadas;
            this.categories = cat;
            return this;
        }
    }
}