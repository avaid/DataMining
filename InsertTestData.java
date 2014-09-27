import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InsertTestData {
	
	private static BufferedReader openFileForReading(String path) throws FileNotFoundException {
		FileInputStream fstream = new FileInputStream(path);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		return br;
	}

    public static void main(String[] args) throws IOException {
        try {
        	
        	BufferedReader br = openFileForReading("testing.txt");
        	
        	Connection con = null;
			Statement st = null;
			ResultSet rs = null;

			String url = "jdbc:postgresql://localhost/postgres";
			String user = "postgres";
			String password = "computer";

			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			
   
            st = con.createStatement();


            
            
            String strLine = null;
            String query = null;
            while((strLine = br.readLine()) != null) {
            	String tokens[] = strLine.split(" ");
            	 query = "INSERT INTO \"TestData\" (record_id, attr_id, attr_value) VALUES (" + tokens[0] + "," +  tokens[1] + "," + tokens[2] + ");";
                int res = st.executeUpdate(query);
                if(res == 1) {
                	System.out.println(strLine + " inserted");
                }
            }
            
            
            

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(InsertTestData.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } 

    }
}