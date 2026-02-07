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
        HtmlTag html = BaseLayout.create(
            "Conversations - Scarlet Portal",
            h1("SCARLET PORTAL - Conversations"),
            BaseLayout.navigation(),
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
