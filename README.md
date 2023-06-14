# backlink2

Dieses Programm soll in der Lage sein festzustellen, welche der im Internet gespeicherten Backlinks auch auf der neuen Seite erfolgreich geroutet werden können. Mit dem Plugin [Redirection](https://de.wordpress.org/plugins/redirection/) sollen Umleitungen erstellt werden, die dann mit diesem Programm bequem überprüft werden können.
Hier findet man eine schöne Erklärung zum Thema: [Backlink](https://www.searchmetrics.com/de/glossar/backlink/).

## status codes
Bei einer Messung am 14ten Juni 2023 wurde folgender Output generiert

```
Folgende Anwort-Codes wurden gefunden: (Gesamt-Anzahl der Backlinks: 177)
Antwort-Code: 307, Anzahl : 23 (12,99%)
Antwort-Code: 404, Anzahl : 87 (49,15%)
Antwort-Code: 200, Anzahl : 61 (34,46%)
Antwort-Code: 504, Anzahl : 2 (1,13%)
Antwort-Code: 301, Anzahl : 2 (1,13%)
Antwort-Code: 302, Anzahl : 2 (1,13%)
```
Die folgende Tabelle listet die Bedeutung der gefundenen Status Codes auf.
| Antwort-Code  |  Nachricht   |  Bedeutung  | aktuelle Messung ([test.it-sicherheit.de](http://test.it-sicherheit.de))
| ------------- | ------------- | ------------- |------------- |
| 307  |  Temporary Redirect | Die angeforderte Ressource steht vorübergehend unter der im „Location“-Header-Feld angegebenen Adresse bereit. Die alte Adresse bleibt gültig. Der Browser soll mit derselben Methode folgen wie beim ursprünglichen Request (d. h. einem POST folgt ein POST). Dies ist der wesentliche Unterschied zu 302/303.   | 23 (12,99%) |
| 404  | Not Found | Die angeforderte Ressource wurde nicht gefunden. Dieser Statuscode kann ebenfalls verwendet werden, um eine Anfrage ohne näheren Grund abzuweisen. Links, die auf solche Fehlerseiten verweisen, werden auch als Tote Links bezeichnet. „404“ gilt zudem als verbreitetes Meme.   | 87 (49,15%) |
| 200  | OK | Die Anfrage wurde erfolgreich bearbeitet und das Ergebnis der Anfrage wird in der Antwort übertragen.  | 61 (34,46%) |
| 504  | Gateway Timeout | Der Server konnte seine Funktion als Gateway oder Proxy nicht erfüllen, weil er innerhalb einer festgelegten Zeitspanne keine Antwort von seinerseits benutzten Servern oder Diensten erhalten hat.   | 2 (1,13%) |
| 301  | Moved Permanently | Die angeforderte Ressource steht ab sofort unter der im „Location“-Header-Feld angegebenen Adresse bereit (auch Redirect genannt). Die alte Adresse ist nicht länger gültig.  | 2 (1,13%) |
| 302  | Found (Moved Temporarily)  |Die angeforderte Ressource steht vorübergehend unter der im „Location“-Header-Feld angegebenen Adresse bereit.[6] Die alte Adresse bleibt gültig. Die Browser folgen meist mit einem GET, auch wenn der ursprüngliche Request ein POST war. Wird in HTTP/1.1 je nach Anwendungsfall durch die Statuscodes 303 oder 307 ersetzt. 302-Weiterleitung ist aufgrund eines Suchmaschinen-Fehlers, des URL-Hijackings, in Kritik geraten.  | 2 (1,13%) |

## aktuelles Output-File
TODO: In Zukunft sollten zwei verschiedene Output-Files bereitgehalten werden.
[aktuelles Output-File](https://github.com/Mschnuff/backlink2/blob/main/backlink_checker/output.txt)
