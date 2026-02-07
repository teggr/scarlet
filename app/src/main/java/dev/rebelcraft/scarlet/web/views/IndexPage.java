package dev.rebelcraft.scarlet.web.views;

import j2html.tags.specialized.HtmlTag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

import java.util.Map;

import static j2html.TagCreator.*;

@Component
public class IndexPage implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HtmlTag html = BaseLayout.create(
            "Scarlet Portal",
            h1("SCARLET PORTAL"),
            BaseLayout.navigation(),
            div(
                text("Archived items: "),
                strong("0").withId("count")
            ).withClass("stats"),
            div(
                div("Target Node:"),
                input().withType("text").withId("node").withPlaceholder("Enter destination"),
                div("Message:"),
                textarea().withId("msg").attr("rows", "3").withPlaceholder("Enter content"),
                button("Transmit").attr("onclick", "send()")
            ).withClass("form"),
            button("Reload").attr("onclick", "load()").withStyle("background:#4ecdc4;color:#1a1625"),
            div().withId("list").withClass("list"),
            script(rawHtml("""
                function load(){
                fetch('/aperture/utterances').then(r=>r.json()).then(d=>{let h='';d.forEach(i=>{let c=i.trajectory==='INBOUND'?'in':'out';h+=`<div class="card ${c}"><div class="top">${esc(i.originatorTag)} [${i.trajectory}]</div><div class="msg">${esc(i.inscription)}</div><div class="bot">token:${esc(i.ephemeralToken)} | thread:${esc(i.conversationThread)}</div></div>`;});document.getElementById('list').innerHTML=h||'<div class="card">No data</div>';});
                fetch('/aperture/statistics').then(r=>r.json()).then(d=>{document.getElementById('count').textContent=d.totalArchivedVolume;});
                }
                function send(){
                let n=document.getElementById('node').value;
                let m=document.getElementById('msg').value;
                if(n&&m){
                fetch('/aperture/transmit',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({destinationNode:n,payloadContent:m})}).then(()=>{document.getElementById('node').value='';document.getElementById('msg').value='';load();});
                }
                }
                function esc(s){let e=document.createElement('div');e.textContent=s;return e.innerHTML}
                load();
                setInterval(load,10000);
                """))
        );

        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.getWriter().write("<!DOCTYPE html>\n");
        response.getWriter().write(html.render());
    }

    @Override
    public String getContentType() {
        return MediaType.TEXT_HTML_VALUE;
    }

}
