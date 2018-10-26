package android.ak.c196.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

@Dao
abstract class BaseDao<T>{

    @Insert
    public abstract void insert(T data);

    @Update
    public abstract void  update(T data);

    @Delete
    public abstract void delete(T data);

}
