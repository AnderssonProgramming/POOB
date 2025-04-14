package domain;  

public class Basic extends Costume{
    
    private Integer price;
    
    public Basic(String name, Integer price, int discount){
        super(name,discount);
        this.price=price;
    }    
    
    
    @Override
    public int price() throws HalloweenShopException{
       if (price == null) throw new HalloweenShopException(HalloweenShopException.PRICE_UNKNOWN);
       if (price < 1) throw new HalloweenShopException(HalloweenShopException.PRICE_ERROR);
       // Añadimos el descuento para retornar el precio a pagar por la pieza
       int totalAmount = price - price*discount;
       return totalAmount;
    }    
    
    @Override
    public String data(){
        return name+". Precio:" +price+".Descuento:"+discount;
    }
}
