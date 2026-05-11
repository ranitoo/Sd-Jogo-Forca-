/*package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Cliente registar(Cliente cliente) {
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> login(String email, String password) {
        Optional<Cliente> cliente = clienteRepository.findByEmail(email);
        if (cliente.isPresent() && passwordEncoder.matches(password, cliente.get().getPassword())) {
            return cliente;
        }
        return Optional.empty();
    }
}*/