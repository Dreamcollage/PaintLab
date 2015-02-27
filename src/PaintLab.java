import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class PaintLab extends Applet implements MouseListener,
		MouseMotionListener {
	private static final long serialVersionUID = 1L;
	// declare your fields here
	protected int mxloc = -1, myloc = -1;
	protected int width, height;
	protected int mxcloc, mycloc;
	protected int mxrloc, myrloc;
	protected int tmlosc;
	protected int iBrushSize; // 0 - 100
	protected boolean isDragging, oob, clicked, pressed, clear;
	protected Rectangle canvas, rectBrush, rectColor;
	protected int counter, opacounter = 255;
	protected TextField brushIn, rIn, gIn, bIn;
	protected Color drawColor = Color.BLACK;
	protected int rCol, gCol, bCol, tempRCol, tempGCol, tempBCol;
	protected String[] err = new String[7];

	Graphics buffer;
	Image img;

	public void init() {
		// define your fields here
		img = createImage(900, 600);
		buffer = img.getGraphics();
		buffer.setColor(Color.CYAN);
		buffer.fillRect(0, 0, 2000, 2000);
		addMouseListener(this);
		addMouseMotionListener(this);
		// rects
		canvas = new Rectangle(80, 20, 900 + 1, 600 + 1);
		rectBrush = new Rectangle(0, 0, 50, 50);
		rectColor = new Rectangle(0, 50, 50, 50);

		// brush textfields
		brushIn = new TextField("Brush Size", 10);

		// color textfields
		rIn = new TextField("Red Value", 10);
		gIn = new TextField("Green Value", 10);
		bIn = new TextField("Blue Value", 10);

		for (int i = 0; i < err.length; i++) {
			err[i] = "Invalid Operation: ";
		}

	}

	public void paint(Graphics g) {

		height = getSize().height;
		width = getSize().width;

		
		if(counter > 200)
			opacounter = opacounter - 2;
		if (counter > 327) {
			counter = 0;
			opacounter = 255;
			for (int i = 0; i < err.length; i++)
				err[i] = "Invalid Operation: ";
		}

		brushIn.setBounds(0, 50, 80, 20);
		rIn.setBounds(0, 100, 80, 20);
		gIn.setBounds(0, 120, 80, 20);
		bIn.setBounds(0, 140, 80, 20);

		g.setColor(getBackground());
		g.fillRect(0, 101, 80, 200);

		buffer.setColor(Color.CYAN);
		g.drawImage(img, 80, 20, 900, 600, this);
		buffer.setColor(Color.BLACK);
		g.setColor(getBackground());
		g.fillRect(0, (height - 20), width, 20);
		g.setColor(Color.BLACK);

		// draw brush button
		g.drawRect(0, 0, 50, 50);
		g.fillOval(5, 5, 40, 40);

		// draw color button
		g.drawRect(0, 50, 50, 50);
		g.setColor(Color.RED);
		g.fillArc(5, 55, 40, 40, 90, 90);
		g.setColor(Color.YELLOW);
		g.fillArc(5, 55, 40, 40, 180, 90);
		g.setColor(Color.GREEN);
		g.fillArc(5, 55, 40, 40, 270, 90);
		g.setColor(Color.BLUE);
		g.fillArc(5, 55, 40, 40, 0, 90);
		g.setColor(getBackground());

		g.fillRect(51, 0, 600, 20);

		// brush button and textbox
		if (pressed && brushIn.isShowing()) {
			try {
				iBrushSize = Integer.parseInt(brushIn.getText());
				if (iBrushSize < 0) {
					err[0] += "Brush minimum value is 0, Recieved \""
							+ iBrushSize + "\" ...Setting to 0.";
					iBrushSize = 0;
					brushIn.setText("0");

				}
			} catch (java.lang.NumberFormatException e) {
				iBrushSize = 0;
			}
			remove(brushIn);
		}

		if (rectBrush.contains(mxloc, myloc) && clicked) {
			add(brushIn);
		}

		// color button and textboxes
		if (pressed && (rIn.isShowing() || gIn.isShowing() || bIn.isShowing())) {
			try {
				rCol = Integer.parseInt(rIn.getText());
				if (rCol > 100) {
					err[1] += "Red maximum value is 100, Recieved \"" + rCol
							+ "\" ...Setting to 100.";
					rCol = 100;
					rIn.setText("100");
				}
				if (rCol < 0) {
					err[2] += "Red minimum value is 0, Recieved \"" + rCol
							+ "\" ...Setting to 0.";
					rCol = 0;
					rIn.setText("0");
				}

			} catch (java.lang.NumberFormatException e) {
				rCol = 0;
			}
			try {
				gCol = Integer.parseInt(gIn.getText());

				if (gCol > 100) {
					err[3] += "Green maximum value is 100, Recieved \"" + gCol
							+ "\" ...Setting to 100.";
					gCol = 100;
					gIn.setText("100");
				}
				if (gCol < 0) {
					err[4] += "Green minimum value is 0, Recieved \"" + gCol
							+ "\" ...Setting to 0.";
					gCol = 0;
					gIn.setText("0");
				}

			} catch (java.lang.NumberFormatException e) {
				gCol = 0;
			}
			try {
				bCol = Integer.parseInt(bIn.getText());
				if (bCol > 100) {
					err[5] += "Blue maximum value is 100, Recieved \"" + bCol
							+ "\" ...Setting to 100.";
					bCol = 100;
					bIn.setText("100");
				}
				if (bCol < 0) {
					err[6] += "Blue minimum value is 0, Recieved \"" + bCol
							+ "\" ...Setting to 0.";
					bCol = 0;
					bIn.setText("0");
				}

			} catch (java.lang.NumberFormatException e) {
				bCol = 0;
			}
			remove(rIn);
			remove(gIn);
			remove(bIn);
		}

		if (rectColor.contains(mxloc, myloc) && clicked) {
			add(rIn);
			add(gIn);
			add(bIn);
		}

		for (int i = 0; i < err.length; i++) {
			if (err[i] != "Invalid Operation: ") {
				g.setColor(new Color(255,0,0,opacounter));
				g.drawString(err[i], 80, 15);
				counter++;
			}
		}

		// canvas coords
		g.setColor(Color.BLACK);
		if ((!canvas.contains(mxloc, myloc))) {
			g.drawString("(N/A, N/A)", 0, (height - 6));
		} else {
			g.drawString("(" + (mxloc - 80) + ", " + (myloc - 20) + ")", 0,
					height - 6);
		}
		// brush
		if (isDragging) {
			drawColor = new Color(rCol * (255 / 100), gCol * (255 / 100), bCol
					* (255 / 100));
			buffer.setColor(drawColor);
			buffer.fillOval((int) (mxloc - 80 - (iBrushSize / 1.75)),
					(int) (myloc - 20 - (iBrushSize / 1.75)), iBrushSize + 1,
					iBrushSize + 1);
		}
		// clear
		if (clear) {
			buffer.setColor(Color.CYAN);
			buffer.fillRect(0, 0, 900, 600);
		}
		System.out.println(counter + " " + mxloc + " " + myloc + " "
				+ isDragging + " " + width + " " + height + " " + iBrushSize
				+ " " + clicked + " " + opacounter);

		clicked = false;
		pressed = false;
		clear = false;
	}

	// Below are the MouseEvent methods. Use as many as you need.

	public void mousePressed(MouseEvent e) {
		mxcloc = e.getX();
		mycloc = e.getY();
		if (e.isAltDown())
			clear = true;
		pressed = true;
	}

	public void mouseReleased(MouseEvent e) {
		mxrloc = e.getX();
		myrloc = e.getY();
		isDragging = false;
	}

	public void mouseClicked(MouseEvent e) {
		clicked = true;
	}

	public void mouseEntered(MouseEvent e) {
		oob = false;
	}

	public void mouseExited(MouseEvent e) {
		mxloc = -1;
		myloc = -1;
		oob = true;
		repaint();
	}

	public void mouseMoved(MouseEvent e) {
		mxloc = e.getX();
		myloc = e.getY();
		isDragging = false;
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
		mxloc = e.getX();
		myloc = e.getY();
		if (canvas.contains(mxloc, myloc))
			isDragging = true;
		repaint();
	}

	// The update method below reduces flicker in your final product.
	// Don't delete or change it in any way.
	public void update(Graphics g) {
		paint(g);
	}
}