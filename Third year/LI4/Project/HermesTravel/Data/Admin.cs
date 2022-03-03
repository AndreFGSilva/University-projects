using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace HermesTravel.Data
{
    public class Admin
    {
        public string email { get; set; }

        public string password { get; set; }

        public List<InterestPoint> interestPoints = new List<InterestPoint>();

        public Admin(string email, string password, List<InterestPoint> points)
        {
            this.email = email;
            this.password = password;
            this.interestPoints = points;
        }

        public Admin()
        {
            this.email = "";
            this.password = "";
            this.interestPoints = new List<InterestPoint>();
        }

        public void buildAdmin (string email, string password, List<InterestPoint> points){
            this.email = email;
            this.password = password;
            foreach (var p in points){
                this.interestPoints.Add(p);
            }
        }

        public bool hasPoint(string s){
            foreach (var p in this.interestPoints){
                if(p.nome.Equals(s)){
                    return true;
                }
            }
            return false;
        }

        public void removePoint(string s){
            foreach (var p in this.interestPoints){
                if(p.nome.Equals(s)){
                    this.interestPoints.Remove(p);
                    break;
                }
            }
        }

        public void acceptPoint(string s){
            InterestPoint po = new InterestPoint();
            List<InterestPoint> pontos = new List<InterestPoint>();
            foreach (var p in this.interestPoints){
                if(p.nome.Equals(s)){
                    po = p;
                }
                else{
                    pontos.Add(p);
                }
            }
            pontos.Add(po);
            this.interestPoints = pontos;
        }

        public bool isPontoNaoValido (string s){
            foreach (var p in this.interestPoints){
                if(p.nome.Equals(s)){
                    return (p.valido == 0);
                }
            }
            return false;
        }

        public InterestPoint getPonto (string s){
            foreach (var p in this.interestPoints){
                if(p.nome.Equals(s)){
                   return p;
                }
            }
            return new InterestPoint();
        }

        public void deleteAdmin()
        {
            this.interestPoints.Clear();
        }

    }
}