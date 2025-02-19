package com.balybus.galaxy.global.utils.mail.dto.contents;

public class ContentDto<T> {
    private T content;

    public ContentDto(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
