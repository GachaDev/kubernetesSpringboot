# Imagen base con Tomcat compatible con Java 17
FROM tomcat:9-jdk17

# Copiar el WAR de la aplicación dentro de Tomcat
COPY build/libs/crmInmobiliaria-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Exponer el puerto 8080
EXPOSE 8080

# Iniciar Tomcat
CMD ["catalina.sh", "run"]
