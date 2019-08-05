//WILLIAM SANDS 31437387
//wsands@u.rochester.edu

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.geom.Line2D;
import java.util.*;
//Creates Main Jframe

public class MapDrawer extends JFrame{
    private static Graph mapGraph;
    public static String mapName;
    public static DrawPane d1;
    //Constructor
    public MapDrawer(Graph g1, String n){
        this.mapGraph=g1;
        this.mapName=n;
        this.d1=d1=new DrawPane(g1);
    }
    //Allows exiting call
    public void exitMap(){
        dispose();
        System.exit(1);
    }

    //Draws the map
    public static void drawMap(){
        JFrame frame= new JFrame();	
        //Handels nice titling of the map window
        if(mapName.toLowerCase().equals("ur.txt")){
            frame.setTitle("Map of the University of Rochester");
        }
        else if(mapName.toLowerCase().equals("monroe.txt")){
            frame.setTitle("Map of Monroe County");
        }
        else if(mapName.toLowerCase().equals("nys.txt")){
            frame.setTitle("Map of the State of New York");
        }

        //Allows for other maps, just uses the text file as the map name
        else{
            frame.setTitle("Map of "+ mapName);
        }

        frame.setContentPane(d1);
        frame.setSize(800, 700);
        d1.setLocation(0,0);

        //Implaments Component Listener to see if window was resized
        ComponentListener listener = new ComponentAdapter() {
            public void componentShown(ComponentEvent evt) {
            }
      
            public void componentHidden(ComponentEvent evt) {
            }
      
            public void componentMoved(ComponentEvent evt) {
            }
            //Allows Dynamic window resize
            public void componentResized(ComponentEvent evt) {
                int height = frame.getHeight();
                int width = frame.getWidth();
                d1.updateSize(width, height);
                d1.setSize(width, height);
                d1.repaint();
            }
          };
        frame.getContentPane().addComponentListener(listener);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    //Passes the path to the DrawPane and calles the repaint
    public static void drawPath(ArrayList<Graph.Vertex> l1){
        d1.updateVertexList(l1);
        d1.repaint();
    }
    //Used to do the actuall drawing of lines for the roads
    static class DrawPane extends JPanel {
        private ArrayList<MappingObject> coordList;
        private Graph map;
        private double maxX=0;
        private double minX=0;
        private double maxY=0;
        private double minY=0;
        private double frameSizeWidth=0;
        private double frameSizeHeight=0;
        private HashMap<String, MappingObject> vertexCoordStorage = new HashMap<String, MappingObject>();
        private ArrayList<Graph.Edge> edgeList = new ArrayList<Graph.Edge>();
        private ArrayList<Graph.Vertex> vertexList = new ArrayList<Graph.Vertex>();
        

        public DrawPane(Graph g1){
            //Sets up map, and gets max and min values for scaling purposes
            this.map=g1;
            this.edgeList=g1.getAllEdges();
            this.coordList=map.getAllCoord();
            //Inits MaxandMin values to the first value in the list 
            this.maxX=Math.abs(coordList.get(0).getLat());
            this.minX=Math.abs(coordList.get(0).getLat());
            this.maxY=Math.abs(coordList.get(0).getLon());
            this.minY=Math.abs(coordList.get(1).getLon());
            //Finds the largest/smallest values for map scaling
            for(MappingObject d:this.coordList){
                if(this.minX>Math.abs(d.getLat())){
                    this.minX=Math.abs(d.getLat());
                }
                if(this.minY>Math.abs(d.getLon())){
                    this.minY=Math.abs(d.getLon());
                }
                if(this.maxX<Math.abs(d.getLat())){
                    this.maxX=Math.abs(d.getLat());
                }
                if(this.maxY<Math.abs(d.getLon())){
                    this.maxY=Math.abs(d.getLon());
                }
                
            }
        }
        //Updates current window size
        public void updateSize(double x, double y){
            this.frameSizeWidth=x;
            this.frameSizeHeight=y-30;
        }

        // from https://stackoverflow.com/questions/5294955/how-to-scale-down-a-range-of-numbers-with-a-known-min-and-max-value
        //Used for scaling all values so that they fit within the JFrame window
        public static double scale(final double valueIn, final double baseMin, final double baseMax, final double limitMin, final double limitMax) {
            return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
        }

        //Scales values for the current window size
        public void recalcValues(){
            vertexCoordStorage = new HashMap<String, MappingObject>();
            for(MappingObject d:coordList){
                d.setY((int)this.frameSizeHeight-(int)Math.abs(scale(Math.abs(d.getLat()),this.minX, this.maxX, 1, this.frameSizeHeight)));
                d.setX((int)this.frameSizeWidth-(int)Math.abs(scale(Math.abs(d.getLon()),this.minY, this.maxY, 1, this.frameSizeWidth)));
                vertexCoordStorage.put(d.getID(), d);
            }
        }
        //Recieves the path list 
        public void updateVertexList(ArrayList<Graph.Vertex> v1){
            this.vertexList=v1;
        }

        //Draws Map by first drawing roads, then drawing the highlitghed path
        public void paintComponent(Graphics g) {
            recalcValues();
            removeAll();
            g.setColor(Color.black);
            //Draws all roads
            for (Graph.Edge e : edgeList) {
                MappingObject start= vertexCoordStorage.get(e.getStartID());
                MappingObject end= vertexCoordStorage.get(e.getEndID());
                g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
            }
            //Changes color to red and updates the map with the shortest path shown in red
            g.setColor(Color.red);
            for(Graph.Vertex v:this.vertexList){
                if(v.getPrevious()!=null){
                    MappingObject start= vertexCoordStorage.get(v.getID());
                    MappingObject end= vertexCoordStorage.get(v.getPrevious().getID());
                    g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
                    
                }
            }
            //resets the color for map redraw if the window is resized again
            g.setColor(Color.black);
            
        }
   }

}