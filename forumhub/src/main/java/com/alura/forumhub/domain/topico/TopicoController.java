package com.alura.forumhub.domain.topico;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC)Pageable paginacao
            ){
        var pagina = repository.findAll(paginacao).map(DadosListagemTopico::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/id")
    public ResponseEntity detalhar(@PathVariable Long id){
        var topico = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoTopico dados){
        var optionalTopico = repository.findById(id);

        //Verifica se o tópico existe
        if (optionalTopico.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        //não permite duplicação de título e mensagem
        if (repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())){
            return ResponseEntity.badRequest().body("Erro: Já existe um tópico com este titulo e mensagem");
        }

        var topico = optionalTopico.get();
        topico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        var optionalTopico = repository.findById(id);

        //verifica se o tópico existe no bancp
        if (optionalTopico.isPresent()){
            repository.deleteById(id);
            return ResponseEntity.noContent().build(); //retorna status 204
        }

        //se não existir, retorna status 404
        return ResponseEntity.notFound().build();
    }
}
