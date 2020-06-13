package it.frigir.msscbeerinventoryservice.services;

import it.frigir.brewery.model.BeerOrderDto;

public interface DeAllocationService {
    void deallocateOrder(BeerOrderDto beerOrder);
}
