package me.kasif.journalApp.controller;

import me.kasif.journalApp.entity.JournalEntry;
import me.kasif.journalApp.entity.User;
import me.kasif.journalApp.service.JournalEntryService;
import me.kasif.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journals")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/u/{username}")
    public ResponseEntity<List<JournalEntry>> getAllEntriesForUser(@PathVariable String username) {
        try {
            User userInDb = userService.findByUserName(username);
            if (userInDb == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<JournalEntry> all = userInDb.getJournalEntries();
            return new ResponseEntity<>(all, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/u/{username}") // "/journal" -> POST
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry, @PathVariable String username) {
        try {
            journalEntryService.saveEntry((JournalEntry) journalEntry, username);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception ex) {
            System.out.println(ex);
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

    @DeleteMapping("/{username}/{myId}")
    public ResponseEntity<JournalEntry> deleteJournalEntryById(@PathVariable ObjectId myId, @PathVariable String username) {
        try {
            JournalEntry deletedJournalEntry = journalEntryService.deleteEntry(myId, username);
            return new ResponseEntity<JournalEntry>(deletedJournalEntry, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<JournalEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
