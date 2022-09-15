package com.techsolutio.teste.configuration;

import com.techsolutio.teste.model.Usuario;
import com.techsolutio.teste.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;


@Service
public class DbInit implements CommandLineRunner{

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void run(String... args) throws Exception {

        Usuario usuario = new Usuario(0L, "root@root.com", "rootroot");
        usuarioService.cadastrarUsuario(usuario);

    }

}
