import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProcessTestData {

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
			rs3.close();
			
			
			// start processing the test data now
			
			for(int i=1; i<=7661; i++) {
				double[] probabilities = new double[21];
				for(j=1; j<=20; j++) {
					double probability = 1.0;
					rs = st.executeQuery("select attr_id, attr_value from \"TestData\" where record_id = " + i);
					while(rs.next()) {
						int attr_id = rs.getInt(1);
						int attr_value = rs.getInt(2);
						
							Statement st4 = con.createStatement(
									ResultSet.TYPE_SCROLL_INSENSITIVE,
									ResultSet.CONCUR_READ_ONLY);
							String query = "select prob_label"+j+" from \"probability\" where attr_id = " + attr_id + " and attr_value = " + attr_value + ";";
							ResultSet rs4 = st4.executeQuery(query);
							java.text.DecimalFormat df = new java.text.DecimalFormat(
									"###.#########");
							if(rs4.next()) {
								probability = df.parse(df.format(probability * 100 * rs4.getDouble(1))).doubleValue();
							}
							rs4.close();
					}
					java.text.DecimalFormat df = new java.text.DecimalFormat(
							"###.#########");
					
					probabilities[j] = df.parse(df.format(probability * label_prevalance[j])).doubleValue();
				}
				rs.close();
				int index = find_largest(probabilities);
				Statement st5 = con.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				
				int result = st5.executeUpdate("update \"TestData\" set label =" + index + "where record_id = " + i);
				if(result == 1) {
					System.out.println("Label for record id = " + i + " updated");
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static int find_largest(double[] probabilities) {
		double max = probabilities[1];
		int index = 1;
		for(int i=2; i<=20; i++) {
			if(probabilities[i] > max) {
				max = probabilities[i];
				index = i;
			}
		}
		return index;
	}
}
