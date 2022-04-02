package br.com.saltoleto.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BankTransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String counterPartyId;
    @ManyToOne
    private SuggestionStatus suggestionStatus;
    private LocalDate dateTransfer;

}
