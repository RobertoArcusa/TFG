package com.robertoarcusa.tfg.clases;

import com.robertoarcusa.tfg.enums.TipoMembresia;
import com.robertoarcusa.tfg.enums.TipoUsuario;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Entidad que representa a un socio del gimnasio.
 * Incluye información personal, de membresía, de usuario, inscripciones y pagos asociados.
 *
 * Cada socio puede tener múltiples inscripciones a clases y múltiples pagos.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */
@Entity
public class Socio {
    private int idSocio;
    private String nombreSocio;
    private String apellidosSocio;
    private String dni;
    private Date fechaNacimiento;
    private String telefono;
    private TipoMembresia tipoMembresia;
    private byte[] fotoPerfil;
    private TipoUsuario tipoUsuario;
    private String contrasena;
    private List<Inscripcion> inscripciones;
    private List<Pago> pagos;

    /**
     * Obtiene el ID único del socio.
     * @return el ID del socio
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio")
    public int getIdSocio() {
        return idSocio;
    }

    /**
     * Establece el ID único del socio.
     * @param idSocio el ID a asignar
     */
    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }

    /**
     * Obtiene el nombre del socio.
     * @return el nombre del socio
     */
    @Basic
    @Column(name = "nombre_socio")
    public String getNombreSocio() {
        return nombreSocio;
    }

    /**
     * Establece el nombre del socio.
     * @param nombreSocio el nombre a asignar
     */
    public void setNombreSocio(String nombreSocio) {
        this.nombreSocio = nombreSocio;
    }

    /**
     * Obtiene los apellidos del socio.
     * @return los apellidos del socio
     */
    @Basic
    @Column(name = "apellidos_socio")
    public String getApellidosSocio() {
        return apellidosSocio;
    }

    /**
     * Establece los apellidos del socio.
     * @param apellidosSocio los apellidos a asignar
     */
    public void setApellidosSocio(String apellidosSocio) {
        this.apellidosSocio = apellidosSocio;
    }

    /**
     * Obtiene el DNI del socio.
     * @return el DNI
     */
    @Basic
    @Column(name = "dni")
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del socio.
     * @param dni el DNI a asignar
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene la fecha de nacimiento del socio.
     * @return la fecha de nacimiento
     */
    @Basic
    @Column(name = "fecha_nacimiento")
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Establece la fecha de nacimiento del socio.
     * @param fechaNacimiento la fecha a asignar
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Obtiene el número de teléfono del socio.
     * @return el teléfono
     */
    @Basic
    @Column(name = "telefono")
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del socio.
     * @param telefono el teléfono a asignar
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el tipo de membresía del socio.
     * @return el tipo de membresía
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_membresia")
    public TipoMembresia getTipoMembresia() {
        return tipoMembresia;
    }

    /**
     * Establece el tipo de membresía del socio.
     * @param tipoMembresia la membresía a asignar
     */
    public void setTipoMembresia(TipoMembresia tipoMembresia) {
        this.tipoMembresia = tipoMembresia;
    }

    /**
     * Obtiene la foto de perfil del socio.
     * @return la foto de perfil en bytes
     */
    @Basic
    @Column(name = "foto_perfil")
    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    /**
     * Establece la foto de perfil del socio.
     * @param fotoPerfil la imagen en formato byte array
     */
    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    /**
     * Obtiene el tipo de usuario del socio (por ejemplo, ADMIN o USUARIO).
     * @return el tipo de usuario
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario")
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    /**
     * Establece el tipo de usuario del socio.
     * @param tipoUsuario el tipo a asignar
     */
    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Obtiene la contraseña del socio.
     * @return la contraseña
     */
    @Basic
    @Column(name = "contrasena")
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del socio.
     * @param contrasena la contraseña a asignar
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Compara si dos objetos Socio son iguales.
     * @param o el objeto a comparar
     * @return true si son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Socio socio = (Socio) o;
        return idSocio == socio.idSocio &&
                Objects.equals(nombreSocio, socio.nombreSocio) &&
                Objects.equals(apellidosSocio, socio.apellidosSocio) &&
                Objects.equals(dni, socio.dni) &&
                Objects.equals(fechaNacimiento, socio.fechaNacimiento) &&
                Objects.equals(telefono, socio.telefono) &&
                Objects.equals(tipoMembresia, socio.tipoMembresia) &&
                Arrays.equals(fotoPerfil, socio.fotoPerfil) &&
                Objects.equals(tipoUsuario, socio.tipoUsuario) &&
                Objects.equals(contrasena, socio.contrasena);
    }

    /**
     * Genera el código hash del socio.
     * @return el valor hash
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(idSocio, nombreSocio, apellidosSocio, dni, fechaNacimiento, telefono, tipoMembresia, tipoUsuario, contrasena);
        result = 31 * result + Arrays.hashCode(fotoPerfil);
        return result;
    }

    /**
     * Obtiene la lista de inscripciones realizadas por el socio.
     * @return lista de inscripciones
     */
    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    /**
     * Establece la lista de inscripciones del socio.
     * @param inscripciones las inscripciones a asignar
     */
    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }

    /**
     * Obtiene la lista de pagos realizados por el socio.
     * @return lista de pagos
     */
    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Pago> getPagos() {
        return pagos;
    }

    /**
     * Establece la lista de pagos del socio.
     * @param pagos los pagos a asignar
     */
    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    /**
     * Devuelve una representación en cadena del socio, con su nombre completo.
     * @return cadena con nombre y apellidos
     */
    @Override
    public String toString() {
        return nombreSocio + " "  + apellidosSocio;
    }
}