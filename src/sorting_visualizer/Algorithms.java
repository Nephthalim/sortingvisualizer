package sorting_visualizer;

import java.util.*;
import java.awt.*;

public class Algorithms extends Sorting_Visualizer {

	private static final long serialVersionUID = 1L;

	private static int partition(Sorting_Visualizer array, int low, int high) {
		int pivot = array.rect[high].height;
		int i = (low - 1);
		for (int j = low; j < high; j++) {
			array.comparisons++;
			if (array.rect[j].height < pivot) {
				i++;
				array.colour[j] = 255;
				array.colour[i] = 150;
				array.swap(array.rect[i], array.rect[j], i);
			}
		}

		array.colour[i + 1] = 255;
		array.colour[high] = 150;
		array.swap(array.rect[i + 1], array.rect[high], i + 1);

		return i + 1;
	}

	// Quick Sort
	// Average Time Complexity: O(n log(n))
	// Worst Space Complexity: O(log(n))
	// Source: https://www.geeksforgeeks.org/quick-sort/
	public static void quickSort(Sorting_Visualizer array, int low, int high) {
		int pi;

		if (low < high) {
			pi = partition(array, low, high);
			quickSort(array, low, pi - 1);
			quickSort(array, pi + 1, high);
		}

	}

	private static void merge(Sorting_Visualizer array, Rectangle[] rect, Rectangle[] temp, int leftStart,
			int rightEnd) {

		int leftEnd = (rightEnd + leftStart) / 2;
		int rightStart = leftEnd + 1;

		int size = rightEnd - leftStart + 1;

		int left = leftStart;
		int right = rightStart;
		int index = leftStart;

		while (left <= leftEnd && right <= rightEnd) {
			array.comparisons++;
			if (rect[left].height < rect[right].height) {
				temp[index].height = rect[left].height;
				temp[index].y = rect[left].y;
				left++;

			} else {
				temp[index].height = rect[right].height;
				temp[index].y = rect[right].y;
				right++;

			}
			index++;

		}
		while (left <= leftEnd) {
			array.comparisons++;
			temp[index].height = rect[left].height;
			temp[index].y = rect[left].y;
			index++;
			left++;

		}
		while (right <= rightEnd) {
			array.comparisons++;
			temp[index].height = rect[right].height;
			temp[index].y = rect[right].y;
			index++;
			right++;
		}
		array.repaint();
		array.longSleep(array.speed);
		array.colour[left] = 255;
		array.colour[right - 1] = 255;

		System.arraycopy(rect, left, temp, index, leftEnd - left + 1);
		System.arraycopy(rect, right, temp, index, rightEnd - right + 1);
		System.arraycopy(temp, leftStart, rect, leftStart, size);

	}

	// Merge Sort
	// Average Time Complexity: O(n log(n))
	// Worst Space Complexity: O(n)
	// Source: https://en.wikipedia.org/wiki/Merge_sort
	public static void mergeSort(Sorting_Visualizer array, Rectangle[] temp, int leftStart, int rightEnd,
			Rectangle[] rect) {
		for (int i = 0; i < temp.length; i++) {
			temp[i] = new Rectangle(rect[i].x, 0, rect[i].width, 0);
		}
		if (leftStart >= rightEnd) {
			return;
		}

		int mid = (leftStart + rightEnd) / 2;
		mergeSort(array, temp, leftStart, mid, rect);
		mergeSort(array, temp, mid + 1, rightEnd, rect);
		merge(array, rect, temp, leftStart, rightEnd);

	}

	// Bubble Sort
	// Average Time Complexity: O(n^2)
	// Worst Space Complexity: O(1)
	// Source: https://en.wikipedia.org/wiki/Bubble_sort
	public static void bubbleSort(Sorting_Visualizer array) {
		int n = array.rect.length - 1;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				array.comparisons++;
				if (array.rect[j].height > array.rect[j + 1].height) {
					array.colour[j + 1] = 255;
					array.colour[j] = 100;
					array.swap(array.rect[j + 1], array.rect[j], j + 1);

				}
			}
		}
	}

	static void heapify(Sorting_Visualizer array, Rectangle[] rect, int length, int i) {
		int root = i;
		int left = 2 * i + 1;
		int right = 2 * i + 2;

		if (left < length && rect[left].height > rect[root].height)
			root = left;

		if (right < length && rect[right].height > rect[root].height)

			root = right;

		if (root != i) {
			array.comparisons++;
			array.colour[root] = 255;
			array.colour[i] = 150;
			array.swap(rect[root], rect[i], root);
			heapify(array, rect, length, root);
		}

	}

	// Heap Sort
	// Average Time Complexity: O(n log(n))
	// Worst Space Complexity: O(1)
	// Source: https://www.geeksforgeeks.org/heap-sort/
	public static void heapSort(Sorting_Visualizer array) {
		int length = array.rect.length;

		for (int i = length / 2 - 1; i > 0; i--) {
			heapify(array, array.rect, length, i);
		}

		for (int i = length - 1; i > 0; i--) {
			array.comparisons++;
			array.colour[0] = 255;
			array.colour[i] = 150;
			array.swap(array.rect[0], array.rect[i], 0);
			heapify(array, array.rect, i, 0);
		}
	}

	// Bucket Sort
	// Worst Case Time Complexity: O(n+k)
	// Worst Space Complexity: O(n)
	// Source: https://www.programiz.com/dsa/bucket-sort
	public static void bucketSort(Sorting_Visualizer array, ArrayList<ArrayList<Rectangle>> bucket, Rectangle[] temp) {
		int buck;
		for (int i = 0; i < array.rect.length; i++) {
			temp[i] = new Rectangle(i, 0, 1, 0);
			bucket.add(new ArrayList<Rectangle>());
			bucket.get(i).add(new Rectangle());
		}

		for (int i = 0; i < array.rect.length; i++) {

			buck = array.rect[i].height / 10;

			bucket.get(buck).add(array.rect[i]);
		}

		for (int i = 0; i < array.rect.length - 1; i++) {

			selectionSort(array, bucket.get(i));

		}

		int index = 0;
		for (int i = 0; i < array.rect.length; i++)
			for (int j = 0; j < bucket.get(i).size() - 1; j++) {
				temp[index].height = bucket.get(i).get(j + 1).height;
				temp[index].y = bucket.get(i).get(j + 1).y;
				index++;
			}

		for (int i = 0; i < array.rect.length; i++) {
			array.longSleep(2);
			array.colour[i] = 255;
			array.repaint();
			array.rect[i] = temp[i];
		}

	}

	private static int getNextGap(int gap) {
		gap = (gap * 10) / 13;

		if (gap < 1)
			return 1;

		return gap;
	}

	// Comb Sort
	// Average Time Complexity: O(n^2/2^p), p is the number of gaps
	// Worst Space Complexity: O(1)
	// Source: https://en.wikipedia.org/wiki/Comb_sort
	public static void combSort(Sorting_Visualizer array) {
		int gap = array.rect.length;

		boolean swapped = true;

		while (gap != 1 || swapped == true) {

			gap = getNextGap(gap);
			swapped = false;

			for (int i = 0; i < array.rect.length - gap; i++) {
				array.colour[i] = 255;
				array.comparisons++;
				array.colour[i + gap] = 100;
				if (array.rect[i].height > array.rect[i + gap].height) {

					array.swap(array.rect[i], array.rect[i + gap], i);
					swapped = true;
				}
			}

		}

	}

	// Selection Sort
	// Worst Case Time Complexity: O(n^2)
	// Worst Space Complexity: O(1)
	// Source: https://www.geeksforgeeks.org/selection-sort/
	public static void selectionSort(Sorting_Visualizer array) {

		for (int i = 0; i < array.rect.length - 1; i++) {
			int min_index = i;
			for (int j = i + 1; j < array.rect.length - 1; j++) {
				array.comparisons++;
				if (array.rect[j].height < array.rect[min_index].height)
					min_index = j;
			}
			array.colour[min_index] = 255;
			array.colour[i] = 100;
			array.swap(array.rect[min_index], array.rect[i], min_index);

		}
	}

	private static void selectionSort(Sorting_Visualizer parent, ArrayList<Rectangle> array) {

		for (int i = 0; i < array.size() - 1; i++) {
			int min_index = i;
			for (int j = i + 1; j < array.size() - 1; j++) {

				parent.comparisons++;
				if (array.get(j).height < array.get(min_index).height)

					min_index = j;
			}
			parent.swap(array.get(min_index), array.get(i), min_index);

		}
	}

}
