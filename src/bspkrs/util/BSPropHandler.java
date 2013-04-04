package bspkrs.util;

/**
 * Much of this code is borrowed/adapted from the decompiled version of Risugami's ModLoader MLProp handling code.
 * @Authors: Risugami, bspkrs
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class BSPropHandler
{
    private String       cfgDir;
    private Class        clazz;
    private String       propFilename;
    private BSProperties props;
    private List<Field>  propFields;
    private String       comments;
    private int          fieldsCheckSum, propsCheckSum;
    
    public BSPropHandler(String cfgDir, String propFilename, Class<?> clazz)
    {
        this.cfgDir = cfgDir;
        this.propFilename = propFilename;
        this.clazz = clazz;
        
        try
        {
            this.synchPropsAndFields(false, true);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
    
    public BSPropHandler(String cfgDir, Class<?> clazz)
    {
        this(cfgDir, clazz.getSimpleName() + ".bsprop.cfg", clazz);
    }
    
    private void readPropsFromFile() throws FileNotFoundException, IOException
    {
        props = new BSProperties();
        propsCheckSum = 0;
        File propFile = new File(cfgDir, propFilename);
        
        // if the config file exists, load props from it
        if (propFile.exists() && propFile.canRead())
        {
            props.load(new FileInputStream(propFile));
        }
        
        // get the checksum of the existing props
        if (props.containsKey("checksum"))
        {
            propsCheckSum = Integer.parseInt(props.getProperty("checksum"), 36);
        }
    }
    
    private void writePropsToFile() throws FileNotFoundException, IOException
    {
        File propFile = new File(cfgDir, propFilename);
        
        if (!props.isEmpty() && (propFile.exists() || propFile.createNewFile()) && propFile.canWrite())
        {
            props.store(new FileOutputStream(propFile), comments);
        }
    }
    
    private void getPropFieldsListAndCheckSum() throws IllegalArgumentException, IllegalAccessException
    {
        propFields = new LinkedList<Field>();
        fieldsCheckSum = 0;
        
        // for each declared field in clazz, check if it has the BSProp annotation, save a list of annotated fields, 
        // and generate the field checksum
        for (Field field : clazz.getDeclaredFields())
        {
            if (isFieldModifierPermissible(field.getModifiers()) && field.isAnnotationPresent(BSProp.class))
            {
                propFields.add(field);
                Object fieldValue = field.get((Object) null);
                fieldsCheckSum += fieldValue.hashCode();
            }
        }
    }
    
    /**
     * 
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void updatePropsFromFields() throws IllegalArgumentException, IllegalAccessException, FileNotFoundException, IOException
    {
        this.synchPropsAndFields(true, false);
    }
    
    /**
     * 
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void reloadPropsToFields() throws IllegalArgumentException, IllegalAccessException, FileNotFoundException, IOException
    {
        this.synchPropsAndFields(false, false);
    }
    
    public static boolean isFieldModifierPermissible(int modifier)
    {
        return Modifier.isStatic(modifier) && Modifier.isPublic(modifier) && !Modifier.isFinal(modifier);
    }
    
    private void synchPropsAndFields(boolean forceUpdatePropsFromFields, boolean isInitialCall) throws IllegalArgumentException, IllegalAccessException, FileNotFoundException, IOException
    {
        if (forceUpdatePropsFromFields && isInitialCall)
        {
            throw new IllegalArgumentException("Tried to call BSPropHandler.synchPropsAndFields() with both parameters == true.");
        }
        
        // Only load the properties file if we are not forcing the field values into the props
        if (!forceUpdatePropsFromFields)
            this.readPropsFromFile();
        else
            // Set the props checksum to 0 so that the fields will take priority
            this.propsCheckSum = 0;
        
        // We only want to do this when the fields have the default values in them
        if (isInitialCall)
            this.getPropFieldsListAndCheckSum();
        
        StringBuilder commentSB = new StringBuilder();
        for (Field propField : propFields)
        {
            // double-check that the field is annotated and we are able to modify its value
            if (isFieldModifierPermissible(propField.getModifiers()) && propField.isAnnotationPresent(BSProp.class))
            {
                Class fieldType = propField.getType();
                BSProp propAnnotation = propField.getAnnotation(BSProp.class);
                String propName = propAnnotation.name().length() != 0 ? propAnnotation.name() : propField.getName();
                Object fieldValue = propField.get((Object) null);
                StringBuilder acceptableRangeSB = new StringBuilder();
                StringBuilder propInfoSB = new StringBuilder();
                
                if (isInitialCall)
                {
                    if (propAnnotation.min() != Double.NEGATIVE_INFINITY)
                    {
                        acceptableRangeSB.append(String.format(",>=%.1f", new Object[] { Double.valueOf(propAnnotation.min()) }));
                    }
                    
                    if (propAnnotation.max() != Double.POSITIVE_INFINITY)
                    {
                        acceptableRangeSB.append(String.format(",<=%.1f", new Object[] { Double.valueOf(propAnnotation.max()) }));
                    }
                    
                    if (propAnnotation.info().length() > 0)
                    {
                        propInfoSB.append(" -- ");
                        propInfoSB.append(propAnnotation.info());
                    }
                    
                    commentSB.append(String.format("%s (%s:%s%s)%s\n", new Object[] { propName, fieldType.getName(), fieldValue, acceptableRangeSB, propInfoSB }));
                }
                
                if (propsCheckSum == fieldsCheckSum && props.containsKey(propName))
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
                            
                            if (propAnnotation.min() != Double.NEGATIVE_INFINITY && doubleValue < propAnnotation.min())
                            {
                                BSLog.warning(String.format("Value %n is less than the specified min (%n) for %s. Value set to %n.", doubleValue, propAnnotation.min(), propName, propAnnotation.min()));
                                wrappedPropValue = Double.valueOf(propAnnotation.min());
                                props.setProperty(propName, wrappedPropValue.toString());
                            }
                            else if (propAnnotation.max() != Double.POSITIVE_INFINITY && doubleValue > propAnnotation.max())
                            {
                                BSLog.warning(String.format("Value %n is more than the specified max (%n) for %s. Value set to %n.", doubleValue, propAnnotation.max(), propName, propAnnotation.max()));
                                wrappedPropValue = Double.valueOf(propAnnotation.max());
                                props.setProperty(propName, wrappedPropValue.toString());
                            }
                        }
                        
                        BSLog.info(propName + " set to " + wrappedPropValue);
                        
                        if (!wrappedPropValue.equals(fieldValue))
                        {
                            propField.set((Object) null, wrappedPropValue);
                        }
                    }
                }
                else
                {
                    BSLog.info(propName + " not in config, using field value: " + fieldValue);
                    props.setProperty(propName, fieldValue.toString());
                }
            }
        }
        
        props.put("checksum", Integer.toString(fieldsCheckSum, 36));
        if (propsCheckSum != fieldsCheckSum)
            propsCheckSum = fieldsCheckSum;
        
        // Only update the comments if this is the initialization call!
        if (isInitialCall)
            comments = commentSB.toString();
        
        this.writePropsToFile();
    }
}
