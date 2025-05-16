package com.robertoarcusa.tfg.clases;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
public class Entrenador {
    private int idEntrenador;
    private String nombreEntrenador;
    private String apellidosEntrenador;
    private String especialidad;
    private String telefono;
    private Date fechaContratacion;
    private double salario;
    private byte[] fotoPerfil;
    private List<Clase> clases;

    @Id
    @Column(name = "id_entrenador")
    public int getIdEntrenador() {
        return idEntrenador;
    }

    public void setIdEntrenador(int idEntrenador) {
        this.idEntrenador = idEntrenador;
    }

    @Basic
    @Column(name = "nombre_entrenador")
    public String getNombreEntrenador() {
        return nombreEntrenador;
    }

    public void setNombreEntrenador(String nombreEntrenador) {
        this.nombreEntrenador = nombreEntrenador;
    }

    @Basic
    @Column(name = "apellidos_entrenador")
    public String getApellidosEntrenador() {
        return apellidosEntrenador;
    }

    public void setApellidosEntrenador(String apellidosEntrenador) {
        this.apellidosEntrenador = apellidosEntrenador;
    }

    @Basic
    @Column(name = "especialidad")
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Basic
    @Column(name = "telefono")
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Basic
    @Column(name = "fecha_contratacion")
    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    @Basic
    @Column(name = "salario")
    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Basic
    @Column(name = "foto_perfil")
    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrenador that = (Entrenador) o;
        return idEntrenador == that.idEntrenador &&
                Double.compare(that.salario, salario) == 0 &&
                Objects.equals(nombreEntrenador, that.nombreEntrenador) &&
                Objects.equals(apellidosEntrenador, that.apellidosEntrenador) &&
                Objects.equals(especialidad, that.especialidad) &&
                Objects.equals(telefono, that.telefono) &&
                Objects.equals(fechaContratacion, that.fechaContratacion) &&
                Arrays.equals(fotoPerfil, that.fotoPerfil);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idEntrenador, nombreEntrenador, apellidosEntrenador, especialidad, telefono, fechaContratacion, salario);
        result = 31 * result + Arrays.hashCode(fotoPerfil);
        return result;
    }

    @OneToMany(mappedBy = "entrenador")
    public List<Clase> getClases() {
        return clases;
    }

    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }

    @Override
    public String toString() {
        return nombreEntrenador + " " + apellidosEntrenador + " - " + especialidad;
    }
}
