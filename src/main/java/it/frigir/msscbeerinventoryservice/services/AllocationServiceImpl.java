package it.frigir.msscbeerinventoryservice.services;

import it.frigir.brewery.model.BeerOrderDto;
import it.frigir.brewery.model.BeerOrderLineDto;
import it.frigir.msscbeerinventoryservice.domain.BeerInventory;
import it.frigir.msscbeerinventoryservice.repositories.BeerInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class AllocationServiceImpl implements AllocationService {

    private final BeerInventoryRepository beerInventoryRepository;

    @Override
    public Boolean allocateOrder(BeerOrderDto beerOrderDto) {
        log.debug("Allocating order " + beerOrderDto.getId());

        AtomicInteger totalOrdered = new AtomicInteger();
        AtomicInteger totalAllocated = new AtomicInteger();

        beerOrderDto.getBeerOrderLines().forEach(beerOrderLineDto -> {

            if (((beerOrderLineDto.getOrderQuantity() != null ? beerOrderLineDto.getOrderQuantity() : 0)
                    - (beerOrderLineDto.getQuantityAllocated() != null ? beerOrderLineDto.getQuantityAllocated() : 0)) > 0
            ) {
                allocateOrderLine(beerOrderLineDto);
            }

            totalOrdered.addAndGet(beerOrderLineDto.getOrderQuantity());
            totalAllocated.addAndGet(beerOrderLineDto.getQuantityAllocated());

        });

        log.debug("Total ordered " + totalOrdered.get() + " - total allocated " + totalAllocated.get());

        return totalOrdered.get() == totalAllocated.get();

    }

    private void allocateOrderLine(BeerOrderLineDto beerOrderLineDto) {
        List<BeerInventory> beerInventoryList = beerInventoryRepository.findAllByUpc(beerOrderLineDto.getUpc());

        beerInventoryList.forEach(beerInventory -> {
            int inventory = beerInventory.getQuantityOnHand() != null ? beerInventory.getQuantityOnHand() : 0;
            int orderQty = beerOrderLineDto.getOrderQuantity() != null ? beerOrderLineDto.getOrderQuantity() : 0;
            int allocateQty = beerOrderLineDto.getQuantityAllocated() != null ? beerOrderLineDto.getQuantityAllocated() : 0;
            int qtyToAllocate = orderQty - allocateQty;

            if (inventory >= qtyToAllocate) {
                inventory = inventory - qtyToAllocate;
                beerOrderLineDto.setQuantityAllocated(inventory);
                beerInventory.setQuantityOnHand(inventory);
                beerInventoryRepository.save(beerInventory);
            } else if (inventory > 0) {
                beerOrderLineDto.setQuantityAllocated(allocateQty + inventory);
                beerInventory.setQuantityOnHand(0);
            }

            if (beerInventory.getQuantityOnHand() == 0)
                beerInventoryRepository.delete(beerInventory);

        });

    }
}
