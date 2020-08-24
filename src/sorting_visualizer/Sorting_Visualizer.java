package sorting_visualizer;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Sorting_Visualizer extends JComponent {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 1000, HEIGHT = 600;
	private Random r = new Random();
	private String title = "Sorting Visualizer";
	protected int speed = 10, comparisons = 0;
	private ButtonGroup checkBoxGroup = new ButtonGroup();
	boolean done = false, scrambling = false, start = false, pause = false;
	protected int[] colour = new int[WIDTH];
	protected Rectangle[] rect = new Rectangle[WIDTH];
	String algorithms;
	ArrayList<AlgorithmCheckBox> checkBoxes = new ArrayList<>();
	JButton startButton = new JButton("Begin Visual Sorter"), scrambleButton = new JButton("Scramble");

	public Sorting_Visualizer() {
		JFrame frame = new JFrame(title);

		addCheckBox("Quick Sort", frame, 10, HEIGHT - 75, 95, 30);
		addCheckBox("Merge Sort", frame, 145, HEIGHT - 75, 95, 30);
		addCheckBox("Bubble Sort", frame, 280, HEIGHT - 75, 95, 30);
		addCheckBox("Heap Sort", frame, 415, HEIGHT - 75, 95, 30);
		addCheckBox("Bucket Sort", frame, 550, HEIGHT - 75, 95, 30);
		addCheckBox("Comb Sort", frame, 685, HEIGHT - 75, 95, 30);
		addCheckBox("Selection Sort", frame, 820, HEIGHT - 75, 95, 30);

		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.add(this);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		frame.addKeyListener(new Keyboard());

	}

	private void addCheckBox(String algorithm, JFrame frame, int x, int y, int width, int height) {

		JCheckBox box = new JCheckBox(algorithm, false);
		box.setFocusable(false);
		box.setAlignmentX(Component.LEFT_ALIGNMENT);
		box.setBounds(x, y, width + 30, height);
		box.setForeground(Color.BLACK);
		checkBoxGroup.add(box);
		frame.add(box);
		checkBoxes.add(new AlgorithmCheckBox(algorithm, box));

	}

	@Override
	public void paintComponent(Graphics g) {
		if (start) {
			try {
				g.drawString(algorithms, WIDTH / 2 - 40, 30);
			} catch (Exception e) {

			}
			g.drawString("Comparisons: " + comparisons, 15, HEIGHT - 30);
			g.drawString("Press L to speed up", WIDTH - 150, HEIGHT - 30);
			g.drawString("Press J to slow down", WIDTH - 150, HEIGHT - 10);
		}
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.BLACK);
		;
		for (int i = 0; i < rect.length - 1; i++) {
			g2.draw(rect[i]);

			int val = colour[i];

			g2.setColor(new Color(0 + val, 0, 0));
			g2.fillRect(rect[i].x, rect[i].y, rect[i].width, rect[i].height);

			colour[i] = 0;

		}

	}

	private void preSetup() {
		startButton.setBounds(WIDTH / 2 - 260, HEIGHT - 40, 250, 30);
		scrambleButton.setBounds(WIDTH / 2, HEIGHT - 40, 250, 30);
		startButton.setFocusable(false);
		scrambleButton.setFocusable(false);
		this.add(startButton);
		this.add(scrambleButton);

		for (int i = 0; i < rect.length; i++) {
			colour[i] = 0;
			int rHeight = r.nextInt(HEIGHT - 75 - 1) + 1;
			rect[i] = new Rectangle(i, HEIGHT - rHeight - 75, 1, rHeight);

		}
		repaint();
	}

	private void scramble() {
		for (int i = 0; i < rect.length; i++) {
			int randInt = r.nextInt(HEIGHT - 75 - 1) + 1;
			rect[i].height = randInt;
			rect[i].y = HEIGHT - randInt - 75;
			colour[i] = 255;
			repaint();
			longSleep(2);

		}
		scrambling = !scrambling;

	}

	protected void swap(Rectangle rect_bigger, Rectangle rect_smaller, int bigger_index) {

		int temp_height;
		int temp_y;

		temp_height = rect_bigger.height;
		rect_bigger.height = rect_smaller.height;
		rect_smaller.height = temp_height;

		temp_y = rect_bigger.y;
		rect_bigger.y = rect_smaller.y;
		rect_smaller.y = temp_y;

		repaint();
		longSleep(speed);

	}

	protected void longSleep(int speed) {
		try {
			Thread.sleep(speed);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public void run() {

		preSetup();

		startButton.addActionListener((ActionEvent e) -> {

			for (AlgorithmCheckBox cb : checkBoxes) {
				if (cb.isSelected()) {
					algorithms = cb.getAlgorithm();
					start = true;
				}
			}

		});

		scrambleButton.addActionListener((ActionEvent e) -> {
			if (!start)
				scrambling = true;

		});

		while (!done) {

			if (scrambling)
				scramble();
			else if (start) {
				switch (algorithms) {
				case "Quick Sort":
					Algorithms.quickSort(this, 0, rect.length - 1);
					break;
				case "Merge Sort":
					Algorithms.mergeSort(this, new Rectangle[rect.length], 0, rect.length - 1, rect);
					break;
				case "Bubble Sort":
					Algorithms.bubbleSort(this);
					break;
				case "Bucket Sort":
					Algorithms.bucketSort(this, new ArrayList<ArrayList<Rectangle>>(), new Rectangle[rect.length]);
					break;
				case "Comb Sort":
					Algorithms.combSort(this);
					break;
				case "Heap Sort":
					Algorithms.heapSort(this);
					break;
				case "Selection Sort":
					Algorithms.selectionSort(this);
					break;
				}
				comparisons = 0;
				speed = 10;
				start = false;
				completedAnimation();
			}
			repaint();
		}

	}

	private void completedAnimation() {
		for (int i = 7; i < rect.length; i++) {
			colour[i] = 255;
			colour[i - 1] = 200;
			colour[i - 2] = 175;
			colour[i - 3] = 150;
			colour[i - 4] = 125;
			colour[i - 5] = 100;
			colour[i - 6] = 75;
			colour[i - 7] = 50;
			longSleep(1);
			repaint();
		}

	}

	private class Keyboard extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent x) {
			if (start) {

				if (x.getKeyCode() == KeyEvent.VK_L && speed > 0)
					speed -= 1;

				else if (x.getKeyCode() == KeyEvent.VK_J && speed < 30)
					speed += 3;
			}

		}

	}

	private class AlgorithmCheckBox {
		private final String algorithm;
		private final JCheckBox box;

		public AlgorithmCheckBox(String algorithm, JCheckBox box) {
			this.algorithm = algorithm;
			this.box = box;
		}

		public boolean isSelected() {
			return box.isSelected();
		}

		public String getAlgorithm() {
			return algorithm;
		}
	}

	/**
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// Run the visualizer
		Sorting_Visualizer vis = new Sorting_Visualizer();
		vis.run();

	}

}
