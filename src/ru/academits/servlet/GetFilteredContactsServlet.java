package ru.academits.servlet;

import ru.academits.PhoneBook;
import ru.academits.coverter.ContactConverter;
import ru.academits.model.Contact;
import ru.academits.service.ContactService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetFilteredContactsServlet extends HttpServlet {
    private static final long serialVersionUID = 4397609390686425955L;
    private final ContactService phoneBookService = PhoneBook.phoneBookService;
    private final ContactConverter contactConverter = PhoneBook.contactConverter;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try (BufferedReader requestReader = req.getReader(); OutputStream responseStream = resp.getOutputStream()) {
            String term = req.getParameter("term");
            List<Contact> filteredContacts = phoneBookService.getFilteredContacts(term);
            String contactListJson = contactConverter.convertToJson(filteredContacts);
            responseStream.write(contactListJson.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("error in GetFilteredContactsServlet GET: ");
            e.printStackTrace();
        }

    }

}
