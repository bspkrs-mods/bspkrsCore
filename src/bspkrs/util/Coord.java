package bspkrs.util;

public class Coord
{
    public int x;
    public int y;
    public int z;
    
    public Coord(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public Coord clone()
    {
        return new Coord(x, y, z);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        
        if (!(obj instanceof Coord))
            return false;
        
        Coord o = (Coord) obj;
        return x == o.x && y == o.y && z == o.z;
    }
    
    @Override
    public int hashCode()
    {
        return x + z << 8 + y << 16;
    }
    
    @Override
    public String toString()
    {
        return x + "," + y + "," + z;
    }
}
