public class Avion extends Maquina
{
    // instance variables - replace the example below with your own
    private boolean enAire = false;
    private Marino piloto;

    /**
     * Constructor for objects of class Avion
     */
    public Avion()    
    {        
    }
    
    @Override
    public boolean seraDestruida(int longitud, int latitud){
        return super.seraDestruido(longitud, latitud) && !enAire;
    }
    
    
    public boolean getEnAire()
    {
        // put your code here
        return enAire;
    }
    
    // third method - first part
    public boolean isWeak(){
        if (piloto != null){
            return true;
        }
        
        return false;
    }
    
    
    
    
}
