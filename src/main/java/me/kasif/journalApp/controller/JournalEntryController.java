package me.kasif.journalApp.controller;

import me.kasif.journalApp.entity.JournalEntry;
import me.kasif.journalApp.repository.JournalEntryRepository;
import me.kasif.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<JournalEntry>> getAll() {
        return new ResponseEntity<>(journalEntryService.getAll(), HttpStatus.OK);
    }

    @PostMapping // "/journal" -> POST
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry) {
        try {
            journalEntryService.saveEntry((JournalEntry) journalEntry);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable ObjectId myId, @RequestBody JournalEntry journalEntry) {
        try {
            journalEntryService.updateEntry(myId, (JournalEntry) journalEntry);
            return new ResponseEntity<JournalEntry>(journalEntry, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<JournalEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{myId}")
    public ResponseEntity<JournalEntry> deleteJournalEntryById(@PathVariable ObjectId myId) {
        try {
            JournalEntry deletedJournal = journalEntryService.deleteEntry(myId);
            return new ResponseEntity<JournalEntry>(deletedJournal, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<JournalEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
