package it.frigir.msscbeerinventoryservice.services.listeners;

import it.frigir.brewery.model.event.AllocateOrderRequest;
import it.frigir.brewery.model.event.AllocateOrderResult;
import it.frigir.msscbeerinventoryservice.config.JmsConfig;
import it.frigir.msscbeerinventoryservice.services.AllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AllocateBeerOrderListener {

    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void allocateOrder(AllocateOrderRequest allocateOrderRequest) {

        AllocateOrderResult allocateOrderResult = new AllocateOrderResult();
        allocateOrderResult.setBeerOrderDto(allocateOrderRequest.getBeerOrder());

        try {
            allocateOrderResult.setPendingInventory(!allocationService.allocateOrder(allocateOrderRequest.getBeerOrder()));
        } catch (Exception e) {
            log.error("Allocation error " + e.getMessage());
            allocateOrderResult.setAllocationError(true);
        }

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESPONSE_QUEUE, allocateOrderResult);

    }

}
