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
        HtmlTag html = html(
            head(
                meta().attr("charset", "UTF-8"),
                title("Conversation - Scarlet Portal"),
                style("""
                    body{font:14px Monaco,monospace;background:#1a1625;color:#c9d1d9;margin:0;padding:20px}
                    h1{color:#ff6b9d;border-bottom:2px solid #ff6b9d;padding-bottom:10px}
                    .nav{margin:15px 0;padding:10px 0;border-bottom:1px solid #3f3755}
                    .nav a{color:#4ecdc4;text-decoration:none;margin-right:20px}
                    .nav a:hover{color:#ff6b9d}
                    .info{background:#252033;padding:12px;margin:15px 0;border-radius:4px}
                    .messages{margin-top:20px}
                    .message{background:#252033;padding:14px;margin:12px 0;border-radius:3px}
                    .message.incoming{border-left:3px solid #4ecdc4}
                    .message.outgoing{border-left:3px solid #ff6b9d}
                    .message .header{font-size:11px;color:#7d8590;margin-bottom:8px}
                    .message .header .sender{color:#ff6b9d;font-weight:bold}
                    .message .header .time{float:right}
                    .message .text{line-height:1.5;word-wrap:break-word}
                    .empty{text-align:center;padding:40px;color:#7d8590}
                    """)
            ),
            body(
                h1("SCARLET PORTAL - Conversation"),
                div(
                    a("Home").withHref("/"),
                    a("Conversations").withHref("/conversations")
                ).withClass("nav"),
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
