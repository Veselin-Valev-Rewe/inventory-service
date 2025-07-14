package com.example.inventory_service.data.entity;

import com.example.inventory_service.data.enums.MessageStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "producedmessage")
public class ProducedMessage extends BaseEntity {
    private String msgKey;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private String payload;
}
