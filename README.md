# client-api-intercorp
Developed by Luca Rodrigo Trejo

## Documentation
- [Swagger Documentation](https://intercorp-client-api.herokuapp.com/swagger-ui.html)

## Endpoints

### POST CLIENT
```
curl --location --request POST 'https://intercorp-client-api.herokuapp.com/api/client' \
 --header 'Content-Type: application/json' \
 --data-raw '{
     "name": "Luca Rodrigo",
     "last_name": "Trejo",
     "age": 31,
     "birth_date": 628473599
 }'
 ```
 
 ### GET CLIENTS
 - [Try Now](https://intercorp-client-api.herokuapp.com/api/client)
 
 ```
curl --location --request GET 'https://intercorp-client-api.herokuapp.com/api/client'
  ```

 ### GET KPI
  - [Try Now](https://intercorp-client-api.herokuapp.com/api/client/kpi)
  ```
curl --location --request GET 'https://intercorp-client-api.herokuapp.com/api/client/kpi'
   ```


## Technologies
- Java 8
- SpringBoot
- ModelMapper
- JUnit
- Swagger
- JPA
- H2
- Heroku
- Maven

## Considerations
- The first request may take a while because the instance has to be initialized
- An in-memory database is used (H2), so it is necessary to load at least one client to obtain results