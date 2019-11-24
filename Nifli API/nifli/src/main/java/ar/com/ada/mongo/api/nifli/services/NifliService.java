package ar.com.ada.mongo.api.nifli.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.mongo.api.nifli.entities.Pelicula;
import ar.com.ada.mongo.api.nifli.entities.Serie;

@Service
public class NifliService {

    @Autowired
    SerieService serieService;

    @Autowired
    PeliculaService peliculaService;

    public void grabarSerie(Serie serie) {
        serieService.grabar(serie);
    }

    public void grabarPelicula(Pelicula pelicula){
        peliculaService.grabar(pelicula);
    }

    public Pelicula buscarPelicula(ObjectId id) {
        return peliculaService.buscarPorId(id);
    }

    public List<Pelicula> getCatalogoPeliculas() {
        return peliculaService.getCatalogo();
    }



    public Serie buscarSerie(String nombre) {
        return serieService.buscarPorNombre(nombre);
    }

    public Serie buscarSerie(ObjectId id) {
        return serieService.buscarPorId(id);
    }

    public List<Serie> getCatalogoSeries() {
        return serieService.getCatalogo();
    }

}
