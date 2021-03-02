package ru.academits.servlet;

import ru.academits.PhoneBook;
import ru.academits.coverter.ContactConverter;
import ru.academits.coverter.ExecutionInfoConverter;
import ru.academits.model.Contact;
import ru.academits.service.ContactService;
import ru.academits.service.ExecutionInfo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;


public class DeleteCheckedContactsServlet extends HttpServlet {
    private static final long serialVersionUID = -861679133465739114L;
    private final ContactService phoneBookService = PhoneBook.phoneBookService;
    private final ContactConverter contactConverter = PhoneBook.contactConverter;
    private final ExecutionInfoConverter executionInfoConverter = PhoneBook.executionInfoConverter;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (BufferedReader requestReader = req.getReader(); OutputStream responseStream = resp.getOutputStream()) {
            String contactsJson = requestReader.lines().collect(Collectors.joining(System.lineSeparator()));
            List<Contact> contacts = contactConverter.convertFormJsonToList(contactsJson);

            ExecutionInfo deletion = phoneBookService.deleteCheckedContacts(contacts);
            if (!deletion.isSuccess()) {
                resp.setStatus(500);
            }

            String deletionJson = executionInfoConverter.convertToJson(deletion);
            responseStream.write(deletionJson.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("error in DeleteCheckedContactsServlet POST: ");
            e.printStackTrace();
        }
    }
}
