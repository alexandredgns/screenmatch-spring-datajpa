package br.com.alura.screenmatch_spring_datajpa;

import br.com.alura.screenmatch_spring_datajpa.main.Main;
import br.com.alura.screenmatch_spring_datajpa.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchSpringDatajpaApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchSpringDatajpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(repository);
		main.displayMenu();
	}
}

// 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
