namespace HermesTravel.Data
{
    public class Evaluation 
    {   
        public int idAvaliacao {get; set;}
        public string descricao {get; set;}

        public float avaliacao {get; set;}

        public Evaluation(int id, string comment, float stars)
        {
            this.idAvaliacao = id;
            this.descricao = comment;
            this.avaliacao = stars;
        }

        public Evaluation()
        {
        }
    }
}