package br.com.hider.onlyone.model.gui;

/**
 * Created by hidertanure on 01/06/13.
 */
public class MessageListReference {

    //Exibição
    private int icon;
    private String authorName;
    private String time;
    private String messageContent;

    //Referencia
    private Long chatId;

    public MessageListReference(int icon, String authorName, String messageContent, String time, Long chatId) {
        super();
        this.icon = icon;
        this.authorName = authorName;
        this.messageContent = messageContent;
        this.time = time;
        this.chatId = chatId;

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String essageContent) {
        messageContent = essageContent;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
