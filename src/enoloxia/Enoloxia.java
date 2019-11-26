/*
 * 3. ejercicio de repaso para el examen buscar en un texto delimitado analisis.txt
 datos y luego buscar y modificar las tablas uvas, clientes y xerado
 */
/* COMANDOS
 lanzar servidor oracle para trabajar desde java con el listener

 . oraenv
 orcl
 rlwrap sqlplus sys/oracle as sysdba 
 startup
 conn hr/hr
 exit
 lsnrctl start
 lsnrctl status

 */
/*
 1º crear tablas lanzando el fichero sql
 2º listener
 3º leer serializado ','
 4º buscar datos en las tablas
 5º actualizar las tablas 
 */
package enoloxia;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author oracle
 */
public class Enoloxia {

    public static Connection conexion = null;

    public static Connection getConexion() throws SQLException {
        String usuario = "hr";
        String password = "hr";
        String host = "localhost";
        String puerto = "1521";
        String sid = "orcl";
        String ulrjdbc = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;

        conexion = DriverManager.getConnection(ulrjdbc);
        return conexion;
    }

    public static void closeConexion() throws SQLException {
        conexion.close();
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {
        //abrir conexion
        getConexion();

        // txt delimitado
        String RutaS = "/home/oracle/Desktop/compartido/enol/analisis.txt";
        BufferedReader leerB = new BufferedReader(new FileReader(RutaS));

        //lee el texto  y lo guarda en un array linea a linea
        String linea;
        
        //todo el codigo de modificar tienen que ir dentro del while que lee el texto para que vaya cambiando el objeto
        while ((linea = leerB.readLine()) != null) {
            String[] leer = linea.split(",");
            
            //guardar en un objeto los datos obtenidos en el txto delimitado
            Analisis a = new Analisis(leer[0], Integer.parseInt(leer[1]), Integer.parseInt(leer[2]), Integer.parseInt(leer[3]), leer[4], Integer.parseInt(leer[5]), leer[6]);
            System.out.println("ANALISIS:");
            System.out.println(a.toString());
            
            //guardar los datos obtenidos en parametros para utilizar para insertar en las tablas de la db
            String nomeUva = nomeUva(a.getTipo());
            System.out.println("nome uva: " + nomeUva);
            String cambiarAcidez = trataracidez(a.getTipo(), a.getAcidez());
            System.out.println("acidez: " + cambiarAcidez);
            int totalPago = totalPagar(a.getCantidade());
            System.out.println("Total a pagar: " + totalPago);

            
            //inxerrir en taboa xerado
            inxerirXerado(a.getCodigoa(), nomeUva, cambiarAcidez, totalPago);
            
            
            //leer  la tabla clientes:
            
            int numeroAnalis= recogerNumAnalisis(a.getDni());
            ////incrementear por uno el numero de analisis
       //numeroAnalis++;
            
            //modificar tabla clientes
        cambiarClientes(a.getDni(),numeroAnalis);

        }//final del while

        //cerrar buffer del texto delimintado
        leerB.close();
        
        //cerrar conexion
        closeConexion();
    }//cierra elmain

    //metodos de tratamiento de datos
    
    //metodo que di como tratar a acidez pasando o tipo de uva e a acidez que teñe 
    public static String trataracidez(String tipo, int acidez) throws SQLException {
        String ta = null;
        int min = 0;
        int max = 0;
        PreparedStatement pst2 = conexion.prepareStatement("select acidezmin,acidezmax from uvas where tipo = ?");
        pst2.setString(1, tipo);

        ResultSet rs2 = pst2.executeQuery();

        while (rs2.next()) {

            min = rs2.getInt("acidezmin");
            max = rs2.getInt("acidezmax");
        }
        if (acidez < min) {
            ta = "subir acidez";
        } else if (acidez > max) {
            ta = "bajar acidez";
        } else {
            ta = "acidez correcta";
        }
        return ta;
    }

    
    //metodo que busca na db o nome da uva pasando o tipo
    public static String nomeUva(String tipo) throws SQLException {
        PreparedStatement pst1 = conexion.prepareStatement("select nomeu from uvas where tipo = ?");
        pst1.setString(1, tipo);

        ResultSet rs1 = pst1.executeQuery();
        rs1.next();

        return rs1.getString("nomeu");
       
    }

    
    //metodo que calcula o total que ten que pagar por o analisis
    public static int totalPagar(int cant) {

        return cant * 15;
    }
    
    //metodo que inxire na taboa xerado pasando os parametros a inxerir

    public static void inxerirXerado(String coda, String nomeuva, String acidez, int total) throws SQLException {
        //INSERTAR FILA EN XERADO:
        PreparedStatement pst3 = conexion.prepareStatement("insert into xerado values(?,?,?,?)");
        pst3.setString(1, coda);
        pst3.setString(2, nomeuva);
        pst3.setString(3, acidez);
        pst3.setInt(4, total);

        pst3.executeQuery();
    }

    //metodo que recoge el numero de analisis anterior de la tabla cliente
    public static int recogerNumAnalisis (String dni) throws SQLException{
    //1º recoger el numero de analisis que tiene el cliente
        PreparedStatement pst4 = conexion.prepareStatement("select numerodeanalisis from clientes where dni = ?");

            pst4.setString(1,dni);

            ResultSet rs4 = pst4.executeQuery();
            rs4.next();

            //recogerlo en una variable y sumarle mas 1
            int numeroa = rs4.getInt("numerodeanalisis");
        System.out.println("CLIENTE : " +dni + " numero de analisis: "+numeroa);
            return numeroa;
    
    }
       //actualizar la tabla con el nuevo numero de analisis
    public static void cambiarClientes(String dni, int numa) throws SQLException{

         
            PreparedStatement pst5 = conexion.prepareStatement("update clientes set numerodeanalisis = ? where dni = ?");

            pst5.setInt(1, numa);
            pst5.setString(2, dni);

            pst5.executeUpdate();

    }
    
    
}//cierra la clase principal
