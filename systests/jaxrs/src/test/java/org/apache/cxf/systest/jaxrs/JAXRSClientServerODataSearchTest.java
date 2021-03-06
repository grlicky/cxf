/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.cxf.systest.jaxrs;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.search.odata.ODataParser;
import org.apache.cxf.jaxrs.model.AbstractResourceInfo;
import org.apache.cxf.testutil.common.AbstractBusClientServerTestBase;
import org.junit.BeforeClass;
import org.junit.Test;

public class JAXRSClientServerODataSearchTest extends AbstractBusClientServerTestBase {
    public static final String PORT = BookServer.PORT;

    @BeforeClass
    public static void startServers() throws Exception {
        final Map< String, Object > properties = new HashMap<>();
        properties.put("search.query.parameter.name", "$filter");
        properties.put("search.parser", new ODataParser< Book >(Book.class));

        AbstractResourceInfo.clearAllMaps();
        assertTrue("server did not launch correctly", launchServer(new BookServer(properties)));
        createStaticBus();
    }

    @Test
    public void testSearchBook123WithWebClient() throws Exception {
        String address = "http://localhost:" + PORT + "/bookstore/books/search";

        WebClient client = WebClient.create(address);
        Book b = client.query("$filter", "name eq 'CXF*' and id ge 123 and id lt 124").get(Book.class);
        assertEquals(b.getId(), 123L);

    }
}
