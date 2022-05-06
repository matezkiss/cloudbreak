package com.sequenceiq.consumption.flow.consumption;

import com.sequenceiq.cloudbreak.cloud.model.CloudCredential;
import com.sequenceiq.consumption.domain.Consumption;
import com.sequenceiq.consumption.dto.Credential;
import com.sequenceiq.flow.core.CommonContext;
import com.sequenceiq.flow.core.FlowParameters;

public class ConsumptionContext extends CommonContext {

    private final Consumption consumption;

    private final Credential credential;

    private final CloudCredential cloudCredential;

    public ConsumptionContext(FlowParameters flowParameters, Consumption consumption, Credential credential, CloudCredential cloudCredential) {
        super(flowParameters);
        this.consumption = consumption;
        this.credential = credential;
        this.cloudCredential = cloudCredential;
    }

    public Consumption getConsumption() {
        return consumption;
    }

    public Credential getCredential() {
        return credential;
    }

    public CloudCredential getCloudCredential() {
        return cloudCredential;
    }
}
