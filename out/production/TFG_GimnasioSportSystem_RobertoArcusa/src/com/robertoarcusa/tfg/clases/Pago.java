package com.robertoarcusa.tfg.clases;

import com.robertoarcusa.tfg.enums.Estado;
import com.robertoarcusa.tfg.enums.MetodoPago;
import com.robertoarcusa.tfg.enums.TipoCuota;
import com.robertoarcusa.tfg.enums.TipoPago;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Objects;

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

    @Id
    @Column(name = "id_pago")
    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    @Basic
    @Column(name = "fecha_pago")
    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    @Basic
    @Column(name = "importe")
    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuota")
    public TipoCuota getTipoCuota() {
        return tipoCuota;
    }

    public void setTipoCuota(TipoCuota tipoCuota) {
        this.tipoCuota = tipoCuota;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago")
    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Basic
    @Column(name = "recibo")
    public byte[] getRecibo() {
        return recibo;
    }

    public void setRecibo(byte[] recibo) {
        this.recibo = recibo;
    }

    @ManyToOne
    @JoinColumn(name = "id_socio", referencedColumnName = "id_socio", nullable = false)
    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

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

    @Override
    public int hashCode() {
        int result = Objects.hash(idPago, fechaPago, importe, tipoCuota, tipoPago, metodoPago, estado);
        result = 31 * result + Arrays.hashCode(recibo);
        return result;
    }
}