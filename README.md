# sample-routing

Il progetto è stato realizzato in JAVA 1.7, SPRING 4.0.2.RELEASE e sviluppato con NETBEANS 8.1

La web-application è stata creata come progetto MAVEN 

I dati utilizzati dai servizi REST sono stati caricati tramite url: "https://restcountries.eu/rest/v2/all"

I servizi REST esposti dalla web-app sono i seguenti:

#1. http://localhost:8080/sample-routing/

Ritorna la lista di tutti i paesi e le valute locali 

#2. http://localhost:8080/sample-routing/paging

(PAGINAZIONE) Ritorna la un sottoinieme di tutti i paesi e le valute locali 
Per facilità è stata impostata di defaul una paginazione di 5 elementi per volta

#3. http://localhost:8080/sample-routing/secured

(BASIC AUTHANTICATION) Ritorna la lista di tutti i paesi e le valute locali effettuando un'autenticazione base 

Nel progetto sono inclusi anche i JUNIT utilizzando spring-test.
