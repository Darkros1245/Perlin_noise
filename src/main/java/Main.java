import java.io.*;
import javax.vecmath.Vector2d;
import java.util.stream.DoubleStream;

public class Main {
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static final int CELL_SIZE_1 = 15;
	public static final int CELL_SIZE_2 = 20;
	public static final int CELL_SIZE_3 = 24;
	public static final int CELL_SIZE_4 = 30;
	public static final int CELL_SIZE_5 = 40;
	public static final int CELL_SIZE_6 = 60;
	public static final int CELL_SIZE_7 = 120;

	public static void main(String[] args) {
		write_ppm_file("noise.ppm");
	}

	public static void write_ppm_file(String path) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write("P3\n" + String.valueOf(WIDTH) + " " + String.valueOf(HEIGHT) + "\n255\n");
			color_surface(writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void color_surface(BufferedWriter writer) throws IOException {
		Perlin perlin_1 = new Perlin(WIDTH, HEIGHT, CELL_SIZE_1);
		Perlin perlin_2 = new Perlin(WIDTH, HEIGHT, CELL_SIZE_2);
		Perlin perlin_3 = new Perlin(WIDTH, HEIGHT, CELL_SIZE_3);
		Perlin perlin_4 = new Perlin(WIDTH, HEIGHT, CELL_SIZE_4);
		Perlin perlin_5 = new Perlin(WIDTH, HEIGHT, CELL_SIZE_5);
		Perlin perlin_6 = new Perlin(WIDTH, HEIGHT, CELL_SIZE_6);
		Perlin perlin_7 = new Perlin(WIDTH, HEIGHT, CELL_SIZE_7);
		double p_noise;
		double start_amplitude = 0.5;
		double[] amplitudes = new double[] {start_amplitude / 64, start_amplitude / 32, start_amplitude / 16, start_amplitude / 8, start_amplitude / 4, start_amplitude / 2, start_amplitude};
		double normalization_factor = 1 / DoubleStream.of(amplitudes).sum();
		int r, g, b;

		for (int y = 0; y < HEIGHT; ++y) {
			for (int x = 0; x < WIDTH; ++x) {
				p_noise = 0;
				p_noise += perlin_1.noise(x, y) * amplitudes[0];
				p_noise += perlin_2.noise(x, y) * amplitudes[1];
				p_noise += perlin_3.noise(x, y) * amplitudes[2];
				p_noise += perlin_4.noise(x, y) * amplitudes[3];
				p_noise += perlin_5.noise(x, y) * amplitudes[4];
				p_noise += perlin_6.noise(x, y) * amplitudes[5];
				p_noise += perlin_7.noise(x, y) * amplitudes[6];
				p_noise *= normalization_factor;

				r = (int)(75 * p_noise);
				g = (int)(0 * p_noise);
				b = (int)(130 * p_noise);

				writer.write(String.format("%d %d %d\t", r, g, b));
			}
			writer.write("\n");
		}
	}
}
