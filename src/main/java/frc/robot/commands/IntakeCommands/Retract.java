// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.IntakeCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.Intake;

public class Retract extends Command {
  private final Intake intake;
  private final PIDController pid;
  public Retract(Intake intake) {
    this.intake = intake;
    this.pid = new PIDController(1.5, 0, 0);
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("Retract Start");
    intake.IntakeNote(0);
    pid.reset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double speed = -pid.calculate(intake.getIntakeAngle(), IntakeConstants.intakeInAngle);
    intake.pivotSpeed(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("Retract End");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return pid.atSetpoint();
  }
}
