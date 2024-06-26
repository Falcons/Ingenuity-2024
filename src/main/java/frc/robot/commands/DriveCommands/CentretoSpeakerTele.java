// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.DriveCommands;

import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LimelightShooter;

public class CentretoSpeakerTele extends Command {
  Drivetrain drivetrain;
  LimelightShooter limelightShooter;
  private final PIDController pid;
   private final Supplier<Double> speed;

  public CentretoSpeakerTele(Drivetrain d, LimelightShooter ls, Supplier<Double> speed) {
    this.drivetrain = d;
    this.limelightShooter = ls;
    this.pid = new PIDController(0.01, 0.01, 0);
    this.speed = speed;
    addRequirements(drivetrain, limelightShooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("CentreToSpeaker Start");
    pid.reset();
    pid.setTolerance(1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double realTimeSpeed = speed.get();
    double leftSpeed;
    double rightSpeed;

    if (limelightShooter.getDoubleEntry("tv") == 1.0) {
      leftSpeed = -pid.calculate(limelightShooter.getX(), 0);
      rightSpeed = pid.calculate(limelightShooter.getX(), 0);
    } else {
      leftSpeed = 0;
      rightSpeed = 0;
    }

    drivetrain.tankDrive(leftSpeed + realTimeSpeed, rightSpeed + realTimeSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopMotors();
    System.out.println("CentreToSpeaker End");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
