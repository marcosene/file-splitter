# File Splitter Web Application
*Sistema Web para carga y split de un archivo cualquier (independiente de su formato) disponibilizando sus segmentos para download*

# Screenshots
* Página para cargar el archivo:
![Upload](https://github.com/marcosene/sovos-file-upload/blob/master/screenshots/upload.png)
* Página para descargar los archivos segmentados:
![Download](https://github.com/marcosene/sovos-file-upload/blob/master/screenshots/download.png)

# Características:
* El servidor divide los bytes del archivo cargado en N segmentos
* Los segmentos estarán en nuevos archivos disponibles para download
* Los archivos tendrán el mismo nombre del archivo original con extensión .000, .001, .002, ...
* La cantidad de archivos N es aleatoria entre 1 y 20.
* El último archivo contendrá los bytes restantes de la división, si hay.
  * Ejemplo: Si el archivo tiene 14 bytes y N=4
    * archivo.000 (3 bytes)
    * archivo.001 (3 bytes)
    * archivo.002 (3 bytes)
    * archivo.003 (3 bytes + 2 bytes restantes)
* Si el archivo está vacio (0 bytes), igual se genera un único archivo vacío con extensión .000

* Los archivos son persistidos en un directório temporal, que puede ser definido según una nueva variable de entorno *upload.path*.
  * Caso esa variable no sea definida, se ocupará automáticamente la variable de la JVM *java.io.tmpdir*

# Tecnologías utilizadas:
* Java 7
* Spring 4 / Spring Web-MVC
* Thymeleaf 3 / Thymeleaf-Spring
* HTML / CSS
* SLF4J (Logging)
* Jetty 9 (servidor para desarrollo)
* WAR (puede ser deployado en cualquier Application Server como Tomcat, JBoss, ...)

------------------------
# Manual de Instalación

1. Deployar el WAR generado *file-splitter.war* en un servidor de aplicación (Tomcat, JBoss, ...)
2. Agregar en JAVA_OPTS la variable de entorno *-Dupload.path=<temp_upload_dir>* para configuración del directório temporal de uploads
  * Caso esa variable no sea definida, se ocupará automáticamente la variable de la JVM *java.io.tmpdir*
3. La aplicación tendrá el contexto /file-splitter/


