package ru.clevertec.kalustau.util;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class JsonProtobufUtility {


    public static String toJson(MessageOrBuilder messageOrBuilder) {
        String json = "";
        try {
            json = JsonFormat.printer().print(messageOrBuilder);

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String toJson(List<? extends MessageOrBuilder> messageOrBuilders) {
        if(messageOrBuilders.isEmpty()) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder("[\n");
        messageOrBuilders.forEach(message -> builder.append(toJson(message)).append(",\n"));
        builder.delete(builder.length() -2 ,builder.length());
        builder.append("\n]");
        return builder.toString();
    }
}
