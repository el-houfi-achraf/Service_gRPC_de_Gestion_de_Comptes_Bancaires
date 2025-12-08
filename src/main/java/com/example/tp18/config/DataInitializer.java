package com.example.tp18.config;

import com.example.tp18.entities.Compte;
import com.example.tp18.repositories.CompteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CompteRepository repository) {
        return args -> {
            // Créer quelques comptes de test
            Compte compte1 = new Compte();
            compte1.setId(UUID.randomUUID().toString());
            compte1.setSolde(1000.0f);
            compte1.setDateCreation(LocalDate.now().toString());
            compte1.setType("COURANT");
            repository.save(compte1);

            Compte compte2 = new Compte();
            compte2.setId(UUID.randomUUID().toString());
            compte2.setSolde(5000.0f);
            compte2.setDateCreation(LocalDate.now().toString());
            compte2.setType("EPARGNE");
            repository.save(compte2);

            Compte compte3 = new Compte();
            compte3.setId(UUID.randomUUID().toString());
            compte3.setSolde(2500.0f);
            compte3.setDateCreation(LocalDate.now().toString());
            compte3.setType("COURANT");
            repository.save(compte3);

            System.out.println("✅ Base de données initialisée avec 3 comptes de test");
        };
    }
}

