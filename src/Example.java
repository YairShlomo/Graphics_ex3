
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;


@SuppressWarnings("serial")
public class Example extends GLJPanel implements GLEventListener {

    //01.iha Start
    private static float angleX = 0;
    private static float angleY = 0;
    private static float angleZ = 0;
    //01.iha End

    private static int width;
    private static int height;
    private FPSAnimator animator;

    private GLModel chairModel = null;

    public Example() {
        setFocusable(true);
        addGLEventListener(this);
        animator = new FPSAnimator(this, 60, false);
        animator.start();
        width = height = 800;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glLoadIdentity();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glTranslatef(0,0,-1);
        //01.iha Start

        gl.glRotatef(angleX, 1f, 0f, 0f);
        gl.glRotatef(angleY, 0f, 1f, 0f);
        gl.glRotatef(angleZ, 0f, 0f, 1f);

        if (angleX > 359) angleX = 0; else angleX += 0.7f;
        if (angleY > 359) angleY = 0; else angleY += 0.7f;
        if (angleZ > 359) angleZ = 0; else angleZ += 0.7f;
        //gl.glScalef(0.01f, 0.01f, 0.01f); //here
        gl.glScalef(.0003f, .0003f, .0003f); //here
        //01.iha End

        chairModel.opengldraw(gl);

        gl.glFlush();
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glEnable(GL2.GL_NORMALIZE);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU glu = new GLU();

        if (false == loadModels(gl)) {
            System.exit(1);
        }

        setLight(gl);

        glu.gluPerspective(1, (double) getWidth() / getHeight(), 0.3, 50);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    private void setLight(GL2 gl) {

        gl.glEnable(GL2.GL_LIGHTING);

        float SHINE_ALL_DIRECTIONS = 1;
        float[] lightPos = { -30, 30, 30, SHINE_ALL_DIRECTIONS };
        float[] lightColorAmbient = { 0.02f, 0.02f, 0.02f, 1f };
        float[] lightColorSpecular = { 0.9f, 0.9f, 0.9f, 1f };

        // Set light parameters.
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightColorSpecular, 0);
        gl.glEnable(GL2.GL_LIGHT1);

    }

    private Boolean loadModels(GL2 gl) {
		/*
		chairModel = ModelLoaderOBJ.LoadModel("./models/c.obj",
				"./models/c.mtl", gl);
		
		chairModel = ModelLoaderOBJ.LoadModel("./models/tcube.obj",
				"./models/tcube.mtl", gl);
	
		chairModel = ModelLoaderOBJ.LoadModel("./models/fight.obj",
				"./models/fight.mtl", gl);
		*/

        chairModel = ModelLoaderOBJ.LoadModel("./resources/lamp1.obj",
                "./resources/lamp1.mtl", gl);


        if (chairModel == null) {
            return false;
        }
        return true;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                        int height) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU glu = new GLU();

        glu.gluPerspective(100, (double) getWidth() / getHeight(), 0.1, 100);
        gl.glMatrixMode(GL2.GL_MODELVIEW);

    }

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.getContentPane().add(new Example());
        window.setSize(width, height);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
