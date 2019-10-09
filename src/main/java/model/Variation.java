
package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Variation {

    private String id;
    @JsonProperty("intent_ranking")
    private List<IntentRanking> intentRanking;
    private String language;
    private String text;
    private boolean trained;
    //是否是标准问法
    @JsonProperty("is_suggestion")
    private boolean standard;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<IntentRanking> getIntentRanking() {
        return intentRanking;
    }

    public void setIntentRanking(List<IntentRanking> intentRanking) {
        this.intentRanking = intentRanking;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTrained() {
        return trained;
    }

    public void setTrained(boolean trained) {
        this.trained = trained;
    }

    public boolean isStandard() {
        return standard;
    }

    public void setStandard(boolean standard) {
        this.standard = standard;
    }
}
