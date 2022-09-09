package com.techsolutio.teste.service;

import java.nio.charset.Charset;
import java.util.Optional;

import com.techsolutio.teste.model.Usuario;
import com.techsolutio.teste.model.UsuarioDTO;
import com.techsolutio.teste.repository.UsuarioRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service //classe de serviço -> regras de negócio
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //cadastro de usuario
    public Optional<Usuario> cadastrarUsuario(Usuario usuario)	{
        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) //isPresent -> verifica usuário cadastrado, se estiver, não cadastra
            return Optional.empty();

        usuario.setSenha(criptografarSenha(usuario.getSenha()));

        return Optional.of(usuarioRepository.save(usuario));
    }

    //atualização de usuário
    public Optional<Usuario> atualizarUsuario(Usuario usuario)
    {
        if (usuarioRepository.findById(usuario.getId()).isPresent())
        {
            usuario.setSenha(criptografarSenha(usuario.getSenha()));

            return Optional.of(usuarioRepository.save(usuario));
        }

        return Optional.empty(); //empity -> retorna uma instancia optional vazia, caso o usuário não seja encontrado
    }

    //autenticação do usuario
    public Optional<UsuarioDTO> autenticarUsuario(Optional<UsuarioDTO> usuarioDTO)
    {
        Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioDTO.get().getUsuario());

        if(usuario.isPresent()) //se o usuario existem
        {
            if (compararSenhas(usuarioDTO.get().getSenha(), usuario.get().getSenha()))
            {
                usuarioDTO.get().setId(usuario.get().getId());
                usuarioDTO.get().setNome(usuario.get().getNome());
                usuarioDTO.get().setToken(gerarBasicToken(usuarioDTO.get().getUsuario(), usuarioDTO.get().getSenha()));
                usuarioDTO.get().setSenha(usuario.get().getSenha());
                /*se as senhas forem uguais(a senha e a criptografia) atualiza o objeto usuarioLogin com os dados
                 * recuperados da DB e insere o Token gerado através do método gerarBasicToken, podendo exibir o nome e a foto
                 * do usuario no front*/

                return usuarioDTO; /*retorna usuarioLogin atualizado para o UsuarioController
				 						classe contoladora checa se deu tudo certo e retorna o status*/
            }
        }
        return Optional.empty();
    }

    //criptografar senha
    private String criptografarSenha(String senha)
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.encode(senha); //encode -> retorna senha criptografada no formato BCrypt
    }

    //comparar senhas
    private boolean compararSenhas(String senhaDigitada, String senhaBanco)
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.matches(senhaDigitada, senhaBanco);
        /*ver se as senhas deu "match"*/
    }

    //gerar basic token
    private String gerarBasicToken(String usuario, String senha)
    {
        String token = usuario + ":" + senha;
        byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(tokenBase64);
    }
}