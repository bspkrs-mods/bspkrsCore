/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 * 
 * Modified from the original Forge implementation to remove references to Google java objects and to live in a different package.
 */

package bspkrs.util;

import static bspkrs.util.Configuration.COMMENT_SEPARATOR;
import static bspkrs.util.Configuration.NEW_LINE;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ConfigCategory implements Map<String, Property>
{
    private String                    name;
    private String                    comment;
    private ArrayList<ConfigCategory> children   = new ArrayList<ConfigCategory>();
    private Map<String, Property>     properties = new TreeMap<String, Property>();
    public final ConfigCategory       parent;
    private boolean                   changed    = false;
    
    public ConfigCategory(String name)
    {
        this(name, null);
    }
    
    public ConfigCategory(String name, ConfigCategory parent)
    {
        this.name = name;
        this.parent = parent;
        if (parent != null)
        {
            parent.children.add(this);
        }
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ConfigCategory)
        {
            ConfigCategory cat = (ConfigCategory) obj;
            return name.equals(cat.name) && children.equals(cat.children);
        }
        
        return false;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getQualifiedName()
    {
        return getQualifiedName(name, parent);
    }
    
    public static String getQualifiedName(String name, ConfigCategory parent)
    {
        return (parent == null ? name : parent.getQualifiedName() + Configuration.CATEGORY_SPLITTER + name);
    }
    
    public ConfigCategory getFirstParent()
    {
        return (parent == null ? this : parent.getFirstParent());
    }
    
    public boolean isChild()
    {
        return parent != null;
    }
    
    public Map<String, Property> getValues()
    {
        return properties;
    }
    
    public void setComment(String comment)
    {
        this.comment = comment;
    }
    
    public boolean containsKey(String key)
    {
        return properties.containsKey(key);
    }
    
    public Property get(String key)
    {
        return properties.get(key);
    }
    
    private void write(BufferedWriter out, String... data) throws IOException
    {
        write(out, true, data);
    }
    
    private void write(BufferedWriter out, boolean new_line, String... data) throws IOException
    {
        for (int x = 0; x < data.length; x++)
        {
            out.write(data[x]);
        }
        if (new_line)
            out.write(NEW_LINE);
    }
    
    public void write(BufferedWriter out, int indent) throws IOException
    {
        String pad0 = getIndent(indent);
        String pad1 = getIndent(indent + 1);
        String pad2 = getIndent(indent + 2);
        
        if (comment != null)
        {
            write(out, pad0, COMMENT_SEPARATOR);
            write(out, pad0, "# ", name);
            write(out, pad0, "#--------------------------------------------------------------------------------------------------------#");
            
            for (String line : comment.split("\r?\n"))
            {
                write(out, pad0, "# ", line);
            }
            
            write(out, pad0, COMMENT_SEPARATOR, NEW_LINE);
        }
        
        if (!CommonUtils.isStringAllLettersOrDigitsOr(name, Configuration.ALLOWED_CHARS))
        {
            name = '"' + name + '"';
        }
        
        write(out, pad0, name, " {");
        
        Property[] props = properties.values().toArray(new Property[properties.size()]);
        
        for (int x = 0; x < props.length; x++)
        {
            Property prop = props[x];
            
            if (prop.comment != null)
            {
                if (x != 0)
                {
                    out.newLine();
                }
                
                for (String commentLine : prop.comment.split("\r?\n"))
                {
                    write(out, pad1, "# ", commentLine);
                }
            }
            
            String propName = prop.getName();
            
            if (!CommonUtils.isStringAllLettersOrDigitsOr(propName, Configuration.ALLOWED_CHARS))
            {
                propName = '"' + propName + '"';
            }
            
            if (prop.isList())
            {
                char type = prop.getType().getID();
                
                write(out, pad1, String.valueOf(type), ":", propName, " <");
                
                for (String line : prop.getStringList())
                {
                    write(out, pad2, line);
                }
                
                write(out, pad1, " >");
            }
            else if (prop.getType() == null)
            {
                write(out, pad1, propName, "=", prop.getString());
            }
            else
            {
                char type = prop.getType().getID();
                write(out, pad1, String.valueOf(type), ":", propName, "=", prop.getString());
            }
        }
        
        if (children.size() > 0)
            out.newLine();
        
        for (ConfigCategory child : children)
        {
            child.write(out, indent + 1);
        }
        
        write(out, pad0, "}", NEW_LINE);
    }
    
    private String getIndent(int indent)
    {
        StringBuilder buf = new StringBuilder("");
        for (int x = 0; x < indent; x++)
        {
            buf.append("    ");
        }
        return buf.toString();
    }
    
    public boolean hasChanged()
    {
        if (changed)
            return true;
        for (Property prop : properties.values())
        {
            if (prop.hasChanged())
                return true;
        }
        return false;
    }
    
    void resetChangedState()
    {
        changed = false;
        for (Property prop : properties.values())
        {
            prop.resetChangedState();
        }
    }
    
    //Map bouncer functions for compatibility with older mods, to be removed once all mods stop using it.
    @Override
    public int size()
    {
        return properties.size();
    }
    
    @Override
    public boolean isEmpty()
    {
        return properties.isEmpty();
    }
    
    @Override
    public boolean containsKey(Object key)
    {
        return properties.containsKey(key);
    }
    
    @Override
    public boolean containsValue(Object value)
    {
        return properties.containsValue(value);
    }
    
    @Override
    public Property get(Object key)
    {
        return properties.get(key);
    }
    
    @Override
    public Property put(String key, Property value)
    {
        changed = true;
        return properties.put(key, value);
    }
    
    @Override
    public Property remove(Object key)
    {
        changed = true;
        return properties.remove(key);
    }
    
    @Override
    public void putAll(Map<? extends String, ? extends Property> m)
    {
        changed = true;
        properties.putAll(m);
    }
    
    @Override
    public void clear()
    {
        changed = true;
        properties.clear();
    }
    
    @Override
    public Set<String> keySet()
    {
        return properties.keySet();
    }
    
    @Override
    public Collection<Property> values()
    {
        return properties.values();
    }
    
    @Override
    public Set<java.util.Map.Entry<String, Property>> entrySet()
    {
        return properties.entrySet();
    }
    
}