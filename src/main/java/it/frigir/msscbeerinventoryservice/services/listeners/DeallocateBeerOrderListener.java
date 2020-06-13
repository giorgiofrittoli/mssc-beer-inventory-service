package it.frigir.msscbeerinventoryservice.services.listeners;

import it.frigir.brewery.model.event.DeallocateOrderRequest;
import it.frigir.msscbeerinventoryservice.config.JmsConfig;
import it.frigir.msscbeerinventoryservice.services.DeAllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeallocateBeerOrderListener {

    private final DeAllocationService deallocationService;

    @JmsListener(destination = JmsConfig.DEALLOCATE_BEER_ODER_QUEUE)
    public void deallocate(DeallocateOrderRequest deallocateOrderRequest){
        deallocationService.deallocateOrder(deallocateOrderRequest.getBeerOrder());
    }

}
