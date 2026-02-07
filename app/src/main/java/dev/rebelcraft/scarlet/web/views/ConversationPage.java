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
public class ConversationPage implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HtmlTag html = BaseLayout.create(
            "Conversation - Scarlet Portal",
            h1("SCARLET PORTAL - Conversation"),
            BaseLayout.navigation(),
            div().withId("info").withClass("info"),
            div().withId("messages").withClass("messages"),
            script(rawHtml("""
                const urlParams = new URLSearchParams(window.location.search);
                const chatId = urlParams.get('chatId');
                function loadConversation(){
                  if(!chatId){
                    document.getElementById('messages').innerHTML='<div class="empty">No chat ID specified</div>';
                    return;
                  }
                  fetch(`/api/conversations/${chatId}`)
                    .then(r=>r.json())
                    .then(conv=>{
                      document.getElementById('info').innerHTML=`
                        <strong>Chat:</strong> ${esc(conv.chatTitle)} | 
                        <strong>ID:</strong> ${conv.chatId} | 
                        <strong>Messages:</strong> ${conv.messageCount}
                      `;
                      if(conv.messages.length===0){
                        document.getElementById('messages').innerHTML='<div class="empty">No messages</div>';
                        return;
                      }
                      let html='';
                      conv.messages.forEach(msg=>{
                        const direction=msg.incoming?'incoming':'outgoing';
                        const time=new Date(msg.timestamp).toLocaleString();
                        html+=`<div class="message ${direction}">
                          <div class="header">
                            <span class="sender">${esc(msg.senderName)}</span>
                            <span class="time">${time}</span>
                          </div>
                          <div class="text">${esc(msg.messageText)}</div>
                        </div>`;
                      });
                      document.getElementById('messages').innerHTML=html;
                    })
                    .catch(e=>{
                      document.getElementById('messages').innerHTML='<div class="empty">Error loading conversation</div>';
                      console.error(e);
                    });
                }
                function esc(s){
                  if(!s) return '';
                  let e=document.createElement('div');
                  e.textContent=s;
                  return e.innerHTML;
                }
                loadConversation();
                setInterval(loadConversation,5000);
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
