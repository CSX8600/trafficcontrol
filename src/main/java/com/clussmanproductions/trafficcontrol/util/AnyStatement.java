package com.clussmanproductions.trafficcontrol.util;

import java.util.ArrayList;
import java.util.function.Function;

public class AnyStatement {
	public static <T> boolean Any(ArrayList<T> list, Function<T, Boolean> condition)
	{
		boolean result = false;
		for(T item : list)
		{
			result = result || condition.apply(item);
		}
		
		return result;
	}
}
