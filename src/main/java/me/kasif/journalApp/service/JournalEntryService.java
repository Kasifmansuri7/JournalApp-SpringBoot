package me.kasif.journalApp.service;

import me.kasif.journalApp.entity.JournalEntry;
import me.kasif.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public JournalEntry saveEntry(JournalEntry journalEntry) {
        journalEntry.setDate(LocalDateTime.now());
        return journalEntryRepository.save(journalEntry);
    }

    public JournalEntry getEntry(ObjectId id) {
        Optional<JournalEntry> result = journalEntryRepository.findById(id);
        return result.orElse(null);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public JournalEntry updateEntry(ObjectId id, JournalEntry journalEntry) {
        JournalEntry journalExists = journalEntryRepository.findById(id).orElse(null);

        // Journal existing
        if (journalExists != null) {
            journalExists.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().equals("") ? journalEntry.getTitle() : journalExists.getTitle());
            journalExists.setContent(journalEntry.getContent() != null && !journalEntry.getContent().equals("") ? journalEntry.getContent() : journalExists.getContent());
        }

        journalEntryRepository.save(journalEntry);
        return journalExists;
    }

    public JournalEntry deleteEntry(ObjectId id) {
        Optional<JournalEntry> entry = journalEntryRepository.findById(id);
        entry.ifPresent(e -> journalEntryRepository.deleteById(id));
        return entry.orElse(null);
    }
}


//  controller --> Service --> Repository