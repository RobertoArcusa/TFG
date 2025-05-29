package com.robertoarcusa.tfg.clases;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Representa un entrenador que imparte clases en el sistema.
 * Contiene información personal y profesional como nombre, especialidad, salario, y las clases que imparte.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */
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

    /**
     * Obtiene el identificador único del entrenador.
     *
     * @return el ID del entrenador
     */
    @Id
    @Column(name = "id_entrenador")
    public int getIdEntrenador() {
        return idEntrenador;
    }

    /**
     * Establece el identificador único del entrenador.
     *
     * @param idEntrenador el ID a asignar
     */
    public void setIdEntrenador(int idEntrenador) {
        this.idEntrenador = idEntrenador;
    }

    /**
     * Obtiene el nombre del entrenador.
     *
     * @return el nombre del entrenador
     */
    @Basic
    @Column(name = "nombre_entrenador")
    public String getNombreEntrenador() {
        return nombreEntrenador;
    }

    /**
     * Establece el nombre del entrenador.
     *
     * @param nombreEntrenador el nombre a asignar
     */
    public void setNombreEntrenador(String nombreEntrenador) {
        this.nombreEntrenador = nombreEntrenador;
    }

    /**
     * Obtiene los apellidos del entrenador.
     *
     * @return los apellidos del entrenador
     */
    @Basic
    @Column(name = "apellidos_entrenador")
    public String getApellidosEntrenador() {
        return apellidosEntrenador;
    }

    /**
     * Establece los apellidos del entrenador.
     *
     * @param apellidosEntrenador los apellidos a asignar
     */
    public void setApellidosEntrenador(String apellidosEntrenador) {
        this.apellidosEntrenador = apellidosEntrenador;
    }

    /**
     * Obtiene la especialidad del entrenador.
     *
     * @return la especialidad del entrenador
     */
    @Basic
    @Column(name = "especialidad")
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Establece la especialidad del entrenador.
     *
     * @param especialidad la especialidad a asignar
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Obtiene el número de teléfono del entrenador.
     *
     * @return el número de teléfono
     */
    @Basic
    @Column(name = "telefono")
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del entrenador.
     *
     * @param telefono el teléfono a asignar
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la fecha de contratación del entrenador.
     *
     * @return la fecha de contratación
     */
    @Basic
    @Column(name = "fecha_contratacion")
    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    /**
     * Establece la fecha de contratación del entrenador.
     *
     * @param fechaContratacion la fecha a asignar
     */
    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    /**
     * Obtiene el salario del entrenador.
     *
     * @return el salario
     */
    @Basic
    @Column(name = "salario")
    public double getSalario() {
        return salario;
    }

    /**
     * Establece el salario del entrenador.
     *
     * @param salario el salario a asignar
     */
    public void setSalario(double salario) {
        this.salario = salario;
    }

    /**
     * Obtiene la foto de perfil del entrenador.
     *
     * @return array de bytes con la imagen de perfil
     */
    @Basic
    @Column(name = "foto_perfil")
    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    /**
     * Establece la foto de perfil del entrenador.
     *
     * @param fotoPerfil array de bytes con la imagen
     */
    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    /**
     * Compara si dos objetos Entrenador son iguales.
     *
     * @param o el objeto a comparar
     * @return true si son iguales, false si no
     */
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

    /**
     * Genera el código hash del entrenador.
     *
     * @return el valor hash
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(idEntrenador, nombreEntrenador, apellidosEntrenador, especialidad, telefono, fechaContratacion, salario);
        result = 31 * result + Arrays.hashCode(fotoPerfil);
        return result;
    }

    /**
     * Obtiene la lista de clases que imparte el entrenador.
     *
     * @return lista de clases
     */
    @OneToMany(mappedBy = "entrenador", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Clase> getClases() {
        return clases;
    }

    /**
     * Establece la lista de clases que imparte el entrenador.
     *
     * @param clases lista de clases a asignar
     */
    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }

    /**
     * Devuelve una representación en texto del entrenador.
     *
     * @return nombre, apellidos y especialidad
     */
    @Override
    public String toString() {
        return nombreEntrenador + " " + apellidosEntrenador + " - " + especialidad;
    }
}