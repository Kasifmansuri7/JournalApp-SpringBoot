package me.kasif.journalApp.repository;

import me.kasif.journalApp.entity.JournalEntry;
import me.kasif.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    public User findByUsername(String username);
}
