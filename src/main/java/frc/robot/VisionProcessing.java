/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Spark;

/**
 * Add your docs here.
 */
public class VisionProcessing {
    AnalogInput lineSensorLeft;
    AnalogInput lineSensorMiddle;
    AnalogInput lineSensorRight;
    boolean visionContinue = false;
    NetworkTable myContoursTable;

    NetworkTableEntry centerX;
    NetworkTableEntry centerY;
    double center;

    public VisionProcessing(DifferentialDrive drive) {
        CameraServer.getInstance().startAutomaticCapture();
        lineSensorLeft = new AnalogInput(1);
        lineSensorMiddle = new AnalogInput(0);
        lineSensorRight = new AnalogInput(2);

        // myContoursTable = NetworkTableInstance.getDefault().getTable("GRIP/myContoursReport");
        // myContoursTable.addEntryListener("Centers", (table, key, entry, value, flags)-> {
        //     if(value.getDoubleArray().length == 2 && visionContinue) {
        //         System.out.println("Center 1: "+value.getDoubleArray()[0]);
        //         System.out.println("Center 2: "+value.getDoubleArray()[1]);
        //         double foundCenter = value.getDoubleArray()[0]+value.getDoubleArray()[1];
        //         foundCenter = foundCenter/2;
        //         System.out.println(foundCenter);
        //         if(foundCenter > 150 && foundCenter < 170) {
        //             drive.arcadeDrive(-.75, 0);
        //         }

        //         else if(foundCenter > 160) {
        //             drive.arcadeDrive(-.5, .5);
        //         }
    
        //         else if(foundCenter < 160) {
        //             drive.arcadeDrive(-.5, -.5);
        //         }
                
        //     }
        // }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate | EntryListenerFlags.kFlags);

        // myContoursTable = NetworkTableInstance.getDefault().getTable("GRIP/myContoursReport2");
        // myContoursTable.addEntryListener("Centers", (table, key, entry, value, flags)-> {
        //     if(value.getDoubleArray().length == 2 && visionContinue) {
        //         System.out.println("Center 1: "+value.getDoubleArray()[0]);
        //         System.out.println("Center 2: "+value.getDoubleArray()[1]);
        //         double foundCenter = value.getDoubleArray()[0]+value.getDoubleArray()[1];
        //         foundCenter = foundCenter/2;
        //         System.out.println(foundCenter);
        //         if(foundCenter > 150 && foundCenter < 170) {
        //             drive.arcadeDrive(-.75, 0);
        //         }

        //         else if(foundCenter > 160) {
        //             drive.arcadeDrive(-.5, .5);
        //         }
    
        //         else if(foundCenter < 160) {
        //             drive.arcadeDrive(-.5, -.5);
        //         }
                
        //     }
        // }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate | EntryListenerFlags.kFlags);

    }

    public void startVisionProcessing() {
        visionContinue = true;
        double leftValue = (lineSensorLeft.getValue() - 3400)*.25;
        double middleValue = (lineSensorMiddle.getValue()-3500)*.3333;
        double rightValue = (lineSensorRight.getValue()-1150)*.76923;
        System.out.print("Left: " + leftValue);
        System.out.print("Middle: " + middleValue);
        System.out.print("Right: " + rightValue);
        System.out.println();
    }

    public void stop(Spark driveMotor4) {
        visionContinue = false;
        driveMotor4.set(0);
    }

    public void startLineFollow(DifferentialDrive drive, Spark driveMotor4) {
        //Line sensor thresholds:
        //Left: (Val - 3400).25
        //Middle: (Val - 3500).333
        //Right: (Val-1150).76923
        //All values should be scaled 0-200 ?
        if(!visionContinue) {
            double leftValue = (lineSensorLeft.getValue() - 3400)*.25;
            double middleValue = (lineSensorMiddle.getValue()-3500)*.3333;
            double rightValue = (lineSensorRight.getValue()-1150)*.76923;
            System.out.println("Left: " + leftValue);
            System.out.println("Middle: " + middleValue);
            System.out.println("Right: " + rightValue);
            if(leftValue < 10 && rightValue > 40) {
                drive.arcadeDrive(0, .6);
                driveMotor4.set(.6);
            }

            else if(rightValue < 10 && leftValue > 40) {
                drive.arcadeDrive(0, -.6);
                driveMotor4.set(-.6);
            }
    
            else {
                drive.arcadeDrive(-.6, 0);
                driveMotor4.set(.6);
            }
        }
    }
}
