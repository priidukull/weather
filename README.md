# USA linnade ilmportaal

### Instruktsioonid rakenduse paigaldamiseks

##### Eeldused

Java 8, PostgreSQL 9.4, Maven 3.2.5

##### Tegevused

Kõigepealt tuleb luua andmebaas ja tabelid.

    cd ProjectRoot
    createdb usweather
    psql usweather -f db/1.sql
    mvn package

Nüüd esimesena tuleb täita tabel city. Selleks loetakse sisu failist us_postal_codes.csv andmebaasi.

    mvn exec:java -Dexec.mainClass="Initiate"
    
Seejärel võib käima panna andmeid koguva protsessi...

    mvn exec:java -Dexec.mainClass="DataDaemon"
    
... ja veebirakenduse.

    mvn exec:java -Dexec.mainClass="spark.App"
    
Need instruktsioonid toimivad minu arendusarvutil (Mac OS). Serveris (Ubuntu) *mvn package* soovitud tulemust ei andnud. Kuna aeg ülesande tegemiseks on läbi, siis seda praegu rohkem uurida ei jõua, kuidas serveris programmi tööle saaks.

Projekti osa on alamkaust "ansible", mis sisaldab automaatskripti deploy tegemiseks (v.a. mvn package).

### Rakenduse kirjeldus

##### Stack

JDBC (Initialize), Hibernate (DataDaemon, App), Spark Framework (veebiraamistik, http://sparkjava.com/), PostgreSQL, Maven, Velocity Templating Language, Jackson (JSON Serialization), Bootstrap. 

##### Initialize

On tark klass nimega CSVLoader. CSVLoader initsialiseeritakse ning käivitatakse selles asuv meetod loadCSV(), et lugeda andmebaasi failis us_postal_codes.csv sisalduv.

##### DataDaemon

Protsess küsib andmebaasist andmed kõigi linnade kohta. Seejärel teeb linna zip-koodi kasutades iga linna ilma kohta päringu (klass WeatherApi). 

Kui andmed käes, siis neist tehakse üks suur JSON selle asemel, et iga andmepunkti kohta teha tabelis omaette tulp. Linna ilmaandmed salvestatakse tekstiväljale, kuid juhul kui neid andmeid get-meetodit kasutades küsitakse, saadakse vastuseks HashMap, kuna get-meetodi sees teisaldatakse tekst Map'iks.

Andmete töötlemine toimub klassis WeatherParser, kus kõigepealt võetakse algandmetest välja ilmaandmed imperial mõõdustikus ning seejärel teisendatakse imperial mõõdustikus olevad andmed meetermõõdustikku.
    
Pärast andmete uuendamist jääb protsess tunniks ajaks pausile, sest just nii pikk on algallika enese uuendamise tsükkel.

Juhul kui DataDaemoni töös ilmenb viga, siis andmete kogumine enne ei jätku, kui see viga on parandatud. Eesmärk on, et protsess ebaõnnestuks valjult ning võimalikud vead saaksid võimalikult kiirelt parandatud.

##### App

Veebirakendus on ehitatud kasutades Hibernate'i Spark Framework'i ja Velocity template'e. Veebirakendus koosneb ühest lehest, mille ülaosas on veebivorm ning alaosas ilmainfo, kui mõni linn on välja valitud, mille kohta infot näidata. Vormil saab valida 11 linna vahel ning iga linna andmeid saab näidata nii meeter- kui imperiaalmõõdustikus. Andmete uuendamiseks tuleb veebilehte värskendada või vorm uuesti saata. Andmete asünkroonset uuendamist ma ajapuudusel ei jõudnud teha.