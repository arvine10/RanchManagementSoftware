package farmItem;

import java.io.IOException;

public interface DroneAnimation {
	public abstract void droneToItem(double xPos, double yPos, double itemWidth, double itemLength, double dronePixelX, double dronePixelY) throws IOException, InterruptedException;
	public abstract void FarmScan(double farmWidth, double farmLength, double dronePixelX, double dronePixelY) throws IOException, InterruptedException;

}
