Projet Spring Boot avec JaCoCo - Documentation
Objectif du Projet

L'objectif de ce projet est d'intégrer JaCoCo (un outil de couverture de code) dans une application Spring Boot en utilisant Maven. Ce guide documente les étapes nécessaires pour configurer Maven, Spring Boot et JaCoCo, puis générer un rapport de couverture de code après l'exécution des tests.

Étape 1 : Configuration du Projet Maven

Initialisation du Projet Maven

Tout d'abord, créez un projet Spring Boot avec Maven. Vous pouvez utiliser Spring Initializr ou un projet Maven existant. Le fichier clé pour la configuration de Maven est le pom.xml.

Configurer les Dépendances et Plugins dans le pom.xml

Ajoutez les dépendances nécessaires pour Spring Boot, JaCoCo, et les autres outils de développement et de tests :

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>0.8.11</version>
    </dependency>
</dependencies>


Configurer les Propriétés Java

Dans la section <properties>, assurez-vous de spécifier la version de Java utilisée pour le projet :

<properties>
    <java.version>17</java.version>
</properties>

Étape 2 : Ajouter et Configurer le Plugin JaCoCo

Ajouter JaCoCo au pom.xml

Ajoutez la configuration du plugin JaCoCo dans la section <build> du pom.xml :

<build>
    <plugins>
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.11</version>
            <executions>
                <execution>
                    <id>prepare-agent</id>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                    <configuration>
                        <propertyName>surefireArgLine</propertyName>
                        <destFile>${project.build.directory}/jacoco.exec</destFile>
                    </configuration>
                </execution>

                <execution>
                    <id>report</id>
                    <phase>verify</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>

                <execution>
                    <id>check</id>
                    <phase>verify</phase>
                    <goals>
                        <goal>check</goal>
                    </goals>
                    <configuration>
                        <rules>
                            <rule>
                                <element>BUNDLE</element>
                                <limits>
                                    <limit>
                                        <counter>INSTRUCTION</counter>
                                        <value>COVEREDRATIO</value>
                                        <minimum>0.50</minimum>
                                    </limit>
                                    <limit>
                                        <counter>BRANCH</counter>
                                        <value>COVEREDRATIO</value>
                                        <minimum>0.30</minimum>
                                    </limit>
                                </limits>
                            </rule>
                        </rules>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>


Cette configuration prépare JaCoCo pour les tests et génère un rapport après chaque exécution de tests.

Étape 3 : Exécution des Tests et Génération du Rapport JaCoCo

Exécuter les Tests et Générer le Rapport

Exécutez la commande suivante pour exécuter les tests et générer le rapport JaCoCo :

mvn clean install


Cela va :

Nettoyer le projet,

Compiler et tester le projet,

Générer un fichier de couverture de code jacoco.exec.

Accéder au Rapport JaCoCo

Le rapport généré sera dans le dossier target/site/jacoco/index.html. Ouvrez ce fichier dans votre navigateur pour consulter le rapport de couverture de code.

Étape 4 : Vérification du Seuil de Couverture

Vérifier le Seuil Minimum de Couverture

Le plugin JaCoCo peut vérifier si le projet atteint un certain seuil de couverture de code. Si le seuil est violé, la commande Maven échouera.

Voici comment configurer les seuils dans le plugin JaCoCo pour les instructions et les branches :

<execution>
    <id>check</id>
    <phase>verify</phase>
    <goals>
        <goal>check</goal>
    </goals>
    <configuration>
        <rules>
            <rule>
                <element>BUNDLE</element>
                <limits>
                    <limit>
                        <counter>INSTRUCTION</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.50</minimum> <!-- Seuil de couverture des instructions -->
                    </limit>
                    <limit>
                        <counter>BRANCH</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.30</minimum> <!-- Seuil de couverture des branches -->
                    </limit>
                </limits>
            </rule>
        </rules>
    </configuration>
</execution>


Résoudre les Erreurs de Couverture

Si les tests ne couvrent pas suffisamment de code, vous devrez ajouter des tests supplémentaires pour atteindre les seuils spécifiés.

Conclusion

Voici un guide détaillé sur la manière d'intégrer JaCoCo dans un projet Spring Boot à l'aide de Maven. Ce guide couvre l'ajout des dépendances nécessaires, la configuration de JaCoCo, et la génération des rapports de couverture de code. En suivant ces étapes, vous pourrez vous assurer que votre code est bien testé et couvert à un niveau adéquat.


<img width="1919" height="395" alt="image" src="https://github.com/user-attachments/assets/dda1af61-3f94-4e0c-ac21-c42e37e2ccde" />
