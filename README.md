Platformă Gestiune Evenimente

Sistemul este construit pe un model de tip "lanț" (chain)  și utilizează:

Backend: Java cu Spring Boot.
Comunicare: gRPC pentru serviciul de autentificare și REST pentru restul resurselor.

Baze de date:

SQL: Pentru evenimente, pachete și utilizatori.

NoSQL (MongoDB): Pentru datele flexibile ale clienților.

Orchestrare: Docker Compose pentru rularea tuturor serviciilor într-un mediu izolat.

Management Evenimente: Crearea și editarea evenimentelor și a pachetelor (colecții de evenimente).


Vânzare Bilete: Validarea automată a locurilor disponibile; nu se pot vinde bilete peste limita setată.


Identity Management (IDM): Sistem de utilizatori cu roluri predefinite: admin, owner-event și client.


Căutare: Filtrarea datelor după nume, locație sau număr de bilete.


Event WebService: Gestionează logica de business pentru evenimente și bilete.


Client WebService: Se ocupă de profilul utilizatorilor și istoricul achizițiilor.


Auth Service: Serviciu bazat pe gRPC pentru autentificare și autorizare.


Pentru a porni întreaga aplicație:

docker-compose up .
