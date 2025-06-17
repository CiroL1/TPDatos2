package Users;

import java.util.Map;

public interface CassandraManager<T> {
    void connect();
    void createTable();
    boolean insert(T t);
    Map<Integer, T> getAll();
    T getOne(int id);
    void update(T t);
    void delete(int id);
    void close();
}
