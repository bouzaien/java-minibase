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

	public void setBufferManager( BufMgr mgr )
	{
		super.setBufferManager(mgr);

		int numBuffers = mgr.getNumBuffers();
		frames = new int[numBuffers];

		for ( int index = 0; index < numBuffers; ++index )
			frames[index] = -index;

		frames[0] = -numBuffers;
	}

	private void update(int frameNo)
	{
		int index;
		int numBuffers=mgr.getNumBuffers();
		for ( index=0; index < numBuffers; ++index )
			if ( frames[index] < 0  ||  frames[index] == frameNo )
				break;

		// If buffer pool is not yet full, add this frame to it...
		if ( frames[index] < 0 )
			frames[index] = frameNo;

		int frame = frames[index];
		while ( index-- > 0 )
			frames[index+1] = frames[index];

		frames[0] = frame;
	}

	public void pin(int frameNo) throws InvalidFrameNumberException
	{
		super.pin(frameNo);
	}

	public int pick_victim() throws BufferPoolExceededException
	{

		int numBuffers = mgr.getNumBuffers();
		int i, frame;

		for ( i = 0; i < numBuffers; ++i )
			if (frames[i] < 0) {
				if ( i == 0 )
					frames[i] = 0;
				else
					frames[i] *= -1;
				frame = frames[i];
				state_bit[frame].state = Pinned;
				(mgr.frameTable())[frame].pin();
				update(frame);
				return frame;
			}

		for ( i = 0; i < numBuffers; ++i ) {
			frame = frames[i];
			if ( state_bit[frame].state != Pinned ) {
				state_bit[frame].state = Pinned;
				(mgr.frameTable())[frame].pin();
				update(frame);
				return frame;
			}
		}

		
			throw new BufferPoolExceededException (null, "BUFMGR: BUFMGR_EXCEEDED.");
		
	}

	public String name() {
		return "LIFO";
	}

}
