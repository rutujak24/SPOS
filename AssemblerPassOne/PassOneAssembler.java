/*
 * PROBLEM STATEMENT:
 * Design suitable data structures and implement pass one of a two pass assembler 
 * for pseudo-machine in Java using object oriented features. Implementation should
 * consist of a few instructions from each category and a few assembler directives.
 * 
 */
import java.util.*;
import java.io.*;

public class PassOneAssembler{
	String fileName;
	String tokens[];
	int lc = 0;
	int counter = 1;// for literals
	int pool = 1;
	String finalcode;
	
	Hashtable<String, String> MachineOpTable = new Hashtable<String, String>();
	Hashtable<String, String> RegistersTable = new Hashtable<String, String>();
	Hashtable<String, String> ConditionCodesTable = new Hashtable<String, String>();
	IndexLinkedHashMap<String, Integer> SymbolTable = new IndexLinkedHashMap<String, Integer>();

	ArrayList<Integer> PoolTable = new ArrayList<>();
	ArrayList<String> LiteralTable = new ArrayList<>();
	ArrayList<Integer> LOC = new ArrayList<>();
	ArrayList<String> output = new ArrayList<String>();

	PassOneAssembler(String fileName){
		this.fileName = fileName;
		createtables();
	}

	public void AssembleCode() throws IOException {
		String line;
		int check;
		
		FileReader f = new FileReader(fileName);
		FileWriter f1 = new FileWriter("IntermediateCode.txt");
		FileWriter f2 = new FileWriter("SymbolTable.txt");
		FileWriter f3 = new FileWriter("LiteralTable.txt");
		FileWriter f4 = new FileWriter("PoolTable.txt");

		BufferedReader br = new BufferedReader(f);
		BufferedWriter bw = new BufferedWriter(f1);
		BufferedWriter bw1 = new BufferedWriter(f2);
		BufferedWriter bw2 = new BufferedWriter(f3);
		BufferedWriter bw3 = new BufferedWriter(f4);

		while ((line = br.readLine()) != null){
			tokens = line.split(" ");
			if (!checkLabel()) { // label is present
				SymbolTable.put(tokens[0], lc);
				check = 1;
			} else
				check = 0;

			String cmd = tokens[check];
			String value;
			if (cmd.equals("START") || cmd.equals("ORIGIN")){
				if (cmd.equals("START")){
					value = MachineOpTable.get("START");
					
					lc = Integer.parseInt(tokens[1]);
					String c = "(C," + lc + ")";
					finalcode = value + " " + c;
					
					bw.write(finalcode);
					bw.append(System.lineSeparator());
					
					System.out.println(finalcode + "start");

				} 
				else{
					value = MachineOpTable.get("ORIGIN");
					// tokens[1] will contain the entire operand
					
					finalcode = value + " " + expression();
					bw.write(finalcode);
					bw.append(System.lineSeparator());

					System.out.println(finalcode + "origin");

				}
			} 
			else if (tokens[check].equalsIgnoreCase("LTORG")){
				PoolTable.add(pool);
				for (int i = pool - 1; i < counter - 1; i++){ 
					LOC.add(lc);
					
					String lit = LiteralTable.get(i);
					lc++;
					
					int l = lit.charAt(2) - '0'; //key is literal
					String c = "(DL,02)" + " " + "(C," + l + ")";
					
					bw.write(c);
					bw.append(System.lineSeparator());

					System.out.println(c + "ltorg");
				}
				pool = counter; //updating to next pool
			} 
			else if(tokens[check].equals("EQU")){ //tokens[0] is a symbol
				if(checkInt(tokens[2])){ //constant
					SymbolTable.put(tokens[0], Integer.parseInt(tokens[2]));
				}
				else{ //operand is a expression
					String operand = tokens[2];
					String ops[];
					if(operand.contains("+")){
						ops = operand.split("\\+");
						SymbolTable.put(tokens[0], SymbolTable.get(ops[0]) + Integer.parseInt(ops[1]));
					} 
					else if(operand.contains("-")){
						ops = operand.split("\\-");
						SymbolTable.put(tokens[0], SymbolTable.get(ops[0]) - Integer.parseInt(ops[1]));
					} 
					else{ //only symbol
						SymbolTable.put(tokens[0], SymbolTable.get(tokens[2]));
					}
				}
			} 
			else if(tokens[check].equalsIgnoreCase("DS")){
				String symbol, c;
				symbol = tokens[0];
				if (tokens[2].contains("'")){
					value = MachineOpTable.get(tokens[1]); // DC
					SymbolTable.put(symbol, lc); // if symbol already present
					lc++; // address will get updated
					int v = tokens[2].charAt(1) - '0'; // if not symbol is
														// inserted
					c = "(C," + v + ")";
				} 
				else{
					value = MachineOpTable.get(tokens[1]);
					SymbolTable.put(symbol, lc);
					lc = lc + Integer.parseInt(tokens[2]);
					c = "(C," + Integer.parseInt(tokens[2]) + ")";
				}
				
				finalcode = value + " " + c;
				bw.write(finalcode);
				bw.append(System.lineSeparator());

				System.out.println(finalcode + "ds");
			} 
			else if(!tokens[check].equals("END")){ //IS
				int len = tokens.length;
				value = MachineOpTable.get(tokens[check]);

				for (int i = check + 1; i < len; i++) {
					output.add(handleOperands(tokens[i]));
				}
				if (output.size() == 0)
					finalcode = value;
				if (output.size() == 1)
					finalcode = value + " " + output.get(0);
				else if (output.size() == 2)
					finalcode = value + " " + output.get(0) + " " + output.get(1);

				lc++;
				output.clear();
				System.out.println(finalcode + "is");
				bw.write(finalcode);
				bw.append(System.lineSeparator());

			}
			if(tokens[check].equals("END")){
				String kp = MachineOpTable.get("END");
				System.out.println(kp);
				bw.write(kp);
				bw.append(System.lineSeparator());
				
				PoolTable.add(pool);
				
				for(int i = pool - 1; i < counter - 1; i++){ 
					LOC.add(lc);
					String lit = LiteralTable.get(i);
					lc++;
					
					int l = lit.charAt(2) - '0';
					String c = "(DL,01)" + " " + "(C," + l + ")";
					System.out.println(c);
					
					bw.write(c);
					bw.append(System.lineSeparator());
				}
				pool = counter; // updating to next pool
			}
		}
		System.out.println("Writing Final Symbol Table");
		Set<String> keys = SymbolTable.keySet();
		for (String i : keys) {
			bw1.write(SymbolTable.getindex(i) + " " + i + " " + SymbolTable.get(i));
			System.out.println(SymbolTable.getindex(i) + " " + i + " " + SymbolTable.get(i));
			bw1.append(System.lineSeparator());
		}
		System.out.println("Writing Final Literal Table");
		for (int i = 0; i < LiteralTable.size(); i++) {
			bw2.write((i + 1) + " " + LiteralTable.get(i) + " " + LOC.get(i));
			System.out.println((i + 1) + " " + LiteralTable.get(i) + " " + LOC.get(i));
			bw2.append(System.lineSeparator());
		}
		System.out.println("Writing Final Pool Table");
		for (int i = 0; i < PoolTable.size(); i++) {
			bw3.write(PoolTable.get(i).toString());
			System.out.println(PoolTable.get(i).toString());
			bw3.append(System.lineSeparator());
		}
		bw.close();
		bw1.close();
		bw2.close();
		bw3.close();
	}

	public boolean checkLabel() {
		String check = tokens[0];
		return MachineOpTable.containsKey(check);
	}

	public String expression() {
		String operand = tokens[1];
		String ops[];
		int index;
		String c = " ";
		
		if(operand.contains("+")){
			ops = operand.split("\\+");
			lc = SymbolTable.get(ops[0]) + Integer.parseInt(ops[1]);
			index = SymbolTable.getindex(ops[0]);
			c = "(S," + index + ")" + "+" + ops[1];
		} 
		else if(operand.contains("-")){
			ops = operand.split("\\-");
			lc = SymbolTable.get(ops[0]) - Integer.parseInt(ops[1]);
			index = SymbolTable.getindex(ops[0]);
			System.out.println(ops[0]);
			c = "(S," + index + ")" + "-" + ops[1];
		} 
		else{// only one operand
			if(checkInt(operand)){
				lc = Integer.parseInt(operand);
				c = "(C," + operand + ")";
			}else {
				index = SymbolTable.getindex(operand);
				lc = SymbolTable.get(operand);
				c = "(S," + index + ")";
			}
		}
		return c;
	}

	public boolean checkInt(String op) {
		try {
			int c = Integer.parseInt(op);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String handleOperands(String c) {
		String c1 = "";
		if (RegistersTable.containsKey(c)) {
			c1 = RegistersTable.get(c);
		} else if (ConditionCodesTable.containsKey(c)) {
			c1 = ConditionCodesTable.get(c);
		} else if (c.contains("=")) {
			int i = c.charAt(2) - '0'; // at index 2 literal will be present
			c1 = "(L," + counter + ")";
			LiteralTable.add(c);
			counter++;
		} else {
			if (SymbolTable.containsKey(c))
				c1 = "(S," + SymbolTable.getindex(c) + ")";
			else {
				// add symbol
				SymbolTable.put(c, 0);
				c1 = "(S," + SymbolTable.getindex(c) + ")";
			}
		}
		return c1;
	}

	public void createtables() {
		MachineOpTable.put("STOP", "(IS,00)"); // IMPERATIVE STATEMENT
		MachineOpTable.put("ADD", "(IS,01)");
		MachineOpTable.put("SUB", "(IS,02)");
		MachineOpTable.put("MULT", "(IS,03)");
		MachineOpTable.put("MOVER", "(IS,04)");
		MachineOpTable.put("MOVEM", "(IS,05)");
		MachineOpTable.put("COMP", "(IS,06)");
		MachineOpTable.put("BC", "(IS,07)");
		MachineOpTable.put("DIV", "(IS,08)");
		MachineOpTable.put("READ", "(IS,09)");
		MachineOpTable.put("PRINT", "(IS,10)");

		MachineOpTable.put("START", "(AD,01)"); // ASSEMBLER DIRECTIVE
		MachineOpTable.put("END", "(AD,02)");
		MachineOpTable.put("ORIGIN", "(AD,03)");
		MachineOpTable.put("EQU", "(Ad,04)");
		MachineOpTable.put("LTORG", "(AD,05)");

		MachineOpTable.put("DS", "(DL,01)"); // DECLARATIVE
		MachineOpTable.put("DC", "(DL,02)");

		RegistersTable.put("AREG", "(1)");
		RegistersTable.put("BREG", "(2)");
		RegistersTable.put("CREG", "(3)");
		RegistersTable.put("DREG", "(4)");

		ConditionCodesTable.put("LT", "(CC,01)");
		ConditionCodesTable.put("LE", "(CC,02)");
		ConditionCodesTable.put("EQ", "(CC,03)");
		ConditionCodesTable.put("GT", "(CC,04)");
		ConditionCodesTable.put("GE", "(CC,05)");
		ConditionCodesTable.put("ANY", "(CC,06)");
	}

	public static void main(String args[]) {
		PassOneAssembler a = new PassOneAssembler("input.txt");
		try {
			a.AssembleCode();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}