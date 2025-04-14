public class Barco
{   
    private ArrayList<Marino> marinos;
    public Barco()
    {
    
    }

    
    public boolean isWeak()
    {
        if (marinos.length < 5){
            return true;
        }   
        
        return false;
    }
}
