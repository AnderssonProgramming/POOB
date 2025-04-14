
/**
 * Write a description of class maquina here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Maquina
{
    // instance variables - replace the example below with your own
    private int x;
    private Ubicacion ubicacion;
    private boolean attack = true;

    /**
     * Constructor for objects of class maquina
     */
    public Maquina()
    {
        // initialise instance variables
    }

    // First method - first part
    public void avance(int dLon, int dLat)
    {
        dLon = dLonSum;
        dLat = dLatSum;        
    }
    
    // implementar get ubicacion que llamen a los lon y lat de ubicacion
    public int getLon(){
        return ubicacion.getLon;
    }
    
    public int getLat(){
        return ubicacion.getLon;
    }
    
    public boolean seraDestruido(int longitud, int latitud){
        int machineLon = this.getLon();
        int machineLat = this.getLat();
        
        if (longitud == machineLon && latitud == machineLat){
            return true;
        }
        
        return false;                
    }
    
    // third method - first part
    public boolean isWeak(){
        return value = this.isWeak();
    }
    
    //fourth method --first part
    public boolean attack(int longitud, int latitud,boolean attack){
        if (attack){
            int machineLon = this.getLon();
            int machineLat = this.getLat();
            if (longitud == machineLon && latitud == machineLat){
                return true;
            } 
            
        }
        return false;
    }
    
}
