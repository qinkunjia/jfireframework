package com.jfireframework.baseutil.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.jfireframework.baseutil.disruptor.ringarray.ComplexMultRingArray;
import com.jfireframework.baseutil.disruptor.ringarray.RingArray;
import com.jfireframework.baseutil.disruptor.ringarray.SimpleMultRingArray;
import com.jfireframework.baseutil.disruptor.waitstrategy.WaitStrategy;

public class Disruptor
{
	public static final int			SimpleMult	= 1;
	public static final int			ComplexMult	= 2;
	private final RingArray			ringArray;
	private volatile int			sureWrite	= 1;
	private final ExecutorService	pool;
	
	public Disruptor(int ringSize, WaitStrategy waitStrategy, EntryAction[] actions, int ringType, ExecutorService pool)
	{
		if (actions[0] instanceof ExclusiveEntryAction)
		{
			for (EntryAction each : actions)
			{
				if (each instanceof ExclusiveEntryAction)
				{
					;
				}
				else
				{
					throw new RuntimeException("action的类型必须都相同");
				}
			}
		}
		else
		{
			for (EntryAction each : actions)
			{
				if (each instanceof SharedEntryAction)
				{
					;
				}
				else
				{
					throw new RuntimeException("action的类型必须都相同");
				}
			}
		}
		if (ringType == SimpleMult)
		{
			ringArray = new SimpleMultRingArray(ringSize, waitStrategy, actions);
		}
		else
		{
			ringArray = new ComplexMultRingArray(ringSize, waitStrategy, actions);
		}
		for (EntryAction action : actions)
		{
			action.setDisruptor(this);
		}
		this.pool = pool;
		for (EntryAction each : actions)
		{
			pool.submit(each);
		}
	}
	
	public Disruptor(int ringSize, WaitStrategy waitStrategy, EntryAction[] actions, int ringType, int ioThreadSize)
	{
		this(ringSize, waitStrategy, actions, ringType, Executors.newFixedThreadPool(ioThreadSize));
	}
	
	protected int nothingButsurewrite()
	{
		return sureWrite;
	}
	
	public void publish(Object data)
	{
		ringArray.publish(data);
	}
	
	public void stop()
	{
		ringArray.stop();
		pool.shutdown();
	}
	
	public boolean stoped()
	{
		return ringArray.stoped();
	}
	
	public RingArray getRingArray()
	{
		return ringArray;
	}
}