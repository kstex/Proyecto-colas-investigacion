
package SimulationClass;

import java.util.LinkedList;

/**
 * 
 */
public class Client {
    //datos de cliente
    private int clientNumber;
    private int clienteArrivalTime;
    private int clienteServiceTime;
    private int clientDepartureTime;
    
    public Client () {
       this.clientNumber= 0;
       this.clienteArrivalTime = 0;
       this.clienteServiceTime = 0;
       this.clientDepartureTime = Integer.MAX_VALUE;
    }
     
    public Client (int clientNumber ,int clienteArrivalTime, int clienteServiceTime,int clientDepartureTime) {
       this.clientNumber = clientNumber;
       this.clienteArrivalTime = clienteArrivalTime;
       this.clienteServiceTime = clienteServiceTime;
       this.clientDepartureTime = clientDepartureTime;
    }
    
    //Setters
    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber ;
    }
    
    public void setClientArrivalTime(int clienteArrivalTime) {
        this.clienteArrivalTime = clienteArrivalTime ;
    }
    
    public void setClientServiceTime (int clienteServiceTime ) {
        this.clienteServiceTime  = clienteServiceTime  ;
    }
    
    public void setClientDepartureTime (int clientDepartureTime ) {
        this.clientDepartureTime  = clientDepartureTime  ;
    }
    //Getters
    public int getClientNumber() {
        return clientNumber ;
    }
    
    public int getClientArrivalTime() {
        return clienteArrivalTime ;
    }
    
    public int getClientServiceTime() {
        return clienteServiceTime ;
    }
    
    public int getClientDepartureTime() {
        return clientDepartureTime ;
    }
    
    public void printClient() {
        System.out.print("Nº "+getClientNumber()+ "| ");
        System.out.print("AT: " +getClientArrivalTime()+ "| ");
        System.out.print("DT: "+getClientDepartureTime()+ "| ");
        System.out.print("TS: "+getClientServiceTime()+ "| ");
    }
    
    public void printWaitClient() {
        System.out.print("Nº "+getClientNumber()+ "| ");
        System.out.print("AT: " + getClientArrivalTime()+ "| ");
    }
 
}
