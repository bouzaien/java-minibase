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

		if (exist) {
			if ( t-last[frameNo]>= CRP ) {
				long refrencedPageCP = last[frameNo] - hist[frameNo][1];
				for (int i=2; i<=lastRef; i++) {
					hist[frameNo][i] = hist[frameNo][i-1] + refrencedPageCP;
				}
				hist[frameNo][1] = t;
				last[frameNo] = t;
			} else {
				last[frameNo] = t;
			}
		} else {
			long min = t;
			for ( int i=0; i<nframes; i++) {
				int frame = frames[i];
				if ( t-last[frame]>= CRP && hist[frame][lastRef]<=min) {
					int victim = frame;
					min = hist[frame][lastRef];
				}
			}

			if (true /*victim is dirty*/) {
				/*write victim to database*/
			}

			// TODO
			//fetch frameNo into the victim frame

			if (true /* hist[frameNo] does not exist */) {
				// allocate hist[frameNo]??
				for (int i=2; i<=lastRef; i++)
					hist[frameNo][i] = (long) 0;
			} else {
				for (int i=2; i<=lastRef; i++)
					hist[frameNo][i] = hist[frameNo][i-1];
			}
			hist[frameNo][1] = t;
			last[frameNo] = t;
		}
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
	 * calll super class the same method
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
	 *		return -1 if failed
	 */
	public int pick_victim()
	{
		return 0;
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

}// End LRUK
