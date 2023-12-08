package br.com.banking.project.test.repository;

import br.com.banking.project.test.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findContaByNumeroConta(int conta);


}

