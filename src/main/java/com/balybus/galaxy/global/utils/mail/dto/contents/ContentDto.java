package com.balybus.galaxy.global.utils.mail.dto.contents;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContentDto<T> {
    private T content;

    public ContentDto(T content) {
        this.content = content;
    }

}
