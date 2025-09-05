package me.kasif.journalApp.repository;

import me.kasif.journalApp.entity.JournalEntry;
import me.kasif.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
public User findByUsername(String username);
}
