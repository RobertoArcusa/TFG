package com.robertoarcusa.tfg.clases;

import com.robertoarcusa.tfg.enums.NivelDificultad;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
public class Clase {
    private int idClase;
    private String nombreClase;
    private int capacidadMaxima;
    private NivelDificultad nivelDificultad;
    private String sala;
    private byte[] imagenClase;
    private Entrenador entrenador;
    private List<SesionClase> sesiones;

    @Id
    @Column(name = "id_clase")
    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    @Basic
    @Column(name = "nombre_clase")
    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    @Basic
    @Column(name = "capacidad_maxima")
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_dificultad")
    public NivelDificultad getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(NivelDificultad nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    @Basic
    @Column(name = "sala")
    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    @Basic
    @Column(name = "imagen_clase")
    public byte[] getImagenClase() {
        return imagenClase;
    }

    public void setImagenClase(byte[] imagenClase) {
        this.imagenClase = imagenClase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clase clase = (Clase) o;
        return idClase == clase.idClase &&
                capacidadMaxima == clase.capacidadMaxima &&
                Objects.equals(nombreClase, clase.nombreClase) &&
                Objects.equals(nivelDificultad, clase.nivelDificultad) &&
                Objects.equals(sala, clase.sala) &&
                Arrays.equals(imagenClase, clase.imagenClase);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idClase, nombreClase, capacidadMaxima, nivelDificultad, sala);
        result = 31 * result + Arrays.hashCode(imagenClase);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "id_entrenador", referencedColumnName = "id_entrenador", nullable = false)
    public Entrenador getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    @OneToMany(mappedBy = "clase")
    public List<SesionClase> getSesiones() {
        return sesiones;
    }

    public void setSesiones(List<SesionClase> sesiones) {
        this.sesiones = sesiones;
    }

    @Override
    public String toString() {
        return nombreClase;
    }

}