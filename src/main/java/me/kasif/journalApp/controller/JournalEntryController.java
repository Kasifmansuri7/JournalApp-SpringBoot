package me.kasif.journalApp.controller;

import me.kasif.journalApp.entity.JournalEntry;
import me.kasif.journalApp.entity.User;
import me.kasif.journalApp.service.JournalEntryService;
import me.kasif.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journals")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllEntriesForUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userInDb = userService.findByUserName(authentication.getName());
            if (userInDb == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<JournalEntry> all = userInDb.getJournalEntries();
            return new ResponseEntity<>(all, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping // "/journal" -> POST
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            journalEntryService.saveEntry((JournalEntry) journalEntry, authentication.getName());
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String myId) {
        JournalEntry entry = null;
        try {
            ObjectId objectId = new ObjectId(myId);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userInDb = userService.findByUserName(authentication.getName());
            List<JournalEntry> collect = userInDb.getJournalEntries().stream().filter(x -> x.getId().equals(objectId)).toList();

            if (!collect.isEmpty()) {
                entry = collect.getFirst();
            }
        } catch (IllegalArgumentException e) {
            // invalid ObjectId format
            return ResponseEntity.notFound().build(); // or 404 if you prefer
        }

        return entry != null ? ResponseEntity.ok(entry) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{myId}")
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable String myId, @RequestBody JournalEntry journalEntry) {
        try {
            ObjectId objectId = new ObjectId(myId);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            JournalEntry entry = journalEntryService.getEntryByIdUser(objectId, authentication.getName());

            // Entry not found
            if (entry == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            journalEntryService.updateEntry(objectId, (JournalEntry) journalEntry);
            return new ResponseEntity<JournalEntry>(journalEntry, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<JournalEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{myId}")
    public ResponseEntity<JournalEntry> deleteJournalEntryById(@PathVariable String myId) {
        try {
            ObjectId objectId = new ObjectId(myId);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            JournalEntry entry = journalEntryService.getEntryByIdUser(objectId, authentication.getName());

            if (entry ==  null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            JournalEntry deletedJournalEntry = journalEntryService.deleteEntry(objectId, authentication.getName());
            return new ResponseEntity<JournalEntry>(deletedJournalEntry, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<JournalEntry>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
