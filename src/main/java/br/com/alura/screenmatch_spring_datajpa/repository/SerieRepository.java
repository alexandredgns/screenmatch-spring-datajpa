package br.com.alura.screenmatch_spring_datajpa.repository;

import br.com.alura.screenmatch_spring_datajpa.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {
}


