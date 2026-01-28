package synthesizer; // Finished: Make sure to make this class a part of the synthesizer package
//package <package name>;

//Make sure this class is public
public class GuitarString {
    /**
     * Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday.
     */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        // Finished: Create a buffer with capacity = SR / frequency. You'll need to
        //       cast the result of this divsion operation into an int. For better
        //       accuracy, use the Math.round() function before casting.
        //       Your buffer should be initially filled with zeros.
        buffer = new ArrayRingBuffer<Double>((int) Math.round(SR / frequency));

        //debug : 必须用 0.0 填满 buffer，否则调用 sample() 或 tic() 时 buffer 是空的会报错
        while (!buffer.isFull()) {
            buffer.enqueue(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        // Finished: Dequeue everything in the buffer, and replace it with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //
        //       Make sure that your random numbers are different from each other.

        //debug：1.先把buffer里面的旧数据全部清空
        while (!buffer.isEmpty()) {
            buffer.dequeue();
        }

        //2.再填充随机噪声
        while (!buffer.isFull()) {
            buffer.enqueue(Math.random() - 0.5);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        // Finished: Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       Do not call StdAudio.play().

        //tic的定义是向前推进一个时间步，只是替换一个样本，不用循环完全部
        buffer.enqueue((buffer.dequeue() + buffer.peek()) * DECAY * 0.5);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        // Finished: Return the correct thing.
        return buffer.peek();
    }
}
