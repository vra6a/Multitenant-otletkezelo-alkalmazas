# Multitenant ötletkezelő alkalmazás Spring alapokon - API

version: 1.0

## Idea Box

/api/idea-box

    GET:    /api/idea-box
            Visszaadja az adatbázisban tárolt ötletdobozokat.

    GET:    /api/idea-box/{id}
            Visszaadja az adott id-jű ötletdobozt.

    GET:    /api/idea-box/{id}/ideas
            Visszaadja az adott id-jű ötletdobozban tárolt ötleteket.

    POST:   /api/idea-box
            Menti az elküldött ötletdobozt.

    POST:   /api/idea-box/{id}/idea
            Menti az adott id-jű ötletdobozba az új ötletet.

    PUT:    /api/idea-box/{id}
            Felülírja az adott id-jű ötletdobozt.

    DELETE: /api/idea-box/{id}
            Törli az ötletdobozt.

## Idea

/api/idea

    GET:    /api/idea/{id}**
            Visszaadja az adott id-jű ötletet. <br>

    GET:    /api/idea/{id}/likes
            Visszaadja, hogy kik like-olták az ötletet

    GET:    /api/idea/{id}/comments
            Visszaadja az ötleten hagyott kommenteket

    POST:   /api/idea
            Menti az elküldött ötletet.

    POST:   /api/idea/{id}/like
            Az adott felhasználó like-olja az ötletet.

    POST:   /api/idea/{id}/dislike
            Az adott felhasználó leveszi a like-ot az ötletről.

    POST:   /api/idea/{id}/comment
            Az adott felhasználó kommenteli az ötletet.

    POST:   /api/idea/{id}/rate
            A bíráló felhasználó bírálja az ötletet.

    PUT:    /api/idea/{id}
            Felülírja az adott id-jű ötletet.

    DELETE: /api/idea/{id}
            Törli az adott id-jű ötletet.

## User

/api/user

    GET:    /api/user
            Visszaadja az összes tárolt felhasználót.

    GET:    /api/user/{id}
            Visszaadja az adott id-jű felhasználót.

    GET:    /api/user/{id}/ideas
            Visszaadja a felhasználó összes ötletét

    GET:    /api/user/{id}/idea-boxes
            Visszaadja a felhasználó összes ötletdobozát

    GET:    /api/user/{id}

    POST:   /api/user
            Menti az elküldött felhasználót.

    PUT:    /api/user/{id}
            Módosítja az elküldött felhasználót.

    DELETE: /api/user/{id}
            Törli az adott id-jű felhasználót.

## Comment

/api/comment

    GET:    /api/comment
            Visszaadja az összes tárolt kommentet.

    GET:    /api/comment/{id}
            Visszaadja az id-jű tárolt kommentet.

    POST:    /api/comment
            Menti az elküldött kommentet.

    POST:   /api/comment/{id}/like
            Az adott felhasználó like-olja az kommentet.

    POST:   /api/comment/{id}/dislike
            Az adott felhasználó leveszi a like-ot a kommentről.

    PUT:    /api/comment/{id}
            Módosítja az elküldött kommentet.

    DELETE: /api/user/{id}
            Törli az adott id-jű kommentet.
