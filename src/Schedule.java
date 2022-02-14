import java.io.*;
import java.util.*;


public class Schedule {

	private LinkedList<Job> myJobs = new LinkedList<Job>(); 
	private boolean finished = false;
	private int value;
	
	public Schedule ()
	{
		
		
	}
	
	public Job insert(int time)
	{
		if (time > value)
			value = time;
		
		Job vertex = new Job(time);
		
		myJobs.add(vertex);
		return vertex;
		
	}
	
	public Job get(int index)
	{
		if(!myJobs.isEmpty())
			return myJobs.get(index);

		return null;
	}
	
	
	
	public int finish()
	{
		if (finished == true)
			return value;
		
		
		finished = true;
		Job u;
		Job v;
		int tmp = 0;
		
		LinkedList<Job> topOrder = new LinkedList<>();
		
		topOrder = topologicalSort();
		
		//Check for cycles
		if(topOrder.size() != myJobs.size())
		{
			value = -1;
		}
		
		
		for(int i = 0; i < topOrder.size(); i++)
		{
			
			u = topOrder.get(i);
			
			for(int j = 0; j < u.requiredBy.size(); j++)
			{
				v = u.requiredBy.get(j);
				
				relax(u, v);
				
			}
			
		
			if(tmp < u.discover + u.time)
				tmp = u.discover + u.time;
			
		}
	
		
		if (value != -1)
			value = tmp;		
		
		return value;
		
	}
	
	
	
	
	//Based off of Khan's algorithm 
	private LinkedList<Job> topologicalSort()
	{
		Job tmp;
		Job edge;
		
		Queue<Job> topSort = new LinkedList<Job>(); 
		LinkedList<Job> topOrder = new LinkedList<>();
		
		//Add Vertices to queue with no incoming edges
		for(int i = 0; i < myJobs.size(); i++)
		{
			tmp = myJobs.get(i);
			
			//ResetData in all Jobs for future finish() calls
			tmp.tmpDegree = tmp.inDegree;
			
			if(tmp.inDegree == 0)
			{
				topSort.add(tmp);
			}
		}
	
		//Dequeue all vertices with no incoming edges and decrement the degree of successors 
		while(!topSort.isEmpty())
		{
			//remove from queue
			tmp = topSort.remove();
			
			//Add to topological order
			topOrder.add(tmp);
			
			//Loop that reduces the degree of each vertex that has an incoming edge from tmp 
			for (int j = 0; j < tmp.requiredBy.size(); j++)
			{
				edge = tmp.requiredBy.get(j);
				
				edge.tmpDegree--;
				
				if(edge.tmpDegree == 0)
				{
					topSort.add(edge);
				}
				
			}
			
		}
		
		
		return topOrder;
		
	}
	
	
	
	private void relax(Job u, Job v)
	{
		
			if (u.discover + u.time > v.discover)
			{
				v.discover = u.discover + u.time;	
				v.parent = u;
			}
			return;
	}
	
	
	
	public class Job
	{
		
		private int discover;
		private int inDegree;
		private int tmpDegree;
		private int time;
		private Job parent;
		private boolean flag = false;
		ArrayList<Job> requiredBy = new ArrayList<Job>();
		
		
		private Job() 
		{
			this.time = 0;
			this.discover = 0;
			this.tmpDegree = 0;
			this.inDegree = 0;
			this.parent = null;
		}
		
		private Job(int weight)
		{
			this.time = weight;
			this.discover = 0;
			this.tmpDegree = 0;
			this.inDegree = 0;
			this.parent = null;
		}
		
		public void requires(Job j)
		{
			
			j.requiredBy.add(this);
			inDegree++;
			//this.discover += j.discover + j.time;
		
			if (this.discover < j.discover + j.time)
			{
				Job tmp = this;
				Job tmp2;
				this.discover = j.discover + j.time;
				this.parent = j;
				this.flag = true;
				
				for(int i = 0; i < tmp.requiredBy.size(); i++)
				{
					tmp2 = tmp.requiredBy.get(i);
					tmp2.discover = tmp.discover + tmp.time;
					
					if((tmp.discover + tmp.time) > value && (value != -1))
						value = tmp.discover + tmp.time;
					
				
				}
				
			
			}
				this.flag = false;
			
			return;
		}
		
		public int start()
		{
			
				finish();
		
			
			if (tmpDegree == 0)
				return discover;
			else
				return -1;
			
		}
		
	}
}
