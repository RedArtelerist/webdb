package com.local.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Row {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<String> values = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_id")
    private Table table;

    public Long getId() {
        return id;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Row) {
            Row temp = (Row) obj;
            return Arrays.equals(this.values.toArray(), temp.values.toArray());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.values.toArray());
    }
}
