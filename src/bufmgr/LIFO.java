/* File LIFO.java */

package bufmgr;

public class LIFO extends Replacer {
	
	private int  frames[];
	private int  nframes;
	
    public LIFO(BufMgr mgrArg)
    {
    	super(mgrArg);
    	frames = null;
    }
	
	private void update(int frameNo)
	//TODO
	{}
	
	public void setBufferManager( BufMgr mgr )
    {
		super.setBufferManager(mgr);
		frames = new int [ mgr.getNumBuffers() ];
		nframes = 0;
    }
	
	public void pin(int frameNo) throws InvalidFrameNumberException
	{
		super.pin(frameNo);
		update(frameNo);
	}

	public int pick_victim() throws BufferPoolExceededException, PagePinnedException {
		// TODO
		return 0;
	}

	public String name() {
		return "LIFO";
	}

}
