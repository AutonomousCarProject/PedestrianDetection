public List<MovingBlob> filterMovingBlobs(List<MovingBlob> blobs){
		List<MovingBlob> ret = new LinkedList<>();
                blobs.sort(new Comparator<MovingBlob>() {
                    @Override
                    public int compare(MovingBlob t, MovingBlob t1) 
                    {
                        if(Math.abs(t.velocityX) > Math.abs(t1.velocityX))
                        {
                            return -1;
                        }
                        else if(Math.abs(t.velocityX) == Math.abs(t1.velocityX))
                        {
                            return 0;
                        }
                        else
                        {
                            return 1;
                        }
                    }
                });
                for(int i = 0; i < blobs.size()/6; i++)
                {
                    if(i >= blobs.size())
                        break;
                    if(blobs.get(i).age > 4 && blobs.get(i).velocityY < 8)
                    {
                        ret.add(blobs.get(i));
                    }
                    
                    
                    
                }
                
		return ret;
	}