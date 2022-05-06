package com.sequenceiq.consumption.flow.consumption.storage.event;

import com.sequenceiq.cloudbreak.common.event.AcceptResult;
import com.sequenceiq.consumption.flow.consumption.ConsumptionContext;
import com.sequenceiq.flow.reactor.api.event.BaseFlowEvent;

import reactor.rx.Promise;

public class StorageConsumptionCollectionEvent extends BaseFlowEvent {

    private final ConsumptionContext context;

    public StorageConsumptionCollectionEvent(String selector, Long resourceId, String resourceCrn, ConsumptionContext context) {
        super(selector, resourceId, resourceCrn);
        this.context = context;
    }

    public StorageConsumptionCollectionEvent(String selector, Long resourceId, String resourceCrn, ConsumptionContext context, Promise<AcceptResult> accepted) {
        super(selector, resourceId, resourceCrn, accepted);
        this.context = context;
    }

    public ConsumptionContext getContext() {
        return context;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String resourceCrn;

        private String selector;

        private Long resourceId;

        private Promise<AcceptResult> accepted;

        private ConsumptionContext context;

        private Builder() {
        }

        public Builder withSelector(String selector) {
            this.selector = selector;
            return this;
        }

        public Builder withResourceId(Long resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Builder withAccepted(Promise<AcceptResult> accepted) {
            this.accepted = accepted;
            return this;
        }

        public Builder withResourceCrn(String resourceCrn) {
            this.resourceCrn = resourceCrn;
            return this;
        }

        public Builder withConsumptionContext(ConsumptionContext context) {
            this.context = context;
            return this;
        }

        public StorageConsumptionCollectionEvent build() {
            return new StorageConsumptionCollectionEvent(selector, resourceId, resourceCrn, context, accepted);
        }
    }
}
