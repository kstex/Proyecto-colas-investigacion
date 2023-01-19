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
    private double PromClientSystem;
    private double clientSystemTime;
    
    // conteo segun tipo de eventos
    // private int[] arrivalNumber;   //evento de llegada segun servidor
    // private int[] departureNumber; //evento de salida segun servidor
    // Cantidad total de evento
    private int extra;

    public DataOut Simulation(DataEntry dataEntry) {

        DataOut dataOut = new DataOut();
        this.eventNumber = 0;
        this.dataEntry = dataEntry;
        this.timeModeling = 0;
        this.caso = 0;
        this.clientNumber = 0;
        this.totalClient = 0;
        this.PromClientSystem = 0;
        long clientOutNumber = 0;

        //Inicializar servidores , depende de la cantidad de servidor que ingresa el usuario
        this.serverStatus = new long[dataEntry.getQuantityServers()];
        for (int i = 0; i < dataEntry.getQuantityServers(); i++) {
            this.serverStatus[i] = 0;
        }
        
        //Inicializar lista de salida , depende de la cantidad de servidor que ingresa el usuario
        this.departureTime = new long[dataEntry.getQuantityServers()];
        for (int i = 0; i < dataEntry.getQuantityServers(); i++) {
            this.departureTime[i] = 9999;
        }

        long dt = 9999;
        long aux=0;
        int posDT = 0;
        long oldTimeModeling = 0;
        boolean flagBusy;
        boolean flagOut = false;

        //Muestra la condicion inicial a la tabla
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
                    //System.out.println("dt:" +dt+ "departureTime[i]:" +departureTime[i]+ "posDT"+posDT);
                }
            }


            flagBusy = true;
            for (int i = 0; i < departureTime.length; i++) {
                if (departureTime[i] == 9999) {
                    flagBusy = false;
                    break;
                }
            }

            long clientsL=totalClient;
            long clientsLq=clientList.size();
            //Llegada
            if (arrivalTime < departureTime[posDT]) {

                clientNumber++;
                totalClient++;            //1
                aux=timeModeling;//0
                timeModeling = arrivalTime;//0

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
                this.randomNumberTELL = (int) (Math.random() * 100);
                this.numberTELL = this.assignmentAT(dataEntry, randomNumberTELL);                      
                arrivalTime = arrivalTime + numberTELL;
              
                eventModelTable.addRow(
                        this.addRow(
                                eventNumber, "Llegada", clientNumber, timeModeling, serverStatus, totalClient,
                                clientList.size(), arrivalTime, departureTime, randomNumberTELL, numberTELL, randomNumberTS, numberTS
                        )
                );

            } else {  //Salida

                totalClient--;
                flagOut = true;   
                aux=timeModeling;
                timeModeling= departureTime[posDT];
                
                if (!clientList.isEmpty()) {
                    serverStatus[posDT] = clientList.remove();

                    this.randomNumberTS = (int) (Math.random() * 100);
                    this.numberTS = this.assignmentTS(dataEntry, randomNumberTS);

                    departureTime[posDT] = timeModeling + (long) numberTS;
                    
                } else {
                    clientOutNumber = serverStatus[posDT];
                    departureTime[posDT] = 9999;
                    dt = 9999;
                    serverStatus[posDT] = 0;
                }
               // System.out.println(arrivalTime);
                eventModelTable.addRow(
                        this.addRow(
                                eventNumber, "Salida", clientNumber, timeModeling, serverStatus, totalClient,
                                clientList.size(), arrivalTime, departureTime, 0, 0, randomNumberTS, numberTS
                        )
                );
            }
        
        //CALCULAR L
        PromClientSystem= ((timeModeling-aux)*clientsL) + (PromClientSystem);
        //System.out.println("L: "+PromClientSystem+" timeModeling "+timeModeling+" aux; "+aux+" clientsL "+(clientsL));
        //*+*+*+*+*+*+*+*+*+*+*++*+*+*+*+*+*+*+*+*+*+*+*+*+*+*
        //CALCULAR LQ
        clientSystemTime = ((timeModeling-aux)*clientsLq) + (clientSystemTime);
        System.out.println("LQ: "+clientSystemTime+" timeModeling "+timeModeling+" aux; "+aux+" clientsLq "+clientsLq);
        

        } while (timeModeling <= dataEntry.getTimeSimulation());

        PromClientSystem = PromClientSystem / timeModeling;
        clientSystemTime = clientSystemTime / timeModeling;
        dataOut.setEventTable(eventModelTable);

        System.out.println("Cantidad promedio de clientes en el sistema: " + PromClientSystem);
        System.out.println("Cantidad promedio de clientes en cola : " + clientSystemTime);
        System.out.println("Tiempo promedio de un cliente en cola y en el sistema: ");
        System.out.println("Tiempo promedio adicional que se trabaja después de cerrar: ");
        System.out.println("Porcentaje de utilización de cada servidor y general: ");
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
}
