# PlayTime
Plugin that counts daily, weekly, monthly time on the server, including the last seen of all players

## Configuration
```
database: #database credentials
  username: "username"
  password: "password"
  databaseName: "database"
  hostname: "localhost"

messages:
  online: '&bOnline'
  player-not-exists: '&cGiocatore non trovato'
  admin:
    - '&f&l&m-------&f&l[ &b&lAttività &f&l]&f&l&m-------'
    - '&8• &fCittadino: &l%player%'
    - ' '
    - '&8• &fUltimo Accesso: &b%lastSeen%'
    - '&8• &fAttività giornaliera: &b%day%'
    - '&8• &fAttività settimanale: &b%week%'
    - '&8• &fAttività mensile: &b%month%'
    - '&f&l&m-------&f&l[ &b&lAttività &f&l]&f&l&m-------'
  player:
    - '&f&l&m-------&f&l[ &b&lAttività &f&l]&f&l&m-------'
    - '&8• &fCittadino: &b&l%player%'
    - ' '
    - '&8• &fUltimo Accesso: &b%lastSeen%'
    - '&f&l&m-------&f&l[ &b&lAttività &f&l]&f&l&m-------'
  self:
    - '&f&l&m-------&f&l[ &b&lAttività &f&l]&f&l&m-------'
    - '&8• &fCittadino: &b&l%player%'
    - ' '
    - '&8• &fAttività giornaliera: &b%day%'
    - '&8• &fAttività settimanale: &b%week%'
    - '&8• &fAttività mensile: &b%month%'
    - '&f&l&m-------&f&l[ &b&lAttività &f&l]&f&l&m-------'

reset: # leave this empty, this section is used for saving reset dates


```


## Screenshots

![Screenshot_1](https://user-images.githubusercontent.com/39953274/212680262-74b1993d-e1a9-4e2c-94c7-52f1cc8d6789.png)
![Screenshot_2](https://user-images.githubusercontent.com/39953274/212680269-5628f1b4-a588-4dc7-ba91-b775c684f3d2.png)
