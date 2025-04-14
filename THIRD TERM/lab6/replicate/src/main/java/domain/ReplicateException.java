package domain;

public class ReplicateException extends Exception
{
    public static final String OPTION_OPEN = "Opción Open en construcción";
    public static final String OPTION_SAVE = "Opción Save en construcción";
    public static final String OPTION_IMPORT = "Opción Import en construcción";
    public static final String OPTION_EXPORT = "Opción Export en construcción";

    public ReplicateException(String message)
    {
        super(message);
        
    }
}