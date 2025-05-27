package com.robertoarcusa.tfg.clases;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

/**
 * Representa la inscripción de un socio a una sesión específica de clase.
 * Contiene la fecha en que se realizó la inscripción y las referencias al socio y a la sesión.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */
@Entity
public class Inscripcion {
    private int idInscripcion;
    private Date fechaInscripcion;
    private SesionClase sesionclase;
    private Socio socio;

    /**
     * Obtiene el identificador único de la inscripción.
     *
     * @return el ID de la inscripción
     */
    @Id
    @Column(name = "id_inscripcion")
    public int getIdInscripcion() {
        return idInscripcion;
    }

    /**
     * Establece el identificador único de la inscripción.
     *
     * @param idInscripcion el ID a asignar
     */
    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    /**
     * Obtiene la fecha en que se realizó la inscripción.
     *
     * @return la fecha de inscripción
     */
    @Basic
    @Column(name = "fecha_inscripcion")
    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    /**
     * Establece la fecha en que se realizó la inscripción.
     *
     * @param fechaInscripcion la fecha a asignar
     */
    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    /**
     * Compara si dos objetos Inscripcion son iguales.
     *
     * @param o el objeto a comparar
     * @return true si son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inscripcion that = (Inscripcion) o;
        return idInscripcion == that.idInscripcion &&
                Objects.equals(fechaInscripcion, that.fechaInscripcion);
    }

    /**
     * Genera el código hash de la inscripción.
     *
     * @return el valor hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(idInscripcion, fechaInscripcion);
    }

    /**
     * Obtiene la sesión de clase a la que se ha inscrito el socio.
     *
     * @return la sesión correspondiente
     */
    @ManyToOne
    @JoinColumn(name = "id_sesion", referencedColumnName = "id_sesion", nullable = false)
    public SesionClase getSesionclase() {
        return sesionclase;
    }

    /**
     * Establece la sesión de clase a la que se inscribe el socio.
     *
     * @param sesionclase la sesión a asignar
     */
    public void setSesionclase(SesionClase sesionclase) {
        this.sesionclase = sesionclase;
    }

    /**
     * Obtiene el socio que realiza la inscripción.
     *
     * @return el socio correspondiente
     */
    @ManyToOne
    @JoinColumn(name = "id_socio", referencedColumnName = "id_socio", nullable = false)
    public Socio getSocio() {
        return socio;
    }

    /**
     * Establece el socio que realiza la inscripción.
     *
     * @param socio el socio a asignar
     */
    public void setSocio(Socio socio) {
        this.socio = socio;
    }
}