import org.junit.Test;

import static org.junit.Assert.*;


public class Tests {

	@Test
	public void testStart()
	{
		Schedule mySchedule = new Schedule();


		Schedule.Job j1 = mySchedule.insert(1);
		Schedule.Job j2 = mySchedule.insert(2);
		Schedule.Job j3 = mySchedule.insert(10);
		Schedule.Job j4 = mySchedule.insert(20);
		Schedule.Job j5 = mySchedule.insert(40);
		Schedule.Job j6 = mySchedule.insert(50);
		
		mySchedule.get(0).requires(j2);
		mySchedule.get(1).requires(j4);
		mySchedule.get(1).requires(j5);
		mySchedule.get(1).requires(j6);
		mySchedule.get(0).start();
		
		assertEquals(52 ,mySchedule.get(0).start());
		assertEquals(50 ,mySchedule.get(1).start());
		assertEquals(0 ,mySchedule.get(2).start());
		assertEquals(0, mySchedule.get(3).start());
		
	}


@Test
public void testBuild()
{
	Schedule mySchedule = new Schedule();

	
	Schedule.Job j1 = mySchedule.insert(5);
	Schedule.Job j2 = mySchedule.insert(10);
	Schedule.Job j3 = mySchedule.insert(15);
	
	assertEquals(j1, mySchedule.get(0));
	assertEquals(j2, mySchedule.get(1));
	assertEquals(j3, mySchedule.get(2));
	

	
}
@Test
public void testRequires()
{
	Schedule mySchedule = new Schedule();


	Schedule.Job j1 = mySchedule.insert(1);
	Schedule.Job j2 = mySchedule.insert(2);
	Schedule.Job j3 = mySchedule.insert(10);
	Schedule.Job j4 = mySchedule.insert(20);
	
	
	
	mySchedule.get(0).requires(j3);
	mySchedule.get(1).requires(j4);
	
	
	assert(mySchedule.get(2).requiredBy.contains(j1));
	assert(mySchedule.get(3).requiredBy.contains(j2));
	
	
}



@Test 

public void testFinish()
{
	Schedule mySchedule = new Schedule();


	Schedule.Job j1 = mySchedule.insert(5);
	Schedule.Job j2 = mySchedule.insert(10);
	Schedule.Job j3 = mySchedule.insert(15);
	Schedule.Job j4 = mySchedule.insert(25);
	Schedule.Job j5 = mySchedule.insert(35);
	Schedule.Job j6 = mySchedule.insert(45);
	Schedule.Job j7 = mySchedule.insert(100);
	
	
	mySchedule.get(0).requires(j5);
	mySchedule.get(0).requires(j3);
	mySchedule.get(1).requires(j6);
	mySchedule.get(1).requires(j4);
	mySchedule.get(0).requires(j7);
	
	assertEquals(105, mySchedule.finish());
	
}

@Test

public void testLoop()
{
	Schedule mySchedule = new Schedule();
	
	Schedule.Job j1 = mySchedule.insert(5);
	Schedule.Job j2 = mySchedule.insert(10);
	Schedule.Job j3 = mySchedule.insert(15);

	mySchedule.get(0).requires(j3);
	mySchedule.get(1).requires(j1);
	mySchedule.get(2).requires(j2);
	
	assertEquals(-1, mySchedule.finish());
	
}

@Test 
public void testStartLoop()
{
	Schedule mySchedule = new Schedule();
	
	Schedule.Job j1 = mySchedule.insert(5);
	Schedule.Job j2 = mySchedule.insert(10);
	Schedule.Job j3 = mySchedule.insert(15);
	Schedule.Job j4 = mySchedule.insert(25);
	Schedule.Job j5 = mySchedule.insert(35);
	

	mySchedule.get(0).requires(j3);
	mySchedule.get(1).requires(j1);
	mySchedule.get(2).requires(j2);
	
	assertEquals(-1, j1.start());
	assertEquals(-1, j2.start());
	assertEquals(-1, j3.start());
}

@Test
public void testNotOnACycle()
{
	Schedule mySchedule = new Schedule();
	
	Schedule.Job j1 = mySchedule.insert(20);
	Schedule.Job j2 = mySchedule.insert(30);
	Schedule.Job j3 = mySchedule.insert(40);
	Schedule.Job j4 = mySchedule.insert(50);
	Schedule.Job j5 = mySchedule.insert(100);
	Schedule.Job j6 = mySchedule.insert(200);
	Schedule.Job j7 = mySchedule.insert(300);

	//This is the loop
	j1.requires(j2);
	j2.requires(j1);
	j2.requires(j3);
	
	//multiple incoming edges to one Job
	j3.requires(j6);
	j3.requires(j5);
	j3.requires(j4);
	
	//Test finish still works and returns the correct value
	assertEquals(-1, mySchedule.finish());
	
	//Ensure edges are still relaxed even with a cycle 
	//and thus will return start times of Jobs not on a cycle
	assertEquals(200, j3.start());
	
	//Returns -1 if  Job is on a loop 
	assertEquals(-1, j2.start());
}

@Test
public void testProvidedExample()
{
	
		Schedule schedule = new Schedule();
		schedule.insert(8); //adds job 0 with time 8
		Schedule.Job j1 = schedule.insert(3); //adds job 1 with time 3
		schedule.insert(5); //adds job 2 with time 5
		assertEquals(8, schedule.finish()); //should return 8, since job 0 takes time 8 to complete.
		/* Note it is not the earliest completion time of any job, but the earliest the entire set can complete. */
		schedule.get(0).requires(schedule.get(2)); //job 2 must precede job
		schedule.finish();
		assertEquals(13, schedule.finish()); //should return 13 (job 0 cannot start until time 5)
		schedule.get(0).requires(j1); //job 1 must precede job 0
		assertEquals(13, schedule.finish()); //should return 13
		assertEquals(5, schedule.get(0).start()); //should return 5
		
		schedule.get(0).start();
		assertEquals(0,j1.start()); //should return 0
		assertEquals(0,schedule.get(2).start()); //should return 0
		j1.requires(schedule.get(2)); //job 2 must precede job 1
		assertTrue(schedule.get(2).requiredBy.contains(j1));
		schedule.finish();
		assertEquals(16,schedule.finish()); //should return 16
		
		assertEquals(8,schedule.get(0).start()); //should return 8
		assertEquals(5,schedule.get(1).start()); //should return 5
		assertEquals(0,schedule.get(2).start()); //should return 0
		schedule.get(1).requires(schedule.get(0)); //job 0 must precede job 1 (creates loop)
		assertEquals(-1,schedule.finish()); //should return -1
		assertEquals(-1, schedule.get(0).start());//Should return -1
		assertEquals(-1,schedule.get(1).start()); //should return -1
 		assertEquals(0,schedule.get(2).start()); //should return 0 (no loops in prerequisites)



}

@Test
public void testEmptyGraph()
{
	Schedule mySchedule = new Schedule();
	
	assertEquals(null,mySchedule.get(0));
	
}

@Test
public void largeCycleTest() {
  Schedule example = new Schedule();
  for(int i = 0; i < 10000; i++) {
    example.insert(10);
  }
  for(int i = 0; i < 9999; i++) {
    example.get(i).requires(example.get(i+1));
  }
  assertEquals(100000, example.finish());
  example.get(9999).requires(example.get(0));
  assertEquals(-1, example.finish());
}

@Test
public void denseGraph() {
int max = 20000;
int min = 1;
int V = 400;
int E = 70000;
Schedule schedule = new Schedule();

for (int i = 0; i < V; i++) {
      int time = (int) (Math.random() * (max - min + 1) + min);
      schedule.insert(time);
}

int count = 0;
for (int j = 0; j < E; j++) {
    count++;
    int smaller = 1;
    int bigger = 0;
    while (smaller >= bigger) {
        smaller = (int) (Math.random() * V);
        bigger = (int) (Math.random() * V);
}
    schedule.get(bigger).requires(schedule.get(smaller));
    if (count % 700 == 0)
        schedule.finish();
    if (count == E - 1)
        schedule.get(smaller).requires(schedule.get(bigger));
}
assertEquals(-1, schedule.finish());
}




}