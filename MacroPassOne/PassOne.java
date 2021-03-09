import java.io.*;
import java.util.*;

public class PassOne {
	ArrayList<MNTEntry>MNT;
	ArrayList<String>MDT;
	ArrayList<String>PNTAB;  //Parameter Name Table [PNTAB] - Fields - Parameter Name 
	HashMap<String, String>KPDTAB; //Keyword parameter Default Table [KPDTAB] 
	
	public PassOne(){
		MNT = new ArrayList<MNTEntry>();
		MDT = new ArrayList<String>();
		PNTAB = new ArrayList<String>();
		KPDTAB = new HashMap<String, String>();
	}
	public MacroOutput getOutput(BufferedReader reader) throws IOException{
		String line;
		int mdtp=0;
		int kpdtp=0;
		int kp=0;
		int pp=0;
		StringBuilder icode= new StringBuilder();
		
		while((line=reader.readLine())!=null){
			String parts[] = line.split("\\s+");
			
			if(parts[0].equals("MACRO")){
				PNTAB.clear(); //new for each macro
				
				String prototype[] = reader.readLine().split("\\s+|,\\s+");
				MNTEntry entry = new MNTEntry(prototype[0], mdtp, kpdtp);
				
				//first, parameters
				for(int i=1; i<prototype.length; i++){
					if(prototype[i].contains("=")){ //keyword parameter
						entry.incrementKP();
						kpdtp++;
						String param[] = prototype[i].replace("&", "").split("=");
						KPDTAB.put(param[0], (param.length == 2) ? param[1] : "-");
						PNTAB.add(param[0]);
					}
					else { //positional parameter
						entry.incrementPP();
						PNTAB.add(prototype[i].replace("&", ""));
					}
				}
				MNT.add(entry);
				
				//second, instructions
				String instruction = reader.readLine();
				StringBuilder instructionRow = new StringBuilder();
				
				while(!instruction.equals("MEND")){
					String tempInstr[] = instruction.split("\\s+|,\\s+");
					
					instructionRow.append(mdtp).append(" ");
					instructionRow.append(tempInstr[0]).append("\t");
					instructionRow.append(handleOperand(tempInstr[1]));
					
					if (tempInstr.length == 3)
						instructionRow.append(",").append(handleOperand(tempInstr[2]));
					
					instructionRow.append("\n");
					mdtp++;
					instruction = reader.readLine();
				}
				MDT.add(instructionRow.toString());	
			}
			else {
				icode.append(line).append("\n");
			}
		}
		MacroOutput output = new MacroOutput();
		output.setIC(icode.toString());
		
		final StringBuilder mnt = new StringBuilder(); 
		MNT.forEach((entry) -> mnt.append(entry).append("\n"));
		output.setMNT(mnt.toString());
		
		final StringBuilder mdt = new StringBuilder();		
		MDT.forEach((entry) -> mdt.append(entry));
		output.setMDT(mdt.toString());
		
		final StringBuilder kpdtab = new StringBuilder();		
		KPDTAB.forEach((key,value) -> kpdtab.append(key).append("\t").append(value).append("\n"));
		output.setKPDTAB(kpdtab.toString());
		
		final StringBuilder pntab = new StringBuilder();		
		PNTAB.forEach((entry) -> pntab.append(entry).append("\n"));
		output.setPNTAB(pntab.toString());
		
		return output;
	}
	
	public String handleOperand(String operand) {
		int idx = PNTAB.indexOf(operand.replace("&", ""));
		String op;
		if (idx != -1)
			op = "(P," + (idx+1) + ")";
		else
			op = operand;
		return op;
	}
}
class MNTEntry{
	String name;
	int pp;
	int kp;
	int mdtp;
	int kpdtp;
	
	public MNTEntry(String name, int mdtp, int kpdtp){
		this.name = name;
		this.pp = 0;
		this.kp = 0;
		this.mdtp = mdtp;
		this.kpdtp = kpdtp;
	}
	public MNTEntry(String name, int pp, int kp, int mdtp, int kpdtp){
		this.name = name;
		this.pp = pp;
		this.kp = kp;
		this.mdtp = mdtp;
		this.kpdtp = kpdtp;
	}
	public void incrementPP(){
		pp++;
	}
	public void incrementKP(){
		kp++;
	}
	
	@Override
	public String toString(){
		return name+" "+pp+" "+kp+" "+mdtp+" "+kpdtp+" ";
		
	}
}
class MacroOutput{
	String IC;
	String MNT;
	String MDT;
	String KPDTAB;
	String PNTAB;
	
	public String getIC(){
		return IC;
	}
	public String getMNT(){
		return MNT;
	}
	public String getKPDTAB(){
		return KPDTAB;
	}
	public String getMDT(){
		return MDT;
	}
	public String getPNTAB(){
		return PNTAB;
	}
	public void setIC(String ic){
		IC = ic;
	}
	public void setMNT(String mnt){
		MNT = mnt;
	}
	public void setKPDTAB(String kpdtab){
		KPDTAB = kpdtab;
	}
	public void setPNTAB(String pntab){
		PNTAB = pntab;
	}
	public void setMDT(String mdt){
		MDT = mdt;
	}
}