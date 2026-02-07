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
        HtmlTag html = html(
            head(
                meta().attr("charset", "UTF-8"),
                title("Scarlet Portal"),
                style("""
                    body{font:14px Monaco,monospace;background:#1a1625;color:#c9d1d9;margin:0;padding:20px}
                    h1{color:#ff6b9d;border-bottom:2px solid #ff6b9d;padding-bottom:10px}
                    .nav{margin:15px 0;padding:10px 0;border-bottom:1px solid #3f3755}
                    .nav a{color:#4ecdc4;text-decoration:none;margin-right:20px}
                    .nav a:hover{color:#ff6b9d}
                    .stats{background:#252033;padding:12px;margin:15px 0;border-radius:4px}
                    .form{background:#252033;padding:18px;margin:15px 0;border-radius:4px}
                    input,textarea{width:98%;background:#1a1625;color:#c9d1d9;border:1px solid #3f3755;padding:8px;margin:6px 0;font-family:inherit}
                    button{background:#ff6b9d;color:#fff;border:none;padding:10px 18px;cursor:pointer;margin-top:8px;border-radius:3px}
                    button:hover{background:#e5568a}
                    .list{margin-top:20px}
                    .card{background:#252033;padding:14px;margin:12px 0;border-left:3px solid #3f3755;border-radius:3px}
                    .card.in{border-left-color:#4ecdc4}
                    .card.out{border-left-color:#ff6b9d}
                    .card .top{font-size:11px;color:#7d8590;margin-bottom:8px}
                    .card .msg{margin:8px 0;line-height:1.5}
                    .card .bot{font-size:10px;color:#545d68;margin-top:8px;padding-top:8px;border-top:1px solid #3f3755}
                    """)
            ),
            body(
                h1("SCARLET PORTAL"),
                div(
                    a("Home").withHref("/"),
                    a("Conversations").withHref("/conversations")
                ).withClass("nav"),
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
            )
        ).attr("lang", "en");

        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.getWriter().write("<!DOCTYPE html>\n");
        response.getWriter().write(html.render());
    }

    @Override
    public String getContentType() {
        return MediaType.TEXT_HTML_VALUE;
    }

}
