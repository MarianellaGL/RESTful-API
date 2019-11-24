package ar.com.ada.mongo.api.nifli.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.mongo.api.nifli.entities.Pelicula;
import ar.com.ada.mongo.api.nifli.entities.Serie;
import ar.com.ada.mongo.api.nifli.repo.PeliculaRepository;
import ar.com.ada.mongo.api.nifli.repo.SerieRepository;


/**
 * SerieService
 */
@Service
public class PeliculaService {

    @Autowired
    PeliculaRepository repo;

    public void grabar(Pelicula pelicula) {
         

        repo.save(pelicula);
    }

    public Pelicula buscarPorId(ObjectId id) {
        return repo.findBy_id(id);
    }

    public Pelicula buscarPorNombre(String nombre) {
        return repo.findByNombre(nombre);
    }

    public List<Pelicula> getCatalogo() {
        return repo.findAll();
    }

    public enum PeliculaValidationType {
        
        PELICULA_OK,

        PELICULA_DUPLICADA,
    

        PELICULA_DATOS_INVALIDOS 
        
    }

    public PeliculaValidationType verificarPelicula(Pelicula pelicula) {

        if (pelicula.getNombre() == null)
            return PeliculaValidationType.PELICULA_DATOS_INVALIDOS;

        if (pelicula.getAño() <= 0)
            return PeliculaValidationType.PELICULA_DATOS_INVALIDOS;

        if (pelicula.getGenero() == null)
            return PeliculaValidationType.PELICULA_DATOS_INVALIDOS;

        Pelicula pelidb = this.buscarPorNombre(pelicula.getNombre());
        if(pelidb == null)
        return PeliculaValidationType.PELICULA_OK;

        else{
            if(pelicula.get_id()!= pelidb.get_id())
            return PeliculaValidationType.PELICULA_DUPLICADA;
        }

        return PeliculaValidationType.PELICULA_OK;



    
    }

    


}

 