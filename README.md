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
