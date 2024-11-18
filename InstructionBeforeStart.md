 ABBACI Juba : Projet 3 Tutoriel

 # Base de données (MariaDB)
    1- Créer une BDD que vous nommerez "projet3" sur votre serveur local (WAMPP ou XAMPP) grâce au fichier présent sur le projet (ressources/sql/script.sql). (Si le script n'est pas reconnu à cause de Timestamp, utiliser scriptSecours.sql).

    2- Assurez-vous que le port de MYSQL sur votre serveur local est bien 3306.

    3- Configurez un utilisateur autorisé à accéder à cette BDD sur PhpMyAdmin ainsi que les variables d'environnement si besoin.

    Note : Si vous utilisez MySQL et non MariaDB, veuillez le changer à la ligne 3 dans le fichier "src/main/ressources/application.properties"
    spring.datasource.url=jdbc:mysql://localhost:3306/projet3

 # Back-end (sur Eclipse)
    1- Après avoir importé le projet nommé "backend" :
        -> faites un clic droit dessus
        -> "Maven"
        -> "Update Project"

    2- Pour démarrer le back-end :
        -> Clic droit sur le projet
        -> Run As
        -> Maven build...
        -> Entrez la valeur "spring-boot:run" dans Goals, puis faites "Run"

    Note : Maven clean est conseillé pour nettoyer le projet avant de le relancer.

# Front-end (sur VS Code)
    1- Exécutez la commande "npm install"

    2- Exécutez la commande "npm run start"

    Rendez-vous sur l'URL http://localhost:4200/ !

    Note : Pour l'arrêter, CTRL C, puis "O", et Entrer.




    
