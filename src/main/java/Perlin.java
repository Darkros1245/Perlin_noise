import javax.vecmath.Vector2d;
import java.util.Random;

public class Perlin {
	private int width;
	private int height;
	private int cell_size;
	private Vector2d[][] gradiant_vectors;

	public Perlin(int width, int height, int cell_size) {
		this.width = width;
		this.height = height;
		this.cell_size = cell_size;
		this.make_gradiant_vectors();
	}

	private void make_gradiant_vectors() {
		assert this.width % this.cell_size == 0 && this.height % this.cell_size == 0; // This shall be fixed later so that the last grid-cell can be smaller
		Random random = new Random();
		this.gradiant_vectors = new Vector2d[(this.width / this.cell_size) + 1][(this.height / this.cell_size) + 1];
		for (int x = 0; x < this.width / this.cell_size + 1; ++x) {
			for (int y = 0; y < this.height / this.cell_size + 1; ++y) {
				this.gradiant_vectors[x][y] = new Vector2d(Helper.map(random.nextDouble(), 0, 1, -1, 1), Helper.map(random.nextDouble(), 0, 1, -1, 1));
				this.gradiant_vectors[x][y].normalize();
			}
		}
	}

	private double bilerp(double dx, double dy, double[] dots) {
		assert dx >= 0 && dx <= 1;
		assert dy >= 0 && dy <= 1;
		assert dots.length == 4;
		double a = dots[0] * (1 - dx) + dots[1] * dx;
		double b = dots[2] * (1 - dx) + dots[3] * dx;
		return a * (1 - dy) + b * dy;
	}

	private double smooth(double x) {
		return 6 * x*x*x*x*x - 15 * x*x*x*x + 10 * x*x*x;
	}


	public double noise(int x, int y) {
		int index_x_l = x / this.cell_size;
		int index_y_b = y / this.cell_size;
		int index_x_r = index_x_l + 1;
		int index_y_t = index_y_b + 1;
		index_x_r = Helper.clamp(0, this.gradiant_vectors.length - 1, index_x_r);
		index_y_t = Helper.clamp(0, this.gradiant_vectors[0].length - 1, index_y_t);

		Vector2d dv_bl = new Vector2d(x - index_x_l, y - index_y_b);
		Vector2d dv_br = new Vector2d(x - index_x_r, y - index_y_b);
		Vector2d dv_tl = new Vector2d(x - index_x_l, y - index_y_t);
		Vector2d dv_tr = new Vector2d(x - index_x_r, y - index_y_t);
		dv_bl.normalize();
		dv_br.normalize();
		dv_tl.normalize();
		dv_tr.normalize();

		double[] dots = new double[] {
			this.gradiant_vectors[index_x_l][index_y_b].dot(dv_bl),
			this.gradiant_vectors[index_x_r][index_y_b].dot(dv_br),
			this.gradiant_vectors[index_x_l][index_y_t].dot(dv_tl),
			this.gradiant_vectors[index_x_r][index_y_t].dot(dv_tr),
		};

		double dx = Helper.map(x - index_x_l * this.cell_size, 0, this.cell_size, 0, 1);
		double dy = Helper.map(y - index_y_b * this.cell_size, 0, this.cell_size, 0, 1);

		double noise = this.smooth(Helper.map(bilerp(dx, dy, dots), -1, 1, 0, 1));

		return noise;
	}
}
