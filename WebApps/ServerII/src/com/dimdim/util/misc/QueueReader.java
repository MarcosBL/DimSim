/**
 * 
 */
package com.dimdim.util.misc;

/**
 * @author Administrator
 *
 */
public class QueueReader implements	Runnable
{
	protected	SimpleQueue 	queue;
	
	public	QueueReader(SimpleQueue queue)
	{
		this.queue = queue;
	}
	
	public	void	run()
	{
		while (true)
		{
			System.out.println("Trying to read from the queue - 5 seconds timeout");
			Integer i = (Integer)queue.get(5000);
			System.out.println("Got from the queue:"+i);
			System.out.println("Trying to read from the queue - no timeout");
			i = (Integer)queue.get();
			System.out.println("Got from the queue:"+i);
		}
	}
	
	public	static	void	main(String[] args)
	{
		try
		{
			System.out.println("Hello World - I am testing a queue");
			
			SimpleQueue sq = new SimpleQueue();
			Integer i = (Integer)sq.get(5000);
			System.out.println("Received: "+i);
			
			sq.put(new Integer(1));
			sq.put(new Integer(2));
			sq.put(new Integer(3));
			sq.put(new Integer(4));
			sq.put(new Integer(5));
			sq.put(new Integer(6));
			
			i = (Integer)sq.get();
			System.out.println("Received: "+i);
			i = (Integer)sq.get();
			System.out.println("Received: "+i);
			i = (Integer)sq.get();
			System.out.println("Received: "+i);
			i = (Integer)sq.get();
			System.out.println("Received: "+i);
			i = (Integer)sq.get();
			System.out.println("Received: "+i);
			i = (Integer)sq.get();
			System.out.println("Received: "+i);
			i = (Integer)sq.get(5000);
			System.out.println("Received: "+i);
			
			QueueReader qr = new QueueReader(sq);
			Thread t = new Thread(qr);
			t.start();
			
			for (int j=0; j<20; j++)
			{
				System.out.println("Writing to the queue:"+(j+20));
				sq.put(new Integer(j+20));
				try { Thread.sleep(20); } catch(Exception e) { }
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
