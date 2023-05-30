# Multitenant ötletkezelő alkalmazás Spring alapokon - Beszámoló

Önálló laboratórium 2 - 2022/23/2 

Varga Ádám Marcell

Konzulens: Forstner Bertalan
<hr>

## **Tartalom**

- [Bevezetés](#bevezetés)
    - [Motiváció](#motiváció)
    - [Megvalósítandó feladat](#megvalósítandó-feladatok)
- [Architektúra](#architektúra)
- [Felhasznált technológiák](#felhasznált-technológiák)
    - [Angular](#angular)
    - [Spring Boot (Kotlin)](#spring-boot-kotlin)
    - [MySQL](#mysql)
- [Front-End](#front-end)
    - [Nézetek](#nézetek)
- [Back-end](#back-end)
- [Továbbfejlesztési lehetőségek](#továbbfejlesztési-lehetőségek)
- [Összefoglalás](#összefoglalás)

<div style="page-break-after: always;"></div>

## **Bevezetés**

### **Motiváció**

Azért választottam ezt a témát, olyan témával akartam foglalkozni, amit továbbvihetek Diplomaterv 1 illetve 2 tárgyakra, mert eég nagy ahhoz, hogy 3 féléven keresztül tudjak benne mivel foglalkozni.

Emellett a témával lehetőségem van olyan dolokat kipróbálni, amikkel eddig nem foglalkoztam. Ilyen például a Spring Boot rendszer megismerése és a multitenant architektúra implementálása.

Emellett az megvalósítandó alkalmazás, ami egy akár ipari környezetben is használható ötletkezelő rendszer fejlesztését is érdekes feladatnak találtam, hiszen alapjaiban véve hasonló felépítésű (csak más célt szolgáló) rendzsereket ma már a szoftverfejlesztő cégek többsége használ a mindennapokban a feladatok kiosztásában. (Jira, Redmine, Trello)

### **Megvalósítandó feladatok**

A félévben a fő célként azt tűztem ki, hogy az alkalmazás alapjait fektetem le, hogy a Diplomaterv 1 illetve 2 tárgyak keretében a már működő rendzsert tudjam továbbfejleszteni új, speciálisabb feature-ökkel, modulokkal.

Ez alapján a félévre tervezett feladatok a következők voltak:

**Rendszer**

Az alkalmazás alapvetően 3 felhasználót támogat, a **USER**-t, a **JURY**-t és az **ADMIN**-t. Ezek a felhasználóknak különböző jogosultságaik vannak, amikkel az alkalmazás különböző részeihez férnek hozzá.

- **USER**

    A USER felhasználó az oldalon limitált képességekkel rendelkezik. A listázott ötletdobozokat látja, amiket meg tud nyitni. Ezekbe az ötletdobozokba létrehozhat új ötleteket. A saját ötletét szerkesztheti, illetve töröleti, de más ötletét csak megnyitni tudja. Az ötleteket Like-olhatja, illetve ha már Like-olta, akkor a Like-ot visszavonhatja. Az ötlet kommentjeit Like/Dislikeolhatja (a sajátját is) illetve új kommentet is létrehozat egy adott ötlethez. A saját kommentjét szerkeszteti.

    Ezen felül hozzáfér a User oldal első részéhez, ami listázza a saját ötleteket és kommenteket.

- **JURY**

    A JURY felhasználónak már több leetősége van interakcióba lépni az oldallal. A JURY felhasználók megkapják a USER felhasználók jogait, illetve új funkciókhoz is hozzájutnak. 
    - Ötletdobozok létrehozása
    - Ötletdobozokba érkező ötletek bírálása

    Ezen felül a JURY felhasználók hozzáférnek még a User oldal egy új részéhez is, ami listázza azokat az ötleteket, amikhez a felhasználó hozzá van rendelve mint kötelező bíráló, illetve azokat az ötleteket, amikhez már készített bírálatot.

- **ADMIN**

    Az ADMIN felhasználó az oldal Superusere, tehát minden joga megvan. Létrehozhat, szerkeszthet ötletdobozokat, a benne lévő ötleteket, kommenteket. A felhasználóknak ő állítja be a jogosultságait

**Biztonság**

A rendszernek mind Front-End-en, mind Back-end-en meg kell valósítani biztonsági intézkedéseket, amikkel csak a megfelelő jogusultságú felhasználók érhetnek el a jogaiknak megfelelő funkciókat.

<div style="page-break-after: always;"></div>

## Architektúra

Az alkalmazást webes környezetben valósítottam meg, és mivel több feladatot is ellát (illetve még a jövőben bővítve is lesz), ezért 3 rétegű architektúra megvalósítása mellett döntöttem.

**Megjelenítési réteg:** Angular 14

**Üzleti logikai réteg:** Spring Boot Kotlin nyelven

**Perzisztencia:** MySQL Adatbázis


Mind a kliens oldali Angular alkalmazásban, mind a szerver oldali Go alkalmazásban 
törekedtem az adott keretrendszer által diktált struktúrák és konvenciók megtartására.

<hr>

Ez a Back-End rétegben megvalósult. A szerver megvalósítja a Controller-Service-Repository mintát.

//TODO kép

A Controllerben vannak összegyűjtve a REST hívások, ezek a függvények hívják tovább az üzleti logikának a megvalósított függvényeit.

A Service-ben vannak az implementált függvények, amiket a Controller hív. Itt történik az adatok összeállítása, kiértékelése, a biztonság egy része.

A Repository mappa nyújtja a hidat az adatbázis és a JPA között.

<hr>

Front-End-en is törekedtem a helyes mappaszerkezet létrehozására, de az alkalmazás hamar túlnőtt az elsőre becsült méretén, így itt nem teljesült maradéktalanul a komponens alapú alkalmazás fejlesztés. Több komponens van amiket kisebb változásokkal újraimplementáltam. Ezeket általánosabb komponensekké ki lehetne vezetni.

<div style="page-break-after: always;"></div>

## **Felhasznált technológiák**

### **Angular**

**Angular**
Az Angular egy webes keretrendszer, amit a Google fejleszt. SPA
(Single Page Application) létrehozására lett kitalálva, ami azt jelenti, 
hogy a megjelenített nézeteket nem a szerver generálja és statikus 
oldalakként küldi a kliens számára, hanem kliens oldalon kerülnek 
generálásra, és ezek a generált oldalak és komponensek 
dinamikusan vannak beillesztve a DOM-ba (Document Object 
Model). Ennek köszönhetően az SPA alkalmazások nagyon gyorsak
és „mozgékonynak” tűnhetnek a végfelhasználók számára.
A fejlesztésük is egyszerű, mivel az Angular is komponenseken alapul. A komponensek kisebb 
alkotóelemei az oldalnak, amik akár önállóan is működhetnek. Ezekből a kisebb komponensekből 
áll elő az oldal. Ezeket a komponensek úgy vannak tervezve, hogy újra felhasználhatóak legyenek 
az alkalmazás több részében is, így jelentősen kevesebb (akár semmiféle) kódismétlést tartalmaz 
az oldal forráskódja

**Angular Material**

Az Angular Material szintén a Google által fejlesztett könyvtár, ami az Angular 
alkalmazásokhoz biztosít különböző előre létrehozott komponenst, amikkel a fejlesztés még 
gyorsabb és egyszerűbb. A könyvtár ezen felül egy nagyon jól definiált material designt is nyújt, 
ami könnyen használható a saját komponensek stílusozására is. 
Az alkalmazásban több komponens is Angular Material komponens, vagy abból lett átalakítva. 
Az egyedi stílusozást SCSS segítségével valósítottam meg, ahol erre szükség volt

### **Spring Boot (Kotlin)**

//TODO

### **MySQL**

A MySQL egy többfelhasználós, többszálú relációs 
adatbázis kiszolgálásához használható szerver. A kora 
ellenére még most is az egyi legelterjedtebb adatbázis 
szerver, ami gyorsaságának, és nyílt forráskódjának
köszönhető. A MySQL szerverek fejlesztése nagyon 
költséghatékony, emiatt rengeted keretrendszer 
támogatja a MySQL könnyű integrálását és ez alól a Go 
sem kivétel.

<div style="page-break-after: always;"></div>

## **Front-End**

### **Nézetek**

Az alkalmazás kliens oldala több nézetet is tartalmaz, amik között a felhasználó (a jogaitól függően) szabadon navigálhat.

**Login/Register oldal**

Ez az odlal fogadja először a felhasználót. Az alkalmazás használata felhasználói fiókhoz kötött, ha nem vagyunk belépve akkor az alkalmazás funkcióit nem tudjuk használni, az odlalakat nem tudjuk látogatni. Ha a felhasználó rendelkezik fiókkal, akkor az email címével és jelszavával be tud lépni az alkalmazása. Ha még nem rendelkezik fiókkal, akkor a Register gomb megnyomásával egy regisztráló oldalra irányítódik át, ahol a vezetéknév, keresztnév, email és jelszó megadását követően tud fiókot létrehozni. 

//TODO KÉP

**Ötletdoboz listázó oldal**

Ez az oldal tekinthető az alkalmazás főoldalának. Itt láthatóak az aktív ötletdobozok, melyekbe bárki ötletet írhat. Ezt a listázó doboz jobb fölső sarkában található **Create** gombbal tehetjük meg. A listázott ötletdobozokból kereshetünk is a keresőmező használatával, illetve a keresődoboz alján található lapozóval lapozhatunk is a találatok között, ha nem férnek ki az ötletdobozok egy oldalra. Az oldalak számát a felhasználó szabhatja meg (4, 8 illetve 12 ötletdoboz).

Ha adminként vagyunk belépve, akkor megnyithatjuk az ötletdobozokat menedzselő felületet.

//TODO KÉP

**Ötletdoboz menedzsekő oldal**

Ez egy egyszerő listázó oldal, ahol az adminok gyorsan és egyszerően hajthatnak végre műveleteket az ötletdobozokon.

A táblázatban az ötletdobozokat gyorsan törölhetjük, illetve megnyithatjuk a szerkesztő oldalukat. A törés gomb megnyomásakor egy felugró ablak figyelmeztet, hogy biztosan törölni akarjuk-e az ötletdobozt, és annak minden ötletét, az ötletek minden kommentjét.

//TODO KÉP

**Ötletdoboz Create/Edit**

Ez a felület két célt szolgál. Új ötletdobozt lehet itt felvenni, vagy egy már meglévő ötletdobozt szerkeszteni.

Az ötletdobozoknak Címet, leírást, kezdeti és zárási időpontot lehet adni. Ha admin felhasználóként vagyunk bejelentkezve, akkor ezen kívül még megjelenik egy táblázat, ami - hasonlóan mint az ötletdoboz menedzselő felületnél - egy gyors módot biztosít az ötletek szerkesztésére és törlésére.

//TODO KÉP

**Ötletdoboz**

Ezen a felületen láthatunk egy adott ötletdobozt és annak adatait, illetve a benne már leadott ötleteket. Az ötletek 4 nagyobb kategóriába sorolhatók.

- **SUBMITTED**

    Ezek a friss ötletek. Amikor egy felhasználó létrehoz egy ötletet, akkor az ebbe a kategóriába kerül.

- **REVIEWED**

    Ebbe a kategóriába akkor kerülnek az ötletek, ha az összes hozzárendelt bíráló lepontozta az ötletet. Ez az áthelyezés automatikus.

- **APPROVED/DENIED**

    A review során összegyűlt pontok alapján az admin dönthet úgy, hogy az adott ötlet megfelelő, ekkor elfogadja az ötletet. Ebben az esetben az ötlet átkerül az APPROVED státuszba. Ellenkező esetben az ötlet a DENIED státuszba kerül. Ez az áthelyezés nem automatikus.

Az admin felhasználó az ötletek státuszát direktben is állíthatja.

A felületen a már megszokott helyen létrehozhatunk új ötletet az ötletdobozba, illetve admin jogosultsággal az ötletdoboz szerkesztőfelülete is megnyitható.

//TODO KÉP

**Ötlet Create/Edit**

Hasonlóan mint az Ötletdoboznál, ez a felület és két célt szolgál, ötlet létrehozását és módosítását. A felületetn megadható az ötlet címe, leírása. Hozzáadhatunk már létező Tag-eket, illetve új Tag-eket is felvehetünk. Az ötlethez itt tudunk hozzárendelni Bírálókat.

//TODO KÉP

**Ötlet**

Ez a felület 4 alrészből áll, ezek: az Idea, a Details, a Comments illetve a Scores rész. Az első három részből a felületen mindig csak 1 látható, a Scores rész azonban a képernyő jobb szélén mindig megjelenik.

Az ötleteket lehet like-olni is a jobb fölső sarokban. Itt a like gomb mellett láthatjuk, hogy hány ember like-olta eddig és az utolsó likeoló nevét is láthatjuk. Abban az esetben, ha az utolsó lájkoló mi vagyunk, akkor a felület a "You"-t fogja kiírni. 

//TODO KÉP

- **Idea:**
    Az Idea fülön található a leírás, illetve a későbbiekben ide lehet még több adatot felvinni.

- **Details:**
    Itt találhatóak az alap adatai az ötletnek, mint például az hogy ki csinálta, mikor, és hogy éppen milyen státuszban van az ötlet. Itt látható, hogy milyen Tag-ekkel van ellátva, illetve mely bírálók vannak az ötlethez adva.

- **Comments:**
    Itt láthatóak az ötlethez fűzött kommentek. Ez az oldal részletesebb mint a többi, így ez a következő részben részletesen kifejtem.

**Comment**

Ez az aloldal arra hívatott, hogy az ötletekre lehessen kommenteket írni. Ezt az aloldal tetején lévő szövegdobozból tudjuk megtenni. Alatta láthatjuk a már létező kommenteket. Ezeket a kommenteket lehet like-olni. itt a likeolás és annak kiírása ugyanúgy működik, mint az ötleten lévő like rendszer. A felhasználó a saját maga által írt kommenteket tudja szerkeszteni. Ekkor a kommentnél megjelenik egy "Edited" szöveg, jelezve, hogy a komment szerkesztve lett. Az admin felhasználók bárkinek a kommentjét szerkeszthetik.

//TODO KÉP

**Score**

Ez az oldal valamilyen formában létrejött, de a kitűzött céloknak majdnem semmilyen formában nem felel meg, így ennek a szolgáltatásnak a fejlesztésére csak a következő félévben lesz lehetőség.

<div style="page-break-after: always;"></div>

## **Back-End**

<div style="page-break-after: always;"></div>

## **Továbbfejlesztési lehetőségek**

<div style="page-break-after: always;"></div>

## **Összefoglalás**
