package farmItem;


import java.io.IOException;

import main.java.surelyhuman.jdrone.control.physical.tello.TelloDrone;

public class TelloAdapter implements DroneAnimation{
	private TelloDrone td;

	// Found this conversion on Stack overflow (please double check to make sure this is correct)
//	There are 2.54 centimeters per inch; if it is sufficient
	//to assume 96 pixels per inch, the formula is rather simple:
//		centimeters = pixels * 2.54 / 96



	public TelloAdapter(TelloDrone td) {
		this.td = td;
	}


	@Override
	public void droneToItem(double xPos, double yPos, double itemWidth, double itemLength, double dronePixelX, double dronePixelY) throws IOException, InterruptedException {
		
		//Converting parameters to centimeter values
		int itemXCM = pixelToCm((xPos + 0.5*itemWidth));
		int itemYCM = pixelToCm((yPos + 0.5*itemWidth));
		int droneXCM = pixelToCm(dronePixelX);
		int droneYCM = pixelToCm(dronePixelY);
		int xDistance = itemXCM - droneXCM;
		int yDistance = itemYCM - droneYCM;
		
		//Setting up the trigonometry for the drone's path
		//Pythagorean theorem to get straight-line distance to object
		int distanceToItem = (int) Math.sqrt(Math.pow(itemXCM - droneXCM, 2) + (Math.pow(itemYCM - droneYCM, 2)));
		System.out.println(distanceToItem);
		//Using arctan to get the angle from the drone location on x-axis to the item location
		int angle = (int) Math.toDegrees(Math.atan2(yDistance, xDistance));
		System.out.println(angle);
	
		
		// Start the drone and take off
		td.activateSDK();
		td.streamOn();
		td.streamViewOn();
		td.hoverInPlace(5);
		td.takeoff();
		
		
		//rotate to face object
		if(angle > 0) {
			td.turnCW(angle);
		}
		else {
			td.turnCCW(angle * -1);
		}
		
		//move towards object and hover over it
		td.flyForward(distanceToItem);
		td.hoverInPlace(3);
		
		//rotate to face command center and fly to it
		td.turnCW(180);
		td.flyForward(distanceToItem);
		
		//reverse the original rotation to face starting direction (right)
		if(angle > 0) {
			td.turnCCW(angle);
		}
		else {
			td.turnCW(angle * -1);
		}
		td.turnCCW(180);
		// Land and stop the drone
		td.land();
		td.streamOff();
		td.streamViewOff();
		System.out.println("Before td.end()");
//		td.end();
		System.out.println("can you reach me?");
//		System.exit(0);
		

	}

	public int pixelToCm(double pixelVal) {
		// Pixels * 0.04 = feet, feet * 30.48 = centimeters
		double cmVal = (pixelVal * 0.04) * 30.48;
		return (int) cmVal;
	}

	@Override
	public void FarmScan(double farmWidth, double farmLength, double dronePixelX, double dronePixelY) throws IOException, InterruptedException {
		// Drone dimension is 10x10cm
		int droneBuffer = 10;
		int farmWidthCM = pixelToCm(farmWidth) - droneBuffer;
		int farmLengthCM = pixelToCm(farmLength) - droneBuffer;
		int droneXcm = pixelToCm(dronePixelX);
		int droneYcm = pixelToCm(dronePixelY);

		// Start the drone and take off
		td.activateSDK();
		td.streamOn();
		td.streamViewOn();
		td.hoverInPlace(5);
		td.takeoff();

		// Fly to top left corner (0,0)
//		td.turnCCW(90);
//		td.flyForward(droneYcm);
//		td.turnCCW(90);
//		td.flyForward(droneXcm);

		// Fly to bottom left corner (0, farmWidth)
		td.turnCW(90);
		td.flyForward(farmWidthCM);

		//Fly to bottom 1/3
		td.turnCCW(90);
		td.flyForward(farmLengthCM / 3);

		// Fly to top 1/3
		td.turnCCW(90);
		td.flyForward(farmWidthCM);

		// Fly to top 2/3
		td.turnCW(90);
		td.flyForward(farmLengthCM / 3);

		// Fly to bottom 2/3
		td.turnCW(90);
		td.flyForward(farmWidthCM);

		// Fly to bottom right
		td.turnCCW(90);
		td.flyForward(farmLengthCM / 3);

		// Fly to top right
		td.turnCCW(90);
		td.flyForward(farmWidthCM);

		// Fly to top left 
		td.turnCCW(90);
		td.flyForward(farmLengthCM);

		// Fly back to Command Center
		td.turnCW(180);
		//td.flyForward(droneXcm);
		//td.turnCW(90);
		//td.flyForward(droneYcm);
		//td.turnCCW(90);

		// Land and stop the drone
		td.land();
		td.streamOff();
		td.streamViewOff();
//		td.end();
		

	}

}
