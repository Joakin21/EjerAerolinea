
package Aerolinea;


public class Viaje {
    
    private String origen;
    private String destino;
    private int horario;
    private double[] precios;//precio

    private char[][] asientos;
    
    public Viaje(){}
    
    public Viaje(String origen, String destino, int horario, char[][] asientos, double distancia){
        this.origen=origen;
        this.destino=destino;
        this.horario=horario;
        this.asientos=asientos;
        //Precio para cada trayecto y para cada clase de ese trayecto
        precios = new double[]{0.0,0.0,0.0};// temuco-concepcion: 1,2 y 3 clase

        
    }
    public int getHorario(){
        return horario;
    }
    public String getOrigen(){
        return origen;
    }
    public String getDestino(){
        return destino;
    }
    public double getPrecio(int clase){
        if(clase==1)    
            return precios[0];
        if(clase==2)
            return precios[1];
        if(clase==3)
            return precios[2];
        else
            return -1;
    }
    public void setPrecio(int clase, double precio, double porc_aumento){
        double aumento = (porc_aumento*precio)/100; 
        if(clase==1)    
            this.precios[0] = precio+aumento;
        if(clase==2)
            this.precios[1] = precio+aumento;
        if(clase==3)
            this.precios[2] = precio+aumento;
    }
    public void showAsientos(int clase){
        int[] lim = limitesClases(clase);
        for(int i=lim[0]; i<lim[1]; i++){
            System.out.print("[ ");
            for(int j=0; j<4; j++){
                System.out.print(asientos[i][j]+" ");
            }
            System.out.print("]");   
            System.out.println();
        }
    }

    public boolean hayCompatibilidad(int cant_pasajes, int clase){
        return asientosDisponibles(clase) >= cant_pasajes;
    }
    public boolean asignarAsiento(int clase, int asiento){
        boolean asignar = false;
        boolean ban = true;
        int c=0;
        int[] lim = limitesClases(clase);
        int i = lim[0];
        while(i < lim[1] && ban){
            for(int j=0; j<4; j++){
                c++;
                if(c == asiento){//llego al asiento que quiere ocupar el cliente
                    if (asientos[i][j]=='L'){
                        asientos[i][j] = 'O';
                        asignar = true;
                    }
                    ban = false;
                } 
            }
            i++;
        }  
        return asignar;
    }
    
    public String descripcion(){
        return "Viaje de "+origen+ " a "+destino+" a las "+horario+" horas";
    }
    public int asientosDisponibles(int clase){
        int libreCont=0;
        int[] lim = limitesClases(clase);
        
        for(int i=lim[0]; i<lim[1]; i++){
            for(int j=0; j<4; j++){
                if(asientos[i][j]=='L')
                  libreCont++;
            }
        } 
        return libreCont;
    }
        private int[] limitesClases(int clase ){
            int a=0;
            int b=0;
            if(clase == 1){
                a = 0;
                b = 6;
            }
            if(clase == 2){
                a=6;
                b=15;
            }
            if(clase == 3){
                a=15;
                b=32;
            }
            int[] limites = new int[]{a,b};
            return limites;
    }
    
}
