package dev.rebelcraft.scarlet.web.views;

import j2html.tags.DomContent;
import j2html.tags.specialized.BodyTag;
import j2html.tags.specialized.HeadTag;
import j2html.tags.specialized.HtmlTag;

import static j2html.TagCreator.*;

public class BaseLayout {

    public static HtmlTag create(String pageTitle, DomContent... bodyContent) {
        return html(
            createHead(pageTitle),
            createBody(bodyContent)
        ).attr("lang", "en");
    }

    private static HeadTag createHead(String pageTitle) {
        return head(
            meta().attr("charset", "UTF-8"),
            title(pageTitle),
            link().withRel("stylesheet").withHref("/styles.css")
        );
    }

    private static BodyTag createBody(DomContent... content) {
        return body(content);
    }

    public static DomContent navigation() {
        return div(
            a("Home").withHref("/"),
            a("Conversations").withHref("/conversations")
        ).withClass("nav");
    }

}
