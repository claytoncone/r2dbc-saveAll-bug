package org.claytoncone.r2dbcsaveallbug.repository;

import org.claytoncone.r2dbcsaveallbug.config.TestDataConfig;
import org.claytoncone.r2dbcsaveallbug.model.Client;
import org.claytoncone.r2dbcsaveallbug.model.Contact;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestDataConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientRepositoryTest {

    private final ClientRepository clientRepository;

    private final ContactRepository contactRepository;

    private final DatabaseClient databaseClient;

    private final TestDataConfig testDataConfig;

    @Autowired
    public ClientRepositoryTest(ClientRepository clientRepository, ContactRepository contactRepository,
                                DatabaseClient databaseClient, TestDataConfig testDataConfig) {
        this.clientRepository = clientRepository;
        this.contactRepository = contactRepository;
        this.databaseClient = databaseClient;
        this.testDataConfig = testDataConfig;
    }

    @BeforeAll
    void setUp() {
        testDataConfig.initializeTestData(databaseClient).block();

        // Step 1: Read client data and map address IDs
        Flux<Client> clients = Flux.fromStream(new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/ClientData/clientdata.csv"))))
                .lines()
                .skip(1) // Skip header line
                .map(line -> {
                    String[] fields = line.split(",");
                    Client client = new Client();
                    client.setGivenName(fields.length > 0 ? fields[0] : null);
                    client.setMiddleInitial(fields.length > 1 ? (!fields[1].isEmpty() ? fields[1].substring(0, 1) : null) : null);
                    client.setSurname(fields.length > 2 ? fields[2] : null);
                    client.setTitle(fields.length > 3 ? fields[3] : null);
                    client.setCompany(fields.length > 4 ? fields[4] : null);
                    return client;
                }));

        AtomicInteger clientIndex = new AtomicInteger(0);
        List<Long> clientIdList = new ArrayList<>();
        clientRepository.saveAll(clients)
                .doOnNext(client -> clientIdList.add(client.getId()))
                .blockLast();

        // Print out the client IDs
        System.out.println("Client IDs: " + clientIdList);

        // Step 3: Read contact data and map client IDs
        Flux<Contact> contacts = Flux.fromStream(new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/ContactData/contactdata.csv"))))
                .lines()
                .skip(1) // Skip header line
                .map(line -> {
                    String[] fields = line.split(",");
                    Contact contact = new Contact();
                    contact.setEmail(fields.length > 0 ? fields[0] : null);
                    contact.setPhone(fields.length > 1 ? fields[1] : null);
                    contact.setMobile(fields.length > 2 ? fields[2] : null);
                    contact.setFax(fields.length > 3 ? fields[3] : null);
                    contact.setClientId(clientIdList.get(clientIndex.getAndIncrement() % clientIdList.size()));
                    return contact;
                }));

        // Step 4: Save contact records using custom insert method
        contactRepository.saveAll(contacts).blockLast();
    }

    @Test
    void testSaveAndFindClient() {
        //given
        Client client = new Client();
        client.setGivenName("John");
        client.setSurname("Doe");

        //when
        clientRepository.save(client).block();

        //then
        Optional<Client> foundClient = clientRepository.findById(client.getId()).blockOptional();
        assertTrue(foundClient.isPresent());
        assertEquals("John", foundClient.get().getGivenName());
    }
}
