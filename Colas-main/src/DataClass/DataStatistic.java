/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataClass;

/**
 *
 * @author Andrea torres y Gustavo Vivas 
 * Clase de las estadisticas de simulacion 
 */
public class DataStatistic {
    
    // Salida de Estadisticas
    
    private int unWaitCustomer;             //Cantidad de clientes que no esperan
    private int  unAttendanceCustomer;      // Cantidad de clientes que se van sin ser atendidos
    private double waitingProbability;      // Probabilidad de esperar (cantidad de clientes que esperan/cantidad total de clientes)
    private double waitingQuantityProm;     //Cantidad promedio de clientes en cola y en el sistema
    private double waitingLengthTimeProm;   // Tiempo promedio de un cliente en cola y en el sistema X
    private double waitingTimeProm;         //Tiempo promedio de espera del cliente que hace cola X
    private double extraWorkTimeProm;       //Tiempo promedio adicional que se trabaja después de cerrar X
    private double useServerPercentage;     //Porcentaje de utilización de cada servidor y general X
    
    //Costos: sistema, servidores y espera/demora del cliente
    private double sistemCost; //x
    private double severCost;
    private double waitCost;

    
    public DataStatistic () {
        this.unWaitCustomer = 0 ;            
        this.unAttendanceCustomer = 0 ;      
        this.waitingProbability = 0;      
        this.waitingQuantityProm = 0;     
        this.waitingLengthTimeProm = 0;   
        this.waitingTimeProm = 0;         
        this.extraWorkTimeProm = 0;       
        this.useServerPercentage = 0;     
        this.sistemCost = 0;
        this.severCost = 0;
        this.waitCost = 0;  
    }
    
    public DataStatistic( int unWaitCustomer, int  unAttendanceCustomer, double waitingProbability,
                           double waitingQuantityProm, double waitingLengthTimeProm, double waitingTimeProm,
                           double extraWorkTimeProm, double useServerPercentage, double sistemCost, 
                           double severCost, double waitCost
                          ){ 
        
        this.unWaitCustomer = unWaitCustomer ;            
        this.unAttendanceCustomer = unAttendanceCustomer;      
        this.waitingProbability = waitingProbability;      
        this.waitingQuantityProm = waitingQuantityProm ;     
        this.waitingLengthTimeProm = waitingLengthTimeProm;   
        this.waitingTimeProm = waitingTimeProm;         
        this.extraWorkTimeProm = extraWorkTimeProm;       
        this.useServerPercentage = useServerPercentage;     
        this.sistemCost = sistemCost;
        this.severCost = severCost;
        this.waitCost = waitCost;    
    }
    
    //Setters

    public void setUnWaitCustomer(int unWaitCustomer) {
        this.unWaitCustomer = unWaitCustomer ;
    }
    
    public void setUnAttendanceCustomer(int unAttendanceCustomer) {
        this.unAttendanceCustomer = unAttendanceCustomer; 
    }
    
    public void setWaitingProbability(double waitingProbability) {
        this.waitingProbability = waitingProbability;  
    }
    
    public void setWaitingQuantityProm (double waitingQuantityProm) {
        this.waitingQuantityProm = waitingQuantityProm; 
    }
    
    public void setWaitingLengthTimeProm  (double waitingLengthTimeProm) {
        this.waitingLengthTimeProm = waitingLengthTimeProm;  
    }
    
    public void setWaitingTimeProm (double waitingTimeProm) {
        this.waitingTimeProm = waitingTimeProm;  
    }
    
    public void setExtraWorkTimeProm(double extraWorkTimeProm) {
        this.extraWorkTimeProm = extraWorkTimeProm;
    }
    
    public void setUseServerPercentage(double useServerPercentage) {
        this.useServerPercentage = useServerPercentage;     

    }
    
    public void setSistemCost(double sistemCost) {
        this.sistemCost = sistemCost;
    }
    
    public void setSeverCost(double severCost) {
        this.severCost = severCost;
    }
    
    public void setWaitCost(double waitCost) {
        this.waitCost = waitCost;
    }
        

           
    //Getters
    
    public int getUnWaitCustomer() {
        return unWaitCustomer ;
    }
    
    public int getUnAttendanceCustomer() {
        return unAttendanceCustomer; 
    }
    
    public double getWaitingProbability() {
        return waitingProbability;  
    }
    
    public double getWaitingQuantityProm () {
        return waitingQuantityProm; 
    }
    
    public double getWaitingLengthTimeProm  () {
        return waitingLengthTimeProm;  
    }
    
    public double getWaitingTimeProm () {
       return waitingTimeProm;  
    }
    
    public double getExtraWorkTimeProm() {
        return extraWorkTimeProm;
    }
    
    public double getUseServerPercentage() {
        return useServerPercentage;     
    }
    
    public double getSistemCost() {
        return sistemCost;
    }
    
    public double getSeverCost() {
        return severCost;
    }
    
    public double getWaitCost() {
        return waitCost;
    }
        
 
    
}
     
     



