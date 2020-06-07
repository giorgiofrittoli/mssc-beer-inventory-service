package it.frigir.msscbeerinventoryservice.services;

import it.frigir.brewery.model.event.BeerOrderDto;

public interface AllocationService {
    Boolean allocateOrder(BeerOrderDto beerOrderDto);
}
