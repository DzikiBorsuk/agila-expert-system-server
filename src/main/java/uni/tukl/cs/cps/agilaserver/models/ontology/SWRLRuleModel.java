package uni.tukl.cs.cps.agilaserver.models.ontology;

public class SWRLRuleModel {

    public SWRLRuleModel() {

    }

    public SWRLRuleModel(String name, String comment, String rule) {
        this.name = name;
        this.comment = comment;
        this.rule = rule;
    }

    private String name;

    private String rule;

    private String comment;

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
