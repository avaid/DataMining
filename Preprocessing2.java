import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;


public class Preprocessing2 {

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
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("attrcount.txt")));
			for(int i=1; i<attributeArray.length; i++) {
				bw.write(i + ":" + attributeArray[i]+"\n");
			}
			bw.close();
			in.close();
			br.close();
			
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
