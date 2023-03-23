# Multitenant ötletkezelő alkalmazás Spring alapokon - Specifikáció

version: 1.0

## Tartalomjegyzék

- [Leírás](#leírás) <br>
- [Stack](#stack) <br>
- [Részletes leírás](#részletes-leírás) <br>
  - [Adatmodell](#adatmodell) <br>

## Leírás

Az alkalamzás egy célja egy ötletkezelő rendszer megvalósítása. Ebvben a rendszerben a felhasználók egy weboldalon a saját ötleteikkel járulhatnak hozzá egy jövendőbeli projekt vagy cél sikeréhez. Az oldalon lévő ötletládákba szabadon helyezhetnek el ötleteket, vagy akár saját ötletládát is nzithatnak.

Az ötletládákat egy erre a célra kialakított bizottság értékeli és pontozza bizonyos szempontok szerint és ez alapján az ötlet vagy elfogadásra, vagy elutasításra kerül.

## Stack

**Front-End** - Angular

Az alkalmazás Front-End keretrendszerének Angulart választottam, mivel ez egy moder front-end keretrendszer, ami kiválóan támogatja a komponens alapú fejlesztést. Ezenkívül sok olyan funkció, amit az alkalmazás használni fog már eleve be van építve a keretrendszerbe, így azokat csak használni kell majd.

Megjelenésben az Angular Material könyvtárat fogom használni, mivel ebben a könyvtárban a legtöbb komponens, amit egy akár üzleti szintű alkalmazás is használna implementálva van. Ezen felül használata rendkívül felhasználóbarát és egyszerű és nem mellesleg az elkészült felület is letisztult és egységes lesz.

---

**Back-End** - Spring

Az alkalmazás szerver oldalát egy Spring szerver fogja biztosítani. A Spring már elég régen használt keretrendszer, ami minden feladatot el tud látni, amire az alkalmazás során szükség lehet és nagyon felhasználó barát módon lehet benne fejleszteni.

A szerver Kotlin nyelven íródik, ezzel is lépést tartva a korral. A Kotlin ezen felül rengeteg olyanb kényelmi és biztonsági funkcióval rendelkezik, ami fejlesztés könnyíti

---

**Data Base** - MySQL

Az alkalmazás adatbázisát MySQL-ben tervezem megvalósítani, mivel ez egy jól bevált megoldás a relációs adatbázisok körében és remekül lehet Spring-el használni.

## Részletes Leírás

### Adatmodell

![Alt text](./images/data_model.png?raw=true "Title")

A fenti képen látható, hogy az alkalmazás az

- [Idea Box](#idea-box)
- [Idea](#idea)
- [User](#user)
- [Score](#score)
- [Comment](#comment)
- [Tag](#tag)

objektumokat fogja használni.

---

#### Idea Box

Az ötletek gyűjtő doboza. Ezt a minden jogosultsággal rendelkező felhasználó létrehozhatja.

A látogató és bíráló jogosultsággal rendelkező felhasználók csak a saját dobozukat szerkeszthetik, mások által készített dobozokat nem.

Az admin felhasználók bármely dobozt szerkeszthetik vagy törölhetik.

---

#### Idea

Az ötleteket a felhasználók azokba a dobozokba helyezhetik, ahova van jogosultságuk ehhez. Az ötleteket akkor lehet elhelyezni a dobozokban, ha a doboz már megnyílt, de még nem zárult le. A dobozokban az ötleteket a nyitott szakaszban bármely felhasználó Like-olhatja, illetve kommentet fűyhet hozzá.

Zárás után az ötleteke a bíráló felhasználók értékelik a megadott szempontok szerint. Ha minden szükséges bíráló pontozta az ötletet, akkor a pontok kiértékelődnek és egy összesített pontszám adja meg, hogy az ötlet elfogadásra vagy elutasításra kerül.

A látogató és bíráló felhasználó jogosultsággal rendelkező felhasználók csak a saját ötletüket szerkeszthetik, mások által készített ötletet nem.

Admin felhasználók bármely ötletet szerkeszthetik vagy törölhetik

---

#### User

A felhasználók többféle jogosultságí típusba tartozhatnak. Az oldal támogat látogatói jogosultságot, bíráló jogosultságot és admin jogosultságot

---

#### Score

A bíráló jogosultsággal rendelkező felhasználók az ötletdoboz lezárását követően pontozzák a dobozban az ötleteket. Ehhez többféle pontozási stratégiát is használniuk kell. Lehet csúszka, ami 1-től 10-ig pontoz, lehet csillagos pontozás 1-től 5ig, illetve lehet egyedi pontozási stratégia is, ahol szöveges dobozokból kell kiválasztani, hogy melyik illik az adott témára legjobban.

Ezeket az egyéni pontozásokat az időszak lezárulta után összegzi a rendszer és létrehoz egy végső pontszámot, amiből kiderül, hogy az ötlet elfogadásra vagy elutasításra kerül.

---

#### Comment

Kommenteket bármely felhasználó létrehozhat egy ötlethez. Itt egy szöveges üzenet jelenik meg, illetve hogy ki készítette a kommentet és mikor. Ugyan úgy mint az ötleteket, a kommenteket is lehet Like-olni.
