package br.com.hider.onlyone.model.gui;

/**
 * Created by hidertanure on 01/06/13.
 */
public class ChatListReference {

    //Exibição
    private int icon;
    private String title;
    private String subTitle;
    private String time;

    //Referencia
    private Long chatId;

    public ChatListReference(int icon, String title, String subTitle, String time, Long chatId) {
        super();
        this.icon = icon;
        this.title = title;
        this.subTitle = subTitle;
        this.time = time;
        this.chatId = chatId;

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
