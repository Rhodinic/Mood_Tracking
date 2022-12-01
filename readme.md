## Problemstellung

Es soll eine Applikation entwickelt werden, mithilfe welcher jeden Tag die Stimmung des Benutzers verfolgt werden kann.
<br>
Es sollen jeden Tag folgenden Daten gespeichert werden:
- Stimmung (0-100)
- Wetterdaten des Tages (Temperatur, Luftfeuchtigkeit, Niederschlag, müssen nicht eingegeben werden)
- Ob an diesem Tag Schule / Arbeit war (y/n)
- Anstrengung in Schule / Arbeit (0-100)
- Menge an sozialer Interaktion (0-100)
- Besonderes Event (y/n, zBsp. Geburtstag, Weihnachten, Feier mit Freunden, ...)
  <br>
  Die dadurch gewonnen Daten können über eine eigene Anwendung ausgelesen und dargestellt werden.
  <br>
  Es sollen mehrere Benutzer gespeichert werden können.


## Lösungsvorschläge

Die Applikation welche die täglichen Stimmungs-Abfragen startet, sollte eine JavaFx-Anwendung sein.
<br>
Die Applikation zur Anzeige der täglich abgefragten Daten sollte ebenfalls in JavaFX geschrieben sein.
<br>
Für die Datenbank zum Speichern der Daten soll eine MySQL-Datenbank in einem Docker-Container verwendet werden.
