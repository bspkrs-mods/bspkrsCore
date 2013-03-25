package bspkrs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Logger;

public class PropHandler
{
    private String     cfgDir;
    private Logger     logger;
    private Class      clazz;
    private String     customFilename;
    private Properties props;
    
    public PropHandler(String cfgDir, String customFilename, Class<?> clazz, Logger logger)
    {
        this.cfgDir = cfgDir;
        this.customFilename = customFilename;
        this.clazz = clazz;
        this.logger = logger;
        
        try
        {
            initProperties();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
    
    public PropHandler(String cfgDir, Class<?> clazz, Logger logger)
    {
        this(cfgDir, clazz.getSimpleName() + ".bsprop.cfg", clazz, logger);
    }
    
    private void initProperties() throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException, NoSuchFieldException, NoSuchAlgorithmException, DigestException
    {
        LinkedList propFields = new LinkedList();
        props = new Properties();
        int newCheckSum = 0;
        int existingCheckSum = 0;
        File propFile = new File(cfgDir, customFilename);
        
        if (propFile.exists() && propFile.canRead())
        {
            props.load(new FileInputStream(propFile));
        }
        
        if (props.containsKey("checksum"))
        {
            existingCheckSum = Integer.parseInt(props.getProperty("checksum"), 36);
        }
        
        Field[] declaredFields;
        int declaredFieldCount = (declaredFields = clazz.getDeclaredFields()).length;
        
        for (int i = 0; i < declaredFieldCount; ++i)
        {
            Field field = declaredFields[i];
            
            if ((field.getModifiers() & 8) != 0 && field.isAnnotationPresent(BSProp.class))
            {
                propFields.add(field);
                Object fieldValue = field.get((Object) null);
                newCheckSum += fieldValue.hashCode();
            }
        }
        
        StringBuilder comments = new StringBuilder();
        Iterator propFieldsIterator = propFields.iterator();
        
        while (propFieldsIterator.hasNext())
        {
            Field propField = (Field) propFieldsIterator.next();
            
            if ((propField.getModifiers() & 8) != 0 && propField.isAnnotationPresent(BSProp.class))
            {
                Class fieldType = propField.getType();
                BSProp propAnnotation = propField.getAnnotation(BSProp.class);
                String propName = propAnnotation.name().length() != 0 ? propAnnotation.name() : propField.getName();
                Object fieldValue = propField.get((Object) null);
                StringBuilder acceptableRange = new StringBuilder();
                
                if (propAnnotation.min() != Double.NEGATIVE_INFINITY)
                {
                    acceptableRange.append(String.format(",>=%.1f", new Object[] { Double.valueOf(propAnnotation.min()) }));
                }
                
                if (propAnnotation.max() != Double.POSITIVE_INFINITY)
                {
                    acceptableRange.append(String.format(",<=%.1f", new Object[] { Double.valueOf(propAnnotation.max()) }));
                }
                
                StringBuilder propInfo = new StringBuilder();
                
                if (propAnnotation.info().length() > 0)
                {
                    propInfo.append(" -- ");
                    propInfo.append(propAnnotation.info());
                }
                
                comments.append(String.format("%s (%s:%s%s)%s\n", new Object[] { propName, fieldType.getName(), fieldValue, acceptableRange, propInfo }));
                
                if (existingCheckSum == newCheckSum && props.containsKey(propName))
                {
                    String existingPropValue = props.getProperty(propName);
                    Object wrappedPropValue = null;
                    
                    if (fieldType.isAssignableFrom(String.class))
                    {
                        wrappedPropValue = existingPropValue;
                    }
                    else if (fieldType.isAssignableFrom(Integer.TYPE))
                    {
                        wrappedPropValue = Integer.valueOf(Integer.parseInt(existingPropValue));
                    }
                    else if (fieldType.isAssignableFrom(Short.TYPE))
                    {
                        wrappedPropValue = Short.valueOf(Short.parseShort(existingPropValue));
                    }
                    else if (fieldType.isAssignableFrom(Byte.TYPE))
                    {
                        wrappedPropValue = Byte.valueOf(Byte.parseByte(existingPropValue));
                    }
                    else if (fieldType.isAssignableFrom(Boolean.TYPE))
                    {
                        wrappedPropValue = Boolean.valueOf(Boolean.parseBoolean(existingPropValue));
                    }
                    else if (fieldType.isAssignableFrom(Float.TYPE))
                    {
                        wrappedPropValue = Float.valueOf(Float.parseFloat(existingPropValue));
                    }
                    else if (fieldType.isAssignableFrom(Double.TYPE))
                    {
                        wrappedPropValue = Double.valueOf(Double.parseDouble(existingPropValue));
                    }
                    
                    if (wrappedPropValue != null)
                    {
                        if (wrappedPropValue instanceof Number)
                        {
                            double doubleValue = ((Number) wrappedPropValue).doubleValue();
                            
                            if (propAnnotation.min() != Double.NEGATIVE_INFINITY && doubleValue < propAnnotation.min() || propAnnotation.max() != Double.POSITIVE_INFINITY && doubleValue > propAnnotation.max())
                            {
                                continue;
                            }
                        }
                        
                        logger.finer(propName + " set to " + wrappedPropValue);
                        
                        if (!wrappedPropValue.equals(fieldValue))
                        {
                            propField.set((Object) null, wrappedPropValue);
                        }
                    }
                }
                else
                {
                    logger.finer(propName + " not in config, using default: " + fieldValue);
                    props.setProperty(propName, fieldValue.toString());
                }
            }
        }
        
        props.put("checksum", Integer.toString(newCheckSum, 36));
        
        if (!props.isEmpty() && (propFile.exists() || propFile.createNewFile()) && propFile.canWrite())
        {
            props.store(new FileOutputStream(propFile), comments.toString());
        }
    }
    
}
