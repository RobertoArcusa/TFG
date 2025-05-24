package com.robertoarcusa.tfg.clases;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.text.SimpleDateFormat;

@Entity
public class SesionClase {
    private int idSesion;
    private Timestamp fechaHora;
    private int capacidadDisponible;
    private Clase clase;
    private List<Inscripcion> inscripciones;

    @Id
    @Column(name = "id_sesion")
    public int getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(int idSesion) {
        this.idSesion = idSesion;
    }

    @Basic
    @Column(name = "fecha_hora")
    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    @Basic
    @Column(name = "capacidad_disponible")
    public int getCapacidadDisponible() {
        return capacidadDisponible;
    }

    public void setCapacidadDisponible(int capacidadDisponible) {
        this.capacidadDisponible = capacidadDisponible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SesionClase that = (SesionClase) o;
        return idSesion == that.idSesion &&
                capacidadDisponible == that.capacidadDisponible &&
                Objects.equals(fechaHora, that.fechaHora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSesion, fechaHora, capacidadDisponible);
    }

    @ManyToOne
    @JoinColumn(name = "id_clase", referencedColumnName = "id_clase", nullable = false)
    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    @OneToMany(mappedBy = "sesionclase", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }


    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return clase.getNombreClase() + " - " + sdf.format(fechaHora) + " - " + "Cap. Disp: " + capacidadDisponible;
    }

}


