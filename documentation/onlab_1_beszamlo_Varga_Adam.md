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

### **MySQL**

<div style="page-break-after: always;"></div>

## **Front-End**

<div style="page-break-after: always;"></div>

## **Back-End**

<div style="page-break-after: always;"></div>

## **Továbbfejlesztési lehetőségek**

<div style="page-break-after: always;"></div>

## **Összefoglalás**