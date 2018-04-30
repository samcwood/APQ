import java.io.File;

public class Queue {
	File Queue[];
	int front, rear, size, length;
	//Creates the Queue and sets size, length, front, rear, and length.
	public Queue(int size) {
		Queue = new File[size];// not sure about runtime of initializing a File array.
		this.size = size;
		front = -1;
		rear = -1;
		length = 0;
	}
	
	//Checks if the current Queue is empty
	public boolean isEmpty() {
		if (front == -1) return true;
		else return false;		
	}
	
	//Doubles the capacity of the File array, when the original array is at capacity.
	public void capacity(File file) {
        File[] temp = new File[size];
        int j = 0;
        for (int i = front; i < Queue.length; i++) {
        	temp[j] = Queue[i];
        	j++;
        }
        front = 0;
        Queue = temp;
        rear = length;
        Queue[rear] = file;
	}
	
	//Adds song to the Queue
	public void enqueue(File file) {
		if (isEmpty()) {
			front++;
			rear++;
			Queue[0] = file;
		} else if ((rear + 1) == size) capacity(file);
		else {
			Queue[++rear] = file;
		}
		length++;
	}
	
	//Removes the front file from the queue
	public boolean dequeue() {
        if (isEmpty()) {
            System.out.println("Playlist is already empty");
            return false;
        } else {      	
             if (front == rear) {
                 front = -1;
                 rear = -1;
             }else front++;
             length--;
             return true;
        }
	}
	
	//Prints the entire Queue line by line.
	public void print() {
		if (!isEmpty()) {
			for (int i = front; i <= rear; i++) {
				System.out.println(Queue[i]);
			}
			System.out.println();
		}
	}
}
