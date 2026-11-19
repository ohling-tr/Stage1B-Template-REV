// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.mechanisms;

import static org.wpilib.units.Units.Seconds;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import first.robot.simulation.SingleFlywheelSim;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.framework.RobotBase;
import org.wpilib.hardware.bus.CANPort;

/** Add your docs here. */
public class IntakeLauncher implements Mechanism {

  private SparkMax motor = new SparkMax(CANPort.CAN_S0, 4, MotorType.kBrushless);
  private final SingleFlywheelSim sim = SingleFlywheelSim.forIntakeLauncher(motor);

  public IntakeLauncher() {
    setDefaultCommand(idle());
  }

  public void periodic() {
    if (RobotBase.isSimulation()) {
      sim.periodic();
    }
  }

  public Command idle() {
    return run(coroutine -> {
          while (true) {
            motor.setThrottle(0.0);
            coroutine.yield();
          }
        })
        .named("idle");
  }

  public Command shoot() {
    return run(coroutine -> {
          while (true) {
            motor.setThrottle(0.9);
            coroutine.wait(Seconds.of(2.0));
            coroutine.yield();
          }
        })
        .named("shoot");
  }

  public Command intake() {
    return run(coroutine -> {
          while (true) {
            motor.setThrottle(0.8);
            coroutine.yield();
          }
        })
        .named("intake");
  }

  public Command outtake() {
    return run(coroutine -> {
          while (true) {
            motor.setThrottle(-0.8);
            coroutine.yield();
          }
        })
        .named("outtake");
  }
}
