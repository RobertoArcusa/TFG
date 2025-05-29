package com.robertoarcusa.tfg.clases;

import com.robertoarcusa.tfg.enums.Estado;
import com.robertoarcusa.tfg.enums.MetodoPago;
import com.robertoarcusa.tfg.enums.TipoCuota;
import com.robertoarcusa.tfg.enums.TipoPago;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Objects;

/**
 * Representa un pago realizado por un socio. Incluye información como la fecha, el importe,
 * el tipo y método de pago, el estado, y un recibo en formato binario.
 *
 * Cada pago está asociado a un socio específico.
 *
 * @author Roberto Arcusa
 * @version 1.0
 * @since 2025
 */
@Entity
public class Pago {
    private int idPago;
    private Date fechaPago;
    private double importe;
    private TipoCuota tipoCuota;
    private TipoPago tipoPago;
    private MetodoPago metodoPago;
    private Estado estado;
    private byte[] recibo;
    private Socio socio;

    /**
     * Obtiene el identificador único del pago.
     *
     * @return el ID del pago
     */
    @Id
    @Column(name = "id_pago")
    public int getIdPago() {
        return idPago;
    }

    /**
     * Establece el identificador único del pago.
     *
     * @param idPago el ID a asignar
     */
    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    /**
     * Obtiene la fecha en la que se realizó el pago.
     *
     * @return la fecha del pago
     */
    @Basic
    @Column(name = "fecha_pago")
    public Date getFechaPago() {
        return fechaPago;
    }

    /**
     * Establece la fecha en la que se realizó el pago.
     *
     * @param fechaPago la fecha a asignar
     */
    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    /**
     * Obtiene el importe del pago.
     *
     * @return el importe del pago
     */
    @Basic
    @Column(name = "importe")
    public double getImporte() {
        return importe;
    }

    /**
     * Establece el importe del pago.
     *
     * @param importe el importe a asignar
     */
    public void setImporte(double importe) {
        this.importe = importe;
    }

    /**
     * Obtiene el tipo de cuota del pago.
     *
     * @return el tipo de cuota
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuota")
    public TipoCuota getTipoCuota() {
        return tipoCuota;
    }

    /**
     * Establece el tipo de cuota del pago.
     *
     * @param tipoCuota el tipo de cuota a asignar
     */
    public void setTipoCuota(TipoCuota tipoCuota) {
        this.tipoCuota = tipoCuota;
    }

    /**
     * Obtiene el tipo de pago realizado.
     *
     * @return el tipo de pago
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago")
    public TipoPago getTipoPago() {
        return tipoPago;
    }

    /**
     * Establece el tipo de pago realizado.
     *
     * @param tipoPago el tipo de pago a asignar
     */
    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    /**
     * Obtiene el método de pago utilizado.
     *
     * @return el método de pago
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    /**
     * Establece el método de pago utilizado.
     *
     * @param metodoPago el método de pago a asignar
     */
    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    /**
     * Obtiene el estado actual del pago.
     *
     * @return el estado del pago
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    public Estado getEstado() {
        return estado;
    }

    /**
     * Establece el estado actual del pago.
     *
     * @param estado el estado a asignar
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el recibo del pago en formato binario.
     *
     * @return el recibo del pago
     */
    @Basic
    @Column(name = "recibo")
    public byte[] getRecibo() {
        return recibo;
    }

    /**
     * Establece el recibo del pago en formato binario.
     *
     * @param recibo el recibo a asignar
     */
    public void setRecibo(byte[] recibo) {
        this.recibo = recibo;
    }

    /**
     * Obtiene el socio asociado al pago.
     *
     * @return el socio que realizó el pago
     */
    @ManyToOne
    @JoinColumn(name = "id_socio", referencedColumnName = "id_socio", nullable = false)
    public Socio getSocio() {
        return socio;
    }

    /**
     * Establece el socio asociado al pago.
     *
     * @param socio el socio a asignar
     */
    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    /**
     * Compara si dos objetos Pago son iguales.
     *
     * @param o el objeto a comparar
     * @return true si son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pago pago = (Pago) o;
        return idPago == pago.idPago &&
                Double.compare(pago.importe, importe) == 0 &&
                Objects.equals(fechaPago, pago.fechaPago) &&
                tipoCuota == pago.tipoCuota &&
                tipoPago == pago.tipoPago &&
                metodoPago == pago.metodoPago &&
                estado == pago.estado &&
                Arrays.equals(recibo, pago.recibo);
    }

    /**
     * Genera el código hash del objeto Pago.
     *
     * @return el valor hash
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(idPago, fechaPago, importe, tipoCuota, tipoPago, metodoPago, estado);
        result = 31 * result + Arrays.hashCode(recibo);
        return result;
    }
}