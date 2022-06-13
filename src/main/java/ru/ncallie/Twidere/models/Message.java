package ru.ncallie.Twidere.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "Сообщение не должно быть пустым")
    @Size(min = 3, max = 1024, message = "Сообщение должно состоять хотя бы из 3 символов")
    private String text;

    @NotEmpty
    @Size(min = 3, max = 20, message = "Тэг должен иметь длину от 3 до 20 символов")
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;
}
