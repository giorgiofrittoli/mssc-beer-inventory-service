package it.frigir.msscbeerinventoryservice.services;

import it.frigir.brewery.model.BeerDto;
import it.frigir.brewery.model.event.NewInventoryEvent;
import it.frigir.msscbeerinventoryservice.config.JmsConfig;
import it.frigir.msscbeerinventoryservice.domain.BeerInventory;
import it.frigir.msscbeerinventoryservice.repositories.BeerInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NewInventoryListener {

    private final BeerInventoryRepository beerInventoryRepository;

    @JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE)
    public void lister(NewInventoryEvent newInventoryEvent) {

        log.debug("Got new inventory request");

        BeerDto beerDto = newInventoryEvent.getBeerDto();

        BeerInventory beerInventory = BeerInventory.builder()
                .upc(beerDto.getUpc())
                .quantityOnHand(beerDto.getQuantityOnHand())
                .beerId(beerDto.getId())
                .build();

        beerInventoryRepository.save(beerInventory);

    }
}
