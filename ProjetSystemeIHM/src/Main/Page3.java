package Main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


public class Page3 extends javax.swing.JFrame {

    /**
     * Creates new form Page3
     */
    Processus[] tableProcess;
    Processus[] tableau;
    int temp = 0 ;
    int sommeRotation = 0 ;
    int sommeAttente = 0 ;
        
    
    String[] colorTable= new String[]{"#FF7F11","#60D394","#AAF683","#FF9B85",
        "#EE6055","#EAD2AC","#D1DEDE","#7EB2DD","#A0B9C6","#A5F8D3","#9A98B5",
        "#E2C044","#CBB3BF"};
    public Page3(Processus[] tab, String type) {
       
        
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        
        
        //Lecture du fichier
        Vector<Processus> VFichier = new Vector<Processus>();
       
            
        try{
            
        JSONObject obj = new JSONObject(new JSONTokener(new FileReader("C:\\"
+ "Users\\Thamer\\Desktop\\1_ISEOC\\projets 1ING\\SE\\ProjetSystemeIHM\\src"
                + "\\Processus.json")));
        JSONArray arr = obj.getJSONArray("Processus");
        for (int i = 0; i < arr.length(); i++)
        {
            String Pnom = arr.getJSONObject(i).getString("nom");
            int PdureeEx = arr.getJSONObject(i).getInt("dureeEx");
            int Ppriorite = arr.getJSONObject(i).getInt("priorite");
            int PtempArr = arr.getJSONObject(i).getInt("tempArr");
            
            VFichier.add(new Processus(Pnom,PdureeEx,Ppriorite,PtempArr));
        }
        
        }catch(Exception e){
            System.out.println(e.toString());
        }
               
        int n = VFichier.size();
        tab = new Processus[n];
        for(int i=0; i<n;i++) //Remplissage du tableau
        {
            tab[i]=VFichier.elementAt(i);
            
        }
        titre.setText(type);
        Ordenancement o = new Ordenancement();
        if(type.equals("FIFO")){
           
           o.FCFS(tab);
           
        }
        
        
        //SJF ou FIFO
        if (type.equals("FIFO") || type.equals("SJF")){
        
        DefaultTableModel dtm = new DefaultTableModel(0, 0);

        // add header of the table
        String header[] = new String[] { "Num Client", "Check IN", 
            "Durée de sejour","Etat"};

        // add header in table model     
         dtm.setColumnIdentifiers(header);
            //set model into the table object
               table.setModel(dtm);
        for (int i = 0 ; i<tab.length ; i++){
            
            int datedebut = tab[0].tempsArr ;
            for (int l = i-1;l>=0;l--){
                datedebut += tab[l].dureeEx;
            }
            
            
            int datefin = datedebut + tab[i].dureeEx; 
            int tempAttente = datedebut - tab[i].tempsArr;
            int tempRotation = (datefin - tab[i].tempsArr)+tab[i].dureeEx;

            
            dtm.addRow(new Object[] { tab[i].nom,tab[i].tempsArr,tab[i].dureeEx,
                tab[i].priorite,Math.abs(tempAttente),Math.abs(tempRotation)});
            //datedebut = 0;
        }
        /*float moyRotation = (float)sommeRotation/(float)tab.length;
        float moyAttente = (float) sommeAttente/(float)tab.length;
        */
        this.tableProcess = tab;
        
        String simulation="<html><table border=1 style=\"border-left: 1px solid "
                + "#cdd0d4;border-bottom: 1px solid #cdd0d4;\"><tr>";
        
        Random rand = new Random();
        for (int i = 0; i<tab.length;i++){
            int color = rand.nextInt(9);
            simulation = simulation+"<td bgcolor="+colorTable[color]+" height=20"
                    + " width=60 >"+tab[i].nom+"</td>";
        }
        simulation = simulation+"</tr></table>";
        this.simuleTxt.setText(simulation);
        
        
        
      }
        
        
        //IF RR ou SRT
        if (type.equals("SRT")){
            
        Vector<Processus> v = o.SJF_P(tab);
            
        DefaultTableModel dtm = new DefaultTableModel(0, 0);

        // add header of the table
        String header[] = new String[] { "Nom des processus", "Temp d'arrivé",
            "Durée d'execution","Priorité","Chambre n°"};

        // add header in table model     
         dtm.setColumnIdentifiers(header);
            //set model into the table object
               table.setModel(dtm);
               
        String chambre;
        for (int i = 0 ; i<v.size() ; i++){
            
            //houni l5edma
            int duree = countElement(v,v.elementAt(i));
            int x = v.lastIndexOf(v.elementAt(i));
            int datefin = x+1+v.elementAt(0).tempsArr;            
            
            dtm.addRow(new Object[] { v.elementAt(i).nom,v.elementAt(i).
          tempsArr, duree , v.elementAt(i).priorite ,v.elementAt(i).chambre});
            
        }
        
        String simulation="<html><table border=1 style=\"border-left: 1px solid"
                + " #cdd0d4;border-bottom: 1px solid #cdd0d4;\"><tr>";
        
        Random rand = new Random();
        for (int i = 0; i<v.size();i++){
            int color = rand.nextInt(9);
            simulation = simulation+"<td bgcolor="+colorTable[color]+" "
                    + "height=20 >"+v.elementAt(i).nom+"</td>";
        }
        simulation = simulation+"</tr></table>";
        this.simuleTxt.setText(simulation);
       
        
      }//fin SRT
        if (type.equals("RR")){
        String ch= JOptionPane.showInputDialog("Donner la valeur de quantum");
             int quantum = Integer.parseInt(ch);
             
             
          Vector<Processus> v = o.RR(tab,quantum);
            
        DefaultTableModel dtm = new DefaultTableModel(0, 0);

        // add header of the table
        String header[] = new String[] { "Nom des processus", "Temp d'arrivé", 
            "Durée d'execution","Priorité","Temp d'attente","Temp rotation"};

        // add header in table model     
         dtm.setColumnIdentifiers(header);
            //set model into the table object
               table.setModel(dtm);
        int sommeRotation = 0;
        int sommeAttente = 0;
        for (int i = 0 ; i<v.size() ; i++){
            
            int duree = countElement(v,v.elementAt(i));
            int x = v.lastIndexOf(v.elementAt(i));
            int datefin = x+1+v.elementAt(0).tempsArr;
            int tempRotation = datefin - v.elementAt(i).tempsArr;
            int tempAttente = tempRotation - duree;
            
            sommeRotation += tempRotation;
            sommeAttente+= tempAttente;
            dtm.addRow(new Object[] { v.elementAt(i).nom,v.elementAt(i).
            tempsArr,getDuree(tab,v.elementAt(i)), v.elementAt(i).
            priorite,Math.abs(tempAttente),Math.abs(tempRotation) });
            
        }
        
        String simulation="<html><table border=1 style=\"border-left: 1px solid"
                + " #cdd0d4;border-bottom: 1px solid #cdd0d4;\"><tr>";
        
        Random rand = new Random();
        for (int i = 0; i<v.size();i++){
            int color = rand.nextInt(9);
            simulation = simulation+"<td bgcolor="+colorTable[color]+" "
                    + "height=20 >"+v.elementAt(i).nom+"</td>";
        }
        simulation = simulation+"</tr></table>";
        this.simuleTxt.setText(simulation);
        float moyRotation = (float)sommeRotation/(float)tab.length;
        float moyAttente = (float) sommeAttente/(float)tab.length;
       
             
        }
        
        
        

        
        
    }

    public int getDuree(Processus[] tab, Processus p){
        for (int i = 0; i<tab.length;i++){
            if(tab[i].nom.equals(p.nom)){
                return tab[i].dureeEx;
            }
        }
        return 0;
    }
    
    public int countElement(Vector<Processus> v, Processus p){
        int nb = 0;
        for (int i = 0 ; i<v.size();i++){
            if(v.elementAt(i).nom.equals(p.nom)){
                nb++;
            }
        }
        return nb;
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        chrono = new javax.swing.JLabel();
        ProcessTxt = new javax.swing.JLabel();
        titre = new javax.swing.JLabel();
        simuleTxt = new javax.swing.JLabel();

        jLabel5.setText("jLabel5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(Color.decode("#e4afcb"));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Num Client", "Check IN", "Duree de sejour", "Etat"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(1).setResizable(false);
            table.getColumnModel().getColumn(2).setResizable(false);
            table.getColumnModel().getColumn(3).setResizable(false);
        }

        ProcessTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        titre.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        titre.setText("FIFO");

        simuleTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        simuleTxt.setText("jLabel2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 843, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(chrono)
                                .addGap(392, 392, 392))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(simuleTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(83, 83, 83))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(ProcessTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 768, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(titre)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titre)
                .addGap(18, 18, 18)
                .addComponent(ProcessTxt)
                .addGap(43, 43, 43)
                .addComponent(simuleTxt)
                .addGap(26, 26, 26)
                .addComponent(chrono)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(130, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 855, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 539, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
int i = 0 ;
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Page3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Page3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Page3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Page3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Page3(null,null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ProcessTxt;
    private javax.swing.JLabel chrono;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel simuleTxt;
    private javax.swing.JTable table;
    private javax.swing.JLabel titre;
    // End of variables declaration//GEN-END:variables
}
