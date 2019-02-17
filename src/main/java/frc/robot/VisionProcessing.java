/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.networktables.NetworkTableEntry;

/**
 * Add your docs here.
 */
public class VisionProcessing {
    DigitalInput lineSensorLeft;
    DigitalInput lineSensorMiddle;
    DigitalInput lineSensorRight;
    boolean lineFollowContinue = true;
    boolean visionContinue = true;
    NetworkTable myContoursTable;

    NetworkTableEntry centerX;
    NetworkTableEntry centerY;

    public VisionProcessing() {
        CameraServer.getInstance().startAutomaticCapture();
        lineSensorLeft = new DigitalInput(6);
        lineSensorMiddle = new DigitalInput(5);
        lineSensorRight = new DigitalInput(7);

        myContoursTable = NetworkTableInstance.create().getTable("myContoursReport1");
    }

    public void processShapes() {

        NetworkTableEntry centerX =  myContoursTable.getEntry("centerX");
        NetworkTableEntry centerY =  myContoursTable.getEntry("centerY");

        double x = centerX.getDouble(0.0);
        double y = centerY.getDouble(0.0);
       
        
    }

    public void startVisionProcessing(DifferentialDrive drive) {
        visionContinue = true;
        while(visionContinue) {
            processShapes();
        }
        //Turn in a certain direction if the distance between the centers of the two lines are centered in relation to the rest of the robot
        //Otherwise, go right at 'em
    }

    public void startLineFollow(DifferentialDrive drive) {
        lineFollowContinue = true;
        while(lineFollowContinue) {

            if(lineSensorLeft.get() && !lineSensorMiddle.get() && !lineSensorRight.get()) {
                drive.arcadeDrive(0, .25);
            }
            
            else if(lineSensorLeft.get() && lineSensorMiddle.get() && !lineSensorRight.get()) {
                drive.arcadeDrive(0, .25);
            }
            
            else if(!lineSensorLeft.get() && !lineSensorMiddle.get() && lineSensorRight.get()) {
                drive.arcadeDrive(0, -.25);
            }
            
            else if(!lineSensorLeft.get() && lineSensorMiddle.get() && lineSensorRight.get()) {
                drive.arcadeDrive(0, -.25);
            }
            
            else if(!lineSensorLeft.get() && !lineSensorMiddle.get() && !lineSensorRight.get()) {
                drive.arcadeDrive(0, .25);
            }
            
            else {
                drive.arcadeDrive(.5, 0);
            }
        }
    }

    public void stop() {
        lineFollowContinue = false;
        visionContinue = false;
    }
}
