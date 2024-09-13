package com.cristian.teste.reservas.hoteis.repository;

import com.cristian.teste.reservas.hoteis.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
}
