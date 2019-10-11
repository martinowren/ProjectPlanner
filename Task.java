import java.util.LinkedList;
class Task {
    int id, time, staff;
    String name;
    int earliestStart, latestStart, slack;
    LinkedList <Integer> tempListeForOutEdges;
    LinkedList <Task> outEdges = new LinkedList<Task>();
    LinkedList <Task> inEdges = new LinkedList<Task>();
    boolean vaertBesokt = false;
    int cntPredecessors, cntPredecessorsOld, numOutEdges, numOutEdges1;
    Project mor;



    /**
     * Constructor
     */
    public Task(int i, String n, int t, int m, LinkedList<Integer> l, Project p) {
        id = i;
        name = n;
        time = t;
        staff = m;
        tempListeForOutEdges = l;
        mor = p;
        
        for(int edge : l) {
            cntPredecessors++;
            cntPredecessorsOld++;
        }
    }

    /**
     * Hjelpefunksjon for Prosjekt.isRealizable()
     * @param forrige
     * @param veienTidligere
     * @return true om det ikke er en sykel
     */
    public boolean isRealizableHelper(Task forrige, String veienTidligere) {
        if( !veienTidligere.contains(String.format(">%d>", this.hentId())) ) {
            veienTidligere += String.format("%d>", this.hentId());
            for(Task oppgaver : outEdges) {
                if(oppgaver.hentId()  != forrige.hentId()) {
                    if(mor.harSykel == false){
                        mor.hentTask(oppgaver.hentId()).isRealizableHelper(this, veienTidligere);
                    } else {
                        return false;
                    }
                }
            }


        } else {
            mor.harSykel = true;
            veienTidligere += String.format("%d", this.hentId());
            mor.cycle = "The cycle is: " + veienTidligere;
            return false;
        }
        return true;
        
    }

    /**
     * hentId()
     * @return id
     */
    public int hentId(){
        return id;
    }

    /**
     * Hjelpefunksjon
     * @return edgene til Task'en
     */
    public LinkedList<Task> hentEdges() {
        return outEdges;
    }

    /**
     * toString()
     * @return String
     */
    public String toString() {
        System.out.print(id + " " + name + " " + time + " " + staff + " ");        
        for(Task ed : outEdges) {
            System.out.print(ed.hentId() +" ");
        }
        return " ";
    }

}