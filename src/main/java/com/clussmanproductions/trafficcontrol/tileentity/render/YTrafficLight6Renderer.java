package com.clussmanproductions.trafficcontrol.tileentity.render;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class YTrafficLight6Renderer extends BaseTrafficLightRenderer {

	@Override
	protected double getBulbZLocation() {
		return 10.3;
	}

	@Override
	protected List<BulbRenderer> getBulbRenderers() {
		return ImmutableList
				.<BulbRenderer>builder()
				.add(new BulbRenderer(12, 11, 1))
				.add(new BulbRenderer(-2, 11, 0)) 
				
				.add(new BulbRenderer(5, 2.5, 2))
				.add(new BulbRenderer(5, -4, 3))
				.build();
	}

}
