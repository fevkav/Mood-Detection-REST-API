Dieses Projekt ist ein REST-basierter Webservice und dient als Backend für die Mood Detection App von Fevzi Kavalci. Sie wurde mithilfe Spring Boot, mit der IDE IntelliJ IDEA als Maven Projekt aufgesetzt.

In dieser Version:

- gibt es keine Unterscheidung von Mitarbeiter/Fachbereichsleiter und somit keine unterschiedlichen Ansichten.
- Man kann für die aktuelle, letzte und vorletzte Woche NUR einmal abstimmen. Keine Bearbeitung möglich.
- durch erstmaligen Start bekommt man einen Token und dieser wird in den nativen Speicher des Geräts bzw. local Storage des Browsers gespeichert.
- zwei Statistiken: Durchschnitt und Anzahl Stimmen


Installation:

1. .zip herunterladen oder Repository clonen.


2. Maven, Spring Boot installieren und Umgebungsvariablen setzen.

3. im Projekt-Ordner "mvn clean install" ausführen, um die Applikation zu bauen.


Um die Applikation mit beliebiger IDE zu starten:
"Projekt öffnen -> Projekt auswählen" und die Main-Klasse ausführen (...Application.java).

Oder:

Im Projekt-Ordner "mvn exec:java" ausführen.

Standardmäßig startet der Webservice auf Port 8080. Um diesen zu ändern, die Zeile "server.port=PORTNUMMER" in die Datei "src/main/resources/application.properties" einfügen.
