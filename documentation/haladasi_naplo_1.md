# Multitenant ötletkezelő alkalmazás Spring alapokon - Haladási napló

Önálló laboratórium 2 - 2022-23/2 félév

## 1. hét

Február 27. - Március 5.

Ismerkedés az ötletkezelő alkalmazásokkal. <br>
https://www.acceptmission.com webes ötletkezelő alkalmazáson regisztrálás, belső működés vizsgálata, ötletgyűjtés.

## 2. hét

Márc 6. - Március 12.

Ezen a héten elkezdtem az alkalmazás felületét és az adatstruktúráját felvázolni.

A kinézetet figmában kezdtem el megtervezni wireframezéssel.

Az adatmodellt a draw.io oldalon kezdtem el rajzolni.

## 3. hét

Március 13. - Március 19.

Tervek finomítása, új feature-ök bevezetése mind a modell tervébe, mind a UI tervbe.

## 4. hét

Március 20. - Március 26.

Ezen a héten elkezdtem elkészíteni a front-end és back-end alkalmazás alap struktúráját, illetve létrehoztam a github repot- ahol a projektet verziókezelem.

A fron-end inicializáló angular project-be behúztam az Angular Materialt UI keretrendszernek.

A back-end inicializáló projektet a Spring Initializr (https://start.spring.io/) segítségével készítettem. Ebben készítettem egy alap endpointot, amivel a fron-end elérheti a backendet, illetve összekötöttem egy MySQL adatbázissal, hogy az adatok menthetővé váljanak.

## 5. hét

Március 27. - Április 2.

Ezen a héten főleg a back-enddel foglalkoztam. Létrehoztam a már megtervezett modellt a Spring JPA segítségével. Továbbá kialakítottam itt is egy rendezett mappaszerkezetet, ahol a felelősségek jól el vannak határolva.

A modellnek létrehoztam egy alap CRUD műveleteket ellátó controllert és service-t is.

## 6. hét

Április 3. - Április 9.

Az alkalmazást ezen a héten főleg front-enden fejlesztettem. Itt finomítottam és rendezettebbé tettem a mappaszerkezetet, létrehoztam egy Komponens struktúrát, aminek a példájára az alkalmazás működni fog.

Létrehoztam egy alap megszemélyesítő felületet, ami az alkalmazás authentikációját jelenti ezen a ponton.

backenden CORS

## 7. hét

Április 10. - Április 16.

Ezen a héten a listázás és szúrést készítettem el. Kész lett a paginálás is, mind front-enden mind backenden. Ideabox liszázó oldal lényei része elkészült.

Adatmodell megváltoztatása, hogy DTO-kat és SlimDTO-kat tudjon kezelni. Már nem fordul elő, hogy listázásnál top level objektumok esetében akár az egész adatbázis elküldésre kerül.

## 8. hét

Április 17. - Április 23

Openapi leírás az API dokumentálására.

Mapperek megjavítása. Ezen a ponton már teljes egészében működik az új DTO és SlimDTO szerkezet, az API tud fogadni új objektumokat, amiket elment és visszaad. Visszaadásnál csak a feltétlenül szükséges adatokat adja vissza.

Spring security elkezdve

## 9. hét

Április 24. - Április 30.

Spring security működik, a felhasználók tudnak regisztrálni, a jelszavuk titkosítva elmentésre kerül az adatbázisban. Regisztrációnál és bejelentkezésnél access tokentkapnak, amivel elérhetik a védett belső endpoint-okat. Egyes endpointok jogosultságokhoz vannak kötve, ha a felhasznló rendelkezik a jogosultsággal akkor elérheti, ha nem akkor Forbidden errorrar visszatér a hívás.

WebResponse bevezetése, hogy egységesen lehessen kezelni a http üzeneteket, mind backenden, mind frontenden. Ehhez front-enden egyedi SnackBar létrehozva, ami a hibákról tájékoztat, illetve ha szöveges 200 http kódokat kapunk arról is (sikeres létrehozás, belépés stb)

IdeaBox page haladás: Felület nagyjából kész, lehet új Idea-t létrehozni, azok megjelennek a felületen.

Idea page nagyjából kész, lehet commentelni, kommenteket likeolni.

## 10. hét

Május 1. - Május 7.

Ezen a héten főleg frontenden történtek változások. Alap permission lezelés implementálva lett. Kommenteket a tulajdonosa szerkesztheti.

Létre lehet hozni tageket, amikkel csoportosítani lehet az ütleteket témakörökbe. Szerkesztő oldalak létrehozva az idea boxoknak, ideáknak. Törlésnél popup figyelmeztető.

## 11. hét

Május 8. - Május 14.

Frontenden create-edit komponenseket összevontam, itt hibajavítások.
User page halad, a user oldal listázói készek (valamennyire).

Ideaknak lehet required jury-t adni. Ha egy required jury pontoz egy Idea-t akkor az automatikusan átkerül Reviewed állapotba.

Admin felületen a user role aloldal kész, a felhasználóknak lehet állítani a role-jukat.

## 12. hét

Május 15. - Mákus 21.

Pontozás csillagokkal és sliderrel.

## 13. hét

Május 22. - Májua 28.

Ezen a héten nem tudtam foglalkozni az önálló laborra egyéb egyetemi teendők miatt.

## 14. hét

Május 29. - Június 4.

Ezen a héten megírtam a beszámolót, frissítettem az API leírást és nagy vonalakban elkészítettem a következő féléves terveket.

## póthét

Május 5. - Június 11.
