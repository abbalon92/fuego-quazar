# Usa una imagen base de OpenJDK
FROM openjdk:17

VOLUME /tmp

ADD ./target/fuegoquazar.jar /app/app.jar

# Establece el directorio de trabajo
WORKDIR /app

# Expone el puerto en el que se ejecuta la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
