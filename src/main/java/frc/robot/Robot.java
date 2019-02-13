/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot {

  DifferentialDrive drive;
  WPI_TalonSRX driveMotor1;
  WPI_TalonSRX driveMotor2;
  WPI_TalonSRX driveMotor3;
  WPI_TalonSRX driveMotor4;
  SpeedControllerGroup leftDrive;
  SpeedControllerGroup rightDrive;
  VictorSP winchMotor;
  Encoder winchEncoder;
  Lift lift;
  Compressor compressor;
  DoubleSolenoid intake;
  DoubleSolenoid intakeSlider;
  DigitalInput elevatorTopSwitch;
  DigitalInput elevatorBottomSwitch;
  XboxController controller;
  VisionProcessing vision;
  PIDController pidController;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    // 1 victor sp, and 2 victor spxes and 6 talon srxes
    // winchMotor and the two lift motors NEED to be TalonSRX
    // Leaving 3 TalonSRX, 1 VictorSP, and 2 VictorSPX
    // Drive will be 3 talon, 1 spx
    // Leaving the liftDrive to sp and spx
    driveMotor1 = new WPI_TalonSRX(1);
    driveMotor1.set(ControlMode.PercentOutput, 0);
    driveMotor2 = new WPI_TalonSRX(2);
    driveMotor2.set(ControlMode.PercentOutput, 0);
    driveMotor3 = new WPI_TalonSRX(3);
    driveMotor3.set(ControlMode.PercentOutput, 0);
    driveMotor4 = new WPI_TalonSRX(4);
    driveMotor4.set(ControlMode.PercentOutput, 0);
    leftDrive = new SpeedControllerGroup(driveMotor1, driveMotor2);
    rightDrive = new SpeedControllerGroup(driveMotor3, driveMotor4);
    drive = new DifferentialDrive(leftDrive, rightDrive);
    lift = new Lift(5, 6, 7, 8);
    winchMotor = new VictorSP(0);
    winchMotor.setInverted(true);
    winchEncoder= new Encoder(1, 2);
    compressor = new Compressor(0);
    intake = new DoubleSolenoid(0, 1);
    intakeSlider = new DoubleSolenoid(2, 3);
    elevatorTopSwitch = new DigitalInput(8);
    elevatorBottomSwitch = new DigitalInput(9);
    controller = new XboxController(0);
    pidController = new PIDController(.7, 0, 0, winchEncoder, winchMotor);
    pidController.setSetpoint(0.9);
    pidController.setPercentTolerance(15.0);
    // vision = new VisionProcessing();
    compressor.start();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    /*
    To get d-pad: controller.getPov(); where:
    Up: 0
    Right: 90
    Down: 180
    Left: 270

    Controls as of 2/9/19
    Left Joystick for drive - done
    Left trigger both lift down - implemented
    Right trigger back-lift down - implemented
    A for front-lift up - implemented
    B for back lift up - implemented
    Bumpers for elevator - implemented
    D-pad up for intake open - implemented
    D-pad down for intake close - implemented
    D-pad left for intake left
    D-pad right for intake out
    Start for start line tracking - Just align, then turn off
    Back for off
    Right joystick for second drive, but only when the lift is down (engaged)

    */
    double rightTrigger = controller.getTriggerAxis(Hand.kRight);
    double leftTrigger = controller.getTriggerAxis(Hand.kLeft);
    double rightJoyStickY = controller.getY(Hand.kRight);
    int dPad = controller.getPOV(0);

    drive.arcadeDrive(controller.getY(Hand.kLeft), controller.getX(Hand.kLeft));
    
    if(controller.getAButton()) {
      lift.frontPostUp(1);
    }
    
    if(controller.getAButtonReleased()) {
      lift.frontPostUpStop();
    }

    if(controller.getBButton()) {
      lift.backPostUp(1);
    }
    
    if(controller.getBButtonReleased()) {
      lift.backPostUpStop();
    }

    if(controller.getXButton() && dPad == 0) {
      lift.frontBrakeDisengage();
    }
    
    if(controller.getYButton() && dPad == 0) {
      lift.backBrakeDisengage();
    }
    
    if(controller.getBumper(Hand.kLeft)) { // Go down
      pidController.disable();
      winchMotor.set(-1);
    }
    
    if(controller.getBumperReleased(Hand.kLeft)) {
      winchMotor.set(0);

    }
    if(controller.getBumper(Hand.kRight)) { // Go up
      pidController.enable();
      winchMotor.set(1);
    }

    if(controller.getBumperReleased(Hand.kRight)) {
      winchMotor.set(0);
    }
    
    // if(!elevatorTopSwitch.get()) {
      // winchMotor.set(0);
    // }

    // if(!elevatorBottomSwitch.get()) {
      // winchMotor.set(0);
    // }

    if(leftTrigger > 0.0) {
      lift.liftRobotUp(leftTrigger);
    }

    if(leftTrigger == 0.0 && dPad != 0) {
      lift.liftRobotUpStop();
    }

    if(rightTrigger > 0.0) {
      lift.frontPostDown(rightTrigger);
    }

    if(rightTrigger == 0.0 && dPad != 0) {
      lift.frontPostDownStop();
    }
    
    if(dPad == 90) {
      intakeSlider.set(Value.kForward);
    }

    if(dPad == 270) {
      intakeSlider.set(Value.kReverse);
    }

    if(controller.getStartButton()) {
      // vision.startProcessing();
    }

    if(controller.getBackButton()) {
      // vision.stop();
    }

    if(rightJoyStickY != 0) {
      lift.drive(rightJoyStickY);
    }

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    compressor.start();
  }
}
