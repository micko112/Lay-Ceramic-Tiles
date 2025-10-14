// prvo transfer receiver-sender-request-response za Request Operation, Object arg Response Object result, Exception,
// CController - Konekcija - DBBroker - Server - ClientHandler - TableModel

//LOGIN -------------------------------------------------------------------------------------------------------------------------------------
    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String email = jTextField1.getText();
        String lozinka = String.valueOf(jPasswordField1.getPassword());
        Nastavnik n = new Nastavnik(-1, "", "", email, lozinka);
        Nastavnik nas = CController.getInstance().login(n);
        System.out.println(nas);
        if(n.getEmail().equals(nas.getEmail()) && n.getLozinka().equals(nas.getLozinka())){
            new ClientForm(nas).setVisible(true);
            this.dispose();
        }
    } 
//Client Controller -------------------------------------------------------------------------------------------------------------------------------------
public class CController {

    private static CController instance;
    public static CController getInstance(){
        if(instance == null){
            instance = new CController();
        }
        return instance;
    }
    Socket socket;
    Sender sender;
    Receiver receiver;

    public CController() {
        try {
            this.socket = new Socket("localhost", 9000);
            this.sender = new Sender(socket);
            this.receiver = new Receiver(socket);
        } catch (IOException ex) {
            Logger.getLogger(CController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Nastavnik login(Nastavnik n) {
        Request req = new Request(Operation.login, n);
        sender.send(req);
        System.out.println("CController salje login");
        Response res = (Response) receiver.receive();
        Nastavnik nastavnik = (Nastavnik) res.getResult();
        System.out.println("CController vration nastavnika" + nastavnik);
        return nastavnik;
    }

    public List<Angazovanje> vratiAngazovanja(Nastavnik nas) {
        Request req = new Request(Operation.vratiAngazovanja, nas);
        sender.send(req);
        System.out.println("CController salje vratiAngazovanja");
        Response res = (Response) receiver.receive();
        List<Angazovanje> angazovanja =  (List<Angazovanje>) res.getResult();
        System.out.println("CController vratio listu ang");
        return angazovanja;
    }

    public List<Angazovanje> vratiAngazovanjaPoPredmetu(Predmet p) {
        Request req = new Request(Operation.vratiAngazovanjaPoPredmetu, p);
        System.out.println(" CC PREDMET JE -----------------------------------------------------"+req.getArgument());
        sender.send(req);
        System.out.println("CController salje vratiAngazovanjaPoPredmetu");
        Response res = (Response) receiver.receive();
        List<Angazovanje> ang =  (List<Angazovanje>) res.getResult();
        System.out.println("CController vratio listu angazovanja po predmetu");
        return ang;
    }

    public List<Predmet> vratiPredmete(Nastavnik nas) {
        Request req = new Request(Operation.vratiPredmete, nas);
        sender.send(req);
        System.out.println("CController salje vratiPredmete");
        Response res = (Response) receiver.receive();
        List<Predmet> predmeti =  (List<Predmet>) res.getResult();
        System.out.println("CController vratio listu predmeta");
        return predmeti;
    }

}
//Client Handler -------------------------------------------------------------------------------------------------------------------------------------

public class ClientHandler extends Thread{
    Server server;
    Socket socket;
    Sender sender;
    Receiver receiver;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.sender = new Sender(socket);
        this.receiver = new Receiver(socket);
    }

    @Override
    public void run() {
        System.out.println("CH je poceo da radi");
        while(true){
            Request req = (Request) receiver.receive();
            Response res = new Response();
            switch (req.getOperation()) {
                case login -> {
                    Nastavnik n = (Nastavnik) req.getArgument();
                    Nastavnik nastavnik = DBBroker.getInstance().login(n);
                    res.setResult(nastavnik);
                }
                case vratiAngazovanja -> {
                    Nastavnik n = (Nastavnik) req.getArgument();
                    List<Angazovanje> ang = DBBroker.getInstance().vratiAngazovanja(n);
                    res.setResult(ang);
                }
                case vratiPredmete -> {
                    Nastavnik n = (Nastavnik) req.getArgument();
                    List<Predmet> predmeti = DBBroker.getInstance().vratiPredmete(n);
                    res.setResult(predmeti);
                }
                case vratiAngazovanjaPoPredmetu -> {
                    Predmet p = (Predmet) req.getArgument();
                    System.out.println("CH PREDMET JE ------------------------------------------------------"+p);
                    List<Angazovanje> ang = DBBroker.getInstance().vratiAngazovanjaPoPredmetu(p);
                    res.setResult(ang);
                }
                default -> throw new AssertionError();
            }
            sender.send(res);
        }
    }
}
// SERVER -------------------------------------------------------------------------------------------------------------------------------------
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Server {
    ServerSocket serverSocket;
    
    public void start(){
        try {
            serverSocket = new ServerSocket(9000);
            System.out.println("Server je povezan i ceka klijente");
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Klijent se povezao");
                
                ClientHandler ch = new ClientHandler(this, socket);
                ch.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void main(String[] args) {
        new Server().start();
    }
}
// DBBroker -------------------------------------------------------------------------------------------------------------------------------------
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DBBroker {
    private static DBBroker instance;
    public static DBBroker getInstance(){
        if(instance == null){
            instance = new DBBroker();
        }
        return instance;
    }
    Konekcija conn = Konekcija.getInstance();

    public Nastavnik login(Nastavnik n) {
        try {
            String upit = "select * from nastavnik where email = ? and lozinka = ?";
            PreparedStatement ps = conn.getConnection().prepareStatement(upit);
            ps.setString(1, n.getEmail());
            ps.setString(2, n.getLozinka());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Nastavnik nastavnik = new Nastavnik(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                return nastavnik;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Angazovanje> vratiAngazovanja(Nastavnik n) {
        try {
            String upit = "select * FROM angazovanje AS a JOIN nastavnik AS n ON a.nastavnikId = n.nastavnikId JOIN predmet AS p ON a.predmetId = p.predmetId WHERE a.nastavnikId = ?";
            PreparedStatement ps = conn.getConnection().prepareStatement(upit);
            ps.setInt(1, n.getNastavnikId());
            ResultSet rs = ps.executeQuery();
            List<Angazovanje> angazovanja = new LinkedList<>();
            while (rs.next()) {                
                //Nastavnik n = new Nastavnik(rs.getInt("n.nastavnikId"), rs.getString("n.ime"), rs.getString("n.prezime"), rs.getString("n.email"), rs.getString("n.lozinka"));
                  Predmet predmet = new Predmet(rs.getInt("p.predmetId"), rs.getString("p.naziv"));
                  OblikNastave oblik = OblikNastave.valueOf(rs.getString("a.oblikNastave"));
                  Angazovanje a = new Angazovanje(n, predmet, oblik);
                  angazovanja.add(a);
               // na kraju ne zaboravi da stavis natavnik.setAng
            }
            n.setAngazovanja(angazovanja);
            return angazovanja;
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }

    public List<Predmet> vratiPredmete(Nastavnik n) {
        try {
            String upit= "Select * from angazovanje a JOIN predmet p on a.predmetId = p.predmetId WHERE a.nastavnikId = ? ";
            PreparedStatement ps = conn.getConnection().prepareStatement(upit);
            ps.setInt(1, n.getNastavnikId());
            ResultSet rs = ps.executeQuery();
            List<Predmet> predmeti = new LinkedList<>();
            while(rs.next()){
                Predmet p = new Predmet(rs.getInt("p.predmetId"), rs.getString("p.naziv"));
                System.out.println(p);
                if(predmeti.contains(p)){
                    
                }else{
                predmeti.add(p);
                }
            }
            return predmeti;
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Angazovanje> vratiAngazovanjaPoPredmetu(Predmet p) {
        try {
            String upit = "select * FROM angazovanje AS a JOIN nastavnik AS n ON a.nastavnikId = n.nastavnikId JOIN predmet AS p ON a.predmetId = p.predmetId WHERE p.predmetId = ?";
            PreparedStatement ps = conn.getConnection().prepareStatement(upit);
            ps.setInt(1, p.getPredmetId());
            ResultSet rs = ps.executeQuery();
            List<Angazovanje> angazovanja = new LinkedList<>();
            while (rs.next()) {                
                Nastavnik n = new Nastavnik(rs.getInt("n.nastavnikId"), rs.getString("n.ime"), rs.getString("n.prezime"), rs.getString("n.email"), rs.getString("n.lozinka"));
                Predmet predmet = new Predmet(rs.getInt("p.predmetId"), rs.getString("p.naziv"));
                OblikNastave oblik = OblikNastave.valueOf(rs.getString("a.oblikNastave"));
                Angazovanje a = new Angazovanje(n, predmet, oblik);
                angazovanja.add(a);
                // na kraju ne zaboravi da stavis natavnik.setAng
                n.setAngazovanja(angazovanja);
            }
            
            return angazovanja;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DBBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
}
// CLIENT FORM  -------------------------------------------------------------------------------------------------------------------------------------
public class ClientForm extends javax.swing.JFrame {

   // List<Angazovanje> angazovanja;
    Nastavnik nas;
    TableModel tm;
PredmetTableModel ptm;
Predmet p;
    /**
     * Creates new form ClientForm
     */
    public ClientForm(Nastavnik nas) {
        initComponents();
        this.nas = nas;
        popuniTabelu();
        popuniCMB();
        
        JComboBox<OblikNastave> comboBoxEdit = new JComboBox<>(OblikNastave.values()); // COMBO BOX ZA IZMENU JEDNE STVARI
        DefaultCellEditor cellEditor = new DefaultCellEditor(comboBoxEdit);
        jTable1.getColumnModel().getColumn(1).setCellEditor(cellEditor);
        
        
        jLabel1.setText(nas.getIme());
        jLabel1.setText(nas.getPrezime());
        jLabel1.setText(nas.getEmail());
        jLabel4.setText(String.valueOf(jComboBox1.getItemCount()));
    }
// ovo je za ako treba da se ubaci detalji po predmetu
	private void jComboBoxPredmetiActionPerformed(java.awt.event.ActionEvent evt) {                                           
            p = (Predmet) jComboBox1.getSelectedItem();
            if(p ==null){
                return;
            }
            
           List<Angazovanje> ang = CController.getInstance().vratiAngazovanjaPoPredmetu(p);
            ptm = new PredmetTableModel();
            ptm.setAngazovanja(ang);
            jTable2.setModel(ptm);
    } 
    private void popuniTabelu() {
        List<Angazovanje> ang = CController.getInstance().vratiAngazovanja(nas);
        System.out.println(ang);
        tm = new TableModel();
        tm.setAngazovanja(ang);
        jTable1.setModel(tm); 
    }

    private void popuniCMB() {
        List<Predmet> predmeti = CController.getInstance().vratiPredmete(nas);
        System.out.println(predmeti);
        jComboBox1.removeAllItems();
        for (Predmet p : predmeti) {
            jComboBox1.addItem(p);
        }
    }
}
// TABLE MODEL  -------------------------------------------------------------------------------------------------------------------------------------
public class TableModel extends AbstractTableModel{
List<Angazovanje> angazovanja;
String[] colNames = {"nastavnik", "oblik Nastave", "predmetId", "naziv predmeta"};
TableModel(){
    this.angazovanja = new LinkedList<>();
}

    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }

    public List<Angazovanje> getAngazovanja() {
        return angazovanja;
    }
    
    public void setAngazovanja(List<Angazovanje> angazovanja) {
        this.angazovanja = angazovanja;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return angazovanja.size();
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Angazovanje a = angazovanja.get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return a.getNastavnik().getIme();
        }
            case 1 -> {
                return a.getOblikNastave();
            }
            case 2 -> {
                return a.getPredmet().getPredmetId();
            }
            case 3 -> {
                return a.getPredmet().getNaziv();
            }
            default -> throw new AssertionError();
        }
    }
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Angazovanje a = angazovanja.get(rowIndex);
        switch (columnIndex) {

            case 1-> {
                a.setOblikNastave((OblikNastave) aValue);
                fireTableDataChanged();
            }
            default -> throw new AssertionError();
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex==1){
            return true;
        }else{
            return false;
    }
}
}



