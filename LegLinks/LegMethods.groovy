import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;
import eu.mihosoft.vrl.v3d.parametrics.*;
import com.neuronrobotics.bowlerstudio.creature.ICadGenerator;
import com.neuronrobotics.bowlerstudio.creature.CreatureLab;
import org.apache.commons.io.IOUtils;
import com.neuronrobotics.bowlerstudio.vitamins.*;
import javafx.scene.paint.Color;
import eu.mihosoft.vrl.v3d.Transform;
import com.neuronrobotics.bowlerstudio.physics.TransformFactory;

class legPiece{

/*
 * A base template for a link. Has no connector connection subtracted yet
 */
public ArrayList<CSG> createShoulder(CSG servo, int xLength){
HashMap<String, Object>  vitaminData = Vitamins.getConfiguration( "hobbyServo","hv6214mg") //replace with servo type 
//println vitaminData

ArrayList<CSG> parts = new ArrayList<CSG>()

//these numbers can be used as universal reference numbers
int servoX = vitaminData.get("flangeLongDimention")//32
int servoY = vitaminData.get("servoThinDimentionThickness")//11.8
int servoZ = vitaminData.get("servoShaftSideHeight")//31.5

servo = servo								.scalex(1.08)
										.rotz(90)
										.rotx(180)
										.movez(-6.3)
										.movex(servoX/2 + (xLength - 80)/2)

//create the main part of the leg that will have an indent in the shape of the servo
CSG sub1 = new Cube(servoX+3, servoY+1.5, servoZ).toCSG().movex(servoX/3 + (xLength - 80)/2 -1.5).movez(7.9)
CSG screwHole = new Cylinder(1.25,10,(int)25).toCSG()//move then subtract
								  .movex(-9 + (xLength - 80)/2)
								  .movey(-5)
								  .movez(-20)


//CSG mainLeg = new Cube(xLength, servoY*2, servoZ+1).toCSG().movez(1).movex(11) 

CSG mainLeg = new RoundedCube(xLength, servoY*2, servoZ+1).cornerRadius(0.3).toCSG().movez(1).movex(11) 
mainLeg =mainLeg.difference(servo)
mainLeg =mainLeg.difference(sub1)
mainLeg =mainLeg.difference(screwHole)
screwHole = screwHole.movey(10)
mainLeg =mainLeg.difference(screwHole)
screwHole = screwHole.movey(20.1+1-10).movex(20)
mainLeg =mainLeg.difference(screwHole)
screwHole = screwHole.movey(-20.1+9-10-20.1+9)//20.1 is servo2
mainLeg =mainLeg.difference(screwHole)
	

//union barriers that will stop the cap (below) from moving onto the main leg
/*
CSG barrier1 = new RoundedCube(2, servoY*2-1, 2).cornerRadius(0.2).toCSG()
								.movez(servoZ/2+2)
								.movex(-xLength/6.8 + (xLength - 80)/2-1.5)
CSG barrier2 = new RoundedCube(2, servoY*2-1, 2).cornerRadius(0.2).toCSG()
								.movez(servoZ/2+2)
								.movex(47.65 + (xLength - 80)/2)
mainLeg = mainLeg.union(barrier1).union(barrier2)
*/

//create the cap that will encapsulate the servo
int capLength = 46.3 + xLength/6.8 -1 ;
CSG cap = new Cube(capLength, servoY*8/3.5, 5).toCSG()
							.movez(servoZ/2+2.5)
							.movex((xLength - 80)+ capLength/2 - 12.5)
CSG capSide1 = new Cube(capLength, 5, servoZ/2).toCSG()
								.movex((xLength - 80)+ capLength/2 - 12.5)
								.movey(servoY+2.5)
								.movez(servoZ/4+5)
CSG capSide2 = new Cube(capLength, 5, servoZ/2).toCSG()
								.movex((xLength - 80)+ capLength/2 - 12.5)
								.movey(-servoY-2.5)
								.movez(servoZ/4+5)
cap = cap.union(capSide1)
cap = cap.union(capSide2)
cap = cap.movex(0.5)
					
//move for visibility
cap = cap.movez(200)

//add parts to the arraylist of parts
parts.add(mainLeg);
parts.add(servo.setColor(javafx.scene.paint.Color.CYAN).movex(-servoX/2).movez(200))//delete
parts.add(cap)
//parts.add(screwHole)

return parts

}


/*
 * Creates a link that will rotate parallel with the link attached to it
 * Uses the base design of shoulder above
 */
public ArrayList<CSG> createThigh(CSG servo, CSG hornRef, int xLength, CSG connector){

	//Recreation of the CSGs from the first part, mainLeg
	ArrayList<CSG> shoulderParts = createShoulder(servo, xLength)
	CSG mainThigh = shoulderParts.get(0)
	CSG cap2 = shoulderParts.get(2)
	CSG servo1 = shoulderParts.get(1)//delete
	connector = connector.movez(18)
	
	HashMap<String, Object>  vitaminData = Vitamins.getConfiguration( "hobbyServo","towerProMG91")
	HashMap<String, Object>  vitaminData2 = Vitamins.getConfiguration( "hobbyServo","hv6214mg")//current servo
	int servoX = vitaminData.get("flangeLongDimention")
	int servoY = vitaminData.get("servoThinDimentionThickness")
	int servoZ = vitaminData.get("servoShaftSideHeight")
	int servoY2 = vitaminData2.get("servoThinDimentionThickness")//20.1
	
	LengthParameter connectorLength = new LengthParameter("Length of Leg",70,[150,60])
	connector = connector.movez(9)
	ArrayList<CSG> parts = new ArrayList<CSG>()
	mainThigh = mainThigh.movex(xLength + 25*xLength/80) //two links are 5/16*xLength length apart
						   		.movez(-13)
						   		.movez(13)

	CSG screwHole = new Cylinder(1.25,30,(int)25).toCSG()//move then subtract
								  .movex(-9 + (xLength - 80)/2)
								  .movey(-5)
								  .movez(-20)
								  .movex(xLength + 25*xLength/80)
								  .movez(-20)
								  
	servo1 = servo1.movex(xLength + 25*xLength/80)
	//replicates parts from mainLeg part to have a similar thigh
	cap2 = cap2.movex((connectorLength.getMM()+33.5) + (xLength - 80)/2)


	int bottMainLeng = 3+xLength/2+(xLength-80)/2//xLength*2.7/5
	CSG bottomCap = new Cube(bottMainLeng, servoY2*2+1, 13).toCSG()
							.movez(servoZ/2+2.5+2.5)
							.toXMin()
							.movex(15 + (xLength - 80)/2)
	CSG capSide1 = new Cube(bottMainLeng, 5, servoZ/2+10).toCSG() 
								.toXMin()
								.movex(15 + (xLength - 80)/2)
								.movey(servoY2+2.6)
								.movez(servoZ/4+9)
	CSG capSide2 = new Cube(bottMainLeng, 5, servoZ/2+10).toCSG() 
								.toXMin()
								.movex(15 + (xLength - 80)/2)
								.movey(-servoY2-2.6)
								.movez(servoZ/4+9)
	
	bottomCap = bottomCap.union(capSide1)
	bottomCap = bottomCap.union(capSide2)
	CSG bottCap2 = new Cube(xLength, 5+servoY2*2, 5).toCSG()
								.toXMin()
								.movez(servoZ/2+6+4.25+1.25)
								.movex(15 + (xLength - 80)/2)
	bottomCap = bottomCap.union(bottCap2)
	
	//see original value declaration above
	bottomCap = bottomCap
				.movez(-60)
				.rotx(180)
				.movez(-73.5)
	bottomCap = bottomCap.toXMin()
					 .movex(xLength/2+11 + 25*xLength/80)//figure this out
	

	//the flat end part of the cap with no sides (to keep connector in place



	CSG connHole = new Cylinder(3.45,4.5,(int)50).toCSG()
								.movez(-19.5)
								.movex(33)

	connHole = connHole.toXMin()
				    .toZMin()
				    .movex(166+ 3.6/2*(xLength-100))//to be accounted for paramaterizing
				    .movez(-32.5)

	bottomCap = bottomCap.movez(8)
					 .union(connHole)

	bottomCap = bottomCap
				.difference(connector)
	mainThigh = mainThigh.difference(connector)

	CSG screwHole2 = new Cylinder(3,30,(int)23).toCSG()//move then subtract
								  .movex(-9 + (xLength - 80)/2)
								  .movey(-5)
								  .movez(-20)
								  .movex(xLength + 25*(xLength/80))
								  .movez(-40)
								  .movey(servoY2+1).movex(20)
	//holes below so that cap can be screwed in
	bottomCap = bottomCap.difference(screwHole)
	screwHole = screwHole.movey(10)
	bottomCap = bottomCap.difference(screwHole)
	screwHole = screwHole.movey(servoY2+1-10).movex(20+(-xLength+80)/80)//+20*xLength/80)
	bottomCap = bottomCap.difference(screwHole)
	screwHole = screwHole.movey(-servoY2+9-10-servoY2+9)
	bottomCap = bottomCap.difference(screwHole)
	
		bottomCap = bottomCap.difference(screwHole2)
		screwHole2 = screwHole2.movey(-servoY2+9-10-servoY2+9)
		bottomCap = bottomCap.difference(screwHole2)
	
	
	//visibility
	bottomCap = bottomCap.movez(-30)

	
							
 	parts.add(mainThigh)
 	parts.add(cap2)
 	parts.add(servo1)//delete
 	parts.add(bottomCap)
 	parts.add(connector.movez(-5).movez(-3.5).movex(128))

	return parts

}

public CSG createCap(CSG servo, CSG hornRef, int xLength){

	HashMap<String, Object>  vitaminData = Vitamins.getConfiguration( "hobbyServo","towerProMG91")
	HashMap<String, Object>  vitaminData2 = Vitamins.getConfiguration( "hobbyServo","hv6214mg")//current servo
	int servoX = vitaminData.get("flangeLongDimention")
	int servoY = vitaminData.get("servoThinDimentionThickness")
	int servoZ = vitaminData.get("servoShaftSideHeight")
	int servoY2 = vitaminData2.get("servoThinDimentionThickness")//20.1
	
	CSG screwHole = new Cylinder(1.25,30,(int)25).toCSG()//move then subtract
								  .movex(-9 + (xLength - 80)/2)
								  .movey(-5)
								  .movez(-20)
								  .movex(xLength + 25*xLength/80)
								  .movez(-20)
								  
	int bottMainLeng = 3+xLength/2+(xLength-80)/2//xLength*2.7/5
	CSG bottomCap = new Cube(bottMainLeng, servoY2*2+1, 13).toCSG()
							.movez(servoZ/2+2.5+2.5)
							.toXMin()
							.movex(15 + (xLength - 80)/2)
	CSG capSide1 = new Cube(bottMainLeng, 5, servoZ/2+10).toCSG() 
								.toXMin()
								.movex(15 + (xLength - 80)/2)
								.movey(servoY2+2.6)
								.movez(servoZ/4+9)
	CSG capSide2 = new Cube(bottMainLeng, 5, servoZ/2+10).toCSG() 
								.toXMin()
								.movex(15 + (xLength - 80)/2)
								.movey(-servoY2-2.6)
								.movez(servoZ/4+9)
	
	bottomCap = bottomCap.union(capSide1)
	bottomCap = bottomCap.union(capSide2)
	CSG bottCap2 = new Cube(xLength, 5+servoY2*2, 5).toCSG()
								.toXMin()
								.movez(servoZ/2+6+4.25+1.25)
								.movex(15 + (xLength - 80)/2)
	bottomCap = bottomCap.union(bottCap2)
	
	//see original value declaration above
	bottomCap = bottomCap
				.movez(-60)
				.rotx(180)
				.movez(-73.5)
	bottomCap = bottomCap.toXMin()
					 .movex(xLength/2+11 + 25*xLength/80)//figure this out

	CSG connHole = new Cylinder(3.45,4.5,(int)50).toCSG()
								.movez(-19.5)
								.movex(33)

	connHole = connHole.toXMin()
				    .toZMin()
				    .movex(166+ 3.6/2*(xLength-100))//to be accounted for paramaterizing
				    .movez(-32.5)

	bottomCap = bottomCap.movez(8)
					 .union(connHole)

	CSG screwHole2 = new Cylinder(3,30,(int)23).toCSG()//move then subtract
								  .movex(-9 + (xLength - 80)/2)
								  .movey(-5)
								  .movez(-20)
								  .movex(xLength + 25*(xLength/80))
								  .movez(-40)
								  .movey(servoY2+1).movex(20)
	//holes below so that cap can be screwed in
	bottomCap = bottomCap.difference(screwHole)
	screwHole = screwHole.movey(10)
	bottomCap = bottomCap.difference(screwHole)
	screwHole = screwHole.movey(servoY2+1-10).movex(20+(-xLength+80)/80)//+20*xLength/80)
	bottomCap = bottomCap.difference(screwHole)
	screwHole = screwHole.movey(-servoY2+9-10-servoY2+9)
	bottomCap = bottomCap.difference(screwHole)
	
	bottomCap = bottomCap.difference(screwHole2)
	screwHole2 = screwHole2.movey(-servoY2+9-10-servoY2+9)
	bottomCap = bottomCap.difference(screwHole2)
	
	
	//visibility
	//bottomCap = bottomCap.movez(-30)

	return bottomCap.movex(-(xLength + 25*xLength/80)-11).movex(-17)
	
}
/*
 * Creates the generic connector that will connect any 2 links
 */
public CSG createConnector(CSG servo, CSG hornRef, int xLength){

	//Recreation of the CSGs from the first part, mainLeg
	HashMap<String, Object>  vitaminData = Vitamins.getConfiguration( "hobbyServo","towerProMG91")
	HashMap<String, Object>  vitaminData2 = Vitamins.getConfiguration( "hobbyServoHorn","hv6214mg_1")
	//print("horn" + vitaminData2)
	int hornRad = vitaminData2.get("hornBaseDiameter")//13.48
	int hornThick = vitaminData2.get("hornThickness")//6.6
	int hornLeng = vitaminData2.get("hornLength")//23.0
	int servoX = vitaminData.get("flangeLongDimention")//32
	int servoY = vitaminData.get("servoThinDimentionThickness")//11.8
	
	LengthParameter connectorLength = new LengthParameter("Length of Leg",70,[150,60])

	int thickness = 10
								
	CSG connector = new Cube((xLength - 80)/2 + 61, servoY*8/7,thickness).toCSG()
								   .movez(-18.5+thickness/5)
				 				   .toXMin()
				 				   .movex(20)
				 				   
	//fancifying, could be removed if we want the connectors shorter
	CSG decor1 = new Cylinder(hornRad+1,thickness,(int)50).toCSG()
								.movez(-21.5)
								.movex(24+hornRad-7.7)
	CSG decor2 = new Cylinder(hornRad/2+2,hornRad/2+2,thickness,(int)50).toCSG()
								.movez(-21.5)
								.movex(37+hornRad-7.7)
	connector = connector.union(decor1)
				 .union(decor2)
				 
	//keyHole is to be subtracted: connHole subtracted from horn (the cylinder hole)
	int cylVal = 4
	CSG connHole = new Cylinder(cylVal,cylVal,4.5,(int)50).toCSG()
								.movez(-19.5)
								.movex(33)
								
	//subtracting the correct horn from the connector
	CSG hornCube = new Cube(14,11,10).toCSG().toZMin().toYMin()// 12 to adjust horn subtraction
	hornRef = hornRef.makeKeepaway(0.5)
	CSG halfHorn = hornRef.intersect(hornCube)
	halfHorn = halfHorn.rotz(90).movex(servoX).movez(-17)
	hornRef = hornRef.rotz(90).movex(servoX).movez(-17)
	CSG keyHole = connHole.union(hornRef).union(halfHorn.movez(3)).makeKeepaway(2)
					.movex(-(8))
	
	connector = connector
				 .difference(keyHole.movez(1))//moved subtraction up 1
				 .movez(-10)

	connHole = connHole.movez(-12)
				.movex(-(8.5)) 
				
	connector = connector.difference(connHole)

	int endLength = (xLength - 80)/2 + 61 +20
	
	CSG connectorEnd1 = new Cylinder(2,2,thickness+2,(int)50).toCSG()
									.movex(endLength)
									.movey(6)
						   			.movez(-32.5)
	CSG connectorEnd2 = new Cylinder(2,2,thickness+2,(int)50).toCSG()
									.movex(endLength)
									.movey(-6)
						   			.movez(-32.5)
	CSG connectorEnds = connectorEnd1.hull(connectorEnd2)
							.movez(1)
	
	CSG endCyl = new Cylinder(2,2,14,(int)40).toCSG()
			.rotx(90)
			.movez(-20)
			.movex(endLength)
			.movey(-7)
			 connectorEnds = connectorEnds.hull(endCyl)
	connector = connector.union(connectorEnds)
	connector = connector
		 connector.setManufactuing({CSG arg0 ->
 				return arg0.toZMin()
 })
 
	connector.setParameter(connectorLength)// add any parameters that are not used to create a primitive
		    .setRegenerate({ createConnector(Vitamins.getConfiguration( "hobbyServo","towerProMG91"))})
		    
	connector = connector.movez(-20).toXMin().movex(servoX-10 + (xLength - 80)/2)

	
	return connector
}

/*
 * Creates a link that will rotate perpendicular with the link attached to it
 * Uses the base design of shoulder above
 */
public ArrayList<CSG> rotatedLegLink(CSG servo, CSG hornRef, int xLength){

	//Recreation of the CSGs from the first part, mainLeg
	HashMap<String, Object>  vitaminData = Vitamins.getConfiguration( "hobbyServo","towerProMG91")
	int servoX = vitaminData.get("flangeLongDimention")//32
	int servoY = vitaminData.get("servoThinDimentionThickness")
	int servoZ = vitaminData.get("servoShaftSideHeight")
	HashMap<String, Object>  vitaminData2 = Vitamins.getConfiguration( "hobbyServo","hv6214mg")//current servo
	int servoY2 = vitaminData2.get("servoThinDimentionThickness")
	CSG connector = createConnector(servo, hornRef, xLength)//.rotx(180) for other side
	
	ArrayList<CSG> shoulderParts = createShoulder(servo, xLength)
	ArrayList<CSG> thighParts = createThigh(servo, hornRef, xLength, connector)

	int bottMainLeng = 3+xLength/2+(xLength-80)/2
	
	//side cap creation
	CSG bottomCap = new Cube(bottMainLeng, servoY2*2+1, 13).toCSG()
							.movez(servoZ/2+2.5+2.5)
							.toXMin()
							.movex(15 + (xLength - 80)/2)
	CSG capSide1 = new Cube(bottMainLeng, 5, servoZ/2+10).toCSG() 
								.toXMin()
								.movex(15 + (xLength - 80)/2)
								.movey(servoY2+2.6)
								.movez(servoZ/4+9)
	CSG capSide2 = new Cube(bottMainLeng, 5, servoZ/2+10).toCSG() 
								.toXMin()
								.movex(15 + (xLength - 80)/2)
								.movey(-servoY2-2.6)
								.movez(servoZ/4+9)
	
	bottomCap = bottomCap.union(capSide1)
	bottomCap = bottomCap.union(capSide2)
	CSG bottCap2 = new Cube(xLength, 5+servoY2*2, 5).toCSG()
								.toXMin()
								.movez(servoZ/2+6+4.25+1.25)
								.movex(15 + (xLength - 80)/2)
	bottomCap = bottomCap.union(bottCap2)
	
	CSG screwHole = new Cylinder(1.25,30,(int)25).toCSG()//move then subtract
								  .movex(-9 + (xLength - 80)/2)
								  .movey(-5)
								  .movez(-20)
								  .movex(xLength + 25*xLength/80)
								  .movez(-20)
	
	bottomCap = bottomCap
				.movez(-60)
				.rotx(180)
				.movez(-73.5)
	bottomCap = bottomCap.toXMin()
					 .movex(xLength/2+11 + 25*xLength/80)//figure this out
	

	//the flat end part of the cap with no sides (to keep connector in place



	CSG connHole = new Cylinder(3.45,4.5,(int)50).toCSG()
								.movez(-19.5)
								.movex(33)

	connHole = connHole.toXMin()
				    .toZMin()
				    .movex(166+ 3.6/2*(xLength-100))//to be accounted for paramaterizing
				    .movez(-32.5)

	bottomCap = bottomCap.movez(8)
					 .union(connHole)
					 
	CSG screwHole2 = new Cylinder(3,30,(int)25).toCSG()//move then subtract
								  .movex(-9 + (xLength - 80)/2)
								  .movey(-5)
								  .movez(-20)
								  .movex(xLength + 25*(xLength/80))
								  .movez(-40)
								  .movey(servoY2+1).movex(20)
	//holes below so that cap can be screwed in
	
//above
	bottomCap = bottomCap.difference(screwHole)
	screwHole = screwHole.movey(10)
	bottomCap = bottomCap.difference(screwHole)
	screwHole = screwHole.movey(servoY2+1-10).movex(20+(-xLength+80)/80)//+20*xLength/80)
	bottomCap = bottomCap.difference(screwHole)
	screwHole = screwHole.movey(-servoY2+9-10-servoY2+9)
	bottomCap = bottomCap.difference(screwHole)

		bottomCap = bottomCap.difference(screwHole2)
		screwHole2 = screwHole2.movey(-servoY2+9-10-servoY2+9)
		bottomCap = bottomCap.difference(screwHole2)
	
	
	bottomCap = bottomCap.movez(-50)
	bottomCap = bottomCap
					.rotx(90)
					.toYMin()
					.movey(-15.5)//amount of connector subtracted from rotated cap
				
	
	CSG rotatedLink = shoulderParts.get(0)
	rotatedLink = rotatedLink.rotx(90)
						.toYMin()
						
	rotatedLink = rotatedLink.movex(xLength + 25*xLength/80)
	bottomCap = bottomCap.toXMin()
					 .movex(xLength/2+11 + 25*xLength/80)
	connector = connector.toZMin()

	connector = connector.movez(-servoY*8/14)//connector height = servoY*8/7

	//scaling because the sideways connector has more trouble than normal one at fitting in the connector hole
	rotatedLink = rotatedLink.difference(connector.scalez(1.05).scalex(1.01))
	bottomCap = bottomCap.difference(connector.scalez(1.05).scalex(1.01))

	//visibility
	bottomCap = bottomCap.movey(-10)
	
	ArrayList<CSG> parts = new ArrayList<CSG>()

	parts.add(rotatedLink.movez(70))
	parts.add(bottomCap.movez(70))
	parts.add(connector.movez(70).movey(-5))

	return parts
}

public CSG theUnion(){
	CSG test1 = new Cube(11,8,3).toCSG()
	
	CSG test2 = new Cube(11,8,3).toCSG()
						   
	CSG shoulder = createShoulder(test1)
	CSG thigh = createThigh(test1)
	CSG connector = createConnector(test1,test2)
	CSG united = shoulder.union(thigh).union(connector)

	united.setRegenerate({theUnion()})
	return united
}

public CSG theUnionNoConnector(){
	CSG test1 = new Cube(11,8,3).toCSG()
	
	CSG test2 = new Cube(11,8,3).toCSG()
	CSG shoulder = createShoulder(test1)
	CSG thigh = createThigh(test1)
	CSG united = shoulder.union(thigh)
	united.setRegenerate({theUnionNoConnector()})
	return united
}
	
public CSG createBaseLink2(CSG servo, CSG hornRef, int xLength){
	HashMap<String, Object>  vitaminData = Vitamins.getConfiguration( "hobbyServo","hv6214mg") //replace with servo type 


ArrayList<CSG> parts = new ArrayList<CSG>()

//these numbers can be used as universal reference numbers
int servoX = vitaminData.get("flangeLongDimention")//32
int servoY = vitaminData.get("servoThinDimentionThickness")//11.8
int servoZ = vitaminData.get("servoShaftSideHeight")//31.5

servo = servo								.scalex(1.08)
										.rotz(90)
										.rotx(180)
										.movez(-6.3)
										.movex(servoX/2 + (xLength - 80)/2)

//create the main part of the leg that will have an indent in the shape of the servo
CSG sub1 = new Cube(servoX+3, servoY+1.5, servoZ).toCSG().movex(servoX/3 + (xLength - 80)/2 -1.5).movez(7.9)
CSG screwHole = new Cylinder(1.25,10,(int)25).toCSG()//move then subtract
								  .movex(-9 + (xLength - 80)/2)
								  .movey(-5)
								  .movez(-20)


//CSG mainLeg = new Cube(xLength, servoY*2, servoZ+1).toCSG().movez(1).movex(11) 

CSG mainLeg = new RoundedCube(xLength, servoY*2, servoZ+1).cornerRadius(0.3).toCSG().movez(1).movex(11) 
mainLeg =mainLeg.difference(servo)
mainLeg =mainLeg.difference(sub1)
mainLeg =mainLeg.difference(screwHole)
screwHole = screwHole.movey(10)
mainLeg =mainLeg.difference(screwHole)
screwHole = screwHole.movey(20.1+1-10).movex(20)
mainLeg =mainLeg.difference(screwHole)
screwHole = screwHole.movey(-20.1+9-10-20.1+9)//20.1 is servo2
mainLeg =mainLeg.difference(screwHole)

mainLeg = mainLeg.movex(-servoX/2)
return mainLeg



}
public CSG createTopCap(int xLength){
int capLength = 46.3 + xLength/6.8 -1 ;
CSG cap = new Cube(capLength, servoY*8/3.5, 5).toCSG()
							.movez(servoZ/2+2.5)
							.movex((xLength - 80)+ capLength/2 - 12.5)
CSG capSide1 = new Cube(capLength, 5, servoZ/2).toCSG()
								.movex((xLength - 80)+ capLength/2 - 12.5)
								.movey(servoY+2.5)
								.movez(servoZ/4+5)
CSG capSide2 = new Cube(capLength, 5, servoZ/2).toCSG()
								.movex((xLength - 80)+ capLength/2 - 12.5)
								.movey(-servoY-2.5)
								.movez(servoZ/4+5)
cap = cap.union(capSide1)
cap = cap.union(capSide2)
return cap;
}


public CSG createBaseLink(CSG servo, CSG hornRef, int xLength, boolean normFalseRotTrue, CSG connector){
		HashMap<String, Object>  vitaminData = Vitamins.getConfiguration( "hobbyServo","hv6214mg") //replace with servo type 

ArrayList<CSG> parts = new ArrayList<CSG>()

//these numbers can be used as universal reference numbers
int servoX = vitaminData.get("flangeLongDimention")//32
int servoY = vitaminData.get("servoThinDimentionThickness")//11.8
int servoZ = vitaminData.get("servoShaftSideHeight")//31.5
	CSG mainLeg;
	if(normFalseRotTrue == false)
	{
		 mainLeg = createThigh(servo, hornRef, xLength, connector).get(0)
	}
	else{
		 mainLeg =  rotatedLegLink(servo, hornRef, xLength).get(0)
		 										.rotx(-90)
		 										.toXMin()
		 										.movex(-servoX/2-(xLength/2-11))
		 										.movez(-servoZ/2+0.5)
		 										.movey(70)
		 										.movex(1.75*xLength-7)//to align
	}
return mainLeg
}

public CSG createFoot(CSG servo, CSG hornRef, double xLength){

	//Recreation of the CSGs from the first part, mainLeg
	HashMap<String, Object>  vitaminData = Vitamins.getConfiguration( "hobbyServo","towerProMG91")
	HashMap<String, Object>  vitaminData2 = Vitamins.getConfiguration( "hobbyServoHorn","hv6214mg_1")
	//print("horn" + vitaminData2)
	int hornRad = vitaminData2.get("hornBaseDiameter")//13.48
	int hornThick = vitaminData2.get("hornThickness")//6.6
	int hornLeng = vitaminData2.get("hornLength")//23.0
	int servoX = vitaminData.get("flangeLongDimention")//32
	int servoY = vitaminData.get("servoThinDimentionThickness")//11.8
	
	LengthParameter connectorLength = new LengthParameter("Length of Leg",70,[150,60])

	int thickness = 10
								
	CSG connector = new Cube((xLength - 80)/2 + 61, servoY*8/7,thickness).toCSG()
								   .movez(-18.5+thickness/5)
				 				   .toXMin()
				 				   .movex(20)
				 				   
	//fancifying, could be removed if we want the connectors shorter
	CSG decor1 = new Cylinder(hornRad+1,thickness,(int)50).toCSG()
								.movez(-21.5)
								.movex(24+hornRad-7.7)
	CSG decor2 = new Cylinder(hornRad/2+2,hornRad/2+2,thickness,(int)50).toCSG()
								.movez(-21.5)
								.movex(37+hornRad-7.7)
	connector = connector.union(decor1)
				 .union(decor2)
				 
	//keyHole is to be subtracted: connHole subtracted from horn (the cylinder hole)
	int cylVal = 4
	CSG connHole = new Cylinder(cylVal,cylVal,4.5,(int)50).toCSG()
								.movez(-19.5)
								.movex(33)
								
	//subtracting the correct horn from the connector
	CSG hornCube = new Cube(14,11,10).toCSG().toZMin().toYMin()// 12 to adjust horn subtraction
	hornRef = hornRef.makeKeepaway(0.5)
	CSG halfHorn = hornRef.intersect(hornCube)
	halfHorn = halfHorn.rotz(90).movex(servoX).movez(-17)
	hornRef = hornRef.rotz(90).movex(servoX).movez(-17)
	CSG keyHole = connHole.union(hornRef).union(halfHorn.movez(3)).makeKeepaway(2)
					.movex(-(8))
	
	connector = connector
				 .difference(keyHole.movez(1))//moved subtraction up 1
				 .movez(-10)

	connHole = connHole.movez(-12)
				.movex(-(8.5)) 
				
	connector = connector.difference(connHole)

	int endLength = (xLength - 80)/2 + 61 +20 - 2
	CSG subCube = new Cube(40,40,40).toCSG().movez(-26)
	.movex(endLength)
							.movez(-25.5)
	CSG footEnd = new Sphere(17).toCSG()
							.movex(endLength)
							.movez(-25.5)
	footEnd = footEnd.difference(subCube)
	CSG connectorEnd1 = new Cylinder(2,2,thickness+2,(int)50).toCSG()
									.movex(endLength)
									.movey(6)
						   			.movez(-32.5)
	CSG connectorEnd2 = new Cylinder(2,2,thickness+2,(int)50).toCSG()
									.movex(endLength)
									.movey(-6)
						   			.movez(-32.5)
	CSG connectorEnds = connectorEnd1.hull(connectorEnd2)
							.movez(1)
	
	CSG endCyl = new Cylinder(2,2,14,(int)40).toCSG()
			.rotx(90)
			.movez(-20)
			.movex(endLength)
			.movey(-7)
			 connectorEnds = connectorEnds.hull(endCyl)
	connector = connector.union(footEnd)
	connector = connector
		 connector.setManufactuing({CSG arg0 ->
 				return arg0.toZMin()
 })
 
	connector.setParameter(connectorLength)// add any parameters that are not used to create a primitive
		    .setRegenerate({ createConnector(Vitamins.getConfiguration( "hobbyServo","towerProMG91"))})
		    
	connector = connector.movez(-20).toXMin().movex(servoX-10 + (xLength - 80)/2)

	
	return connector
}

public CSG createFootCap(CSG servo, CSG hornRef, double xLength){

	//Recreation of the CSGs from the first part, mainLeg
	HashMap<String, Object>  vitaminData = Vitamins.getConfiguration( "hobbyServo","towerProMG91")
	HashMap<String, Object>  vitaminData2 = Vitamins.getConfiguration( "hobbyServoHorn","hv6214mg_1")
	//print("horn" + vitaminData2)
	int hornRad = vitaminData2.get("hornBaseDiameter")//13.48
	int hornThick = vitaminData2.get("hornThickness")//6.6
	int hornLeng = vitaminData2.get("hornLength")//23.0
	int servoX = vitaminData.get("flangeLongDimention")//32
	int servoY = vitaminData.get("servoThinDimentionThickness")//11.8
	
	LengthParameter connectorLength = new LengthParameter("Length of Leg",70,[150,60])
	CSG fCap = new Cylinder(40, // Radius at the bottom
                      		25, // Radius at the top
                      		20, // Height
                      		(int)30 //resolution
                      		).toCSG()//convert to CSG to display                    			         ).toCSG()//convert to CSG to display 
                      		.movey(50)
	int thickness = 10
								
	CSG connector = new Cube((xLength - 80)/2 + 61, servoY*8/7,thickness).toCSG()
								   .movez(-18.5+thickness/5)
				 				   .toXMin()
				 				   .movex(20)
				 				   
	//fancifying, could be removed if we want the connectors shorter
	CSG decor1 = new Cylinder(hornRad+1,thickness,(int)50).toCSG()
								.movez(-21.5)
								.movex(24+hornRad-7.7)
	CSG decor2 = new Cylinder(hornRad/2+2,hornRad/2+2,thickness,(int)50).toCSG()
								.movez(-21.5)
								.movex(37+hornRad-7.7)
	connector = connector.union(decor1)
				 .union(decor2)
				 
	//keyHole is to be subtracted: connHole subtracted from horn (the cylinder hole)
	int cylVal = 4
	CSG connHole = new Cylinder(cylVal,cylVal,4.5,(int)50).toCSG()
								.movez(-19.5)
								.movex(33)
								
	//subtracting the correct horn from the connector
	CSG hornCube = new Cube(14,11,10).toCSG().toZMin().toYMin()// 12 to adjust horn subtraction
	hornRef = hornRef.makeKeepaway(0.5)
	CSG halfHorn = hornRef.intersect(hornCube)
	halfHorn = halfHorn.rotz(90).movex(servoX).movez(-17)
	hornRef = hornRef.rotz(90).movex(servoX).movez(-17)
	CSG keyHole = connHole.union(hornRef).union(halfHorn.movez(3)).makeKeepaway(2)
					.movex(-(8))
	
	connector = connector
				 .difference(keyHole.movez(1))//moved subtraction up 1
				 .movez(-10)

	connHole = connHole.movez(-12)
				.movex(-(8.5)) 
				
	connector = connector.difference(connHole)

	int endLength = (xLength - 80)/2 + 61 +20 - 2
	CSG cube = new Cube(20,20,20).toCSG().movez(10)
	CSG footEnd = new Sphere(17).toCSG()
							//.movex(endLength)
							//.movez(-25.5)
	CSG connectorEnd1 = new Cylinder(2,2,thickness+2,(int)50).toCSG()
									.movex(endLength)
									.movey(6)
						   			.movez(-32.5)
	CSG connectorEnd2 = new Cylinder(2,2,thickness+2,(int)50).toCSG()
									.movex(endLength)
									.movey(-6)
						   			.movez(-32.5)
	CSG connectorEnds = connectorEnd1.hull(connectorEnd2)
							.movez(1)
	
	CSG endCyl = new Cylinder(2,2,14,(int)40).toCSG()
			.rotx(90)
			.movez(-20)
			.movex(endLength)
			.movey(-7)
			 connectorEnds = connectorEnds.hull(endCyl)
	connector = connector.union(footEnd)
	connector = connector
		 connector.setManufactuing({CSG arg0 ->
 				return arg0.toZMin()
 })
 
	connector.setParameter(connectorLength)// add any parameters that are not used to create a primitive
		    .setRegenerate({ createConnector(Vitamins.getConfiguration( "hobbyServo","towerProMG91"))})
		    
	connector = connector.movez(-20).toXMin().movex(servoX-10 + (xLength - 80)/2)

	
	return fCap
}

}

return new legPiece()

