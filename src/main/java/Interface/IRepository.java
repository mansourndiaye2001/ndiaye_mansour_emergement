package Interface;

import java.util.List;

public interface IRepository <T>{
    public  void add(T t);
    public  void update(T t);
    public  void delete (T t);
    public List<T> list ();



}
