package graphics;

public final class VBOConverter {
	
	public static float[] toPosColor(float[] vertices, Color color) {
		float[] ret = new float[vertices.length * 2];
		for (int x = 0; x < vertices.length; x += 3) {
			ret[2 * x] = vertices[x];
			ret[2 * x + 1] = vertices[x + 1];
			ret[2 * x + 2] = vertices[x + 2];
			ret[2 * x + 3] = color.redf();
			ret[2 * x + 4] = color.greenf();
			ret[2 * x + 5] = color.bluef();
		}
		return ret;
	}
	
	@Deprecated
	public static float[] toPosNormColor(float[] verticesAndNorms, Color color) {
		float[] ret = new float[verticesAndNorms.length * 3 / 2];
		for (int x = 0; x < verticesAndNorms.length; x += 6) {
			ret[3 * x / 2] = verticesAndNorms[x];
			ret[3 * x / 2 + 1] = verticesAndNorms[x + 1];
			ret[3 * x / 2 + 2] = verticesAndNorms[x + 2];
			ret[3 * x / 2 + 3] = verticesAndNorms[x + 3];
			ret[3 * x / 2 + 4] = verticesAndNorms[x + 4];
			ret[3 * x / 2 + 5] = verticesAndNorms[x + 5];
			ret[3 * x / 2 + 6] = color.redf();
			ret[3 * x / 2 + 7] = color.greenf();
			ret[3 * x / 2 + 8] = color.bluef();
		}
		return ret;
	}
	
	public static float[] toPosNormColor(float[] verticesAndNorms) {
		float[] ret = new float[verticesAndNorms.length * 4 / 3];
		for (int x = 0; x < verticesAndNorms.length; x += 6) {
			ret[4 * x / 3] = verticesAndNorms[x];
			ret[4 * x / 3 + 1] = verticesAndNorms[x + 1];
			ret[4 * x / 3 + 2] = verticesAndNorms[x + 2];
			ret[4 * x / 3 + 3] = verticesAndNorms[x + 3];
			ret[4 * x / 3 + 4] = verticesAndNorms[x + 4];
			ret[4 * x / 3 + 5] = verticesAndNorms[x + 5];
			ret[4 * x / 3 + 6] = 0;
			ret[4 * x / 3 + 7] = 0;
		}
		return ret;
	}
	
}
