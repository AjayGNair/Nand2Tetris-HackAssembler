import java.io.*;
import java.util.Scanner;

public class Assembler 
{
    public static void main(String args[])                                                       // MAIN FUNCTION
    {
        
        try 
        {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter the filename");
            String filename = in.next();
            File read = new File("H:\\Code\\"+ filename +".asm");
            FileWriter write = new FileWriter("H:\\Code\\"+ filename + ".hack");
            int linenumber = -1;
            int i;
            
            Scanner scan = new Scanner(read);
            String line = "";
            String trim = "";
            String trailzero = "";
            String Binval = "";
            while(scan.hasNextLine())
            {
                line = scan.nextLine();
                trim = line.replaceAll("\\s", "");
                if(trim.indexOf("/") == 0)                                                         // COMMENTS
                {

                }
                else if(trim.indexOf("@") == 0  && Character.isDigit(trim.charAt(1)))                      // A INSTRUCTION
                {   
                    String value = trim.substring(1,trim.length());
                    int decimal = Integer.parseInt(value);
                    String Binary = Integer.toBinaryString(decimal);
                    for(i=0;i<(16-Binary.length());i++)
                    {
                        trailzero = trailzero + "0";
                    }
                    String binvalue = trailzero + Binary + "\n";                                  // BINARY VALUE OF "A INSTRUCTION"
                    write.write(binvalue);
                    trailzero = "";
                    binvalue = "";
                    linenumber++;
                }
                else if(trim.contains("=") && !trim.contains("/"))                                // C INSTRUCTION 1
                {
                    String[] parts = trim.split("=");
                    String part1 = parts[0]; 
                    String part2 = parts[1];
                    Binval = Cinstruction(part1, part2, "");
                    write.write(Binval);
                    linenumber++;
                }
                else if(trim.contains(";") && !trim.contains("/"))                                // C INSTRUCTION 2
                {
                    String[] parts = trim.split(";");
                    String part1 = parts[0]; 
                    String part2 = parts[1];
                    Binval = Cinstruction("",part1,part2);
                    write.write(Binval);
                    linenumber++;
                }
                else if(trim.length() == 0)                                                       // EMPTY LINES
                {

                }
                else
                {
                     // PREDEFINDED SYMBOLS   
                    if((trim.indexOf("@") == 0) && (trim.indexOf("R") == 1))                      // CONVERTING TO BINARY OF PREDEFINED SYMBOLS: R0 TO R15
                    {
                        String value = trim.substring(2,trim.length());
                        int decimal = Integer.parseInt(value);
                        String Binary = Integer.toBinaryString(decimal);
                        for(i=0;i<(16-Binary.length());i++)
                        {
                            trailzero = trailzero + "0";
                        }
                        String binvalue = trailzero + Binary + "\n";
                        trailzero = "";
                        write.write(binvalue);
                    }
                    else if(trim.indexOf("@") == 0)                                                 // OTHER PREDEFINED SYMBOLS
                    { 
                        
                        String otherpredef = "";
                        String binvalue = "";
                        String exsym[][] =                   
                        {
                            {"SCREEN","0100000000000000"},
                            {"KBD","0110000000000000"},
                            {"SP","0000000000000000"},
                            {"LCL","0000000000000001"},
                            {"ARG","0000000000000010"},
                            {"THIS","0000000000000011"},
                            {"THAT","0000000000000100"},
                        };
                        for(i=0;i<exsym.length;i++)
                        {
                            if(trim.equals(exsym[i][0]))
                            {
                                otherpredef = exsym[i][1];
                            }
                            else
                            {

                            }
                        }
                        binvalue = otherpredef + "\n";
                        write.write(binvalue);
                        linenumber++;        
                    }
                    else if(trim.indexOf("(") == 0 && trim.indexOf(")") == (trim.length()-1))                                                    // LABEL SYMBOL
                    {
                        String Label = trim.substring(1,trim.length()-1);
                        int  p = 1;
                        String[][] Labels = new String[100][100];
                        for(i = 0;i < p; i++)
                        {
                            Labels[i][0] = Label;
                            Labels[i][1] = Integer.toString(linenumber);
                        }
                        p++;   
                    }
                    else
                    {
                        linenumber++;
                    }  
                        
                }

            }
            
            write.close();
            scan.close();
            in.close();
            //System.out.println("Number of Lines = "+linenumber);
            
            System.out.println("Successfully created "+ filename + ".hack file");
        } 
        catch (IOException e) 
        {
            System.out.println("An error occurred.");
        }
        finally
        {
        }  
    }
    public static String Cinstruction(String part1, String part2, String part3)                          // C INSTRUCTION FUNCTION
    {
        int i;
        String a = "0";
        String destination_part = part1;
        String computing_part = part2;
        String jump_part = part3;
        String comp_part_bin = "";
        String dest_part_bin = "";
        String jump_part_bin = "";
        String binaryvalue;
        if(computing_part.contains("M"))                                                                  // COMPUTATION IN MEMORY
        {
            a = "1";
        }
        String dest[][] =                                                                                 //DESTINATION PART
        {
            {"null","000"},
            {"M","001"},
            {"D","010"},
            {"MD","011"},
            {"A","100"},
            {"AM","101"},
            {"AD","110"},
            {"AMD","111"},
    
        };
        String comp[][] =                                                                                 //COMPUTING PART
        { 
            {"0","101010"},
            {"1","111111"},
            {"-1","111010"},
            {"D","001100"},
            {"A","110000"},
            {"!D","001101"},
            {"!A","110001"},
            {"-D","001111"},
            {"-A","110011"},
            {"D+1","011111"},
            {"A+1","110111"},
            {"D-1","001110"},
            {"A-1","110010"},
            {"D-1","001110"},
            {"A-1","110010"},
            {"D+A","000010"},
            {"D-A","010011"},
            {"A-D","000111"},
            {"D&A","000000"},
            {"D|A","010101"},
            {"M","110000"},
            {"!M","110001"},
            {"-M","110011"},
            {"M+1","110111"},
            {"M-1","110010"},
            {"D+M","000010"},
            {"D-M","010011"},
            {"M-D","000111"},
            {"D&M","000000"},
            {"D|M","010101"},
        };
        String jump[][] =                                                                                         //JUMP PART
        {
            {"null","000"},
            {"JGT","001"},
            {"JEQ","010"},
            {"JGE","011"},
            {"JLT","100"},
            {"JNE","101"},
            {"JLE","110"},
            {"JMP","111"},

        };
        
        for(i=0;i<comp.length;i++)
        {
            if(computing_part.equals(comp[i][0]))
            {
                comp_part_bin = comp[i][1];
            }
        }
        for(i=0;i<dest.length;i++)
        {
            if(destination_part.equals(dest[i][0]))
            {
                dest_part_bin = dest[i][1];
            }
        }
        for(i=0;i<jump.length;i++)
        {
            if(jump_part.equals(jump[i][0]))
            {
                jump_part_bin = jump[i][1];
            }
        }
        if(jump_part == null || jump_part == "")
        {
            jump_part_bin = "000";
        }
        if(computing_part == null || computing_part == "0")
        {
            comp_part_bin = "101010";
        }
        if(destination_part == null || destination_part == "")
        {
            dest_part_bin = "000";
        } 
        binaryvalue = "111"+ a + comp_part_bin + dest_part_bin + jump_part_bin + "\n";
         a = "0";
         destination_part = "";
         computing_part = "";
         jump_part = "";
         comp_part_bin = "";
         dest_part_bin = "";
         jump_part_bin = "";
        return binaryvalue;                                                                        // RETURN BINARY VALUE OF C INSTRUCTION
    }
    public static String Symbols(String s)                                                          // SYMBOLS
    {
        // PREDEFINED SYMBOLS
        int i;
        String trailzero = "";
        if((s.indexOf("@") == 0) && (s.indexOf("R") == 1))                                          // CONVERTING TO BINARY OF PREDEFINED SYMBOLS: R0 TO R15
        {
            String value = s.substring(2,s.length());
            int decimal = Integer.parseInt(value);
            String Binary = Integer.toBinaryString(decimal);
            for(i=0;i<(16-Binary.length());i++)
            {
                trailzero = trailzero + "0";
            }
            String binvalue = trailzero + Binary + "\n";
            trailzero = "";
            return binvalue;
        }
        else                                                                                          // OTHER PREDEFINED SYMBOLS
        { 
            
            String otherpredef = "";
            String binvalue = "";
            String exsym[][] =                   
            {
                {"SCREEN","0100000000000000"},
                {"KBD","0110000000000000"},
                {"SP","0000000000000000"},
                {"LCL","0000000000000001"},
                {"ARG","0000000000000010"},
                {"THIS","0000000000000011"},
                {"THAT","0000000000000100"},
            };
            for(i=0;i<exsym.length;i++)
            {
                if(s.equals(exsym[i][0]))
                {
                    otherpredef = exsym[i][1];
                }
            }
            binvalue = otherpredef + "\n";
            return binvalue;          
        }
    }
}