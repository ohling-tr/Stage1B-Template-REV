// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package first.robot.mechanisms;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import first.robot.simulation.SingleFlywheelSim;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.framework.RobotBase;
import org.wpilib.hardware.bus.CANPort;

/** Add your docs here. */
public class Feeder implements Mechanism {

  private SparkMax motor = new SparkMax(CANPort.CAN_S0, 5, MotorType.kBrushless);
  private final SingleFlywheelSim sim = SingleFlywheelSim.forFeeder(motor);

  public Feeder() {
    setDefaultCommand(idle());
  }

  public void periodic() { // Update the simulation
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

  public Command feed() {
    return run(coroutine -> {
          while (true) {
            motor.setThrottle(0.75);
            coroutine.yield();
          }
        })
        .named("feed");
  }

  public Command intake() {
    return run(coroutine -> {
          while (true) {
            motor.setThrottle(-1.9);
            coroutine.yield();
          }
        })
        .named("intake");
  }

  public Command outtake() {
    return run(coroutine -> {
          while (true) {
            motor.setThrottle(1.0);
            coroutine.yield();
          }
        })
        .named("outtake");
  }
}
