class Helper {
	public static int clamp(int min, int max, int value) {
		if (value < min) {
			value = min;
		}
		else if (value > max) {
			value = max;
		}
		return value;
	}

	public static double clamp(double min, double max, double value) {
		if (value < min) {
			value = min;
		}
		else if (value > max) {
			value = max;
		}
		return value;
	}

	public static double map(double value, double actual_min, double actual_max, double allowed_min, double allowed_max) {
		return ((allowed_max - allowed_min) * (value - actual_min)) / (actual_max - actual_min) + allowed_min;

	}
}
