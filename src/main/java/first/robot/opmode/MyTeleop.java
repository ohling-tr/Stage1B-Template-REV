/*
 * Copyright 2026 FRCSoftware
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package first.robot.opmode;

import first.robot.Robot;
import org.wpilib.command3.button.CommandXboxController;
import org.wpilib.opmode.PeriodicOpMode;
import org.wpilib.opmode.Teleop;

@Teleop
public class MyTeleop extends PeriodicOpMode {
  private final Robot robot;
  private CommandXboxController xbox = new CommandXboxController(0);

  /** The Robot instance is passed into the opmode via the constructor. */
  public MyTeleop(Robot robot) {
    this.robot = robot;

    xbox.leftBumper()
        .whileTrue(this.robot.intakeLauncher.intake())
        .whileTrue(this.robot.feeder.intake());

    xbox.rightBumper()
        .whileTrue(this.robot.intakeLauncher.shoot())
        .whileTrue(this.robot.feeder.feed());

    xbox.a().whileTrue(this.robot.intakeLauncher.outtake()).whileTrue(this.robot.feeder.outtake());

    this.robot.driveTrain.setDefaultCommand(
        this.robot.driveTrain.arcadeDrive(() -> xbox.getLeftY(), () -> xbox.getRightX()));
  }

  @Override
  public void periodic() {
    /* Called periodically (set time interval) while the robot is enabled. */
  }
}
