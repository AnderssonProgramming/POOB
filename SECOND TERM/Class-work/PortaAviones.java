import java.util.*;

public class PortaAviones extends Barco
{
    private ArrayList<Avion> aviones;
    
    public PortaAviones()
    {
    }
    
    
    // third method - first part
    @Override
    public boolean isWeak()
    {   
        boolean weakPlane = false;
        for (Avion plane : aviones){
            weakPlane = plane.isWeak();
        }
        
        return (super.isWeak() || weakPlane);
    }
}
