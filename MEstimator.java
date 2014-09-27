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
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MEstimator {

	public static void main(String[] args) throws IOException {
		try {
			Connection con = null;
			Statement st = null;
			ResultSet rs = null, rs3 = null;

			String url = "jdbc:postgresql://localhost/postgres";
			String user = "postgres";
			String password = "computer";

			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			Statement st3 = con.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs3 = st3
					.executeQuery("SELECT count FROM \"LabelCount\" order by label;");
			int[] label_cnt = new int[21];
			double[] label_prevalance = new double[21];

			int j = 0;
			while (rs3.next()) {
				label_cnt[++j] = rs3.getInt(1);
				java.text.DecimalFormat df = new java.text.DecimalFormat(
						"###.#########");
				label_prevalance[j] = df.parse(df.format((double)label_cnt[j] / (double) 11113))
						.doubleValue();
			}
			
			double[] mestimate = new double[21];
			
			for(int i=1; i <=20; i++) {
				java.text.DecimalFormat df = new java.text.DecimalFormat(
						"###.#########");
				mestimate[i] = df.parse(df.format((20*label_prevalance[i])/(label_cnt[i] + 20))).doubleValue();
			}
			
			for(int i=1; i<=20; i++){
				String query = "update probability set prob_label" + i+ "=" + mestimate[i] + " where prob_label" + i + "=0;";
				st.executeUpdate(query);
			}
			

			

		} catch (SQLException ex) {
			Logger lgr = Logger
					.getLogger(MEstimator.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}