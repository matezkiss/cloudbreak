package com.sequenceiq.cloudbreak.saas.client.sdx;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudera.thunderhead.service.sdxsvcadmin.SDXSvcAdminGrpc;
import com.cloudera.thunderhead.service.sdxsvcadmin.SDXSvcAdminProto;
import com.google.common.collect.Sets;
import com.sequenceiq.cloudbreak.auth.crn.RegionAwareInternalCrnGeneratorFactory;
import com.sequenceiq.cloudbreak.grpc.altus.AltusMetadataInterceptor;
import com.sequenceiq.cloudbreak.grpc.util.GrpcUtil;

import io.grpc.ManagedChannel;
import io.opentracing.Tracer;

public class SdxSaasClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SdxSaasClient.class);

    private final ManagedChannel channel;

    private final Tracer tracer;

    private final RegionAwareInternalCrnGeneratorFactory regionAwareInternalCrnGeneratorFactory;

    SdxSaasClient(ManagedChannel channel, Tracer tracer,
            RegionAwareInternalCrnGeneratorFactory regionAwareInternalCrnGeneratorFactory) {
        this.channel = checkNotNull(channel, "channel should not be null.");
        this.tracer = tracer;
        this.regionAwareInternalCrnGeneratorFactory = regionAwareInternalCrnGeneratorFactory;
    }

    private SDXSvcAdminGrpc.SDXSvcAdminBlockingStub newStub(String requestId) {
        checkNotNull(requestId, "requestId should not be null.");
        return SDXSvcAdminGrpc.newBlockingStub(channel)
                .withInterceptors(
                        GrpcUtil.getTracingInterceptor(tracer),
                        new AltusMetadataInterceptor(requestId, regionAwareInternalCrnGeneratorFactory.iam().getInternalCrnForServiceAsString()));
    }

    public SDXSvcAdminProto.Instance createInstance(String requestId, String sdxInstanceName, String environmentCrn, String region) {
        checkNotNull(requestId, "requestId should not be null.");
        SDXSvcAdminProto.CreateInstanceRequest request = SDXSvcAdminProto.CreateInstanceRequest.newBuilder()
                .setCloudPlatform(SDXSvcAdminProto.CloudPlatform.Value.AWS)
                .setCloudRegion(region)
                .setEnvironment(environmentCrn)
                .setName(sdxInstanceName)
                .build();
        return newStub(requestId).createInstance(request).getInstance();
    }

    public SDXSvcAdminProto.Instance deleteInstance(String requestId, String sdxInstanceCrn) {
        checkNotNull(requestId, "requestId should not be null.");
        SDXSvcAdminProto.DeleteInstanceRequest request = SDXSvcAdminProto.DeleteInstanceRequest.newBuilder()
                .setInstance(sdxInstanceCrn)
                .build();
        return newStub(requestId).deleteInstance(request).getInstance();
    }

    public Set<SDXSvcAdminProto.Instance> listInstances(String requestId, String accountId) {
        checkNotNull(requestId, "requestId should not be null.");
        // TODO request filter for environment
        SDXSvcAdminProto.ListInstancesRequest.Builder requestBuilder = SDXSvcAdminProto.ListInstancesRequest.newBuilder().setAccountId(accountId);
        SDXSvcAdminGrpc.SDXSvcAdminBlockingStub stub = newStub(requestId);
        SDXSvcAdminProto.ListInstancesResponse listInstancesResponse;
        Set<SDXSvcAdminProto.Instance> instances = Sets.newHashSet();
        do {
            listInstancesResponse = stub.listInstances(requestBuilder.build());
            instances.addAll(listInstancesResponse.getInstancesList());
            requestBuilder.setStartingToken(listInstancesResponse.getNextToken());
        } while (StringUtils.isNotEmpty(listInstancesResponse.getNextToken()));
        return instances;
    }
}
