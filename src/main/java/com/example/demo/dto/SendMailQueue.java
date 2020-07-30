package com.example.demo.dto;

import java.time.LocalDateTime;

import org.seasar.doma.*;

import lombok.Getter;
import lombok.Setter;

@Table(name = "send_mail_queue")
@Entity
@Getter
@Setter
public class SendMailQueue extends DomaDtoImpl {

    private static final long serialVersionUID = -4135869799913706558L;

    @OriginalStates // 差分UPDATEのために定義する
    SendMailQueue originalStates;

    @Id
    @Column(name = "send_mail_queue_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "from_address")
    String from;

    @Column(name = "to_address")
    CommaSeparatedString to;

    @Column(name = "cc_address")
    CommaSeparatedString cc;

    @Column(name = "bcc_address")
    CommaSeparatedString bcc;

    LocalDateTime sentAt;

    String subject;

    String body;
}
