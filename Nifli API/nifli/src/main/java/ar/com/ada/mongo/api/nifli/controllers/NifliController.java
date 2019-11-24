package ar.com.ada.mongo.api.nifli.controllers;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.mongo.api.nifli.entities.Pelicula;
import ar.com.ada.mongo.api.nifli.entities.Serie;
import ar.com.ada.mongo.api.nifli.models.response.NuevoContenidoResponse;
import ar.com.ada.mongo.api.nifli.services.NifliService;

@RestController
public class NifliController {

    @Autowired
    NifliService nifliService;


    @PostMapping("api/series")
    public NuevoContenidoResponse postRegisterSerie(@RequestBody Serie reqSerie) {
        NuevoContenidoResponse r = new NuevoContenidoResponse();
        // aca creamos la persona y el usuario a travez del service.

        nifliService.grabarSerie(reqSerie);

        r.isOk = true;
        r.message = "Serie cargada con éxito";
        r.id = reqSerie.get_id().toString();
        return r;

    }

    @GetMapping("api/series")
    public ResponseEntity<?> GetSeries() throws Exception {

        List<Serie> series = nifliService.getCatalogoSeries();
        if (series.size() == 0)
            return (ResponseEntity<?>) ResponseEntity.notFound();

        return ResponseEntity.ok(series);

    }

    @PostMapping("api/peliculas")
    public NuevoContenidoResponse postRegisterPelicula(@RequestBody Pelicula reqPelicula) {
        NuevoContenidoResponse r = new NuevoContenidoResponse();
        // aca creamos la persona y el usuario a travez del service.

        nifliService.grabarPelicula(reqPelicula);

        r.isOk = true;
        r.message = "Pelicula cargada con éxito";
        r.id = reqPelicula.get_id().toString();
        return r;

    }

    @GetMapping("api/peliculas")
    public ResponseEntity<?> GetPeliculas() throws Exception {

        List<Pelicula> peliculas = nifliService.getCatalogoPeliculas();
        if (peliculas.size() == 0)
            return (ResponseEntity<?>) ResponseEntity.notFound();

        return ResponseEntity.ok(peliculas);

    }

    @PutMapping("api/series/{id}")
    public NuevoContenidoResponse actualizarContenido(@PathVariable String id, @RequestBody Serie reqSerie) {
        NuevoContenidoResponse r = new NuevoContenidoResponse();
        
        reqSerie.set_id(new ObjectId(id));
        nifliService.grabarSerie(reqSerie);//graba la serie y cuando se le pone el id graba la serie con el contenido que se la haya puesto eso es pisar 
     /**si se quiere se puede hacer un request chico para pasar los parametros que se deseen actualizar o cambiar pero lo ideal seria generar un 
      * merge entre dos archivos json lo cual es más complejo que pisar los archivos(ejemplo dearriba) */

        
        
        r.isOk = true;
        r.message = "Serie actualizada";
    
        return r;
    }



}