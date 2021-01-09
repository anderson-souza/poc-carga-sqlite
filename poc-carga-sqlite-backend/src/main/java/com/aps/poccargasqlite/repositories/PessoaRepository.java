package com.aps.poccargasqlite.repositories;

import com.aps.poccargasqlite.model.Pessoa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends CrudRepository<Pessoa, Long> {

}
