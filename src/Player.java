public class Player {

    //player coordinate
    float camera_x[] = new float[3];
    float camera_y[] = new float[3];
    float camera_z[] = new float[3];
    float position[] = new float[3];
    float lookAt[] = new float[3];
    float step;
    float camera_angle;
    float axisTrans[][] = new float[3][3];

    public Player(float move_step, float angle_move) {
        camera_x[0] = 1;
        camera_y[1] = 1;
        camera_z[2] = -1;
        step = move_step;
        camera_angle = angle_move;
        setTransMatrix();
        //position[1]=-19.199997f;
        position[1] = -15f;
    }


    /**
     * set the Coordinate Changing matrix with the player current Coordinate
     */
    public void setTransMatrix() {
        for (int i = 0; i < 3; i++) {
            axisTrans[i][0] = camera_x[i];
            axisTrans[i][1] = camera_y[i];
            axisTrans[i][2] = camera_z[i];
        }
    }


    /**
     * rotate the player axis and make norm
     *
     * @param angle_step
     * @param axis
     */
    public void moveCamera(float angle_step, String axis) {
        float[] new_x = camera_x;
        float[] new_y = camera_y;
        float[] new_z = camera_z;
        float alfa = angle_step * camera_angle;
        switch (axis) {
            case "x":
                new_z = addVecs(multScalarInVec(camera_z, COS(alfa)), multScalarInVec(camera_y, SIN(alfa)));
                new_y = subVecs(multScalarInVec(camera_y, COS(alfa)), multScalarInVec(camera_z, SIN(alfa)));
                break;
            case "y":
                new_x = addVecs(multScalarInVec(camera_x, COS(alfa)), multScalarInVec(camera_z, SIN(alfa)));
                new_z = subVecs(multScalarInVec(camera_z, COS(alfa)), multScalarInVec(camera_x, SIN(alfa)));
                break;
            case "z":
                new_x = addVecs(multScalarInVec(camera_x, COS(alfa)), multScalarInVec(camera_y, SIN(alfa)));
                new_y = subVecs(multScalarInVec(camera_y, COS(alfa)), multScalarInVec(camera_x, SIN(alfa)));
        }
        camera_x = new_x;
        camera_y = new_y;
        camera_z = new_z;
        norm();


    }

    /**
     * given current position, calculate the only 1 step forward
     * @param x
     * @param y
     * @param z
     * @return
     */
    public float[] calcOneStep(float x, float y, float z) {
        float[] move_coordinate = new float[3];
        move_coordinate[0] = x;
        move_coordinate[1] = y;
        move_coordinate[2] = z;

        //set the matrix with the current player coordinate
        setTransMatrix();
        float[] trans_move = multVecMatrix(axisTrans, move_coordinate);
        move_coordinate[0] = position[0] + step*trans_move[0];
        move_coordinate[1] = position[1] + step*trans_move[1];
        move_coordinate[2] = position[2] + step*trans_move[2];
        return move_coordinate;
    }
    /**
     * move the player according to the direction axis and considering speed -step
     *
     * @param x
     * @param y
     * @param z
     */
    public void movePlayer(float x, float y, float z) {
        float[] move_coordinate = new float[3];
        move_coordinate[0] = x;
        move_coordinate[1] = y;
        move_coordinate[2] = z;
        //set the matrix with the current player coordinate
        setTransMatrix();
        //transform player coordinate and to world coordinate
        float[] trans_move = multVecMatrix(axisTrans, move_coordinate);
        position[0] += step * trans_move[0];
        position[1] += step * trans_move[1];
        position[2] += step * trans_move[2];


    }

    /**
     * set look at point camera coordinate
     */
    public void setLookAtCoordinate() {
        lookAt[0] = position[0] + camera_z[0];
        lookAt[1] = position[1] + camera_z[1];
        lookAt[2] = position[2] + camera_z[2];
    }

    /**
     * multVecMatrix.
     *
     * @param mat is a matrix.
     * @param v   is a vector.
     * @return the product of matrix and vector.
     */
    public float[] multVecMatrix(float[][] mat, float[] v) {
        int i, k;
        int rowsA = mat.length;
        int colsA = mat[0].length;
        float[] resVec = new float[rowsA];
        for (i = 0; i < rowsA; i++) {
            for (k = 0; k < colsA; k++) {
                resVec[i] += mat[i][k] * v[k];
            }
        }
        return resVec;
    }

    /**
     * norm.
     */
    public void norm() {
        float xNumerator = getVecNumerator(camera_x);
        float yNumerator = getVecNumerator(camera_y);
        float zNumerator = getVecNumerator(camera_z);
        for (int i = 0; i < 3; i++) {
            camera_x[i] /= xNumerator;
            camera_y[i] /= yNumerator;
            camera_z[i] /= zNumerator;
        }
    }

    /**
     * getVecNorm.
     *
     * @param x is a vector.
     * @return the norm of the vector.
     */
    public float getVecNumerator(float[] x) {
        return (float) Math.sqrt(Math.pow(x[0], 2) + Math.pow(x[1], 2) + Math.pow(x[2], 2));
    }

    public float SIN(float x) {
        return (float) Math.sin((float) x * Math.PI / 180);
    }

    public float COS(float x) {
        return (float) Math.cos((float) x * Math.PI / 180);
    }

    public float[] addVecs(float[] x, float[] y) {
        float[] result = new float[3];
        result[0] = x[0] + y[0];
        result[1] = x[1] + y[1];
        result[2] = x[2] + y[2];
        return result;
    }

    public float[] subVecs(float[] x, float[] y) {
        float[] result = new float[3];
        result[0] = x[0] - y[0];
        result[1] = x[1] - y[1];
        result[2] = x[2] - y[2];
        return result;
    }

    public float[] multScalarInVec(float[] x, float s) {
        float[] result = new float[3];
        result[0] = x[0] * s;
        result[1] = x[1] * s;
        result[2] = x[2] * s;
        return result;
    }

    /**
     * reset the player axis to the basis axis
     */
    public void resetCamera() {
        camera_x[0] = 1;
        camera_x[1] = 0;
        camera_x[2] = 0;

        camera_y[0] = 0;
        camera_y[1] = 1;
        camera_y[2] = 0;

        camera_z[0] = 0;
        camera_z[1] = 0;
        camera_z[2] = -1;
        setTransMatrix();
    }

    /**
     * set player position
     *
     * @param pos
     */
    public void setPos(float[] pos) {
        position[0] = pos[0];
        position[1] = pos[1];
        position[2] = pos[2];
    }

    public void check(float[] angle_step) {
        System.out.print(angle_step);

    }



}
