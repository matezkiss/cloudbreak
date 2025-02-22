package com.sequenceiq.cloudbreak.reactor.api.event.cluster.upgrade.ccm;

import com.sequenceiq.cloudbreak.reactor.api.event.StackEvent;

public class UpgradeCcmReconfigureNginxResult extends StackEvent {

    public UpgradeCcmReconfigureNginxResult(Long stackId) {
        super(stackId);
    }

    public UpgradeCcmReconfigureNginxResult(String selector, Long stackId) {
        super(selector, stackId);
    }
}
