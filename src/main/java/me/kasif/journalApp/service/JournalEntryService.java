package me.kasif.journalApp.service;

import me.kasif.journalApp.entity.JournalEntry;
import me.kasif.journalApp.entity.User;
import me.kasif.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public JournalEntry saveEntry(JournalEntry journalEntry, String username) throws Exception {
        try {
            User user = userService.findByUserName(username);
            if (user == null) {
                throw new Exception("User not found!");
            }

            journalEntry.setDate(LocalDateTime.now());

            // Save the journal
            JournalEntry savedJournal = journalEntryRepository.save(journalEntry);

            // Add the journal in the user db for ref
            user.getJournalEntries().add(savedJournal);
            userService.saveEntry(user);

            return savedJournal;
        } catch (Exception e) {
            throw e;
        }
    }

    public JournalEntry getEntry(ObjectId id) {
        Optional<JournalEntry> result = journalEntryRepository.findById(id);
        return result.orElse(null);
    }

    public List<JournalEntry> getAll(String username) {
        return journalEntryRepository.findAll();
    }

    public JournalEntry updateEntry(ObjectId id, JournalEntry journalEntry) throws Exception {
        JournalEntry journalEntryExists = journalEntryRepository.findById(id).orElse(null);

        // Journal existing
        if (journalEntryExists != null) {
            journalEntryExists.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().equals("") ? journalEntry.getTitle() : journalEntryExists.getTitle());
            journalEntryExists.setContent(journalEntry.getContent() != null && !journalEntry.getContent().equals("") ? journalEntry.getContent() : journalEntryExists.getContent());
        }

        journalEntryRepository.save(journalEntry);
        return journalEntryExists;
    }

    @Transactional
    public JournalEntry deleteEntry(ObjectId id, String username) throws Exception {
        User user = userService.findByUserName(username);

        // User not found
        if (user == null) {
            throw new Exception("User not found!");
        }

        user.getJournalEntries().remove(getEntry(id));
        userService.saveEntry(user);

        Optional<JournalEntry> entry = journalEntryRepository.findById(id);
        entry.ifPresent(e -> journalEntryRepository.deleteById(id));
        return entry.orElse(null);
    }
}


//  controller --> Service --> Repository