package com.sequenceiq.cloudbreak.reactor.api.event.cluster.upgrade.ccm;

import com.sequenceiq.cloudbreak.reactor.api.event.StackEvent;

public class CcmUnregisterHostsRequest extends StackEvent {
    public CcmUnregisterHostsRequest(Long stackId) {
        super(stackId);
    }
}