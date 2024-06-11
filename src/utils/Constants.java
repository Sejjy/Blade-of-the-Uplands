package utils;

public class Constants {

	public static class Directions {
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}

	public static class PlayerConstants {
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int JUMP = 2;
		public static final int FALLING = 3;
		public static final int GROUND = 4;
		public static final int HIT = 5;
		public static final int ATTACK_1 = 6;
		public static final int ATTACK_JUMP_1 = 7;
		public static final int ATTACK_JUMP_2 = 8;

		public static int GetSpriteAmount(int player_action) {
			switch (player_action) {
			case ATTACK_1:
			case RUNNING:
				return 6;
			case ATTACK_JUMP_2:
				return 5;
			case ATTACK_JUMP_1:
			case HIT:
			case IDLE:
				return 4;
			case JUMP:
				return 3;
			case FALLING:
			case GROUND:
				return 2;
			default:
				return 1;
			}
		}
	}

}