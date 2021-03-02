package ru.academits.dao;

import ru.academits.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by Anna on 17.06.2017.
 */
public class ContactDao {
    private final List<Contact> contactList = new ArrayList<>();
    private final AtomicInteger idSequence = new AtomicInteger(0);

    public ContactDao() {
        Contact contact = new Contact();
        contact.setId(getNewId());
        contact.setFirstName("Иван");
        contact.setLastName("Иванов");
        contact.setPhone("9123456789");
        contactList.add(contact);
    }

    private int getNewId() {
        return idSequence.addAndGet(1);
    }

    public List<Contact> getAllContacts() {
        return contactList;
    }

    public List<Contact> getFilteredContacts(String term) {
        if (term.length() == 0) {
            return getAllContacts();
        }

        String finalTerm = term.toLowerCase();
        return contactList.stream()
                .filter(contact -> contact.getFirstName().toLowerCase().contains(finalTerm)
                || contact.getLastName().toLowerCase().contains(finalTerm)
                || contact.getPhone().toLowerCase().contains(finalTerm))
                .collect(Collectors.toList());
    }

    public void add(Contact contact) {
        contact.setId(getNewId());
        contactList.add(contact);
    }

    public boolean deleteChecked(List<Contact> contacts) {
        return contactList.removeAll(contacts);
    }

    public boolean delete(Contact contact) {
        return contactList.remove(contact);
    }
}
