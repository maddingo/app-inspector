package no.maddin.inspector;


import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InspectorDataTest {

    @Test
    public void testParse() {
        InspectData data = InspectData.fromAgentString("62E0E068|org.springframework.context.support.LiveBeansView|MBEAN_DOMAIN_PROPERTY_NAME|138B45AE|java.lang.String");

        assertThat(data, is(notNullValue()));

    }
}
