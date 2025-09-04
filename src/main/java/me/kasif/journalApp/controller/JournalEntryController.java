package me.kasif.journalApp.controller;

import me.kasif.journalApp.entity.JournalEntry;
import me.kasif.journalApp.repository.JournalEntryRepository;
import me.kasif.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping // "/journal" -> GET
    public List<JournalEntry> getAll() {
        return journalEntryService.getAll();

    }

    @PostMapping // "/journal" -> POST
    public JournalEntry createJournalEntry(@RequestBody JournalEntry journalEntry) {
        return journalEntryService.saveEntry((JournalEntry) journalEntry);
    }

    @GetMapping("/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String myId) {
        JournalEntry entry = null;
        try {
            ObjectId objectId = new ObjectId(myId); // convert string to ObjectId
            entry = journalEntryService.getEntry(objectId);
        } catch (IllegalArgumentException e) {
            // invalid ObjectId format
            return ResponseEntity.notFound().build(); // or 404 if you prefer
        }

        return entry != null ? ResponseEntity.ok(entry) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{myId}")
    public JournalEntry updateJournalEntry(@PathVariable ObjectId myId, @RequestBody JournalEntry journalEntry) {
        return journalEntryService.updateEntry(myId,(JournalEntry) journalEntry);
    }

    @DeleteMapping("/{myId}")
    public JournalEntry deleteJournalEntryById(@PathVariable ObjectId myId) {
        return journalEntryService.deleteEntry(myId);
    }
}
