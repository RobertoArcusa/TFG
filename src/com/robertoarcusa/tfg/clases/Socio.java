package com.robertoarcusa.tfg.clases;

import com.robertoarcusa.tfg.enums.TipoMembresia;
import com.robertoarcusa.tfg.enums.TipoUsuario;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio")
    public int getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }

    @Basic
    @Column(name = "nombre_socio")
    public String getNombreSocio() {
        return nombreSocio;
    }

    public void setNombreSocio(String nombreSocio) {
        this.nombreSocio = nombreSocio;
    }

    @Basic
    @Column(name = "apellidos_socio")
    public String getApellidosSocio() {
        return apellidosSocio;
    }

    public void setApellidosSocio(String apellidosSocio) {
        this.apellidosSocio = apellidosSocio;
    }

    @Basic
    @Column(name = "dni")
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    @Basic
    @Column(name = "fecha_nacimiento")
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Basic
    @Column(name = "telefono")
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_membresia")
    public TipoMembresia getTipoMembresia() {
        return tipoMembresia;
    }

    public void setTipoMembresia(TipoMembresia tipoMembresia) {
        this.tipoMembresia = tipoMembresia;
    }

    @Basic
    @Column(name = "foto_perfil")
    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario")
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Basic
    @Column(name = "contrasena")
    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

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

    @Override
    public int hashCode() {
        int result = Objects.hash(idSocio, nombreSocio, apellidosSocio, dni, fechaNacimiento, telefono, tipoMembresia, tipoUsuario, contrasena);
        result = 31 * result + Arrays.hashCode(fotoPerfil);
        return result;
    }

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    @Override
    public String toString() {
        return nombreSocio + " "  + apellidosSocio;
    }
}