import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListT {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        List<Integer>[] lists = t3(list, 3);
        for(List<Integer> l:lists){
            for(int i:l){
                System.out.print(i+" ");
            }
            System.out.println();
        }
    }

    @Test
    public static <T> List<T>[] t3(List<T> allList,int threadNum){
        List<T>[] listA = new List[threadNum];
        for(int i=0;i<listA.length;i++){
            listA[i] = new ArrayList();
        }
        for(int i=0;i<allList.size();i++){
            listA[i%threadNum].add(allList.get(i));
        }
        return listA;
    }

}
