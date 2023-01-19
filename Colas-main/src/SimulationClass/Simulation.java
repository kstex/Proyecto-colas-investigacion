package SimulationClass;

import DataClass.Client;
import DataClass.DataEntry;
import DataClass.DataOut;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class Simulation {

    //Datos de estadisticas
    //DataStatistics dataStatistics = new DataStatistics();
    //Datos de cliente
    /*Client client = new Client();
    Client clientout = new Client();*/
    // conteo segun tipo de eventos
    // private int[] arrivalNumber;   //evento de llegada segun servidor
    // private int[] departureNumber; //evento de salida segun servidor
    // Cantidad total de evento
    /*Estadisticas 
    
    private int noWait;          //Cantidad de clientes que no esperan
    private int unAttendance;    //Cantidad de clientes que se van sin ser atendidos
    
    private double wait;            //Cantidad total de clientes que espera
    private double probWait;        // Probabilidad de esperar
    
    //Tiempo que pasa un Cliente en el sistema
    
    
    //Tiempo que pasa un Cliente en Línea de Espera
    private double[] clientWaitingTime; 
    private double totalClientWaitingTime;
    
    //Número promedio de Clientes en el sistema
    private double [] promClient;
    private double  totalPromClient;
    
    //Número promedio de Clientes en Línea de Espera
    private double [] promWaitingClient;
    private double  totalPromWaitingClient;
    
    //Porcentaje de utilización de cada servidor y general
    private double [] serverUsage;
    private double  totalserverUsage;
    
    //Tiempo promedio adicional que se trabaja después de cerrar
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
    // Datos de entrada
    DataEntry dataEntry = new DataEntry();

    //Lista de clientes
    LinkedList<Client> queueSim = new LinkedList<>();

    //Datos de simulacion 
    private int eventNumber;        //numero de eventos segun servidor 
    private int caso;
    private String eventType;
    private long timeModeling;    //Tiempo a modelar
    private long[][] serverStatus;  // [i][0]Numero de cliente / [i][1]tiempo de llegada / [i][2]tiempo de salida / [i][3]tiempo de servicio
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


    private double clientSystemTime;

    private int extra;

    public DataOut Simulation(DataEntry dataEntry) {

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
        DataOut dataOut = new DataOut();

        this.eventNumber = 0;
        this.dataEntry = dataEntry;

        this.timeModeling = 0;
        this.caso = 0;
        this.clientNumber = 0;

        this.totalClient = 0;

        this.clientSystemTime = 0;

        long clientOutNumber = 0;

        /*             Ejemplo de tabla
         No Evento /// Tipo Evento /// TM /// SS1-2... /// WL /// Cantidad Clientes /// AT /// DT1-2... /// No Random llegada /// TELL
                                                                                                            /// No Random Servicio /// TS
         */
        this.serverStatus = new long[dataEntry.getQuantityServers()][4];
        for (int i = 0; i < serverStatus.length; i++) {
            for (int j = 0; j < 4; j++) {
                this.serverStatus[i][j] = 0;
            }
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

        System.out.print("|| Evento N° || Tipo de Evento || N° Cliente || TM ||");
        for (int i = 0; i < serverStatus.length; i++) {
            System.out.print(" SS" + i + " ||");
        }
        System.out.print(" WL || Cant Clientes || AT ||");

        for (int i = 0; i < serverStatus.length; i++) {
            System.out.print(" DT" + i + " ||");
        }

        System.out.println(" N° R. TELL || TELL || N° R. TS || TS ||");

        System.out.print("|| " + eventNumber + "         || Cond. Inic. || " + clientNumber + "           || " + timeModeling + "  ||");
        for (int i = 0; i < serverStatus.length; i++) {
            System.out.print(" " + serverStatus[i][0] + "  ||");
        }
        System.out.print(" " + totalClient + "  || " + queueSim.size() + "           || " + arrivalTime + "  ||");

        for (int i = 0; i < serverStatus.length; i++) {
            System.out.print(" " + departureTime[i] + "  ||");
        }

        System.out.println(" " + randomNumberTELL + "          || " + numberTELL + "    || " + randomNumberTS + "        || " + numberTS + "  ||");

        /*DefaultTableModel eventModelTable = new DefaultTableModel(null, this.getTitles());
        eventModelTable.addRow(
                this.addRow(
                        eventNumber, "Cond. Inic.", clientNumber, timeModeling, serverStatus, totalClient,
                        clientList.size(), arrivalTime, departureTime, randomNumberTELL, numberTELL, randomNumberTS, numberTS
                )
        );*/
        do {
            eventNumber++;

            //Determino el tipo de evento
            for (int i = 0; i < departureTime.length; i++) {
                if (departureTime[i] < dt) {
                    dt = departureTime[i];
                    posDT = i;
                }
            }

            /* calculo de Ti en L
            if (flagOut && totalClient > 0) {
                flagOut = false;
                if (arrivalTime < dt) {
                    queueSim.set((int) totalClient - 1, queueSim.get((int) totalClient - 1) + (timeModeling - arrivalTime));
                    //System.out.println("cliente: " + totalClient + "calculo salida: " + totalClientSystemTime.get((int) totalClient - 1));
                } else {
                    queueSim.set((int) totalClient - 1, queueSim.get((int) totalClient - 1) + (timeModeling - dt));
                    //System.out.println("cliente: " + totalClient + "calculo salida: " + totalClientSystemTime.get((int) totalClient - 1));
                }
            }*/
            flagBusy = true;
            for (int i = 0; i < departureTime.length; i++) {
                if (departureTime[i] == 9999) {
                    flagBusy = false;
                    break;
                }
            }

            //Llegada
            if (arrivalTime < dt) {

                clientNumber++;
                totalClient++;

                //oldTimeModeling = timeModeling;
                timeModeling = arrivalTime;
                
                this.randomNumberTELL = (int) (Math.random() * 99) + 1;
                this.numberTELL = this.assignmentAT(dataEntry, randomNumberTELL);

                arrivalTime = arrivalTime + numberTELL;

                if (!queueSim.isEmpty() || flagBusy) {
                    this.queueSim.add(new Client(clientNumber,arrivalTime));

                    randomNumberTS = 0;
                    numberTS = 0;
                } else {
                    for (int i = 0; i < serverStatus.length; i++) {
                        if (serverStatus[i][0] == 0) {

                            serverStatus[i][0] = clientNumber;

                            this.randomNumberTS = (int) (Math.random() * 99) + 1;
                            this.numberTS = this.assignmentTS(dataEntry, randomNumberTS);

                            departureTime[i] = timeModeling + (long) numberTS;

                            break;
                        }
                    }
                }

                

                /* calculo de Ti en L
                if (totalClient > 0) {
                    if (queueSim.size() < totalClient) {
                        queueSim.add(arrivalTime - timeModeling);
                        //System.out.println("cliente: " + totalClient + "calculo llegada: " + totalClientSystemTime.get((int) totalClient - 1));
                    } else {
                        queueSim.set((int) totalClient - 1, queueSim.get((int) totalClient - 1) + (arrivalTime - timeModeling));
                        //System.out.println("cliente: " + totalClient + "calculo llegada: " + totalClientSystemTime.get((int) totalClient - 1));
                    }
                }*/
 /*
                eventModelTable.addRow(
                        this.addRow(
                                eventNumber, "Llegada", clientNumber, timeModeling, serverStatus, totalClient,
                                clientList.size(), arrivalTime, departureTime, randomNumberTELL, numberTELL, randomNumberTS, numberTS
                        )
                );*/
                System.out.print("|| " + eventNumber + "         || Llegada || " + clientNumber + "           || " + timeModeling + "  ||");
                for (int i = 0; i < serverStatus.length; i++) {
                    System.out.print(" " + serverStatus[i][0] + "  ||");
                }
                System.out.print(" " + totalClient + "  || " + queueSim.size() + "           || " + arrivalTime + "  ||");

                for (int i = 0; i < serverStatus.length; i++) {
                    System.out.print(" " + departureTime[i] + "  ||");
                }

                System.out.println(" " + randomNumberTELL + "          || " + numberTELL + "    || " + randomNumberTS + "        || " + numberTS + "  ||");

            } else {  //Salida

                totalClient--;
                flagOut = true;
                timeModeling = departureTime[posDT];

                if (!queueSim.isEmpty()) {
                    serverStatus[posDT][0] = queueSim.remove().getClientNumber();
                    

                    this.randomNumberTS = (int) (Math.random() * 99) + 1;
                    this.numberTS = this.assignmentTS(dataEntry, randomNumberTS);

                    departureTime[posDT] = timeModeling + (long) numberTS;

                } else {
                    clientOutNumber = serverStatus[posDT][0];
                    departureTime[posDT] = 9999;
                    dt = 9999;
                    serverStatus[posDT][0] = 0;
                }

                /*
                eventModelTable.addRow(
                        this.addRow(
                                eventNumber, "Salida", clientOutNumber, timeModeling, serverStatus, totalClient,
                                clientList.size(), arrivalTime, departureTime, 0, 0, randomNumberTS, numberTS
                        )
                );*/
                System.out.print("|| " + eventNumber + "         || Salida  || " + clientOutNumber+ "           || " + timeModeling + "  ||");
                for (int i = 0; i < serverStatus.length; i++) {
                    System.out.print(" " + serverStatus[i][0] + "  ||");
                }
                System.out.print(" " + totalClient + "  || " + queueSim.size() + "           || " + arrivalTime + "  ||");

                for (int i = 0; i < serverStatus.length; i++) {
                    System.out.print(" " + departureTime[i] + "  ||");
                }

                System.out.println(" " + randomNumberTELL + "          || " + numberTELL + "    || " + randomNumberTS + "        || " + numberTS + "  ||");
            }

        } while (timeModeling <= dataEntry.getTimeSimulation());

        /*
        long additionL = 0;
        
        for (int i = 0; i < queueSim.size(); i++) {
            additionL = additionL + (i + 1 * queueSim.get(i));
            //System.out.println("totalClientSystemTime: " + totalClientSystemTime.get(i));
            //System.out.println("additionL: " + additionL);
        }

        clientSystemTime = additionL / timeModeling;
         */
        //dataOut.setEventTable(eventModelTable);

        /*
        System.out.println("Cantidad promedio de clientes en el sistema: " + clientSystemTime);
        System.out.println("Cantidad promedio de clientes en cola : ");
        System.out.println("Tiempo promedio de un cliente en cola y en el sistema: ");
        System.out.println("Tiempo promedio adicional que se trabaja después de cerrar: ");
        System.out.println("Porcentaje de utilización de cada servidor y general: ");
        System.out.println("Costos: servidores y cliente: ");*/
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

    }

    private String[] getTitles() {
        int ntitles = 4 + this.dataEntry.getQuantityServers() + 3 + this.dataEntry.getQuantityServers() + 4;
        int contTitles = 0;
        String[] titles = new String[ntitles];

        titles[contTitles] = "Evento N°";
        contTitles++;

        titles[contTitles] = "tipo de Evento";
        contTitles++;

        titles[contTitles] = "N° Cliente";
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
        System.out.format("Número promedio de Clientes en el sistema:%.2f",totalPromClient);System.out.println("");
        
        for ( int j = 0; j < dataEntry.getQuantityServers(); j++) {
            this.totalPromWaitingClient = this.totalPromWaitingClient+this.promWaitingClient[j];
        }
        this.totalPromWaitingClient = this.totalPromWaitingClient / dataEntry.getTimeSimulation();
        System.out.format("Número promedio de Clientes en Línea de Espera:%.2f",Math.abs(totalPromWaitingClient));System.out.println("");
        
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
        System.out.format("Tiempo promedio adicional que se trabaja después de cerrar:%.2f",totalPromExtraClient);System.out.println(""); 
        
    
        //PROMEDIO DE USO DEL SERVIDOR
        for ( int j = 0; j < dataEntry.getQuantityServers(); j++) {
            this.totalserverUsage = this.totalserverUsage+(this.serverUsage[j]/dataEntry.getTimeSimulation());
        }
        System.out.format("Porcentaje de utilización del sistema: %.2f",(this.totalserverUsage ));System.out.println(""); 
        
        for ( int j = 0; j < dataEntry.getQuantityServers(); j++) {
            this.totalserverExtraUsage = this.totalserverExtraUsage+(this.serverExtraUsage[j]/dataEntry.getTimeSimulation());
        }
        System.out.format("Porcentaje de utilización del sistema: %.2f",(this.totalserverUsage *100 ));System.out.println(""); 

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
}
