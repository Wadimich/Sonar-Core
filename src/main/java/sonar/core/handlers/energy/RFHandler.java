package sonar.core.handlers.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Loader;
import sonar.core.api.energy.EnergyHandler;
import sonar.core.api.energy.EnergyType;
import sonar.core.api.energy.StoredEnergyStack;
import sonar.core.api.utils.ActionType;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;

public class RFHandler extends EnergyHandler {

	public static String name = "RF-Provider";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean canProvideEnergy(TileEntity tile, EnumFacing dir) {
		return tile != null && (tile instanceof IEnergyReceiver || tile instanceof IEnergyProvider);
	}

	@Override
	public void getEnergy(StoredEnergyStack energyStack, TileEntity tile, EnumFacing dir) {
		if (tile == null) {
			return;
		}
		if (tile instanceof IEnergyReceiver) {
			IEnergyReceiver receiver = (IEnergyReceiver) tile;
			energyStack.setStorageValues(receiver.getEnergyStored(dir), receiver.getMaxEnergyStored(dir));
			int simulateAdd = receiver.receiveEnergy(dir, Integer.MAX_VALUE, true);
			energyStack.setMaxInput(simulateAdd);
		}
		if (tile instanceof IEnergyProvider) {
			IEnergyProvider provider = (IEnergyProvider) tile;
			energyStack.setStorageValues(provider.getEnergyStored(dir), provider.getMaxEnergyStored(dir));
			int simulateRemove = provider.extractEnergy(dir, Integer.MAX_VALUE, true);
			energyStack.setMaxOutput(simulateRemove);
		}
	}

	@Override
	public StoredEnergyStack addEnergy(StoredEnergyStack transfer, TileEntity tile, EnumFacing dir, ActionType action) {
		EnumFacing side = dir;
		if (tile instanceof IEnergyReceiver) {
			IEnergyReceiver receiver = (IEnergyReceiver) tile;
			if (side==null || receiver.canConnectEnergy(side = side.getOpposite())) {
				int transferRF = Math.min(receiver.getMaxEnergyStored(dir), transfer.stored < Integer.MAX_VALUE ? (int) transfer.stored : Integer.MAX_VALUE);
				//transfer.stored -= receiver.receiveEnergy(side, transferRF, action.shouldSimulate());
			}
		}
		if (transfer.stored == 0)
			transfer = null;
		return transfer;
	}

	@Override
	public StoredEnergyStack removeEnergy(StoredEnergyStack transfer, TileEntity tile, EnumFacing dir, ActionType action) {
		EnumFacing side = dir;
		if (tile instanceof IEnergyProvider) {
			IEnergyProvider receiver = (IEnergyProvider) tile;			
			if (side==null || receiver.canConnectEnergy(side = side.getOpposite())) {
				int transferRF = Math.min(receiver.getMaxEnergyStored(dir), transfer.stored < Integer.MAX_VALUE ? (int) transfer.stored : Integer.MAX_VALUE);
				transfer.stored -= receiver.extractEnergy(side, transferRF, action.shouldSimulate());
			}
		}
		if (transfer.stored == 0)
			transfer = null;
		return transfer;
	}

	@Override
	public EnergyType getProvidedType() {
		return EnergyType.RF;
	}
}