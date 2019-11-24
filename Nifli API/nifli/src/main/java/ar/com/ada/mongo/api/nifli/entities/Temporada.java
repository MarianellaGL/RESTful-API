package ar.com.ada.mongo.api.nifli.entities;

import java.util.*;

public class Temporada {
    
    public String nombretemporada;
    public int numerotemporada;
    public List<Episodio> episodios = new ArrayList<Episodio>();
    public List<Websodio> websodios = new ArrayList<Websodio>();

    public Temporada(){
        
    }
    public Temporada(String nombretemporada) {

        this.nombretemporada = nombretemporada;
    }

    public Episodio getEpisodio(int nro) {

        for (Episodio ep : this.episodios) {

            // "te"guarda el objeto temporada

            if (ep.numerodeepisodio == nro) {

                return ep;

            }

        }
        return null;
    }

    public Websodio getWebsodio(int nro) {

        for (Websodio web : this.websodios) {

            if (web.numerodeepisodio == nro) {

                return web;

            }
        }
        return null;

    }

    public String getNombretemporada() {
        return nombretemporada;
    }

    public void setNombretemporada(String nombretemporada) {
        this.nombretemporada = nombretemporada;
    }

    public int getNumerotemporada() {
        return numerotemporada;
    }

    public void setNumerotemporada(int numerotemporada) {
        this.numerotemporada = numerotemporada;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        this.episodios = episodios;
    }

    public List<Websodio> getWebsodios() {
        return websodios;
    }

    public void setWebsodios(List<Websodio> websodios) {
        this.websodios = websodios;
    }
}

    

