package com.alura.forumhub.controller;

import com.alura.forumhub.domain.usuario.DadosAutenticacao;
import com.alura.forumhub.domain.usuario.Usuario; // Import necessário
import com.alura.forumhub.infra.security.DadosTokenJWT; // Import do DTO que criamos
import com.alura.forumhub.infra.security.TokenService; // Import do Service
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService; // 1. Injeção do serviço de token

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        // 2. Gera o token para o usuário que acabou de ser autenticado
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        // 3. Retorna o token no corpo da resposta
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}