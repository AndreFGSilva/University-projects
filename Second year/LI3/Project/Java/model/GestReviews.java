package src.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GestReviews implements Serializable {
    private Users users;
    private Businesses businesses;
    private Reviews reviews;
    private Stats stats;
    private StructuresStats structuresStats;

    public GestReviews() {
        this.users = new Users();
        this.businesses = new Businesses();
        this.reviews = new Reviews();
        this.stats = new Stats();
        this.structuresStats = new StructuresStats();
    }

    public GestReviews(Users users, Businesses businesses, Reviews reviews, Stats stats, StructuresStats structuresStats) {
        this.users = users;
        this.businesses = businesses;
        this.reviews = reviews;
        this.stats = stats;
        this.structuresStats = structuresStats;
    }

    public GestReviews(GestReviews gr) {
        this.users = gr.getUsers();
        this.businesses = gr.getBusinesses();
        this.reviews = gr.getReviews();
        this.stats = gr.getStats();
        this.structuresStats = gr.getStructuresStats();
    }

    // Getters
    public Users getUsers() {
        return this.users.clone();
    }

    public Businesses getBusinesses() {
        return this.businesses.clone();
    }

    public Reviews getReviews() {
        return this.reviews.clone();
    }

    public Stats getStats() {
        return this.stats.clone();
    }

    public StructuresStats getStructuresStats() {
        return this.structuresStats.clone();
    }

    // Leitura feita através de ficheiros de texto.
    public void loadSGRFromFiles(String usersFilePath, String businessesFilePath, String reviewsFilePath) {
        this.stats = new Stats(usersFilePath, businessesFilePath, reviewsFilePath);
        this.users = new Users();
        this.businesses = new Businesses();
        this.reviews = new Reviews();
        
        try {   // Adição das estruturas tem de ser tratada por um 'parse' respetivo
            System.out.print("\nUsers...");
            Files.lines(Paths.get(usersFilePath)).forEach(u -> this.stats.addUserStats(this.users.addUser(parseUser(u))));
            System.out.print(" DONE!!\nBusinesses...");
            Files.lines(Paths.get(businessesFilePath)).forEach(b -> this.stats.addBusinessStats(this.businesses.addBusiness(parseBusiness(b))));
            System.out.print(" DONE!!\nReviews...");
            Files.lines(Paths.get(reviewsFilePath)).forEach(r -> this.stats.addReviewStats(this.reviews.addReview(parseReview(r))));
            System.out.println(" DONE!!");
            this.structuresStats.finishMedia();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addStats();
    }

    public User parseUser(String input) {
        String[] fields = input.split(";");
        if (fields.length < 3)
            return new User();

        String id = fields[0]; 
        String userName = fields[1];
        return new User(id, userName);
    }

    public Business parseBusiness(String input) {
        List<String> categories = new ArrayList<>();
        String[] fields = input.split(";");
        if (fields.length < 4)
            return new Business();

        String businessID = fields[0]; 
        String businessName = fields[1];
        String businessCity = fields[2];
        String businessState = fields[3];
        int i = 0;
        if (fields.length > 4) {
            String[] fields_aux = fields[4].split(",");

            while(i < fields_aux.length -1){
                categories.add(fields_aux[i]);
                i++;
            }
        }
        return new Business(businessID, businessName, businessCity, businessState, categories);
    }

    public Review parseReview(String input) {
        String[] fields = input.split(";");
        if (fields.length < 8)
            return new Review();

        String reviewID = fields[0]; 
        String userID = fields[1];
        String businessID = fields[2];
        float stars;
        try {
            stars = Float.parseFloat(fields[3]);
        } catch (Exception NumberFormatException) {
            stars = -1;
        }

        int usefull;
        try {
            usefull = Integer.parseInt(fields[4]);
        } catch (Exception NumberFormatException) {
            usefull = -1;
        }

        int funny;
        try {
            funny = Integer.parseInt(fields[5]);
        } catch (Exception NumberFormatException) {
            funny = -1;
        }

        int cool;
        try {
            cool = Integer.parseInt(fields[6]);
        } catch (Exception NumberFormatException) {
            cool = -1;
        }
        
        Calendar date;
        try {
            date = convertDate(fields[7]);
        } catch (Exception NumberFormatException) {
            date = Calendar.getInstance();
        }

        String text = "";
        if (fields.length > 8)
            text = fields[8];
        
        if (!this.users.getTreeUsers().contains(new User(userID)))
            return new Review();

        if (!this.businesses.getTreeBusinesses().contains(new Business(businessID)))
            return new Review();

        if (usefull+funny+cool == 0)
            this.stats.addNoImpactReview();

        this.structuresStats.addRev(date.get(Calendar.MONTH));
        this.structuresStats.addMedia(date.get(Calendar.MONTH), stars);

        return new Review(reviewID, userID, businessID, stars, usefull, funny, cool, date, text);
    }

    public Calendar convertDate(String string) {
        String[] fields = string.split("-");
        String[] fields_aux = fields[2].split(" ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
        Calendar calendar = new GregorianCalendar(Integer.parseInt(fields[0]), Integer.parseInt(fields[1])-1, Integer.parseInt(fields_aux[0]));
        return calendar;
    }

    public void addStats() {
        TreeSet<String> bizs = new TreeSet<>();
        TreeSet<String> users = new TreeSet<>();

        for (Review r: this.reviews.getReviews()) {
            if (!bizs.contains(r.getBusinessID())) {
                bizs.add(r.getBusinessID());
                this.stats.addBusinessesReviewed();
            }
            if (!users.contains(r.getUserID())) {
                users.add(r.getUserID());
                this.stats.addUsersThatReviewed();
                this.structuresStats.addRevUser(r.getDate().get(Calendar.MONTH));
            }
        }
        this.stats.setBusinessesNotReviewed(this.stats.getValidBusinesses()-this.stats.getBusinessesReviewed());
        this.stats.setUsersThatDidntReview(this.stats.getValidUsers()-this.stats.getUsersThatReviewed());
    }
    
    // Guardar o estado do programa num ficheiro de objetos.
    public static void saveObject(GestReviews sgr, String fileName) throws IOException {
        System.out.print("\nSaving...");
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(sgr);
        oos.close();
        fos.close();
        System.out.println(" DONE!!");
    }

    // Leitura de um estado através de um ficheiro de objetos.
    public static GestReviews loadObject(String fileName) throws IOException, ClassNotFoundException {
        System.out.print("\nLoading...");
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        GestReviews loaded = (GestReviews) ois.readObject();
        ois.close();
        fis.close();
        System.out.println(" DONE!!");
        return loaded;
    }

    // *---- QUERYS ----*

    // Query 1
    //  - Lista ordenada alfabeticamente com os identificadores dos negócios nunca avaliados e o seu respetivo total.
    public List<String> query1() {
        TreeSet<String> list  = new TreeSet<>(this.reviews.setBusinessesIDs()); //Set com os ids de negocios avaliados
        return this.businesses.filter(b -> !list.contains(b.getBusinessID())).stream().map(Business :: getBusinessID).collect(Collectors.toList()); //Se o negocio nao tiver sido avaliado é adicionado
    }

    // Query 2
    //   - Dado um mês e um ano (válidos), determinar o número total de reviews realizadas e o número total de users distintos que as realizaram.
    public Pair query2(int month, int year) {
        int total = 0;
        TreeSet<String> ids = new TreeSet<>();
        for (Review r : this.reviews.getReviews())
            if (r.getDate().get(Calendar.YEAR) == year && r.getDate().get(Calendar.MONTH) == month-1) { //comparar a data de cada review
                total++; //adicionar ao total
                ids.add(r.getUserID());// adicionar ao set de ids
            }
        return new Pair(total, ids.size());
    }

    // Query 3
    //  - Dado um código de utilizador, determinar, para cada mês, quantas reviews fez, quantos negócios distintos avaliou e que nota média atribuiu.
    public TupleIIF query3(String userid) {
        int[] numRevs = new int[12];
        int[] difBizs = new int[12];
        float[] average = new float[12];
        List<Set<String>> ids = new ArrayList<>(12);

        for (int i = 0; i <= 11; i++)
            ids.add(i, new TreeSet<String>()); //inicializamos cada posição da lista com um set vazio

        for (Review r : this.reviews.getReviews())
            if (r.getUserID().equals(userid)) { //percorremos as reviews do utilizador dado
                numRevs[r.getDate().get(Calendar.MONTH)]++; //incrementamos o numero de reviews para o mes da review
                ids.get(r.getDate().get(Calendar.MONTH)).add(r.getBusinessID()); //adicionamos o id ao set de ids (set para nao ter repetidos)
                average[r.getDate().get(Calendar.MONTH)] += r.getStars(); //adicionamos as estrelas da review
            }

        for (int j = 0; j <= 11; j++) {
            difBizs[j] = ids.get(j).size(); //o size do set que contem os ids é o numero total de negocios diferentes avaliados
            average[j] = average[j]/numRevs[j]; //para fazer a media dividimos as estrelas que fomos guardando pelo numero total de reveiws nesse mes
        }
        return new TupleIIF(numRevs, difBizs, average);
    }
    
    // Query 4
    //  - Dado o código de um negócio, determinar, mês a mês, quantas vezes foi avaliado, por quantos users diferentes e a média de classificação.
    public TupleIIF query4(String businessid) {
        int[] numRevs = new int[12];
        int[] difUsers = new int[12];
        float[] average = new float[12];
        List<Set<String>> ids = new ArrayList<>(12);

        for (int i = 0; i <= 11; i++)
            ids.add(i, new TreeSet<String>());//inicializamos cada posição da lista com um set vazio

        for (Review r : this.reviews.getReviews())
            if(r.getBusinessID().equals(businessid)){
                numRevs[r.getDate().get(Calendar.MONTH)]++;//incrementamos o numero de reviews para o mes da review
                ids.get(r.getDate().get(Calendar.MONTH)).add(r.getUserID());//adicionamos o id ao set de ids (set para nao ter repetidos)
                average[r.getDate().get(Calendar.MONTH)] += r.getStars();//adicionamos as estrelas da review
            }
        

        for (int j = 0; j <= 11; j++) {
            difUsers[j] = ids.get(j).size();//o size do set que contem os ids é o numero total de users diferentes que avaliaram o business
            average[j] = average[j]/numRevs[j];//para fazer a media dividimos as estrelas que fomos guardando pelo numero total de reveiws nesse mes
        }

        return new TupleIIF(numRevs, difUsers, average);
    }

    // Query 5
    //  - Dado o código de um utilizador determinar a lista de nomes de negócios que mais avaliou (e quantos), ordenada por ordem decrescente de quantidade e, para quantidades iguais, 
    // por ordem alfabética dos negócios.
    public List<Map.Entry<String, Integer>> query5(String userid) {
        Map<String, Integer> result = new TreeMap<>();
        Map<String, String> businessinfo = new TreeMap<>();

        for (Business b : this.businesses.getTreeBusinesses())
            businessinfo.put(b.getBusinessID(), b.getName()); //associamos num mapa cada id de negocio com o seu nome

        for (Map.Entry<String,String> entry : businessinfo.entrySet())
            result.put(entry.getValue(), 0); //percorremos o mapa acima e adicionamos a um novo mapa que para cada nome de negocio associa as vezes que foi avaliado

        for (Review r : this.reviews.getReviews()) //percorremos todas as reviews para saber quais as que foram feitas pelo user
            if (r.getUserID().equals(userid))
                result.put(businessinfo.get(r.getBusinessID()), result.get(businessinfo.get(r.getBusinessID()))+1); //vamos ao mapa e para o nome do negocio que avaliou adicionamos +1 no total de reviews desse nome

        return result.entrySet().stream()
        .sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue()) == 0 ? e1.getKey().compareTo(e2.getKey()) : e2.getValue().compareTo(e1.getValue())) //ordenar por ordem decrescente e alfabetica
        .collect(Collectors.toList());
    }

    // Query 6
    //  - Determinar o conjunto dos X negócios mais avaliados (com mais reviews) em cada ano, indicando o número total de distintos utilizadores que o avaliaram (X é um inteiro dado 
    // pelo utilizador).
    public List<Map.Entry<Integer, Map<String, Integer>>> query6(int N) {
        Map<Integer, Map<String, Integer>> result = new HashMap<>(); //Map de retorno
        Map<String, Integer> businessInfo = new HashMap<>(); //Map com <Bus_ID, numero total de distintos utilizadores>
        Set<Integer> years = new TreeSet<>(); //Set de anos

        //Preeche o businessInfo com todos os bus_id a 0
        for (Business b: this.businesses.getTreeBusinesses())
            businessInfo.put(b.getBusinessID(), 0);

        //Preenche os years com os anos
        for (Review r : this.reviews.getReviews())
            years.add(r.getDate().get(Calendar.YEAR));

        //Preenche o result como <Ano, <Bus_ID, 0>>
        for(Integer y: years)
            result.put(y, cloneMap(businessInfo));

        //Atualiza o result com o numero total de distintos utilizadores
        for (Review r: this.reviews.getReviews()) {
            Map<String, Integer> innerMap = result.get(r.getDate().get(Calendar.YEAR));
            int howmany = innerMap.get(r.getBusinessID()) + 1;
            innerMap.put(r.getBusinessID(), howmany);
        }

        //Ordena e Corta o Map até N elementos
        for (Map.Entry<Integer,Map<String,Integer>> a : result.entrySet()) {
            Map<String,Integer> tmp;
            tmp = a.getValue().entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(N)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            a.setValue(tmp);
        }

        return result.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
    }

    Map<String,Integer> cloneMap(Map<String,Integer> a) {
        Map<String,Integer> newMAP = new HashMap<>();
        for (Map.Entry<String,Integer> e : a.entrySet())
            newMAP.put(e.getKey(),e.getValue());
        return newMAP;
    }

    // Query 7
    //  - Determinar, para cada cidade, a lista dos três mais famosos negócios em termos de número de reviews.
    public Map<String, List<Map.Entry<String, Integer>>> query7() {
        Map<String, List<Map.Entry<String, Integer>>> result = new HashMap<>();
        Map<String, Integer> map = new HashMap<>();
        Map<String, String> map_aux = new HashMap<>();

        for(Business b : this.businesses.getTreeBusinesses()){
            map_aux.put(b.getBusinessID(), b.getCity()); //mapear para cada id de negocio a sua cidade
        }
        
        for(Review r : this.reviews.getReviews()){
            if(map.containsKey(r.getBusinessID())){
                map.put(r.getBusinessID(), map.get(r.getBusinessID())+1); //mapear para cada negocio as vezes que foi avaliado
            }
            else{
                map.put(r.getBusinessID(), 1);
            }
        }

        for (Business b : this.businesses.getTreeBusinesses()){
            result.put(b.getCity(), (map.entrySet().stream().filter(v -> map_aux.get(v.getKey()).equals(b.getCity())).sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()) == 0 ? e1.getKey().compareTo(e2.getKey()) : e2.getValue().compareTo(e1.getValue()))
            .limit(3).collect(Collectors.toList()))); //mapear para cada cidade os negocios avaliados nessa cidade e ordenar pelo numero de reviews e fazer o top 3
        }

        return result;
    }

    // Query 8
    //  - Determinar os códigos dos X utilizadores (sendo X dado pelo utilizador) que avaliaram mais negócios diferentes, indicando quantos, sendo o critério de ordenação a ordem
    // decrescente do número de negócios.
    public List<Map.Entry<String, Integer>> query8(int X) {
        Map<String, Set<String>> u = new HashMap<>(); //Map com <User_ID, Set<Bus_ID>

        //Preenche o U com a informação necessária
        for (Review r: this.reviews.getReviews()) {
            if (u.get(r.getUserID()) == null) {
                Set<String> a = new TreeSet<>();
                u.put(r.getUserID(), a);
            }
            u.put(r.getUserID(), addBusiness(u.get(r.getUserID()), r.getBusinessID()));
        }

        //Transforma o U num Set
        u.forEach((k,v) -> u.merge(k, v, (v1,v2) -> Stream.concat(v1.stream(),v2.stream()).collect(Collectors.toSet())));

        //Ordena e corta até X elementos
        return u.entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().size()))
                .sorted((e1, e2) -> e2.getValue() - e1.getValue() == 0 ? e1.getKey().compareTo(e2.getKey()) : e2.getValue() - e1.getValue())
                .limit(X)
                .collect(Collectors.toList());
    }

    //Adiciona uma String< num Set<String>
    public Set<String> addBusiness(Set<String> buses, String bus) {
        buses.add(bus);
        return buses;
    }

    // Query 9
    //  - Dado o código de um negócio, determinar o conjunto dos X users que mais o avaliaram e, para cada um, qual o valor médio de classificação (ordenação cf. 5).
    public List<Map.Entry<String, PairF>> query9(String bizid, int X) {
        Map<String, PairF> result = new HashMap<>();
        Map<String, Integer> map = new HashMap<>();
        Map<String, Float> map_aux = new HashMap<>();

        for(Review r : this.reviews.getReviews()){
            if(r.getBusinessID().equals(bizid)){
                if(map.containsKey(r.getUserID())){ //percorrer as reviews e para as que foram do negocio recebido mapear o id do user com o numero de reviews feitas por esse user
                    map.put(r.getUserID(), map.get(r.getUserID()) + 1);
                    map_aux.put(r.getUserID(), map.get(r.getUserID()) + r.getStars()); //mapear para o user o numero total de stars que ele deu
                }
                else {
                    map.put(r.getUserID(), 1);
                    map_aux.put(r.getUserID(), r.getStars());
                }
            }
        }

        for(Map.Entry<String, Float> entry : map_aux.entrySet()){
            map_aux.put(entry.getKey(), entry.getValue() / map.get(entry.getKey())); //dividir o numero de vezes que avaliou um negocio pelo total de stars para ter a media
        }

        for(Map.Entry<String, Float> entry : map_aux.entrySet()){ //iterar os dois mapas criados acima
            for(Map.Entry<String, Integer> entry_2 : map.entrySet()){
                if(entry.getKey().equals(entry_2.getKey())){ //para o mesmo user criar um par  que contem o numero de vezes que avaliou e a media
                    PairF pair = new PairF(entry.getValue(), entry_2.getValue());
                    result.put(entry.getKey(), pair); //mapear para o result o id desse user e o par
                    break;
                }
            }
        }

        return result.entrySet().stream().sorted(Map.Entry.comparingByValue(new ComparatorQuery9())).limit(X).collect(Collectors.toList()); //comparar pelo numero de reviews e cortar pelo numero do stor
    }

    // Query 10 
    //  - Determinar para cada estado, cidade a cidade, a média de classificação de cada negócio.
    public List<Map.Entry<String, List<Map.Entry<String, List<Map.Entry<String, Float>>>>>> query10() {
        
        
        Map<String, Integer> id_NumberReviewed = new HashMap<>(); //Map que guarda <Bus_ID, Vezes classificada>
        Map<String, Float> id_Average = new HashMap<>(); //Map que guarda <Bus_ID, Média de classificação>

        Map<String, String> id_State = new HashMap<>(); //Map que guarda <Bus_ID, State>
        Map<String, String> id_City = new HashMap<>(); //Map que guarda <Bus_ID, City>

        List<Map.Entry<String, Float>> listOf_BusID_Average; //Lista de <Bus_ID, Média de classificação>
        List<Map.Entry<String, List<Map.Entry<String, Float>>>> listOf_City_BusID_Average; //Lista de <City, Lista<Bus_ID, Média de classificação>>

        Map<String, List<Map.Entry<String, Float>>> city_BusID_Average = new HashMap<>(); //Map que guarda <City, Lista<Bus_ID, Média de classificação>>
        Map<String, List<Map.Entry<String, List<Map.Entry<String, Float>>>>> result = new HashMap<>(); //Map de retorno que guarda <State, List<City, List<Bus_ID, Average>>>


        //Percorre os businesses e preenche os maps id_State e id_City 
        for (Business b : this.businesses.getTreeBusinesses()) {
            id_State.put(b.getBusinessID(), b.getState());
            id_City.put(b.getBusinessID(), b.getCity());
        }


        //Percorre as reviews e preenche os maps id_NumberReviewed e id_Average(apenas a soma das classificações e mais tarde calcula a média)
        for (Review r : this.reviews.getReviews()) {
            if (id_NumberReviewed.containsKey(r.getBusinessID())) {
                id_NumberReviewed.put(r.getBusinessID(), id_NumberReviewed.get(r.getBusinessID()) + 1);
                id_Average.put(r.getBusinessID(), id_Average.get(r.getBusinessID()) + r.getStars());
            }
            else {
                id_NumberReviewed.put(r.getBusinessID(), 1);
                id_Average.put(r.getBusinessID(), r.getStars());
            }
        }
        
        //Calcula a média para o id_Average
        id_Average.replaceAll((k, v) -> v / id_NumberReviewed.get(k));

        listOf_BusID_Average = new ArrayList<>(id_Average.entrySet());

        //Através da lista id_Average, cria um Map <City, <Id, Average>> 
        for (Map.Entry<String, Float> entry : listOf_BusID_Average) {
            if (!city_BusID_Average.containsKey(id_City.get(entry.getKey()))) {
                city_BusID_Average.put(id_City.get(entry.getKey()), new ArrayList<>());
            }

            city_BusID_Average.put(id_City.get(entry.getKey()), addToMap(city_BusID_Average.get(id_City.get(entry.getKey())), entry));

        }

        listOf_City_BusID_Average = new ArrayList<>(city_BusID_Average.entrySet());
        
        //Através da lista city_busID_Average, cria um Map <State, <City, <Id, Average>>> 
        for(Map.Entry<String, List<Map.Entry<String, Float>>> entry : listOf_City_BusID_Average){
            for( Map.Entry<String, Float> entry_2 : entry.getValue()){
                if (!result.containsKey(id_State.get(entry_2.getKey()))){
                    result.put(id_State.get(entry_2.getKey()), new ArrayList<>());
                }

                result.put(id_State.get(entry_2.getKey()), addToMap2(result.get(id_State.get(entry_2.getKey())), entry));

            }
        }

        return new ArrayList<>(result.entrySet());
    }


    //Adiciona a uma Lista de <Bus_ID, Média de classificação> um <Bus_ID, Média de classificação>
    public List<Map.Entry<String, Float>> addToMap(List<Map.Entry<String, Float>> list, Map.Entry<String, Float> entry) {
        list.add(entry);
        return list;
    }

    //Adiciona a uma Lista de <City, Lista<Bus_ID, Média de classificação>> um <City, Lista<Bus_ID, Média de classificação>>
    public List<Map.Entry<String, List<Map.Entry<String, Float>>>> addToMap2(List<Map.Entry<String, List<Map.Entry<String, Float>>>> list, Map.Entry<String, List<Map.Entry<String, Float>>> entry) {
        list.add(entry);
        return list;
    }

}