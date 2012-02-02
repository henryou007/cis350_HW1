package edu.upenn.cis350.gpx;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class GPXcalculatorTest {
	
	private static final double delta = 0.00001; //used as a parameter for delta
	
	//Normal points
	private GPXtrkpt pt0;
	private GPXtrkpt pt1;
	private GPXtrkpt pt2;
	private GPXtrkpt pt3;
	private GPXtrkpt pt4;
	
	//Normal segments
	private GPXtrkseg seg0;//from 0 -> 1 -> 2
	private GPXtrkseg seg1;//from 3->4
	
	//Normal track
	private GPXtrk trk0;
	
	
	//Boundary points
	private GPXtrkpt pt_lat_90;
	private GPXtrkpt pt_lat_neg90;
	private GPXtrkpt pt_lon_180;
	private GPXtrkpt pt_lon_neg180;
	
	private GPXtrkpt pt_lat_91;
	private GPXtrkpt pt_lat_neg91;
	private GPXtrkpt pt_lon_181;
	private GPXtrkpt pt_lon_neg181;	


	//Set up the points, segment needed
	@Before
	public void setUp() throws Exception{
		//set up normal points
		this.pt0 = new GPXtrkpt(3,4,new Date());
		this.pt1 = new GPXtrkpt(0,0,new Date());
		this.pt2 = new GPXtrkpt(3,4,new Date());
		this.pt3 = new GPXtrkpt(-1,-100,new Date());
		this.pt4 = new GPXtrkpt(5,-92,new Date());
		
		//set up boundary points
		this.pt_lat_90 = new GPXtrkpt(90,0,new Date());
		this.pt_lat_neg90 = new GPXtrkpt(-90,0,new Date());
		this.pt_lon_180 = new GPXtrkpt(180,0,new Date());
		this.pt_lon_neg180 = new GPXtrkpt(-180,0,new Date());
		
		this.pt_lat_91 = new GPXtrkpt(91,0,new Date());
		this.pt_lat_neg91 = new GPXtrkpt(-91,0,new Date());
		this.pt_lon_181 = new GPXtrkpt(181,0,new Date());
		this.pt_lon_neg181 = new GPXtrkpt(-181,0,new Date());
		
		//set up normal segments
		ArrayList<GPXtrkpt> pt_list0 = new ArrayList<GPXtrkpt>();
		pt_list0.add(pt0);
		pt_list0.add(pt1);
		pt_list0.add(pt2);
		this.seg0 = new GPXtrkseg(pt_list0);
		
		ArrayList<GPXtrkpt> pt_list1 = new ArrayList<GPXtrkpt>();
		pt_list1.add(pt3);
		pt_list1.add(pt4);
		this.seg1 = new GPXtrkseg(pt_list1);
		
		ArrayList<GPXtrkseg> seg_list0 = new ArrayList<GPXtrkseg>();
		seg_list0.add(this.seg0);
		seg_list0.add(this.seg1);
		this.trk0 = new GPXtrk("gpx_test", seg_list0);
	}
	
	//Normal Operation
	@Test
	public void testNormalOperation(){
		assertEquals(20,GPXcalculator.calculateDistanceTraveled(trk0),delta);
	}
	
	//Error handling
	/**
	 * Test case: when GPXtrk is null, the method should return -1
	 */
	@Test
	public void testNullGPXtrk(){
		assertEquals(-1,GPXcalculator.calculateDistanceTraveled(null),delta);
	}
	
	/**
	 * Test case: when GPXtrk contains no GPXtrkseg, the method should return -1
	 */
	@Test
	public void testEmptyGPXtrk(){
		ArrayList<GPXtrkseg> empty_seg_list = new ArrayList<GPXtrkseg>();
		GPXtrk test = new GPXtrk("test",empty_seg_list);
		assertEquals(-1,GPXcalculator.calculateDistanceTraveled(test),delta);
	}
	
	/**
	 * Test case: If GPXtrkseg in GPXtrk is null (and is the only element in the GPXtrk),
	 *  the distance traveled for that GPXtrkseg should be considered 0
	 */
	@Test
	public void testOnlyOneNullGPXtrkseg(){
		ArrayList<GPXtrkseg> seg_list = new ArrayList<GPXtrkseg>();
		seg_list.add(null);
		GPXtrk test = new GPXtrk("test",seg_list);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(test),delta);
	}
	
	
	/**
	 * Test case: If GPXtrkseg in GPXtrk is null (and is not the only element in the GPXtrk),
	 *  the distance traveled for that GPXtrkseg should be considered 0
	 */
	@Test
	public void testNullGPXtrkseg(){
		ArrayList<GPXtrkseg> seg_list_temp = new ArrayList<GPXtrkseg>();
		seg_list_temp.add(null);
		seg_list_temp.add(seg0);
		GPXtrk test = new GPXtrk("test",seg_list_temp);
		assertEquals(10,GPXcalculator.calculateDistanceTraveled(test),delta);
	}
	
	/**
	 * Test case: If GPXtrkseg contains only one GPXtrkpt, 
	 * the distance traveled for that GPXtrkseg should be considered 0
	 */
	@Test
	public void testSingleGPXtrkpt(){
		ArrayList<GPXtrkpt> pt_list_temp = new ArrayList<GPXtrkpt>();
		pt_list_temp.add(this.pt0);
		GPXtrkseg seg_temp = new GPXtrkseg(pt_list_temp);
		ArrayList<GPXtrkseg> seg_list_temp = new ArrayList<GPXtrkseg>();
		seg_list_temp.add(seg_temp);
		GPXtrk test = new GPXtrk("test",seg_list_temp);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(test),delta);
	}
	
	/**
	 * Test case: If any GPXtrkpt(and is the only point) in a GPXtrkseg is null , 
	 * the distance traveled for that GPXtrkseg should be considered 0
	 */
	@Test
	public void testNullGPXtrkpt(){
		ArrayList<GPXtrkpt> pt_list_temp = new ArrayList<GPXtrkpt>();
		pt_list_temp.add(null);
		GPXtrkseg seg_temp = new GPXtrkseg(pt_list_temp);
		ArrayList<GPXtrkseg> seg_list_temp = new ArrayList<GPXtrkseg>();
		seg_list_temp.add(seg_temp);
		GPXtrk test = new GPXtrk("test",seg_list_temp);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(test),delta);
	}
	
	/**
	 * Test case: If any GPXtrkpt in a GPXtrkseg is null, 
	 * the distance traveled for that GPXtrkseg should be considered 0
	 */
	@Test
	public void testNullGPXtrkptWithOtherpts(){
		ArrayList<GPXtrkpt> pt_list_temp = new ArrayList<GPXtrkpt>();
		pt_list_temp.add(null);
		pt_list_temp.add(this.pt0);
		pt_list_temp.add(this.pt1);
		GPXtrkseg seg_temp = new GPXtrkseg(pt_list_temp);
		ArrayList<GPXtrkseg> seg_list_temp = new ArrayList<GPXtrkseg>();
		seg_list_temp.add(seg_temp);
		GPXtrk test = new GPXtrk("test",seg_list_temp);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(test),delta);
	}
	
	
	

	//Boundary condition
	/**
	 * Test case: If any GPXtrkpt in a GPXtrkseg has a latitude that is greater than 90 or less than -90,
	 * the distance traveled for that GPXtrkseg should be considered 0.
	 */
	@Test
	public void testLatitudeBound(){
		//Test the valid boundary
		ArrayList<GPXtrkpt> pt_list_temp0 = new ArrayList<GPXtrkpt>();
		pt_list_temp0.add(this.pt_lat_90);
		pt_list_temp0.add(this.pt1);
		GPXtrkseg seg_temp0 = new GPXtrkseg(pt_list_temp0);
		ArrayList<GPXtrkseg> seg_list_temp0 = new ArrayList<GPXtrkseg>();
		seg_list_temp0.add(seg_temp0);
		GPXtrk test_temp0 = new GPXtrk("test0",seg_list_temp0);
		
		ArrayList<GPXtrkpt> pt_list_temp1 = new ArrayList<GPXtrkpt>();
		pt_list_temp1.add(this.pt_lat_neg90);
		pt_list_temp1.add(this.pt1);
		GPXtrkseg seg_temp1 = new GPXtrkseg(pt_list_temp1);
		ArrayList<GPXtrkseg> seg_list_temp1 = new ArrayList<GPXtrkseg>();
		seg_list_temp1.add(seg_temp1);
		GPXtrk test_temp1 = new GPXtrk("test1",seg_list_temp1);
		
		//Test exceeding boundary
		ArrayList<GPXtrkpt> pt_list_temp2 = new ArrayList<GPXtrkpt>();
		pt_list_temp2.add(this.pt_lat_91);
		pt_list_temp2.add(this.pt1);
		GPXtrkseg seg_temp2 = new GPXtrkseg(pt_list_temp2);
		ArrayList<GPXtrkseg> seg_list_temp2 = new ArrayList<GPXtrkseg>();
		seg_list_temp2.add(seg_temp2);
		GPXtrk test_temp2 = new GPXtrk("test2",seg_list_temp2);
		
		
		ArrayList<GPXtrkpt> pt_list_temp3 = new ArrayList<GPXtrkpt>();
		pt_list_temp3.add(this.pt_lat_neg91);
		pt_list_temp3.add(this.pt1);
		GPXtrkseg seg_temp3 = new GPXtrkseg(pt_list_temp3);
		ArrayList<GPXtrkseg> seg_list_temp3 = new ArrayList<GPXtrkseg>();
		seg_list_temp3.add(seg_temp3);
		GPXtrk test_temp3 = new GPXtrk("test3",seg_list_temp3);
	
		assertEquals(90,GPXcalculator.calculateDistanceTraveled(test_temp0),delta);
		assertEquals(90,GPXcalculator.calculateDistanceTraveled(test_temp1),delta);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(test_temp2),delta);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(test_temp3),delta);
		
	}
	
	
	//Boundary condition
	/**
	 * Test case: If any GPXtrkpt in a GPXtrkseg has a longitude that is greater than 180 or less than -180,
	 * the distance traveled for that GPXtrkseg should be considered 0.
	 */
	@Test
	public void testLongitudeBound(){
		//Test the valid boundary
		ArrayList<GPXtrkpt> pt_list_temp0 = new ArrayList<GPXtrkpt>();
		pt_list_temp0.add(this.pt_lon_180);
		pt_list_temp0.add(this.pt1);
		GPXtrkseg seg_temp0 = new GPXtrkseg(pt_list_temp0);
		ArrayList<GPXtrkseg> seg_list_temp0 = new ArrayList<GPXtrkseg>();
		seg_list_temp0.add(seg_temp0);
		GPXtrk test_temp0 = new GPXtrk("test0",seg_list_temp0);
		
		ArrayList<GPXtrkpt> pt_list_temp1 = new ArrayList<GPXtrkpt>();
		pt_list_temp1.add(this.pt_lon_neg180);
		pt_list_temp1.add(this.pt1);
		GPXtrkseg seg_temp1 = new GPXtrkseg(pt_list_temp1);
		ArrayList<GPXtrkseg> seg_list_temp1 = new ArrayList<GPXtrkseg>();
		seg_list_temp1.add(seg_temp1);
		GPXtrk test_temp1 = new GPXtrk("test1",seg_list_temp1);
		
		//Test exceeding boundary
		ArrayList<GPXtrkpt> pt_list_temp2 = new ArrayList<GPXtrkpt>();
		pt_list_temp2.add(this.pt_lon_181);
		pt_list_temp2.add(this.pt1);
		GPXtrkseg seg_temp2 = new GPXtrkseg(pt_list_temp2);
		ArrayList<GPXtrkseg> seg_list_temp2 = new ArrayList<GPXtrkseg>();
		seg_list_temp2.add(seg_temp2);
		GPXtrk test_temp2 = new GPXtrk("test2",seg_list_temp2);
		
		
		ArrayList<GPXtrkpt> pt_list_temp3 = new ArrayList<GPXtrkpt>();
		pt_list_temp3.add(this.pt_lon_neg181);
		pt_list_temp3.add(this.pt1);
		GPXtrkseg seg_temp3 = new GPXtrkseg(pt_list_temp3);
		ArrayList<GPXtrkseg> seg_list_temp3 = new ArrayList<GPXtrkseg>();
		seg_list_temp3.add(seg_temp3);
		GPXtrk test_temp3 = new GPXtrk("test3",seg_list_temp3);
	
		assertEquals(180,GPXcalculator.calculateDistanceTraveled(test_temp0),delta);
		assertEquals(180,GPXcalculator.calculateDistanceTraveled(test_temp1),delta);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(test_temp2),delta);
		assertEquals(0,GPXcalculator.calculateDistanceTraveled(test_temp3),delta);
		
	}
}
