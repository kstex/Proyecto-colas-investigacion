package SimulationClass;

import DataClass.DataEntry;
import DataClass.DataOut;
import SimulationClass.Client;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class Simulation {

    // Datos de entrada
    DataEntry dataEntry = new DataEntry();

    //Datos de estadisticas
    //DataStatistics dataStatistics = new DataStatistics();
    //Datos de cliente
    /*Client client = new Client();
    Client clientout = new Client();*/
    //Lista de clientes
    LinkedList<Long> clientList = new LinkedList<>();

    //Datos de simulacion 
    private int eventNumber;        //numero de eventos segun servidor 
    private int caso;
    private String eventType;
    private long timeModeling;    //Tiempo a modelar
    private long[] serverStatus;  //Cantidad de servidores  
    //private double waitingLength;      //lista de espera segun servidores
    private long clientNumber;    //Cantidad de clientes que han pasado por la simulacion
    private long totalClient;     //Cantidad total de clientes
    private int randomNumberTELL;
    private int numberTELL;
    private int randomNumberTS;
    private int numberTS;

    //Tiempo de llegada y salida segun servidor 
    private long arrivalTime;   //evento de llegada segun servidor
    private long[] departureTime; //evento de salida segun servidor

    private int nextArrivalTime;   //timpo de la siguiente llegada segun servidor

    private int serviceTime;   //tiempo de servicio segun servidor

    LinkedList<Long> totalClientSystemTime = new LinkedList<>();

    private double clientSystemTime;

    // conteo segun tipo de eventos
    // private int[] arrivalNumber;   //evento de llegada segun servidor
    // private int[] departureNumber; //evento de salida segun servidor
    // Cantidad total de evento
    private int extra;

    /*Estadisticas 
    
    private int noWait;          //Cantidad de clientes que no esperan
    private int unAttendance;    //Cantidad de clientes que se van sin ser atendidos
    
    private double wait;            //Cantidad total de clientes que espera
    private double probWait;        // Probabilidad de esperar
    
    //Tiempo que pasa un Cliente en el sistema
    
    
    //Tiempo que pasa un Cliente en L??nea de Espera
    private double[] clientWaitingTime; 
    private double totalClientWaitingTime;
    
    //N??mero promedio de Clientes en el sistema
    private double [] promClient;
    private double  totalPromClient;
    
    //N??mero promedio de Clientes en L??nea de Espera
    private double [] promWaitingClient;
    private double  totalPromWaitingClient;
    
    //Porcentaje de utilizaci??n de cada servidor y general
    private double [] serverUsage;
    private double  totalserverUsage;
    
    //Tiempo promedio adicional que se trabaja despu??s de cerrar
    private double [] promExtraClient;
    private double  totalPromExtraClient;
    
    //Porcentaje extra de Servidor
    private double[] serverExtraUsage;
    private double  totalserverExtraUsage;
    
    //tiempo de espera segun servidores
    private int[] unusedTime;    //tiempo de ocio segun servidores
    
    //Manejo del tiempo
    private int day ;
    private int laborHour;
    private int clock;*/
    public DataOut Simulation(DataEntry dataEntry) {

        DataOut dataOut = new DataOut();

        this.eventNumber = 0;
        this.dataEntry = dataEntry;

        this.timeModeling = 0;
        this.caso = 0;
        this.clientNumber = 0;

        this.totalClient = 0;

        this.clientSystemTime = 0;

        long clientOutNumber = 0;
        /*this.noWait = 0;
        this.totalClient = 0;
        this.wait = 0;
        this.probWait = 0;
        this.totalClientSystemTime = 0;
        this.totalClientWaitingTime = 0;
        this.totalPromWaitingClient = 0;
        this.totalserverUsage = 0;
        
        this.totalPromExtraClient = 0;
        this.totalPromClient = 0 ;
        this.totalserverExtraUsage = 0 ;
        
        this.day =1440 ;
        this.laborHour = 600;
        this.clock =0;*/
        //Modificar getTitles


        /*             Ejemplo de tabla
         No Evento /// Tipo Evento /// TM /// SS1-2... /// WL /// Cantidad Clientes /// AT /// DT1-2... /// No Random llegada /// TELL
                                                                                                            /// No Random Servicio /// TS
         */
        this.serverStatus = new long[dataEntry.getQuantityServers()];
        for (int i = 0; i < dataEntry.getQuantityServers(); i++) {
            this.serverStatus[i] = 0;
        }

        this.departureTime = new long[dataEntry.getQuantityServers()];
        for (int i = 0; i < dataEntry.getQuantityServers(); i++) {
            this.departureTime[i] = 9999;
        }

        long dt = 9999;
        int posDT = 0;
        long oldTimeModeling = 0;
        boolean flagBusy;
        boolean flagOut = false;

        DefaultTableModel eventModelTable = new DefaultTableModel(null, this.getTitles());
        eventModelTable.addRow(
                this.addRow(
                        eventNumber, "Cond. Inic.", clientNumber, timeModeling, serverStatus, totalClient,
                        clientList.size(), arrivalTime, departureTime, randomNumberTELL, numberTELL, randomNumberTS, numberTS
                )
        );

        do {
            eventNumber++;

            //Determino el tipo de evento
            for (int i = 0; i < departureTime.length; i++) {
                if (departureTime[i] < dt) {
                    dt = departureTime[i];
                    posDT = i;
                }
            }

            //calculo de Ti en L
            if (flagOut && totalClient > 0) {
                flagOut = false;
                if (arrivalTime < dt) {
                    totalClientSystemTime.set((int) totalClient - 1, totalClientSystemTime.get((int) totalClient - 1) + (timeModeling - arrivalTime));
                    //System.out.println("cliente: " + totalClient + "calculo salida: " + totalClientSystemTime.get((int) totalClient - 1));
                } else {
                    totalClientSystemTime.set((int) totalClient - 1, totalClientSystemTime.get((int) totalClient - 1) + (timeModeling - dt));
                    //System.out.println("cliente: " + totalClient + "calculo salida: " + totalClientSystemTime.get((int) totalClient - 1));
                }
            }

            flagBusy = true;
            for (int i = 0; i < departureTime.length; i++) {
                if (departureTime[i] == 9999) {
                    flagBusy = false;
                    break;
                }
            }

            //Llegada
            if (arrivalTime < departureTime[posDT]) {

                clientNumber++;
                totalClient++;

                //oldTimeModeling = timeModeling;
                timeModeling = arrivalTime;

                if (!clientList.isEmpty() || flagBusy) {
                    this.clientList.add(clientNumber);

                    randomNumberTS = 0;
                    numberTS = 0;
                } else {
                    for (int i = 0; i < serverStatus.length; i++) {
                        if (serverStatus[i] == 0) {

                            serverStatus[i] = clientNumber;

                            this.randomNumberTS = (int) (Math.random() * 99) + 1;
                            this.numberTS = this.assignmentTS(dataEntry, randomNumberTS);

                            departureTime[i] = timeModeling + (long) numberTS;

                            break;
                        }
                    }
                }

                this.randomNumberTELL = (int) (Math.random() * 99) + 1;
                this.numberTELL = this.assignmentAT(dataEntry, randomNumberTELL);

                arrivalTime = arrivalTime + numberTELL;

                //calculo de Ti en L
                if (totalClient > 0) {
                    if (totalClientSystemTime.size() < totalClient) {
                        totalClientSystemTime.add(arrivalTime - timeModeling);
                        //System.out.println("cliente: " + totalClient + "calculo llegada: " + totalClientSystemTime.get((int) totalClient - 1));
                    } else {
                        totalClientSystemTime.set((int) totalClient - 1, totalClientSystemTime.get((int) totalClient - 1) + (arrivalTime - timeModeling));
                        //System.out.println("cliente: " + totalClient + "calculo llegada: " + totalClientSystemTime.get((int) totalClient - 1));
                    }
                }

                eventModelTable.addRow(
                        this.addRow(
                                eventNumber, "Llegada", clientNumber, timeModeling, serverStatus, totalClient,
                                clientList.size(), arrivalTime, departureTime, randomNumberTELL, numberTELL, randomNumberTS, numberTS
                        )
                );
            } else {  //Salida

                totalClient--;
                flagOut = true;
                timeModeling = departureTime[posDT];

                if (!clientList.isEmpty()) {
                    serverStatus[posDT] = clientList.remove();

                    this.randomNumberTS = (int) (Math.random() * 99) + 1;
                    this.numberTS = this.assignmentTS(dataEntry, randomNumberTS);

                    departureTime[posDT] = timeModeling + (long) numberTS;

                } else {
                    clientOutNumber = serverStatus[posDT];
                    departureTime[posDT] = 9999;
                    dt = 9999;
                    serverStatus[posDT] = 0;
                }

                eventModelTable.addRow(
                        this.addRow(
                                eventNumber, "Salida", clientOutNumber, timeModeling, serverStatus, totalClient,
                                clientList.size(), arrivalTime, departureTime, 0, 0, randomNumberTS, numberTS
                        )
                );
            }

        } while (timeModeling <= dataEntry.getTimeSimulation());

        long additionL = 0;

        for (int i = 0; i < totalClientSystemTime.size(); i++) {
            additionL = additionL + (i + 1 * totalClientSystemTime.get(i));
            //System.out.println("totalClientSystemTime: " + totalClientSystemTime.get(i));
            //System.out.println("additionL: " + additionL);
        }

        clientSystemTime = additionL / timeModeling;

        dataOut.setEventTable(eventModelTable);

        System.out.println("Cantidad promedio de clientes en el sistema: " + clientSystemTime);
        System.out.println("Cantidad promedio de clientes en cola : ");
        System.out.println("Tiempo promedio de un cliente en cola y en el sistema: ");
        System.out.println("Tiempo promedio adicional que se trabaja despu??s de cerrar: ");
        System.out.println("Porcentaje de utilizaci??n de cada servidor y general: ");
        System.out.println("Costos: servidores y cliente: ");

        return dataOut;
    }

    public int assignmentAT(DataEntry data, int random) {

        float[] p = new float[data.getArrivedCustomers().getRowCount()];

        DefaultTableModel table = data.getArrivedCustomers();

        float acumProb = 0;
        for (int i = 0; i < p.length; i++) {
            p[i] = acumProb + (Float.parseFloat((String) table.getValueAt(i, 1)) * 100);
            acumProb = p[i];
        }

        for (int i = 0; i < p.length; i++) {
            if (p[i] > random) {
                return Integer.parseInt((String) table.getValueAt(i, 0));
            }
        }

        return 0;
    }

    public int assignmentTS(DataEntry data, int random) {

        float[] p = new float[data.getServiceTime().getRowCount()];

        DefaultTableModel table = data.getServiceTime();
        float acumProb = 0;
        for (int i = 0; i < p.length; i++) {
            p[i] = acumProb + (Float.parseFloat((String) table.getValueAt(i, 1)) * 100);
            acumProb = p[i];
        }

        for (int i = 0; i < p.length; i++) {
            if (p[i] > random) {
                return Integer.parseInt((String) table.getValueAt(i, 0));
            }
        }

        return 0;

        /* int[] p = new int[4];
        p[0] = 10;
        p[1] = 40;
        p[2] = 65;
        p[2] = 100;

        int[] v = new int[4];
        v[0] = 15;
        v[1] = 17;
        v[2] = 20;
        p[3] = 25;

        int num = 0;
        int i = 0;
        int b = (int) (Math.random() * 99);
        while (num == 0) {
            if (b < (p[i])) {
                num = v[i];
            }
            i++;
        }
        return num;*/
    }

    /*
    public DataOut process() {

        DataOut dataOut = new DataOut();
        String eventName = "condicion inicial";
        int i = 0;
        DefaultTableModel eventModelTable = new DefaultTableModel(null, this.getTitles());
         eventModelTable.addRow(this.addRow(this.eventNumber[i],eventName,0,timeModeling,this.serverStatus,this.waitingLength[i],
                                            0,0,this.nextArrivalTime[i],this.serviceTime[i]
                                            )
                                );   
        
        do {
            for( i = 0; i < this.dataEntry.getQuantityServers(); i++ ){
                
                /**
                 * this.day =1440 ;
                 * this.laborHour = 600;
                 * this.clock =0;
                ****
                
                //MANEJO DEL DIA 

                if(this.clock <= this.laborHour){
                    
                    if( this.arrivalTime[i] <= this.departureTime[i] ){ 
                   
                        //PROCESANDO UNA LLEGADA
                        this.eventNumber[i]= this.eventNumber[i]+1;
                        this.arrivalNumber[i] = this.arrivalNumber[i]+1;

                        eventName = "Llegada de cliente";

                        //Establecer TM = AT 
                        this.timeModeling = this.arrivalTime[i];

                        //Revisamos estados de los servidores 
                        for(int j = 0; j < this.dataEntry.getQuantityServers();j++){

                            if(this.serverStatus[j] == 0){

                                //Cambiamos estado del servido j
                                this.serverStatus[j] = 1;

                                //Generamos un tiempo de servicio para generar el tiempo de salida
                                this.serviceTime[i] = this.assignmentTS();

                                //DT = TM + TS
                                this.departureTime[i] = this.timeModeling + this.serviceTime[i];

                                //Asignando caso para poner de primero en la cola 
                                this.caso = 1;

                                //Estadisticas 
                                this.noWait++;

                                break;
                            }else {
                                this.waitingLength[i] = this.waitingLength[i]+1;
                                //Asignando caso para poner de ultimo en la cola
                                this.caso = 2; 

                               //Estadisticas
                               this.unAttendance++;
                               this.wait++;
                            } 
                        }
                        //Generamos tiempo entre llegadas
                        this.nextArrivalTime[i] = this.assignmentAT(); 

                        //actualizar clock al tiempo actual 
                        this.clock = this.clock + this.nextArrivalTime[i];

                        //Establecer AT = TM + TE  
                        this.arrivalTime[i]= (this.timeModeling + this.nextArrivalTime[i]);
                    

                        //Casos para asginar al inicio o al final
                        if (caso == 1){
                            this.clientList[i].addFirst(new Client(this.arrivalNumber[i],this.arrivalTime[i],this.serviceTime[i],this.departureTime[i]));
                        } else if  (caso == 2){
                            this.clientList[i].addLast(new Client(this.arrivalNumber[i],this.arrivalTime[i],0,0));
                        }
   
                    }else if( this.arrivalTime[i] > this.departureTime[i] ){

                        //PROCESANDO UNA SALIDA
                        this.eventNumber[i] = this.eventNumber[i]+1;
                        this.departureNumber[i] = this.departureNumber[i]+1;
                        eventName = "Salida de cliente";

                        //Establecer TM = DT
                        this.timeModeling = this.departureTime[i];
                    
                        // Sacamos el primer elemento de las lista (Cliente actual)porque se va
                        this.clientout = this.clientList[i].pollFirst();
                        
                        //Estadistiscas
                        this.clientSystemTime[i] = this.clientSystemTime[i] + (this.clientout.getClientDepartureTime()/this.departureNumber[i]);
                        this.promClient[i] = this.promClient[i] + (this.clientout.getClientDepartureTime()*this.clientout.getClientNumber()) ;
                        
                        this.serverUsage[i]= this.serverUsage[i]+ this.clientout.getClientServiceTime();
                        
                        if (this.waitingLength[i] > 0) {
                        
                        this.waitingLength[i] = this.waitingLength[i] - 1;

                            //Generamos tiempo de Salida 

                            //Generamos un tiempo de servicio para generar el tiempo de salida
                            this.serviceTime[i] = this.assignmentTS();

                            //Establecemos  DT =  TM + TS
                            this.departureTime[i] = this.timeModeling + this.serviceTime[i];

                            //Asignando TS y DT al primero que esta en la cola... el nuevo cliente que se encuentra servicio ahora
                            this.client = this.clientList[i].pollFirst();
                            this.client.setClientServiceTime(this.serviceTime[i]);
                            this.client.setClientDepartureTime(this.departureTime[i]);
                            this.clientList[i].addFirst(new Client(this.client.getClientNumber(),this.client.getClientArrivalTime(),this.client.getClientServiceTime(),this.client.getClientDepartureTime()));

                            //Estadisticas
                          
                            this.unAttendance--;
                            this.clientWaitingTime[i] = this.clientWaitingTime[i] + ((this.clientout.getClientDepartureTime()- this.client.getClientArrivalTime())/this.client.getClientNumber());

                            this.promWaitingClient[i] = this.promWaitingClient[i] + (this.clientout.getClientDepartureTime()- this.client.getClientArrivalTime());
                        } else if (this.waitingLength[i] == 0) {
                         
                            this.serverStatus[i] = 0;
                            this.departureTime[i] = Integer.MAX_VALUE;
                        }
                    }
               
                    //Impresion de simulacion

                    if ("Llegada de cliente".equals(eventName)&& this.waitingLength[i] == 0){
                        eventModelTable.addRow(this.addRow(this.eventNumber[i],eventName,this.clientList[i].peekFirst().getClientNumber(),timeModeling,
                                               this.serverStatus,this.waitingLength[i],this.clientList[i].peekFirst().getClientArrivalTime(),this.clientList[i].peekFirst().getClientDepartureTime(),
                                                this.nextArrivalTime[i],this.serviceTime[i]
                                                            )
                                               ); 

                    } else if ("Llegada de cliente".equals(eventName)){
                        eventModelTable.addRow(this.addRow(this.eventNumber[i],eventName,this.clientList[i].peekLast().getClientNumber(),timeModeling,
                                               this.serverStatus,this.waitingLength[i],this.clientList[i].peekLast().getClientArrivalTime(),this.clientList[i].peekLast().getClientDepartureTime(),
                                                this.nextArrivalTime[i],0
                                                            )
                                               );

                    } else if ("Salida de cliente".equals(eventName)){
                        eventModelTable.addRow(this.addRow(this.eventNumber[i],eventName,this.clientout.getClientNumber(),timeModeling,
                                               this.serverStatus,this.waitingLength[i],this.clientout.getClientArrivalTime(),this.clientout.getClientDepartureTime(),
                                                0,this.serviceTime[i]
                                                            )
                                               );
                    }  
                
                }else if(this.clock > this.laborHour && this.clock <= this.day  ){
                    
                    if (this.clientList[i].isEmpty() == true){
                        this.clock = Integer.MAX_VALUE; 
                    }else{
                        
                        //PROCESANDO UNA SALIDA
                        this.eventNumber[i] = this.eventNumber[i]+1;
                        this.departureNumber[i] = this.departureNumber[i]+1;
                        eventName = "Salida de cliente";

                        //Establecer TM = DT
                        this.timeModeling = this.departureTime[i];
                    
                        // Sacamos el primer elemento de las lista (Cliente actual)porque se va
                        this.clientout = this.clientList[i].pollFirst();
                        
                        //Estadistiscas
                        this.clientSystemTime[i] = this.clientSystemTime[i] + (this.clientout.getClientDepartureTime()/this.departureNumber[i]);
                        this.promClient[i] = this.promClient[i] + (this.clientout.getClientDepartureTime()*this.clientout.getClientNumber()) ;
                        
                        this.serverExtraUsage[i]= this.serverExtraUsage[i]+ this.clientout.getClientServiceTime();
                        
                        if (this.waitingLength[i] > 0) {
                        
                        this.waitingLength[i] = this.waitingLength[i] - 1;

                            //Generamos tiempo de Salida 

                            //Generamos un tiempo de servicio para generar el tiempo de salida
                            this.serviceTime[i] = this.assignmentTS();

                            //Establecemos  DT =  TM + TS
                            this.departureTime[i] = this.timeModeling + this.serviceTime[i];

                            //Asignando TS y DT al primero que esta en la cola... el nuevo cliente que se encuentra servicio ahora
                            this.client = this.clientList[i].pollFirst();
                            this.client.setClientServiceTime(this.serviceTime[i]);
                            this.client.setClientDepartureTime(this.departureTime[i]);
                            this.clientList[i].addFirst(new Client(this.client.getClientNumber(),this.client.getClientArrivalTime(),this.client.getClientServiceTime(),this.client.getClientDepartureTime()));

                            //Estadisticas
                            this.clientWaitingTime[i] = this.clientWaitingTime[i] + ((this.clientout.getClientDepartureTime()- this.client.getClientArrivalTime())/this.client.getClientNumber());

                            this.promWaitingClient[i] = this.promWaitingClient[i] + (this.clientout.getClientDepartureTime()- this.client.getClientArrivalTime());
                        
                            this.promExtraClient[i] = this.promExtraClient[i] + ((this.clientout.getClientDepartureTime()- this.client.getClientArrivalTime())/this.client.getClientNumber());
                            
                            this.unAttendance--;
                        } else if (this.waitingLength[i] == 0) {
                         
                            this.serverStatus[i] = 0;
                            this.departureTime[i] = Integer.MAX_VALUE;
                        }
                        
                        //Impresion de salida
                        eventModelTable.addRow(this.addRow(this.eventNumber[i],eventName,this.clientout.getClientNumber(),timeModeling,
                                               this.serverStatus,this.waitingLength[i],this.clientout.getClientArrivalTime(),this.clientout.getClientDepartureTime(),
                                                0,this.serviceTime[i]
                                                            )
                                               );      
                    }
                    
                }else if(this.clock > this.day ){
                    this.clock = 0;
                }
 
            } 
        }while(this.timeModeling < this.dataEntry.getTimeSimulation());  
        
        // Impresion de estadisticas
        System.out.println("-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- ESTADISTICAS -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --");
        System.out.println("Cantidad de clientes que no esperan: " + this.noWait);
        System.out.println("Cantidad de clientes que se ven sin ser atendidos: " + this.unAttendance);
        
        for ( int j = 0; j < dataEntry.getQuantityServers(); j++) {
            this.totalClient = this.totalClient + this.arrivalTime[j];
        }
        this.probWait = (this.wait/this.totalClient) * 100;
        System.out.format("Probabilidad de esperar:%.2f",this.probWait);System.out.println(""); 
        
//PROMEDIO DE CANTIDADES
        for ( int j = 0; j < dataEntry.getQuantityServers(); j++) {
            this.totalPromClient = this.totalPromClient+this.promClient[j];
        }
        this.totalPromClient = this.totalPromClient / dataEntry.getTimeSimulation();
        System.out.format("N??mero promedio de Clientes en el sistema:%.2f",totalPromClient);System.out.println("");
        
        for ( int j = 0; j < dataEntry.getQuantityServers(); j++) {
            this.totalPromWaitingClient = this.totalPromWaitingClient+this.promWaitingClient[j];
        }
        this.totalPromWaitingClient = this.totalPromWaitingClient / dataEntry.getTimeSimulation();
        System.out.format("N??mero promedio de Clientes en L??nea de Espera:%.2f",Math.abs(totalPromWaitingClient));System.out.println("");
        
        //PROMEDIO DE TIEMPOS
        for ( int j = 0; j < dataEntry.getQuantityServers(); j++) {
            this.totalClientSystemTime = this.totalClientSystemTime+this.clientSystemTime[j];
        }
        System.out.format("Tiempo promedio de los clientes en el sistema:%.2f",this.totalClientSystemTime);System.out.println(""); 
        
        for ( int j = 0; j < dataEntry.getQuantityServers(); j++) {
            this.totalClientWaitingTime = this.totalClientWaitingTime+this.clientWaitingTime[j];
        }
        System.out.format("Tiempo promedio de los clientes en linea de espera:%.2f",Math.abs(this.totalClientWaitingTime));System.out.println(""); 
        
        for ( int j = 0; j < dataEntry.getQuantityServers(); j++) {
            this.totalPromExtraClient = this.totalPromExtraClient+this.promExtraClient[j];
        }
        System.out.format("Tiempo promedio adicional que se trabaja despu??s de cerrar:%.2f",totalPromExtraClient);System.out.println(""); 
        
    
        //PROMEDIO DE USO DEL SERVIDOR
        for ( int j = 0; j < dataEntry.getQuantityServers(); j++) {
            this.totalserverUsage = this.totalserverUsage+(this.serverUsage[j]/dataEntry.getTimeSimulation());
        }
        System.out.format("Porcentaje de utilizaci??n del sistema: %.2f",(this.totalserverUsage ));System.out.println(""); 
        
        for ( int j = 0; j < dataEntry.getQuantityServers(); j++) {
            this.totalserverExtraUsage = this.totalserverExtraUsage+(this.serverExtraUsage[j]/dataEntry.getTimeSimulation());
        }
        System.out.format("Porcentaje de utilizaci??n del sistema: %.2f",(this.totalserverUsage *100 ));System.out.println(""); 

        //COSTOS
            //SERVIDORES
        double u = ((this.totalserverUsage * dataEntry.getBusyServercost())/5);
        double t = ((this.totalserverExtraUsage * dataEntry.getExtraTimeServerCost())/5);
        
        double totalSeverCost = u+t;
        
        System.out.format("Costo Servidor tiempo Normal: %.2f",(u));System.out.println(""); 
        
        System.out.format("Costo Servidor tiempo Extra: %.2f",(t));System.out.println(""); 

        System.out.format("Costo Total del uso de Servidores: %.2f",(totalSeverCost));System.out.println(""); 
        
            //CLIENTES
        u =(dataEntry.getCostNormalOperation() * totalPromClient)/5;
        t= (totalClientWaitingTime * dataEntry.getExtraOperationCost())/5;
        double totalClientCost = (u + t);
        
        System.out.format("Costo total de Cliente en hora normal: %.2f",(u));System.out.println(""); 
        
        System.out.format("Costo total de Clientes en cola: %.2f",(t));System.out.println(""); 

        System.out.format("Costo total de  operaciones de Clientes : %.2f",(totalClientCost));System.out.println(""); 
        
            //SISTEMA
        System.out.format("Costo total del sistema: %.2f",(totalSeverCost+totalClientCost));System.out.println(""); 

        
        dataOut.setEventTable(eventModelTable);
        return dataOut;
    }*/
    private String[] getTitles() {
        int ntitles = 4 + this.dataEntry.getQuantityServers() + 3 + this.dataEntry.getQuantityServers() + 4;
        int contTitles = 0;
        String[] titles = new String[ntitles];

        titles[contTitles] = "Evento N??";
        contTitles++;

        titles[contTitles] = "tipo de Evento";
        contTitles++;

        titles[contTitles] = "N?? Cliente";
        contTitles++;

        titles[contTitles] = "TM";
        contTitles++;

        for (int i = 0; i < this.dataEntry.getQuantityServers(); i++, contTitles++) {
            titles[contTitles] = "SS " + (i + 1);
        }

        titles[contTitles] = "WL";
        contTitles++;

        titles[contTitles] = "Cantidad Clientes";
        contTitles++;

        titles[contTitles] = "AT";
        contTitles++;

        for (int i = 0; i < this.dataEntry.getQuantityServers(); i++, contTitles++) {
            titles[contTitles] = "DT " + (i + 1);
        }

        titles[contTitles] = "No. Aleatorio p/ TELL";
        contTitles++;

        titles[contTitles] = "TELL";
        contTitles++;

        titles[contTitles] = "No. Aleatorio p/ TS";
        contTitles++;

        titles[contTitles] = "TS";
        contTitles++;

        return titles;
    }

    private Object[] addRow(int eventNumber, String eventName, long clientNumber, long timeModeling, long[] statusServers, long totalClient,
            long queueLenght, long arrivalTime, long[] departureTime, int randomNumberTELL, int numberTELL, int randomNumberTS, int numberTS) {

        ArrayList row = new ArrayList();

        row.add(eventNumber);
        row.add(eventName);
        row.add(clientNumber);
        row.add(timeModeling);

        for (int i = 0; i < statusServers.length; i++) {
            row.add(statusServers[i]);
        }
        row.add(queueLenght);
        row.add(totalClient);
        row.add(arrivalTime);

        for (int i = 0; i < departureTime.length; i++) {
            row.add(departureTime[i]);
        }

        row.add(randomNumberTELL);
        row.add(numberTELL);
        row.add(randomNumberTS);
        row.add(numberTS);

        return row.toArray();
    }
}
