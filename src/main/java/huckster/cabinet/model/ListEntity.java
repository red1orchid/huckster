package huckster.cabinet.model;

/**
 * Created by PerevalovaMA on 10.08.2016.
 */
public class ListEntity<K, V> {
    private K key;
    private V value;

    public ListEntity(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
