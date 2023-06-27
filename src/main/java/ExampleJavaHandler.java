import com.here.naksha.lib.core.IEventContext;
import com.here.naksha.lib.core.IEventHandler;
import com.here.naksha.lib.core.models.payload.Event;
import com.here.naksha.lib.core.models.payload.XyzResponse;
import com.here.naksha.lib.core.models.payload.responses.SuccessResponse;
import org.jetbrains.annotations.NotNull;

public class ExampleJavaHandler implements IEventHandler {

  /**
   * The method invoked by the XYZ-Hub directly (embedded) or indirectly, when running in an HTTP vertx or as AWS lambda.
   * This is just an example. Any production code for processing input events into responses should be placed in this method,
   * in a new production handler.
   *
   * @param eventContext the event context to process.
   * @return the response to send.
   */
  @Override
  public @NotNull XyzResponse processEvent(@NotNull IEventContext eventContext) {
    final Event event = eventContext.getEvent();
    return new SuccessResponse().withStatus(String.format("Example success response for event with stream ID %s",event.getStreamId()));
  }
}
