package bspkrs.util;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableSetMultimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Provides a fake world that can be used to render entities in client-side GUIs without a world actually running.
 * 
 * @author bspkrs
 * 
 */
public class FakeWorld extends World
{
    public FakeWorld()
    {
        super(new FakeSaveHandler(), "", new FakeWorldProvider(), new WorldSettings(new WorldInfo(new NBTTagCompound())), null);
        this.difficultySetting = EnumDifficulty.HARD;
    }
    
    @Override
    public BiomeGenBase getBiomeGenForCoords(int par1, int par2)
    {
        return BiomeGenBase.plains;
    }
    
    @Override
    public BiomeGenBase getBiomeGenForCoordsBody(int par1, int par2)
    {
        return BiomeGenBase.plains;
    }
    
    @Override
    public WorldChunkManager getWorldChunkManager()
    {
        return super.getWorldChunkManager();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    protected void finishSetup()
    {
        super.finishSetup();
    }
    
    @Override
    protected void initialize(WorldSettings par1WorldSettings)
    {
        super.initialize(par1WorldSettings);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void setSpawnLocation()
    {
        super.setSpawnLocation();
    }
    
    @Override
    public Block getTopBlock(int x, int z)
    {
        return Blocks.grass;
    }
    
    @Override
    public boolean isAirBlock(int x, int y, int z)
    {
        return y > 63;
    }
    
    @Override
    public boolean doChunksNearChunkExist(int par1, int par2, int par3, int par4)
    {
        return false;
    }
    
    @Override
    public boolean checkChunksExist(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        return false;
    }
    
    @Override
    public Chunk getChunkFromBlockCoords(int x, int z)
    {
        return super.getChunkFromBlockCoords(x, z);
    }
    
    @Override
    public boolean setBlock(int x, int y, int z, Block block, int meta, int notify)
    {
        return true;
    }
    
    @Override
    public int getBlockMetadata(int x, int y, int z)
    {
        return 0;
    }
    
    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int par4, int par5)
    {
        return true;
    }
    
    @Override
    public boolean setBlockToAir(int x, int y, int z)
    {
        return true;
    }
    
    @Override
    public boolean func_147480_a(int x, int y, int z, boolean p_147480_4_)
    {
        return this.isAirBlock(x, y, z);
    }
    
    @Override
    public boolean setBlock(int x, int y, int z, Block p_147449_4_)
    {
        return true;
    }
    
    @Override
    public void markBlockForUpdate(int p_147471_1_, int p_147471_2_, int p_147471_3_)
    {}
    
    @Override
    public void notifyBlockChange(int p_147444_1_, int p_147444_2_, int p_147444_3_, Block p_147444_4_)
    {}
    
    @Override
    public void markBlocksDirtyVertical(int par1, int par2, int par3, int par4)
    {}
    
    @Override
    public void markBlockRangeForRenderUpdate(int p_147458_1_, int p_147458_2_, int p_147458_3_, int p_147458_4_, int p_147458_5_, int p_147458_6_)
    {}
    
    @Override
    public void notifyBlocksOfNeighborChange(int p_147459_1_, int p_147459_2_, int p_147459_3_, Block p_147459_4_)
    {}
    
    @Override
    public void notifyBlocksOfNeighborChange(int p_147441_1_, int p_147441_2_, int p_147441_3_, Block p_147441_4_, int p_147441_5_)
    {}
    
    @Override
    public void notifyBlockOfNeighborChange(int p_147460_1_, int p_147460_2_, int p_147460_3_, Block p_147460_4_)
    {}
    
    @Override
    public boolean isBlockTickScheduledThisTick(int p_147477_1_, int p_147477_2_, int p_147477_3_, Block p_147477_4_)
    {
        return false;
    }
    
    @Override
    public boolean canBlockSeeTheSky(int x, int y, int z)
    {
        return y > 62;
    }
    
    @Override
    public int getFullBlockLightValue(int x, int y, int z)
    {
        return 14;
    }
    
    @Override
    public int getBlockLightValue(int x, int y, int z)
    {
        return 14;
    }
    
    @Override
    public int getBlockLightValue_do(int x, int y, int z, boolean par4)
    {
        return 14;
    }
    
    @Override
    public int getHeightValue(int x, int z)
    {
        return 63;
    }
    
    @Override
    public int getChunkHeightMapMinimum(int x, int z)
    {
        return 63;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getSkyBlockTypeBrightness(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4)
    {
        return 14;
    }
    
    @Override
    public int getSavedLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4)
    {
        return 14;
    }
    
    @Override
    public void setLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4, int par5)
    {}
    
    @Override
    public void func_147479_m(int p_147479_1_, int p_147479_2_, int p_147479_3_)
    {}
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getLightBrightnessForSkyBlocks(int par1, int par2, int par3, int par4)
    {
        return 14;
    }
    
    @Override
    public float getLightBrightness(int par1, int par2, int par3)
    {
        return super.getLightBrightness(par1, par2, par3);
    }
    
    @Override
    public boolean isDaytime()
    {
        return true;
    }
    
    @Override
    public MovingObjectPosition rayTraceBlocks(Vec3 par1Vec3, Vec3 par2Vec3)
    {
        return null;
    }
    
    @Override
    public MovingObjectPosition rayTraceBlocks(Vec3 par1Vec3, Vec3 par2Vec3, boolean par3)
    {
        return null;
    }
    
    @Override
    public MovingObjectPosition func_147447_a(Vec3 p_147447_1_, Vec3 p_147447_2_, boolean p_147447_3_, boolean p_147447_4_, boolean p_147447_5_)
    {
        return null;
    }
    
    @Override
    public void playSoundAtEntity(Entity par1Entity, String par2Str, float par3, float par4)
    {}
    
    @Override
    public void playSoundToNearExcept(EntityPlayer par1EntityPlayer, String par2Str, float par3, float par4)
    {}
    
    @Override
    public void playSoundEffect(double par1, double par3, double par5, String par7Str, float par8, float par9)
    {}
    
    @Override
    public void playSound(double par1, double par3, double par5, String par7Str, float par8, float par9, boolean par10)
    {}
    
    @Override
    public void playRecord(String par1Str, int par2, int par3, int par4)
    {}
    
    @Override
    public void spawnParticle(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12)
    {}
    
    @Override
    public boolean addWeatherEffect(Entity par1Entity)
    {
        return false;
    }
    
    @Override
    public boolean spawnEntityInWorld(Entity par1Entity)
    {
        return false;
    }
    
    @Override
    public void onEntityAdded(Entity par1Entity)
    {}
    
    @Override
    public void onEntityRemoved(Entity par1Entity)
    {}
    
    @Override
    public void removeEntity(Entity par1Entity)
    {}
    
    @Override
    public void removePlayerEntityDangerously(Entity par1Entity)
    {}
    
    @Override
    public void addWorldAccess(IWorldAccess par1iWorldAccess)
    {
        super.addWorldAccess(par1iWorldAccess);
    }
    
    @Override
    public List getCollidingBoundingBoxes(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB)
    {
        return super.getCollidingBoundingBoxes(par1Entity, par2AxisAlignedBB);
    }
    
    @Override
    public List func_147461_a(AxisAlignedBB p_147461_1_)
    {
        return super.func_147461_a(p_147461_1_);
    }
    
    @Override
    public int calculateSkylightSubtracted(float par1)
    {
        return 6;
    }
    
    @Override
    public void removeWorldAccess(IWorldAccess par1iWorldAccess)
    {}
    
    @Override
    @SideOnly(Side.CLIENT)
    public float getSunBrightness(float par1)
    {
        return super.getSunBrightness(par1);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Vec3 getSkyColor(Entity par1Entity, float par2)
    {
        return super.getSkyColor(par1Entity, par2);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Vec3 getSkyColorBody(Entity par1Entity, float par2)
    {
        return super.getSkyColorBody(par1Entity, par2);
    }
    
    @Override
    public float getCelestialAngle(float par1)
    {
        return super.getCelestialAngle(par1);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getMoonPhase()
    {
        return super.getMoonPhase();
    }
    
    @Override
    public float getCurrentMoonPhaseFactor()
    {
        return super.getCurrentMoonPhaseFactor();
    }
    
    @Override
    public float getCelestialAngleRadians(float par1)
    {
        return super.getCelestialAngleRadians(par1);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Vec3 getCloudColour(float par1)
    {
        return super.getCloudColour(par1);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Vec3 drawCloudsBody(float par1)
    {
        return super.drawCloudsBody(par1);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float par1)
    {
        return super.getFogColor(par1);
    }
    
    @Override
    public int getPrecipitationHeight(int par1, int par2)
    {
        return super.getPrecipitationHeight(par1, par2);
    }
    
    @Override
    public int getTopSolidOrLiquidBlock(int par1, int par2)
    {
        return 63;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1)
    {
        return super.getStarBrightness(par1);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightnessBody(float par1)
    {
        return super.getStarBrightnessBody(par1);
    }
    
    @Override
    public void scheduleBlockUpdate(int p_147464_1_, int p_147464_2_, int p_147464_3_, Block p_147464_4_, int p_147464_5_)
    {}
    
    @Override
    public void scheduleBlockUpdateWithPriority(int p_147454_1_, int p_147454_2_, int p_147454_3_, Block p_147454_4_, int p_147454_5_, int p_147454_6_)
    {}
    
    @Override
    public void func_147446_b(int p_147446_1_, int p_147446_2_, int p_147446_3_, Block p_147446_4_, int p_147446_5_, int p_147446_6_)
    {}
    
    @Override
    public void updateEntities()
    {}
    
    @Override
    public void func_147448_a(Collection p_147448_1_)
    {}
    
    @Override
    public void updateEntity(Entity par1Entity)
    {}
    
    @Override
    public void updateEntityWithOptionalForce(Entity par1Entity, boolean par2)
    {}
    
    @Override
    public boolean checkNoEntityCollision(AxisAlignedBB par1AxisAlignedBB)
    {
        return true;
    }
    
    @Override
    public boolean checkNoEntityCollision(AxisAlignedBB par1AxisAlignedBB, Entity par2Entity)
    {
        return true;
    }
    
    @Override
    public boolean checkBlockCollision(AxisAlignedBB par1AxisAlignedBB)
    {
        return false;
    }
    
    @Override
    public boolean isAnyLiquid(AxisAlignedBB par1AxisAlignedBB)
    {
        return false;
    }
    
    @Override
    public boolean func_147470_e(AxisAlignedBB p_147470_1_)
    {
        return false;
    }
    
    @Override
    public boolean handleMaterialAcceleration(AxisAlignedBB par1AxisAlignedBB, Material par2Material, Entity par3Entity)
    {
        return false;
    }
    
    @Override
    public boolean isMaterialInBB(AxisAlignedBB par1AxisAlignedBB, Material par2Material)
    {
        return false;
    }
    
    @Override
    public boolean isAABBInMaterial(AxisAlignedBB par1AxisAlignedBB, Material par2Material)
    {
        return false;
    }
    
    @Override
    public Explosion createExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9)
    {
        return super.createExplosion(par1Entity, par2, par4, par6, par8, par9);
    }
    
    @Override
    public Explosion newExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9, boolean par10)
    {
        return super.newExplosion(par1Entity, par2, par4, par6, par8, par9, par10);
    }
    
    @Override
    public float getBlockDensity(Vec3 par1Vec3, AxisAlignedBB par2AxisAlignedBB)
    {
        return super.getBlockDensity(par1Vec3, par2AxisAlignedBB);
    }
    
    @Override
    public boolean extinguishFire(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5)
    {
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public String getDebugLoadedEntities()
    {
        return "";
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public String getProviderName()
    {
        return "";
    }
    
    @Override
    public TileEntity getTileEntity(int x, int y, int z)
    {
        return null;
    }
    
    @Override
    public void setTileEntity(int x, int y, int z, TileEntity p_147455_4_)
    {}
    
    @Override
    public void removeTileEntity(int x, int y, int z)
    {}
    
    @Override
    public void func_147457_a(TileEntity p_147457_1_)
    {}
    
    @Override
    public boolean func_147469_q(int p_147469_1_, int p_147469_2_, int p_147469_3_)
    {
        return false;
    }
    
    @Override
    public boolean isBlockNormalCubeDefault(int p_147445_1_, int p_147445_2_, int p_147445_3_, boolean p_147445_4_)
    {
        return true;
    }
    
    @Override
    public void calculateInitialSkylight()
    {
        super.calculateInitialSkylight();
    }
    
    @Override
    public void setAllowedSpawnTypes(boolean par1, boolean par2)
    {
        super.setAllowedSpawnTypes(par1, par2);
    }
    
    @Override
    public void tick()
    {}
    
    @Override
    public void calculateInitialWeatherBody()
    {
        super.calculateInitialWeatherBody();
    }
    
    @Override
    protected void updateWeather()
    {}
    
    @Override
    public void updateWeatherBody()
    {}
    
    @Override
    protected void setActivePlayerChunksAndCheckLight()
    {
        super.setActivePlayerChunksAndCheckLight();
    }
    
    @Override
    protected void func_147467_a(int p_147467_1_, int p_147467_2_, Chunk p_147467_3_)
    {}
    
    @Override
    protected void func_147456_g()
    {}
    
    @Override
    public boolean isBlockFreezable(int par1, int par2, int par3)
    {
        return false;
    }
    
    @Override
    public boolean isBlockFreezableNaturally(int par1, int par2, int par3)
    {
        return false;
    }
    
    @Override
    public boolean canBlockFreeze(int par1, int par2, int par3, boolean par4)
    {
        return false;
    }
    
    @Override
    public boolean canBlockFreezeBody(int par1, int par2, int par3, boolean par4)
    {
        return false;
    }
    
    @Override
    public boolean func_147478_e(int p_147478_1_, int p_147478_2_, int p_147478_3_, boolean p_147478_4_)
    {
        return false;
    }
    
    @Override
    public boolean canSnowAtBody(int p_147478_1_, int p_147478_2_, int p_147478_3_, boolean p_147478_4_)
    {
        return false;
    }
    
    @Override
    public boolean func_147451_t(int p_147451_1_, int p_147451_2_, int p_147451_3_)
    {
        return false;
    }
    
    @Override
    public boolean updateLightByType(EnumSkyBlock p_147463_1_, int p_147463_2_, int p_147463_3_, int p_147463_4_)
    {
        return false;
    }
    
    @Override
    public boolean tickUpdates(boolean par1)
    {
        return false;
    }
    
    @Override
    public List getPendingBlockUpdates(Chunk par1Chunk, boolean par2)
    {
        return null;
    }
    
    @Override
    public List getEntitiesWithinAABBExcludingEntity(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB)
    {
        return super.getEntitiesWithinAABBExcludingEntity(par1Entity, par2AxisAlignedBB);
    }
    
    @Override
    public List getEntitiesWithinAABBExcludingEntity(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB, IEntitySelector par3iEntitySelector)
    {
        return super.getEntitiesWithinAABBExcludingEntity(par1Entity, par2AxisAlignedBB, par3iEntitySelector);
    }
    
    @Override
    public List getEntitiesWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB)
    {
        return super.getEntitiesWithinAABB(par1Class, par2AxisAlignedBB);
    }
    
    @Override
    public List selectEntitiesWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB, IEntitySelector par3iEntitySelector)
    {
        return super.selectEntitiesWithinAABB(par1Class, par2AxisAlignedBB, par3iEntitySelector);
    }
    
    @Override
    public Entity findNearestEntityWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB, Entity par3Entity)
    {
        return null;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public List getLoadedEntityList()
    {
        return super.getLoadedEntityList();
    }
    
    @Override
    public void markTileEntityChunkModified(int p_147476_1_, int p_147476_2_, int p_147476_3_, TileEntity p_147476_4_)
    {}
    
    @Override
    public int countEntities(Class par1Class)
    {
        return 0;
    }
    
    @Override
    public void addLoadedEntities(List par1List)
    {}
    
    @Override
    public void unloadEntities(List par1List)
    {}
    
    @Override
    public boolean canPlaceEntityOnSide(Block p_147472_1_, int p_147472_2_, int p_147472_3_, int p_147472_4_, boolean p_147472_5_, int p_147472_6_, Entity p_147472_7_, ItemStack p_147472_8_)
    {
        return true;
    }
    
    @Override
    public PathEntity getPathEntityToEntity(Entity par1Entity, Entity par2Entity, float par3, boolean par4, boolean par5, boolean par6, boolean par7)
    {
        return super.getPathEntityToEntity(par1Entity, par2Entity, par3, par4, par5, par6, par7);
    }
    
    @Override
    public PathEntity getEntityPathToXYZ(Entity par1Entity, int par2, int par3, int par4, float par5, boolean par6, boolean par7, boolean par8, boolean par9)
    {
        return super.getEntityPathToXYZ(par1Entity, par2, par3, par4, par5, par6, par7, par8, par9);
    }
    
    @Override
    public int isBlockProvidingPowerTo(int par1, int par2, int par3, int par4)
    {
        return 0;
    }
    
    @Override
    public int getBlockPowerInput(int par1, int par2, int par3)
    {
        return 0;
    }
    
    @Override
    public boolean getIndirectPowerOutput(int par1, int par2, int par3, int par4)
    {
        return false;
    }
    
    @Override
    public int getIndirectPowerLevelTo(int par1, int par2, int par3, int par4)
    {
        return 0;
    }
    
    @Override
    public boolean isBlockIndirectlyGettingPowered(int par1, int par2, int par3)
    {
        return false;
    }
    
    @Override
    public int getStrongestIndirectPower(int par1, int par2, int par3)
    {
        return 0;
    }
    
    @Override
    public EntityPlayer getClosestPlayerToEntity(Entity par1Entity, double par2)
    {
        return super.getClosestPlayerToEntity(par1Entity, par2);
    }
    
    @Override
    public EntityPlayer getClosestPlayer(double par1, double par3, double par5, double par7)
    {
        return super.getClosestPlayer(par1, par3, par5, par7);
    }
    
    @Override
    public EntityPlayer getClosestVulnerablePlayerToEntity(Entity par1Entity, double par2)
    {
        return super.getClosestVulnerablePlayerToEntity(par1Entity, par2);
    }
    
    @Override
    public EntityPlayer getClosestVulnerablePlayer(double par1, double par3, double par5, double par7)
    {
        return super.getClosestVulnerablePlayer(par1, par3, par5, par7);
    }
    
    @Override
    public EntityPlayer getPlayerEntityByName(String par1Str)
    {
        return super.getPlayerEntityByName(par1Str);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void sendQuittingDisconnectingPacket()
    {
        super.sendQuittingDisconnectingPacket();
    }
    
    @Override
    public void checkSessionLock() throws MinecraftException
    {}
    
    @Override
    @SideOnly(Side.CLIENT)
    public void func_82738_a(long par1)
    {}
    
    @Override
    public long getSeed()
    {
        return 1;
    }
    
    @Override
    public long getTotalWorldTime()
    {
        return 1;
    }
    
    @Override
    public long getWorldTime()
    {
        return 1;
    }
    
    @Override
    public void setWorldTime(long par1)
    {}
    
    @Override
    public ChunkCoordinates getSpawnPoint()
    {
        return new ChunkCoordinates(0, 64, 0);
    }
    
    @Override
    public void setSpawnLocation(int par1, int par2, int par3)
    {
        super.setSpawnLocation(par1, par2, par3);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void joinEntityInSurroundings(Entity par1Entity)
    {}
    
    @Override
    public boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int par4)
    {
        return false;
    }
    
    @Override
    public boolean canMineBlockBody(EntityPlayer par1EntityPlayer, int par2, int par3, int par4)
    {
        return false;
    }
    
    @Override
    public void setEntityState(Entity par1Entity, byte par2)
    {}
    
    @Override
    public IChunkProvider getChunkProvider()
    {
        return super.getChunkProvider();
    }
    
    @Override
    public void addBlockEvent(int p_147452_1_, int p_147452_2_, int p_147452_3_, Block p_147452_4_, int p_147452_5_, int p_147452_6_)
    {}
    
    @Override
    public ISaveHandler getSaveHandler()
    {
        return super.getSaveHandler();
    }
    
    @Override
    public WorldInfo getWorldInfo()
    {
        return super.getWorldInfo();
    }
    
    @Override
    public GameRules getGameRules()
    {
        return super.getGameRules();
    }
    
    @Override
    public void updateAllPlayersSleepingFlag()
    {}
    
    @Override
    public float getWeightedThunderStrength(float par1)
    {
        return 0.0F;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void setThunderStrength(float p_147442_1_)
    {}
    
    @Override
    public float getRainStrength(float par1)
    {
        return 0.0F;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void setRainStrength(float par1)
    {}
    
    @Override
    public boolean isThundering()
    {
        return false;
    }
    
    @Override
    public boolean isRaining()
    {
        return false;
    }
    
    @Override
    public boolean canLightningStrikeAt(int par1, int par2, int par3)
    {
        return false;
    }
    
    @Override
    public boolean isBlockHighHumidity(int par1, int par2, int par3)
    {
        return false;
    }
    
    @Override
    public void setItemData(String par1Str, WorldSavedData par2WorldSavedData)
    {}
    
    @Override
    public WorldSavedData loadItemData(Class par1Class, String par2Str)
    {
        return super.loadItemData(par1Class, par2Str);
    }
    
    @Override
    public int getUniqueDataId(String par1Str)
    {
        return super.getUniqueDataId(par1Str);
    }
    
    @Override
    public void playBroadcastSound(int par1, int par2, int par3, int par4, int par5)
    {}
    
    @Override
    public void playAuxSFX(int par1, int par2, int par3, int par4, int par5)
    {}
    
    @Override
    public void playAuxSFXAtEntity(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5, int par6)
    {}
    
    @Override
    public int getHeight()
    {
        return 256;
    }
    
    @Override
    public int getActualHeight()
    {
        return 256;
    }
    
    @Override
    public Random setRandomSeed(int par1, int par2, int par3)
    {
        return super.setRandomSeed(par1, par2, par3);
    }
    
    @Override
    public ChunkPosition findClosestStructure(String p_147440_1_, int p_147440_2_, int p_147440_3_, int p_147440_4_)
    {
        return super.findClosestStructure(p_147440_1_, p_147440_2_, p_147440_3_, p_147440_4_);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean extendedLevelsInChunkCache()
    {
        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public double getHorizon()
    {
        return super.getHorizon();
    }
    
    @Override
    public CrashReportCategory addWorldInfoToCrashReport(CrashReport par1CrashReport)
    {
        return super.addWorldInfoToCrashReport(par1CrashReport);
    }
    
    @Override
    public void destroyBlockInWorldPartially(int p_147443_1_, int p_147443_2_, int p_147443_3_, int p_147443_4_, int p_147443_5_)
    {}
    
    @Override
    public Vec3Pool getWorldVec3Pool()
    {
        return super.getWorldVec3Pool();
    }
    
    @Override
    public Calendar getCurrentDate()
    {
        return super.getCurrentDate();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void makeFireworks(double par1, double par3, double par5, double par7, double par9, double par11, NBTTagCompound par13nbtTagCompound)
    {}
    
    @Override
    public Scoreboard getScoreboard()
    {
        return super.getScoreboard();
    }
    
    @Override
    public void func_147453_f(int p_147453_1_, int p_147453_2_, int p_147453_3_, Block p_147453_4_)
    {}
    
    @Override
    public float func_147462_b(double p_147462_1_, double p_147462_3_, double p_147462_5_)
    {
        return super.func_147462_b(p_147462_1_, p_147462_3_, p_147462_5_);
    }
    
    @Override
    public float func_147473_B(int p_147473_1_, int p_147473_2_, int p_147473_3_)
    {
        return super.func_147473_B(p_147473_1_, p_147473_2_, p_147473_3_);
    }
    
    @Override
    public void func_147450_X()
    {}
    
    @Override
    public void addTileEntity(TileEntity entity)
    {}
    
    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side)
    {
        return y <= 63;
    }
    
    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default)
    {
        return y <= 63;
    }
    
    @Override
    public ImmutableSetMultimap<ChunkCoordIntPair, Ticket> getPersistentChunks()
    {
        return super.getPersistentChunks();
    }
    
    @Override
    public int getBlockLightOpacity(int x, int y, int z)
    {
        return super.getBlockLightOpacity(x, y, z);
    }
    
    @Override
    public int countEntities(EnumCreatureType type, boolean forSpawnCount)
    {
        return 0;
    }
    
    @Override
    public boolean blockExists(int par1, int par2, int par3)
    {
        return false;
    }
    
    @Override
    protected boolean chunkExists(int par1, int par2)
    {
        return false;
    }
    
    @Override
    protected IChunkProvider createChunkProvider()
    {
        return new FakeChunkProvider();
    }
    
    @Override
    public Entity getEntityByID(int i)
    {
        return EntityList.createEntityByID(i, this);
    }
    
    @Override
    public Chunk getChunkFromChunkCoords(int par1, int par2)
    {
        return null;
    }
    
    @Override
    public Block getBlock(int x, int y, int z)
    {
        return y > 63 ? Blocks.air : Blocks.grass;
    }
    
    protected static class FakeWorldProvider extends WorldProvider
    {
        @Override
        protected void generateLightBrightnessTable()
        {}
        
        @Override
        protected void registerWorldChunkManager()
        {
            super.registerWorldChunkManager();
        }
        
        @Override
        public IChunkProvider createChunkGenerator()
        {
            return super.createChunkGenerator();
        }
        
        @Override
        public float calculateCelestialAngle(long par1, float par3)
        {
            return super.calculateCelestialAngle(par1, par3);
        }
        
        @Override
        public int getMoonPhase(long par1)
        {
            return super.getMoonPhase(par1);
        }
        
        @Override
        public boolean isSurfaceWorld()
        {
            return true;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public float[] calcSunriseSunsetColors(float par1, float par2)
        {
            return super.calcSunriseSunsetColors(par1, par2);
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public Vec3 getFogColor(float par1, float par2)
        {
            return super.getFogColor(par1, par2);
        }
        
        @Override
        public boolean canRespawnHere()
        {
            return true;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public float getCloudHeight()
        {
            return super.getCloudHeight();
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public boolean isSkyColored()
        {
            return true;
        }
        
        @Override
        public ChunkCoordinates getEntrancePortalLocation()
        {
            return super.getEntrancePortalLocation();
        }
        
        @Override
        public int getAverageGroundLevel()
        {
            return 63;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public boolean getWorldHasVoidParticles()
        {
            return false;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public double getVoidFogYFactor()
        {
            return super.getVoidFogYFactor();
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public boolean doesXZShowFog(int par1, int par2)
        {
            return false;
        }
        
        @Override
        public void setDimension(int dim)
        {}
        
        @Override
        public String getSaveFolder()
        {
            return null;
        }
        
        @Override
        public String getWelcomeMessage()
        {
            return "";
        }
        
        @Override
        public String getDepartMessage()
        {
            return "";
        }
        
        @Override
        public double getMovementFactor()
        {
            return super.getMovementFactor();
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public IRenderHandler getSkyRenderer()
        {
            return super.getSkyRenderer();
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public void setSkyRenderer(IRenderHandler skyRenderer)
        {
            super.setSkyRenderer(skyRenderer);
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public IRenderHandler getCloudRenderer()
        {
            return super.getCloudRenderer();
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public void setCloudRenderer(IRenderHandler renderer)
        {
            super.setCloudRenderer(renderer);
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public IRenderHandler getWeatherRenderer()
        {
            return super.getWeatherRenderer();
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public void setWeatherRenderer(IRenderHandler renderer)
        {
            super.setWeatherRenderer(renderer);
        }
        
        @Override
        public ChunkCoordinates getRandomizedSpawnPoint()
        {
            return new ChunkCoordinates(0, 64, 0);
        }
        
        @Override
        public boolean shouldMapSpin(String entity, double x, double y, double z)
        {
            return false;
        }
        
        @Override
        public int getRespawnDimension(EntityPlayerMP player)
        {
            return 0;
        }
        
        @Override
        public BiomeGenBase getBiomeGenForCoords(int x, int z)
        {
            return BiomeGenBase.plains;
        }
        
        @Override
        public boolean isDaytime()
        {
            return true;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
        {
            return super.getSkyColor(cameraEntity, partialTicks);
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public Vec3 drawClouds(float partialTicks)
        {
            return super.drawClouds(partialTicks);
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public float getStarBrightness(float par1)
        {
            return super.getStarBrightness(par1);
        }
        
        @Override
        public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful)
        {}
        
        @Override
        public void calculateInitialWeather()
        {}
        
        @Override
        public void updateWeather()
        {}
        
        @Override
        public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
        {
            return false;
        }
        
        @Override
        public boolean canSnowAt(int x, int y, int z, boolean checkLight)
        {
            return false;
        }
        
        @Override
        public void setWorldTime(long time)
        {}
        
        @Override
        public long getSeed()
        {
            return 1;
        }
        
        @Override
        public long getWorldTime()
        {
            return 1;
        }
        
        @Override
        public void setSpawnPoint(int x, int y, int z)
        {
            super.setSpawnPoint(x, y, z);
        }
        
        @Override
        public boolean canMineBlock(EntityPlayer player, int x, int y, int z)
        {
            return false;
        }
        
        @Override
        public boolean isBlockHighHumidity(int x, int y, int z)
        {
            return false;
        }
        
        @Override
        public int getHeight()
        {
            return 256;
        }
        
        @Override
        public int getActualHeight()
        {
            return 256;
        }
        
        @Override
        public double getHorizon()
        {
            return super.getHorizon();
        }
        
        @Override
        public void resetRainAndThunder()
        {}
        
        @Override
        public boolean canDoLightning(Chunk chunk)
        {
            return false;
        }
        
        @Override
        public boolean canDoRainSnowIce(Chunk chunk)
        {
            return false;
        }
        
        @Override
        public String getDimensionName()
        {
            return "";
        }
        
        @Override
        public ChunkCoordinates getSpawnPoint()
        {
            return new ChunkCoordinates(0, 64, 0);
        }
        
        @Override
        public boolean canCoordinateBeSpawn(int par1, int par2)
        {
            return true;
        }
    }
    
    protected static class FakeSaveHandler implements ISaveHandler
    {
        @Override
        public WorldInfo loadWorldInfo()
        {
            return null;
        }
        
        @Override
        public void checkSessionLock()
        {}
        
        @Override
        public IChunkLoader getChunkLoader(WorldProvider var1)
        {
            return null;
        }
        
        @Override
        public void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2)
        {}
        
        @Override
        public void saveWorldInfo(WorldInfo var1)
        {}
        
        @Override
        public IPlayerFileData getSaveHandler()
        {
            return null;
        }
        
        @Override
        public void flush()
        {}
        
        @Override
        public File getWorldDirectory()
        {
            return null;
        }
        
        @Override
        public File getMapFileFromName(String var1)
        {
            return null;
        }
        
        @Override
        public String getWorldDirectoryName()
        {
            return null;
        }
    }
    
    public static class FakeChunkProvider implements IChunkProvider
    {
        @Override
        public boolean chunkExists(int var1, int var2)
        {
            return false;
        }
        
        @Override
        public Chunk provideChunk(int var1, int var2)
        {
            return null;
        }
        
        @Override
        public Chunk loadChunk(int var1, int var2)
        {
            return null;
        }
        
        @Override
        public void populate(IChunkProvider var1, int var2, int var3)
        {}
        
        @Override
        public boolean saveChunks(boolean var1, IProgressUpdate var2)
        {
            return false;
        }
        
        @Override
        public boolean unloadQueuedChunks()
        {
            return false;
        }
        
        @Override
        public boolean canSave()
        {
            return false;
        }
        
        @Override
        public String makeString()
        {
            return null;
        }
        
        @Override
        public List getPossibleCreatures(EnumCreatureType var1, int var2, int var3, int var4)
        {
            return null;
        }
        
        @Override
        public ChunkPosition func_147416_a(World var1, String var2, int var3, int var4, int var5)
        {
            return null;
        }
        
        @Override
        public int getLoadedChunkCount()
        {
            return 0;
        }
        
        @Override
        public void recreateStructures(int var1, int var2)
        {}
        
        @Override
        public void saveExtraData()
        {}
    }
}