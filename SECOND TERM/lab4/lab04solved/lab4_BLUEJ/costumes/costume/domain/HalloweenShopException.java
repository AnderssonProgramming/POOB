package domain;


/**
 * Class HalloweenShopException .
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class HalloweenShopException extends Exception
{
    
    public static final String PRICE_UNKNOWN = "Price unknown";
    public static final String PRICE_ERROR = "Error value has error";
    public static final String COMPLETE_EMPTY = "Don't have basics";
    public static final String DUPLICATE_BASIC = "You can't add a duplicate basic";
    public static final String NO_NUMERIC_VALUES = "It seems that price or discount are not numeric values, please check them out.";
    public static final String INVALID_DISCOUNT = "Discount should be between 0 and 100";
    public static final String NO_EXIST_BASICS = "You can use only basics that already exist.";
    public static final String NEGATIVE_PRICE = "El precio no puede ser negativo";
    

    /**
     * Constructor for objects of class HalloweenShopException
     */
    public HalloweenShopException(String message)
    {
        super(message);
        Log.record(this);
    }

}
