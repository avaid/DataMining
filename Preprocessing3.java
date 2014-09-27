import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class Preprocessing3 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		BufferedReader br2 = openFileForReading("training_with_labels.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("final_training_data.txt")));
		
		String strLine = null, strLine2 = null;
		while((strLine2 = br2.readLine()) != null) {
			String[] tokens = strLine2.split(" ");
			BufferedReader br = openFileForReading("attrcount.txt");
			
			while((strLine = br.readLine()) != null) {
				String tokens2[] = strLine.split(":");
				if(tokens[1].equals(tokens2[0]) && !tokens2[1].equals("0")) {
					bw.write(strLine2 + "\n");
				}
			}
			br.close();
			
		}
		br2.close();
		bw.close();

	}
	
	private static BufferedReader openFileForReading(String path) throws FileNotFoundException {
		FileInputStream fstream = new FileInputStream(path);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		return br;
	}

}
