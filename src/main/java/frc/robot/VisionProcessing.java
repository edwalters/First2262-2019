/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * Add your docs here.
 */
public class VisionProcessing {
    DigitalInput lineSensorLeft;
    DigitalInput lineSensorMiddle;
    DigitalInput lineSensorRight;
    boolean lineFollowContinue = true;

    public VisionProcessing() {
        CameraServer.getInstance().startAutomaticCapture();
        lineSensorLeft = new DigitalInput(5);
        lineSensorMiddle = new DigitalInput(6);
        lineSensorRight = new DigitalInput(7);
    }
    public void startProcessing(DifferentialDrive drive) {
        new Thread() {
            public void run() {
                while(lineFollowContinue) {

                    if(lineSensorLeft.get() && !lineSensorMiddle.get() && !lineSensorRight.get()) {
                        drive.arcadeDrive(0, .5);
                    }
                    
                    else if(lineSensorLeft.get() && lineSensorMiddle.get() && !lineSensorRight.get()) {
                        drive.arcadeDrive(0, .5);
                    }
                    
                    else if(!lineSensorLeft.get() && !lineSensorMiddle.get() && lineSensorRight.get()) {
                        drive.arcadeDrive(0, -.5);
                    }
                    
                    else if(!lineSensorLeft.get() && lineSensorMiddle.get() && lineSensorRight.get()) {
                        drive.arcadeDrive(0, -.5);
                    }
                    
                    else if(!lineSensorLeft.get() && !lineSensorMiddle.get() && !lineSensorRight.get()) {
                        drive.arcadeDrive(0, .5);
                    }
                    
                    else {
                        drive.arcadeDrive(1, 0);
                    }
                }
            }
        }.start();
    }

    public void stop() {
        lineFollowContinue = false;
    }
}
