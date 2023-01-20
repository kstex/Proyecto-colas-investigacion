package FileClass;

import DataClass.DataEntry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FileManager {

    private final String fileSimulation;

    public FileManager() {
        this.fileSimulation = "simulation.csv";
    }
    

    public void save_txt(DataEntry data, JFileChooser jf) {
        FileWriter fw;
        PrintWriter pw;
        try {
            String[] dataTitles = {"Unidad de tiempo", "Tabla de eventos", "tiempo de simulacion", "cantidad de clientes",
                "Cantidad de servidores", "costo del tiempo en servicio del cliente", "Costo de tiempo de espera del cliente",
                "Costo del servidor ocupado", "costo del servidor desocupado", "costo del tiempo extra del servidor",
                "costo normal de la operacion", "costo de operacion extra"};
            String[] titlesArrivedTable = {"Tiempo de llegada", "Probabilidad"};
            String[] titlesServiceTable = {"Tiempo de servicio", "Probabilidad"};
            fw = new FileWriter(jf.getCurrentDirectory() + "/" + fileSimulation);
            pw = new PrintWriter(fw);

            for (int i = 0; i < dataTitles.length; i++) {
                pw.print(dataTitles[i] + ";");
            }
            pw.println();
            pw.println(data.getTimeUnit() + " ;" + data.getEventTable() + " ;" + data.getTimeSimulation() + " ;" + data.getQuantityCustomers() + " ;"
                    + data.getQuantityServers() + " ;" + data.getCostTimeCustomer() + " ;" + data.getCostTimeWaitCustomer() + " ;" + data.getBusyServercost() + " ;");

            //Here you can write the customers arrived table
            for (int i = 0; i < titlesArrivedTable.length; i++) {
                pw.print(titlesArrivedTable[i] + ";"); //Titles
            }
            pw.println();
            for (int i = 0; i < data.getArrivedCustomers().getRowCount(); i++) {
                //Values
                pw.println(data.getArrivedCustomers().getValueAt(i, 0) + ";" + data.getArrivedCustomers().getValueAt(i, 1) + ";");
            }

            //Here you can read the service time table
            for (int i = 0; i < titlesServiceTable.length; i++) {
                pw.print(titlesServiceTable[i] + ";");//Titles
            }
            pw.println();
            for (int i = 0; i < data.getServiceTime().getRowCount(); i++) {
                //Values
                pw.println(data.getServiceTime().getValueAt(i, 0) + ";" + data.getServiceTime().getValueAt(i, 1) + ";");
            }

            pw.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al grabar archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex.getMessage());
        }
    }

    public DataEntry readText(String file) {
        DataEntry data = new DataEntry();
        String[] titlesArrivedTable = {"Tiempo de llegada", "Probabilidad"};
        String[] titlesServiceTable = {"Tiempo de servicio", "Probabilidad"};
        DefaultTableModel modelArrived = new DefaultTableModel(null, titlesArrivedTable);
        DefaultTableModel modelService = new DefaultTableModel(null, titlesServiceTable);
        File ruta = new File(file);
        try {

            FileReader fi = new FileReader(ruta);
            BufferedReader bu = new BufferedReader(fi);

            String linea = bu.readLine();
            StringTokenizer st;
            while (!(linea = bu.readLine()).contains("Tiempo de llegada")) {
                
                st = new StringTokenizer(linea, ";");
                data.setTimeUnit(st.nextToken().trim());
                data.setEventTable(st.nextToken().trim());
                data.setTimeSimulation(Double.parseDouble(st.nextToken().trim()));
                data.setQuantityCustomers(Integer.parseInt(st.nextToken().trim()));
                data.setQuantityServers(Integer.parseInt(st.nextToken().trim()));
                data.setCostTimeCustomer(Double.parseDouble(st.nextToken().trim()));
                data.setCostTimeWaitCustomer(Double.parseDouble(st.nextToken().trim()));
                data.setBusyServercost(Double.parseDouble(st.nextToken().trim()));

            }
            while (!(linea = bu.readLine()).contains("Tiempo de servicio")) {
                st = new StringTokenizer(linea, ";");
                modelArrived.addRow(new Object[]{
                    st.nextToken().trim(),
                    st.nextToken().trim()
                });
            }
            data.setArrivedCustomers(modelArrived);

            while ((linea = bu.readLine()) != null) {
                st = new StringTokenizer(linea, ";");
                modelService.addRow(new Object[]{
                    st.nextToken().trim(),
                    st.nextToken().trim()
                });

            }
            data.setServiceTime(modelService);

            bu.close();
            JOptionPane.showMessageDialog(null, "Archivo cargado exitosamente","Operacion exitosa",JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex.getMessage());
        }
        return data;
    }

}
