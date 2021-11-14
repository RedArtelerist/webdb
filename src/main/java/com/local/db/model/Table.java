package com.local.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@javax.persistence.Table(name = "tbl")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name must not be empty")
    @Length(max = 20, message = "Name too long (more than 20)")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "base_id")
    private Base base;

    @OneToMany(mappedBy = "table", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Attribute> attributes;

    @OneToMany(mappedBy = "table", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Row> rows;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}
