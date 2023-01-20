
package DataClass;

import javax.swing.table.DefaultTableModel;

public class DataEntry  {
    
    private String timeUnit;
    
    private boolean eventTable;
    
    private double timeSimulation;
    
    private int quantityCustomers ;
    
    private int quantityServers;
    
    private DefaultTableModel ArrivedCustomers;
    
    private DefaultTableModel ServiceTime;
    
    private double costTimeCustomer ; //cost time in customer service
    
    private double costTimeWaitCustomer ; // cost of customer waiting
    
    private double busyServercost; // cost server busy  

    public DataEntry() {
        this.timeUnit = "Horas";
        this.eventTable = false;
        this.timeSimulation = 0;
        this.quantityCustomers = 0;
        this.quantityServers = 0;
        this.ArrivedCustomers = new DefaultTableModel();
        this.ServiceTime = new DefaultTableModel();
        this.costTimeCustomer = 0;
        this.costTimeWaitCustomer = 0;
        this.busyServercost = 0;
    }

    public DataEntry(String timeUnit, boolean eventTable, double timeSimulation, int quantityCustomers, 
            int quantityServers, DefaultTableModel ArrivedCustomers, DefaultTableModel ServiceTime, 
            double costTimeCustomer, double costTimeWaitCustomer, double busyServercost) {
        this.timeUnit = timeUnit;
        this.eventTable = eventTable;
        this.timeSimulation = timeSimulation;
        this.quantityCustomers = quantityCustomers;
        this.quantityServers = quantityServers;
        this.ArrivedCustomers = ArrivedCustomers;
        this.ServiceTime = ServiceTime;
        this.costTimeCustomer = costTimeCustomer;
        this.costTimeWaitCustomer = costTimeWaitCustomer;
        this.busyServercost = busyServercost;
    }

    public String getTimeUnit() {
        return timeUnit;
    }
    
    public String getEventTable(){
        return this.eventTable ? "si":"no";
    }

    public boolean isEventTable() {
        return eventTable;
    }

    public double getTimeSimulation() {
        return timeSimulation;
    }

    public int getQuantityCustomers() {
        return quantityCustomers;
    }

    public int getQuantityServers() {
        return quantityServers;
    }

    public DefaultTableModel getArrivedCustomers() {
        return ArrivedCustomers;
    }

    public DefaultTableModel getServiceTime() {
        return ServiceTime;
    }

    public double getCostTimeCustomer() {
        return costTimeCustomer;
    }

    public double getCostTimeWaitCustomer() {
        return costTimeWaitCustomer;
    }

    public double getBusyServercost() {
        return busyServercost;
    }

    //Seters

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void setEventTable(boolean eventTable) {
        this.eventTable = eventTable;
    }
    
    public void setEventTable(String eventTable){
        this.eventTable = eventTable.equals("si");
    }

    public void setTimeSimulation(double timeSimulation) {
        this.timeSimulation = timeSimulation;
    }

    public void setQuantityCustomers(int quantityCustomers) {
        this.quantityCustomers = quantityCustomers;
    }

    public void setQuantityServers(int quantityServers) {
        this.quantityServers = quantityServers;
    }

    public void setArrivedCustomers(DefaultTableModel ArrivedCustomers) {
        this.ArrivedCustomers = ArrivedCustomers;
    }

    public void setServiceTime(DefaultTableModel ServiceTime) {
        this.ServiceTime = ServiceTime;
    }

    public void setCostTimeCustomer(double costTimeCustomer) {
        this.costTimeCustomer = costTimeCustomer;
    }

    public void setCostTimeWaitCustomer(double costTimeWaitCustomer) {
        this.costTimeWaitCustomer = costTimeWaitCustomer;
    }

    public void setBusyServercost(double busyServercost) {
        this.busyServercost = busyServercost;
    }

}
