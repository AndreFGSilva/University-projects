-- Schema mydb
USE `mydb` ;
--
-- Permissão para fazer operações de remoção de dados.
SET SQL_SAFE_UPDATES = 0;

INSERT INTO `admin`
    (`email`, `password`)
    VALUES 
        ("admin@gmail.com","admin")
    ;

INSERT INTO `utilizador`
    (`email`, `nome`, `password`, `nivel`, `login`, `imagemURL`)
    VALUES 
        ("armando@gmail.com","Armando","123456","0",false,"./images/InitialAvatar.png"),
        ("joana@gmail.com","Joana","123456","276", false,"./images/InitialAvatar.png"),
        ("andre@gmail.com","Andre","123456","532", false,"./images/InitialAvatar.png"),
        ("joao@gmail.com","Joao","123456","1157", false,"https://lh3.googleusercontent.com/7ny0aqNK9w94E7VQlexohWzIjwtG8blTbZG8SFnCofARu7J_C8OWNKpEgYKc94i9yUggWSR-LK3w8eNrcXlX4RjcWKj9apOZecLUbw=w600")
    ;
    
INSERT INTO `rota`
    (`nome`) 
    VALUES 
        ("Rota do Centro"),
        ("Rota Gualtar"),
        ("Rota das Montanhas"),
        ("Rota Turistica")
    ;

INSERT INTO `utilizador_hashistorico_rota`
    (`Utilizador_email`, `Rota_nome`) 
    VALUES 
        ("joana@gmail.com","Rota Gualtar"),
        ("joao@gmail.com","Rota Gualtar"),
        ("joao@gmail.com","Rota das Montanhas"),
        ("andre@gmail.com","Rota Turistica")
    ;

INSERT INTO `utilizador_has_utilizador`
    (`Utilizador_email`, `Utilizador_email1`) 
    VALUES 
        ("joao@gmail.com","joana@gmail.com"),
        ("joao@gmail.com","andre@gmail.com"),
        ("andre@gmail.com", "joana@gmail.com")
    ;

INSERT INTO `pontointeresse`
    (`nome`, `descricao`, `cidade`, `coordenadas`,`imagemURL`,`valido`) 
    VALUES 
        ("Prometeu","Uma estatua","Braga","41.559550978259956, -8.397245077052588","https://lh5.googleusercontent.com/p/AF1QipN4ZWGRZsrtbu_8C4I2tu-wW7-MUFuAcxleBCWW=w408-h544-k-no",1),
        ("Sameiro","Uma igreja","Braga","41.54228249141272, -8.367789523533432","https://lh5.googleusercontent.com/p/AF1QipMDJf4W7B1f4oi1oO3vX0zjVlQkBD7Qyy2NUel1=w408-h306-k-no",1),
        ("Bom Jesus","Um edificio com um comboio","Braga","41.555631223306015, -8.374821777940548","https://lh5.googleusercontent.com/p/AF1QipOi12zzLARNlQyYNg7-EGcsitA1r4ffm4DrAaxt=w408-h306-k-no",1),
        ("Carrinha de Gualtar","Wtf uma carrinha!","Braga","41.571004867707636, -8.375128305346506","https://lh5.googleusercontent.com/p/AF1QipNRoR9impCW5wGh12s-4Y7yJe7H7hJbsnz9Lq7d=w408-h544-k-no",1),
        ("Termas Romanas","Umas pedras antigas","Braga","41.546880618677335, -8.42640991437258","https://lh5.googleusercontent.com/p/AF1QipMPxRGPfS-RAX9MpbMhMuh138PqF2U53KtuePsS=w426-h240-k-no",1),
        ("Cascata da Soutinha","Uma cascata com agua","Braga","41.545732026923886, -8.41197863539102","https://streetviewpixels-pa.googleapis.com/v1/thumbnail?panoid=JNlvXKM19w9IsIxR5eTkbQ&cb_client=search.gws-prod.gps&w=408&h=240&yaw=239.67993&pitch=0&thumbfov=100",1),
        ("Sé de Braga","Catedral bonita","Braga","41.55064540150164, -8.425433760040862","https://lh5.googleusercontent.com/p/AF1QipOrNJ_1zCVAZuq9ABjUm-tp_Pz7JC9Ps5YGx5bD=w408-h306-k-no",1),
        ("Miradouro Picoto","Uma local com boas vistas","Braga","41.53817343019713, -8.411893052832434","https://lh5.googleusercontent.com/p/AF1QipOGeDIVuPPgmFkRHGB43J76k22XDvrR-zDnH1jE=w408-h306-k-no",0),
        ("Palacio do Raio","Palacio antigo do estilo barroco","Braga","41.54872370594276, -8.421632061563022","https://lh5.googleusercontent.com/p/AF1QipO_BX94ahHiQUspUbGyNIgmSMa4IkEs3Q8cJ2-9=w408-h306-k-no",1),
        ("Torre dos Coimbras","Uma torre com vidros as cores","Braga","41.55017663105795, -8.42364704794341","https://lh5.googleusercontent.com/p/AF1QipN4VqZE5ejN4NWWwvwj7js2YamVQwTO3IPbtvfE=w408-h906-k-no",1),
        ("Museu Arqueologia","Museu com arqueologia local","Braga","41.5464455287023, -8.426222676360139","https://lh5.googleusercontent.com/p/AF1QipNfYcrCIoyGXR9kyLGoLrv0HOIjKAxB8xzOjg00=w408-h306-k-no",0),
        ("Arco da Porta Nova","Arco no centro de Braga","Braga","41.55007396043243, -8.429814942169138","https://lh5.googleusercontent.com/p/AF1QipNlIzQYSziL8z9TnMhE_KwotbxkKNM7Me6AHSQl=w408-h544-k-no",0),
        ("Monte Castro","Monte com vista para o estadio do Braga","Braga","41.56206736168623, -8.425120711345757","https://lh5.googleusercontent.com/p/AF1QipMvCm1lBMUP6a3EHoE0d3PZZQCKT_xa2ePRPuCQ=w408-h306-k-no",0),
        ("Braga Parque","Centro comercial","Braga","41.55822952614296, -8.404342371830175","https://lh5.googleusercontent.com/p/AF1QipMe-aoclZfqx1sTaBsaC9Owwsz7kj_fjMFJBpyD=w426-h240-k-no",1)
    ;


INSERT INTO `utilizador_hasfavorite_pontointeresse`
    (`Utilizador_email`, `PontoInteresse_nome`) 
    VALUES 
        ("joao@gmail.com","Carrinha de Gualtar"),
        ("joana@gmail.com","Bom Jesus"),
        ("andre@gmail.com","Sameiro"),
        ("andre@gmail.com","Braga Parque"),
        ("andre@gmail.com","Carrinha de Gualtar"),
        ("joao@gmail.com","Prometeu")
    ;

INSERT INTO `utilizador_hassugestao_pontointeresse`
    (`Utilizador_email`, `PontoInteresse_nome`) 
    VALUES 
        ("joao@gmail.com","Miradouro Picoto"),
        ("joao@gmail.com","Monte Castro"),
        ("andre@gmail.com","Arco da Porta Nova"),
        ("joana@gmail.com","Museu Arqueologia")
    ;

INSERT INTO `rota_has_pontointeresse`
    (`Rota_nome`, `PontoInteresse_nome`) 
    VALUES 
        ("Rota Gualtar","Carrinha de Gualtar"),
        ("Rota Gualtar","Bom Jesus"),
        ("Rota Gualtar","Sameiro"),
        ("Rota Gualtar","Prometeu"),
        ("Rota do Centro","Termas Romanas"),
        ("Rota do Centro","Torre dos Coimbras"),
        ("Rota do Centro","Palacio do Raio"),
        ("Rota das Montanhas","Sameiro"),
        ("Rota das Montanhas","Bom Jesus"),
        ("Rota Turistica","Torre dos Coimbras"),
        ("Rota Turistica","Palacio do Raio")
    ;

INSERT INTO `categoria`
    (`idCategoria`, `descricao`) 
    VALUES 
        (1,"Historico"),
        (2,"Religiao"),
        (3,"Cultural"),
        (4,"Natureza"),
        (5,"Comercial")
    ;

INSERT INTO `pontointeresse_has_categoria`
    (`PontoInteresse_nome`, `Categoria_idCategoria`) 
    VALUES 
        ("Carrinha de Gualtar",3),
        ("Bom Jesus",2),
        ("Bom Jesus",4),
        ("Bom Jesus",3),
        ("Sameiro",2),
        ("Sameiro",4),
        ("Prometeu",3),
        ("Termas Romanas",1),
        ("Cascata da Soutinha",4),
        ("Sé de Braga",1),
        ("Sé de Braga",3),
        ("Palacio do Raio",3),
        ("Palacio do Raio",1),
        ("Torre dos Coimbras",1),
        ("Braga Parque",5),
        ("Monte Castro",4),
        ("Arco da Porta Nova",1),
        ("Museu Arqueologia",3),
        ("Miradouro Picoto",4)
    ;

INSERT INTO `avaliacao`
    (`idAvaliacao`, `avaliacao`, `descricao`, `PontoInteresse_nome`, `Utilizador_email`) 
    VALUES 
        (1,5,"Bom lugar para um piquenique.","Carrinha de Gualtar","andre@gmail.com"),
        (2,4,"Muitas escadas, dificil acesso!","Bom Jesus","joana@gmail.com"),
        (3,4,"Muitas lojas e muito grande.","Braga Parque","andre@gmail.com"),
        (4,4,"Vista muito boa.","Sameiro","andre@gmail.com"),
        (5,3,"Agradavel.","Bom Jesus","andre@gmail.com"),
        (6,4,"Boa paisagem","Bom Jesus","joao@gmail.com"),
        (7,3,"Estatua bem conservada","Prometeu","joao@gmail.com"),
        (8,4,"Muitas escadas, mas o elevador é engraçado","Bom Jesus","joao@gmail.com")
    ;
