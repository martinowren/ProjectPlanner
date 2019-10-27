import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Oblig2 {
    static Project prosjektObj = null;
    

    public static void main(String[] args) {
        File filen = new File(args[0]);
        lesFraFil(filen , args[0]);
        
        if(prosjektObj.isRealizable()) {
            prosjektObj.mostOptimalScheduel();
            prosjektObj.debug();
        } else {
            System.out.println("The project is not relizable due to a cycle");
            System.out.println(prosjektObj.cycle);
        }
        
    }

    /**
     * lesFraFil()
     * @param fil
     * @param navn
     */
    public static void lesFraFil(File fil, String navn) {
        
        Scanner scannerObj = null;
        try {
            scannerObj = new Scanner(fil);
        } catch (FileNotFoundException e) {
            System.out.print("Kunne ikke finne filen.\n");
            return;
        }

        int innlestLinje = scannerObj.nextInt();
        prosjektObj = new Project(navn, innlestLinje);


        while(scannerObj.hasNextLine()) {
            int id = scannerObj.nextInt();
            String name = scannerObj.next();
            int time = scannerObj.nextInt();
            int man = scannerObj.nextInt();
            String inputet = scannerObj.nextLine();
            inputet = inputet.replaceAll("\\s{2,}", " ").trim();
            String[] depend = inputet.split(" ");
            LinkedList<Integer> tempLink = new LinkedList<Integer>();

            for(String dp : depend) {
                int i = Integer.parseInt(dp);
                if (i != 0) {
                    tempLink.add(i);
                }   

            }

            System.out.println("");
            Task T = new Task(id, name, time, man, tempLink, prosjektObj);
            prosjektObj.addTask(T);
        }

        for(Task t1 : prosjektObj.listen) {
            for(int t2 : t1.tempListeForOutEdges) {
                prosjektObj.listen.get(t2-1).outEdges.add(t1);
                prosjektObj.listen.get(t2-1).numOutEdges++;
                prosjektObj.listen.get(t2-1).numOutEdges1++;                
            }
        }
        System.out.println("");

        for(Task t1 : prosjektObj.listen) {
            for(Task t2 : t1.outEdges) {
                t2.inEdges.add( prosjektObj.hentTask(t1.id)); 
            }
        }

    }

}