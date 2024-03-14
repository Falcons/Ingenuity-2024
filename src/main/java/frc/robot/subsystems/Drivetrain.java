// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.Constants.DriveConstants;

public class Drivetrain extends SubsystemBase {
  private final Pigeon2 gyro = new Pigeon2(DriveConstants.pigeonID);
  private final CANSparkMax frontRight = new CANSparkMax(DriveConstants.frontRightID,  MotorType.kBrushless);
  private final CANSparkMax frontLeft = new CANSparkMax(DriveConstants.frontLeftID,  MotorType.kBrushless);
  private final CANSparkMax backRight = new CANSparkMax(DriveConstants.backRightID,  MotorType.kBrushless);
  private final CANSparkMax backLeft = new CANSparkMax(DriveConstants.backLeftID,  MotorType.kBrushless);
  
  private final RelativeEncoder frontRightEncoder = frontRight.getEncoder();
  private final RelativeEncoder frontLeftEncoder = frontLeft.getEncoder();
  private final RelativeEncoder backRightEncoder = backRight.getEncoder();
  private final RelativeEncoder backLeftEncoder = backLeft.getEncoder();

  private final DifferentialDrive drive = new DifferentialDrive(frontLeft::set, frontRight::set);

  private final DifferentialDriveOdometry m_odometry;
  private final DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(0.683);

  private final DifferentialDrivePoseEstimator poseEstimator = 
    new DifferentialDrivePoseEstimator(
      kinematics, 
      gyro.getRotation2d(), 
      frontLeftEncoder.getPosition(), 
      frontRightEncoder.getPosition(), 
      new Pose2d(),
      VecBuilder.fill(0.05, 0.05, Units.degreesToRadians(5)),
      VecBuilder.fill(0.5,0.5,Units.degreesToRadians(30)));

  public Drivetrain() {
    frontRight.restoreFactoryDefaults();
    frontLeft.restoreFactoryDefaults();
    backRight.restoreFactoryDefaults();
    backLeft.restoreFactoryDefaults();

    Timer.delay(4);

    drive.setSafetyEnabled(false);

    frontLeft.setInverted(true);
    backLeft.setInverted(true);

    frontRight.setInverted(false);
    backRight.setInverted(false);

    backRight.follow(frontRight);
    backLeft.follow(frontLeft);

    resetEncoders();
    gyro.setYaw(0);
    
    frontRightEncoder.setPositionConversionFactor(DriveConstants.RevToMetre);
    frontLeftEncoder.setPositionConversionFactor(DriveConstants.RevToMetre);

    m_odometry = new DifferentialDriveOdometry(gyro.getRotation2d(), frontLeftEncoder.getPosition(), frontRightEncoder.getPosition());
  }

  public void updateOdometry() {
    poseEstimator.update(
      gyro.getRotation2d(), 
      frontLeftEncoder.getPosition(), 
      frontRightEncoder.getPosition());
  }

  public void setMaxSpeed(double speed) {
    drive.setMaxOutput(speed);
  }

  public void setBrakeMode() {
    frontRight.setIdleMode(IdleMode.kBrake);
    frontLeft.setIdleMode(IdleMode.kBrake);
    backRight.setIdleMode(IdleMode.kBrake);
    backLeft.setIdleMode(IdleMode.kBrake);
  }

  public void setCoastMode() {
    frontRight.setIdleMode(IdleMode.kCoast);
    frontLeft.setIdleMode(IdleMode.kCoast);
    backRight.setIdleMode(IdleMode.kCoast);
    backLeft.setIdleMode(IdleMode.kCoast);
  }

  public void stopMotors() {
    frontLeft.stopMotor();
    frontRight.stopMotor();
  }

  public void invertMotors() {
    frontLeft.setInverted(true);
    backLeft.setInverted(true);

    frontRight.setInverted(false);
    backRight.setInverted(false);
  }

  public void resetEncoders() {
    frontLeftEncoder.setPosition(0);
    frontRightEncoder.setPosition(0);
    backLeftEncoder.setPosition(0);
    backRightEncoder.setPosition(0);
  }

  public double getDistance() {
    return (frontLeftEncoder.getPosition() + frontRightEncoder.getPosition()) / 2.0;
  }

  public void setSafteyEnabled(boolean state) {
    drive.setSafetyEnabled(state);
  }
  
  public void tankDrive(double leftSpeed, double rightSpeed){
    drive.tankDrive(leftSpeed, rightSpeed);
  }

  public void setYaw(double value) {
    gyro.setYaw(value);
  }

  public void arcadeDriveManual(double speed, double rotation) {
    if (speed < 0.1 && speed > -0.1) {
      speed = 0;
    }
    if (rotation < 0.1 && rotation > -0.1) {
      rotation = 0;
    }

      frontLeft.set(speed + rotation);
      frontRight.set(speed - rotation);
  }

  public void arcadeDrive(double speed, double rotation) {
    drive.arcadeDrive(speed, rotation);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());

    m_odometry.update(gyro.getRotation2d(), frontLeftEncoder.getPosition(), frontRightEncoder.getPosition());
  }

  public double getAngle() {
    return gyro.getAngle();
  }

  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }
}