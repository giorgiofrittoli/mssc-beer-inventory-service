package it.frigir.msscbeerinventoryservice.services;

import it.frigir.brewery.model.BeerOrderDto;
import it.frigir.msscbeerinventoryservice.domain.BeerInventory;
import it.frigir.msscbeerinventoryservice.repositories.BeerInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeAllocationServiceImpl implements DeAllocationService {

    private final BeerInventoryRepository beerInventoryRepository;

    @Override
    public void deallocateOrder(BeerOrderDto beerOrder) {
        beerOrder.getBeerOrderLines().forEach(beerOrderLineDto -> {

            BeerInventory beerInventory = BeerInventory.builder()
                    .beerId(beerOrderLineDto.getBeerId())
                    .upc(beerOrderLineDto.getUpc())
                    .quantityOnHand(beerOrderLineDto.getOrderQuantity())
                    .build();

            BeerInventory savedBeerInventory = beerInventoryRepository.save(beerInventory);

            log.debug("Saved inv for beer upc " + savedBeerInventory.getUpc() + " inventory id " + savedBeerInventory.getId());
        });
    }
}
