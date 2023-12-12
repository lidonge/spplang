package free.servpp.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-12-06@version 1.0
 */
public class Test1 {
    public static void main(String[] args) throws JsonProcessingException {
        String json = "{\n" +
                "  \"fields\": [\n" +
                "    {\n" +
                "      \"type\": \"int\",\n" +
                "      \"name\": \"tranMode\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"String\",\n" +
                "      \"name\": \"sourceBranchNo\"\n" +
                "    },{\n" +
                "      \"type\": \"String\",\n" +
                "      \"name\": \"userLang\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"String\",\n" +
                "      \"name\": \"srcSysTmnlNo\"\n" +
                "    },{\n" +
                "      \"type\": \"String\",\n" +
                "      \"name\": \"srcSysSvrId\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"String\",\n" +
                "      \"name\": \"imageSeqNo\"\n" +
                "    },{\n" +
                "      \"type\": \"String\",\n" +
                "      \"name\": \"reference\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"String\",\n" +
                "      \"name\": \"runDate\"\n" +
                "    },{\n" +
                "      \"type\": \"String\",\n" +
                "      \"name\": \"totalNum\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"String\",\n" +
                "      \"name\": \"currentNum\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"String\",\n" +
                "      \"name\": \"totalRows\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        Map obj = objectMapper.readValue(json, Map.class);
        List lines = (List) obj.get("fields");
        for(Object o : lines){
            Map line = (Map) o;
            String name = (String) line.get("name");
            String upperName = firstToUpperCase(name);
            line.put("upper", upperName);
        }
        String muastache = "/json2cls.mustache";
        String s = getObjectString(obj,muastache);
        System.out.println(s);
    }
    public static String firstToUpperCase(String ptype) {
        String name;
        char first = ptype.charAt(0);
        first = Character.toUpperCase(first);
        StringBuffer sb = new StringBuffer();
        name = sb.append(first).append(ptype.substring(1)).toString();
        return name;
    }

    private static String getObjectString( Object obj, String mastache) {
        InputStream mustache = Test.class.getResourceAsStream(mastache);
        Template template = Mustache.compiler().escapeHTML(false).compile(new InputStreamReader(mustache));
        String s = template.execute(obj);
        return s;
    }
}
