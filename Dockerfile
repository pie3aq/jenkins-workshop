# Wybierz obraz bazowy z Docker Hub
FROM docker.io/openjdk:17-jdk-alpine

# Ustaw katalog roboczy
WORKDIR /app

# Skopiuj plik JAR aplikacji do katalogu /app w kontenerze
COPY target/*.jar /app/app.jar

# Zdefiniuj port, na którym będzie działała Twoja aplikacja
EXPOSE 8080

# Uruchom aplikację po uruchomieniu kontenera
CMD ["java", "-jar", "app.jar"]
