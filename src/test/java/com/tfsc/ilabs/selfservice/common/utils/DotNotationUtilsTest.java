package com.tfsc.ilabs.selfservice.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * Created by ravi.b on 03-06-2019.
 */
@RunWith(SpringRunner.class)
public class DotNotationUtilsTest {

        @Test
        public void test_addToJsonObject() {
                ObjectNode obj = DotNotationUtils.jsonNodeFactory.objectNode();

                obj.put("name", "Ravi");
                obj.put("FN", "Ravi");
                obj.put("LN", "Bhushan");
                ObjectNode contact = DotNotationUtils.jsonNodeFactory.objectNode();
                contact.put("phone", 1123456789);
                obj.set("contact", contact);

                DotNotationUtils.addToJsonObject("contact.email", "ravi@gmail.com", obj);

                Assert.assertEquals("{\"name\":\"Ravi\",\"FN\":\"Ravi\",\"LN\":\"Bhushan\",\"contact\":{\"phone\":1123456789,\"email\":\"ravi@gmail.com\"}}", obj.toString());

                // testing array
                DotNotationUtils.addToJsonObject("testArr[]", 1, obj);

                Assert.assertEquals("{\"name\":\"Ravi\",\"FN\":\"Ravi\",\"LN\":\"Bhushan\",\"contact\":{\"phone\":1123456789,\"email\":\"ravi@gmail.com\"},\"testArr\":[1]}", obj.toString());

                DotNotationUtils.addToJsonObject("testArr[]", 2, obj);

                Assert.assertEquals("{\"name\":\"Ravi\",\"FN\":\"Ravi\",\"LN\":\"Bhushan\",\"contact\":{\"phone\":1123456789,\"email\":\"ravi@gmail.com\"},\"testArr\":[1,2]}", obj.toString());

                // testing adding another object node in tree

                ObjectNode marks = DotNotationUtils.jsonNodeFactory.objectNode();
                marks.put("English", 90);
                marks.put("Maths", 100);
                marks.put("Science", 95);

                DotNotationUtils.addToJsonObject("my.marks", marks, obj);

                Assert.assertEquals("{\"name\":\"Ravi\",\"FN\":\"Ravi\",\"LN\":\"Bhushan\",\"contact\":{\"phone\":1123456789,\"email\":\"ravi@gmail.com\"},\"testArr\":[1,2],\"my\":{\"marks\":{\"English\":90,\"Maths\":100,\"Science\":95}}}", obj.toString());

        }

        @Test
        public void test_createJSONObject() {
                HashMap<String, JsonNode> dottedLocations = new HashMap<>();
                dottedLocations.put("name", TextNode.valueOf("ravi"));
                dottedLocations.put("age", IntNode.valueOf(25));
                dottedLocations.put("contact.email", TextNode.valueOf("ravi@gmail.com"));
                dottedLocations.put("contact.phone", IntNode.valueOf(1234567895));
                dottedLocations.put("contact.nicks[]", TextNode.valueOf("Iron man"));

                ObjectNode objectNode = DotNotationUtils.createJsonObject(dottedLocations);

                Assert.assertEquals("{\"contact\":{\"nicks\":[\"Iron man\"],\"email\":\"ravi@gmail.com\",\"phone\":1234567895},\"name\":\"ravi\",\"age\":25}", objectNode.toString());
        }

        @Test
        public void test_getValueFromJson() {
                String json = "{\"name\":\"Ravi\",\"FN\":\"Ravi\",\"LN\":\"Bhushan\",\"contact\":{\"email\":[\"ravi1@gmail.com\",\"ravi2@gmail.com\"],\"phone\":1123456789}}";

                Assert.assertEquals("Ravi", DotNotationUtils.getValueFromJson("name", json).asText());
                Assert.assertEquals("1123456789", DotNotationUtils.getValueFromJson("contact.phone", json).asText());
                Assert.assertEquals("ravi2@gmail.com",
                                DotNotationUtils.getValueFromJson("contact.email[1]", json).asText());
                Assert.assertEquals("[\"ravi1@gmail.com\",\"ravi2@gmail.com\"]",
                                DotNotationUtils.getValueFromJson("contact.email", json).toString());
        }

}
