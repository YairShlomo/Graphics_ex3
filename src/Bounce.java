import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.awt.AWTKeyAdapter;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;


public class Bounce extends KeyAdapter implements GLEventListener , MouseMotionListener {
//	the main class will implement the GLEventListener adding the init & display methods
	public static GLU glu; //static object GLU that will be init at the init() method
	public static GLUT glut; //static object GLU that will be init at the init() method
	public int ball;
	public int Mode;
	private GLModel lampModel = null;

	float Zrot = (float)0.0, Zstep = (float)0.2;
	float Xvel = (float)0.004, Yvel = (float)0.0;
	float Xmin = (float)-4.0, Xmax = (float)4.0;
	float Ymin = (float)-3.8 , Ymax = (float)1.0;
	float Xpos = (float)0.0, Ypos = Ymax;
	float G = (float)-0.0001;

		public static void main(String[] args) {
			java.awt.Frame frame = new java.awt.Frame("Bounce");	 //create a frame
			frame.setSize(800, 640);
			frame.setLayout(new java.awt.BorderLayout());

			final Animator animator = new Animator();  //create animator class that gets the GLCanvas and calls it�s display methods sequentially
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

			GLCanvas canvas = new GLCanvas(); //create a GLCanvas
			animator.add(canvas);

			final Bounce bounce = new Bounce();
			canvas.addGLEventListener(bounce);	//add the bounce as an event listener for GLCanvas events

			frame.add(canvas, java.awt.BorderLayout.CENTER);	//add the canvas to the frame
			frame.validate();

			frame.setVisible(true);
			animator.start();
			canvas.requestFocus();
		}

		public void display(GLAutoDrawable arg0) {
			// TODO Auto-generated method stub
			moveBall();
			GL2 gl = arg0.getGL().getGL2();//get the GL object
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); //clear the depth buffer and the color buffer
			gl.glMatrixMode(GL2.GL_MODELVIEW); //switch to model matrix
			gl.glLoadIdentity(); //init the matrix
//			glu.gluLookAt(eyeX,eyeY,eyeZ,cX,cY,cZ,upX,upY,upZ)); //set the camera view
			
			//draw things using openGL
			int i;
		
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
			gl.glColor3f(0.0f, 1.0f, 1.0f);
			gl.glBegin(GL.GL_LINES);
//			for (i = -5; i <= 5; i++) {
//				gl.glVertex2i(i, -5);
//				gl.glVertex2i(i, 5);
//			}
//			for (i = -5; i <= 5; i++) {
//				gl.glVertex2i(-5, i);
//				gl.glVertex2i(5, i);
//			}
			// draw the vertical lines of the floor (tilted)
			for (i = -5; i <= 5; i++) {
				gl.glVertex2i(i, -5);
				gl.glVertex2f((float)(i * 1.15), (float)-5.9);
			}
			// draw the horizontal lines of the floor
			gl.glVertex2d(-5, -5);
			gl.glVertex2d(5, -5);
			gl.glVertex2d(-5.3, -5.35);
			gl.glVertex2d(5.3, -5.35);
			gl.glVertex2d(-5.75, -5.9);
			gl.glVertex2d(5.75, -5.9);
			gl.glEnd();
		
			gl.glPushMatrix();
			gl.glTranslatef(Xpos, Ypos, 0.0f);
			gl.glScalef(2.0f, 2.0f, 2.0f);
			gl.glRotatef(8.0f, 0.0f, 0.0f, 1.0f);
			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			gl.glRotatef(Zrot, 0.0f, 0.0f, 1.0f);
		
			gl.glCallList(ball);
		
			gl.glPopMatrix();
			gl.glPushMatrix();
			gl.glTranslatef(0.0f, 30.0f, -10.0f);
			gl.glScalef(.008f, .008f, .008f); //here
			lampModel.opengldraw(gl);
			gl.glPopMatrix();
			gl.glFlush();
		}

		public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
			// TODO Auto-generated method stub
			
		}

		public float SIN(float x)
		{
			return (float) Math.sin((float)x*3.14159/180);
		}
		
		public float COS(float x)
		{
			return (float) Math.cos((float)x*3.14159/180);
		}
		
		public int make_ball(GL2 gl)
		{
		  int list;
		  float a, b;
		  float da = 18.0f, db = 18.0f;
		  float radius = 1.0f;
		  int color;
		  float x, y, z;

		  list = gl.glGenLists(1);

		  gl.glNewList(list, GL2.GL_COMPILE);

		  color = 0;
		  for (a = -90.0f; a + da <= 90.0; a += da) {

		    gl.glBegin(GL2.GL_QUAD_STRIP);
		    for (b = 0.0f; b <= 360.0; b += db) {

		      if (color>0) {
				  gl.glColor3f(1.0f, 0.0f, 0.0f);
		      } else {
				  gl.glColor3f(1.0f, 1.0f, 1.0f);
		      }

		      x = radius * COS(b) * COS(a);
		      y = radius * SIN(b) * COS(a);
		      z = radius * SIN(a);
		      gl.glVertex3f(x, y, z);

		      x = radius * COS(b) * COS(a + da);
		      y = radius * SIN(b) * COS(a + da);
		      z = radius * SIN(a + da);
		      gl.glVertex3f(x, y, z);

		      color = 1 - color;
		    }
		    gl.glEnd();

		  }

		  gl.glEndList();

		  return list;
		}

		public void init(GLAutoDrawable drawable) {
			// TODO Auto-generated method stub
		    if (drawable instanceof Window) {
		        Window window = (Window) drawable;
		        window.addKeyListener(this);
		    } else if (GLProfile.isAWTAvailable() && drawable instanceof java.awt.Component) {
		        java.awt.Component comp = (java.awt.Component) drawable;
		        new AWTKeyAdapter(this, drawable).addTo(comp);
		    }
		    
			GL2 gl = drawable.getGL().getGL2(); // get the GL from the GLAutoDrawable
			lampModel = ModelLoaderOBJ.LoadModel("./resources/lamp1.obj", "./resources/lamp1.mtl", gl);

			gl.glEnable(GL2.GL_NORMALIZE); //all the vecs wil be normalize
			gl.glEnable(GL2.GL_CULL_FACE); //enable cull face
			gl.glEnable(GL2.GL_DEPTH_TEST); //enable depth buffer
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); //define the clear color to black
			glu = new GLU(); //init the GLU object
			gl.glMatrixMode(GL2.GL_PROJECTION); //switch to projection matrix
			gl.glLoadIdentity(); //init the matrix
			glu.gluPerspective(45.0f, 800 / 640, 1, 1000);//init the 3D perspective view
			ball = make_ball(gl);
		}

		public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
			// TODO Auto-generated method stub
			GL2 gl = arg0.getGL().getGL2();//get the GL object
			gl.glViewport(arg1, arg2, arg3, arg4);
			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glOrtho(-6.0, 6.0, -6.0, 6.0, -6.0, 6.0);
			gl.glMatrixMode(GL2.GL_MODELVIEW);

		}

		public void moveBall() {
			Zrot += Zstep;
		
			Xpos += Xvel;
			if (Xpos >= Xmax) {
				Xpos = Xmax;
				Xvel = -Xvel;
				Zstep = -Zstep;
			}
			if (Xpos <= Xmin) {
				Xpos = Xmin;
				Xvel = -Xvel;
				Zstep = -Zstep;
			}
			Ypos += Yvel;
			Yvel += G;
			if (Ypos < Ymin) {
				Ypos = Ymin;
				Yvel = -Yvel;
			}
			if (Ypos > Ymax) {
				Ypos = Ymax;
			}
		}

		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getKeyCode()== KeyEvent.VK_ESCAPE) {
		    	System.exit(0);
			}
			
		}

		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose(GLAutoDrawable arg0) {
			// TODO Auto-generated method stub
			
		}
	}
