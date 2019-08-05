// Based on Graph.java from Computer Science S-111, Harvard University
// HEAVILY MODIFIED by WILLIAM SANDS

import java.awt.Canvas;
import java.awt.Graphics;
import java.io.*;
import java.util.*;

public class Graph {
    //Used to store list of all edges, imporant for procedurlaly drawing the map
    private ArrayList<Edge> allEdges = new ArrayList<Edge>();
    
   //Vertex object
    public class Vertex implements Comparable<Vertex>{
        private String id;
        private Edge edges;           //List of all edges connect to this vertex
        private Vertex next;
        private Vertex previous;  
        private boolean done;      
        private double cost;          // cost of shortest known path
        private double longitude;     //Longitudal location of vertex
        private double lattitude;     //Lattidinal locatiion of vertex
        
        public Vertex(String id, double lo, double la) {
            this.id = id;
            this.longitude=lo;
            this.lattitude=la;
            this.done=false;
            cost = Double.POSITIVE_INFINITY;
        }
        
        //Resets Vertex values, and sets cost to infinity  
        private void reinit() {
            previous = null;
            done=false;
            cost = Double.POSITIVE_INFINITY;
        }
        //Returns previous vertex
        public Vertex getPrevious(){
            return previous;
        }
        //Tracks edges of the vertex
        private void addToAdjacencyList(Edge e) {
            e.next = edges;
            edges = e;
        }
        private double getLongitude(){
            return this.longitude;
        }
        private double getLattitude(){
            return this.lattitude;
        }
        public String getID(){
            return this.id;
        }
        public MappingObject getCoordinates(){
            return new MappingObject(this.lattitude, this.longitude, this.id);
        }
        //Used to insert into priority que
        public int compareTo(Vertex v){
            if(this.cost<v.cost){
                return -1;
            }
            else if(this.cost==v.cost){
                return 0;
            }
            return 1;
        }
        public double getCost(){
            return this.cost;
        }
        
       //Used for troubleshooting, to check where each vertex leads 
        public String toString() {
            String str = "vertex " + id + ":\n";
            Edge e = edges;
            while (e != null) {
                str += "\tedge to " + e.end.id;
                str += " (cost = " + e.cost + ")\n"; 
                e = e.next;
            }
            return str;
        }
    }
    //Edge Object
    public class Edge {
        private Vertex start;
        private Vertex end;
        private String edgeName;
        private double cost;
        private Edge next;           
 
        public Edge(Vertex start, Vertex end, String id, double cost) {
            this.start = start;
            this.end = end;
            this.edgeName=id;
            this.cost=cost;
        }
        public Vertex getStart(){
            return this.start;
        }
        public Vertex getEnd(){
            return this.end;
        }
        public String getStartID(){
            return this.start.getID();
        }
        public String getEndID(){
            return this.end.getID();
        }
    }
    //Used to reverse an edge so that all edges are non-directed
    private Edge reversal(Edge e){
        return new Edge(e.end,e.start,e.edgeName,e.cost);
    }
    
    
    
    //Linked list of verticies in graph, used to occasionally interate through the verticies
    private Vertex vertices;

    //Used to store all verticies for quick lookup
    private HashMap<String, Vertex> vertexHashMap = new HashMap<String,Vertex>();
    
   //Resets Verticies for shortestPath
    private void reinitVertices() {
        Vertex v = vertices;
        while (v != null) {
            v.reinit();
            v = v.next;
        }
    }
    //getVertex uses the vertex hashmap to to return selected vertex for addition of edges.
    public Vertex getVertex(String id) {
        return vertexHashMap.get(id);
    }
    
    
   //Adds a spesified vertex to the linked list and to the hashmap
    public Vertex addVertex(String id, double lo, double la) {
        Vertex v = new Vertex(id, lo, la);
        vertexHashMap.put(v.getID(), v);
        /* Add to the front of the list. */
        v.next = vertices;
        vertices = v;
        return v;
    }
    //Looks up the two vertex from the hashMap and ads the edge to both of these vertexes edge adjacyncy sets
    public void addEdge(String startVertexID, String endVertexID,String id) {
        Vertex start = getVertex(startVertexID);
        Vertex end = getVertex(endVertexID);
        
        Edge e = new Edge(start, end, id, findDistance(start, end) );
        allEdges.add(e);
        start.addToAdjacencyList(e);
        end.addToAdjacencyList(reversal(e));
    }
    //Returns a list of all edges
    public ArrayList<Edge> getAllEdges(){
        return this.allEdges;
    }
    //Prints out the road taken to travel between two vertex, used for generating the directions list
    public String directionHelper(Vertex v1, Vertex v2){
        Edge e = v1.edges;
        while(e!=null){
            if(e.end.equals(v2)){
                return "Take "+ e.edgeName+ " from "+ v1.getID() +" to "+ v2.getID();
            }
            e=e.next;
        } 
    return "";   
    }
    //Takes two vertex and finds distance between them
    public double findDistance(Vertex v1, Vertex v2){
        double longitude1 = v1.getLongitude();
        double lattitude1= v1.getLattitude();
        double longitude2= v2.getLongitude();
        double lattitude2= v2.getLattitude();
        return haversine(lattitude1, longitude1, lattitude2 , longitude2);
    }

    //FROM https://rosettacode.org/wiki/Haversine_formula#Java
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6372.8; // In kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

    //Allows exporting of coordinates for map drawing 
    public ArrayList<MappingObject> getAllCoord(){
        ArrayList<MappingObject>listTemp = new ArrayList<MappingObject>();
        Vertex v = vertices;
        while (v != null) {
            listTemp.add(v.getCoordinates());
            v = v.next;
        }
        return listTemp;
    }
    //Used for troubleshooting 
    public String toString() {
        String str = "";
        
        Vertex v = vertices;
        while (v != null) {
            str += v;
            v = v.next;
        }
        
        return str;
    }
    //Tracks the current shortest path between two nodes
    ArrayList<Vertex> pathList = new ArrayList<Vertex>();

    //Origonal method based on Dijkstra's Shortes Path Algorythm to find the shortest path bewteen two given Vertex
    //ends once the desired end vertex has been marked as done
    public ArrayList<Vertex> shortestPath(String start, String end) {
        reinitVertices();
        Vertex beginning = getVertex(start);
        Vertex ending = getVertex(end);
        beginning.cost = 0;
        PriorityQueue<Vertex> pq= new PriorityQueue<Vertex>();
        Vertex v = vertices;
        while (v != null) {
            pq.add(v);
            v = v.next;
        }
        while(!pq.isEmpty()&& !ending.done){
            Vertex u = pq.poll();
            u.done=true;
            Edge e = u.edges;
            while (e != null) {
                if(!e.end.done){
                    pq.remove(e.end);
                    double tempCost = u.cost+e.cost;
                    if(tempCost<e.end.cost){
                        e.end.cost=tempCost;
                        e.end.previous=u;
                    }
                    pq.add(e.end);
                }
                e = e.next;
            }
        }
        Vertex j = ending;
        if(ending.cost!=Double.POSITIVE_INFINITY){
            while(j!=null){
                pathList.add(j);
                j=j.previous;
            }
        }
        return pathList;
    } 
}