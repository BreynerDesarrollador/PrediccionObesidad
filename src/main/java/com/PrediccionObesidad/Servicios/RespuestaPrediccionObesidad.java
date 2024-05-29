package com.PrediccionObesidad.Servicios;

public class RespuestaPrediccionObesidad {
    private boolean exito = false;
    private String mensaje = "";
    private data datos = new data();

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public data getDatos() {
        return datos;
    }

    public void setDatos(data datos) {
        this.datos = datos;
    }
}

class data {
    private double promedio;
    private String obeso;
    private String valorClase = "";
    private double prediccion;

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public String getObeso() {
        return obeso;
    }

    public void setObeso(String obeso) {
        this.obeso = obeso;
    }

    public String getValorClase() {
        return valorClase;
    }

    public void setValorClase(String valorClase) {
        this.valorClase = valorClase;
    }

    public double getPrediccion() {
        return prediccion;
    }

    public void setPrediccion(double prediccion) {
        this.prediccion = prediccion;
    }
}