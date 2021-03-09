import java.util.*;

public class IndexedLinkedHashMap<K, V> extends LinkedHashMap<K, V>{
	int pos=1;
	HashMap<K,Integer> h1 = new HashMap<K,Integer>();
	
	@Override
	public V put(K k,V v){
		super.put(k,v);
		h1.put(k,pos);
		pos++;
		return v;
	}
	
	public Integer getIndexOf(K k){
		return h1.get(k);
	}
}