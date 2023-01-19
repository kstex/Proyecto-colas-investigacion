/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataClass;

/**
 *
 * @author Gustavo
 */
public class Client {
    protected long clientNumber;
    protected long arriveTime;

    public Client(long clientNumber, long arriveTime) {
        this.clientNumber = clientNumber;
        this.arriveTime = arriveTime;
    }

    public long getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(long clientNumber) {
        this.clientNumber = clientNumber;
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }
    
    
}
