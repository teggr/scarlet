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
public class ConversationsPage implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HtmlTag html = html(
            head(
                meta().attr("charset", "UTF-8"),
                title("Conversations - Scarlet Portal"),
                style("""
                    body{font:14px Monaco,monospace;background:#1a1625;color:#c9d1d9;margin:0;padding:20px}
                    h1{color:#ff6b9d;border-bottom:2px solid #ff6b9d;padding-bottom:10px}
                    .nav{margin:15px 0;padding:10px 0;border-bottom:1px solid #3f3755}
                    .nav a{color:#4ecdc4;text-decoration:none;margin-right:20px}
                    .nav a:hover{color:#ff6b9d}
                    .card{background:#252033;padding:14px;margin:12px 0;border-left:3px solid #4ecdc4;border-radius:3px;cursor:pointer;transition:all 0.2s}
                    .card:hover{background:#2d2640;border-left-color:#ff6b9d}
                    .card .title{font-size:16px;font-weight:bold;color:#ff6b9d;margin-bottom:8px}
                    .card .meta{font-size:11px;color:#7d8590;margin-bottom:8px}
                    .card .preview{color:#c9d1d9;line-height:1.5}
                    .empty{text-align:center;padding:40px;color:#7d8590}
                    """)
            ),
            body(
                h1("SCARLET PORTAL - Conversations"),
                div(
                    a("Home").withHref("/"),
                    a("Conversations").withHref("/conversations")
                ).withClass("nav"),
                div().withId("conversations"),
                script(rawHtml("""
                    function loadConversations(){
                      fetch('/api/conversations')
                        .then(r=>r.json())
                        .then(conversations=>{
                          if(conversations.length===0){
                            document.getElementById('conversations').innerHTML='<div class="empty">No conversations yet</div>';
                            return;
                          }
                          let html='';
                          conversations.forEach(conv=>{
                            const lastMsg=conv.messages[conv.messages.length-1];
                            const preview=lastMsg?lastMsg.messageText.substring(0,100):'No messages';
                            html+=`<div class="card" onclick="viewConversation(${conv.chatId})">
                              <div class="title">${esc(conv.chatTitle)}</div>
                              <div class="meta">Chat ID: ${conv.chatId} | Messages: ${conv.messageCount}</div>
                              <div class="preview">${esc(preview)}</div>
                            </div>`;
                          });
                          document.getElementById('conversations').innerHTML=html;
                        })
                        .catch(e=>{
                          document.getElementById('conversations').innerHTML='<div class="empty">Error loading conversations</div>';
                          console.error(e);
                        });
                    }
                    function viewConversation(chatId){
                      window.location.href=`/conversation?chatId=${chatId}`;
                    }
                    function esc(s){
                      if(!s) return '';
                      let e=document.createElement('div');
                      e.textContent=s;
                      return e.innerHTML;
                    }
                    loadConversations();
                    setInterval(loadConversations,5000);
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
