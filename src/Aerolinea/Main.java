
package Aerolinea;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    Scanner input = new Scanner(System.in);
    int[] horarios = new int[]{9, 12, 15, 20};
    //Ventas maximas
    final int MAX_VENTAS = 3168;
    //Viajes
    Viaje[] temuco_concepcion = new Viaje[4];
    Viaje[] concepcion_temuco = new Viaje[4];
    Viaje[] temuco_santiago = new Viaje[4];
    Viaje[] santiago_temuco = new Viaje[4];
    Viaje[] temuco_serena = new Viaje[4];
    Viaje[] serena_temuco = new Viaje[4];
    //Precios
    int tem_con_precio=0;
    int tem_san_precio=0;
    int tem_ser_precio=0;
    boolean preciosFijados = false;
    //Aumento porcentaje segun clase
    double primera_clase=0;
    double segunda_clase=0;
    double tercera_clase=0;
    boolean porcentajesFijados = false;
    //Datos que ingresa el cliente
    String origen;
    String destino;
    int horario;
    int cant_pasajes;
    int clase;
    int selec;
    //Viaje deseado por el cliente
    Viaje myViaje;
    //Ventas
    List<Venta> ventas = new ArrayList<>();
    Venta myVenta;
    //Contador
    int contador_pasajes_vendidos = 0;
    //app
    boolean salir=false;
    
    public Main(){
        
        //Iniciamos los viajes 
        for(int i=0; i<horarios.length;i++ ){
            
            temuco_concepcion[i]=new Viaje("temuco","concepcion",horarios[i] , myAsientos(),300);
            concepcion_temuco[i]=new Viaje("concepcion","temuco",horarios[i] , myAsientos(),300);
            
            temuco_santiago[i]=new Viaje("temuco","santiago",horarios[i] , myAsientos(),680);
            santiago_temuco[i]=new Viaje("santiago","temuco",horarios[i] , myAsientos(),680);
            
            temuco_serena[i]=new Viaje("temuco","serena",horarios[i] , myAsientos(),1150);
            serena_temuco[i]=new Viaje("serena","temuco",horarios[i] , myAsientos(),1150);         
        }
        //Usuario asigna los precios de los trayectos
        while(preciosFijados==false){
            System.out.println("Fijar precios");
            System.out.print("de Temuco a Concepcion: ");
            tem_con_precio = input.nextInt();
            System.out.print("de Temuco a Santiago: ");
            tem_san_precio = input.nextInt();
            System.out.print("de Temuco a La Serena: ");
            tem_ser_precio = input.nextInt();
            if (tem_con_precio < tem_san_precio && tem_con_precio < tem_ser_precio && tem_san_precio< tem_ser_precio)
                preciosFijados = true;
            else
                System.out.println("Error, debe fijar los precios segun la distacia de los trayectos");
            //tem_con_precio>=tem_san_precio || tem_con_precio >= tem_ser_precio || tem_san_precio >= tem_ser_precio 
        }
        //Usuario asigna porcentaje de aumentos segun la clase del asiento
        while(porcentajesFijados==false){
            System.out.println("Fijar porcentaje de aumento al precio");
            System.out.print("Primera Clase: ");
            primera_clase = input.nextInt();
            System.out.print("Clase Ejecutiva: ");
            segunda_clase = input.nextInt();
            System.out.print("Clase Economica: ");
            tercera_clase = input.nextInt();
            if(tercera_clase < segunda_clase && tercera_clase < primera_clase && segunda_clase < primera_clase)
                porcentajesFijados = true;
            else
                System.out.println("Error, debe fijar los porcentajes de aumento segun la clase del asiento");
        }
        //Asignar precio de los trayectos + porcentaje de aumento por el tipo de asiento
         double[] aPorcentaje = new double[]{primera_clase,segunda_clase,tercera_clase}; 
         for(int i=0 ; i <horarios.length ; i++ ){
             for(int j = 1; j<=3; j++){
                 temuco_concepcion[i].setPrecio(j, tem_con_precio, aPorcentaje[j-1]);
                 concepcion_temuco[i].setPrecio(j, tem_con_precio, aPorcentaje[j-1]);
                 temuco_santiago[i].setPrecio(j, tem_san_precio, aPorcentaje[j-1]);
                 santiago_temuco[i].setPrecio(j, tem_san_precio, aPorcentaje[j-1]);
                 temuco_serena[i].setPrecio(j, tem_ser_precio, aPorcentaje[j-1]);
                 serena_temuco[i].setPrecio(j, tem_ser_precio, aPorcentaje[j-1]);
             }
             
         }         
         //Comienza el programa
         while(!salir){
             System.out.println("Vender pasaje");
             
             //Datos del viaje
             System.out.print("Origen: ");
             origen = input.next();
             if(origen.equals("temuco")){
                 System.out.print("Destino: ");
                 destino = input.next();  
             }
             else{
                destino = "temuco";
                System.out.println("Destino: "+destino);
             }
             System.out.print("Horario: ");
             horario = input.nextInt();
             System.out.print("Cantidad de pasajes: ");
             cant_pasajes = input.nextInt();
             while(cant_pasajes > 5 && cant_pasajes <=0){
                System.out.println("Error, la cantidad de pasajes debe estar entre 1 y 5");
                System.out.print("Cantidad de pasajes: ");
                cant_pasajes = input.nextInt();
             }
             System.out.print("clase: ");//1,2,o 3
             clase = input.nextInt();
             //Obtengo el viaje asociado
             myViaje = buscador(origen, destino, horario);
             //Test
             System.out.println(myViaje.getPrecio(1));
             System.out.println(myViaje.getPrecio(2));
             System.out.println(myViaje.getPrecio(3));
             //Verifico si hay compatibilidad
             if(myViaje.hayCompatibilidad(cant_pasajes,clase)){
                myVenta = new Venta();
                myVenta.setHorario(horario);
                myVenta.setTipoAsiento(clase);
                myVenta.setTrayecto(origen, destino);
                gestionarPasaje();//Asign los pasajes uno a uno
                System.out.println("¿Desea comprar pasaje de vuelta?");
                System.out.println("1.- Si");
                System.out.println("2.- No");
                selec = input.nextInt();
                if(selec==1){//Si compra pasaje de vuelta
                    String aux = origen;
                    origen = destino;
                    destino = aux;
                    myViaje = buscador(origen, destino, horario);
                    if(myViaje.hayCompatibilidad(cant_pasajes,clase)){
                        //Si hay disponibilidad gestiono el viaje de vuelta y hago el descuento
                        gestionarPasaje();
                        myVenta.descuentoVuelta();
                    }
                    else
                        System.out.println("No hay asientos disponibles para el viaje de vuelta, solo quedan "+myViaje.asientosDisponibles(clase));  
                }
                ventas.add(myVenta);//Agrego las ventas a un arrayList
                System.out.println("Total a pagar: "+myVenta.getTotal());
             }
             else
               System.out.println("No hay asientos disponibles para este viaje, solo quedan "+myViaje.asientosDisponibles(clase));
             
             System.out.println("¿Salir del programa?");
             System.out.println("1.- Si");
             System.out.println("2.- No");
             selec = input.nextInt();
             if(selec==1)
                 salir=true;      
         }
         infoEstadistica();
        
        
    }
    public void gestionarPasaje(){
        int asiento; 
        int i=0;
        System.out.println(myViaje.descripcion()+" en "+myVenta.getTipoAsiento());
        System.out.println("cantidad de asientos disponibles: "+myViaje.asientosDisponibles(clase));
        myViaje.showAsientos(clase);
        while(i<cant_pasajes){
            System.out.print("numero de asiento : ");
            asiento = input.nextInt();
            if(myViaje.asignarAsiento(clase, asiento)){
                if(isFirstTwoRows(asiento))
                    myVenta.sumTotal(sumCincoPorciento(myViaje.getPrecio(clase)));
                else
                    myVenta.sumTotal(myViaje.getPrecio(clase));
                System.out.println("Asiento asignado");
                contador_pasajes_vendidos++;
                i++;
            }
            else
                System.out.println("Asiento no disponible");
            System.out.println(myViaje.descripcion()+" en "+myVenta.getTipoAsiento());
            System.out.println("cantidad de asientos disponibles: "+myViaje.asientosDisponibles(clase));
            myViaje.showAsientos(clase);
        }
       
    }
    //Detecta si el pasaje esta en las dos primeras filas
    public boolean isFirstTwoRows(int asiento){
        return asiento <= 8;
    }
    //le suma un 5% al valor de ese pasaje 
    public double sumCincoPorciento(double precio){
        double aumento;
        aumento = (5*precio)/100;
        return precio+aumento;
    }
    public void infoEstadistica(){
        //tres ventas mas altas
        Venta[] ventas_mas_altas;
        //Monto total por tipo de asiento, trayecto y horario
        double[] m_tipo_asiento = new double[]{0.0,0.0,0.0};//1, 2, 3
        double[] m_horario = new double[]{0.0,0.0,0.0,0.0};//9, 12, 15, 20
        double[] m_Trayecto = new double[]{0.0,0.0,0.0};//Temuco-Concepción, Temuco-Santiago, Temuco-La Serena
        for (Venta venta : ventas) {
            //total por tipo de asiento
            if(venta.getTipoAsiento().equals("primera clase"))
                m_tipo_asiento[0]+=venta.getTotal();
            if(venta.getTipoAsiento().equals("clase ejecutiva"))
                m_tipo_asiento[1]+=venta.getTotal();
            if(venta.getTipoAsiento().equals("clase economica"))
                m_tipo_asiento[2]+=venta.getTotal();
            //total por horario
            if(venta.getHorario() == 9)
                m_horario[0]+=venta.getTotal();
            if(venta.getHorario() == 12)
                m_horario[1]+=venta.getTotal();
            if(venta.getHorario() == 15)
                m_horario[2]+=venta.getTotal();
            if(venta.getHorario() == 20)
                m_horario[3]+=venta.getTotal();
            //total por trayecto
            if(venta.getTrayecto().equals("temuco-santiago") || venta.getTrayecto().equals("santiago-temuco"))
                m_Trayecto[0]+=venta.getTotal();
            if(venta.getTrayecto().equals("temuco-concepcion") || venta.getTrayecto().equals("concepcion-temuco"))
                m_Trayecto[1]+=venta.getTotal();
            if(venta.getTrayecto().equals("temuco-serena") || venta.getTrayecto().equals("serena-temuco"))
                m_Trayecto[2]+=venta.getTotal();
        }
        ventas_mas_altas = tresVentasAltas();
        
        System.out.println("\nCantidad de pasajes vendidos: "+contador_pasajes_vendidos);
        
        System.out.println("Total por tipo de asiento");
        System.out.println("primera clase = $"+ m_tipo_asiento[0]);
        System.out.println("clase ejecutiva = $"+ m_tipo_asiento[1]);
        System.out.println("clase economica = $"+ m_tipo_asiento[2]);
        
        System.out.println("Total por Horario");
        for(int i=0; i<m_horario.length;i++){
            System.out.println(horarios[i]+":00 = $"+m_horario[i]);
        }
        System.out.println("Total por trayecto");
        System.out.println("temuco-santiago = $"+ m_Trayecto[0]);
        System.out.println("temuco-concepcion = $"+ m_Trayecto[1]);
        System.out.println("temuco-serena = $"+ m_Trayecto[2]);
        
        if(ventas.size()>= 3){
            System.out.println("Tres ventas mas altas");
            for (int i=0; i<ventas_mas_altas.length;i++){
                System.out.println(i+1+" Venta = $"+ventas_mas_altas[i].getTotal());
                System.out.println("Tipo de asiento = "+ventas_mas_altas[i].getTipoAsiento());
                System.out.println("Trayecto = "+ventas_mas_altas[i].getTrayecto());
                System.out.println("Horario = "+ventas_mas_altas[i].getHorario()+":00");
            }
        }
       
        
    }
    public Venta[] tresVentasAltas(){
        double mas_alta = ventas.get(0).getTotal();
        int i_mas_alta = 0;
        double segunda_mas_alta = 0;
        int i_segunda_mas_alta=0;
        double tercera_mas_alta = 0;
        Venta[] tres_ventas_altas = new Venta[3]; 
        for(int i=0 ; i<ventas.size(); i++){//Obtengo la venta mas alta
            if(ventas.get(i).getTotal()>mas_alta){
                mas_alta = ventas.get(i).getTotal();
                tres_ventas_altas[0] = ventas.get(i);
                i_mas_alta = i;
            }
        }
        for(int i=0 ; i<ventas.size(); i++){//Obtengo la segundaventa mas alta
            if(i!=i_mas_alta && ventas.get(i).getTotal()>segunda_mas_alta){
                segunda_mas_alta = ventas.get(i).getTotal();
                tres_ventas_altas[1] = ventas.get(i);
                i_segunda_mas_alta = i;
            }
        }
        for(int i=0 ; i<ventas.size(); i++){//Obtengo la segundaventa mas alta
            if(i!=i_mas_alta && i!=i_segunda_mas_alta && ventas.get(i).getTotal()>tercera_mas_alta){
                tercera_mas_alta = ventas.get(i).getTotal();
                tres_ventas_altas[2] = ventas.get(i);
            }
        }
        //double[] tres_ventas_altas = new double[]{mas_alta,segunda_mas_alta,tercera_mas_alta};
        return tres_ventas_altas;
    }
    public Viaje buscador(String origen, String destino, int horario){
        Viaje myViaje = new Viaje();
        for(int i=0; i<horarios.length;i++ ){
            if(temuco_concepcion[i].getOrigen().equals(origen) && temuco_concepcion[i].getDestino().equals(destino) && temuco_concepcion[i].getHorario()==horario)
                myViaje =temuco_concepcion[i];
            if(concepcion_temuco[i].getOrigen().equals(origen) && concepcion_temuco[i].getDestino().equals(destino) && concepcion_temuco[i].getHorario()==horario)
                myViaje =concepcion_temuco[i];
            
            if(temuco_santiago[i].getOrigen().equals(origen) && temuco_santiago[i].getDestino().equals(destino) && temuco_santiago[i].getHorario()==horario)
                myViaje =temuco_santiago[i];
            if(santiago_temuco[i].getOrigen().equals(origen) && santiago_temuco[i].getDestino().equals(destino) && santiago_temuco[i].getHorario()==horario)
                myViaje =santiago_temuco[i];
            
            if(temuco_serena[i].getOrigen().equals(origen) && temuco_serena[i].getDestino().equals(destino) && temuco_serena[i].getHorario()==horario)
                myViaje =temuco_serena[i];
            if(serena_temuco[i].getOrigen().equals(origen) && serena_temuco[i].getDestino().equals(destino) && serena_temuco[i].getHorario()==horario)
                myViaje =serena_temuco[i];
            
        }
        
        return myViaje;
    }
    
    public static void main(String args[]){
        Main app = new Main();
        
    }
    
    public char[][] myAsientos(){
        
        char[][] asientos = new char[][]{
            {'L','L','L','L'},//primera clase
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},//segunda clase
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},//tercera clase
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'},
            {'L','L','L','L'}
            
        };
        
        return asientos;
    }
}
