package domain; 

import java.util.ArrayList;
import java.util.TreeMap;
import javax.swing.*;

/**
 * CostumeShop
 * @author POOB  
 * @version ECI 2024
 */

public class HalloweenShop{
    private ArrayList<Costume> costumes;
    private TreeMap<String,Basic> basics;

    /**
     * Create a HalloweenShop
     */
    public HalloweenShop(){
        costumes = new ArrayList<Costume>();
        basics = new TreeMap<String,Basic>();
        addSome();
    }

    private void addSome(){
        String [][] basics = {{"Camisa blanca", "5000", "10"},
                              {"Pantalon negro", "10000", "20"},
                              {"Capa negra", "15000", "0"},
                              {"Pantalon rojo", "30000", "25"}}; // Nuevo disfraz básico
        try {
            for (String [] c: basics){
                addBasic(c[0], c[1], c[2]);
            }
            
            String [][] Complete = {{"Zorro", "2000","0","Camisa blanca\nPantalon negro\nCapa negra"},
                                    {"Diablo","25000","50", "Pantalon rojo\nCapa negra\n"}
                                    };
            for (String [] s: Complete){
                addComplete(s[0],s[1],s[2],s[3]);
            }
        } catch (HalloweenShopException e){
            System.out.println(e.getMessage());
        }
}



    /**
     * Consult a costume
     * @param name
     * @return 
     */
    public Costume consult(String name){
        Costume c=null;
        for(int i=0;i<costumes.size() && c == null;i++){
            if (costumes.get(i).name().compareToIgnoreCase(name)==0) 
               c=costumes.get(i);
        }
        return c;
    }

    
    /**
     * Add a new basic costume
     * @param name 
     * @param price
     * @param discount
    */
    public void addBasic(String name, String price, String discount) throws HalloweenShopException { 
        // Verificar si el disfraz ya existe
        if (!select(name).isEmpty()) {
            throw new HalloweenShopException(HalloweenShopException.DUPLICATE_BASIC);
        }
    
        try {
            int intPrice = Integer.parseInt(price);
            int intDiscount = Integer.parseInt(discount);
    
            // Verificar que el precio no sea negativo
            if (intPrice < 0) {
                throw new HalloweenShopException(HalloweenShopException.NEGATIVE_PRICE);
            }
    
            // Verificar que el descuento esté en el rango válido
            else if (intDiscount < 0 || intDiscount > 100) {
                throw new HalloweenShopException(HalloweenShopException.INVALID_DISCOUNT);
            }
            else{
            // Crear y agregar el disfraz básico si todas las validaciones pasan
            Basic nc = new Basic(name, intPrice, intDiscount);
            costumes.add(nc);
            basics.put(name.toUpperCase(), nc); 
            }
        } catch (NumberFormatException e) {
            throw new HalloweenShopException(HalloweenShopException.NO_NUMERIC_VALUES);
        }
            catch (HalloweenShopException f){
                if(f.getMessage().equals(HalloweenShopException.NEGATIVE_PRICE)){
                    JOptionPane.showMessageDialog(null, f.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                }
                
                if(f.getMessage().equals(HalloweenShopException.INVALID_DISCOUNT)){
                    JOptionPane.showMessageDialog(null, f.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                };
            }
    }

    
    /**
     * Add a new Complete costume
     * @param name 
     * @param makeUp
     * @param basics
    */
    public void addComplete(String name, String makeUp, String discount, String theBasics) throws HalloweenShopException{ 
        if (!existBasics(theBasics)) throw new HalloweenShopException(HalloweenShopException.NO_EXIST_BASICS);
        Complete c = new Complete(name,Integer.parseInt(makeUp),Integer.parseInt(discount));
        String [] aBasics= theBasics.split("\n");
        for (String b : aBasics){
            c.addBasic(basics.get(b.toUpperCase()));
        }
        costumes.add(c);
    }
    
    private boolean existBasics(String basicsSet){
        // dividimos los basics
        String[] basicsArray = basicsSet.split("\n");

        // Recorremos cada elemento para verificar que exista en el TreeMap
        for (String basicArrayElement : basicsArray) {
            if (!basics.containsKey(basicArrayElement.toUpperCase())) {
                return false; // Si algún elemento no existe, devuelve false inmediatamente
            }   
        }
        return true;
    }
    
    /**
     * Consults the costumes that start with a prefix
     * @param  
     * @return 
     */
    public ArrayList<Costume> select(String prefix) {
        ArrayList<Costume> answers = new ArrayList<>();
        prefix = prefix.toUpperCase();
        for (int i = 0; i < costumes.size(); i++) {  // Cambiamos `<=` a `<` para evitar un IndexOutOfBoundsException
            if (costumes.get(i).name().toUpperCase().startsWith(prefix)) {
                answers.add(costumes.get(i));
            }
        }
        return answers;
    }



   public void clearCostumes() {
        costumes.clear();
        basics.clear();
   }
    
    /**
     * Consult selected costumes
     * @param selected
     * @return  
     */
    public String data(ArrayList<Costume> selected){
        StringBuffer answer=new StringBuffer();
        answer.append(costumes.size()+ " disfraces\n");
        for(Costume p : selected) {
            try{
                answer.append('>' + p.data());
                answer.append("\n");
            }catch(HalloweenShopException e){
                answer.append("**** "+e.getMessage());
            }
        }    
        return answer.toString();
    }
    
    
     /**
     * Return the data of costumes with a prefix
     * @param prefix
     * @return  
     */ 
    public String search(String prefix){
        return data(select(prefix));
    }
    
    
    /**
     * Return the data of all costumes
     * @return  
     */    
    public String toString(){
        return data(costumes);
    }
    
    /**
     * Consult the number of costumes
     * @return 
     */
    public int numberCostumes(){
        return costumes.size();
    }

}
