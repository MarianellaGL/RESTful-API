package ar.com.ada.mongo.api.nifli;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.com.ada.mongo.api.nifli.entities.Pelicula;
import ar.com.ada.mongo.api.nifli.entities.Serie;
import ar.com.ada.mongo.api.nifli.services.PeliculaService;
import ar.com.ada.mongo.api.nifli.services.SerieService;
import ar.com.ada.mongo.api.nifli.services.PeliculaService.PeliculaValidationType;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class PeliApplicationTests {

	@Autowired
	PeliculaService peliculaservice;
	@Autowired
	Pelicula pelicula;

	@Test
	void contextLoads() {
	}

	@Test
	void verificarPelicula() {

		PeliculaValidationType PeliculaValidationType = peliculaservice.verificarPelicula(pelicula);

		assertEquals(PeliculaValidationType.PELICULA_DATOS_INVALIDOS, PeliculaValidationType);

	}

	@Test
	void verificarPeliculaDuplicada() {

		pelicula.setNombre("The Rocky Horror Picture Show");
		pelicula.setAño(1975);
		pelicula.setGenero("Musical");

		PeliculaValidationType peliculaValidationType = peliculaservice.verificarPelicula(pelicula);

		assertEquals(PeliculaValidationType.PELICULA_DUPLICADA, peliculaValidationType);

	}

}
