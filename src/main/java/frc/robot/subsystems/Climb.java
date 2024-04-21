// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ClimbConstants;

public class Climb extends SubsystemBase {
  private final CANSparkMax leftClimb = new CANSparkMax(ClimbConstants.leftClimbID, MotorType.kBrushless);
  private final CANSparkMax rightClimb = new CANSparkMax(ClimbConstants.rightClimbID, MotorType.kBrushless);

  private final RelativeEncoder leftClimbEncoder = leftClimb.getEncoder();
  private final RelativeEncoder rightClimbEncoder = rightClimb.getEncoder();

  private final DigitalInput leftClimbLimit = new DigitalInput(ClimbConstants.leftClimbLimitDIOPort);
  private final DigitalInput rightClimbLimit = new DigitalInput(ClimbConstants.rightClimbLimitDIOPort);

  public Climb() {
    leftClimb.restoreFactoryDefaults();
    rightClimb.restoreFactoryDefaults();

    rightClimb.setInverted(true);

    leftClimb.setSmartCurrentLimit(40);
    rightClimb.setSmartCurrentLimit(40);
    setCoastMode();
  }

  public void setLeftClimb(double speed){
    leftClimb.set(speed);
  }
  public void setRightClimb(double speed){
    rightClimb.set(speed);
  }

  public void setClimb(double leftSpeed, double rightSpeed) {
    leftClimb.set(leftSpeed);
    rightClimb.set(rightSpeed);
  }

  public void stopClimb() {
    leftClimb.stopMotor();
    rightClimb.stopMotor();
  }

  // Hard Limits
  public boolean getHardLeftBottomLimit() {
    return leftClimbLimit.get();
  }

  public boolean getHardRightBottomLimit() {
    return rightClimbLimit.get();
  }

  //
  public void setBrakeMode() {
    leftClimb.setIdleMode(IdleMode.kBrake);
    rightClimb.setIdleMode(IdleMode.kBrake);
  }

  public void setCoastMode() {
    leftClimb.setIdleMode(IdleMode.kCoast);
    rightClimb.setIdleMode(IdleMode.kCoast);
  }
  
  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Climb/Left Climb Limit", leftClimbLimit.get());
    SmartDashboard.putBoolean("Climb/Right Climb Limit", rightClimbLimit.get());
    SmartDashboard.putNumber("Climb/Left Climb Encoder", leftClimbEncoder.getPosition());
    SmartDashboard.putNumber("Climb/Right Climb Encoder", rightClimbEncoder.getPosition());
  }

  public Command Up(double speed) {
    return this.startEnd(() -> this.setClimb(speed, speed), () -> this.stopClimb());
  }

  public Command Down(double speed) {
    return this.startEnd(() -> this.setClimb(-speed, -speed), () -> this.stopClimb());
  }
}
