import javax.swing.JOptionPane;
import java.util.ArrayList;

/**
 * Write a description of class Flota here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Flota
{
    // instance variables - replace the example below with your own
//    private int x;
    private ArrayList<Maquina> maquinas;
    private ArrayList<Maquina> maquinasSeranDestruidas;
    private ArrayList<Maquina> maquinasDebiles;

    /**
     * Constructor for objects of class Flota
     */
    public Flota()
    {
        // initialise instance variables
    }

    public void avance(int dLon, int dLat)
    {
        if (dLon >= 0 && dLon <= 180 && dLat >= -90 && dLat <= 90){
            for(Maquina machine : maquinas){
            dLonSum = dLon + machine.getdLon();
            dLatSum = dLat + machine.getdLat();
                if (dLonSum >= 0 && dLonSum <= 180 && dLatSum >= -90 && dLatSum <= 90){               
                    machine.avance(dLonSum,dLatSum);
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"The position is out the world","Error",JOptionPane.ERROR_MESSAGE);
                }
    
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"The position is out the world","Error",JOptionPane.ERROR_MESSAGE);
    }
    }
    
    
    
    // Second method  -  first part
    
    public ArrayList<Maquina> seranDestruidos(int longitud, int latitud){
        if (longitud >= 0 && longitud <= 180 && latitud >= -90 && latitud <= 90){
            for(Maquina machine : maquinas){
            // Validar si el avion esta en el aire.                                                     
                boolean flag = machine.seraDestruido(longitud, latitud);                
                if (flag){
                    maquinasSeranDestruidas.add(machine);    
            }
        }        
        return maquinasSeranDestruidas;
    }
}
    
    // Third method  -  first part
    public ArrayList<Maquina>  maquinasDebiles(){
            for(Maquina machine : maquinas){
            // Validar si el avion esta en el aire.                                                     
                machineIsWeak = machine.IsWeak();
                
                if (machineIsWeak){
                    maquinasSeranDestruidas.add(maquinasDebiles);    
                }
            }
            
        return maquinasDebiles;
    }
    
    // fourth method -- first part
    public boolean esBuenAtaque(int longitud, int latitud){
        if (longitud >= 0 && longitud <= 180 && latitud >= -90 && latitud <= 90){
            for(Maquina machine: maquinas){
                if(maquinas.attack()){
                    maquinas.remove(maquinasDebiles);
                    return true;
                }
            }
            return false;
        }
        return false;
    
    }
    
}