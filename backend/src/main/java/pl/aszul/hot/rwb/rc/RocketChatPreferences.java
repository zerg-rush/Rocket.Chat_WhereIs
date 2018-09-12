package pl.aszul.hot.rwb.rc;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class RocketChatPreferences {

    @JsonIgnore
    private Map<String, Object> preferences = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getPreferences() {
        return this.preferences;
    }

    @JsonAnySetter
    public void setPreferences(String name, Object value) {
        this.preferences.put(name, value);
    }

}
