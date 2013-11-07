package br.com.hider.poc.domain.model;

public class Place {
	
	private Long id;
	
	private String name;
	private String category;
	
	private Long likes;
	private Long talkAbout;
	
	public Place(){
		super();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Long getLikes() {
		return likes;
	}
	public void setLikes(Long likes) {
		this.likes = likes;
	}
	public Long getTalkAbout() {
		return talkAbout;
	}
	public void setTalkAbout(Long talkAbout) {
		this.talkAbout = talkAbout;
	}
}
