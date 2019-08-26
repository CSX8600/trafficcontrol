package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.function.Consumer;

public class ShuntBorderTileEntity extends ShuntBaseTileEntity {

	@Override
	protected <T extends ShuntBaseTileEntity> Consumer<T> getRelayAddOrRemoveShuntMethod(
			RelayTileEntity relayTileEntity) {
		return shuntBase -> relayTileEntity.addOrRemoveShuntBorder((ShuntBorderTileEntity)shuntBase);
	}

}
