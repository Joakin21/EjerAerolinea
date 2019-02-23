
package Aerolinea;

public class Venta {
    
    private double total;
    private String trayecto;
    private int horario;
    private String tipoAsiento;
    
    public Venta(){
        total = 0;
    }
    public void sumTotal(double total){
        this.total += total;
    }
    public void setTrayecto(String origen, String destino){
        trayecto = origen+"-"+destino;
    }
    public void setHorario(int horario){
        this.horario = horario;
    }
    public void setTipoAsiento(int clase){
        if(clase==1)
            tipoAsiento = "primera clase";
        if(clase==2)
            tipoAsiento = "clase ejecutiva";
        if(clase==3)
            tipoAsiento = "clase economica";
    }
    public double getTotal(){
        return total;
    }
    public String getTipoAsiento(){
        return tipoAsiento;
    }
    public String getTrayecto(){
        return trayecto;
    }
    public int getHorario(){
        return horario;
    }
    public void descuentoVuelta(){//Descuento del 10% del total de la compra
        double descuento;
        descuento = (10*total)/100;
        total -= descuento;
    }
    
}

