package com.robertoarcusa.tfg.clases;

import com.robertoarcusa.tfg.enums.NivelDificultad;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Representa una clase deportiva que forma parte de la aplicación.
 * Contiene información sobre el nombre, capacidad, dificultad, sala, imagen, entrenador y sesiones asociadas.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */
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

    /**
     * Obtiene el identificador único de la clase.
     *
     * @return el ID de la clase
     */
    @Id
    @Column(name = "id_clase")
    public int getIdClase() {
        return idClase;
    }

    /**
     * Establece el identificador único de la clase.
     *
     * @param idClase el ID a asignar
     */
    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    /**
     * Obtiene el nombre de la clase.
     *
     * @return el nombre de la clase
     */
    @Basic
    @Column(name = "nombre_clase")
    public String getNombreClase() {
        return nombreClase;
    }

    /**
     * Establece el nombre de la clase.
     *
     * @param nombreClase el nombre a asignar
     */
    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    /**
     * Obtiene la capacidad máxima de la clase.
     *
     * @return la capacidad máxima
     */
    @Basic
    @Column(name = "capacidad_maxima")
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    /**
     * Establece la capacidad máxima de la clase.
     *
     * @param capacidadMaxima la capacidad a asignar
     */
    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    /**
     * Obtiene el nivel de dificultad de la clase.
     *
     * @return el nivel de dificultad
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_dificultad")
    public NivelDificultad getNivelDificultad() {
        return nivelDificultad;
    }

    /**
     * Establece el nivel de dificultad de la clase.
     *
     * @param nivelDificultad el nivel de dificultad a asignar
     */
    public void setNivelDificultad(NivelDificultad nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    /**
     * Obtiene el nombre de la sala donde se imparte la clase.
     *
     * @return el nombre de la sala
     */
    @Basic
    @Column(name = "sala")
    public String getSala() {
        return sala;
    }

    /**
     * Establece el nombre de la sala donde se imparte la clase.
     *
     * @param sala el nombre de la sala a asignar
     */
    public void setSala(String sala) {
        this.sala = sala;
    }

    /**
     * Obtiene la imagen asociada a la clase.
     *
     * @return un array de bytes con la imagen
     */
    @Basic
    @Column(name = "imagen_clase")
    public byte[] getImagenClase() {
        return imagenClase;
    }

    /**
     * Establece la imagen asociada a la clase.
     *
     * @param imagenClase array de bytes de la imagen
     */
    public void setImagenClase(byte[] imagenClase) {
        this.imagenClase = imagenClase;
    }

    /**
     * Compara si dos objetos Clase son iguales.
     *
     * @param o el objeto a comparar
     * @return true si son iguales, false si no
     */
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

    /**
     * Genera el hash code de la clase.
     *
     * @return el valor hash
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(idClase, nombreClase, capacidadMaxima, nivelDificultad, sala);
        result = 31 * result + Arrays.hashCode(imagenClase);
        return result;
    }

    /**
     * Obtiene el entrenador asignado a la clase.
     *
     * @return el entrenador
     */
    @ManyToOne
    @JoinColumn(name = "id_entrenador", referencedColumnName = "id_entrenador", nullable = false)
    public Entrenador getEntrenador() {
        return entrenador;
    }

    /**
     * Establece el entrenador asignado a la clase.
     *
     * @param entrenador el entrenador a asignar
     */
    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    /**
     * Obtiene la lista de sesiones asociadas a esta clase.
     *
     * @return lista de sesiones
     */
    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<SesionClase> getSesiones() {
        return sesiones;
    }

    /**
     * Establece la lista de sesiones asociadas a esta clase.
     *
     * @param sesiones lista de sesiones a asignar
     */
    public void setSesiones(List<SesionClase> sesiones) {
        this.sesiones = sesiones;
    }

    /**
     * Devuelve el nombre de la clase como representación en texto.
     *
     * @return nombre de la clase
     */
    @Override
    public String toString() {
        return nombreClase;
    }

}