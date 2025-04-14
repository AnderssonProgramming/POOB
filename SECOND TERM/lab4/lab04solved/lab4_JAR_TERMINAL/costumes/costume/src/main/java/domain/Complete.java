package domain;
import domain.*;  

import java.util.ArrayList;


public class Complete extends Costume{
   

    private int makeUp;
    private ArrayList<Basic> pieces;
    
    /**
     * Constructs a new complete custom
     * @param name 
     * @param makeUp
     * @param discount 
     */
    public Complete(String name, int makeUp, int discount){
        super(name,discount);
        this.makeUp=makeUp;
        pieces= new ArrayList<Basic>();
    }


     /**
     * Add a new basic piece
     * @param b
     */   
    public void addBasic(Basic b){
        pieces.add(b);
    }
       
 
    
    @Override
    public int price() throws HalloweenShopException{        
        int totalAmount = 0;
        if (pieces.isEmpty()) throw new HalloweenShopException(HalloweenShopException.COMPLETE_EMPTY);        
        for (Basic piece : pieces){
                totalAmount += piece.price();
        }            
        return totalAmount;
    };
    
    
     /**
     * Calculates an estimate price
     * For basics where the price cannot be known or has error, the unknown or the error value is assumed
     * @param unknown sum totalAmount with unknown
     * @param error sum totalAmount with error
     * @return totalAmount
     * @throws HalloweenException COMPLETE_EMPTY, if it don't have basics. PRICE_ERROR, if the unknown or error value has error
     */
    public int price(int unknown, int error) throws HalloweenShopException{        
        int totalAmount = 0;
        
        if (pieces.isEmpty()) throw new HalloweenShopException(HalloweenShopException.COMPLETE_EMPTY); 
        
        for (Basic piece : pieces){          
          try {
              totalAmount += piece.price();
            } catch(HalloweenShopException e){
                if (e.getMessage().equals(HalloweenShopException.PRICE_UNKNOWN)){
                    if (unknown <0) throw new HalloweenShopException(HalloweenShopException.PRICE_ERROR);
                    totalAmount += unknown;                
                }
                if (e.getMessage().equals(HalloweenShopException.PRICE_ERROR)){
                    if (error < 0) throw new HalloweenShopException(HalloweenShopException.PRICE_ERROR);
                    totalAmount += error;
                }
            }                                
        }            
        return totalAmount;        
    }   
    
    
     /**
     * Calculates an estimate price
     * For basics where the price cannot be known, the maximum or the minimum value of the other basics is assumed
     * @param maximum - if true, assumes the maximum value; otherwise, assumes the minimum.
     * @return estimated price.
     * @throws CostumeShopException COMPLETE_EMPTY, if it don't have basics. PRICE_ERROR, if some basic has error
     */
    public int price(boolean maximum) throws HalloweenShopException{
        
        if (pieces.isEmpty()) throw new HalloweenShopException(HalloweenShopException.COMPLETE_EMPTY);

        Integer estimatedPrice = null; 
        
        for (Basic piece : pieces) {
            try {
                int piecePrice = piece.price();
                
                if (estimatedPrice == null) { // First valid price found
                    estimatedPrice = piecePrice;
                } else if (maximum) {
                    estimatedPrice = Math.max(estimatedPrice, piecePrice);
                } else {
                    estimatedPrice = Math.min(estimatedPrice, piecePrice);
                }
            } catch (HalloweenShopException e) {
                if (e.getMessage().equals(HalloweenShopException.PRICE_ERROR) && maximum) {
                    if(estimatedPrice < 0) throw new HalloweenShopException(HalloweenShopException.PRICE_ERROR);
                }
                // If not maximum or PRICE_ERROR, skip this piece
            }
        }
        
        if (estimatedPrice == null) throw new HalloweenShopException(HalloweenShopException.COMPLETE_EMPTY);

        return estimatedPrice;
    }
    
    @Override
    public String data() throws HalloweenShopException{
        StringBuffer answer=new StringBuffer();
        answer.append(name+". Maquillaje "+ makeUp+". Descuento: "+ discount);
        for(Basic b: pieces) {
            answer.append("\n\t"+b.data());
        }
        return answer.toString();
    } 
    

}
