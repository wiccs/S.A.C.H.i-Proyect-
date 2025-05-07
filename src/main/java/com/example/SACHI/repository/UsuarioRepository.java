package com.example.SACHI.repository;

import com.example.SACHI.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Page<Usuario> findByUsuarioNombreContaining(String nombre, Pageable pageable);

}






