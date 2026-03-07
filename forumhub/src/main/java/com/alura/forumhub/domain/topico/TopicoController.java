package com.alura.forumhub.domain.topico;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar (@RequestBody @Valid DadosCadastroTopico dados){
        if (repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())){
            return ResponseEntity.badRequest().body("Erro: Tópico já existe");
        }
        var topico = new Topico(dados);
        repository.save(topico);
        return ResponseEntity.ok(topico);
    }
}
