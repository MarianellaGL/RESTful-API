package ar.com.ada.mongo.api.nifli.entities;

public class Episodio {

    public String nombreepisodio;
    public int numerodeepisodio;
    public int duracion;

    public void reproducir() {

        System.out.println("reproduciendo " + this.nombreepisodio + " " + this.numerodeepisodio);

    }

    public String getNombreepisodio() {
        return nombreepisodio;
    }

    public void setNombreepisodio(String nombreepisodio) {
        this.nombreepisodio = nombreepisodio;
    }

    public int getNumerodeepisodio() {
        return numerodeepisodio;
    }

    public void setNumerodeepisodio(int numerodeepisodio) {
        this.numerodeepisodio = numerodeepisodio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Episodio(String nombreepisodio, int numerodeepisodio, int duracion) {
        this.nombreepisodio = nombreepisodio;
        this.numerodeepisodio = numerodeepisodio;
        this.duracion = duracion;
    }

    public Episodio() {

    }

}