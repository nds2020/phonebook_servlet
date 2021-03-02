package ru.academits.coverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.academits.service.ExecutionInfo;

public class ExecutionInfoConverter {
    private final Gson gson = new GsonBuilder().create();

    public String convertToJson(ExecutionInfo executionInfo) {
        return gson.toJson(executionInfo);
    }
}
