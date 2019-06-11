import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.awt.AWTKeyAdapter;
import com.jogamp.opengl.util.Animator;

public class Demo1 extends KeyAdapter implements GLEventListener {
	float rotateT = 0.0f;
	private GLModel lampModel = null;

	public static void main(String[] args) {

		java.awt.Frame frame = new java.awt.Frame("Demo1");
		frame.setSize(800, 600);
		frame.setLayout(new java.awt.BorderLayout());

		final Animator animator = new Animator();
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				// Run this on another thread than the AWT event queue to
				// make sure the call to Animator.stop() completes before
				// exiting
				new Thread(new Runnable() {
					public void run() {
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});

		GLCanvas canvas = new GLCanvas();
		animator.add(canvas);

		final Demo1 demo = new Demo1();
		canvas.addGLEventListener(demo);

		frame.add(canvas, java.awt.BorderLayout.CENTER);
		frame.validate();

		frame.setVisible(true);
		animator.start();
	}

	public void init(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();
		lampModel = ModelLoaderOBJ.LoadModel("./resources/lamp1.obj", "./resources/lamp1.mtl", gl);

		gl.glClearDepth(1.0f); // Depth Buffer Setup
		gl.glEnable(GL2.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL2.GL_LEQUAL); // The Type Of Depth Testing To Do

		// Really Nice Perspective Calculations
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

		// keyboard
	    if (drawable instanceof Window) {
	        Window window = (Window) drawable;
	        window.addKeyListener(this);
	    } else if (GLProfile.isAWTAvailable() && drawable instanceof java.awt.Component) {
	        java.awt.Component comp = (java.awt.Component) drawable;
	        new AWTKeyAdapter(this, drawable).addTo(comp);
	    }
	}

	public void dispose(GLAutoDrawable drawable) {
	}

	public void display(GLAutoDrawable drawable) {

		// Get the GL corresponding to the drawable we are animating
		GL2 gl = drawable.getGL().getGL2();


		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);


		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 30.0f, -10.0f);
		gl.glScalef(.008f, .008f, .008f); //here
		lampModel.opengldraw(gl);
		gl.glPopMatrix();

		gl.glEnd();


	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {

		GL2 gl = drawable.getGL().getGL2();

		float h = (float) height / (float) width;

		gl.glMatrixMode(GL2.GL_PROJECTION);

		gl.glLoadIdentity();

		if (h < 1)
			gl.glFrustum(-1.0f, 1.0f, -h, h, 1.0f, 60.0f);
		else {
			h = 1.0f / h;
			gl.glFrustum(-h, h, -1.0f, 1.0f, 1.0f, 60.0f);
		}

		gl.glMatrixMode(GL2.GL_MODELVIEW);

	}

	public void keyPressed(KeyEvent e) {
		int kc = e.getKeyCode();
		if (KeyEvent.VK_ESCAPE == kc) {
			System.exit(0);
		}
	}

}
