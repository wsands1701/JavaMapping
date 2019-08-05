//William Sands 31437387
//wsands@u.rochester.edu

//used to store information about vertex for quick map drawing access
//Stores ID, along with lattitude and longitude, and the respective x-y coordinate for the Jframe
public class MappingObject{
    private int x;
    private int y;
    double lat;
    double lon;
    private String id;
    public MappingObject(int xin, int yin, String idin){
        this.id=idin;
        this.x=xin;
        this.y=yin;
    }
    public MappingObject(double latt, double lonn, String idin){
        this.lat=latt;
        this.lon=lonn;
        this.id=idin;
    }
    public void setX(int i){
        this.x=i;
    }
    public void setY(int i){
        this.y=i;
    }
    public double getLat(){
        return this.lat;
    }
    public double getLon(){
        return this.lon;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public String getID(){
        return this.id;
    }
}