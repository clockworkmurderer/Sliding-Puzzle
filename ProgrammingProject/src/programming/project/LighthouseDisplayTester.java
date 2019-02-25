package programming.project;

import java.io.IOException;
import de.cau.infprogoo.lighthouse.LighthouseDisplay;

public class LighthouseDisplayTester {

	public static void main (String[] args) {
		
		LighthouseDisplay display = new LighthouseDisplay("stu213278", "API-TOK_p+6z-8s84-LOwF-TEwL-R/Y7");
		
		try { // Try connecting to the display
			display.connect();
		} catch (Exception e) {
			System.out.println("Connection failed: " + e.getMessage());
			e.printStackTrace();
		}

		try { // Send data to the display
				// This array contains for every window (14 rows, 28 columns) three
				// bytes that define the red, green, and blue component of the color
				// to be shown in that window. See documentation of LighthouseDisplay's
				// send(...) method.
			byte[] data = new byte[14 * 28 * 3];
			
			for (int element = 0; element < data.length; element++) {
				data[element] = (byte)255;
			}

			display.send(data);
		} catch (IOException e) {
			System.out.println("Connection failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
