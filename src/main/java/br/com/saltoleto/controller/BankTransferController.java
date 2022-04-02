package br.com.saltoleto.controller;


import br.com.saltoleto.service.BankTransferSuggestionManagerProcessorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/bank")
@AllArgsConstructor
public class BankTransferController {

    private BankTransferSuggestionManagerProcessorService bankTransferSuggestionManagerProcessorService;

    @GetMapping
    public ResponseEntity process(@PathParam(value = "counterPartyId") String counterPartyId) {
        bankTransferSuggestionManagerProcessorService.process(counterPartyId);
        return ResponseEntity.ok().build();
    }

}
