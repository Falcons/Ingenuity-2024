// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.HashMap;
import java.util.Map;

public final class Constants {
    public static final class DriveConstants {
        public static final int frontRightID = 3;
        public static final int frontLeftID = 4;
        public static final int backRightID = 1;
        public static final int backLeftID = 2;
        
        public static final double RevToMetre = 1188.0 * Math.PI / 82991.96;
        public static final double RPMToMetresPerSecond = 1188.0 * Math.PI / 4979517.6;

        public static final int pigeonID = 12;

        HashMap<Double, Double> shooterDistanceValues = new HashMap<Double, Double> ();
    }
//test
    public static final class IntakeConstants {
        public static final int pivotID = 10;
        public static final int wheelID = 11;

        public static final double intakeOutAngle = 0.07;
        public static final double intakeInAngle = 0.66;

        public static final int intakeBottomLimit = 3;
    }

    public static final class ShooterConstants {
        public static final int leftFlywheelID = 6;
        public static final int rightFlywheelID = 5;

        public static final int pivotID = 7;
        public static final double pivotThruBoreToRadians = 2.0 * Math.PI;
        public static final double thruBoreZeroOffset = 0;


        public static final int pivotBottomLimitPort = 0;

        public static final double speakerDepth = 0.89;


    }

    public static final class ClimbConstants {
        public static final int leftClimbID = 9;
        public static final int rightClimbID = 8;

        public static final int leftClimbLimitPort = 1;
        public static final int rightClimbLimitPort = 2;

    }
}
