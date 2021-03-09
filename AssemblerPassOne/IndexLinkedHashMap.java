import java.util.*;

public class IndexLinkedHashMap<K,V> extends LinkedHashMap<K, V> {

	HashMap<K, Integer> index = new HashMap<K,Integer>();
	HashSet<K> flag = new HashSet<>() ;	
	int pos = 0;

	@Override
	public V put(K Key,V Value){
		super.put(Key,Value);
		if(!flag.contains(Key)){
			pos++;
			flag.add(Key);
			index.put(Key,pos);
		}

		return Value;
	}

	public int getindex(K Key){
		return index.get(Key);
	}
}