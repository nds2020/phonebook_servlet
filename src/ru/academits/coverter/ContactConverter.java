package ru.academits.coverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.academits.model.Contact;
import java.util.List;

public class ContactConverter {
    private final Gson gson = new GsonBuilder().create();

    public String convertToJson(List<Contact> contactList) {
        return gson.toJson(contactList);
    }

    public Contact convertFormJson(String contactJson) {
        return gson.fromJson(contactJson, Contact.class);
    }

    public List<Contact> convertFormJsonToList(String contactsJson) {
        return gson.fromJson(contactsJson, new TypeToken<List<Contact>>(){}.getType());
    }
}