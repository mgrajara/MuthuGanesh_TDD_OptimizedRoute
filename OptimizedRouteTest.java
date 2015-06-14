package Airport.Baggage.PathFinding;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

public class OptimizedRouteTest {
    
    @Before
    public void setup(){
        OptimizedRoute.reset();
    }
    @Test
    public void fromGateNullTest(){
        System.out.println("fromGateNullTest:::");
        OptimizedRoute.insertGateInfo(null, "B", 5);
        HashMap map = OptimizedRoute.getGateMap();
        if(map.size()>0)
            Assert.fail("Null value inserted to FROM gate");
        System.out.println("Null value insertion to FROM gate successfully rejected");
    }
    
    @Test
    public void toGateNullTest(){
        System.out.println("toGateNullTest:::");
        OptimizedRoute.insertGateInfo("A", null, 5);
        HashMap map = OptimizedRoute.getGateMap();
        if(map.size()>0)
            Assert.fail("Null value inserted to TO gate");
        System.out.println("Null value insertion to TO gate successfully rejected");
    }
    
    @Test
    public void gateOneInsertionTest(){
        System.out.println("gateInsertionTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        HashMap map = OptimizedRoute.getGateMap();
        Assert.assertEquals("Gate insertion count check failed", 2, map.size());
        System.out.println("Successfully inserted two gates");
        
        Gate from = (Gate)map.get("A");
        ArrayList<ConnectedTo> conGates = from.adjGates;
        Iterator<ConnectedTo> itr = conGates.iterator();
        ConnectedTo conGate = itr.next();
        Gate adj = conGate.toGate;
        Assert.assertEquals("Not the expected Name", "B", adj.name);
        System.out.println("Success - Expected connection from A to B");
        Assert.assertEquals(5, conGate.trvlTime,0);
        System.out.println("Success - Expected cost from A to B - "+conGate.trvlTime); 
        
        Gate to = (Gate)map.get("B");
        conGates = to.adjGates;
        itr = conGates.iterator();
        conGate = itr.next();
        adj = conGate.toGate;
        Assert.assertEquals("Not the expected Name", "A", adj.name);
        System.out.println("Success - Expected connection from B to A");
        Assert.assertEquals(5, conGate.trvlTime,0);
        System.out.println("Success - Expected cost from B to A - "+conGate.trvlTime);
    }
    
    @Test
    public void noGateFlightDepTest(){
        System.out.println("noGateFlightDepTest:::");
        OptimizedRoute.insertFlightDeparture("U01", "A", "MIA", "08:00");
        HashMap map = OptimizedRoute.getDepartureMap();
        Assert.assertEquals("Gate insertion count check failed", 0, map.size());
        System.out.println("Test Passed. No insertion as gate doesn't exist");
        
    }
    
    @Test
    public void wrongGateFlightDepTest(){
        System.out.println("wrongGateFlightDepTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertFlightDeparture("U01", "C", "MIA", "08:00");
        HashMap map = OptimizedRoute.getDepartureMap();
        Assert.assertEquals("Gate insertion count check failed", 0, map.size());
        System.out.println("Test Passed. No insertion as gate doesn't exist");
        
    }
    
    @Test
    public void insertOneFlightDepTest(){
        System.out.println("insertOneFlightDepTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertFlightDeparture("U01", "A", "MIA", "08:00");
        HashMap map = OptimizedRoute.getDepartureMap();
        Assert.assertEquals("Gate insertion count check failed", 1, map.size());
        System.out.println("Test Passed. Flight Id U01 successful");        
    }
    
    @Test
    public void insertSameFlightDepTest(){
        System.out.println("insertSameFlightDepTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertFlightDeparture("U01", "A", "MIA", "08:00");
        OptimizedRoute.insertFlightDeparture("U01", "B", "MIA", "08:00");
        HashMap map = OptimizedRoute.getDepartureMap();
        Assert.assertEquals("Gate insertion count check failed", 1, map.size());
        System.out.println("Test Passed. Flight Id U01 not inserted twice");        
    }
    
    @Test
    public void insertTwoFlightDepTest(){
        System.out.println("insertTwoFlightDepTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertFlightDeparture("U01", "A", "MIA", "08:00");
        OptimizedRoute.insertFlightDeparture("U02", "B", "MIA", "08:00");
        HashMap map = OptimizedRoute.getDepartureMap();
        Assert.assertEquals("Gate insertion count check failed", 2, map.size());
        System.out.println("Test Passed. Flight Id U01 & U02 inserted");        
    }
    
    @Test
    public void noGateBagTransferTest(){
        System.out.println("noGateBagTransferTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertFlightDeparture("U01", "B", "MIA", "08:00");
        OptimizedRoute.insertBagList("0001", "C", "U01");
        HashMap map = OptimizedRoute.getBagListMap();
        Assert.assertEquals("Bag List insertion count check failed", 0, map.size());
        System.out.println("Test Passed. Bag List not inserted as Gate C doesn't exist");        
    }
    
    @Test
    public void noFlightBagTransferTest(){
        System.out.println("noGateBagTransferTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertFlightDeparture("U01", "B", "MIA", "08:00");
        OptimizedRoute.insertBagList("0001", "A", "U02");
        HashMap map = OptimizedRoute.getBagListMap();
        Assert.assertEquals("Bag List insertion count check failed", 0, map.size());
        System.out.println("Test Passed. Bag List not inserted as Flight with Id U02 doesn't exist");        
    }
    
    @Test
    public void insertOneBagListTest(){
        System.out.println("insertOneBagListTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertFlightDeparture("U01", "B", "MIA", "08:00");
        OptimizedRoute.insertBagList("0001", "A", "U01");
        HashMap map = OptimizedRoute.getBagListMap();
        Assert.assertEquals("Bag List insertion count check failed", 1, map.size());
        System.out.println("Test Passed. Bag List inserted successfully");        
    }
    
    @Test
    public void insertThreeOneFailBagListTest(){
        System.out.println("insertThreeOneFailBagListTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertFlightDeparture("U01", "B", "MIA", "08:00");
        OptimizedRoute.insertBagList("0001", "A", "U01");
        OptimizedRoute.insertBagList("0002", "C", "U01");
        OptimizedRoute.insertBagList("0002", "B", "U01");
        HashMap map = OptimizedRoute.getBagListMap();
        Assert.assertEquals("Bag List insertion count check failed", 2, map.size());
        System.out.println("Test Passed. Bag Lists inserted successfully");        
    }  

    @Test
    public void finddirectPathTest(){
        System.out.println("finddirectPathTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertFlightDeparture("U01", "B", "MIA", "08:00");
        OptimizedRoute.insertBagList("0001", "A", "U01");
        HashMap pathMap = OptimizedRoute.computePaths();
        Assert.assertEquals("Wrong path", "A B : 5.0", pathMap.get("AB"));
        System.out.println("Test Passed. Computed path is as expected");        
    } 

    @Test
    public void finddirectRevPathTest(){
        System.out.println("finddirectRevPathTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertFlightDeparture("U01", "A", "MIA", "08:00");
        OptimizedRoute.insertBagList("0001", "B", "U01");
        HashMap pathMap = OptimizedRoute.computePaths();
        Assert.assertEquals("Wrong path", "B A : 5.0", pathMap.get("BA"));
        System.out.println("Test Passed. Computed path is as expected");        
    } 

    @Test
    public void noPathTest(){
        System.out.println("noPathTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertGateInfo("C", "D", 2);
        OptimizedRoute.insertFlightDeparture("U01", "A", "MIA", "08:00");
        OptimizedRoute.insertBagList("0001", "C", "U01");
        HashMap pathMap = OptimizedRoute.computePaths();
        Assert.assertEquals("Wrong path", "No Path", pathMap.get("CA"));
        System.out.println("Test Passed. No Path as expected");        
    } 

    @Test
    public void simplePathTest(){
        System.out.println("simplePathTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertGateInfo("B", "D", 2);
        OptimizedRoute.insertFlightDeparture("U01", "D", "MIA", "08:00");
        OptimizedRoute.insertBagList("0001", "A", "U01");
        HashMap pathMap = OptimizedRoute.computePaths();
        Assert.assertEquals("Wrong path", "A B D : 7.0", pathMap.get("AD"));
        System.out.println("Test Passed. Computed path is as expected");        
    } 

    @Test
    public void simplePathRevTest(){
        System.out.println("simplePathRevTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertGateInfo("B", "D", 2);
        OptimizedRoute.insertFlightDeparture("U01", "A", "MIA", "08:00");
        OptimizedRoute.insertBagList("0001", "D", "U01");
        HashMap pathMap = OptimizedRoute.computePaths();
        Assert.assertEquals("Wrong path", "D B A : 7.0", pathMap.get("DA"));
        System.out.println("Test Passed. Computed path is as expected");        
    }  

    @Test
    public void simplePathARRIVALTest(){
        System.out.println("simplePathARRIVALTest:::");
        OptimizedRoute.insertGateInfo("A", "B", 5);
        OptimizedRoute.insertGateInfo("B", "BaggageClaim", 2);
        OptimizedRoute.insertFlightDeparture("U01", "A", "MIA", "08:00");
        OptimizedRoute.insertBagList("0001", "A", "ARRIVAL");
        HashMap pathMap = OptimizedRoute.computePaths();
        Assert.assertEquals("Wrong path", "A B BaggageClaim : 7.0", pathMap.get("ABaggageClaim"));
        System.out.println("Test Passed. Computed path is as expected");        
    }

    @Test
    public void complexPathTest(){
        System.out.println("complexPathTest:::");
        OptimizedRoute.insertGateInfo("Concourse_A_Ticketing", "A5", 5);
        OptimizedRoute.insertGateInfo("A5", "BaggageClaim", 5);
        OptimizedRoute.insertGateInfo("A5", "A10", 4);
        OptimizedRoute.insertGateInfo("A5", "A1", 6);
        OptimizedRoute.insertGateInfo("A1", "A2", 1);
        OptimizedRoute.insertGateInfo("A2", "A3", 1);
        OptimizedRoute.insertGateInfo("A3", "A4", 1);
        OptimizedRoute.insertGateInfo("A10", "A9", 1);
        OptimizedRoute.insertGateInfo("A9", "A8", 1);
        OptimizedRoute.insertGateInfo("A8", "A7", 1);
        OptimizedRoute.insertGateInfo("A7", "A6", 1);
        OptimizedRoute.insertFlightDeparture("UA10", "A1", "MIA", "08:00");
        OptimizedRoute.insertFlightDeparture("UA11", "A1", "LAX", "09:00");
        OptimizedRoute.insertFlightDeparture("UA12", "A1", "JFK", "09:45");
        OptimizedRoute.insertFlightDeparture("UA13", "A2", "JFK", "08:30");
        OptimizedRoute.insertFlightDeparture("UA14", "A2", "JFK", "09:45");
        OptimizedRoute.insertFlightDeparture("UA15", "A2", "JFK", "10:00");
        OptimizedRoute.insertFlightDeparture("UA16", "A3", "JFK", "09:00");
        OptimizedRoute.insertFlightDeparture("UA17", "A4", "MHT", "09:15");
        OptimizedRoute.insertFlightDeparture("UA18", "A5", "LAX", "10:15");
        OptimizedRoute.insertBagList("0001", "Concourse_A_Ticketing", "UA12");
        OptimizedRoute.insertBagList("0002", "A5", "UA17");
        OptimizedRoute.insertBagList("0003", "A2", "UA10");
        OptimizedRoute.insertBagList("0004", "A8", "UA18");
        OptimizedRoute.insertBagList("0005", "A7", "ARRIVAL");
        HashMap pathMap = OptimizedRoute.computePaths();
        Assert.assertEquals("Wrong path", "Concourse_A_Ticketing A5 A1 : 11.0", pathMap.get("Concourse_A_TicketingA1"));
        Assert.assertEquals("Wrong path", "A5 A1 A2 A3 A4 : 9.0", pathMap.get("A5A4"));
        Assert.assertEquals("Wrong path", "A2 A1 : 1.0", pathMap.get("A2A1"));
        Assert.assertEquals("Wrong path", "A8 A9 A10 A5 : 6.0", pathMap.get("A8A5"));
        Assert.assertEquals("Wrong path", "A7 A8 A9 A10 A5 BaggageClaim : 12.0", pathMap.get("A7BaggageClaim"));
        System.out.println("Test Passed. Computed paths were as expected");        
    }
}
