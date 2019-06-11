//Inbar Demuth 204885370
//Yakir Pinchas 203200530

public class PlayerAxis {
	
	//player coordinate
	float x[] = new float[3];
	float y[] = new float[3];
	float z[] = new float[3];
	
	//player position
	float position[] = new float[3];
	
	//look at point
	float lookAt[] = new float[3];
	
	//move step in every press(rate move)
	float step;
	
	//angle for rotation rate
	float angle;
	
	//translate matrix to player axis
	float coordiTrans[][] = new float[3][3];
			
	public PlayerAxis(float move_step, float angle_move) {
		x[0] = 1;
		y[1] = 1;
		z[2] = -1;
		step = move_step;
		angle = angle_move;
		setTransMatrix();
	}
	
	/**
	 * rotate the player axis to get camera movement
	 * and then normalize the player axis
	 * @param angle_step
	 * @param axis
	 */
	public void cameraMoving(float angle_step, String axis) {
		
		float[] new_x = x;
		float[] new_y = y;
		float[] new_z = z;
		float alfa = angle_step * angle;
		
		switch(axis) {
		case "x":
			new_z = addVectors(multScalar(z, COS(alfa)), multScalar(y, SIN(alfa)));
			new_y = subVectors(multScalar(y, COS(alfa)), multScalar(z, SIN(alfa)));
			break;
		case "y":
			new_x = addVectors(multScalar(x, COS(alfa)), multScalar(z, SIN(alfa)));
			new_z = subVectors(multScalar(z, COS(alfa)), multScalar(x, SIN(alfa)));
			break;
		case "z":
			new_x = addVectors(multScalar(x, COS(alfa)), multScalar(y, SIN(alfa)));
			new_y = subVectors(multScalar(y, COS(alfa)), multScalar(x, SIN(alfa)));
		}
		
		x = new_x;
		y = new_y;
		z = new_z;
		normalization();
	}
	
	/**
	 * move the player in the space, get the direction move in basis axis,
	 * translate it to the player coordinate and then move in rate of "step"
	 * @param x_move
	 * @param y_move
	 * @param z_move
	 */
	public void playerMoving(float x_move, float y_move, float z_move) {
		float[] move = new float[3];
		move[0] = x_move;
		move[1] = y_move;
		move[2] = z_move;
		
		//set the matrix with the current player coordinate
		setTransMatrix();
		float[] trans_move = transVector(move);
		position[0] = position[0] + step*trans_move[0];
		position[1] = position[1] + step*trans_move[1];
		position[2] = position[2] + step*trans_move[2];
	}
	
	/**
	 * create the look at point for the "gluLookAt" func
	 */
	public void setLookAtPoint() {
		lookAt[0] = position[0] + z[0];
		lookAt[1] = position[1] + z[1];
		lookAt[2] = position[2] + z[2];
	}
	
	/**
	 * set the Coordinate Changing matrix with the player current Coordinate
	 */
	public void setTransMatrix() {
		for (int i = 0; i < 3; i++) {
			coordiTrans[i][0] = x[i];
			coordiTrans[i][1] = y[i];
			coordiTrans[i][2] = z[i];
		}
	}
	
	/**
	 * get vector in the player coordinate and change it to 
	 * world coordinate
	 * @param v
	 * @return
	 */
	public float[] transVector(float[] v) {
		return multiplyMatrixInVector(coordiTrans, v);
	}
	
	/**
	 * get the vector length
	 * @param v
	 * @return
	 */
	public float vectorLen(float[] v) {
		return (float)Math.sqrt(Math.pow(v[0], 2)
                + Math.pow(v[1], 2) + Math.pow(v[2], 2));
	}
	
	/**
	 * normalize the player coordinate
	 */
	public void normalization() {
		float x_len = vectorLen(x);
		float y_len = vectorLen(y);
		float z_len = vectorLen(z);
		
		for (int i = 0; i < 3; i++) {
			x[i] = x[i] / x_len;
			y[i] = y[i] / y_len;
			z[i] = z[i] / z_len;
		}
	}
	
	/**
     * calculate the multiplication of matrix-vector
     * @param a is matrix.
     * @param x is vector.
     * @return multiplication (y = A * x)
     */
    public static float[] multiplyMatrixInVector(float[][] a, float[] x) {
        int m = a.length;
        int n = a[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        float[] y = new float[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i] += a[i][j] * x[j];
        return y;
    }
	
	public float SIN(float x) {
		return (float)java.lang.Math.sin((float)x*3.14159/180);
	}
	
	public float COS(float x) {
		return (float)java.lang.Math.cos((float)x*3.14159/180);
	}
	
	public float[] addVectors(float[] x, float[] y) {
		float[] result = new float[3];
		result[0] = x[0]+y[0];
		result[1] = x[1]+y[1];
		result[2] = x[2]+y[2];
		return result;
	}
	
	public float[] subVectors(float[] x, float[] y) {
		float[] result = new float[3];
		result[0] = x[0]-y[0];
		result[1] = x[1]-y[1];
		result[2] = x[2]-y[2];
		return result;
	}
	
	public float[] multScalar(float[] x, float s) {
		float[] result = new float[3];
		result[0] = x[0]*s;
		result[1] = x[1]*s;
		result[2] = x[2]*s;
		return result;
	}
	
	/*public int rotation(float angle, String axis) {
		float[][] R = new float[4][4];
		switch(axis) {
		case "x":
			R[0][0] = 1;
			R[1][1] = COS(angle);
			R[1][2] = -SIN(angle);
			R[2][1] = SIN(angle);
			R[2][2] = COS(angle);
			R[3][3] = 1;
			break;
		case "y":
			R[0][0] = COS(angle);
			R[0][2] = -SIN(angle);
			R[1][1] = 1;
			R[2][0] = SIN(angle);
			R[2][2] = COS(angle);
			R[3][3] = 1;
			break;
		case "z":
			R[0][0] = COS(angle);
			R[0][1] = -SIN(angle);
			R[1][0] = SIN(angle);
			R[1][1] = COS(angle);
			R[2][2] = 1;
			R[3][3] = 1;
		}
	
		return 0;
	}*/
}
