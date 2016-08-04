package sonar.core.common.block.properties;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockStateSpecial<T extends TileEntity, S extends IBlockState> extends IBlockState {
	
	public T getTileEntity(World world);
	
	public T getTileEntity();

	public BlockPos getPos();

	public S getWrappedState();
}