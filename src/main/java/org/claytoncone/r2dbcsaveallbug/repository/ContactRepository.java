package org.claytoncone.r2dbcsaveallbug.repository;

import org.claytoncone.r2dbcsaveallbug.model.Contact;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContactRepository extends ReactiveCrudRepository<Contact, Long> {

    Mono<Contact> findContactByEmail(String email);

    Flux<Contact> findContactByPhone(String phone);
}
