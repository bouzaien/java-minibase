package bufmgr;

public class LRUK extends Replacer {

	private static final long CRP = 0;

	/**
	 * number of last references to consider
	 */
	private int lastRef;

	/**
	 * private field
	 * An array to hold number of frames in the buffer pool
	 */
	private int frames[];

	/**
	 * private field
	 * number of frames used
	 */
	private int nframes;

	/**
	 * Denotes the time of last i references of page frameNo
	 * hist[frameNo][i]
	 */
	private long hist[][];

	/**
	 * The time of the most recent reference to page frameNo
	 * last[frameNo]
	 */
	private long last[];

	/**
	 * This pushes the given frame to the end of the list.
	 * @param frameNo	the frame number
	 */
	private void update(int frameNo)
	{	
		long 	t = System.currentTimeMillis();
		int 	index;
		boolean	exist = false; // To check if the page is already in the buffer


		for ( index=0; index < nframes; ++index )
			if ( frames[index] == frameNo ) {
				exist = true;
				break;
			}

		// Update history information of page p already in the buffer
		if (exist) {
			if ( t-last[frameNo]>= CRP ) {
				// A new uncorrelated reference
//				System.out.print ("last\n");
//				System.out.print (last[frameNo]);
//				System.out.print ("\n");
//				System.out.print ("hist\n");
//				System.out.print (lastRef);
//				System.out.print ("\n");
//				System.out.print (hist[frameNo].length);
//				System.out.print ("\n");
				long refrencedPageCP = last[frameNo] - hist[frameNo][0];
				for (int i=1; i<lastRef; i++) {
					hist[frameNo][i] = hist[frameNo][i-1] + refrencedPageCP;
				}
				hist[frameNo][0] = t;
				last[frameNo] = t;
			} else {
				// A correlated reference
				last[frameNo] = t;
			}

		} 
		/*
		 * else { for (int j=1; j<=lastRef; j++) { hist[frameNo][j] =
		 * hist[frameNo][j-1]; } System.out.print (frameNo); System.out.print ("\n");
		 * System.out.print (hist.length); hist[frameNo][0] = t; last[frameNo] = t; }
		 */
	}	



	/**
	 * Calling super class the same method
	 * Initializing the frames[] with number of buffer allocated
	 * by buffer manager
	 * set number of frame used to zero
	 *
	 * @param	mgr	a BufMgr object
	 * @see	BufMgr
	 * @see	Replacer
	 */
	public void setBufferManager( BufMgr mgr )
	{
		super.setBufferManager(mgr);
		frames = new int [ mgr.getNumBuffers() ];
		nframes = 0;
		lastRef = mgr.getLastRef();
//		System.out.print("lastRef of LRUK\n");
//		System.out.print(lastRef);
		hist = new long [mgr.getNumBuffers()][lastRef];
		last = new long [mgr.getNumBuffers()];
	}

	/* public methods */

	/**
	 * Class constructor
	 * Initializing frames[] pinter = null.
	 */
	public LRUK(BufMgr mgrArg)
	{
		super(mgrArg);
		frames = null;
		lastRef = mgrArg.getLastRef();
	}

	/**
	 * call super class the same method
	 * pin the page in the given frame number 
	 * move the page to the end of list  
	 *
	 * @param	 frameNo	 the frame number to pin
	 * @exception  InvalidFrameNumberException
	 */
	public void pin(int frameNo) throws InvalidFrameNumberException
	{
		super.pin(frameNo);
		update(frameNo);
	}

	/**
	 * Finding a free frame in the buffer pool
	 * or choosing a page to replace using LRU policy
	 *
	 * @return 	return the frame number
	 *		return BufferPoolExceededException if failed
	 */
	public int pick_victim() throws BufferPoolExceededException
	{
		int numBuffers = mgr.getNumBuffers();
		int frame;
		long t = System.currentTimeMillis();
		long min = t;
		int victim = 0;

		if ( nframes < numBuffers ) {
			frame = nframes++;
			frames[frame] = frame;
			state_bit[frame].state = Pinned;
			(mgr.frameTable())[frame].pin();
			return frame;
		}

		for ( int i = 0; i < numBuffers; ++i ) {
			frame = frames[i];
			if ( t-last[frame]>= CRP && hist[frame][lastRef-1]<=min && state_bit[frame].state != Pinned ) {
				victim = frame;
				min = hist[victim][lastRef-1];
			}
		}

		if (victim >=0) {
			state_bit[victim].state = Pinned;
			(mgr.frameTable())[victim].pin();
			return victim;
		}

		throw new BufferPoolExceededException (null, "BUFMGR: BUFMGR_EXCEEDED.");
	}

	/**
	 * get the page replacement policy name
	 *
	 * @return	return the name of replacement policy used
	 */  
	public String name() { return "LRUK"; }

	/**
	 * print out the information of frame usage
	 */  
	public void info()
	{
		super.info();
		System.out.print( "LRU-k REPLACEMENT");

		for (int i = 0; i < nframes; i++) {
			if (i % 5 == 0)
				System.out.println( );
			System.out.print( "\t" + frames[i]);
		}
		System.out.println();
	} // End info()

	public int[] getFrames() { return frames;	}

	public long HIST(int pagenumber, int i) { return hist[pagenumber][i];	}

	public long back(int pagenumber, int i) {
		long t = System.currentTimeMillis();
		long b = t - hist[pagenumber][i];
		return b;
	}
	
	public long last(int pagenumber) {
		return hist[pagenumber][0];
	}

}// End LRUK
