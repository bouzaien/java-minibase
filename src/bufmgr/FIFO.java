/* File FIFO.java */

package bufmgr;

public class FIFO extends Replacer {
	
	private int  frames[];
	private int  nframes;
	
    public FIFO(BufMgr mgrArg)
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
	
	public int pick_victim()
	//TODO
	{
		return -1;
	}

	public String name() {
		return "FIFO";
	}
	
}
