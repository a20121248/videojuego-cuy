javac Modelo/*.java Controlador/*java
jar cvf Modelo.jar Modelo/*.class  Controlador/*.class
javac -cp Modelo.jar Vista/*.java
jar cvfm MiPrograma.jar MANIFEST.MF Vista/*.class *.txt
java -jar MiPrograma.jar