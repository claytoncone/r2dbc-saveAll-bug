package org.claytoncone.r2dbcsaveallbug.repository;

import org.claytoncone.r2dbcsaveallbug.model.Client;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends R2dbcRepository<Client, Long> {
    Flux<Client> findClientBySurname(String surname);

    Flux<Client> findClientByCompany(String company);

    @Query("SELECT c.* FROM client c JOIN contact ct ON c.id = ct.client_id WHERE ct.email = :email")
    Mono<Client> findClientByEmail(String email);

}
