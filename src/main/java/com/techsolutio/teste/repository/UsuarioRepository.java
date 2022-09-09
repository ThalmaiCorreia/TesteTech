package com.techsolutio.teste.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techsolutio.teste.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

     Optional<Usuario> findByUser(String user);
}
