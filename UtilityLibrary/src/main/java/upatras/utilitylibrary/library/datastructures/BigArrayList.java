package upatras.utilitylibrary.library.datastructures;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Iterator;

/**Not in Use , was for when tasks exceeded 2.3 bn in number
 *
 * @author Paris
 * @param <T>
 */
final public class BigArrayList<T> implements Iterable<T> {

    ArrayList<ArrayList<T>> extendedarray = new ArrayList<>();
    ArrayList<T> currarraylist;
    Long size = 0L;
    static final int maxarraysize = Integer.MAX_VALUE - 1;

    /**
     *
     */
    public BigArrayList() {
        expand();
    }

    private void expand() {
        currarraylist = new ArrayList<T>();
        extendedarray.add(currarraylist);

    }

    /**
     *
     * @return
     */
    public long size() {
        return size;
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     *
     * @param e
     * @return
     */
    public boolean add(T e) {

        if (currarraylist.size() == maxarraysize) {
            expand();
        }

        size++;

        return currarraylist.add(e);

    }

    /**
     *
     */
    public void clear() {
        for (ArrayList ar : extendedarray) {
            ar.clear();
        }
        extendedarray.clear();
        size = 0L;
        expand();

    }

    /**
     *
     * @param index
     * @return
     */
    public T get(long index) {
        int exarrayloc = (int) (index / (maxarraysize + 1));
        int arrayloc = (int) (index % (maxarraysize + 1));
        return extendedarray.get(exarrayloc).get(arrayloc);

    }

    /**
     *
     * @param index
     * @param element
     * @return
     */
    public T set(long index, T element) {

        int exarrayloc = (int) (index / (maxarraysize + 1));
        int arrayloc = (int) (index % (maxarraysize + 1));
        return extendedarray.get(exarrayloc).set(arrayloc, element);
    }

    public T poplast() {

        if (currarraylist.isEmpty()) {
            extendedarray.remove(extendedarray.size() - 1);
            currarraylist = extendedarray.get(extendedarray.size() - 1);

        }

        T obj = currarraylist.remove(currarraylist.size() - 1);
        size--;
        return obj;

    }

    /**
     *
     * @param index
     * @return
     */
    public T replace_with_last_and_pop(long index) {

        T obj= get(index);
        set(index, getlast());
        poplast();

        return obj;
    }

    /**Gets the last item in the Array
     *
     * @return
     */
    public T getlast() {

        return get(size - 1);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator() {

            long loc = 0;

            @Override
            public boolean hasNext() {
                return loc < size;
            }

            @Override
            public Object next() {
                return get(loc++);
            }

        };
    }

}
