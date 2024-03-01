package com.magnusr.routes.util;

import com.magnusr.routes.model.CatFact;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpUtilsTest {

    @Test
    void testGetRequestWithHttpClient()  {
        var url = "https://catfact.ninja/fact";
        var exceptionWasCaught = false;

        CatFact cf = null;
        try {
            cf = HttpUtils
                    .createHttpClient()
                    .withGetRequest(url, null)
                    .execute(CatFact.class);
        } catch (IOException | InterruptedException e) {
            exceptionWasCaught = true;
        }

        if (!exceptionWasCaught) {
            assertThat(cf).isNotNull();
            assertThat(cf.length).isGreaterThan(0);
        }
    }

}
