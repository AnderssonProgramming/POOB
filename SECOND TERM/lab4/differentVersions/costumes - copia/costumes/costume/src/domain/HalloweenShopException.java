package domain;


/**
 * Write a description of class HalloweenShopException here.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class HalloweenShopException extends Exception
{
    
    public static final String PRICE_UNKNOWN = "Unknown value has error";
    public static final String PRICE_ERROR = "Error value has error";
    public static final String COMPLETE_EMPTY = "Don't have basics";
    

    /**
     * Constructor for objects of class HalloweenShopException
     */
    public HalloweenShopException(String message)
    {
        super(message);
    }

}
