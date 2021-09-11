package com.clussmanproductions.trafficcontrol.tileentity.render;

import java.util.List;
import com.google.common.collect.ImmutableList;

public class TrafficLightHorizontalRenderer extends BaseTrafficLightRenderer
{
    @Override
    protected double getBulbZLocation() {
        return 10.1;
    }

    @Override
    protected List<BulbRenderer> getBulbRenderers() {
        return ImmutableList
                .<BulbRenderer>builder()
                .add(new BulbRenderer(5.2, 9, 0))
                .add(new BulbRenderer(5.2, 2.5, 1))
                .add(new BulbRenderer(5.2, -4, 2))
                .build();
    }
}
