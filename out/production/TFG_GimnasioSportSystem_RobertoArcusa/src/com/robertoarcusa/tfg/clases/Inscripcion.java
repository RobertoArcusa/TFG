package com.robertoarcusa.tfg.clases;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Inscripcion {
    private int idInscripcion;
    private Date fechaInscripcion;
    private SesionClase sesionclase;
    private Socio socio;

    @Id
    @Column(name = "id_inscripcion")
    public int getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    @Basic
    @Column(name = "fecha_inscripcion")
    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inscripcion that = (Inscripcion) o;
        return idInscripcion == that.idInscripcion &&
                Objects.equals(fechaInscripcion, that.fechaInscripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInscripcion, fechaInscripcion);
    }

    @ManyToOne
    @JoinColumn(name = "id_sesion", referencedColumnName = "id_sesion", nullable = false)
    public SesionClase getSesionclase() {
        return sesionclase;
    }

    public void setSesionclase(SesionClase sesionclase) {
        this.sesionclase = sesionclase;
    }

    @ManyToOne
    @JoinColumn(name = "id_socio", referencedColumnName = "id_socio", nullable = false)
    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }
}