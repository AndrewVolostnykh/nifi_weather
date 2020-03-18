/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package webScraper.processors.utils;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.util.StandardValidators;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tags({"web scraping"})
@CapabilityDescription("This taking json files from specified URL, parsing and giving JSONObject file.")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute="", description="")})
@WritesAttributes({@WritesAttribute(attribute="", description="")})
public class WebScraper extends AbstractProcessor {

    public static final PropertyDescriptor URL_PROPERTY = new PropertyDescriptor
            .Builder().name("URL_PROPERTY")
            .displayName("URL")
            .description("Here must be URL to scrape json ")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

//    public static final Relationship MY_RELATIONSHIP = new Relationship.Builder()
//            .name("MY_RELATIONSHIP")
//            .description("Example relationship")
//            .build();

    public static final Relationship FAIL_RELATIONSHIP = new Relationship.Builder()
            .name("FAIL")
            .description("This url incorrect and we cant take JSON from here")
            .build();

    public static final Relationship SUCCESS_RELATIONSHIP = new Relationship.Builder()
            .name("SUCCESS")
            .description("URL is correct and passed to success")
            .build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
        descriptors.add(URL_PROPERTY);
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<Relationship>();
        relationships.add(SUCCESS_RELATIONSHIP);
        relationships.add(FAIL_RELATIONSHIP);
        this.relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @OnScheduled
    public void onScheduled(final ProcessContext context) {

    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        FlowFile flowFile = session.get();

        if ( flowFile == null ) {
            return;
        }

        String access_url = context.getProperty(URL_PROPERTY).getValue();

        try {
            if (access_url != null) {
                getLogger().info("S: URL not null");


                String resultData = readJsonFromUrl(access_url);
                getLogger().info("Crutch: Got json from web-site " + (resultData.isEmpty() ? " nothing here " : resultData.substring(0, 10))); // crutch that will shows me how it works
                flowFile = session.putAttribute(flowFile, "webscraper.data", resultData); // finally, when next processor will take this String, they must wrap it in JSONObject

                session.transfer(flowFile, SUCCESS_RELATIONSHIP);
                getLogger().info("S: Transfer done");
            } else {
                getLogger().info("F: URL is null");

                return;
            }


        } catch (Exception e)
        {
            getLogger().info("F: Except: " + e.getMessage());

            flowFile = session.putAttribute(flowFile, "webscraper.error", null);
            session.transfer(flowFile, FAIL_RELATIONSHIP);
        }

    }

        private String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

        public String readJsonFromUrl(String url) throws IOException, JSONException {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                return jsonText;
            } finally {
                is.close();
            }
        }

}
