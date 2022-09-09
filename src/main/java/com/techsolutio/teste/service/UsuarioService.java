package com.techsolutio.teste.service;

import java.nio.charset.Charset;
import java.util.Optional;

import com.techsolutio.teste.model.Usuario;
import com.techsolutio.teste.model.UsuarioDTO;
import com.techsolutio.teste.repository.UsuarioRepository;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service //classe de serviço -> regras de negócio
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public static final Logger LOGGER = LoggerFactory.getLogger(UsuarioService.class);

    //cadastro de usuario
    public Optional<Usuario> cadastrarUsuario(Usuario usuario)	{
      try{
          if (usuarioRepository.findByUser(usuario.getUser()).isPresent()) //isPresent -> verifica usuário cadastrado, se estiver, não cadastra
               return Optional.empty();
          LOGGER.error("Usuario já existe.");

          usuario.setPassword(criptografarSenha(usuario.getPassword()));
      }
      catch (Exception ex){
          LOGGER.error("Erro ao cadastrar usuario.");
      }
        return Optional.of(usuarioRepository.save(usuario));
    }

    //atualização de usuário
    public Optional<Usuario> atualizarUsuario(Usuario usuario)
    {
        try{
            if (usuarioRepository.findById(usuario.getId()).isPresent())
            {
                usuario.setPassword(criptografarSenha(usuario.getPassword()));
            }
            else{
                LOGGER.error("Usuario não  existe.");
                return Optional.empty();

            }

        }
        catch (Exception ex){
            LOGGER.error("Erro ao atualizar usuario.");
        }
        return Optional.of(usuarioRepository.save(usuario));
    }

    //autenticação do usuario
    public Optional<UsuarioDTO> autenticarUsuario(Optional<UsuarioDTO> usuarioDTO)
    {
        Optional<Usuario> usuario = usuarioRepository.findByUser(usuarioDTO.get().getUser());

        if(usuario.isPresent()) //se o usuario existem
        {
            if (compararSenhas(usuarioDTO.get().getPassword(), usuario.get().getPassword()))
            {
                usuarioDTO.get().setId(usuario.get().getId());
                usuarioDTO.get().setToken(gerarBasicToken(usuarioDTO.get().getUser(), usuarioDTO.get().getPassword()));
                usuarioDTO.get().setPassword(usuario.get().getPassword());
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