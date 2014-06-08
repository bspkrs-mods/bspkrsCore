package bspkrs.testmod.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemLead;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTreeBranch extends Block
{
    private final String textureName;
    
    public BlockTreeBranch(String p_i45406_1_, Material material)
    {
        super(material);
        this.textureName = p_i45406_1_;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the mask.) Parameters:
     * World, X, Y, Z, mask, list, colliding entity
     */
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List collisionBoxes, Entity entity)
    {
        boolean canConnectNorth = this.canConnectBranchTo(world, x, y, z - 1);
        boolean canConnectSouth = this.canConnectBranchTo(world, x, y, z + 1);
        boolean canConnectDown = this.canConnectBranchTo(world, x, y - 1, z);
        boolean canConnectUp = this.canConnectBranchTo(world, x, y + 1, z);
        boolean canConnectWest = this.canConnectBranchTo(world, x - 1, y, z);
        boolean canConnectEast = this.canConnectBranchTo(world, x + 1, y, z);
        float minX = 0.375F;
        float maxX = 0.625F;
        float minY = 0.375F;
        float maxY = 0.625F;
        float minZ = 0.375F;
        float maxZ = 0.625F;
        
        if (canConnectNorth)
        {
            minZ = 0.0F;
        }
        
        if (canConnectSouth)
        {
            maxZ = 1.0F;
        }
        
        if (canConnectNorth || canConnectSouth)
        {
            this.setBlockBounds(minX, 0.0F, minZ, maxX, 1.0F, maxZ);
            super.addCollisionBoxesToList(world, x, y, z, aabb, collisionBoxes, entity);
        }
        
        if (canConnectWest)
        {
            minX = 0.0F;
        }
        
        if (canConnectEast)
        {
            maxX = 1.0F;
        }
        
        if (canConnectWest || canConnectEast || !canConnectNorth && !canConnectSouth)
        {
            this.setBlockBounds(minX, 0.0F, minZ, maxX, 1.0F, maxZ);
            super.addCollisionBoxesToList(world, x, y, z, aabb, collisionBoxes, entity);
        }
        
        if (canConnectDown)
        {
            minZ = 0.0F;
        }
        
        if (canConnectUp)
        {
            maxZ = 1.0F;
        }
        
        this.setBlockBounds(minX, 0.0F, minZ, maxX, 1.0F, maxZ);
    }
    
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        boolean canConnectNorth = this.canConnectBranchTo(world, x, y, z - 1);
        boolean canConnectSouth = this.canConnectBranchTo(world, x, y, z + 1);
        boolean canConnectWest = this.canConnectBranchTo(world, x - 1, y, z);
        boolean canConnectEast = this.canConnectBranchTo(world, x + 1, y, z);
        float minX = 0.375F;
        float maxX = 0.625F;
        float minZ = 0.375F;
        float maxZ = 0.625F;
        
        if (canConnectNorth)
        {
            minZ = 0.0F;
        }
        
        if (canConnectSouth)
        {
            maxZ = 1.0F;
        }
        
        if (canConnectWest)
        {
            minX = 0.0F;
        }
        
        if (canConnectEast)
        {
            maxX = 1.0F;
        }
        
        this.setBlockBounds(minX, 0.0F, minZ, maxX, 1.0F, maxZ);
    }
    
    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether or not to render the shared face of two adjacent blocks and
     * also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z)
    {
        return false;
    }
    
    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return 11;
    }
    
    /**
     * Returns true if the specified block can be connected by a fence
     */
    public boolean canConnectBranchTo(IBlockAccess world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        return block == this || block.isWood(world, x, y, z) || block.isLeaves(world, x, y, z);
    }
    
    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given coordinates. Args:
     * blockAccess, x, y, z, side
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon(this.textureName);
    }
    
    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        return world.isRemote ? true : ItemLead.func_150909_a(entityPlayer, world, x, y, z);
    }
}