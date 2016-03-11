package tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Singleton;

@Singleton
public class RandomLongIdGenerator {
	
	private static List<Long> alreadyGeneratedIds = new ArrayList<Long>();
	private final static long UPPER_RANDOM_GENERATION_RANGE = 1000;
	public Long generateRandomLongValue()
	{
		long idToReturn = 0;
		boolean properIdFinded = false;
		long v;
		while(properIdFinded!=true)
		{
			v = ThreadLocalRandom.current().nextLong(UPPER_RANDOM_GENERATION_RANGE);
			if(checkIfListContainsGivenLong(alreadyGeneratedIds, v)==false)
			{
				idToReturn=v;
				properIdFinded = true;
			}
			else
			{
				properIdFinded = false;
			}
		}
		return idToReturn;
	}
	public boolean checkIfListContainsGivenLong(List<Long> alreadyGeneratedIds, Long valueToCheck)
	{
		boolean contains=false;
		for(Long currentLong :  alreadyGeneratedIds)
		{
			long currentLongValue = Long.valueOf(currentLong);
			if(currentLongValue==Long.valueOf(valueToCheck))
			
				contains=true;
				break;
		}
		return contains;
	}
	
}
