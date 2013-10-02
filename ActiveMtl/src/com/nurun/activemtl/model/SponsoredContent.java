package com.nurun.activemtl.model;

public class SponsoredContent {
	private final String contentId;
	private final String title;
	private String thumbnail;
	private String description;

	public SponsoredContent(final String contentId, final String title) {
		this.contentId = contentId;
		this.title = title;
	}

	public String getId() {
		return contentId;
	}

	public String getTitle() {
		return title;
	}

	public void setThumbnail(final String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
