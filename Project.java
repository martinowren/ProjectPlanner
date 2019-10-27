import java.util.LinkedList;
class Project {
    public boolean harSykel;
    String name;
    int antTasks;
    int kortesteTid = 0;
    String cycle = "";

    LinkedList<Task> listen = new LinkedList<Task>();

    /**
     * Constructor
     */
    public Project(String n, int a) {
        name = n;
    }

    /**
     * Funksjon som returnerer true hvis Prosjektet ikke har en sykel
     * @return if the project is realizable
     */
    public boolean isRealizable() {
        tilbakeStilling();
        for(Task oppgave : listen) {
            if( !(oppgave.isRealizableHelper(oppgave, "")) ) {
                // System.out.println("x");
                return false;
            }

        }
        // System.out.println("TRUE");
        return true;
    }


    /**
     * Sorterer Prosjektet Topologisk
     */
    public void TopologicalSort() {
        this.tilbakeStilling();
        
        LinkedList<Task> prioKoe = new LinkedList<Task>();

        Task task;
        // int teller = 0;

        // Trenger ikke å sjekke noe hvis tasken ikke har noen predececcors
        for(Task opg : listen) {
            opg.earliestStart = 0;
            if(opg.cntPredecessors==0) {
                opg.earliestStart = 0;
                prioKoe.add(opg);
            }
        }

        while(!prioKoe.isEmpty()) {
            task = prioKoe.removeFirst();
            // teller++;

            for(Task opg : task.outEdges) {
                opg.cntPredecessors--;

                // Task sin starttid er lik predecessoren sin pluss dens tid.
                System.out.println(opg.earliestStart);
                if(opg.earliestStart < (task.earliestStart+task.time)) {
                    opg.earliestStart = task.earliestStart+task.time;
                }

                // Er tiden for at tasken skal avsluttes lengre enn cur kortesteTid
                if(opg.earliestStart+opg.time > this.kortesteTid) {
                    this.kortesteTid = opg.earliestStart+opg.time;
                }

                // Legger til i køen
                if(opg.cntPredecessors==0) {
                    prioKoe.add(opg);
                }
            }
        }

        // Snur på den for å finne latestStart
        prioKoe = new LinkedList<Task>();


        for(Task opg : listen) {
            opg.latestStart = this.kortesteTid;

            // Hvis ingen tasks er avhengige av gjeldende task. Legger vi den til i køen
            if(opg.numOutEdges==0) {
                opg.latestStart = this.kortesteTid - opg.time;
                prioKoe.add(opg);
            }
        }

        while(!prioKoe.isEmpty()) {
            Task gjeldendeTask = prioKoe.remove();

            // Går igjennom alle inEdgene (Predecessor tasks)
            for(Task predecessorTask : gjeldendeTask.inEdges) {

                // Hvis forrige tasks in latestStart er senere enn latestStart av den 
                // gjeldende tasken - tiden til forrige task.
                // Så er latestStart til forrige task lik dette. 
                if(predecessorTask.latestStart > (gjeldendeTask.latestStart-predecessorTask.time)) {
                    predecessorTask.latestStart = gjeldendeTask.latestStart-predecessorTask.time;
                }
                predecessorTask.numOutEdges--;

                if(predecessorTask.numOutEdges==0) {
                    prioKoe.add(predecessorTask);
                }
            }
        }

        for(Task t : listen) {
            t.slack=t.latestStart-t.earliestStart;
        }

    }

    private void tilbakeStilling() {
        this.harSykel = false;
        this.kortesteTid = 0;
        for(Task oppgave : listen) {
            oppgave.vaertBesokt = false;
            oppgave.earliestStart = 0;
            oppgave.latestStart = 0;
            oppgave.slack = 0;
            oppgave.numOutEdges = oppgave.numOutEdges1;
            oppgave.cntPredecessors = oppgave.cntPredecessorsOld;
        }
        

    }

    public void mostOptimalScheduel() {
        this.TopologicalSort();
        int curStaff = 0;

        for(int i = 0; i<= kortesteTid; i++) {

            boolean vilPrintes = false;
            
            for(Task opg : listen) {
                if(opg.earliestStart==i) {
                    if(!vilPrintes) {
                        System.out.println("------------------------------------");
                        System.out.println("Time: " + i);
                        vilPrintes = true;
                    }
                    System.out.println("        " + "Starting: " + opg.hentId()); 
                    curStaff += opg.staff;            

                } else if(opg.earliestStart+opg.time == i) {
                    if(!vilPrintes) {
                        vilPrintes = true;
                    }
                    System.out.println("        " + "Finished: " + opg.hentId()); 
                    curStaff -= opg.staff;      
                }
            }
            if(vilPrintes) {System.out.println("        " + "Current staff: " + curStaff +"\n");}
        }
        System.out.println("**** Shortest possible project execution is " + kortesteTid + " ****");
    }

    public void debug() {
        System.out.println("\n**************************\n**-- Task information --**\n**************************\n");
        for(Task t : listen) {
            System.out.println("--------");
            System.out.println("ID: " + t.hentId());
            System.out.println("Name: " + t.name);
            System.out.println("Time: " + t.time);
            System.out.println("Staff: " + t.staff);
            System.out.println("Earliest starting time: " + t.earliestStart);
            System.out.println("Latest starting time: " + t.latestStart);
            System.out.println("Slack: " + t.slack);
            System.out.println("Is this task critical: " + (t.slack== 0 ? "Yes" : "No"));
            String tempString = "";
            for (Task t2 : t.outEdges) {
                tempString += t2.id + " ";
            }
            System.out.println("Tasks that depend on this task: " + tempString + "\n");
        }

    }

    /**
     * addTask
     * @param Task t
     */
    public void addTask(Task t) {
        listen.add(t);
    }

    /**
     * hentTask()
     * @param i
     * @return Task
     */
    public Task hentTask(int i) {
        for(Task counter : listen) {
            if(counter.hentId() == i) {
                return counter;
            }
        }
        return null;
    }
    
    /**
     * printInfo()
     * param:
     * return void
     */

    public void martinDebug() {
        for(Task t:listen){
            for(Task t2: t.outEdges) {
                System.out.print(t2.id);
            }
            System.out.println("");
        }
        System.out.println("");

        for(Task t:listen){
            for(Task t2: t.inEdges) {
                System.out.print(t2.id);
            }
            System.out.println("");
        }

    }
}