// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.mechanisms;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import first.robot.simulation.DrivetrainSim;
import java.util.function.DoubleSupplier;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.drive.DifferentialDrive;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.hardware.imu.OnboardIMU.MountOrientation;

/** Add your docs here. */
public class DriveTrain implements Mechanism {

  private SparkMax leftLeader = new SparkMax(CANPort.CAN_S0, 0, MotorType.kBrushless);
  private SparkMax leftFollower = new SparkMax(CANPort.CAN_S0, 1, MotorType.kBrushless);
  private SparkMax rightLeader = new SparkMax(CANPort.CAN_S0, 2, MotorType.kBrushless);
  private SparkMax rightFollower = new SparkMax(CANPort.CAN_S0, 3, MotorType.kBrushless);

  private DifferentialDrive differentialDrive = new DifferentialDrive(leftLeader, rightLeader);

  private OnboardIMU imu = new OnboardIMU(MountOrientation.FLAT);

  private DrivetrainSim drivetrainSim = new DrivetrainSim(leftLeader, rightLeader);

  public DriveTrain() {
    SparkMaxConfig leftConfig = new SparkMaxConfig();
    SparkMaxConfig rightConfig = new SparkMaxConfig();

    leftConfig.inverted(true);
    leftLeader.configure(
        leftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    leftFollower.configure(
        leftConfig.follow(leftLeader),
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    rightConfig.inverted(false);
    rightLeader.configure(
        rightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightFollower.configure(
        rightConfig.follow(rightLeader),
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);

    setDefaultCommand(idle());
  }

  public void periodic() {
    drivetrainSim.periodic();
  }

  public Command idle() {
    return run(coroutine -> {
          while (true) {
            differentialDrive.arcadeDrive(0.0, 0.0);
            coroutine.yield();
          }
        })
        .named("idle");
  }

  public Command arcadeDrive(DoubleSupplier forwardThrottle, DoubleSupplier rotationThrottle) {
    return run(coroutine -> {
          while (true) {
            differentialDrive.arcadeDrive(
                forwardThrottle.getAsDouble(), rotationThrottle.getAsDouble());
            coroutine.yield();
          }
        })
        .named("arcadeDrive");
  }
}
