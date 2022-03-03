namespace HermesTravel.Data
{
    public class Category 
    {
        public int idCategoria {get; set;}

        public String descricao {get; set;}

        public Category(int id, String description)
        {
            this.idCategoria = id;
            this.descricao = description;
        }

        public Category()
        {
            this.idCategoria = -1;
            this.descricao = "null";
        }
    }
}