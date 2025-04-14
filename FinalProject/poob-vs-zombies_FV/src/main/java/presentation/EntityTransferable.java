package presentation;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * The EntityTransferable class implements the Transferable interface to enable
 * the transfer of EntityData objects between different parts of the application
 * or between applications.
 * 
 */
public class EntityTransferable implements Transferable {
    public static final DataFlavor ENTITY_FLAVOR = new DataFlavor(EntityData.class, "EntityData");
    private static final DataFlavor[] SUPPORTED_FLAVORS = { ENTITY_FLAVOR };

    private EntityData data;

    /**
     * Constructs an EntityTransferable object with the specified EntityData.
     *
     * @param data the EntityData to be transferred
     */
    public EntityTransferable(EntityData data) {
        this.data = data;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return SUPPORTED_FLAVORS;
    }

    /**
     * Checks if the specified data flavor is supported.
     *
     * @param flavor the data flavor to check
     * @return true if the specified data flavor is supported, false otherwise
     */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(ENTITY_FLAVOR);
    }

    /**
     * Returns the transfer data for the specified data flavor.
     *
     * @param flavor the requested flavor for the data
     * @return the transfer data for the specified flavor
     * @throws UnsupportedFlavorException if the requested data flavor is not supported
     */
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return data;
    }
}
