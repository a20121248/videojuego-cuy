set path=%path%;D:\Program Files (x86)\Java\jdk1.8.0_60\bin
javac Modelo/*.java Controlador/*java
jar cvf Modelo.jar Modelo/*.class  Controlador/*.class
javac -cp Modelo.jar Vista/*.java
jar cvfm MiPrograma.jar MANIFEST.MF Vista/*.class *.txt
