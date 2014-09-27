import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProbabilityCalculator {

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

			rs = st.executeQuery("SELECT distinct attr_id  FROM \"Attr_Label_Count\" order by attr_id asc;");

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

			while (rs.next()) {
				int attr_id = rs.getInt(1);
				
				ResultSet rs4 = null;
				Statement st4 = con.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				rs4 = st4
						.executeQuery("select distinct attr_value FROM \"Attr_Label_Count\" where attr_id ="
								+ attr_id + "order by attr_value;");
				
				while(rs4.next()) {
					int attr_value = rs4.getInt(1);
					ResultSet rs2 = null;
					Statement st2 = con.createStatement(
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					rs2 = st2
							.executeQuery("select count,label FROM \"Attr_Label_Count\" where attr_id ="
									+ attr_id + " and attr_value =" + attr_value +";");
					double[] prob = new double[21];
					while (rs2.next()) {
						java.text.DecimalFormat df = new java.text.DecimalFormat(
								"###.#########");
	
						double count = rs2.getInt(1);
						int label = rs2.getInt(2);
						
						prob[label] = df.parse(
								df.format((count + (label_prevalance[label])*20)/(label_cnt[label] + 20)))
								.doubleValue();
					}
							
					Statement st5 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					String insert_query = "insert into probability values("+attr_id +"," + attr_value + "," + prob[1] + "," + prob[2] + "," + prob[3] + "," + prob[4] + "," + prob[5] + "," + prob[6] + "," + prob[7] + "," + prob[8] + "," +prob[9] + "," + prob[10] + "," + prob[11] + "," + prob[12] + "," + prob[13] + "," + prob[14] + "," + prob[15] + "," + prob[16] + "," + prob[17] + "," + prob[18] + "," + prob[19] + "," + prob[20] + ");";
					st5.executeUpdate(insert_query);
				}
				
				
				

			}

		} catch (SQLException ex) {
			Logger lgr = Logger
					.getLogger(ProbabilityCalculator.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}