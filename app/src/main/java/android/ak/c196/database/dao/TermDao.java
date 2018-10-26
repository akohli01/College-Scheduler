package android.ak.c196.database.dao;

import android.ak.c196.entity.Term;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class TermDao extends BaseDao<Term>{

    @Query("Select * FROM term")
    public abstract List<Term> getTerms();


    @Delete
    public abstract void delete(Term term);


}
