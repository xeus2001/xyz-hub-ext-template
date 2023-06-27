package com.here.naksha.handler.examples

import com.here.naksha.lib.core.IEventContext
import com.here.naksha.lib.core.IEventHandler
import com.here.naksha.lib.core.models.features.Connector
import com.here.naksha.lib.core.models.payload.Event
import com.here.naksha.lib.core.models.payload.XyzResponse
import com.here.naksha.lib.core.models.payload.responses.SuccessResponse
import org.jetbrains.annotations.NotNull

/**
 * Just a blank constructor, but currently every handler need one, for the main Naksha to invoke.
 */
class ExampleKotlinHandler(connector : Connector) : IEventHandler {

    init {}

    /**
     * The method invoked by the XYZ-Hub directly (embedded) or indirectly, when running in an HTTP vertx or as AWS lambda.
     * This is just an example. Any production code for processing input events into responses should be placed in this method,
     * in a new production handler.
     *
     * @param eventContext the event context to process.
     * @return the response to send.
     */
    override fun processEvent(eventContext: IEventContext): @NotNull XyzResponse {
        val event: Event = eventContext.event
        return SuccessResponse().withStatus("Example success response for event with stream ID ${event.streamId}")
    }
}
