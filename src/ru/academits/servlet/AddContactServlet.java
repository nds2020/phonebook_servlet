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
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class AddContactServlet extends HttpServlet {
    private static final long serialVersionUID = -4116838952926573387L;
    private final ContactService phoneBookService = PhoneBook.phoneBookService;
    private final ContactConverter contactConverter = PhoneBook.contactConverter;
    private final ExecutionInfoConverter executionInfoConverter = PhoneBook.executionInfoConverter;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (OutputStream responseStream = resp.getOutputStream()) {
            String contactJson = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Contact contact = contactConverter.convertFormJson(contactJson);

            ExecutionInfo addition = phoneBookService.addContact(contact);
            String additionJson = executionInfoConverter.convertToJson(addition);
            if (!addition.isSuccess()) {
                resp.setStatus(500);
            }

            responseStream.write(additionJson.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("error in AddContactServlet POST: ");
            e.printStackTrace();
        }
    }
}
