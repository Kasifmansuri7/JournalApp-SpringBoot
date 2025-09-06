package me.kasif.journalApp.service;

import me.kasif.journalApp.entity.User;
import me.kasif.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    // Query for sentiment analysis
    public List<User> getUserForSentimentAnalysis() {
        Query query = new Query();

//       query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
//       query.addCriteria(Criteria.where("email").ne(null));

        Criteria criteria = new Criteria();
        query.addCriteria(criteria.orOperator(
                        Criteria.where("sentimentAnalysis").is(true),
                        Criteria.where("email").ne(null).ne("")));
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
    }
}
