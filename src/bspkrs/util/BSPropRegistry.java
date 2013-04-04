package bspkrs.util;

import java.util.ArrayList;
import java.util.List;

/**
 * BSPropRegistry manages the list of registered PropHandler objects.
 * 
 * @author bspkrs
 */

public final class BSPropRegistry
{
    private static List<BSPropHandler> registeredPropHandlers  = new ArrayList<BSPropHandler>();
    private static List<Class>         registeredBSPropClasses = new ArrayList<Class>();
    
    /**
     * Registers a new BSProp annotation handler and initializes annotated fields for the specified class using the default prop filename.
     * 
     * @param clazz the class that contains the BSProp annotated fields
     */
    public static void registerPropHandler(Class<?> clazz)
    {
        registerPropHandler(clazz, null);
    }
    
    /**
     * Registers a new BSProp annotation handler and initializes annotated fields for the specified class.
     * 
     * @param clazz the class that contains the BSProp annotated fields
     * @param customFilename the filename to be used for the properties output (if blank the classname will be used). It is recommended that
     *            .bsprop.cfg is used as the extension to be sure that FML will not think it's an unused MLProp file and rename it.
     */
    public static void registerPropHandler(Class<?> clazz, String customFilename)
    {
        if (clazz != null && !registeredBSPropClasses.contains(clazz))
        {
            if (customFilename != null && customFilename.length() > 0)
                registeredPropHandlers.add(new BSPropHandler(CommonUtils.getConfigDir(), customFilename, clazz));
            else
                registeredPropHandlers.add(new BSPropHandler(CommonUtils.getConfigDir(), clazz));
            
            registeredBSPropClasses.add(clazz);
        }
    }
}
