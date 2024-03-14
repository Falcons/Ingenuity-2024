// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.AutoCommands.OneNoteAuto;
import frc.robot.commands.ClimbCommands.ClimbUp;
import frc.robot.commands.DriveCommands.ArcadeDriveCmd;
import frc.robot.commands.DriveCommands.CentretoNote;
import frc.robot.commands.DriveCommands.CentretoSpeaker;
import frc.robot.commands.DriveCommands.DriveStraight;
import frc.robot.commands.DriveCommands.FastMode;
import frc.robot.commands.DriveCommands.InvertDrive;
import frc.robot.commands.DriveCommands.RotateToAngle;
import frc.robot.commands.DriveCommands.SlowMode;
import frc.robot.commands.IntakeCommands.Extend;
import frc.robot.commands.IntakeCommands.Retract;
import frc.robot.commands.ShooterCommands.Down;
import frc.robot.commands.ShooterCommands.SetShooterFixed;
import frc.robot.commands.ShooterCommands.SetShooterPosition;
import frc.robot.commands.ShooterCommands.Shoot;
import frc.robot.commands.ShooterCommands.Up;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LimelightIntake;
import frc.robot.subsystems.LimelightShooter;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterPivot;

public class RobotContainer {
  private final Drivetrain drivetrain = new Drivetrain();
  private final Climb climb = new Climb();
  private final Intake intake = new Intake();
  private final Shooter shooter = new Shooter();
  private final ShooterPivot shooterpivot = new ShooterPivot();
  private final LimelightIntake limelightintake = new LimelightIntake();
  private final LimelightShooter limelightshooter = new LimelightShooter();

  private final CommandXboxController driver = new CommandXboxController(0);
  private final CommandXboxController operator = new CommandXboxController(1);

  public RobotContainer() {
    drivetrain.setDefaultCommand(
      new ArcadeDriveCmd(drivetrain, 
      () -> -driver.getLeftY(), 
      () -> -driver.getRightX()));

    climb.setDefaultCommand(
      new ClimbUp(climb, 
      () -> operator.getLeftY(), 
      () -> operator.getRightY())
    );

    configureBindings();
  }

  private void configureBindings() {
    shooterpivot.stopShooterPivot();

    // Centring
    driver.leftBumper().whileTrue(new CentretoNote(drivetrain, limelightintake));
    driver.rightBumper().whileTrue(new CentretoSpeaker(drivetrain, limelightshooter));

    // Shooter adjustment
    driver.povUp().onTrue(new SetShooterFixed(shooterpivot, 42));

    //driver.povUp().onTrue(new SetShooterFixed(shooterpivot, 56.93));
    driver.povDown().onTrue(new SetShooterFixed(shooterpivot, 36.3));
    //driver.povRight().onTrue(new SetShooterFixed(shooterpivot, 0.93));
    
    driver.povLeft().whileTrue(new SetShooterPosition(shooterpivot, limelightshooter));

    //invert
    driver.a().whileTrue(new InvertDrive(drivetrain));

    //Fast and Slow
    driver.x().whileTrue(new FastMode(drivetrain));
    driver.rightTrigger().whileTrue(new SlowMode(drivetrain));
/*
    // Intake wheels manual
    operator.x().whileTrue(intake.IntakeNoteCmd(0.3));
    operator.a().whileTrue(intake.EjectNoteCmd(1));
    //Intake Setpoint Manual
    operator.povUp().onTrue(new Extend(intake));
    operator.povDown().onTrue(new Retract(intake));
    //Extend -> Intake -> Retract
    operator.rightBumper().onTrue(
      new Extend(intake)
      .andThen(intake.IntakeNoteCmd(0.5)).until(intake::hasNote)
      .andThen(new Retract(intake)));
*/
    // Shooter Instant
    operator.y().whileTrue(shooter.Shoot(1, 0.95));
    // shooter variable
    operator.leftTrigger(0.3).whileTrue(shooter.Shoot(0.5, 0.5).unless(operator.leftTrigger(0.9)));
    operator.leftTrigger(0.9).whileTrue(shooter.Shoot(1, 0.95));

    // Shooter Pivot Manual
    operator.povRight().whileTrue(new Down(shooterpivot, 0.1));
    operator.povLeft().whileTrue(new Up(shooterpivot, 0.1));
  }

  public Command getAutonomousCommand() {
    return new OneNoteAuto(drivetrain, intake, shooter, shooterpivot);
  }
}
