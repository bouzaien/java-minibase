package bufmgr;

public class LRUK extends Replacer {

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
	 * This pushes the given frame to the end of the list.
	 * @param frameNo	the frame number
	 */
	private void update(int frameNo)
	{

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
	public String name() { return "LRU-k"; }

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

}// End LRU_k
