/*
 * Copyright (C) 2017-2023 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * License-Filename: LICENSE
 */
package com.here.naksha.handler.examples;

import com.here.naksha.lib.core.IEventContext;
import com.here.naksha.lib.core.IEventHandler;
import com.here.naksha.lib.core.models.features.Connector;
import com.here.naksha.lib.core.models.payload.Event;
import com.here.naksha.lib.core.models.payload.XyzResponse;
import com.here.naksha.lib.core.models.payload.responses.SuccessResponse;
import org.jetbrains.annotations.NotNull;

public class ExampleJavaHandler implements IEventHandler {

  /**
   * Just a blank constructor, but currently every handler need one, for the main Naksha to invoke.
   */
  public ExampleJavaHandler(Connector connector) {}

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
    return new SuccessResponse()
        .withStatus(String.format("Example success response for event with stream ID %s", event.getStreamId()));
  }
}
