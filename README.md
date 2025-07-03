# Restaurant Service API

Base URL: `/restaurants`

---

## 1. Creazione massiva di ristoranti

**POST** `/restaurants/api/create`  

Importa fino a **N** ristoranti da Google Places in base a una località.

- **Request Body** (`application/json`):

  | Campo      | Tipo    | Descrizione                               |
  |------------|---------|-------------------------------------------|
  | `total`    | integer | Numero massimo di ristoranti da inserire. |
  | `location` | string  | Località in cui cercare (es. “Roma”).     |

  ```json
  {
    "total": 10,
    "location": "Roma"
  }
Response (200 OK):
Plain‑text:
Ristoranti inseriti con successo

2. Recupero di tutti i ristoranti
GET /restaurants

Restituisce la lista di tutti i ristoranti.

Response

200 OK + JSON array di RestaurantDTO

204 No Content se non ci sono ristoranti

Esempio di RestaurantDTO:
{
  "id": 1,
  "name": "Trattoria Toscana Roma",
  "address": "Via Gasperina, 166, 00173 Roma RM, Italy",
  "mainImage": "https://…",
  "googleRank": 2,
  "reviewScore": 4.8,
  "googleMapsUrl": "https://…",
  "website": "https://…",
  "plateformCustomerId": null,
  "plateformBookingUrl": null,
  "isEdited": false,
  "categoryIds": [1, 3],
  "photoIds": [101, 102]
}

3. Recupero di un ristorante per ID
GET /restaurants/{id}

Path Parameter:

Nome	Tipo	Descrizione
id	Long	Identificativo del ristorante

Response

200 OK + singolo RestaurantDTO

404 Not Found se l’ID non esiste

4. Soft delete di un ristorante
DELETE /restaurants/delete/{id}

Segna un ristorante come “cancellato” (isDeleted = true) senza rimuoverlo fisicamente.

Path Parameter:

Nome	Tipo	Descrizione
id	Long	Identificativo del ristorante

Response

200 OK + messaggio di conferma
Soft delete effettuato per id: 5
404 Not Found se l’ID non esiste

Note generali
Tutte le risposte utilizzano Content-Type: application/json (tranne i messaggi di conferma plain‑text).

RestaurantDTO espone solo i campi rilevanti al frontend; proprietà interne (coordinate, placeId, ecc.) non vengono incluse.

Il service gestisce internamente il filtro su isDeleted e isEdited per non mostrare entità logiche cancellate o già modificate.
