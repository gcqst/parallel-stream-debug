import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import model.Faq;
import util.JsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        Main obj = new Main();
        List<Faq> faqs = obj.listFaq();
        obj.debug(faqs);
    }

    private List<Faq> listFaq() throws IOException {
        URL url = Resources.getResource("faq.json");
        String text = Resources.toString(url, Charsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrayNode = mapper.readTree(text).get("dataset");
        return JsonUtils.toObjList(arrayNode.toString().replaceAll("\"answer\":\\{\\}", "\"answer\":\"\""), Faq.class);
    }

    private void debug(List<Faq> faqs) {
        faqs.forEach(faq -> {
                    List<Map<String, Object>> maps = toMap(faq);
                    long diff = maps.size() - maps.stream().filter(Objects::isNull).count() - maps.stream().filter(Objects::nonNull).count();
                    //diff should be 0, right?
                    if (diff != 0) {
                        System.out.println("********");
                        System.out.println(faq.getId());
                    }
                });
    }

    private List<Map<String, Object>> toMap(Faq faq) {
        List<Map<String, Object>> result = Lists.newLinkedList();
        String intentId = faq.getId();
        String question = faq.getQuestion();
        String answer = faq.getAnswer();
        boolean enabled = faq.getEnabled();
        String intentName = faq.getIntentName();
        String intentLanguage = faq.getLanguage();

        //parallelStream() cause problem
        faq.getVariations().parallelStream().forEach(variation -> {
            Map<String, Object> map = Maps.newHashMap();
            map.put("intentId", intentId);
            map.put("question", question);
            map.put("answer", answer);
            map.put("enabled", enabled);
            map.put("intentName", intentName);
            map.put("intentLanguage", intentLanguage);
            map.put("variationText", variation.getText());
            map.put("variationId", variation.getId());
            map.put("variationLanguage", variation.getLanguage());
            result.add(map);
        });

        return result;
    }
}
