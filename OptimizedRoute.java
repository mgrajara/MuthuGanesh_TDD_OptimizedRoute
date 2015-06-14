package Airport.Baggage.PathFinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;


public class OptimizedRoute {
    
    static HashMap<String,Gate> gateMap;        
    static HashMap<String,Departures> depMap;        
    static HashMap<String, Bags> bagMap;
    
    public static HashMap computePaths(){
        HashMap returnMap = new HashMap();
        String destFId, target;
        Departures dest;
        Set<String> keys = bagMap.keySet();
        for(String bagKey : keys){
            Bags bag = bagMap.get(bagKey);
            Gate source = gateMap.get(bag.entry);
            if(source == null){
                System.out.println("No route defined from Entry gate " + bag.entry);
            }else{
                destFId = bag.flightId;
                dest = depMap.get(destFId);
                if ("ARRIVAL".equals(destFId)) {
                    target = "BaggageClaim";
                }else if(dest == null){
                    System.out.println("Flight Number doesn't exist");
                    continue;
                }else {
                    target = dest.gate;
                }
                findShortPath(gateMap.get(bag.entry));
                System.out.print(bag.bagNo+" ");
                String path = printPath(gateMap.get(target));
                if(!"No Path".equals(path)){
                    path+=" : "+ (gateMap.get(target)).minTravel;
                }
                returnMap.put(source.name+target, path);
                System.out.println(returnMap.get(source.name+target));

                // Reset the values to re-calculate for other shortest path
                Set<String> keySet = gateMap.keySet();
                for (String key : keySet) {
                    Gate g = gateMap.get(key);
                    g.reset();
                }
            }  
        }
        return returnMap;
    }
    
    private static void findShortPath(Gate gate){
        gate.minTravel=0;
        PriorityQueue<Gate> queue = new PriorityQueue<Gate>();
        queue.add(gate);
        
        while(!queue.isEmpty()){
            Gate curGate = queue.poll();
            double myTravel = curGate.minTravel;
            ArrayList<ConnectedTo> conGates = curGate.adjGates;
            Iterator<ConnectedTo> itr = conGates.iterator();
            while(itr.hasNext()){
                ConnectedTo conGate = itr.next();
                Gate adj = conGate.toGate;
                if(adj.minTravel == -1 || adj.minTravel > (myTravel+conGate.trvlTime)){
                    queue.remove(adj);
                    adj.minTravel = (myTravel+conGate.trvlTime);
                    adj.prevGate = curGate;
                    queue.add(adj);
                }
            }
        }        
    }
    
    public static String printPath(Gate dest){       

        if(dest.minTravel==-1){
            System.out.println("No Route to Gate "+dest.name);
            return "No Path";
        }
        
        List<Gate> revPath = new ArrayList<Gate>();
        String path=null;
        for(Gate g = dest; g!=null; g = g.prevGate){
            revPath.add(g);            
        }
        Collections.reverse(revPath);
        
        for(Gate g : revPath){
            //System.out.print(" "+g.name);
            if(path==null){
                path=g.name;
            }else{
                path+=" "+g.name;
            }
        }
        return path;        
    }
    
    public static void insertGateInfo(String from, String to, double cost){
        Gate f,t;
        if(gateMap==null){
            gateMap = new HashMap<String,Gate>();
        }
        
        if(from==null || to==null){
            System.out.println("FROM/TO Gate cannont be null");
            return;
        }

        f=gateMap.get(from);
        t=gateMap.get(to);
        if(f == null){
            f=new Gate(from);
            gateMap.put(from,f);
        }            
        if(t == null){
            t=new Gate(to);
            gateMap.put(to,t);
        }
        // BiDirectional graph - Both adjacent to each other with the same cost
        f.addAdjGate(t, cost);
        t.addAdjGate(f, cost);
    }
    
    public static HashMap<String,Gate> getGateMap(){
        return gateMap;
    }
    
    public static void insertFlightDeparture(String fId, String gate, String destination, String time){
        Departures depFlight;
        
        Gate g = null;
        if(depMap==null){
            depMap  = new HashMap<String,Departures>();
        }
        depFlight = depMap.get(fId);
        if(gateMap!=null) g = gateMap.get(gate);
        if(g == null){
            System.out.println("Please specify a valid gate");
            return;
        }
        if(depFlight != null){
            System.out.println("Flight id already exist at the same time. Enter unique flight id");
        }else{
            depFlight = new Departures(fId,gate,destination,time);
            depMap.put(fId, depFlight);
        }
    }
    
    public static HashMap<String, Departures> getDepartureMap(){
        return depMap;
    }
    
    public static void insertBagList(String bagNo, String entry, String flightId){
        
        if(gateMap==null){
            System.out.println("Invalid data. Gate not found");
            return;
        }
        if(depMap==null){
            System.out.println("Invalid data. Flight not found");
            return;
        }
        if(bagMap==null){
            bagMap = new HashMap<String, Bags>();
        }

        Bags bag = bagMap.get(bagNo);
        Gate gate = gateMap.get(entry);
        Gate arrGate=null;
        Departures depFlight=null;
        if("ARRIVAL".equals(flightId)){
            arrGate=gateMap.get("BaggageClaim");
        }else{
            depFlight=depMap.get(flightId);
        }
        if(bag != null){
            System.out.println("Duplicate Bag number");
            return;
        }
        if(gate==null){
            System.out.println("Invalid data. Gate "+entry+" doesn't exist");
            return;
        }
        if(("ARRIVAL".equals(flightId) && arrGate == null) || (!"ARRIVAL".equals(flightId) && depFlight==null)){
            System.out.println("Invalid data. Flight with ID "+flightId+" doesn't exist");
            return;
        }
        bag = new Bags(bagNo, entry, flightId);
        bagMap.put(bagNo, bag);
    }
    
    public static HashMap<String, Bags> getBagListMap(){
        return bagMap;
    }
    
    public static void reset(){
        gateMap = null;
        depMap = null;
        bagMap = null;
    }
    
    public static void main(String args[]){
        
        Scanner in = new Scanner(System.in);
        String readLine = null;        

        System.out.println("Enter Conveyor System route with cost in <FROM> <TO> <COST> format. Enter with empty line to terminate input");
        String from, to;
        double cost=0;
        while (!(readLine = in.nextLine()).isEmpty()){
            String[] values = readLine.split("\\s+");
            if(values.length != 3){
                System.out.println("Invalid input. Try again");
            }else{
                from = values[0];
                to = values[1];
                try{
                    cost = Double.parseDouble(values[2]);
                }catch(NumberFormatException e){
                    System.out.println("Please enter a valid number as <COST>");
                    continue;
                }catch(Exception e){
                    System.out.println("Exception converting <COST> to double");
                    continue;
                }                
                insertGateInfo(from,to,cost);
            }
        }
        
        System.out.println("Enter details of the flight departure in <FLIGHTID> <GATE> <DESTINATION> <TIME> format. Enter with empty line to terminate input");
        String fId, gate, destination, time;
        while (!(readLine = in.nextLine()).isEmpty()){
            String[] values = readLine.split("\\s+");
            if(values.length != 4){
                System.out.println("Invalid input. Try again");
            }else{
                fId = values[0];
                gate = values[1];
                destination = values[2];
                time = values[3];
                insertFlightDeparture(fId,gate,destination,time);
            }
        }
        
        System.out.println("Enter Backage information to find the shortest path - <BAG_NO> <ENTRY> <FLIGHT_ID> format. Enter with empty line to terminate input");
        
        while (!(readLine = in.nextLine()).isEmpty()){
            String[] values = readLine.split("\\s+");
            if(values.length != 3){
                System.out.println("Invalid input. Try again");
            }else{
                String bagNo = values[0], entry = values[1], flightId = values[2];
                insertBagList(bagNo, entry, flightId);
            }
        }
        
        System.out.println("OUTPUT:");        
        computePaths();
              
    }
}

class Gate implements Comparable<Gate>{
    public Gate(String name) {
        this.name = name;
    }
    String name;
    ArrayList<ConnectedTo> adjGates;
    double minTravel = -1;
    Gate prevGate;
    
    public void addAdjGate(Gate adjGate,double time){
        if(adjGates == null){
            adjGates = new ArrayList<ConnectedTo>();
        }
        if(adjGates.contains(adjGate)){
            System.out.println("Gates already adjacent");
            return;
        }
        adjGates.add(new ConnectedTo(adjGate, time));        
    }
    
    public void reset(){
        minTravel = -1;
        prevGate = null;
    }
    
    public int compareTo(Gate gate){
        return Double.compare(minTravel, gate.minTravel);
        //return 0;
    }
}
    
class ConnectedTo {
    public ConnectedTo(Gate adjGate, double time){
        toGate = adjGate;
        trvlTime = time;
    }
    Gate toGate;
    double trvlTime;
}

class Departures{
    
    public Departures(String id, String gate, String dest, String time){
        this.flightId = id;
        this.gate = gate;
        this.destination = dest;
        this.time = time;
    }
    
    String flightId;
    String gate;
    String destination;
    String time;
}

class Bags{
    
    public Bags(String bNo, String ent, String fId){
        bagNo = bNo;
        entry = ent;
        flightId = fId;
    }    
    String bagNo, entry, flightId;
    
}
