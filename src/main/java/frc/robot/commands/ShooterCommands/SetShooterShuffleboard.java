// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ShooterCommands;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterPivot;

public class SetShooterShuffleboard extends Command {
  private final ShooterPivot shooterpivot;
  private final PIDController pidToSetpoint;
  private final ArmFeedforward armFF;
  public SetShooterShuffleboard(ShooterPivot shooterpivot) {
    this.shooterpivot = shooterpivot;
    this.pidToSetpoint = new PIDController(0.3, 0, 0);
    this.armFF = new ArmFeedforward(0, 0.35, 1.95, 0.02);
    addRequirements(shooterpivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("PivotShuffle Start");
    shooterpivot.setBrakeMode();
    pidToSetpoint.reset();
    pidToSetpoint.setTolerance(1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double angle = SmartDashboard.getNumber("Pivot/AngleSlider", 36.3);
    SmartDashboard.putNumber("Pivot/AngleSlider", angle);
    pidToSetpoint.setSetpoint(angle);
    //double FFOutput = armFF.calculate(shooterpivot.getDegreesFromRaw() * ShooterConstants.degreesToRadians, 0);
    double PIDToSetpointOutput = pidToSetpoint.calculate(shooterpivot.getDegreesFromRaw());
    //double PIDFixedOutput = pidFixed.calculate(shooterpivot.getDegreesFromRaw(), angle);
    double speed;

    if (pidToSetpoint.getPositionError() < 0) {
      speed = PIDToSetpointOutput / 5.0;
    } else {
       speed = PIDToSetpointOutput;
    }

    if (shooterpivot.getSoftUpperLimit() && speed > 0 ) {
      speed = 0;
    } else if (shooterpivot.getSoftLowerLimit() && speed < 0) {
      speed = 0;
    }

    shooterpivot.setVoltage(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("PivotShuffle End");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
