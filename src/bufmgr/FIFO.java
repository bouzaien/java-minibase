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
	{
		int index;
		for ( index=0; index < nframes; ++index )
			if ( frames[index] == frameNo )
				break;

		while ( ++index < nframes )
			frames[index-1] = frames[index];
		frames[nframes-1] = frameNo;
	}


	public void setBufferManager( BufMgr mgr )
	{
		super.setBufferManager(mgr);
		frames = new int [ mgr.getNumBuffers() ];
		nframes = 0;
	}

	/**
	 * calll super class the same method
	 * pin the page in the given frame number 
	 * without moving the page to the end of list  
	 *
	 * @param	 frameNo	 the frame number to pin
	 * @exception  InvalidFrameNumberException
	 */
	public void pin(int frameNo) throws InvalidFrameNumberException
	{
		super.pin(frameNo);
	}

	public int pick_victim()
	{
		int numBuffers = mgr.getNumBuffers();
		int frame;

		if ( nframes < numBuffers ) {
			frame = nframes++;
			frames[frame] = frame;
			state_bit[frame].state = Pinned;
			(mgr.frameTable())[frame].pin();
			return frame;
		}

		for ( int i = 0; i < numBuffers; ++i ) {
			frame = frames[i];
			if ( state_bit[frame].state != Pinned ) {
				state_bit[frame].state = Pinned;
				(mgr.frameTable())[frame].pin();
				update(frame);
				return frame;
			}
		}
		return -1;
	}

	public String name() {
		return "FIFO";
	}

}
