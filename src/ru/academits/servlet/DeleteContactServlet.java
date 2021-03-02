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
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class DeleteContactServlet extends HttpServlet {
    private static final long serialVersionUID = -4241734906178441799L;
    private final ContactService phoneBookService = PhoneBook.phoneBookService;
    private final ContactConverter contactConverter = PhoneBook.contactConverter;
    private final ExecutionInfoConverter executionInfoConverter = PhoneBook.executionInfoConverter;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (BufferedReader requestReader = req.getReader(); OutputStream responseStream = resp.getOutputStream()) {
            String contactJson = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Contact contact = contactConverter.convertFormJson(contactJson);

            ExecutionInfo deletion = phoneBookService.deleteContact(contact);
            if (!deletion.isSuccess()) {
                resp.setStatus(500);
            }

            String deletionJson = executionInfoConverter.convertToJson(deletion);
            responseStream.write(deletionJson.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("error in DeleteContactServlet POST: ");
            e.printStackTrace();
        }
    }
}
