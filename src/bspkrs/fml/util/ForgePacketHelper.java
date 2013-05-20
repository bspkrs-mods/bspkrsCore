package bspkrs.fml.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;

public final class ForgePacketHelper
{
    public static Packet250CustomPayload createPacket(String channel, int packetID, Object[] input)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(bytes);
        try
        {
            data.write(packetID);
            
            if (input != null)
            {
                for (Object obj : input)
                {
                    writeObjectToStream(obj, data);
                }
            }
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = channel;
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;
        
        return packet;
    }
    
    public static Object[] readPacketData(DataInputStream data, Class[] packetDataTypes)
    {
        List result = new ArrayList<Object>();
        
        try
        {
            for (Class curClass : packetDataTypes)
            {
                result.add(readObjectFromStream(data, curClass));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return result.toArray();
    }
    
    private static void writeObjectToStream(Object obj, DataOutputStream data) throws IOException
    {
        Class objClass = obj.getClass();
        
        if (objClass.equals(Boolean.class))
        {
            data.writeBoolean((Boolean) obj);
        }
        else if (objClass.equals(Byte.class))
        {
            data.writeByte((Byte) obj);
        }
        else if (objClass.equals(Integer.class))
        {
            data.writeInt((Integer) obj);
        }
        else if (objClass.equals(String.class))
        {
            data.writeUTF((String) obj);
        }
        else if (objClass.equals(Double.class))
        {
            data.writeDouble((Double) obj);
        }
        else if (objClass.equals(Float.class))
        {
            data.writeFloat((Float) obj);
        }
        else if (objClass.equals(Long.class))
        {
            data.writeLong((Long) obj);
        }
        else if (objClass.equals(Short.class))
        {
            data.writeShort((Short) obj);
        }
        else if (objClass.equals(NBTTagCompound.class))
        {
            writeNBTTagCompound((NBTTagCompound) obj, data);
        }
    }
    
    private static Object readObjectFromStream(DataInputStream data, Class curClass) throws IOException
    {
        if (curClass.equals(Boolean.class))
        {
            return data.readBoolean();
        }
        else if (curClass.equals(Byte.class))
        {
            return data.readByte();
        }
        else if (curClass.equals(Integer.class))
        {
            return data.readInt();
        }
        else if (curClass.equals(String.class))
        {
            return data.readUTF();
        }
        else if (curClass.equals(Double.class))
        {
            return data.readDouble();
        }
        else if (curClass.equals(Float.class))
        {
            return data.readFloat();
        }
        else if (curClass.equals(Long.class))
        {
            return data.readLong();
        }
        else if (curClass.equals(Short.class))
        {
            return data.readShort();
        }
        else if (curClass.equals(NBTTagCompound.class))
        {
            return readNBTTagCompound(data);
        }
        
        return null;
    }
    
    /**
     * Reads a compressed NBTTagCompound from the InputStream
     */
    public static NBTTagCompound readNBTTagCompound(DataInputStream data) throws IOException
    {
        short length = data.readShort();
        
        if (length < 0)
        {
            return null;
        }
        else
        {
            byte[] buf = new byte[length];
            data.readFully(buf);
            return CompressedStreamTools.decompress(buf);
        }
    }
    
    /**
     * Writes a compressed NBTTagCompound to the OutputStream
     */
    public static void writeNBTTagCompound(NBTTagCompound ntc, DataOutputStream data) throws IOException
    {
        if (ntc == null)
        {
            data.writeShort(-1);
        }
        else
        {
            byte[] buf = CompressedStreamTools.compress(ntc);
            data.writeShort((short) buf.length);
            data.write(buf);
        }
    }
    
    public static int readPacketID(DataInputStream data)
    {
        int result = -1;
        
        try
        {
            result = data.read();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return result;
    }
}
