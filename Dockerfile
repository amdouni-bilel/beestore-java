# Image de base avec JRE 17
FROM eclipse-temurin:17-jre-jammy

# Métadonnées
LABEL maintainer="contact@beedigital.tn"
LABEL version="1.0"
LABEL description="BeeStore - API de gestion de produits"

# Répertoire de travail
WORKDIR /app

# Copier le JAR déjà construit par Maven
COPY target/beestore-1.0.0.jar app.jar

# Port exposé (documentation, ne publie rien à lui seul)
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]
