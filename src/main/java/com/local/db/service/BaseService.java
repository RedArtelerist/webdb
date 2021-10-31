package com.local.db.service;

import com.local.db.model.Base;
import com.local.db.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BaseService {
    @Autowired
    private BaseRepository baseRepository;

    public List<Base> findAll() {
        return baseRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Base findById(Long baseId){
        Optional<Base> baseOptional = baseRepository.findById(baseId);

        if (!baseOptional.isPresent()) {
            throw new RuntimeException("Base Not Found!");
        }
        return baseOptional.get();
    }

    public Base findByName(String name){
        Base base = baseRepository.findByNameIgnoreCase(name);

        if (base == null) {
            throw new RuntimeException("Base Not Found!");
        }
        return base;
    }

    public boolean addBase(Base base){
        Base dbBase = baseRepository.findByNameIgnoreCase(base.getName());
        if (dbBase != null)
            return false;

        baseRepository.save(base);
        return true;
    }

    /*public Base createBase(Base base) throws Exception {
        Base dbBase = baseRepository.findByNameIgnoreCase(base.getName());
        if (dbBase != null)
            throw new Exception("Base with the same name exists!");

        return baseRepository.save(base);
    }*/

    public void removeBase(Base base) {
        baseRepository.delete(base);
    }
}
