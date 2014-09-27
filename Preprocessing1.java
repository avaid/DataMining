import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class Preprocessing1 {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		FileInputStream fstream = new FileInputStream("training.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String strLine;
		try {
			// this will get the count of each attribute's appearance in the file
			int[] attributeArray = new int[108522];
			int[] recordCountArray = new int[11114];
			while ((strLine = br.readLine()) != null) {
				String[] tokens = strLine.split(" ");
				attributeArray[Integer.parseInt(tokens[1])]++;
				recordCountArray[Integer.parseInt(tokens[0])]++;
				
			}			
			BufferedReader br2 = openFileForReading("label_training.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("training_with_labels.txt")));
			br = openFileForReading("training.txt");
			for(int j=1; j<=11113; j++) {		
				strLine = br2.readLine();
				String strLine2 = null;
				
				for(int k=1; k<=recordCountArray[j]; k++) {
					strLine2 = br.readLine();
					String[] tokens = strLine2.split(" ");		
					bw.write(tokens[0] + " " + tokens[1] + " " + tokens[2] + " " + strLine + "\n");
				}
			}
			bw.close();
			in.close();
			br.close();
				
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static BufferedReader openFileForReading(String path) throws FileNotFoundException {
		FileInputStream fstream = new FileInputStream(path);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		return br;
	}

}
