package com.example.demo;

import com.example.demo.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    //Optional<Cliente> findByEmail(String email);
	@Query("SELECT c FROM Cliente c WHERE c.email = ?1")
	Cliente findByEmail(String email);
}
