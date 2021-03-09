/* 
 * PROBLEM STATEMENT:
 * Design suitable data structures and implement pass-I of a two-pass macro-processor using OOP features in Java.
 * 
 */

import java.io.*;

public class Main {
	public static void main(String[] args) {
		BufferedWriter writer;
		BufferedReader reader;
		PassOne macroProcessor = new PassOne();
		try{
			reader = new BufferedReader(new FileReader("input.txt"));
			MacroOutput output = macroProcessor.getOutput(reader);
			writer = new BufferedWriter(new FileWriter("IC.txt"));
			writer.write(output.getIC());
			System.out.println("Intermediate Code: ");
			System.out.println(output.getIC());
			writer.close();
			
			writer = new BufferedWriter(new FileWriter("MNT.txt"));
			writer.write(output.getMNT());
			System.out.println("MNT: ");
			System.out.println(output.getMNT());
			writer.close();
			
			writer = new BufferedWriter(new FileWriter("MDT.txt"));
			writer.write(output.getMDT());
			System.out.println("MDT: ");
			System.out.println(output.getMDT());
			writer.close();

			writer = new BufferedWriter(new FileWriter("PNTAB.txt"));
			writer.write(output.getPNTAB());
			System.out.println("PNTAB: ");
			System.out.println(output.getPNTAB());
			writer.close();

			writer = new BufferedWriter(new FileWriter("KPDTAB.txt"));
			writer.write(output.getKPDTAB());
			System.out.println("KPDTAB: ");
			System.out.println(output.getKPDTAB());
			writer.close();

		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

}
