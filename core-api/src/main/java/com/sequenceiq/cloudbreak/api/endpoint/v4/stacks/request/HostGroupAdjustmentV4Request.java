package com.sequenceiq.cloudbreak.api.endpoint.v4.stacks.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sequenceiq.common.model.JsonEntity;
import com.sequenceiq.cloudbreak.doc.ModelDescriptions.HostGroupAdjustmentModelDescription;
import com.sequenceiq.cloudbreak.doc.ModelDescriptions.HostGroupModelDescription;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class HostGroupAdjustmentV4Request implements JsonEntity {

    @NotNull
    @ApiModelProperty(value = HostGroupModelDescription.HOST_GROUP_NAME, required = true)
    private String hostGroup;

    @NotNull
    @ApiModelProperty(value = HostGroupAdjustmentModelDescription.SCALING_ADJUSTMENT, required = true)
    private Integer scalingAdjustment;

    @ApiModelProperty(HostGroupAdjustmentModelDescription.WITH_STACK_UPDATE)
    private Boolean withStackUpdate = Boolean.FALSE;

    @ApiModelProperty(HostGroupAdjustmentModelDescription.VALIDATE_NODE_COUNT)
    private Boolean validateNodeCount = Boolean.TRUE;

    @ApiModelProperty(HostGroupAdjustmentModelDescription.FORCED)
    private Boolean forced;

    public String getHostGroup() {
        return hostGroup;
    }

    public void setHostGroup(String hostGroup) {
        this.hostGroup = hostGroup;
    }

    public Integer getScalingAdjustment() {
        return scalingAdjustment;
    }

    public void setScalingAdjustment(Integer scalingAdjustment) {
        this.scalingAdjustment = scalingAdjustment;
    }

    public Boolean getWithStackUpdate() {
        return withStackUpdate;
    }

    public void setWithStackUpdate(Boolean withStackUpdate) {
        this.withStackUpdate = withStackUpdate;
    }

    public Boolean getValidateNodeCount() {
        return validateNodeCount;
    }

    public void setValidateNodeCount(Boolean validateNodeCount) {
        this.validateNodeCount = validateNodeCount;
    }

    public void setForced(Boolean forced) {
        this.forced = forced;
    }

    public Boolean getForced() {
        return forced;
    }
}
