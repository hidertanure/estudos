package br.com.hider.onlyone.model.business;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.hider.onlyone.model.entity.AppType;
import br.com.hider.onlyone.model.entity.Author;
import br.com.hider.onlyone.model.entity.Chat;
import br.com.hider.onlyone.model.entity.Message;

/**
 * Created by hidertanure on 13/06/13.
 */
public class BusinessController {

    public BusinessController(){
        super();
    }

    public void newMessage(Context ctx, AppType appType, ArrayList<String> raw){

        System.out.println(appType.toString());
        System.out.println(raw);
        Long appTypeId = 0L;

        if(appType.equals(AppType.WHATS)){
            appTypeId = 1L;
        }else if(appType.equals(AppType.HANGOUTS)){
            appTypeId = 2L;
        }else if(appType.equals(AppType.FACEBOOK)){
            appTypeId = 3L;
        }

        String chatTitle = "Not Defined.";
        if(appType.equals(AppType.WHATS)){
            chatTitle = raw.get(0);
        }else if(appType.equals(AppType.HANGOUTS)){
            chatTitle = raw.get(0);
        }

        String messageAuthorDisplayName = "Not Defined.";
        String messageAuthorExternalId = "";
        if(appType.equals(AppType.WHATS)){
            messageAuthorDisplayName = raw.get(0);
        }else if(appType.equals(AppType.HANGOUTS)){
            messageAuthorDisplayName = raw.get(0);
            if(raw.size()>2){
            	messageAuthorExternalId = raw.get(1);
            }
        }

        String messageContent = "";
        if(appType.equals(AppType.WHATS)){
        	if(raw.size()>2){
        		messageContent = raw.get(2);
        	}else{
        		messageContent = raw.get(1);
        	}
        }else if(appType.equals(AppType.HANGOUTS)){
        	if(raw.size()>2){
        		messageContent = raw.get(2);
        	}else{
        		messageContent = raw.get(1);
        	}
        }


        Author author = new Author();
        author.setDisplayName(messageAuthorDisplayName);
        author.setExternalId(messageAuthorExternalId);

        author.openDb(ctx);
        List<Author> authorVerify = author.list(author);
        if(authorVerify==null || authorVerify.isEmpty()){
            Long id = author.save(author);
            author.setId(id);
        }else{
            author = authorVerify.get(0);
        }

        Chat chat = new Chat();
        chat.setTitle(chatTitle);
        chat.setApptype_id(appTypeId);

        chat.openDb(ctx);
        List<Chat> chatVerify = chat.list(chat);
        if(chatVerify==null || chatVerify.isEmpty()){
            Long id = chat.save(chat);
            chat.setId(id);
        }else{
            chat = chatVerify.get(0);
        }

        Message message = new Message();
        message.setAuthorId(author.getId());
        message.setMessageContent(messageContent);
        message.setChatId(chat.getId());
        message.setTimeStamp(System.currentTimeMillis());

        message.openDb(ctx);
        List<Message> messageVerify = message.list(message);
        if(messageVerify==null || messageVerify.isEmpty()){
            Long id = message.save(message);
            message.setId(id);
        }else{
            message = messageVerify.get(0);
        }
        return;
    }

    public List<Chat> getChatsForList(Context ctx){

        List<Chat> newChatList = new ArrayList<Chat>();

        Chat cahtFilter = new Chat();
        try{
            cahtFilter.openDb(ctx);
            List<Chat> chatList = cahtFilter.list(cahtFilter);
            for(Chat chat : chatList){
                Message messageFilter = new Message();
                messageFilter.setChatId(chat.getId());
                chat.setMessages(new ArrayList<Message>());

                try{
                    messageFilter.openDb(ctx);
                    List<Message> messageList = messageFilter.list(messageFilter);
                    for(Message message : messageList){
                        Author authorFilter = new Author();
                        authorFilter.setId(message.getAuthorId());

                        try{
                            authorFilter.openDb(ctx);
                            message.setAuthor(authorFilter.retrive(authorFilter));
                            chat.getMessages().add(message);
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }finally{
                            authorFilter.closeDb();
                        }
                    }
                    newChatList.add(chat);
                }catch(Exception ex){
                    ex.printStackTrace();
                }finally{
                    messageFilter.closeDb();
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            cahtFilter.closeDb();
        }

        return newChatList;
    }
}
