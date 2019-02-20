/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

/**
 * Lifting mechanism on the robot
 */
public class Lift {
    WPI_TalonSRX frontLiftMotor;
    WPI_TalonSRX backLiftMotor;
    WPI_VictorSPX leftDriveMotor;
    WPI_VictorSPX rightDriveMotor;
    SpeedControllerGroup liftMotors;
    DoubleSolenoid frontLiftBrake;
    DoubleSolenoid backLiftBrake;
    boolean isFrontBrakeEnaged;
    boolean isBackBrakeEnaged;


    public Lift(int idFrontLiftMotor, int idBackLiftMotor, int idLeftDriveMotor, int idRightDriveMotor) {
        frontLiftMotor = new WPI_TalonSRX(idFrontLiftMotor);
        frontLiftMotor.setNeutralMode(NeutralMode.Brake);
        backLiftMotor = new WPI_TalonSRX(idBackLiftMotor);
        backLiftMotor.setNeutralMode(NeutralMode.Brake);
        backLiftMotor.setInverted(true);
        leftDriveMotor= new WPI_VictorSPX(idLeftDriveMotor);
        leftDriveMotor.configNeutralDeadband(.1);
        rightDriveMotor= new WPI_VictorSPX(idRightDriveMotor);
        rightDriveMotor.setInverted(true);
        liftMotors = new SpeedControllerGroup(frontLiftMotor, backLiftMotor);
        frontLiftBrake = new DoubleSolenoid(6, 7);
        backLiftBrake = new DoubleSolenoid(4, 5);
        rightDriveMotor.follow(leftDriveMotor);

        frontBrakeEngage();
        backBrakeDisengage();
    }
    
    public void liftRobotUp() { //Push the robot up by putting both sets of wheels down
        if(isFrontBrakeEnaged) {
            frontBrakeDisengage();
        }
        if(isBackBrakeEnaged) {
            backBrakeDisengage();
        }
        frontLiftMotor.set(1);
        backLiftMotor.set(1);
    }

    public void liftRobotUpStop() {
        liftMotors.set(0);
    }
    
    public void robotDown() { //Bring both wheels back in at the same time, lowering the bot
        new Thread() {
            public void run() {
                frontBrakeDisengage();
                backBrakeDisengage();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { }
                liftMotors.set(-1);
            }
        }.start();
    }   
    
    public void robotDownStop() {
        liftMotors.set(0);
    }

    public void frontPostDown() { //Put down only this set of wheels
        new Thread() {
            public void run() {
                frontBrakeDisengage();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) { }
                frontLiftMotor.set(1);
            }
        }.start();
    }

    public void frontPostUp() { //Bring back up only this set of wheels
        new Thread() {
            public void run() {
                frontBrakeDisengage();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) { }
                frontLiftMotor.set(-1);
            }
        }.start();
    }

    public void frontPostStop() {
        liftMotors.set(0);
    }

    public void backPostDown() { //Put down only this set of wheels
        new Thread() {
            public void run() {
                if(isBackBrakeEnaged) {
                    backBrakeDisengage();
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) { }
                backLiftMotor.set(1);
            }
        }.start();
    }

    public void backPostUp() { //Bring back up only this set of wheels
        new Thread() {
            public void run() {
                if(isBackBrakeEnaged) {
                    backBrakeDisengage();
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) { }
                backLiftMotor.set(-1);
            }
        }.start();
    }

    public void backPostStop() {
        backLiftMotor.set(0);
    }

    public void drive(double speed) {
        leftDriveMotor.set(ControlMode.PercentOutput, speed); // Controls both this motor and the rightDriveMotor
    }

    public void frontBrakeEngage() {
        isFrontBrakeEnaged = true;
        frontLiftBrake.set(Value.kForward);
    }

    public void frontBrakeDisengage() {
        isFrontBrakeEnaged = false;
        frontLiftBrake.set(Value.kReverse);
    }

    public void backBrakeEngage() {
        isBackBrakeEnaged = true;
        backLiftBrake.set(Value.kForward);
    }

    public void backBrakeDisengage() {
        isBackBrakeEnaged = false;
        backLiftBrake.set(Value.kReverse);
    }
}
