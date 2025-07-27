package com.github.hjgf0624.sideproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "message_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageCategoryEntity {

    @EmbeddedId
    private MessageCategoryId id;

    @ManyToOne
    @MapsId("messageId")
    @JoinColumn(name = "message_id", referencedColumnName = "message_id", insertable = false, updatable = false)
    private MessageEntity message;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private CategoryEntity category;

}
