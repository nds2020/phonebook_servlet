package ru.academits.service;

import ru.academits.PhoneBook;
import ru.academits.dao.ContactDao;
import ru.academits.model.Contact;

import java.util.List;


public class ContactService {
    private final ContactDao contactDao = PhoneBook.contactDao;

    private boolean isExistContactWithPhone(String phone) {
        List<Contact> contactList = contactDao.getAllContacts();
        for (Contact contact : contactList) {
            if (contact.getPhone().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    public ExecutionInfo validateContact(Contact contact) {
        ExecutionInfo contactValidation = new ExecutionInfo();
        contactValidation.setSuccess(true);
        if (contact.getFirstName().isEmpty()) {
            contactValidation.setSuccess(false);
            contactValidation.setMessage("Поле Имя должно быть заполнено.");
            return contactValidation;
        }

        if (contact.getLastName().isEmpty()) {
            contactValidation.setSuccess(false);
            contactValidation.setMessage("Поле Фамилия должно быть заполнено.");
            return contactValidation;
        }

        if (contact.getPhone().isEmpty()) {
            contactValidation.setSuccess(false);
            contactValidation.setMessage("Поле Телефон должно быть заполнено.");
            return contactValidation;
        }

        if (isExistContactWithPhone(contact.getPhone())) {
            contactValidation.setSuccess(false);
            contactValidation.setMessage("Номер телефона не должен дублировать другие номера в телефонной книге.");
            return contactValidation;
        }
        return contactValidation;
    }

    public ExecutionInfo addContact(Contact contact) {
        ExecutionInfo contactValidation = validateContact(contact);
        if (contactValidation.isSuccess()) {
            contactDao.add(contact);
        }
        return contactValidation;
    }

    public List<Contact> getAllContacts() {
        return contactDao.getAllContacts();
    }

    public List<Contact> getFilteredContacts(String term) {
        return contactDao.getFilteredContacts(term);
    }

    public ExecutionInfo deleteContact(Contact contact) {
        ExecutionInfo contactDeletion = new ExecutionInfo();
        contactDeletion.setSuccess(contactDao.delete(contact));
        if (!contactDeletion.isSuccess()) {
            contactDeletion.setMessage("Ошибка при удалении контакта.");
        }
        return contactDeletion;
    }

    public ExecutionInfo deleteCheckedContacts(List<Contact> contacts) {
        ExecutionInfo checkedContactsDeletion = new ExecutionInfo();
        checkedContactsDeletion.setSuccess(contactDao.deleteChecked(contacts));
        if (!checkedContactsDeletion.isSuccess()) {
            checkedContactsDeletion.setMessage("Ошибка при удалении выбранных контактов.");
        }
        return checkedContactsDeletion;
    }
}
