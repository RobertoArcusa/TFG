package com.robertoarcusa.tfg.clases;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.text.SimpleDateFormat;

/**
 * Representa una sesión específica de una clase en el gimnasio. Contiene la fecha y hora de la sesión,
 * la capacidad disponible, la clase asociada y la lista de inscripciones realizadas para dicha sesión.
 *
 * Cada sesión pertenece a una única clase y puede tener múltiples inscripciones.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */
@Entity
public class SesionClase {
    private int idSesion;
    private Timestamp fechaHora;
    private int capacidadDisponible;
    private Clase clase;
    private List<Inscripcion> inscripciones;

    /**
     * Obtiene el identificador único de la sesión.
     *
     * @return el ID de la sesión
     */
    @Id
    @Column(name = "id_sesion")
    public int getIdSesion() {
        return idSesion;
    }

    /**
     * Establece el identificador único de la sesión.
     *
     * @param idSesion el ID a asignar
     */
    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    /**
     * Obtiene la fecha y hora programadas para la sesión.
     *
     * @return la fecha y hora de la sesión
     */
    @Basic
    @Column(name = "fecha_hora")
    public Timestamp getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora programadas para la sesión.
     *
     * @param fechaHora la fecha y hora a asignar
     */
    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * Obtiene la cantidad de plazas disponibles para la sesión.
     *
     * @return la capacidad disponible
     */
    @Basic
    @Column(name = "capacidad_disponible")
    public int getCapacidadDisponible() {
        return capacidadDisponible;
    }

    /**
     * Establece la cantidad de plazas disponibles para la sesión.
     *
     * @param capacidadDisponible el número de plazas disponibles
     */
    public void setCapacidadDisponible(int capacidadDisponible) {
        this.capacidadDisponible = capacidadDisponible;
    }

    /**
     * Obtiene la clase a la que pertenece esta sesión.
     *
     * @return la clase asociada
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SesionClase that = (SesionClase) o;
        return idSesion == that.idSesion &&
                capacidadDisponible == that.capacidadDisponible &&
                Objects.equals(fechaHora, that.fechaHora);
    }

    /**
     * Genera el código hash del objeto SesionClase.
     *
     * @return el valor hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(idSesion, fechaHora, capacidadDisponible);
    }

    /**
     * Obtiene la clase a la que pertenece esta sesión.
     *
     * @return la clase asociada
     */
    @ManyToOne
    @JoinColumn(name = "id_clase", referencedColumnName = "id_clase", nullable = false)
    public Clase getClase() {
        return clase;
    }

    /**
     * Establece la clase a la que pertenece esta sesión.
     *
     * @param clase la clase a asignar
     */
    public void setClase(Clase clase) {
        this.clase = clase;
    }

    /**
     * Obtiene la lista de inscripciones realizadas para esta sesión.
     *
     * @return lista de inscripciones
     */
    @OneToMany(mappedBy = "sesionclase", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    /**
     * Establece la lista de inscripciones para esta sesión.
     *
     * @param inscripciones lista de inscripciones a asignar
     */
    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }

    /**
     * Devuelve una representación en cadena de la sesión.
     * Incluye el nombre de la clase, la fecha formateada y la capacidad disponible.
     *
     * @return una cadena descriptiva de la sesión
     */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return clase.getNombreClase() + " - " + sdf.format(fechaHora) + " - " + "Cap. Disp: " + capacidadDisponible;
    }

}


