package com.alura.forumhub.domain.topico;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    //validar se já existe titulo e mensagem iguais
    boolean existsByTituloAndMensagem(String titulo, String mensagem);
}
