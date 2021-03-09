/*
 * PROBLEM STATEMENT: 
 * Implement Pass-II of two pass assembler for pseudo-machine in Java using object oriented features. 
 * The output of assignment-1 (intermediate file and symbol table) should be input for this assignment.
 * 
 * SUBMISSION DATE:
 * 
 */

import java.io.*;
import java.util.*;

public class PassTwoAssembler {
	
	int LC = 0;
	ArrayList<String> LiteralTable = new ArrayList<String>();
	ArrayList<Integer> LOC = new ArrayList<Integer>();
	ArrayList<Integer> PoolTable = new ArrayList<Integer>();
	IndexedLinkedHashMap<String, Integer> SymbolTable = new IndexedLinkedHashMap<String, Integer>();
	
	PassTwoAssembler() throws IOException{
		LoadTables();
		System.out.println("Successfully loaded Pass-I data\nSymbol Table:");
		
		Set <String> keys = SymbolTable.keySet();
		for(String i: keys){
			System.out.println(SymbolTable.getIndexOf(i) +" " + i +" "+ SymbolTable.get(i));
		}
	}
	
	public void LoadTables() throws IOException{
		FileReader f1 = new FileReader("LiteralTable.txt");
		FileReader f2 = new FileReader("PoolTable.txt");
		FileReader f3 = new FileReader("SymbolTable.txt");
		
		BufferedReader br1 = new BufferedReader(f1);
		BufferedReader br2 = new BufferedReader(f2);
		BufferedReader br3 = new BufferedReader(f3);
		
		String line;
		
		while((line=br1.readLine())!=null){
			String tokens[] = line.split(" ");
			LiteralTable.add(tokens[0]);
			LOC.add(operandTypeHandler(tokens[1]));
		}
		while((line=br2.readLine())!=null){
			PoolTable.add(Integer.parseInt(line));
		}
		while((line=br3.readLine())!=null){
			String tokens[] = line.split(" ");
			SymbolTable.put(tokens[1], Integer.parseInt(tokens[2]));
		}
		
		br1.close();
		br2.close();
		br3.close();
	}
	
	private Integer operandTypeHandler(String loc){
		int newLOC = Integer.parseInt(loc.replaceAll("[^0-9]", "")); 
		return newLOC;
	}
	
	public int getImperative(String o){
		int temp = 0;
		if(o.contains("L")) {
			return LOC.get(operandTypeHandler(o)-1);
		}
		else {
			for(String i:SymbolTable.keySet()) {
				if(SymbolTable.getIndexOf(i)==operandTypeHandler(o)) {
					temp = SymbolTable.get(i);
					break;
				}
			}
			return temp;
		}
	}
	
	public void pass2Assembly(String name) throws IOException{
		FileWriter f = new FileWriter("targetCode.txt");
		BufferedReader reader = new BufferedReader(new FileReader(name));
		String line, codes[], ops[];
		int len=0;
				
		while((line=reader.readLine())!=null){
			codes = line.split(" ");
			len = codes.length;
			if(codes[1].compareTo("(AD,01)")==0){
				LC = operandTypeHandler(codes[2]);
				System.out.println("start found at "+LC);
			}
			else if(codes[1].compareTo("(AD,03)")==0){ 
				if(codes[2].contains("+")) {
					ops = codes[2].split("\\+");
					int temp = operandTypeHandler(ops[0]);
					for(String i:SymbolTable.keySet()) {
						if(SymbolTable.getIndexOf(i)==temp) {
							LC = SymbolTable.get(i) + Integer.parseInt(ops[1]);
							System.out.println(LC);
							break;
						}
					}
				}
			}
			else if(codes[1].compareTo("(IS,00)")==0){ //(no operands)
				f.write(LC +" 00 ");
				f.write("\n");
				LC++;
			}
			else if(codes[1].compareTo("(IS,01)")==0){
				if(len==3)
					f.write(LC + " 01 "+getImperative(codes[2]));
				else if(len==4)
					f.write(LC + " 01 "+codes[2].charAt(1)+" "+getImperative(codes[3]));
				f.write("\n");
				LC++;
			}
			else if(codes[1].compareTo("(IS,02)")==0){
				if(len==3)
					f.write(LC + " 02 "+getImperative(codes[2]));
				else if(len==4)
					f.write(LC + " 02 "+codes[2].charAt(1)+" "+getImperative(codes[3]));
				f.write("\n");
				LC++;
			}
			else if(codes[1].compareTo("(IS,03)")==0){
				if(len==3)
					f.write(LC + " 03 "+getImperative(codes[2]));
				else if(len==4)
					f.write(LC + " 03 "+codes[2].charAt(1)+" "+getImperative(codes[3]));
				f.write("\n");
				LC++;
			}
			else if(codes[1].compareTo("(IS,04)")==0){
				if(len==3)
					f.write(LC + " 04 "+getImperative(codes[2]));
				else if(len==4)
					f.write(LC + " 04 "+codes[2].charAt(1)+" "+getImperative(codes[3]));
				f.write("\n");
				LC++;
			}
			else if(codes[1].compareTo("(IS,05)")==0){
				if(len==3)
					f.write(LC + " 05 "+getImperative(codes[2]));
				else if(len==4)
					f.write(LC + " 05 "+codes[2].charAt(1)+" "+getImperative(codes[3]));
				f.write("\n");
				LC++;
			}
			else if(codes[1].compareTo("(IS,06)")==0){
				if(len==3)
					f.write(LC + " 06 "+getImperative(codes[2]));
				else if(len==4)
					f.write(LC + " 06 "+codes[2].charAt(1)+" "+getImperative(codes[3]));
				f.write("\n");
				LC++;
			}
			else if(codes[1].compareTo("(IS,07)")==0){
				if(len==3)
					f.write(LC + " 07 "+getImperative(codes[2]));
				else if(len==4)
					f.write(LC + " 07 "+codes[2].charAt(5)+" "+getImperative(codes[3]));
				f.write("\n");
				LC++;
			}
			else if(codes[1].compareTo("(IS,08)")==0){
				if(len==3)
					f.write(LC + " 08 "+getImperative(codes[2]));
				else if(len==4)
					f.write(LC + " 08 "+codes[2].charAt(1)+" "+getImperative(codes[3]));
				f.write("\n");
				LC++;
			}
			else if(codes[1].compareTo("(IS,09)")==0){
				if(len==3)
					f.write(LC + " 09 "+getImperative(codes[2]));
				else if(len==4)
					f.write(LC + " 09 "+codes[2].charAt(1)+" "+getImperative(codes[3]));
				f.write("\n");
				LC++;
			}
			else if(codes[1].compareTo("(IS,10)")==0){
				if(len==3)
					f.write(LC + " 10 "+getImperative(codes[2]));
				else if(len==4)
					f.write(LC + " 10 "+codes[2].charAt(1)+" "+getImperative(codes[3]));
				f.write("\n");
				LC++;
			}
			else if(codes[1].compareTo("(DL,01)")==0){
				f.write(LC + " 00 00 " + operandTypeHandler(codes[2]));
				f.write("\n");
				LC++;
			}
			else if(codes[1].compareTo("(DL,02)")==0){
				f.write(LC+"\n");
				LC++;
			}
		}
		f.close();
		reader.close();
	}
	
	public static void main(String[] args) throws IOException {
		PassTwoAssembler obj = new PassTwoAssembler();
		obj.pass2Assembly("IntermediateCode.txt");
	}
}
