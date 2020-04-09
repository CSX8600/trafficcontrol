package com.clussmanproductions.trafficcontrol.util;

public class Tuple<A, B>
{
	private A first;
	private B second;
	
	public Tuple(A first, B second)
	{
		this.first = first;
		this.second = second;
	}
	
	public A getFirst() { return first; }
	public B getSecond() { return second; }
	
	@Override
	public int hashCode() {
		int hash = 17;
		hash += 397 * (first == null ? 0 : first.hashCode());
		hash += 397 * (second == null? 0 : second.hashCode());
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
		{
			return false;
		}
		
		if (!(obj instanceof Tuple<?,?>))
		{
			return false;
		}
		
		Tuple<?,?> tuple = (Tuple<?,?>)obj;
		boolean firstEqual = (getFirst() == null && tuple.getFirst() == null) || 
								(getFirst() != null && tuple.getFirst() != null && getFirst().equals(tuple.getFirst()));
		
		boolean secondEqual = (getSecond() == null && tuple.getSecond() == null) || 
								(getSecond() != null && tuple.getSecond() != null && getSecond().equals(tuple.getSecond()));
		
		return firstEqual && secondEqual;
	}
}
