/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Lifting mechanism on the robot
 */
public class Lift {
    boolean liftEngaged;
    WPI_TalonSRX frontLiftMotor;
    WPI_TalonSRX backLiftMotor;
    WPI_VictorSPX leftDriveMotor;
    WPI_VictorSPX rightDriveMotor;
    SpeedControllerGroup liftMotors;
    DoubleSolenoid frontLiftBrake;
    DoubleSolenoid backLiftBrake;

    public Lift(int idFrontLiftMotor, int idBackLiftMotor, int idLeftDriveMotor, int idRightDriveMotor) {
        frontLiftMotor = new WPI_TalonSRX(idFrontLiftMotor);
        backLiftMotor = new WPI_TalonSRX(idBackLiftMotor);
        backLiftMotor.setInverted(true);
        leftDriveMotor= new WPI_VictorSPX(idLeftDriveMotor);
        rightDriveMotor= new WPI_VictorSPX(idRightDriveMotor);
        liftMotors = new SpeedControllerGroup(frontLiftMotor, backLiftMotor);
        frontLiftBrake = new DoubleSolenoid(6, 7);
        backLiftBrake = new DoubleSolenoid(4, 5);
        leftDriveMotor.follow(rightDriveMotor);
    }
    
    public void liftRobotUp(double speed) { //Push the robot up by putting both sets of wheels down
        liftEngaged = true;
        new Thread() {
            public void run() {
                frontBrakeDisengage();
                backBrakeDisengage();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { }
                liftMotors.set(1);
            }
        }.start();
    }

    public void liftRobotUpStop() {
        new Thread() {
            public void run() {
                liftMotors.set(0);
                frontBrakeEngage();
                backBrakeEngage();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { }
            }
        }.start();
    }
    
    public void robotDown() { //Bring both wheels back in at the same time, lowering the bot
        liftEngaged = false;
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
        new Thread() {
            public void run() {
                liftMotors.set(0);
                frontBrakeEngage();
                backBrakeEngage();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { }
            }
        }.start();
    }

    public void frontPostDown(double speed) { //Put down only this set of wheels
        new Thread() {
            public void run() {
                frontBrakeDisengage();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) { }
                frontLiftMotor.set(-speed);
            }
        }.start();
    }

    public void frontPostDownStop() {
        new Thread() {
            public void run() {
                frontLiftMotor.set(0);
                frontBrakeEngage();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) { }
            }
        }.start();
    }

    public void frontPostUp(double speed) { //Bring back up only this set of wheels
        new Thread() {
            public void run() {
                frontBrakeDisengage();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) { }
                frontLiftMotor.set(speed);
            }
        }.start();
    }

    public void frontPostUpStop() {
        new Thread() {
            public void run() {
                frontLiftMotor.set(0);
                frontBrakeEngage();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) { }
            }
        }.start();
    }

    public void backPostDown(double speed) { //Put down only this set of wheels
        liftEngaged = true;
        new Thread() {
            public void run() {
                backBrakeDisengage();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) { }
                backLiftMotor.set(-speed);
            }
        }.start();
    }
    
    public void backPostDownStop() {
        new Thread() {
            public void run() {
                backLiftMotor.set(0);
                backBrakeEngage();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) { }
            }
        }.start();
    }

    public void backPostUp(double speed) { //Bring back up only this set of wheels
        liftEngaged = false;
        new Thread() {
            public void run() {
                backBrakeDisengage();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) { }
                backLiftMotor.set(speed);
            }
        }.start();
    }

    public void backPostUpStop() {
        new Thread() {
            public void run() {
                backLiftMotor.set(0);
                backBrakeEngage();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) { }
            }
        }.start();
    }

    public void drive(double speed) {
        if(liftEngaged) {
            leftDriveMotor.set(ControlMode.PercentOutput, speed); // Controls both this motor and the rightDriveMotor
        }
    }

    public void frontBrakeEngage() {
        frontLiftBrake.set(Value.kForward);
    }

    public void frontBrakeDisengage() {
        frontLiftBrake.set(Value.kReverse);
    }

    public void backBrakeEngage() {
        backLiftBrake.set(Value.kForward);
    }

    public void backBrakeDisengage() {
        backLiftBrake.set(Value.kReverse);
    }
}
