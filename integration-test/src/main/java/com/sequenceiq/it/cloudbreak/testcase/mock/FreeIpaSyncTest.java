package com.sequenceiq.it.cloudbreak.testcase.mock;

import static java.lang.String.format;

import java.util.Optional;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.cloudera.thunderhead.service.authdistributor.AuthDistributorProto.UserState;
import com.sequenceiq.freeipa.api.v1.freeipa.stack.model.common.Status;
import com.sequenceiq.freeipa.api.v1.operation.model.OperationState;
import com.sequenceiq.it.cloudbreak.AuthDistributorClient;
import com.sequenceiq.it.cloudbreak.actor.CloudbreakActor;
import com.sequenceiq.it.cloudbreak.assertion.Assertion;
import com.sequenceiq.it.cloudbreak.client.FreeIpaTestClient;
import com.sequenceiq.it.cloudbreak.context.Description;
import com.sequenceiq.it.cloudbreak.context.MockedTestContext;
import com.sequenceiq.it.cloudbreak.context.TestContext;
import com.sequenceiq.it.cloudbreak.dto.authdistributor.FetchAuthViewTestDto;
import com.sequenceiq.it.cloudbreak.dto.freeipa.FreeIpaTestDto;
import com.sequenceiq.it.cloudbreak.dto.freeipa.FreeIpaUserSyncTestDto;
import com.sequenceiq.it.cloudbreak.exception.TestFailException;

public class FreeIpaSyncTest extends AbstractMockTest {

    @Inject
    private FreeIpaTestClient freeIpaTestClient;

    @Inject
    private CloudbreakActor cloudbreakActor;

    @Override
    protected void setupTest(TestContext testContext) {
        createDefaultUser(testContext);
        createDefaultCredential(testContext);
        createDefaultEnvironment(testContext);
        createDefaultImageCatalog(testContext);
        initializeDefaultBlueprints(testContext);
    }

    @Test(dataProvider = TEST_CONTEXT_WITH_MOCK)
    @Description(
            given = "environment is present",
            when = "calling a freeipe start",
            then = "freeipa sould be available")
    public void testSyncFreeIpaWithInternalActor(MockedTestContext testContext) {
        FreeIpaUserSyncTestDto freeIpaUserSyncTestDto = testContext
                .given(FreeIpaTestDto.class)
                .when(freeIpaTestClient.create())
                .await(Status.AVAILABLE)
                .given(FreeIpaUserSyncTestDto.class)
                .when(freeIpaTestClient.getLastSyncOperationStatus())
                .await(OperationState.COMPLETED);
        String environmentCrn = freeIpaUserSyncTestDto.getEnvironmentCrn();
        testContext.given(FetchAuthViewTestDto.class)
                .withEnvironmentCrn(environmentCrn)
                .then(validateUpdatedAuthView(true))
                .given(FreeIpaUserSyncTestDto.class)
                .when(freeIpaTestClient.syncAllInternal())
                .await(OperationState.COMPLETED)
                .given(FreeIpaTestDto.class)
                .when(freeIpaTestClient.delete())
                .await(Status.DELETE_COMPLETED)
                .given(FetchAuthViewTestDto.class)
                .withEnvironmentCrn(environmentCrn)
                .then(validateUpdatedAuthView(false))
                .validate();
    }

    public static Assertion<FetchAuthViewTestDto, AuthDistributorClient> validateUpdatedAuthView(boolean exists) {
        return (testContext, fetchAuthViewTestDto, authDistributorClient) -> {
            String environmentCrn = fetchAuthViewTestDto.getRequest().getEnvironmentCrn();
            Optional<UserState> userState = authDistributorClient.getDefaultClient().fetchAuthViewForEnvironment(environmentCrn);

            if (exists && userState.isEmpty()) {
                throw new TestFailException(format("User state is not exists for environment '%s' ", environmentCrn));
            }
            if (!exists && userState.isPresent()) {
                throw new TestFailException(format("User state exists for environment '%s' ", environmentCrn));
            }
            return fetchAuthViewTestDto;
        };
    }
}
