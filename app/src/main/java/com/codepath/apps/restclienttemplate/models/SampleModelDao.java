package com.codepath.apps.restclienttemplate.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SampleModelDao {

    // @Query annotation requires knowing SQL syntax
    // See http://www.sqltutorial.org/


    //Query an object using the UNIQUE IDENTIFIER
    @Query("SELECT * FROM SampleModel WHERE id = :id")
    SampleModel byId(long id);

    //PULL THE LAST 300 ENTRIES THAT WERE CREATED FOR THIS TABLE
    @Query("SELECT * FROM SampleModel ORDER BY ID DESC LIMIT 300")
    List<SampleModel> recentItems();

    //If I pass a SampleModel object, that has a unique identifier, update the table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(SampleModel... sampleModels);
}
