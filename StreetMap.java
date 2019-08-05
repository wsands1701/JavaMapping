//William Sands - 31437387
//wsands@u.rochester.edu

import java.util.Arrays;
import java.util.Timer;
import java.util.ArrayList;
import java.io.*;

public class StreetMap{

    public static void main(String[] args){
        //Read in input file line by line, and adds intersextions as vertex and adds roads as edges
        String inputFile = args[0];
        BufferedReader reader;
        Graph g1= new Graph();
        System.out.println("Building Graph. Please Wait");
        double start=System.currentTimeMillis();
		try {
            reader = new BufferedReader(new FileReader(inputFile));
            String line = reader.readLine();
			while (line != null) {
                //Splits line for reading
                String[] lineArr = line.split("\t");
                
                //Handles Intersection
                if(lineArr[0].equals("i")){
                    g1.addVertex(lineArr[1], Double.parseDouble(lineArr[3]),  Double.parseDouble(lineArr[2]));
                }

                //Handles roads
                else{
                    g1.addEdge(lineArr[2], lineArr[3], lineArr[1]);
                }
				line = reader.readLine();
			}
            reader.close();
        }
        catch (IOException e) {
            System.out.println("Error reading file. Please try again.");
        }

        //Tracks runtime of graph building 
        double stop = System.currentTimeMillis();
        //Displays for user the time taken to generate the graph
        System.out.println("Graph of "+ inputFile+ " built in "+ (stop-start)+ " ms.");

        //Inits the arguments for commandline argument input
        String desiredStart = "";
        String desiredStop = "";
        String argTaker = "";
        boolean shown=false;

        //inits the mapList ArrayList which will store the path, in reverse from one vertex to annother
        ArrayList<Graph.Vertex> mapList = new ArrayList<Graph.Vertex>();
        for(String s: args){
            argTaker = argTaker+" "+ s;
        }

        //Allows for parsing of --show and --directions
        String[] splitted = argTaker.split("--");
        MapDrawer m1 = new MapDrawer(g1, inputFile);
        for(String s:splitted){
            if(s.contains("directions")){
                String[] dirs = s.split(" ");
                desiredStart=dirs[1];
                desiredStop=dirs[2];
                System.out.println("Generating directions from "+ desiredStart+" to "+desiredStop);
                double startRoute = System.currentTimeMillis();
                mapList = g1.shortestPath(desiredStart,desiredStop);
                double stopRoute = System.currentTimeMillis();
                double elapsedTimeRoute = stopRoute-startRoute;
                String outputFileName = "Directions_"+desiredStart+"_"+desiredStop+".txt";
                m1.drawPath(mapList);
                //If no directions are found, notify the user, since not all intersecitos are connected
                if(mapList.size()==0){
                    System.out.println("No directions found between these two points");
                }
                //If path found, call the direction helper class to print out the path between the vertecis of the shortest route
                else {
                    try{
                    File fout = new File(outputFileName);
	                FileOutputStream fos = new FileOutputStream(fout);
	                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                    for(int j=mapList.size()-1;j>0;j--){
                    bw.write(g1.directionHelper(mapList.get(j), mapList.get(j-1)));
                    bw.newLine();
                    System.out.println(mapList.get(j).getID());
                    }
                    System.out.println(desiredStop);
                    bw.close();
                }
                //if file write fails, just write directions to console
                catch(IOException e){
                    for(int j=mapList.size()-1;j>0;j--){
                        System.out.println(mapList.get(j).getID());
                    }
                    System.out.println(desiredStop);
                }
                } 
                //displays for the user how long pathfinding took
                System.out.println("PathFinding took "+ elapsedTimeRoute+" ms.");
                //Prints out direction file name
                System.out.println("Directions stored in "+outputFileName);
                //Prints out (in the case that a path is found) the total distance traveled between the two vertex
                if(g1.getVertex(desiredStop).getCost()!=Double.POSITIVE_INFINITY){
                    System.out.println("Total distance traveled is "+ g1.getVertex(desiredStop).getCost()*0.621371+" mi."); 
                }
            }
            //If instructed show the map
            if(s.contains("show")){
                shown=true;
            }


        }
        //Just to allow map showing to be done in any order in the command line arguemnts
        //Since m1 must be initied to be able to pass the direcion list, if it is not asked to be shown, imidiatly kill the JFrame
        if(shown){
            m1.drawPath(mapList);
            m1.drawMap();
        }
        else{
            m1.exitMap();
        }
    }
}