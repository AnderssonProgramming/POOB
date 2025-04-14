package domain; 

import java.util.ArrayList;
import java.util.TreeMap;

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
        String [][] basics = {{"Camisa blanca","5000","10"},
                              {"Pantalon negro","10000","20"},
                              {"Capa negra","15000","0"}};
        for (String [] c: basics){
            addBasic(c[0],c[1],c[2]);
        }
        String [][] Complete = {{"Zorro", "2000","0","Camisa blanca\nPantalon negro\nCapa negra"}};
        for (String [] s: Complete){
            addComplete(s[0],s[1],s[2],s[3]);
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
    public void addBasic(String name, String price, String discount){ 
        Basic nc=new Basic(name,Integer.parseInt(price),Integer.parseInt(discount));
        costumes.add(nc);
        basics.put(name.toUpperCase(),nc); 
    }
    
    /**
     * Add a new Complete costume
     * @param name 
     * @param makeUp
     * @param basics
    */
    public void addComplete(String name, String makeUp, String discount, String theBasics){ 
        Complete c = new Complete(name,Integer.parseInt(makeUp),Integer.parseInt(discount));
        String [] aBasics= theBasics.split("\n");
        for (String b : aBasics){
            c.addBasic(basics.get(b.toUpperCase()));
        }
        costumes.add(c);
    }

    /**
     * Consults the costumes that start with a prefix
     * @param  
     * @return 
     */
    public ArrayList<Costume> select(String prefix){
        ArrayList <Costume> answers=new ArrayList<Costume>();
        prefix=prefix.toUpperCase();
        for(int i=0;i<=costumes.size();i++){
            if(costumes.get(i).name().toUpperCase().startsWith(prefix)){
                answers.add(costumes.get(i));
            }   
        }
        return answers;
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
